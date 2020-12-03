package com.fury.game.container.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.types.StackContainer;
import com.fury.game.entity.character.player.content.BankPin;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class Trade extends StackContainer {

    public static final int INTERFACE_ID = 3322;
    public static final int INTERFACE_REMOVAL_ID = 3415;

    public Trade(Player player) {
        super(player, 28);
    }

    @Override
    public void refresh() {
        Player partner = GameWorld.getPlayers().get(getTradeWith());
        if(player == null || partner == null || getTradeWith() == player.getIndex() || player.isBanking())
            return;
        player.getPacketSender().sendItemContainer(this, 3415);
        player.getPacketSender().sendItemContainer(partner.getTrade(), 3416);

        partner.getPacketSender().sendItemContainer(partner.getTrade(), 3415);
        partner.getPacketSender().sendItemContainer(this, 3416);

        sendText(partner);
    }

    private boolean inTrade = false;
    private boolean tradeRequested = false;
    private int tradeWith = -1;
    private int tradeStatus;
    public long lastTradeSent, lastAction;
    private boolean canOffer = true;
    public boolean tradeConfirmed = false;
    public boolean tradeConfirmed2 = false;
    public boolean acceptedTrade = false;
    public boolean goodTrade = false;

    public void setTrading(boolean trading) {
        this.inTrade = trading;
    }

    public boolean inTrade() {
        return this.inTrade;
    }

    public void setTradeRequested(boolean tradeRequested) {
        this.tradeRequested = tradeRequested;
    }

    public boolean tradeRequested() {
        return this.tradeRequested;
    }

    public void setTradeWith(int tradeWith) {
        this.tradeWith = tradeWith;
    }

    public int getTradeWith() {
        return this.tradeWith;
    }

    public void setTradeStatus(int status) {
        this.tradeStatus = status;
    }

    public int getTradeStatus() {
        return this.tradeStatus;
    }

    public void setCanOffer(boolean canOffer) {
        this.canOffer = canOffer;
    }

    public boolean getCanOffer() {
        return canOffer && player.getInterfaceId() == 3323 && !player.isBanking() && !player.getPriceChecker().isOpen();
    }

    public int inTradeWith = -1;


    public void requestTrade(Player partner) {
        if(player == null || partner == null || player.isDead() || partner.isDead() || player.getMovement().getTeleporting() || partner.getMovement().getTeleporting())
            return;

        if(player.getGameMode().isIronMan()) {
            player.message("Ironman players are not allowed to trade.");
            return;
        }
        if(partner.getGameMode().isIronMan()) {
            player.message("That player is a ironman player and therefore can not trade.");
            return;
        }

        if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
            BankPin.init(player);
            return;
        }

        if(System.currentTimeMillis() - lastTradeSent < 5000 && !inTrade()) {
            player.message("You're sending trade requests too frequently. Please slow down.");
            return;
        }
        if(!player.getControllerManager().canTrade()) {
            player.message("You are far too busy to trade at the moment!");
            return;
        }

        if(inTrade()) {
            declineTrade(true);
            return;
        }

        if(player.isShopping() || player.isBanking()) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        if(player.busy()) {
            return;
        }
        if(partner.busy() || partner.getInterfaceId() > 0 || partner.getTrade().inTrade() || partner.isBanking() || partner.isShopping()/* || player2.getDueling().inDuelScreen*/) {
            player.message("The other player is currently busy.");
            return;
        }
        if(player.getInterfaceId() > 0 || inTrade() || player.isBanking() || player.isShopping()/* || player.getDueling().inDuelScreen*/) {
            player.message("You are currently unable to trade another player.");
            if(player.getInterfaceId() > 0)
                player.message("Please close all open interfaces before requesting to open another one.");
            return;
        }
        tradeWith = partner.getIndex();
        if(getTradeWith() == player.getIndex())
            return;

        if(!player.isWithinDistance(partner, 2)) {
            player.message("Please get closer to request a trade.");
            return;
        }
        if(!inTrade() && partner.getTrade().tradeRequested() && partner.getTrade().getTradeWith() == player.getIndex()) {
            openTrade();
            partner.getTrade().openTrade();
        } else if(!inTrade()) {
            setTradeRequested(true);
            player.message("Sending trade offer...");
            partner.getPacketSender().sendTradeRequestMessage(player);
        }
        lastTradeSent = System.currentTimeMillis();
    }

    public void openTrade() {
        player.getPacketSender().sendClientRightClickRemoval();
        Player player2 = GameWorld.getPlayers().get(getTradeWith());
        if(player == null || player2 == null || getTradeWith() == player.getIndex() || player.isBanking())
            return;
        setTrading(true);
        setTradeRequested(false);
        setCanOffer(true);
        setTradeStatus(1);
        refresh();
        player.getInventory().refresh();
        player.getMovement().reset();
        inTradeWith = player2.getIndex();
    }

    public void sendText(Player partner) {
        if(partner == null)
            return;
        partner.getPacketSender().sendString(3451, "" + Misc.formatName(player.getUsername()) + "");
        partner.getPacketSender().sendString(3417, "Trading with: " + Misc.formatName(player.getUsername()) + "");
        partner.getPacketSender().sendString(53505, player.getUsername() + "\\nhas " + player.getInventory().getSpaces() + " free\\n inventory slots.");
        player.getPacketSender().sendString(53505, partner.getUsername() + "\\nhas " + partner.getInventory().getSpaces() + " free\\n inventory slots.");
        player.getPacketSender().sendString(3451, "" + Misc.formatName(partner.getUsername()) + "");
        player.getPacketSender().sendString(3417, "Trading with: " + Misc.formatName(partner.getUsername()) + "");
        player.getPacketSender().sendString(3431, "");
        player.getPacketSender().sendString(3535, "Are you sure you want to make this trade?");
        player.getPacketSender().sendInterfaceSet(3323, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    public void declineTrade(boolean tellOther) {
        Player player2 = getTradeWith() >= 0 && !(getTradeWith() > GameWorld.getPlayers().capacity()) ? GameWorld.getPlayers().get(getTradeWith()) : null;
        for (Item item : getItems()) {
            if (item == null || item.getAmount() < 1)
                continue;
            player.getInventory().add(item);
        }
        clear();
        if(tellOther && getTradeWith() > -1) {
            if(player2 == null)
                return;
            player2.getTrade().declineTrade(false);
            player2.message("Other player declined the trade.");
        }
        resetTrade();
    }

    public void resetTrade() {
        inTradeWith = -1;
        clear();
        setCanOffer(true);
        setTrading(false);
        setTradeWith(-1);
        setTradeStatus(0);
        lastTradeSent = 0;
        acceptedTrade = false;
        tradeConfirmed = false;
        tradeConfirmed2 = false;
        tradeRequested = false;
        canOffer = true;
        goodTrade = false;
        player.getPacketSender().sendString(3535, "Are you sure you want to make this trade?");
        player.getPacketSender().sendInterfaceRemoval();
    }

    public void tradeItem() {

        Player partner = GameWorld.getPlayers().get(getTradeWith());
        if(partner == null || player == null)
            return;

        player.getTrade().acceptedTrade = false;
        player.getTrade().tradeConfirmed = false;
        player.getTrade().tradeConfirmed2 = false;
        partner.getTrade().acceptedTrade = false;
        partner.getTrade().tradeConfirmed = false;
        partner.getTrade().tradeConfirmed2 = false;
    }

    public void acceptTrade(int stage) {
        if(!player.getTimers().getClickDelay().elapsed(1000))
            return;

        if(getTradeWith() < 0) {
            declineTrade(false);
            return;
        }

        Player partner = GameWorld.getPlayers().get(getTradeWith());
        if(player == null || partner == null) {
            declineTrade(false);
            return;
        }

        if(player.getInventory().getSpaces() < partner.getTrade().getItemTotal()) {
            player.getInventory().full();
            return;
        }
        if(partner.getInventory().getSpaces() < player.getTrade().getItemTotal()) {
            player.message(partner.getUsername() + " does not have enough inventory space.");
            return;
        }
        if(!twoTraders(player, partner)) {
            player.message("An error has occurred. Please try re-trading the player.");
            return;
        }

        if(stage == 2) {
            if(!inTrade() || !partner.getTrade().inTrade() || !partner.getTrade().tradeConfirmed) {
                declineTrade(true);
                return;
            }
            if(!tradeConfirmed)
                return;
            acceptedTrade = true;
            tradeConfirmed2 = true;
            partner.getPacketSender().sendString(3535, "Other player has accepted.");
            player.getPacketSender().sendString(3535, "Waiting for other player...");
            if (inTrade() && partner.getTrade().tradeConfirmed2) {
                acceptedTrade = true;
                giveItems();
                player.message("Trade accepted.");
                partner.getTrade().acceptedTrade = true;
                partner.getTrade().giveItems();
                partner.message("Trade accepted.");
                resetTrade();
                partner.getTrade().resetTrade();
            }
        } else if(stage == 1) {
            partner.getTrade().goodTrade = true;
            partner.getPacketSender().sendString(3431, "Other player has accepted.");
            goodTrade = true;
            player.getPacketSender().sendString(3431, "Waiting for other player...");
            tradeConfirmed = true;
            if (inTrade() && partner.getTrade().tradeConfirmed && partner.getTrade().goodTrade && goodTrade) {
                confirmScreen();
                partner.getTrade().confirmScreen();
            }
        }
        player.getTimers().getClickDelay().reset();
    }

    public void confirmScreen() {
        Player partner = GameWorld.getPlayers().get(getTradeWith());
        if (partner == null)
            return;
        setCanOffer(false);
        player.getInventory().refresh();
        String trade = "Absolutely nothing!";
        String send;
        int count = 0;
        for (Item item : getItems()) {
            if(item == null)
                continue;

            if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
                send = FontUtils.CYAN + (item.getAmount() / 1000) + "K " + FontUtils.WHITE + "(" + Misc.format(item.getAmount()) + ")";
            } else if (item.getAmount() >= 1000000) {
                send = FontUtils.GREEN + (item.getAmount() / 1000000) + " million " + FontUtils.WHITE + "(" + Misc.format(item.getAmount()) + ")";
            } else {
                send = "" + Misc.format(item.getAmount());
            }
            if (count == 0) {
                trade = item.getName().replaceAll("_", " ");
            } else
                trade = trade + "\\n" + item.getName().replaceAll("_", " ");
            if (item.getDefinition().isStackable())
                trade = trade + " x " + send;
            count++;
        }

        player.getPacketSender().sendString(3557, trade);
        trade = "Absolutely nothing!";
        send = "";
        count = 0;
        for (Item item : partner.getTrade().getItems()) {

            if(item == null)
                continue;

            if (item.getAmount() >= 1000 && item.getAmount() < 1000000) {
                send = FontUtils.CYAN + (item.getAmount() / 1000) + "K " + FontUtils.WHITE + "(" + Misc.format(item.getAmount()) + ")";
            } else if (item.getAmount() >= 1000000) {
                send = FontUtils.GREEN + (item.getAmount() / 1000000) + " million " + FontUtils.WHITE + "(" + Misc.format(item.getAmount()) + ")";
            } else {
                send = "" + Misc.format(item.getAmount());
            }
            if (count == 0) {
                trade = item.getName().replaceAll("_", " ");
            } else
                trade = trade + "\\n" + item.getName().replaceAll("_", " ");
            if (item.getDefinition().isStackable())
                trade = trade + " x " + send;
            count++;
        }

        player.getPacketSender().sendString(3558, trade);
        player.getPacketSender().sendInterfaceSet(3443, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    public void giveItems() {
        Player partner = GameWorld.getPlayers().get(getTradeWith());
        if (partner == null)
            return;
        if(!inTrade() || !partner.getTrade().inTrade())
            return;
        try {
            for (Item item : partner.getTrade().getItems()) {
                if(item != null)
                    player.getInventory().add(item);
            }

            //logs
            for (Item item : player.getTrade().getItems()) {
                if(item != null)
                    player.getLogger().addTradeItem(item, partner, false);
            }
            for (Item item : partner.getTrade().getItems()) {
                if(item != null)
                    player.getLogger().addTradeItem(item, partner, true);
            }
        } catch (Exception ignored) {
        }
    }

    public static boolean twoTraders(Player p1, Player p2) {
        int count = 0;
        for(Player player : GameWorld.getPlayers()) {
            if(player == null)
                continue;

            if(player.getTrade().inTradeWith == p1.getIndex() || player.getTrade().inTradeWith == p2.getIndex())
                count++;
        }
        return count == 2;
    }
}
