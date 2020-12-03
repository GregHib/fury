package com.fury.game.content.global.minigames.impl;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.core.task.Task;
import com.fury.game.container.impl.Inventory;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.content.PlayerInteractingOption;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.Flag;
import com.fury.util.FontUtils;
import com.fury.util.Colours;
import com.fury.util.Misc;

import java.util.concurrent.CopyOnWriteArrayList;

public class OldDueling {

    Player player;

    public OldDueling(Player player) {
        this.player = player;
    }

    /*public void challengePlayer(Player playerToDuel) {
        if (player.getLocation() != Location.DUEL_ARENA)
            return;
        if (player.getInterfaceId() > 0) {
            player.getPacketSender().sendMessage("Please close the interface you have open before trying to open a new one.");
            return;
        }
        if (!Locations.goodDistance(player.getX(), player.getY(), playerToDuel.getX(), playerToDuel.getY(), 2)) {
            player.getPacketSender().sendMessage("Please get closer to request a duel.");
            return;
        }
        if (!checkDuel(player, 0)) {
            player.getPacketSender().sendMessage("Unable to request duel. Please try logging out and then logging back in.");
            return;
        }
        if (!checkDuel(playerToDuel, 0) || playerToDuel.getInterfaceId() > 0) {
            player.getPacketSender().sendMessage("The other player is currently busy.");
            return;
        }
        if (player.getDueling().duelingStatus == 5) {
            player.getPacketSender().sendMessage("You can only challenge people outside the arena.");
            return;
        }
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
            BankPin.init(player, false);
            return;
        }

        if (player.getFamiliar() != null) {
            player.getPacketSender().sendMessage("You must dismiss your familiar before being allowed to start a duel.");
            return;
        }

        if (inDuelScreen)
            return;
        if (player.getTrade().inTrade())
            player.getTrade().declineTrade(true);
        duelingWith = playerToDuel.getIndex();
        if (duelingWith == player.getIndex())
            return;
        duelRequested = true;
        boolean challenged = playerToDuel.getDueling().duelingStatus == 0 && duelRequested || playerToDuel.getDueling().duelRequested;
        if (duelingStatus == 0 && challenged && duelingWith == playerToDuel.getIndex() && playerToDuel.getDueling().duelingWith == player.getIndex()) {
            if (duelingStatus == 0) {
                openDuel();
                playerToDuel.getDueling().openDuel();
            } else {
                player.getPacketSender().sendMessage("You must decline this duel before accepting another one!");
            }
        } else if (duelingStatus == 0) {
            playerToDuel.getPacketSender().sendDuelChallengeRequestMessage(player, false);
            player.getPacketSender().sendMessage("You have sent a duel request to " + playerToDuel.getUsername() + ".");
        }
    }*/

    /*public void openDuel() {
        Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;
        player.getPacketSender().sendClientRightClickRemoval();
        inDuelWith = playerToDuel.getIndex();
        stakedItems.clear();
        inDuelScreen = true;
        duelingStatus = 1;
        if (!checkDuel(player, 1))
            return;
        for (int i = 0; i < selectedDuelRules.length; i++)
            selectedDuelRules[i] = false;
        for (int i = 0; i < selectedDuelRules.length; i++)
            tempRules[i] = true;
        sendDuelConfig(player, new boolean[22]);
        //player.getPacketSender().sendConfig(286, 0);
        player.getTrade().setCanOffer(true);
        player.getPacketSender().sendDuelEquipment();
        player.getPacketSender().sendString(6671, "Opponent: " + playerToDuel.getUsername());
        String col = FontUtils.ORANGE_2;
        if (player.getSkills().getCombatLevel() - playerToDuel.getSkills().getCombatLevel() >= 10)
            col = FontUtils.GREEN_1;
        player.getPacketSender().sendString(6628, "Opponent's Level: " + col + playerToDuel.getSkills().getCombatLevel());
        player.getPacketSender().sendString(6684, "").sendString(669, "Lock Weapon").sendString(8278, "Neither player is allowed to change weapon.");
        player.getPacketSender().sendInterfaceSet(6575, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
        player.getPacketSender().sendInterfaceItems(6670, playerToDuel.getDueling().stakedItems);
        player.getPacketSender().sendInterfaceItems(6669, player.getDueling().stakedItems);
        canOffer = true;
    }*/

