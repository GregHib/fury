package com.fury.game.content.global.treasuretrails;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.Wilderness;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class handling all the necessary actions for treasure trails
 *
 * @author Greg
 */
public class TreasureTrails {

    public static void giveReward(Player player, ClueTiers tier) {
        int spacesRequired = 0;
        switch (tier) {
            case EASY:
                spacesRequired = 4;
                break;
            case MEDIUM:
                spacesRequired = 5;
                break;
            case HARD:
            case ELITE:
                spacesRequired = 6;
                break;
        }
        if(player.getInventory().getSpaces() < spacesRequired) {
            player.message("You need at least " + spacesRequired + " empty inventory slots.");
            return;
        }
        CopyOnWriteArrayList<Item> rewards = new CopyOnWriteArrayList();
        int numberOfRewards;
        switch (tier) {
            case EASY:
                numberOfRewards = Misc.inclusiveRandom(2, 4);
                while(rewards.size() < numberOfRewards) {
                    for(int i = 0; i < Rewards.EASY.values().length; i++) {
                        Rewards.EASY r = Rewards.EASY.values()[Misc.random(Rewards.EASY.values().length - 1)];
                        if(Misc.getRandom(r.getRarity().getRandom(player)) == 1) {
                            rewards.add(new Item(r.getItemId(), Misc.inclusiveRandom(r.getMinAmount(), r.getMaxAmount())));
                            break;
                        }
                    }
                }
                break;
            case MEDIUM:
                numberOfRewards = Misc.inclusiveRandom(3, 5);
                while(rewards.size() < numberOfRewards) {
                    for(int i = 0; i < Rewards.MEDIUM.values().length; i++) {
                        Rewards.MEDIUM r = Rewards.MEDIUM.values()[Misc.random(Rewards.MEDIUM.values().length - 1)];
                        if(Misc.getRandom(r.getRarity().getRandom(player)) == 1) {
                            rewards.add(new Item(r.getItemId(), Misc.inclusiveRandom(r.getMinAmount(), r.getMaxAmount())));
                            break;
                        }
                    }
                }
                break;
            case HARD:
                numberOfRewards = Misc.inclusiveRandom(4, 6);
                while(rewards.size() < numberOfRewards) {
                    for(int i = 0; i < Rewards.HARD.values().length; i++) {
                        Rewards.HARD r = Rewards.HARD.values()[Misc.random(Rewards.HARD.values().length - 1)];
                        if(Misc.getRandom(r.getRarity().getRandom(player)) == 1) {
                            rewards.add(new Item(r.getItemId(), Misc.inclusiveRandom(r.getMinAmount(), r.getMaxAmount())));
                            break;
                        }
                    }
                }
                break;
            case ELITE:
                numberOfRewards = Misc.inclusiveRandom(4, 6);
                while(rewards.size() < numberOfRewards) {
                    for(int i = 0; i < Rewards.ELITE.values().length; i++) {
                        Rewards.ELITE r = Rewards.ELITE.values()[Misc.random(Rewards.ELITE.values().length - 1)];
                        if(Misc.getRandom(r.getRarity().getRandom(player)) == 1) {
                            rewards.add(new Item(r.getItemId(), Misc.inclusiveRandom(r.getMinAmount(), r.getMaxAmount())));
                            break;
                        }
                    }
                }
                break;
        }
        if(tier == ClueTiers.ELITE)
            Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_15_ELITE_CLUE_SCROLLS);
        else if(tier == ClueTiers.MEDIUM)
            Achievements.finishAchievement(player, Achievements.AchievementData.COMPLETE_A_MEDIUM_CLUE);
        player.setClueScroll(tier.ordinal(), null);
        Item[] items = rewards.toArray(new Item[rewards.size()]);
        player.getPacketSender().sendInterface(ClueConstants.CLUE_REWARD_INTERFACE);
        player.getPacketSender().sendInterfaceItems(6963, rewards);
        player.getInventory().delete(new Item(tier.getCasketId()));
        player.getInventory().add(items);
        player.getInventory().refresh();

    }

    public static boolean hasRequiredItemsEquipped(Player player, int[][] equipment) {
        for(int[] equip : equipment) {
            int slot = equip[0];
            int item = equip[1];
            if(!player.getEquipment().validate(item, slot))
                continue;
            if(player.getEquipment().get(slot).getId() == item)
                continue;
            return false;
        }
        return true;
    }

    public static void spawnDoubleAgent(Player player, ClueScroll clue) {
        Position position = getSpotNearPlayer(player);

        int doubleAgentId = player.isInWilderness() && ((Wilderness) player.getControllerManager().getController()).getWildLevel() == 0 ? ClueConstants.DOUBLE_AGENT_HIGH : ClueConstants.DOUBLE_AGENT_LOW;

        DoubleAgent da = new DoubleAgent(doubleAgentId, position);
        da.setTarget(player);
        da.getDirection().face(player);
        da.graphic(188);
        da.forceChat("I expect you to die!");

        clue.setDoubleAgent(da);
        clue.setDoubleAgentDead(false);
        player.setClueScroll(clue.getIndex(), clue);
    }

    public static void spawnUri(Player player, ClueScroll clue) {
        Position position = getSpotNearPlayer(player);

        Mob uri = new Mob(ClueConstants.URI, position);
        uri.getDirection().face(player);
        uri.graphic(188);
        uri.animate(863);

        clue.setUri(uri);
        clue.setUriSpawned(true);
        player.setClueScroll(clue.getIndex(), clue);
    }

    private static Position getSpotNearPlayer(Player player) {
        Position position = player.copyPosition();

        if(!World.blockedNorth(player))
            position.add(0, 1);
        else if(World.blockedEast(player))
            position.add(1, 0);
        else if(World.blockedSouth(player))
            position.add(0, -1);
        else if(World.blockedWest(player))
            position.add(-1, 0);
        return position;
    }

    public static void openInterface(Player player, ClueScroll clue) {
        if (clue == null)
            return;
        switch (clue.getType()) {
            case MAP:
                player.getPacketSender().sendInterface(ClueConstants.Maps.values()[clue.getIndex()].getInterfaceId());
                break;
            case SIMPLE:
            case EMOTE://Text
                player.getPacketSender().sendInterface(ClueConstants.CLUE_TEXT_INTERFACE);
                int indexOfType = clue.getIndex();
                String[] text = clue.getType() == ClueTypes.SIMPLE ? ClueConstants.SimpleClues.values()[indexOfType].getClue() : ClueConstants.Emotes.values()[indexOfType].getClue();
                sendClueText(player, text);
                break;
        }
    }

    private static void sendClueText(Player player, String[] clue) {
        int start = (int) Math.ceil((double) (8 - clue.length) / 2);
        for (int i = 0; i < 8; i++) {
            int index = i - start;

            if (index >= 0 && index < clue.length)
                player.getPacketSender().sendString(ClueConstants.CLUE_TEXT_INTERFACE + 3 + i, clue[index]);
            else
                player.getPacketSender().sendString(ClueConstants.CLUE_TEXT_INTERFACE + 3 + i, "");
        }
    }

    public static boolean hasActiveClue(Player player) {
        ClueScroll[] scrolls = player.getClueScrolls();
        for (int i = 0; i < scrolls.length; i++) {
            if (scrolls[i] != null)
                return true;
        }
        return false;
    }

    public static boolean[] getActiveClues(Player player) {
        ClueScroll[] scrolls = player.getClueScrolls();
        boolean[] active = new boolean[scrolls.length];

        for (int i = 0; i < scrolls.length; i++)
            active[i] = scrolls[i] != null;

        return active;
    }

    public static int getIndexUsingType(ClueTypes type, String name) {
        switch (type) {
            case MAP:
                return ClueConstants.Maps.getIndex(name);
            case SIMPLE:
                return ClueConstants.SimpleClues.getIndex(name);
            case EMOTE:
                return ClueConstants.Emotes.getIndex(name);
            default:
                return 0;
        }
    }

    public static void dropClue(Player player, Mob mob, ClueTiers clueTier) {
        boolean[] active = getActiveClues(player);

        if(active[clueTier.ordinal()] && player.hasItem(clueTier.getScrollId()))
            return;

        //Drop clue
        FloorItemManager.addGroundItem(new Item(clueTier.getScrollId()), mob.copyPosition(), player);

        if(!active[clueTier.ordinal()]) {
            player.setClueScroll(clueTier.ordinal(), new ClueScroll());
            setNewClueInfo(player, clueTier, true);
        }
    }

    public static void giveClue(Player player, ClueTiers clueTier) {
        boolean[] active = getActiveClues(player);

//        if(active[clueTier.ordinal()])
//            return;

        player.getInventory().addSafe(new Item(clueTier.getScrollId()));
        setNewClueInfo(player, clueTier, false);
    }

    public static void setNewClueInfo(Player player, ClueTiers clueTier, boolean setRemaining) {
        ClueScroll clue = player.getClueScroll(clueTier.ordinal());
        if(clue == null)
            clue = new ClueScroll();
        /*
        Different type of random generation
        ClueTypes type = ClueTypes.getRandom(clueTier);
        if(type == null)
            return;
        int index = getRandomIndexOfType(clueTier, type);*/

        //Newer method of random generation
        Object[] a = getRandom(clueTier);
        ClueTypes type = (ClueTypes) a[0];
        int index = (Integer) a[1];
        clue.setType(type);
        clue.setIndex(index);

        if(setRemaining) {
            int minClues = ClueConstants.MIN_NUMB_CLUES[clueTier.ordinal()];
            int maxClues = ClueConstants.MAX_NUMB_CLUES[clueTier.ordinal()];
            int numberOfClues = Misc.inclusiveRandom(minClues, maxClues);
            clue.setRemainingClues(numberOfClues);
        }
        player.setClueScroll(clueTier.ordinal(), clue);
    }

    public static int getRandomIndexOfType(ClueTiers tier, ClueTypes type) {
        switch (type) {
            case MAP:
                return Misc.random(ClueConstants.Maps.values().length - 1);
            case SIMPLE:
                return Misc.random(ClueConstants.SimpleClues.values().length - 1);
            case EMOTE:
                ClueConstants.Emotes emote = ClueConstants.Emotes.getRandom(tier);
                if(emote == null)
                    return 0;
                return emote.ordinal();
            default:
                return 0;
        }
    }

    public static Object[] getRandom(ClueTiers tier) {
        List<ClueTypes> allowedTypes = new ArrayList<>();
        List<Integer> allowed = new ArrayList<>();

        for(ClueTypes type : tier.getAvailableTypes()) {
            switch (type) {
                case MAP:
                    for(ClueConstants.Maps clue : ClueConstants.Maps.values()) {
                        allowedTypes.add(type);
                        allowed.add(clue.ordinal());
                    }
                    break;
                case SIMPLE:
                    for(ClueConstants.SimpleClues clue : ClueConstants.SimpleClues.values()) {
                        allowedTypes.add(type);
                        allowed.add(clue.ordinal());
                    }
                    break;
                case EMOTE:
                    for(ClueConstants.Emotes clue : ClueConstants.Emotes.values()) {
                        switch (tier) {
                            case EASY:
                                if (clue.getEmoteTwo() == null && !clue.isDoubleAgent()) {
                                    allowedTypes.add(type);
                                    allowed.add(clue.ordinal());
                                }
                                break;
                            case MEDIUM:
                                if (clue.getEmoteTwo() != null && !clue.isDoubleAgent()) {
                                    allowedTypes.add(type);
                                    allowed.add(clue.ordinal());
                                }
                                break;
                            case HARD:
                                if (clue.getEmoteTwo() == null && clue.isDoubleAgent()) {
                                    allowedTypes.add(type);
                                    allowed.add(clue.ordinal());
                                }
                                break;
                        }
                    }
                    break;
            }
        }
        Object[] keys = allowedTypes.toArray();
        Object[] values = allowed.toArray();
        int random = Misc.random(keys.length - 1);
        return new Object[] {keys[random], values[random]};
    }

    public static void interactObject(Player player, GameObject object) {
        if(hasClueInInventory(player)) {
            ClueScroll[] clues = getActiveCarriedClues(player);
            for(int i = 0; i < 4; i++) {
                ClueScroll clue = clues[i];
                if(clue == null)
                    continue;

                if(isInClueLocation(object, clue)) {
                    switch (clue.getType()) {
                        case MAP:
                        case SIMPLE:
                            discoverClue(player, i, clue, null);
                            return;
                    }
                }
            }
        }
    }

    public static void discoverClue(Player player, int tier, ClueScroll clue, Mob mob) {
        boolean cont = true;
        if(tier == 2 && clue.getType() == ClueTypes.EMOTE) {
            if(!clue.isDoubleAgentDead())
                cont = false;
        }
        if(cont) {
            player.getInventory().delete(new Item(ClueTiers.values()[tier].getScrollId()));
            if (clue.getRemainingClues() > 1) {
                player.getInventory().addSafe(new Item(ClueTiers.values()[tier].getBoxId()));
                clue.setRemainingClues(clue.getRemainingClues() - 1);
                if(mob == null)
                    player.message("You find a scroll box!");
                else
                    player.message(mob.getName() + " hands you a scroll box!");
            } else {
                player.getInventory().addSafe(new Item(ClueTiers.values()[tier].getCasketId()));
                player.setClueScroll(tier, null);
                if(mob == null)
                    player.message("You find a casket!");
                else
                    player.message(mob.getName() + " hands you a casket!");
            }
        }
    }

    public static boolean dig(Player player) {
        if(hasClueInInventory(player)) {
            ClueScroll[] clues = getActiveCarriedClues(player);
            for(int i = 0; i < 4; i++) {
                ClueScroll clue = clues[i];
                if(clue == null)
                    continue;

                if(isInClueLocation(player, clue)) {
                    switch (clue.getType()) {
                        case MAP:
                        case SIMPLE:
                            discoverClue(player, i, clue, null);
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static ClueScroll[] getActiveCarriedClues(Player player) {
        boolean[] active = getActiveClues(player);
        ClueScroll[] scrolls = new ClueScroll[4];
        for(int i = 0; i < ClueTiers.values().length; i++)
            if(active[i])
                if (player.getInventory().contains(new Item(ClueTiers.values()[i].getScrollId())))
                    scrolls[i] = player.getClueScroll(i);
        return scrolls;
    }

    public static boolean hasClueInInventory(Player player) {
        for(ClueTiers tier : ClueTiers.values())
            if(player.getInventory().contains(new Item(tier.getScrollId())))
                return true;

        return false;
    }

    public static boolean isInClueLocation(Position pos, ClueScroll clue) {
        switch (clue.getType()) {
            case MAP:
                Position mapsSpot = ClueConstants.Maps.values()[clue.getIndex()].getLocation();
                if(mapsSpot == null)
                    return true;
                return pos.sameAs(mapsSpot);
            case SIMPLE:
                Position simpleSpot = ClueConstants.SimpleClues.values()[clue.getIndex()].getLocation();
                if(simpleSpot == null)
                    return true;
                return pos.sameAs(simpleSpot);
            case EMOTE:
                ClueConstants.Emotes emoteData = ClueConstants.Emotes.values()[clue.getIndex()];
                if(emoteData == null)
                    return true;
                return inLocation(pos, emoteData.getX(), emoteData.getY(), emoteData.getPlane());
        }
        return false;
    }

    private static boolean inLocation(Position pos, int[] x, int[] y, int z) {
        int checks = x.length - 1;
        for(int i = 0; i <= checks; i+=2) {
            if(pos.getZ() == z) {
                if (pos.getX() >= x[i] && pos.getX() <= x[i + 1]) {
                    if (pos.getY() >= y[i] && pos.getY() <= y[i + 1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
