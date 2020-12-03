package com.fury.game.content.controller.impl.duel;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.container.impl.Inventory;
import com.fury.game.container.impl.StakeContainer;
import com.fury.game.content.controller.impl.Rule;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.loaders.item.ItemConstants;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class DuelConfigurations {

    public Player player, target;

    public boolean[] duelRules = new boolean[26];

    public Position DUEL_ARENA_LOBBY = new Position(3365, 3275);

    private StakeContainer stake;

    private StakeContainer stake2;

    public DuelConfigurations(Player player, Player target, boolean friendly) {
        stake = new StakeContainer(player);
        stake2 = new StakeContainer(target);
        this.target = target;
        this.player = player;
        updateScreen(player, friendly);
        updateScreen(target, friendly);
        player.setCloseInterfacesEvent(() -> declineDuel(player));
        target.setCloseInterfacesEvent(() -> declineDuel(target));
    }


    private void updateScreen(Player player, boolean friendly) {
        if (!friendly) {
            sendInventory(player);
            stake.clear();
            stake2.clear();
        }
        player.getPacketSender().sendItemContainer(stake, 6669);
        player.getPacketSender().sendItemContainer(stake2, 6670);

        player.getPacketSender().sendDuelEquipment();

//        player.getPacketSender().sendInterfaceSet(6575, 3321);
//        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);

        String col = FontUtils.ORANGE_2;
        if(player.getSkills().getCombatLevel() - getOther(player).getSkills().getCombatLevel() >= 10)
            col = FontUtils.GREEN_1;

        player.getPacketSender().sendString(6671, "Opponent: " +Utils.formatPlayerNameForDisplay(getOther(player).getUsername()));
        player.getPacketSender().sendString(6628, "Opponent's Level: " + col + (getOther(player).getSkills().getCombatLevel()) + FontUtils.COL_END);
        player.getPacketSender().sendString(6684, "").sendString(669, "Lock Weapon").sendString(8278, "Neither player is allowed to change weapon.");

        sendDuelConfig(player, new boolean[22]);

        player.getTemporaryAttributes().put("firstScreen", true);
//        player.getPacketSender().sendInterface(631);
    }

    public static void sendDuelConfig(Player player, boolean[] rules) {
        for(int i = 0; i < rules.length; i++)
            player.getPacketSender().sendButtonToggle(6734 + i, rules[i]);
    }

    private static final int LOGOUT = 0, TELEPORT = 1, DUEL_END_LOSE = 2, DUEL_END_WIN = 3;

    public void endDuel(Player player, boolean declined, boolean logout) {
        for (int i = 0; i < duelRules.length; i++) {
            duelRules[i] = false;
        }
        if (!declined) {
            if (logout)
                player.moveTo(DUEL_ARENA_LOBBY);
            else
                player.moveTo(DUEL_ARENA_LOBBY);
        } else {
            this.player.getInventory().add(stake2.getItems());
            target.getInventory().add(stake2.getItems());
            player.getPacketSender().sendInterfaceRemoval();
        }
        stake.clear();
        stake2.clear();
        player.setCanPvp(false);
        player.getControllerManager().removeControllerWithoutCheck();
        player.getPacketSender().sendEntityHintRemoval(true);
        player.reset();
        player.getInventory().refresh();
    }

    public void sendInventory(Player player) {

        player.getPacketSender().sendInterfaceSet(6575, 3321);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);

        /*player.getInterfaceManager().sendInventoryInterface(628);
        player.getPacketSender().sendItems(93, player.getInventory().getItems());
        player.getPacketSender().sendUnlockIComponentOptionSlots(628, 0, 0, 27, 0,
                1, 2, 3, 4, 5);
        player.getPacketSender().sendInterSetItemsOptionsScript(628, 0, 93, 4, 7,
                "Stake 1", "Stake 5", "Stake 10", "Stake All", "Examine");
        player.getPacketSender().sendUnlockIComponentOptionSlots(631, 103, 0, 27, 0,
                1, 2, 3, 4, 5);
        player.getPacketSender().sendInterSetItemsOptionsScript(631, 0, 134, 4, 7,
                "Remove 1", "Remove 5", "Remove 10", "Remove All", "Examine");*/

    }

    public boolean processButtonClick(Player player, int interfaceId, int buttonId) {
        if (!player.isDueling())
            return false;

        switch (interfaceId) {
            case 6575:
                switch (buttonId) {
                    case 6734: // no range
                        setRules(Rule.NO_RANGED);
                        return false;
                    case 6735: // no melee
                        setRules(Rule.NO_MELEE);
                        return false;
                    case 6736: // no magic
                        setRules(Rule.NO_MAGIC);
                        return false;
                    case 6737:// no spec
                        setRules(Rule.NO_SPECIAL_ATTACK);
                        return false;
                    case 6738: // fun wep//TODO lock weapon
                        setRules(Rule.LOCK_WEAPONS);
                        return false;
                    case 6739: // no forfiet
                        setRules(Rule.NO_FORFEIT);
                        return false;
                    case 6740: // no drinks
                        setRules(Rule.NO_DRINKS);
                        return false;
                    case 6741: // no food
                        setRules(Rule.NO_FOOD);
                        return false;
                    case 6742: // no prayer
                        setRules(Rule.NO_PRAYER);
                        return false;
                    case 6743: // no movement
                        setRules(Rule.NO_MOVEMENT);
                        if (getRule(Rule.OBSTACLES)) {
                            setRule(Rule.OBSTACLES, false);
                            player.getPacketSender().sendButtonToggle(6734 + Rule.OBSTACLES.ordinal(), false);
                            getOther(player).getPacketSender().sendButtonToggle(6734 + Rule.OBSTACLES.ordinal(), false);
                            player.message("You can't have movement without obstacles.");
                        }
                        return false;
                    case 6744: // obstacles
                        setRules(Rule.OBSTACLES);
                        if (getRule(Rule.NO_MOVEMENT)) {
                            setRule(Rule.NO_MOVEMENT, false);
                            player.getPacketSender().sendButtonToggle(6734 + Rule.NO_MOVEMENT.ordinal(), false);
                            getOther(player).getPacketSender().sendButtonToggle(6734 + Rule.NO_MOVEMENT.ordinal(), false);
                            player.message("You can't have obstacles without movement.");
                        }
                        return false;
                    /*case 65: // enable summoning
                        setRules(24);
                        return false;*/
                    case 6745:// no helm
                        setRules(Rule.NO_HEAD);
                        return false;
                    case 6746:// no cape
                        setRules(Rule.NO_CAPE);
                        return false;
                    case 6747:// no ammy
                        setRules(Rule.NO_AMULET);
                        return false;
                    case 6748:// arrows
                        setRules(Rule.NO_ARROWS);
                        return false;
                    case 6749:// weapon
                        setRules(Rule.NO_WEAPON);
                        return false;
                    case 6750:// body
                        setRules(Rule.NO_BODY);
                        return false;
                    case 6751:// shield
                        setRules(Rule.NO_SHIELD);
                        return false;
                    case 6752:// legs
                        setRules(Rule.NO_LEGS);
                        return false;
                    case 6753: // gloves
                        setRules(Rule.NO_HANDS);
                        return false;
                    case 6754: // boots
                        setRules(Rule.NO_FEET);
                        return false;
                    case 6755:// ring
                        setRules(Rule.NO_RING);
                        return false;
                    case 6676:
                        declineDuel(player);
                        return false;
                    case 6674:
                        handleAccept(player, false);
                        return false;
                }
                break;
            case 6412:
                switch (buttonId) {
                    case 6520:
                        handleSecondScreenAccept(player, false);
                        return false;
                    case 6558:
                        declineDuel(player);
                        return false;
                }
        }
        return true;
    }

    private void declineDuel(Player player) {
        Player other = getOther(player);
        other.setCloseInterfacesEvent(null);
        player.setCloseInterfacesEvent(null);
        player.message("You've declined the duel.");
        other.message("Other player has declined the duel.");
        endDuel(player, true, false);
        endDuel(other, true, false);
        player.getControllerManager().startController(new DuelController());
        other.getControllerManager().startController(new DuelController());
    }

    public void handleAccept(final Player player, boolean friendly) {
        if (canAccept(player)) {
            Player other = getOther(player);
            player.getTemporaryAttributes().put("accepted", true);
            player.getPacketSender().sendString(6684, "Waiting for other player...");
            other.getPacketSender().sendString(6684, "Other player has accepted.");
            if (other.getTemporaryAttributes().get("accepted") != null) {
//                player.setCloseInterfacesEvent(null);
//                other.setCloseInterfacesEvent(null);
                player.getTemporaryAttributes().put("accepted", false);
                other.getTemporaryAttributes().put("accepted", false);
                player.getTemporaryAttributes().remove("firstScreen");
                other.getTemporaryAttributes().remove("firstScreen");
                openSecondInterface(player, other, friendly);
                openSecondInterface(other, player, friendly);
//                if (stake.getSpaces() != 0) {
//                    player.getPacketSender().sendIComponentText(626, 25, "");
//                    other.getPacketSender().sendIComponentText(626, 26, "");
//                }
//                if (stake2.getSpaces() != 0) {
//                    player.getPacketSender().sendIComponentText(626, 26, "");
//                    other.getPacketSender().sendIComponentText(626, 25, "");
//                }
            }
        }
    }

    public void handleSecondScreenAccept(final Player player, boolean ifFriendly) {
        final Player other = getOther(player);
        player.getTemporaryAttributes().put("accepted", true);
        player.getPacketSender().sendString(6571, "Waiting for other player...");
        other.getPacketSender().sendString(6571, "Other player has accepted");
        if (other.getTemporaryAttributes().get("accepted") == Boolean.TRUE) {
            player.setCloseInterfacesEvent(null);
            other.setCloseInterfacesEvent(null);
            player.getTemporaryAttributes().put("accepted", false);
            other.getTemporaryAttributes().put("accepted", false);
            removeEquipment(player);
            removeEquipment(other);
            teleportPlayer(player, other);
        }
    }
    private void teleportPlayer(Player player, Player other) {
        Position[] teleports = getArenaTeleport();
        int random = Utils.getRandom(1);
        player.moveTo(random == 0 ? teleports[0] : teleports[1]);
        other.moveTo(random == 0 ? teleports[1] : teleports[0]);
        player.getControllerManager().startController(new DuelArena());
        other.getControllerManager().startController(new DuelArena());
    }

    private void removeEquipment(Player player) {
        for (Rule rule : Rule.values()) {
            if(rule.ordinal() < Rule.NO_HEAD.ordinal())
                continue;

            if (getRule(rule))
                player.getEquipment().move(player.getEquipment().get(rule.getSlot()), player.getInventory());
        }
    }

    private Position[] getArenaTeleport() {
        final int arenaChoice = Utils.getRandom(2);
        Position[] locations = new Position[2];
        int[] arenaBoundariesX = { 3337, 3367, 3336 };
        int[] arenaBoundariesY = { 3246, 3227, 3208 };
        int[] maxOffsetX = { 14, 14, 16 };
        int[] maxOffsetY = { 10, 10, 10 };
        int finalX = arenaBoundariesX[arenaChoice] + Utils.getRandom(maxOffsetX[arenaChoice]);
        int finalY = arenaBoundariesY[arenaChoice] + Utils.getRandom(maxOffsetY[arenaChoice]);
        locations[0] = (new Position(finalX, finalY, 0));
        if (getRule(Rule.NO_MOVEMENT)) {
            int direction = Utils.getRandom(1);
            if (direction == 0) {
                finalX--;
            } else {
                finalY++;
            }
        } else {
            finalX = arenaBoundariesX[arenaChoice] + Utils.getRandom(maxOffsetX[arenaChoice]);
            finalY = arenaBoundariesY[arenaChoice] + Utils.getRandom(maxOffsetY[arenaChoice]);
        }
        locations[1] = (new Position(finalX, finalY, 0));
        return locations;
    }

    private boolean canAccept(Player player) {
        if (getRule(Rule.NO_RANGED) && getRule(Rule.NO_MAGIC) && getRule(Rule.NO_MELEE)) {
            player.message("You have to be able to use at least one combat style in a duel.", true);
            return false;
        }
        int count = 0;
        Item item;
        for (Rule rule : Rule.values()) {
            if(rule.ordinal() < Rule.NO_HEAD.ordinal())
                continue;

            if (getRule(rule) && (item = player.getEquipment().get(rule.getSlot())) != null) {
                if (rule == Rule.NO_ARROWS) {// arrows
                    if (!(item.getDefinition().isStackable() && player.getInventory().containsAmount(new Item(item, 1))))
                        count++;
                } else {
                    count++;
                }
            }
        }
        int freeSlots = player.getInventory().getSpaces() - count;
        if (freeSlots < 0) {
            player.message("You do not have enough inventory space to remove all the equipment.");
            getOther(player).message("Your opponent does not have enough space to remove all the equipment.");
            return false;
        }
        //TODO getCapacity maybe?
        for (int i = 0; i < stake.getItemTotal() + stake2.getItemTotal(); i++) {
            if (stake.get(i) != null && stake2.get(i) != null) {
                freeSlots--;
            }
        }

        if (freeSlots < 0) {
            player.message("You do not have enough room in your inventory for this stake.");
            getOther(player).message("Your opponent does not have enough room in his inventory for this stake.");
            return false;
        }

        return true;
    }

    private void openSecondInterface(Player player, Player other, boolean friendly) {
        String items = "";
        for (Item item : getStake(player).getItems()) {
            if(item == null)
                continue;
            ItemDefinition def = item.getDefinition();
            if (def.isStackable()) {
                String col = FontUtils.YELLOW;
                if(item.getId() == 995)
                    col = (item.getAmount() >= 10000000) ? FontUtils.GREEN_3 : FontUtils.WHITE;
                items += def.getName() + " " + FontUtils.WHITE + "x " + col + Misc.format(item.getAmount()) + "\\n";
            } else {
                items += def.getName() + "\\n";
            }
        }
        player.getPacketSender().sendString(6516, items);
        items = "";
        for (Item item : getStake(other).getItems()) {
            if(item == null)
                continue;
            ItemDefinition def = item.getDefinition();
            if (def.isStackable()) {
                String col = FontUtils.YELLOW;
                if(item.getId() == 995)
                    col = (item.getAmount() >= 10000000) ? FontUtils.GREEN_3 : FontUtils.WHITE;
                items += def.getName() + " " + FontUtils.WHITE + "x " + col + Misc.format(item.getAmount()) + "\\n";
            } else {
                items += def.getName() + "\\n";
            }
        }

        player.getPacketSender().sendString(6517, items);
        player.getPacketSender().sendString(8242, "");
        for (int i = 8238; i <= 8253; i++)
            player.getPacketSender().sendString(i, "");

        player.getPacketSender().sendString(8238, "Boosted stats will be restored.");

        player.getPacketSender().sendString(8240, "");
        player.getPacketSender().sendString(8241, "");
        int lineNumber = 8243;
        for (Rule rule : Rule.values()) {
            if(rule == Rule.OBSTACLES)
                continue;

            if (duelRules[rule.ordinal()]) {
                player.getPacketSender().sendString(lineNumber, "" + rule.toString());
                lineNumber++;
            }
        }
        player.getPacketSender().sendString(6571, "");
        player.getPacketSender().sendInterfaceSet(6412, Inventory.INTERFACE_ID);
        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
    }

    private void setRules(Rule rule) {
        if (!getRule(rule)) {
            setRule(rule, true);
            player.getPacketSender().sendButtonToggle(6734 + rule.ordinal(), true);
            target.getPacketSender().sendButtonToggle(6734 + rule.ordinal(), true);
        } else if (getRule(rule)) {
            setRule(rule, false);
            player.getPacketSender().sendButtonToggle(6734 + rule.ordinal(), false);
            target.getPacketSender().sendButtonToggle(6734 + rule.ordinal(), false);
        }
        player.getTemporaryAttributes().remove("accepted");
        target.getTemporaryAttributes().remove("accepted");

        player.getPacketSender().sendString(6684, "");
        target.getPacketSender().sendString(6684, "");
    }

    public void offerStake(Player p, int slot, int amount) {
        Item item = p.getInventory().get(slot);
        if (item == null)
            return;
        if (!ItemConstants.isTradeable(item)) {
            p.message("That item isn't stakeable.");
            return;
        }
        player.getTemporaryAttributes().remove("accepted");
        target.getTemporaryAttributes().remove("accepted");

        player.getPacketSender().sendString(6684, "");
        target.getPacketSender().sendString(6684, "");
        Item[] itemsBefore = getStake(p).getItems();
        int maxAmount = p.getInventory().getAmount(item);
        int trueAmount = amount > maxAmount ? maxAmount : amount;
        if (item.getAmount() < trueAmount)
            trueAmount = item.getAmount();
        item = new Item(item.getId(), trueAmount);
        getStake(p).add(item);
        p.getInventory().delete(item);
        refreshItems(p, itemsBefore);
    }

    public void removeStake(Player p, int clickSlotId, int amount) {
        Item item = getStake(p).get(clickSlotId);
        if (item == null)
            return;
        player.getTemporaryAttributes().remove("accepted");
        target.getTemporaryAttributes().remove("accepted");

        player.getPacketSender().sendString(6684, "");
        target.getPacketSender().sendString(6684, "");
        Item[] itemsBefore = getStake(p).getItems();
        int maxAmount = getStake(p).getAmount(item);
        if (amount < maxAmount)
            item = new Item(item.getId(), amount);
        else
            item = new Item(item.getId(), maxAmount);
        getStake(p).delete(clickSlotId);
        p.getInventory().add(item);
        refreshItems(p, itemsBefore);
    }

    private void refreshItems(Player p, Item[] itemsBefore) {
        int[] changedSlots = new int[itemsBefore.length];
        int count = 0;
        for (int index = 0; index < itemsBefore.length; index++) {
            Item item = getStake(p).getItems()[index];
            if (item != null)
                if (itemsBefore[index] != item) {
                    changedSlots[count++] = index;
                }
        }
        int[] finalChangedSlots = new int[count];
        System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
        refresh(finalChangedSlots);
    }

    public void refresh(int... slots) {
        player.getPacketSender().sendItemContainer(stake, 6669);
        player.getPacketSender().sendItemContainer(stake2, 6670);
        target.getPacketSender().sendItemContainer(stake2, 6669);
        target.getPacketSender().sendItemContainer(stake, 6670);

        player.getPacketSender().sendItemContainer(player.getInventory(), 3322);
        target.getPacketSender().sendItemContainer(target.getInventory(), 3322);
//        player.getInventory().refresh();
//        target.getInventory().refresh();
    }

    public void reward(Player player) {
        player.getInventory().add(stake2.getItems());
        player.getInventory().add(stake.getItems());
        player.getInventory().refresh();
    }

    public void addSpoils(Player player) {
        Player other = getOther(player);
        player.getPacketSender().sendInterface(6733);

        player.getPacketSender().sendItemContainer(stake, 6822);
        player.getPacketSender().sendString(6840, other != null ? other.getUsername() : "Disconnected");
        player.getPacketSender().sendString(6839, other != null ? other.getSkills().getCombatLevel() + "" : "3");
//        if (player == this.player)
//            other.getPacketSender().sendItems(136, false, stake);

//        if (player == target)
//            other.getPacketSender().sendItems(136, false, stake2);

//        other.getPacketSender().sendUnlockIComponentOptionSlots(634, 28, 0, 35, 0,
//                1, 2, 3, 4, 5);
//        other.getPacketSender().sendInterSetItemsOptionsScript(634, 28, 136, 4, 7,
//                "", "", "", "", "");
//        other.getPacketSender().sendIComponentText(634, 23,
//                Utils.formatPlayerNameForDisplay(player.getUsername()));
//        other.getPacketSender().sendIComponentText(634, 22,
//                Integer.toString(player.getSkills().getCombatLevel()));
    }

    private boolean setRule(Rule rule, boolean value) {
        return duelRules[rule.ordinal()] = value;
    }

    public boolean getRule(Rule rule) {
        return duelRules[rule.ordinal()];
    }

    public Player getOther(Player player) {
        return player == this.player ? this.target : this.player;
    }

    private StakeContainer getStake(Player p) {
        return p == player ? stake : stake2;
    }
}
