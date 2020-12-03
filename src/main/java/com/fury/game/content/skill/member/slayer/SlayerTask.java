package com.fury.game.content.skill.member.slayer;

import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.io.Serializable;

public enum SlayerTask implements Serializable {// 79 matches out of 117
//    MIGHTY_BANSHEE(37, new String[]{ "Banshees are fearsome creatures with a mighty scream.", " You need something to cover your ears", "Beware of their scream.", "Banshees are found in the Slayer Tower." }),
    BANSHEE(15, "N/A", new String[] { "Banshees are fearsome creatures with a mighty scream.", " You need something to cover your ears", "Beware of their scream.", "Banshees are found in the Slayer Tower." }, new Position(3441, 3542)/*, MIGHTY_BANSEE*/),
    BAT(1, "N/A", new String[] {}, new Position(2909, 9831)),
    AVIANSIE(1, 0.8, "N/A", new String[] {}),//Godwars
    CHICKEN(1, "N/A", new String[] {}, new Position(3234, 3295)),
    DUCK(1, "N/A", new String[] {}, new Position(3245, 3236)),
    BEAR(1, "N/A", new String[] {}, new Position(2707, 3336)),
    CAVE_BUG(7, "N/A", new String[] {}, new Position(3152, 9574)),
    CAVE_SLIME(17, "N/A", new String[] {}, new Position(3163, 9588)),
    COW(1, "N/A", new String[] {}, new Position(3256, 3266)),
    CRAWLING_HAND(5, "N/A", new String[] {}, new Position(3412, 3539)),
    DWARF(1, "N/A", new String[] {}, new Position(3026, 9815)),
    LIZARD(22, "N/A", null, new Position(3346, 2935)),
    DESERT_LIZARD(22, "N/A", new String[] {}, new Position(3346, 2935), LIZARD),
    REVENANT(1, 0.65, "easy", new String[] {}),//Wilderness
    GHOST(1, "N/A", new String[] {}, new Position(2914, 9819), REVENANT),
    GOBLIN(1, "N/A", new String[] {}, new Position(3253, 3239)),
    ICEFIEND(1, "N/A", new String[] {}, new Position(3014, 3473)),//Godwars
    MINOTAUR(1, "N/A", new String[] {}, new Position(1871, 5236)),
    MONKEY(1, "N/A", new String[] {}, new Position(2602, 3275)),
    SCORPION(1, "N/A", new String[] {}, new Position(3299, 3284)),
    SKELETON(1, "N/A", new String[] {}, new Position(3135, 9874)),
    SPIDER(1, "N/A", new String[] {}, new Position(3179, 3222)),
    WOLF(1, "N/A", new String[] {}, new Position(2848, 3497)),
    ZOMBIE(1, "N/A", new String[] {}, new Position(2844, 9763)),
    CATABLEPON(1, "N/A", new String[] {}, new Position(2132, 5280)),
    CAVE_CRAWLER(10, "N/A", new String[] {}, new Position(2790, 9996)),
    DOG(1, "N/A", new String[] {}, new Position(2635, 3309)),
    FLESH_CRAWLER(1, "N/A", new String[] {}, new Position(2019, 5236)),
    HOBGOBLIN(1, "N/A", new String[] {}, new Position(3023, 9581)),
    KALPHITE(1, "N/A", new String[] {}, new Position(3501, 9521, 2)),
    ROCKSLUG(20, "N/A", new String[] {}, new Position(2794, 10016)),
    ROCK_SLUG(20, "N/A", new String[] {}, new Position(2794, 10016), ROCKSLUG),
    HOLE_IN_THE_WALL(35, "N/A", new String[] {}, new Position(3168, 9556)),
    WALL_BEAST(35, "N/A", new String[] {}, new Position(3168, 9556), HOLE_IN_THE_WALL),
    ABERRANT_SPECTRE(60, "N/A", new String[] {}, new Position(3421, 3550, 1)),
    ANKOU(1, "N/A", new String[] {}, new Position(2334, 5227)),
    BASILISK(40, "N/A", new String[] {}, new Position(2743, 10009)),
    BLOODVELD(50, "N/A", new String[] {}, new Position(3417, 3565, 1)),
    BRINE_RAT(47, "N/A", new String[] {}, new Position(2714, 10133)),
    COCKATRICE(25, "N/A", new String[] {}, new Position(2790, 10035)),
    CROCODILE(1, 0.5, "N/A", new String[] {}, new Position(3366, 3103)),
    DUST_DEVIL(65, "N/A", new String[] {}, new Position(3207, 9379)),
    EARTH_WARRIOR(1, 0.5, "N/A", new String[] {}, new Position(3149, 5531)),
    GHOUL(1, "N/A", new String[] {}, new Position(3418, 3512)),
    GREEN_DRAGON(1, "hard", new String[] {}),//Wilderness
    GROTWORM(1, 0.64, "N/A", new String[] {}, new Position(1188, 6500)),
    HARPIE_BUG_SWARM(33, "N/A", new String[] {}, new Position(2867, 3111)),
    CYCLOPS(1, "N/A", new String[] {}),
    HILL_GIANT(1, "N/A", new String[] {}, new Position(3119, 9834), CYCLOPS),
    ICE_GIANT(1, "N/A", new String[] {}, new Position(3224, 5555)),
    ICE_WARRIOR(1, "N/A", new String[] {}, new Position(3220, 5445)),
    INFERNAL_MAGE(45, "N/A", new String[] {}, new Position(3439, 3562, 1)),
    JELLY(52, "N/A", new String[] {}, new Position(3274, 5516)),
//    JUNGLE_HORROR(1, 0.64, new String[] {}),
    LESSER_DEMON(1, "hard", new String[] {}, new Position(2932, 9783)),//Wilderness
//    MOLANISK(39, new String[] {}),
    MOSS_GIANT(1, "N/A", new String[] {}, new Position(3236, 5560)),
    OGRE(1, "N/A", new String[] {}, new Position(2495, 3096)),//Godwars
//    OTHERWORLDLY_BEING(1, new String[] {}),
    PYREFIEND(30, "N/A", new String[] {}, new Position(2760, 10005)),
    SHADE(1, "N/A", new String[] {}, new Position(2360, 5212)),
//    SHADOW_WARRIOR(1, new String[] {}),
    TUROTH(55, "N/A", new String[] {}, new Position(2723, 10004)),
//    VAMPYRE(1, new String[] {}),
    WEREWOLF(1, "N/A", new String[] {}, new Position(3495, 3489)),
    BLUE_DRAGON(1, 0.8, "N/A", new String[] {}, new Position(2892, 9790)),
    BRONZE_DRAGON(1, 0.35, "N/A", new String[] {}, new Position(3188, 5562)),
    CAVE_HORROR(58, "N/A", new String[] {}, new Position(3747, 9374)),
    DAGANNOTH(1, 0.96, "N/A", new String[] {}, new Position(2442, 10146)),//TODO animations from here onwards
//    ELF_WARRIOR(1, 0.49, new String[] {}),
//    FEVER_SPIDER(42, new String[] {}),
    FIRE_GIANT(1, "N/A", new String[] {}, new Position(3216, 5518)),
    FUNGAL_MAGE(1, "N/A", new String[] {}),
    GARGOYLE(75, "N/A", new String[] {}, new Position(3426, 3539, 2)),
    GRIFOLAPINE(88, 0.316, "N/A", new String[] {}, new Position(4620, 5458, 3)),
    GRIFOLAROO(82, 0.316, "N/A", new String[] {}, new Position(4620, 5458, 3)),
    JUNGLE_STRYKEWYRM(73, "N/A", new String[] {}, new Position(2462, 2904)),
    KURASK(70, "N/A", new String[] {}, new Position(2712, 9991)),
//    FUNGI(57, 0.81, new String[] {}),
//    ZYGOMITE(57, new String[] {}, FUNGI),
//    VYRELORD(31, 0.68, new String[] {}),
//    VYRELADY(31, 0.68, new String[] {}),
//    VYREWATCH(31, 0.68, new String[] {}, VYRELORD, VYRELADY),
//    WARPED_TORTOISE(56, new String[] {}),
    ABYSSAL_DEMON(85, "N/A", new String[] {}, new Position(3411, 3553, 2)),
    AQUANITE(78, 0.96, "N/A", new String[] {}, new Position(1885, 4378)),
    BLACK_DEMON(1, "N/A", new String[] {}, new Position(2879, 9769)),
    DESERT_STRYKEWYRM(77, 0.64, "N/A", new String[] {}, new Position(3372, 3152)),
    GREATER_DEMON(1, "hard", new String[] {}),//Wilderness
    HELLHOUND(1, 0.92, "N/A", new String[] {}, new Position(2851, 9834)),
    IRON_DRAGON(1, 0.468, "N/A", new String[] {}, new Position(2708, 9474)),
    MUTATED_JADINKO_MALE(91, 0.88, "N/A", new String[] {}, new Position(2948, 2955)),
    MUTATED_JADINKO_GAURD(86, 0.88, "N/A", new String[] {}, new Position(2948, 2955)),
    MUTATED_JADINKO_BABY(80, 0.88, "N/A", new String[] {}, new Position(2948, 2955)),
    JADINKO(80, 0.88, "N/A", new String[] {}, MUTATED_JADINKO_MALE, MUTATED_JADINKO_GAURD, MUTATED_JADINKO_BABY),
    NECHRYAEL(80, 0.88, "N/A", new String[] {}, new Position(3446, 3555, 2)),
    RED_DRAGON(1, 0.43, "N/A", new String[] {}, new Position(2689, 9507)),
//    LOCUST(1, 0.4, new String[] {}),
//    SCABARAS(1, 0.4, new String[] {}),
//    SCARAB(1, 0.4, new String[] {}),
//    SCABARITE(1, 0.4, new String[] {}, LOCUST, SCABARAS, SCARAB),
    SPIRITUAL_MAGE(83, 0.96, "N/A", new String[] {}),//Godwars
    SPIRITUAL_WARRIOR(68, "N/A", new String[] {}),//Godwars
//    TERROR_DOG(40, 0.28, new String[] {}),
//    ROCK(1, 1.033, new String[] {}),
//    TROLL(1, 1.033, new String[] {}, ROCK),
    BLACK_DRAGON(1, 0.36, "N/A", new String[] {}, new Position(2817, 9829)),
    DARK_BEAST(90, "N/A", new String[] {}, new Position(1993, 4649)),
    GANODERMIC(95, 0.356, "N/A", new String[] {}, new Position(4651, 5388)),
    GORAK(1, 0.4, "N/A", new String[] {}),//Godwars
    ICE_STRYKEWYRM(93, 0.64, "N/A", new String[] {}, new Position(3425, 5665)),
    MITHRIL_DRAGON(1, 0.136, "N/A", new String[] {}, new Position(1772, 5349, 1)),
//    SKELETAL_WYVERN(72, 0.36, new String[] {}),
    STEEL_DRAGON(1, 0.432, "N/A", new String[] {}, new Position(2708, 9474)),
//    SUQAH(1, 0.4, "N/A", new String[] {}),
//    WARPED_TERRORBIRD(56, "N/A", new String[] {}),
    WATERFIEND(1, "N/A", new String[] {}, new Position(1738, 5341)),
    LIVING_ROCK(75, 0.74, "N/A", new String[] {}, new Position(3651, 5122)),
    TZHAAR(1, 0.44, "N/A", new String[] {}, new Position(2451, 5166)),
    GENERAL_GRAARDOR(85, "N/A", new String[] {}),
    KREEARRA(85, "N/A", new String[] {}),
    COMMANDER_ZILYANA(85, "N/A", new String[] {}),
    KRIL_TSUTSAROTH(85, "N/A", new String[] {}),
    KALPHITE_QUEEN(85, "N/A", new String[] {}, new Position(3507, 9493)),
    CORPOREAL_BEAST(92, 0.25, "N/A", new String[] {}, new Position(2885, 4374)),
    NEX(96, 0.25, "N/A", new String[] {}, new Position(2903, 5204)),
    DAGANNOTH_PRIME(85, "N/A", new String[] {}, new Position(1912, 4367)),
    DAGANNOTH_REX(85, "N/A", new String[] {}, new Position(1912, 4367)),
    DAGANNOTH_SUPREME(85, "N/A", new String[] {}, new Position(1912, 4367)),
    TORMENTED_DEMON(85, "N/A", new String[] {}, new Position(2569, 5735)),
    ;