    public void declineDuel(boolean tellOther) {
        Player playerToDuel = duelingWith >= 0 ? GameWorld.getPlayers().get(duelingWith) : null;
        if (tellOther) {
            if (playerToDuel == null)
                return;
            if (playerToDuel == null || playerToDuel.getDueling().duelingStatus == 6) {
                return;
            }
            playerToDuel.getDueling().declineDuel(false);
        }
        for (Item item : stakedItems) {
            if (item.getAmount() < 1)
                continue;
            player.getInventory().add(item);
        }
        reset();
        player.getPacketSender().sendInterfaceRemoval();
    }

    public void resetAcceptedStake() {
        Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;
        if (player.getDueling().duelingStatus == 2 || playerToDuel.getDueling().duelingStatus == 2) {
            player.getDueling().duelingStatus = 1;
            player.getPacketSender().sendString(6684, "");
            playerToDuel.getPacketSender().sendString(6684, "");
            playerToDuel.getDueling().duelingStatus = 1;
        }
    }

    /*public void stakeItem(int itemId, int amount, int slot) {
        if (slot < 0)
            return;

        if (!getCanOffer())
            return;

        resetAcceptedStake();

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item toStake = new Item(itemId);

        if (!player.getInventory().contains(toStake) || !inDuelScreen)
            return;

        if (!toStake.tradeable()) {
            player.message("That item is not tradeable.");
            return;
        }

        Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;

        if (player.getGameMode().isIronMan()) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to stake.");
            return;
        }

        if (playerToDuel.getGameMode().isIronMan()) {
            player.getPacketSender().sendMessage("That player is a ironman player and therefore can not stake.");
            return;
        }

        if (!checkDuel(player, 1) || !checkDuel(playerToDuel, 1) || slot >= player.getInventory().capacity() || !player.getInventory().get(slot).isEqual(toStake) || player.getInventory().get(slot).getAmount() <= 0) {
            declineDuel(false);
            playerToDuel.getDueling().declineDuel(false);
            return;
        }
        if (player.getInventory().getAmount(toStake) < amount) {
            amount = player.getInventory().getAmount(toStake);
            if (amount == 0 || player.getInventory().getAmount(toStake) < amount) {
                return;
            }
        }
        if (!Loader.getItem(itemId).isStackable()) {
            for (int a = 0; a < amount; a++) {
                if (player.getInventory().contains(toStake)) {
                    stakedItems.add(toStake);
                    player.getInventory().delete(toStake);
                }
            }
        } else {
            if (amount <= 0 || player.getInventory().get(slot).getAmount() <= 0)
                return;
            boolean itemInScreen = false;
            for (Item item : stakedItems) {
                if (item.isEqual(toStake)) {
                    itemInScreen = true;
                    item.setAmount(item.getAmount() + amount);
                    player.getInventory().delete(new Item(toStake, amount));
                    break;
                }
            }
            if (!itemInScreen) {
                player.getInventory().delete(new Item(toStake, amount));
                stakedItems.add(new Item(toStake, amount));
            }
        }
        CopyOnWriteArrayList<Item> sta = player.getDueling().stakedItems;
        for (int i = 0; i < sta.size(); i++) {
            player.getPacketSender().sendItemOnInterface(6639 + i, sta.get(i).getId(), sta.get(i).getAmount());

        }
        player.getPacketSender().sendClientRightClickRemoval();

        player.getPacketSender().sendInterfaceItems(6670, playerToDuel.getDueling().stakedItems);
        player.getPacketSender().sendInterfaceItems(6669, player.getDueling().stakedItems);
        playerToDuel.getPacketSender().sendInterfaceItems(6670, player.getDueling().stakedItems);
        player.getPacketSender().sendString(6684, "");
        playerToDuel.getPacketSender().sendString(6684, "");
        duelingStatus = 1;
        playerToDuel.getDueling().duelingStatus = 1;
        player.getInventory().refresh();
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    public void removeStakedItem(int itemId, int amount) {
        if (!inDuelScreen || !getCanOffer())
            return;
        Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;
        resetAcceptedStake();
        if (!checkDuel(player, 1) || !checkDuel(playerToDuel, 1)) {
            declineDuel(false);
            playerToDuel.getDueling().declineDuel(false);
            return;
        }
        *//*
	        if (Item.itemStackable[itemID]) {
	            if (playerToDuel.getInventory().getFreeSlots() - 1 < (c.duelSpaceReq)) {
	                c.sendMessage("You have too many rules set to remove that item.");
	                return false;
	            }
	        }*//*
        player.getPacketSender().sendClientRightClickRemoval();
        if (!Loader.getItem(itemId).isStackable()) {
            if (amount > 28)
                amount = 28;
            for (int a = 0; a < amount; a++) {
                for (Item item : stakedItems) {
                    if (item.getId() == itemId) {
                        if (!item.getDefinition().isStackable()) {
                            if (!checkDuel(player, 1) || !checkDuel(playerToDuel, 1)) {
                                declineDuel(false);
                                playerToDuel.getDueling().declineDuel(false);
                                return;
                            }
                            stakedItems.remove(item);
                            player.getInventory().add(item);
                        }
                        break;
                    }
                }
            }
        } else
            for (Item item : stakedItems) {
                if (item.getId() == itemId) {
                    if (item.getDefinition().isStackable()) {
                        if (item.getAmount() > amount) {
                            item.setAmount(item.getAmount() - amount);
                            player.getInventory().add(new Item(itemId, amount));
                        } else {
                            amount = item.getAmount();
                            stakedItems.remove(item);
                            player.getInventory().add(new Item(item, amount));
                        }
                    }
                    break;
                }
            }
        player.getPacketSender().sendInterfaceItems(6670, playerToDuel.getDueling().stakedItems);
        player.getPacketSender().sendInterfaceItems(6669, player.getDueling().stakedItems);
        playerToDuel.getPacketSender().sendInterfaceItems(6670, player.getDueling().stakedItems);
        playerToDuel.getPacketSender().sendString(6684, "");
        player.getPacketSender().sendString(6684, "");
        duelingStatus = 1;
        playerToDuel.getDueling().duelingStatus = 1;
        player.getInventory().refresh();
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }*/

