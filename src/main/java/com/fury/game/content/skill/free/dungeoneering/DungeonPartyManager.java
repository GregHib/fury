package com.fury.game.content.skill.free.dungeoneering;

import com.fury.game.GameSettings;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.FontUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Gabriel Hannason
 */
public class DungeonPartyManager {

    private String leader; // username
    private int floor;
    private int complexity;
    private int size;
    private int difficulty;
    private boolean guideMode;
    private int keyType;
    private int shopId;

    private CopyOnWriteArrayList<Player> team;
    private DungeonManager dungeon;

    public DungeonPartyManager() {
        team = new CopyOnWriteArrayList<>();
    }

    public void setDefaults() {
        floor = 1;
        complexity = 1;
        difficulty = team.size();
        guideMode = false;
    }

    public void generateShop() {
        if (complexity > 1) {
            Item[] items = new Item[72 + 24];
            for (int i = 0; i < items.length; i++)
                items[i] = new Item(-1, 0);
            int[] ores = new int[]{17630, 17632, 17634, 17636, 17638, 17640, 17642, 17644, 17646, 17648};
            int[] herbs = new int[]{17448, 17450, 17452, 17454, 17456, 17458, 17460, 17462, 17464, 17466};
            int[] hides = new int[]{17424, 17426, 17428, 17430, 17432, 17434, 17436, 17438, 17440, 17442};
            int[] food = new int[]{17797, 17799, 17801, 17803, 17805, 17807, 17809, 17811, 17813, 17815};
            int[] logs = new int[]{17682, 17684, 17686, 17688, 17690, 17692, 17694, 17696, 17698, 17700};
            int[] seeds = new int[]{17823, 17824, 17825, 17826, 17827, 17828, 17829, 17830, 17831, 17832, 17833, 17834};
            int esse = 17776;
            int[] arrows = new int[]{16427, 16432, 16437, 16442, 16447};
            int[] misc = new int[]{17530, 17536, 17534, 17532};

            int[] meleeEquip = new int[]{16867, 16317, 16977, 16935, 17341};
            int[] pickaxes = new int[]{16295, 16297, 16299};
            int[] hatchets = new int[]{16361, 16363, 16365};
            int hammer = 17883;
            int tinderbox = 17678;
            int fly_fishing_rod = 17794;
            int knife = 17754;
            int bowstring = 17752;
            int needle = 17446;
            int thread = 17447;
            int empty_vial = 17490;
            int feather = 17796;
            int antipoison = 17566;
            int toolkit = 19650;

            int index = 0;

            int amountOfResources = complexity == 2 ? 1 : complexity == 6 ? 10 : complexity;
            int stockSize = 1000000;

            /**
             * Resources
             */
            //Smithing
            if (complexity >= 3)
                for (int i = 0; i < amountOfResources; i++)
                    items[index++] = new Item(ores[i], stockSize);

            //Herblore
            if (complexity >= 5)
                for (int i = 0; i < amountOfResources; i++)
                    items[index++] = new Item(herbs[i], stockSize);

            //Crafting
            if (complexity >= 4)
                for (int i = 0; i < amountOfResources; i++)
                    items[index++] = new Item(hides[i], stockSize);

            //Cooking
            if (complexity >= 2)
                for (int i = 0; i < amountOfResources; i++)
                    items[index++] = new Item(food[i], stockSize);

            //Firemaking
            if (complexity >= 2)
                for (int i = 0; i < amountOfResources; i++)
                    items[index++] = new Item(logs[i], stockSize);

            //Farming
            if (complexity >= 5)
                for (int i = 0; i < amountOfResources + 2; i++)
                    items[index++] = new Item(seeds[i], stockSize);

            //Runecrafting
            if (complexity >= 5)
                items[index++] = new Item(esse, stockSize);

            //Ranged
            if (complexity >= 3)
                for (int i = 0; i < arrows.length; i++)
                    items[index++] = new Item(arrows[i], stockSize);

            //Misc
            if (complexity >= 4)
                for (int i = 0; i < misc.length; i++)
                    items[index++] = new Item(misc[i], stockSize);


            int dungeoneeringSplit = index;
            /**
             * Tools
             */

            index = 72;
            //Melee Equipment
            if (complexity >= 3)
                for (int i = 0; i < meleeEquip.length; i++)
                    items[index++] = new Item(meleeEquip[i], stockSize);
            //Pickaxes
            if (complexity >= 3)
                for (int i = 0; i < (complexity - 3); i++)
                    items[index++] = new Item(pickaxes[i], stockSize);
            //Hatchets
            if (complexity >= 3)
                for (int i = 0; i < (complexity >= 5 ? 3 : complexity - 2); i++)
                    items[index++] = new Item(hatchets[i], stockSize);

            if (complexity >= 3)
                items[index++] = new Item(hammer, stockSize);
            if (complexity >= 2)
                items[index++] = new Item(tinderbox, stockSize);
            if (complexity >= 2)
                items[index++] = new Item(fly_fishing_rod, stockSize);
            if (complexity >= 4)
                items[index++] = new Item(knife, stockSize);
            if (complexity >= 4)
                items[index++] = new Item(bowstring, stockSize);
            if (complexity >= 4)
                items[index++] = new Item(needle, stockSize);
            if (complexity >= 4)
                items[index++] = new Item(thread, stockSize);
            if (complexity >= 5)
                items[index++] = new Item(empty_vial, stockSize);
            if (complexity >= 2)
                items[index++] = new Item(feather, stockSize);
            if (complexity >= 2)
                items[index++] = new Item(antipoison, stockSize);
            if (complexity >= 6)
                items[index++] = new Item(toolkit, stockSize);
            /*for (int i = 0; i < 72; i++)
				items[i] = new Item(17630 + i, 1);
			for (int i = 0; i < 24; i++)
				items[72 + i] = new Item(17754 + i, 1);*/
            for (int i = 500; i < Integer.MAX_VALUE; i++) {
                if (ShopManager.getShops().containsKey(i))
                    continue;

                setShop(i);
                Shop shop = new Shop(null, getShop(), "Shop", new Item(DungeonConstants.RUSTY_COINS), items, true);
                shop.setDungeoneeringSplit(dungeoneeringSplit);
                ShopManager.getShops().put(getShop(), shop);
                break;
            }
        }
    }

