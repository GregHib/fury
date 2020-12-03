package com.fury.game.content.skill.free.dungeoneering;

import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.input.impl.BuyDungeoneeringExp;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.TempKeys;

/**
 * Created by Greg on 07/12/2016.
 */
public enum DungeoneeringRewards {
    BONECRUSHER(18337, 34000, Skill.PRAYER, 21, "This item requires a Prayer", "level of 21 to purchase.", "If you have a bonecrusher in", "your inventory, dropped", "bones from monster kills will", "automatically be converted", "into Prayer XP."),
    HERBICIDE(19675, 34000, Skill.HERBLORE, 21, "Instead of receiving certain", "types of herb from monster", "drops, you may choose for", "this item to corrode and", "destroy them. This provides", "you with 2x the Herblore XP", "that you would have gained", "for cleaning them. (Does not", "apply to certed herb drops.)"),
    GEM_BAG(18338, 2000, Skill.CRAFTING, 25, "This item requires a Crafting", "level of 25. The gem bag can", "hold uncut gems of the", "following types: sapphire,", "emerald, ruby, diamond. The", "bag can hold a maximum of 100", "gems."),
    SCROLL_OF_LIFE(18336, 10000, Skill.FARMING, 25, "This item requires a Farming", "level of 25 to unlock. When", "unlocked, farmers who", "harvest patches (including", "dead ones) have a permanent", "10% chance of receiving their", "seeds back. There is also a", "5% chance of getting a tree", "seed back from a dead", "tree/stump. For herb seeds,", "the seed type returned may be", "random."),
    ARCANE_PULSE_NECKLACE(18333, 6500, Skill.MAGIC, 30, "This item can be worn by", "mages with a Magic level of 30", "or higher and provides +10", "magic attack and +5% magical", "damage."),
    TWISTED_BIRD_SKULL_NECKLACE(19886, 8500, Skill.PRAYER, 30, "This item restores 1 prayer", "point for each normal bone", "you bury (Requires 30", "prayer)."),
    //MAGICAL_BLASTBOX(19671, 40000, "This item can be charged with", "the runes required for bolt", "and blast spells in one worn", "slot."),
    COAL_BAG(18339, 4000, Skill.MINING, 35, "This item requires a Mining", "level of 35. The coal bag holds", "up to 27 pieces of coal. When", "smithing or superheating, coal", "in the bag will be used before", "the coal in your inventory."),
    SCROLL_OF_CLEANSING(19890, 20000, Skill.HERBLORE, 49, "Reading this scroll", "permanently unlocks the", "wasteless herblore ability:", "when concocting a potion,", "there is a chance you will", "complete the potion slightly", "quicker, and that you will save", "the secondary item completely."),
    LONGBOW_SIGHT(18330, 10000, Skill.RANGED, 45, "This item can be attached to a", "maple or magic longbow to", "increase its ranged abilities.", "(Requires 45 Ranged for", "maple, 55 for magic.)"),
    GRAVITE_RAPIER(18365, 40000, Skill.ATTACK, 55, "This item requires an Attack", "level of 45 to wield and will", "last 10 hours in combat before", "it requires recharging."),
    GRAVITE_LONGSWORD(18367, 40000, Skill.ATTACK, 55, "This item requires an Attack", "level of 45 to wield and will", "last 10 hours in combat before", "it requires recharging."),
    GRAVITE_2H_SWORD(18369, 40000, Skill.STRENGTH, 55, "This item requires an Attack", "level of 45 to wield and will", "last 10 hours in combat before", "it requires recharging."),//Or attack
    GRAVITE_STAFF(18371, 40000, Skill.MAGIC, 55, "This item requires a Magic", "level of 45 to wield and will", "last 10 hours in combat before", "it requires recharging."),
    GRAVITE_SHORTBOW(18373, 40000, Skill.RANGED, 55, "This item requires a Ranged", "level of 45 to wield and will", "last 10 hours in combat before", "it requires recharging."),
    LAW_STAFF(18342, 40000, Skill.MAGIC, 45, "This item requires a Magic", "level of 45 and an Attack level", "of 40 to wield. You can store", "up to 1000 law runes in the", "staff. When casting spells", "that ue law runes, there is a ", "1/10 chance that a law rune will", "not be used."),
    TOMB_OF_FROST(18346, 43000, Skill.MAGIC, 48, "This item requires a Magic", "level of 48 to wield. This item", "acts as an infinite number of", "water runes, and is carried in", "the offhand slot."),
    AMULET_OF_ZEALOTS(19892, 40000, Skill.PRAYER, 48, "This item, when wielded, adds", "+2 to each of your attack", "modifiers, with a -8 penalty to", "your prayer points. It also", "improves the effect of any", "prayer that boosts only one", "skill."),
    ARCANE_BLAST_NECKLACE(18334, 15000, Skill.MAGIC, 50, "This item can be worn by", "mages with a Magic level of 50", "or higher and provides +12", "magic attack and +10% magical", "damage."),
    SPIRIT_CAPE(19893, 45000, Skill.SUMMONING, 50, "This item, when wielded, gives", "+3 to each of your defence", "modifiers, with a reduction to", "the cost of any scroll abilities", "that your combat familiars", "have."),//+ 50 defence
    NATURE_STAFF(18341, 12000, Skill.MAGIC, 53, "This item requires a Magic", "level of 53 and an Attack level", "of 40 to wield. You can store", "up to 1000 nature runes in the", "staff. When casting spells", "that use nature runes, there", "is a 1/10 chance that a nature", "rune will not be used."),
    SCROLL_OF_EFFICIENCY(19670, 20000, Skill.SMITHING, 55, "Reading this scroll", "permanently unlocks the", "wasteless smithing ability:", "when smithing an item that", "requires three bars or more,", "there is a chance that you will", "retain one bar. The chance is", "50% for bronze items, 25%", "for iron, 20% for steel, 10%", "for mithril, 8% for adamant", "and 5% for rune."),
    ANTI_POISON_TOTEM(18340, 44000, Skill.HERBLORE, 70, "This item requires 60 Defence", "and 70 Herblore to wield.", "While wielding the totem, you", "cannot be poisoned. (The totem", "does not remove any existing", "poison effects.)"),
    SPLIT_DRAGONTOOTH_NECKLACE(19887, 17000, Skill.PRAYER, 60, "This item restores 1 prayer", "point for each normal bone", "you bury, and 2 prayer points", "for every big, baby dragon or", "wyvern bone (Requires 60", "prayer)."),//+ 60 defence
    RING_OF_VIGOUR(19669, 50000, Skill.ATTACK, 62, "This item provides a +3 bonus", "to your Strength, and reduces", "the cost of any weapon's", "special attack by 10%."),
    SCROLL_OF_RENEWAL(18343, 107000, Skill.PRAYER, 65, "Reading this scroll", "permanently unlocks Rapid", "Renewal, a level 65 prayer.", "This prayer, when activated,", "will restore life points at 5", "times the normal restore rate."),
    ARCANE_STREAM_NECKLATE(18335, 30500, Skill.MAGIC, 80, "This item can be worn by", "mages with a Magic level of 70", "or higher and provides +14", "magic attack and +15% magical", "damage."),
    //CELESTIAL_SURGEBOX(19889, 65000, "This item can be charged with", "the runes required for wave", "and surge spells in one worn", "slot."),
    MERCENARYS_GLOVES(18347, 48500, Skill.RANGED, 73, "This item requires a Ranged", "level of 73 to wear. When", "worn, these gloves provide a", "+13 ranged attack bonus."),
    SCROLL_OF_RIGOUR(18839, 140000, Skill.PRAYER, 74, "Reading this scroll", "permanently unlocks Rigour, a", "level 74 prayer. This prayer,", "when activated, will boost", "your Ranged by 20% and", "defences by 25%."),
    SCROLL_OF_AUGURY(18344, 153000, Skill.PRAYER, 77, "Reading this scroll", "permanently unlocks Augury, a", "level 77 prayer. This prayer,", "when activated, will boost", "your magical attacks by 20%", "and defence by 25%."),
    CHAOTIC_RAPIER(18349, 200000, Skill.ATTACK, 80, "This item requires an Attack", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging."),
    CHAOTIC_LONGSWORD(18351, 200000, Skill.ATTACK, 80, "This item requires an Attack", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging."),
    CHAOTIC_MAUL(18353, 200000, Skill.STRENGTH, 80, "This item requires an Attack", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging."),
    CHAOTIC_STAFF(18355, 200000, Skill.MAGIC, 80, "This item requires a Magic", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging."),
    CHAOTIC_CROSSBOW(18357, 200000, Skill.RANGED, 80, "This item requires a Ranged", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging."),
    CHAOTIC_KITESHIELD(18359, 200000, Skill.DEFENCE, 80, "This item requires a Defence", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging. It also", "reduces damage taken from", "ranged attacks."),
    EAGLE_EYE_KITESHIELD(18361, 200000, Skill.DEFENCE, 80, "This item requires a Defence", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging. It also", "reduces damage taken from", "magic attacks."),
    FARSEER_KITESHIELD(18363, 200000, Skill.DEFENCE, 80, "This item requires a Defence", "level of 80 to wield and will", "last 10 hours in combat before", "it needs recharging. It also", "reduces damage taken from", "melee attacks."),
    SNEAKERPEEPER_SPAWN(19894, 85000, Skill.SUMMONING, 80, "This is a pet stalker from", "Daemonheim."),
    DEMON_HORN_NECKLACE(19888, 35000, Skill.PRAYER, 90, "This item restores 1 prayer", "point for burying normal", "bones, 2 prayer points for", "burying big, baby dragon or", "wyvern bones, and 3 prayer", "points for burying dragon,", "ourg and frost draogn bones", "(Requires 90 prayer)."),
    DUNGEONEERING_EXPERIENCE(18348, 100, "Exchange 100 tokens for 100", "dungeoneering experience.", "(Not including game-mode", "multiplier.)");


    public static Item[] getItems() {
        Item[] items = new Item[values().length];
        for (int i = 0; i < values().length; i++)
            items[i] = new Item(values()[i].itemId, 1000);
        return items;
    }

    public static int[] getPrices() {
        int[] prices = new int[values().length];
        for (int i = 0; i < values().length; i++)
            prices[i] = values()[i].cost;
        return prices;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCost() {
        return cost;
    }

    public String[] getInfo() {
        return info;
    }

    public Skill getRequiredSkill() {
        return requiredSkill;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    int itemId;
    int cost;
    String[] info;
    Skill requiredSkill;
    int requiredLevel;

    DungeoneeringRewards(int itemId, int cost, String... info) {
        this.itemId = itemId;
        this.cost = cost;
        this.info = info;
    }

    DungeoneeringRewards(int itemId, int cost, Skill requiredSkill, int requiredLevel, String... info) {
        this.itemId = itemId;
        this.cost = cost;
        this.info = info;
        this.requiredSkill = requiredSkill;
        this.requiredLevel = requiredLevel;
    }

    public static final int REWARDS_ITEM_CONTAINER_INTERFACE = 613;

    public static void handleRewardsInterface(Player player, int slot, int id) {
        //Get & Check Selection
        if (slot > DungeoneeringRewards.values().length)
            return;
        DungeoneeringRewards reward = DungeoneeringRewards.values()[slot];
        if (reward == null || reward.getItemId() != id)
            return;

        //Set selection
        player.getTemporaryAttributes().put(TempKeys.DUNGEONEERING_REWARD_SELECTED, reward);

        //Send Info
        player.getPacketSender().sendString(617, Loader.getItem(reward.getItemId()).getName());
        player.getPacketSender().sendItemOnInterface(618, reward.getItemId(), 1);
        String[] info = reward.getInfo();
        for (int i = 0; i < 12; i++)
            player.getPacketSender().sendString(619 + i, i < info.length ? info[i] : "");
        player.getPacketSender().sendString(15280, "" + reward.getCost());
    }


    private static boolean hasRequirement(DungeoneeringRewards reward) {
        return reward.getRequiredSkill() != null && reward.getRequiredLevel() > 0;
    }

    public static void buy(Player player) {
        if (!player.getTimers().getClickDelay().elapsed(2000))
            return;

        Object temp = player.getTemporaryAttributes().get(TempKeys.DUNGEONEERING_REWARD_SELECTED);
        if (temp == null)
            return;

        DungeoneeringRewards reward = (DungeoneeringRewards) temp;

        if (!passedChecks(player, reward))
            return;


        if (reward == DUNGEONEERING_EXPERIENCE) {
            player.getPacketSender().sendEnterAmountPrompt("How many dungeoneering lamps would you like to buy?");
            player.setInputHandling(new BuyDungeoneeringExp());
            return;
        }
        //Charge
        player.getDungManager().addTokens(-reward.getCost());

        //Give Reward
        player.getInventory().add(new Item(reward.getItemId()));

        //Refresh
        refreshTokens(player);
        player.getTimers().getClickDelay().reset();
    }

    private static boolean passedChecks(Player player, DungeoneeringRewards reward) {
        if (hasRequirement(reward)) {
            Skill skill = reward.getRequiredSkill();
            int level = reward.getRequiredLevel();
            if (!player.getSkills().hasRequirement(skill, level, "buy this"))
                return false;
            if (!player.getSkills().hasRequirement(Skill.DUNGEONEERING, level, "buy this"))
                return false;
        }

        if (player.getInventory().getSpaces() <= 0) {
            player.getInventory().full();
            return false;
        }
        int tokens = player.getDungManager().getTokens();
        if (tokens - reward.getCost() < 0) {
            player.message("You do not have enough tokens to purchase this item.");
            return false;
        }

        if (reward == SCROLL_OF_AUGURY || reward == SCROLL_OF_RIGOUR || reward == SCROLL_OF_RENEWAL || reward == SCROLL_OF_CLEANSING || reward == SCROLL_OF_EFFICIENCY || reward == SCROLL_OF_LIFE)
            if (player.hasItem(reward.itemId)) {
                player.message("You've already got one of those. Go read it.");
                return false;
            } else {
                for (DungManager.UnlockScrolls scroll : DungManager.UnlockScrolls.values()) {
                    if (reward.itemId == scroll.getItemId()) {
                        if (player.getDungManager().getUnlockedScrolls()[scroll.ordinal()]) {
                            player.message("You have already unlocked this scrolls ability.");
                            return false;
                        }
                    }
                }
            }

        return true;
    }

    private static void clearInterface(Player player) {
        player.getPacketSender().sendString(617, "");
        player.getPacketSender().sendItemOnInterface(618, -1, 1);
        for (int i = 0; i < 12; i++)
            player.getPacketSender().sendString(619 + i, "");
        player.getPacketSender().sendString(15280, "");
    }

    public static void refreshTokens(Player player) {
        player.getPacketSender().sendString(15283, "" + player.getDungManager().getTokens());
    }

    public static void openInterface(Player player) {
        clearInterface(player);
        refreshTokens(player);
        player.getPacketSender().sendShopPrice(DungeoneeringRewards.getPrices(), 0);
        player.getPacketSender().sendItemContainer(DungeoneeringRewards.REWARDS_ITEM_CONTAINER_INTERFACE, DungeoneeringRewards.values().length, DungeoneeringRewards.getItems());
        player.getPacketSender().sendString(3901, "Daemonheim Rewards");
        player.getPacketSender().sendInterface(610);
        player.getTemporaryAttributes().remove(TempKeys.DUNGEONEERING_REWARD_SELECTED);
    }
}