    public void selectRule(DuelRule duelRule) {
        System.out.println("Rule: " + duelingWith);
        if (duelingWith < 0)
            return;
        final Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;
        System.out.println(player.getInterfaceId());
        if (player.getInterfaceId() != 6575)
            return;
        int index = duelRule.ordinal();
        if (duelRule == DuelRule.LOCK_WEAPON) {
            if (player.getEquipment().get(Slot.WEAPON).getId() != playerToDuel.getEquipment().get(Slot.WEAPON).getId()) {
                player.getPacketSender().sendMessage("This rule requires you and your duel partner to have the same weapon equipped.", Colours.RED);
                player.getPacketSender().sendButtonToggle(6734 + index, false);
                return;
            }
        }
        boolean alreadySet = selectedDuelRules[duelRule.ordinal()];
        boolean slotOccupied = duelRule.getEquipmentSlot() > 0 && player.getEquipment().get(duelRule.getEquipmentSlot()).getId() > 0;
        boolean oSlotOccupied = duelRule.getEquipmentSlot() > 0 && playerToDuel.getEquipment().get(duelRule.getEquipmentSlot()).getId() > 0;
        if (duelRule == DuelRule.NO_SHIELD) {
            if (player.getEquipment().get(Slot.WEAPON).getId() > 0 && Equipment.isTwoHandedWeapon(player.getEquipment().get(Slot.WEAPON)))
                slotOccupied = true;
            if (Equipment.isTwoHandedWeapon(playerToDuel.getEquipment().get(Slot.WEAPON)))
                oSlotOccupied = true;
        }
        int spaceRequired = slotOccupied ? duelRule.getInventorySpaceReq() : 0;
        int oSpaceRequired = oSlotOccupied ? duelRule.getInventorySpaceReq() : 0;
        for (int i = 10; i < this.selectedDuelRules.length; i++) {
            if (selectedDuelRules[i]) {
                DuelRule rule = DuelRule.forId(i);
                if (rule.getEquipmentSlot() > 0) {
                    if (player.getEquipment().get(rule.getEquipmentSlot()).getId() > 0)
                        spaceRequired += rule.getInventorySpaceReq();
                    if (playerToDuel.getEquipment().get(rule.getEquipmentSlot()).getId() > 0)
                        oSpaceRequired += rule.getInventorySpaceReq();
                }
            }
        }
        if (!alreadySet && player.getInventory().getSpaces() < spaceRequired) {
            player.getPacketSender().sendButtonToggle(6734 + index, false);
            player.message("You do not have enough free inventory space to set this rule.");
            return;
        }
        if (!alreadySet && playerToDuel.getEquipment().getSpaces() < oSpaceRequired) {
            player.getPacketSender().sendButtonToggle(6734 + index, false);
            player.message("" + playerToDuel.getUsername() + " does not have enough inventory space for this rule to be set.");
            return;
        }
        if (!player.getDueling().selectedDuelRules[index]) {
            player.getDueling().selectedDuelRules[index] = true;

            player.getDueling().duelConfig += duelRule.getConfigId();
        } else {
            player.getDueling().selectedDuelRules[index] = false;
            player.getDueling().duelConfig -= duelRule.getConfigId();
        }
        //sendDuelConfig(player, player.getDueling().selectedDuelRules);
        //sendDuelConfig(playerToDuel, player.getDueling().selectedDuelRules);
        //player.getPacketSender().sendConfig(286, player.getDueling().duelConfig);
        player.getPacketSender().sendButtonToggle(6734 + index, player.getDueling().selectedDuelRules[index]);
        playerToDuel.getPacketSender().sendButtonToggle(6734 + index, player.getDueling().selectedDuelRules[index]);
        playerToDuel.getDueling().duelConfig = player.getDueling().duelConfig;
        playerToDuel.getDueling().selectedDuelRules[index] = player.getDueling().selectedDuelRules[index];
        //playerToDuel.getPacketSender().sendConfig(286, playerToDuel.getDueling().duelConfig);
        player.getPacketSender().sendString(6684, "");
        resetAcceptedStake();
        if (selectedDuelRules[DuelRule.OBSTACLES.ordinal()]) {
            if (selectedDuelRules[DuelRule.NO_MOVEMENT.ordinal()]) {
                Position duelTele = new Position(3366 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0);
                player.getDueling().duelTelePos = duelTele;
                playerToDuel.getDueling().duelTelePos = player.getDueling().duelTelePos.copyPosition();
                playerToDuel.getDueling().duelTelePos.setX(player.getDueling().duelTelePos.getX() - 1);
            }
        } else {
            if (selectedDuelRules[DuelRule.NO_MOVEMENT.ordinal()]) {
                Position duelTele = new Position(3335 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0);
                player.getDueling().duelTelePos = duelTele;
                playerToDuel.getDueling().duelTelePos = player.getDueling().duelTelePos.copyPosition();
                playerToDuel.getDueling().duelTelePos.setX(player.getDueling().duelTelePos.getX() - 1);
            }
        }

        if (duelRule == DuelRule.LOCK_WEAPON && selectedDuelRules[duelRule.ordinal()]) {
            player.getPacketSender().sendMessage("Warning! The rule 'Lock Weapon' has been enabled. You will not be able to change", Colours.RED).sendMessage("weapon during the duel!", Colours.RED);
            playerToDuel.getPacketSender().sendMessage("Warning! The rule 'Lock Weapon' has been enabled. You will not be able to change", Colours.RED).sendMessage("weapon during the duel!", Colours.RED);
        }
    }