    public void leaveParty(Player player, boolean logout, boolean animate) {
        if (dungeon != null)
            dungeon.exitDungeon(player, logout, animate);
        else {
            player.stopAll();
            player.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, DungeonConstants.FORM_PARTY_INTERFACE);
            remove(player, logout);
        }
    }

    public void remove(Player player, boolean logout) {
        synchronized (this) {
            team.remove(player);
            player.getDungManager().setParty(null);
            player.getDungManager().expireInvitation();
            player.getDungManager().refreshPartyDetailsComponents();
            player.message("You leave the party.");
            player.setShop(null);
            if (dungeon != null && team.size() == 0) {
                if (dungeon.hasLoadedNoRewardScreen() && logout) //destroy timer cant exist with a party member on anyway, team must be 0
                    dungeon.setDestroyTimer();
                else
                    dungeon.destroy();
            } else {
                for (Player p2 : team)
                    p2.message(player.getUsername() + " has left the party.");
                if (isLeader(player) && team.size() > 0)
                    setLeader(team.get(0));
                for (Player p2 : team)
                    refreshPartyDetails(p2);
            }
        }
    }

    public void refreshPartyDetails(Player player) {
        player.getDungManager().refreshPartyDetailsComponents();
        player.getDungManager().refreshNames();
        player.getDungManager().refreshComplexity();
        player.getDungManager().refreshFloor();
    }

    public void add(Player player) {
        synchronized (this) {
            for (Player p2 : team)
                p2.message(player.getUsername() + " has joined the party.");
            team.add(player);
            player.getDungManager().setParty(this);
            if (team.size() == 1) {
                setLeader(player);
                if (dungeon != null)
                    dungeon.endDestroyTimer();
            } else
                player.message("You join the party.");

            int lowestLevel = 99;
            for (Player member : team) {
                if (!member.getSkills().hasLevel(Skill.DUNGEONEERING, lowestLevel))
                    lowestLevel = member.getSkills().getMaxLevel(Skill.DUNGEONEERING);
            }
            if ((getFloor() * 2) - 1 > lowestLevel)
                setFloor((int) Math.ceil((lowestLevel + 1) / 2));

            for (Player p2 : team)
                refreshPartyDetails(p2);
        }
    }

    public boolean isLeader(Player player) {
        return player.getUsername().equals(leader);
    }

    public void setLeader(Player player) {
        leader = player.getUsername();
        if (team.size() > 1) {
            Player positionZero = team.get(0);
            team.set(0, player);
            team.remove(player);
            team.add(positionZero);
        }
        player.message("You have been set as the party leader.");
    }

    public void lockParty() {
        for (Player player : team) {
            player.stopAll();
            player.getMovement().lock();
        }
    }

    public void start() {
        generateShop();
        synchronized (this) {
            if (dungeon != null)
                return;
            dungeon = new DungeonManager(this);
        }
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
        for (Player player : team)
            player.getDungManager().refreshComplexity();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty > team.size())
            difficulty = team.size();
        this.difficulty = difficulty;
    }

    public int getFloor() {
        return floor;
    }

    public String getLeader() {
        return leader;
    }

    public Player getLeaderPlayer() {
        for (Player player : team)
            if (player.getUsername().equals(leader))
                return player;
        return null;
    }

    public Player getGateStonePlayer() {
        for (Player player : team)
            if (player.getInventory().contains(DungeonConstants.GROUP_GATESTONE))
                return player;
        return null;
    }

    public void setFloor(int floor) {
        this.floor = floor;
        for (Player player : team)
            player.getDungManager().refreshFloor();
    }

    public int getFloorType() {
        return DungeonUtils.getFloorType(floor);
    }

    public int getDungeoneeringLevel() {
        int level = 120;
        for (Player player : team) {
            int playerLevel = player.getSkills().getMaxLevel(Skill.DUNGEONEERING);
            if (playerLevel < level)
                level = playerLevel;
        }
        return level;
    }

    public double getLevelDifferencePenalty(Player player) {
        int average = getAverageCombatLevel();
        int cb = player.getSkills().getCombatLevel();
        double diff = Math.abs(cb - average);
        return (diff > 50 ? ((diff - 50) * 0.01) : 0);

    }

    public int getMaxLevelDiference() {
        if (team.size() <= 1)
            return 0;
        int maxLevel = 0;
        int minLevel = 138;
        for (Player player : team) {
            int level = player.getSkills().getCombatLevel();
            if (maxLevel < level)
                maxLevel = level;
            if (minLevel > level)
                minLevel = level;
        }
        return Math.abs(maxLevel - minLevel);
    }

    public DungeonManager getDungeon() {
        return dungeon;
    }

    public int getMaxFloor() {
        int floor = 1;
        for (Player player : team) {
            if (player.getDungManager().getMaxFloor() >= floor)
                floor = player.getDungManager().getMaxFloor();
        }
        return floor;
    }

    public int getMaxComplexity() {
        int complexity = 6;
        for (Player player : team) {
            if (player.getDungManager().getMaxComplexity() < complexity)
                complexity = player.getDungManager().getMaxComplexity();
        }
        return complexity;
    }

    public int getCombatLevel() {
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getCombatLevel();
        return team.size() == 0 ? 138 : level;
    }

    public int getAverageCombatLevel() {
        if (team.size() == 0)
            return 138;
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getCombatLevel();
        return level / team.size();
    }

    public int getDefenceLevel() {
        if (team.size() == 0)
            return 99;
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getMaxLevel(Skill.DEFENCE);
        return level / team.size();
    }

    public double getDificultyRatio() {
        if (difficulty > team.size())
            return 1;
        return 0.7 + (((double) difficulty / (double) team.size()) * 0.3);
    }

    public int getMaxLevel(Skill skill) {
        if (team.size() == 0)
            return 1;
        int level = 0;
        for (Player player : team) {
            int lvl = player.getSkills().getMaxLevel(skill);
            if (lvl > level)
                level = lvl;
        }
        return level;
    }

    public int getAttackLevel() {
        if (team.size() == 0)
            return 99;
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getMaxLevel(Skill.ATTACK);
        return level / team.size();
    }

    public int getMagicLevel() {
        if (team.size() == 0)
            return 99;
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getMaxLevel(Skill.MAGIC);
        return level / team.size();
    }

    public int getRangeLevel() {
        if (team.size() == 0)
            return 99;
        int level = 0;
        for (Player player : team)
            level += player.getSkills().getMaxLevel(Skill.RANGED);
        return level / team.size();
    }

    public CopyOnWriteArrayList<Player> getTeam() {
        return team;
    }

    public int getSize() {
        return size;
    }

    public int getIndex(Player player) {
        int index = 0;
        for (Player p2 : team) {
            if (p2 == player)
                return index;
            index++;
        }
        return 0;
    }

    public int getDifficulty() {
        if (difficulty > team.size())
            difficulty = team.size();
        return difficulty;
    }

    public boolean isGuideMode() {
        return guideMode || complexity <= 4;
    }

	/*
	 * dont use for dung itself
	 */

    public boolean getGuideMode() {
        return guideMode;
    }

    public void setGuideMode(boolean guideMode) {
        this.guideMode = guideMode;
    }

    public boolean isKeyShare() {
        return keyType == 1;
    }

    public void setKeyShare(boolean isKeyShare) {
        this.keyType = isKeyShare ? 1 : 2;
    }

    public int getKeyType() {
        return keyType;
    }

    public void refreshInterface() {
        for (Player member : getTeam()) {
            if (member != null) {
                for (int s = 26236; s < 26240; s++)
                    member.getPacketSender().sendString(s, "");
                member.getPacketSender().sendString(26235, leader);
                member.getPacketSender().sendString(26240, floor == -1 ? "0" : floor + "");
                member.getPacketSender().sendString(26241, complexity == -1 ? "0" : complexity + "");
                for (int i = 0; i < getTeam().size(); i++) {
                    Player p = getTeam().get(i);
                    if (p != null) {
                        if (p == getLeaderPlayer())
                            continue;
                        member.getPacketSender().sendString(26235 + i, p.getUsername());
                    }
                }
            }
        }
    }

    public void sendMessage(String message) {
        for (Player member : getTeam()) {
            if (member != null) {
                member.message(FontUtils.imageTags(535) + " " + message, 0x660000);
            }
        }
    }

    public void sendFrame(int frame, String string) {
        for (Player member : getTeam()) {
            if (member != null) {
                member.getPacketSender().sendString(frame, string);
            }
        }
    }

    public int getShop() {
        return shopId;
    }

    public void setShop(int shop) {
        this.shopId = shop;
    }
}