    private String[] tips;
    private String wildernessDifficulty;
    private double taskFactor;
    private SlayerTask[] alternatives;
    private int levelRequired;
    private Position location;

    SlayerTask(int levelRequired, double taskFactor, String wildernessDifficulty, String[] tips, SlayerTask... alternatives) {
        this.levelRequired = levelRequired;
        this.taskFactor = taskFactor;
        this.tips = tips;
        this.alternatives = alternatives;
        this.wildernessDifficulty = wildernessDifficulty;
    }

    SlayerTask(int levelRequired, String wildernessDifficulty, String[] tips, SlayerTask... alternatives) {
        this(levelRequired, 1, wildernessDifficulty, tips, alternatives);
    }

    SlayerTask(int levelRequired, double taskFactor, String wildernessDifficulty, String[] tips, Position location, SlayerTask... alternatives) {
        this.levelRequired = levelRequired;
        this.taskFactor = taskFactor;
        this.tips = tips;
        this.alternatives = alternatives;
        this.location = location;
        this.wildernessDifficulty = wildernessDifficulty;
    }

    SlayerTask(int levelRequired, String wildernessDifficulty, String[] tips, Position position, SlayerTask... alternatives) {
        this(levelRequired, 1, wildernessDifficulty, tips, position, alternatives);
    }

    public String[] getTips() {
        return tips;
    }

    public SlayerTask[] getAlternatives() {
        return alternatives;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public String getName() {
        return Misc.formatPlayerNameForDisplay(toString());
    }

    public double getTaskFactor() {
        return taskFactor;
    }

    public Position getLocation() {
        return location;
    }

    public String getWildernessDifficulty() {
        return wildernessDifficulty;
    }


    @Override
    public String toString() {
        return name();
    }
}