    public static void sendDuelConfig(Player p, boolean[] rules) {
        for (int i = 0; i < rules.length; i++) {
            p.getPacketSender().sendButtonToggle(6734 + i, rules[i]);
        }
    }

    /**
     * Checks if two players are the only ones in a duel.
     *
     * @param p1 Player1 to check if he's 1/2 player in trade.
     * @param p2 Player2 to check if he's 2/2 player in trade.
     * @return true if only two people are in the duel.
     */
    public static boolean twoDuelers(Player p1, Player p2) {
        int count = 0;
        for (Player player : GameWorld.getPlayers()) {
            if (player == null)
                continue;
            if (player.getDueling().inDuelWith == p1.getIndex() || player.getDueling().inDuelWith == p2.getIndex()) {
                count++;
            }
        }
        return count == 2;
    }

    public void confirmDuel() {
        final Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null)
            return;
        else {
            if (!twoDuelers(player, playerToDuel)) {
                player.message("An error has occurred. Please try requesting a new duel.");
                return;
            }
        }
        String itemId = "";
        for (Item item : player.getDueling().stakedItems) {
            ItemDefinition def = item.getDefinition();
            if (def.isStackable()) {
                String col = FontUtils.YELLOW;
                if (item.getId() == 995)
                    col = (item.getAmount() >= 10000000) ? FontUtils.GREEN_3 : FontUtils.WHITE;
                itemId += def.getName() + " " + FontUtils.WHITE + "x " + col + Misc.format(item.getAmount()) + "\\n";
            } else {
                itemId += def.getName() + "\\n";
            }
        }
        player.getPacketSender().sendString(6516, itemId);
        itemId = "";
        for (Item item : playerToDuel.getDueling().stakedItems) {
            ItemDefinition def = item.getDefinition();
            if (def.isStackable()) {
                String col = FontUtils.YELLOW;
                if (item.getId() == 995)
                    col = (item.getAmount() >= 10000000) ? FontUtils.GREEN_3 : FontUtils.WHITE;
                itemId += def.getName() + " " + FontUtils.WHITE + "x " + col + Misc.format(item.getAmount()) + "\\n";
            } else {
                itemId += def.getName() + "\\n";
            }
        }
        canOffer = false;
        player.getPacketSender().sendString(6517, itemId);
        player.getPacketSender().sendString(8242, "");
        for (int i = 8238; i <= 8253; i++)
            player.getPacketSender().sendString(i, "");
        //player.getPacketSender().sendString(8250, "Hitpoints will be restored.");
        player.getPacketSender().sendString(8238, "Boosted stats will be restored.");
        if (selectedDuelRules[DuelRule.OBSTACLES.ordinal()])
            player.getPacketSender().sendString(8239, "There will be obstacles in the arena.", Colours.RED);
        player.getPacketSender().sendString(8240, "");
        player.getPacketSender().sendString(8241, "");
        int lineNumber = 8243;
        for (int i = 0; i < DuelRule.values().length; i++) {
            if (i == DuelRule.OBSTACLES.ordinal())
                continue;
            if (selectedDuelRules[i]) {
                player.getPacketSender().sendString(lineNumber, "" + DuelRule.forId(i).toString());
                lineNumber++;
            }
        }
        player.getPacketSender().sendString(6571, "");
        player.getPacketSender().sendInterfaceSet(6412, Inventory.INTERFACE_ID);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    public static boolean handleDuelingButtons(final Player player, int button) {
        if (DuelRule.forButtonId(button) != null) {
            player.getDueling().selectRule(DuelRule.forButtonId(button));
            return true;
        } else {
            if (player.getDueling().duelingWith < 0)
                return false;
            final Player playerToDuel = GameWorld.getPlayers().get(player.getDueling().duelingWith);
            switch (button) {
                case 6676:
                case 6558:
                    player.getDueling().declineDuel(player.getDueling().duelingWith >= 0);
                    return true;
                case 6674:
                    if (!player.getDueling().inDuelScreen)
                        return true;
                    if (playerToDuel == null)
                        return true;
                    if (!checkDuel(player, 1) && !checkDuel(player, 2))
                        return true;
                    if (player.getDueling().selectedDuelRules[DuelRule.NO_MELEE.ordinal()] && player.getDueling().selectedDuelRules[DuelRule.NO_RANGED.ordinal()] && player.getDueling().selectedDuelRules[DuelRule.NO_MAGIC.ordinal()]) {
                        player.message("You won't be able to attack the other player with the current rules.");
                        return true;
                    }
                    player.getDueling().duelingStatus = 2;
                    if (player.getDueling().duelingStatus == 2) {
                        player.getPacketSender().sendString(6684, "Waiting for other player...");
                        playerToDuel.getPacketSender().sendString(6684, "Other player has accepted.");
                    }
                    if (playerToDuel.getDueling().duelingStatus == 2) {
                        playerToDuel.getPacketSender().sendString(6684, "Waiting for other player...");
                        player.getPacketSender().sendString(6684, "Other player has accepted.");
                    }
                    if (player.getDueling().duelingStatus == 2 && playerToDuel.getDueling().duelingStatus == 2) {
                        player.getDueling().duelingStatus = 3;
                        playerToDuel.getDueling().duelingStatus = 3;
                        playerToDuel.getDueling().confirmDuel();
                        player.getDueling().confirmDuel();
                    }
                    return true;
                case 6520:
                    if (!player.getDueling().inDuelScreen || (!checkDuel(player, 3) && !checkDuel(player, 4)) || playerToDuel == null)
                        return true;
                    player.getDueling().duelingStatus = 4;
                    if (playerToDuel.getDueling().duelingStatus == 4 && player.getDueling().duelingStatus == 4) {
                        player.getDueling().startDuel();
                        playerToDuel.getDueling().startDuel();
                    } else {
                        player.getPacketSender().sendString(6571, "Waiting for other player...");
                        playerToDuel.getPacketSender().sendString(6571, "Other player has accepted");
                    }
                    return true;
            }
        }
        return false;
    }

