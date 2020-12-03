package com.fury.game.content.skill.member.slayer;

import com.fury.game.content.dialogue.impl.ComingSoonNpcD;
import com.fury.game.content.dialogue.impl.skills.slayer.QuickTaskD;
import com.fury.game.content.dialogue.impl.skills.slayer.SlayerMasterD;
import com.fury.game.content.dialogue.impl.skills.slayer.SlayerMasterShopD;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public enum SlayerMaster {

    SPRIA(8462, 85, 1, new int[]{ 0, 0, 0 }, new int[]{ 15, 25 }, SlayerTask.BANSHEE, SlayerTask.BAT, SlayerTask.BEAR, SlayerTask.COW, SlayerTask.CAVE_BUG/*, SlayerTask.CAVE_SLIME*/, SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF, SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.ICEFIEND, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION, SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),
    TURAEL(8480, 3, 1, new int[]{ 0, 0, 0 }, new int[]{ 15, 25 }, SlayerTask.BANSHEE, SlayerTask.BAT, SlayerTask.BEAR, SlayerTask.COW, SlayerTask.CAVE_BUG/*, SlayerTask.CAVE_SLIME*/, SlayerTask.DWARF, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DWARF, SlayerTask.GHOST, SlayerTask.GOBLIN, SlayerTask.ICEFIEND, SlayerTask.MINOTAUR, SlayerTask.MONKEY, SlayerTask.SCORPION, SlayerTask.SKELETON, SlayerTask.SPIDER, SlayerTask.WOLF, SlayerTask.ZOMBIE),
    MAZCHNA(8481, 20, 1, new int[]{ 2, 5, 15 }, new int[]{ 20, 35 }, SlayerTask.BANSHEE, SlayerTask.BAT, SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER, SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.GROTWORM, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN, SlayerTask.ICE_WARRIOR, SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG, SlayerTask.SKELETON/*, SlayerTask.VAMPYRE*/, SlayerTask.WALL_BEAST, SlayerTask.WOLF, SlayerTask.ZOMBIE),
    ACHTRYN(8465, 20, 1, new int[]{ 2, 5, 15 }, new int[]{ 20, 35 }, SlayerTask.BANSHEE, SlayerTask.BAT, SlayerTask.CATABLEPON, SlayerTask.CAVE_CRAWLER, SlayerTask.COCKATRICE, SlayerTask.CRAWLING_HAND, SlayerTask.DESERT_LIZARD, SlayerTask.DOG, SlayerTask.FLESH_CRAWLER, SlayerTask.GHOUL, SlayerTask.GHOST, SlayerTask.GROTWORM, SlayerTask.HILL_GIANT, SlayerTask.HOBGOBLIN, SlayerTask.ICE_WARRIOR, SlayerTask.KALPHITE, SlayerTask.PYREFIEND, SlayerTask.ROCKSLUG, SlayerTask.SKELETON/*, SlayerTask.VAMPYRE*/, SlayerTask.WALL_BEAST, SlayerTask.WOLF, SlayerTask.ZOMBIE),
    VANNAKA(1597, 40, 1, new int[]{ 4, 20, 60 }, new int[]{ 25, 45 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ANKOU, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BRINE_RAT, SlayerTask.COCKATRICE, SlayerTask.CROCODILE, SlayerTask.DUST_DEVIL, SlayerTask.EARTH_WARRIOR, SlayerTask.GHOUL, SlayerTask.GREEN_DRAGON, SlayerTask.GROTWORM, SlayerTask.HARPIE_BUG_SWARM, SlayerTask.HILL_GIANT, SlayerTask.ICE_GIANT, SlayerTask.ICE_WARRIOR, SlayerTask.INFERNAL_MAGE, SlayerTask.JELLY/*, SlayerTask.JUNGLE_HORROR*/, SlayerTask.LESSER_DEMON/*, SlayerTask.MOLANISK*/, SlayerTask.MOSS_GIANT, SlayerTask.OGRE/*, SlayerTask.OTHERWORLDLY_BEING*/, SlayerTask.PYREFIEND, SlayerTask.SHADE/*, SlayerTask.SHADOW_WARRIOR*/, SlayerTask.TUROTH/*, SlayerTask.VAMPYRE*/, SlayerTask.WEREWOLF),
    CHAELDAR(1598, 70, 1, new int[]{ 10, 50, 100 }, new int[]{ 30, 55 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.BRINE_RAT, SlayerTask.BRONZE_DRAGON, SlayerTask.CAVE_CRAWLER, SlayerTask.CAVE_HORROR, SlayerTask.CRAWLING_HAND, SlayerTask.DAGANNOTH, SlayerTask.DUST_DEVIL/*, SlayerTask.ELF_WARRIOR, SlayerTask.FEVER_SPIDER*/, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GARGOYLE, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HARPIE_BUG_SWARM, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.INFERNAL_MAGE, SlayerTask.JELLY/*, SlayerTask.JUNGLE_HORROR*/, SlayerTask.KALPHITE, SlayerTask.KALPHITE, SlayerTask.KURASK, SlayerTask.LESSER_DEMON/*, SlayerTask.ZYGOMITE, SlayerTask.SHADOW_WARRIOR*/, SlayerTask.TUROTH/*, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE*/),
    SUMONA(7780, 85, 35, new int[]{ 12, 60, 180 }, new int[]{ 35, 65 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BANSHEE, SlayerTask.BASILISK, SlayerTask.BLACK_DEMON, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.CAVE_CRAWLER, SlayerTask.CAVE_HORROR, SlayerTask.DAGANNOTH, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL/*, SlayerTask.ELF_WARRIOR*/, SlayerTask.FUNGAL_MAGE, SlayerTask.GARGOYLE, SlayerTask.GREATER_DEMON, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.KURASK, SlayerTask.JADINKO, SlayerTask.NECHRYAEL, SlayerTask.RED_DRAGON/*, SlayerTask.SCABARITE*/, SlayerTask.SPIRITUAL_MAGE, SlayerTask.SPIRITUAL_WARRIOR/*, SlayerTask.TERROR_DOG*//*, SlayerTask.TROLL*/, SlayerTask.TUROTH/*, SlayerTask.VYREWATCH*/),
    DURADEL(8466, 100, 50, new int[]{ 15, 75, 225 }, new int[]{ 40, 75 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GANODERMIC, SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.ICE_STRYKEWYRM, SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON, SlayerTask.JADINKO, SlayerTask.NECHRYAEL/*, SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN*/, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON/*, SlayerTask.SUQAH, SlayerTask.VYREWATCH, SlayerTask.WARPED_TERRORBIRD*/, SlayerTask.WATERFIEND),
    LAPALOK(8467, 100, 50, new int[]{ 15, 75, 225 }, new int[]{ 45, 85 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ABYSSAL_DEMON, SlayerTask.AQUANITE, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.FUNGAL_MAGE, SlayerTask.GANODERMIC, SlayerTask.GARGOYLE, SlayerTask.GORAK, SlayerTask.GREATER_DEMON, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.ICE_STRYKEWYRM, SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.MITHRIL_DRAGON, SlayerTask.JADINKO, SlayerTask.NECHRYAEL/*, SlayerTask.SCABARITE, SlayerTask.SKELETAL_WYVERN*/, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON/*, SlayerTask.SUQAH, SlayerTask.VYREWATCH, SlayerTask.WARPED_TERRORBIRD*/, SlayerTask.WATERFIEND),
    KURADAL(9085, 110, 75, new int[]{ 18, 90, 270 }, new int[]{ 50, 95 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.GANODERMIC, SlayerTask.GARGOYLE, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.LIVING_ROCK, SlayerTask.MITHRIL_DRAGON, SlayerTask.JADINKO, SlayerTask.NECHRYAEL/*, SlayerTask.SKELETAL_WYVERN*/, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON/*, SlayerTask.SUQAH, SlayerTask.TERROR_DOG*/, SlayerTask.TZHAAR, SlayerTask.TZHAAR/*, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE*/, SlayerTask.WATERFIEND),
    RAPTOR(13955, 115, 80, new int[]{ 20, 100, 280 }, new int[]{ 55, 100 }, SlayerTask.ABERRANT_SPECTRE, SlayerTask.ABYSSAL_DEMON, SlayerTask.BLACK_DEMON, SlayerTask.BLACK_DRAGON, SlayerTask.BLOODVELD, SlayerTask.BLUE_DRAGON, SlayerTask.DAGANNOTH, SlayerTask.DARK_BEAST, SlayerTask.DESERT_STRYKEWYRM, SlayerTask.DUST_DEVIL, SlayerTask.FIRE_GIANT, SlayerTask.GANODERMIC, SlayerTask.GARGOYLE, SlayerTask.GRIFOLAPINE, SlayerTask.GRIFOLAROO, SlayerTask.GROTWORM, SlayerTask.HELLHOUND, SlayerTask.IRON_DRAGON, SlayerTask.JUNGLE_STRYKEWYRM, SlayerTask.KALPHITE, SlayerTask.LIVING_ROCK, SlayerTask.MITHRIL_DRAGON, SlayerTask.JADINKO, SlayerTask.NECHRYAEL/*, SlayerTask.SKELETAL_WYVERN*/, SlayerTask.SPIRITUAL_MAGE, SlayerTask.STEEL_DRAGON/*, SlayerTask.SUQAH, SlayerTask.TERROR_DOG*/, SlayerTask.TZHAAR, SlayerTask.TZHAAR/*, SlayerTask.VYREWATCH, SlayerTask.WARPED_TORTOISE*/, SlayerTask.WATERFIEND),
    DEATH(14386, 120, 85, new int[]{ 25, 115, 300 }, new int[]{ 25, 40 }, SlayerTask.GENERAL_GRAARDOR, SlayerTask.KREEARRA, SlayerTask.COMMANDER_ZILYANA, SlayerTask.COMMANDER_ZILYANA, SlayerTask.KRIL_TSUTSAROTH, SlayerTask.KALPHITE_QUEEN, SlayerTask.DAGANNOTH_PRIME, SlayerTask.DAGANNOTH_REX, SlayerTask.DAGANNOTH_SUPREME, SlayerTask.TORMENTED_DEMON, SlayerTask.CORPOREAL_BEAST, SlayerTask.NEX),
    DEATHWILD(14385, 120, 3, new int[]{ 25, 115, 300 }, new int[]{ 25, 40 }, SlayerTask.GREEN_DRAGON, SlayerTask.REVENANT, SlayerTask.GREATER_DEMON, SlayerTask.LESSER_DEMON),
    ;

    private SlayerTask[] task;
    private int[] tasksRange, pointsRange;
    private int requiredCombatLevel, requiredSlayerLevel, npcId;

    SlayerMaster(int npcId, int requiredCombatLevel, int requiredSlayerLevel, int[] pointsRange, int[] tasksRange, SlayerTask... task) {
        this.npcId = npcId;
        this.requiredCombatLevel = requiredCombatLevel;
        this.requiredSlayerLevel = requiredSlayerLevel;
        this.pointsRange = pointsRange;
        this.tasksRange = tasksRange;
        this.task = task;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getRequiredCombatLevel() {
        return requiredCombatLevel;
    }

    public int getRequiredSlayerLevel() {
        return requiredSlayerLevel;
    }

    public SlayerTask[] getTask() {
        return task;
    }

    public int[] getTasksRange() {
        return tasksRange;
    }

    public int[] getPointsRange() {
        return pointsRange;
    }

    public static boolean startInteractionForId(Player player, int npcId, int option) {
        if(npcId == 14385) {
            player.getDialogueManager().startDialogue(new ComingSoonNpcD(), npcId, SlayerMaster.DEATH);
            return true;
        }
        for (SlayerMaster master : SlayerMaster.values()) {
            if (master.getNpcId() == npcId) {
                if (option == 1)
                    player.getDialogueManager().startDialogue(new SlayerMasterD(), npcId, master);
                else if (option == 2)
                    player.getDialogueManager().startDialogue(new QuickTaskD(), master);
                else if (option == 3)
                    player.getDialogueManager().startDialogue(new SlayerMasterShopD(), npcId, master);
                else if (option == 4)
                    player.getSlayerManager().sendSlayerInterface(SlayerManager.BUY_INTERFACE);
                return true;
            }
        }
        return false;
    }

    public int getPrice() {
        return requiredCombatLevel * 1000;
    }
}