    public void startDuel() {
        if(player.getSession().isPresent())
            player.getSession().get().clearMessages();
        inDuelScreen = false;
        final Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
        if (playerToDuel == null) {
            duelVictory();
            return;
        }
        player.getTrade().clear();
        duelingData[0] = playerToDuel != null ? playerToDuel.getUsername() : "Disconnected";
        duelingData[1] = playerToDuel != null ? playerToDuel.getSkills().getCombatLevel() : 3;
        Item equipItem;
        for (int i = 10; i < selectedDuelRules.length; i++) {
            DuelRule rule = DuelRule.forId(i);
            if (selectedDuelRules[i]) {
                if (rule.getEquipmentSlot() < 0)
                    continue;
                if (player.getEquipment().get(rule.getEquipmentSlot()).getId() > 0) {
                    equipItem = player.getEquipment().get(rule.getEquipmentSlot()).copy();
                    player.getEquipment().move(equipItem, player.getInventory());
                }
            }
        }
        if (selectedDuelRules[DuelRule.NO_WEAPON.ordinal()] || selectedDuelRules[DuelRule.NO_SHIELD.ordinal()]) {
            if (player.getEquipment().get(Slot.WEAPON).getId() > 0) {
                if (Equipment.isTwoHandedWeapon(player.getEquipment().get(Slot.WEAPON))) {
                    equipItem = player.getEquipment().get(Slot.WEAPON).copy();
                    player.getEquipment().move(equipItem, player.getInventory());
                }
            }
        }
        equipItem = null;
        player.getInventory().refresh();
        player.getEquipment().refresh();
        player.getMovement().reset();

        player.setCanPvp(true);
        if (player.getPlayerInteractingOption() != PlayerInteractingOption.ATTACK)
            player.getPacketSender().sendInteractionOption("Attack", 2, true);
        player.getMovement().lock();
        player.getPacketSender().sendInterfaceRemoval();
        if (selectedDuelRules[DuelRule.OBSTACLES.ordinal()]) {
            if (selectedDuelRules[DuelRule.NO_MOVEMENT.ordinal()]) {
                player.moveTo(duelTelePos);
            } else {
                player.moveTo(new Position(3366 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0));
            }
        } else {
            if (selectedDuelRules[DuelRule.NO_MOVEMENT.ordinal()]) {
                player.moveTo(duelTelePos);
            } else {
                player.moveTo(new Position(3335 + Misc.getRandom(12), 3246 + Misc.getRandom(6), 0));
            }
        }
        player.reset();
        player.getPacketSender().sendPositionalHint(playerToDuel.copyPosition(), 10);
        player.getPacketSender().sendEntityHint(playerToDuel);
        GameWorld.schedule(new Task(true, 2) {
            @Override
            public void run() {
                if (!DuelController.isAtDuelArena(player)) {
                    player.getMovement().unlock();
                    stop();
                    return;
                }
                if (timer == 3 || timer == 2 || timer == 1)
                    player.forceChat(timer + "..");
                else {
                    player.forceChat("FIGHT!!");
                    player.getMovement().unlock();
                    timer = -1;
                    duelingStatus = 5;
                    stop();
                    return;
                }
                timer--;
            }
        });
        player.getUpdateFlags().add(Flag.APPEARANCE);
        BonusManager.update(player);
    }

    public void duelVictory() {
        final boolean refund = player.getHealth().getHitpoints() == 0;
        duelingStatus = 6;
        player.reset();
        player.getMovement().reset();
        player.getMovement().unlock();
        CopyOnWriteArrayList<Item> wonItems = new CopyOnWriteArrayList<>();
        if (duelingWith > 0) {
            Player playerDuel = GameWorld.getPlayers().get(duelingWith);
            if (playerDuel != null && playerDuel.getDueling().stakedItems.size() > 0) {
                for (Item item : playerDuel.getDueling().stakedItems) {
                    if (item.getId() > 0 && item.getAmount() > 0) {
                        if (refund) {
                            playerDuel.getInventory().add(item);
                        } else {
                            PlayerLogs.log(player.getUsername(), "Player won " + playerDuel.getUsername() + "' staked item in duel: " + item.getId() + ", " + item.getAmount());
                            //stakedItems.add(item);
                            wonItems.add(item);
                        }
                    }
                }
                if (refund) {
                    playerDuel.message("Staked items have been refunded as both duelists died.");
                    player.message("Staked items have been refunded as both duelists died.");
                }
            }
        }
        player.getPacketSender().sendInterfaceItems(6822, wonItems);
        player.getPacketSender().sendString(6840, "" + duelingData[0]);
        player.getPacketSender().sendString(6839, "" + duelingData[1]);
        player.moveTo(new Position(3368 + Misc.getRandom(5), 3267 + Misc.getRandom(3), 0));
        if (wonItems.size() > 0) {
            for (Item item : wonItems) {
                if (item.getId() > 0 && item.getAmount() > 0) {
                    player.getInventory().add(item);
                    PlayerLogs.log(player.getUsername(), "Player won THEIR staked items in duel: " + item.getId() + ", " + item.getAmount());
                }
            }
        }
        for (Item item : stakedItems) {
            if (item.getId() > 0 && item.getAmount() > 0) {
                player.getInventory().add(item);
                //PlayerLogs.log(player.getUsername(), "Player won THEIR staked items in duel: "+item.getScrollId()+", "+item.getAmount());
            }
        }
        reset();
        arenaStats[0]++;
        player.getDirection().setInteracting(null);
        player.getMovement().reset();
        player.getPacketSender().sendInterface(6733);
        player.getPointsHandler().refreshPanel();
    }

    public static boolean checkDuel(Player playerToDuel, int statusReq) {
        boolean goodInterfaceId = playerToDuel.getInterfaceId() == -1 || playerToDuel.getInterfaceId() == 6575 || playerToDuel.getInterfaceId() == 6412;
        return !(playerToDuel.getDueling().duelingStatus != statusReq || playerToDuel.isBanking() || playerToDuel.isShopping() || playerToDuel.getHealth().getHitpoints() <= 0 || playerToDuel.getSettings().isResting() || !goodInterfaceId);
    }

    public static boolean checkRule(Player player, DuelRule rule) {
        if (/*player.getLocation() == Location.DUEL_ARENA && */player.getDueling().duelingStatus == 5) {
            if (player.getDueling().selectedDuelRules[rule.ordinal()])
                return true;
        }
        return false;
    }

    public void reset() {
        inDuelWith = -1;
        duelingStatus = 0;
        inDuelScreen = false;
        duelRequested = false;
        canOffer = false;
        for (int i = 0; i < selectedDuelRules.length; i++)
            selectedDuelRules[i] = false;
        player.getTrade().setCanOffer(true);
        sendDuelConfig(player, new boolean[22]);
        //player.getPacketSender().sendConfig(286, 0);
        stakedItems.clear();
        if (duelingWith >= 0) {
            Player playerToDuel = GameWorld.getPlayers().get(duelingWith);
            if (playerToDuel != null) {
                player.getPacketSender().sendInterfaceItems(6670, playerToDuel.getDueling().stakedItems);
                playerToDuel.getPacketSender().sendInterfaceItems(6670, player.getDueling().stakedItems);
            }
            player.getPacketSender().sendInterfaceItems(6669, player.getDueling().stakedItems);
        }
        duelingWith = -1;
        duelConfig = 0;
        duelTelePos = null;
        timer = 3;
        player.getActionManager().forceStop();
        player.getMovement().reset();
        player.getPacketSender().sendEntityHintRemoval(true);
    }

    public boolean getCanOffer() {
        return canOffer && player.getInterfaceId() == 6575 && !player.isBanking() && !player.getPriceChecker().isOpen();
    }

    public int duelingStatus = 0;
    public int duelingWith = -1;
    public boolean inDuelScreen = false;
    public boolean duelRequested = false;
    public boolean[] selectedDuelRules = new boolean[DuelRule.values().length];
    private static boolean[] tempRules = new boolean[DuelRule.values().length];
    public CopyOnWriteArrayList<Item> stakedItems = new CopyOnWriteArrayList<>();
    public int arenaStats[] = {0, 0};
    public int spaceReq = 0;
    public int duelConfig;
    public int timer = 3;
    public int inDuelWith = -1;
    private boolean canOffer;

    public Object[] duelingData = new Object[2];
    protected Position duelTelePos = null;

    public enum DuelRule {
        NO_RANGED(-1, 6734, -1, -1),
        NO_MELEE(-1, 6735, -1, -1),
        NO_MAGIC(-1, 6736, -1, -1),
        NO_SPECIAL_ATTACKS(-1, 6737, -1, -1),
        LOCK_WEAPON(-1, 6738, -1, -1),
        NO_FORFEIT(-1, 6739, -1, -1),
        NO_POTIONS(-1, 6740, -1, -1),
        NO_FOOD(-1, 6741, -1, -1),
        NO_PRAYER(-1, 6742, -1, -1),
        NO_MOVEMENT(-1, 6743, -1, -1),
        OBSTACLES(-1, 6744, -1, -1),

        NO_HELM(-1, 6745, 1, Slot.HEAD.ordinal()),
        NO_CAPE(-1, 6746, 1, Slot.CAPE.ordinal()),
        NO_AMULET(-1, 6747, 1, Slot.AMULET.ordinal()),
        NO_AMMUNITION(-1, 6748, 1, Slot.ARROWS.ordinal()),
        NO_WEAPON(-1, 6749, 1, Slot.WEAPON.ordinal()),
        NO_BODY(-1, 6750, 1, Slot.BODY.ordinal()),
        NO_SHIELD(-1, 6751, 1, Slot.SHIELD.ordinal()),
        NO_LEGS(-1, 6752, 1, Slot.LEGS.ordinal()),
        NO_GLOVES(-1, 6753, 1, Slot.HANDS.ordinal()),
        NO_BOOTS(-1, 6754, 1, Slot.FEET.ordinal()),
        NO_RING(-1, 6755, 1, Slot.RING.ordinal());

        DuelRule(int configId, int buttonId, int inventorySpaceReq, int equipmentSlot) {
            this.configId = configId;
            this.buttonId = buttonId;
            this.inventorySpaceReq = inventorySpaceReq;
            this.equipmentSlot = equipmentSlot;
        }

        private int configId;
        private int buttonId;
        private int inventorySpaceReq;
        private int equipmentSlot;

        public int getConfigId() {
            return configId;
        }

        public int getButtonId() {
            return this.buttonId;
        }

        public int getInventorySpaceReq() {
            return this.inventorySpaceReq;
        }

        public int getEquipmentSlot() {
            return this.equipmentSlot;
        }

        public static DuelRule forId(int i) {
            for (DuelRule r : DuelRule.values()) {
                if (r.ordinal() == i)
                    return r;
            }
            return null;
        }

        static DuelRule forButtonId(int buttonId) {
            for (DuelRule r : DuelRule.values()) {
                if (r.getButtonId() == buttonId)
                    return r;
            }
            return null;
        }

        @Override
        public String toString() {
            return Misc.formatText(this.name().toLowerCase());
        }
    }

    public static final int INTERFACE_REMOVAL_ID = 6669;
}
