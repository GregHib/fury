package com.fury.game.content.skill.free.dungeoneering;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.rooms.BossRoom;
import com.fury.game.content.skill.free.dungeoneering.rooms.NormalRoom;
import com.fury.game.content.skill.free.dungeoneering.rooms.RoomEvent;
import com.fury.game.content.skill.free.dungeoneering.rooms.StartRoom;
import com.fury.game.content.skill.free.smithing.SmeltingData;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 04/07/2016.
 */
public class DungeonConstants {

    public static final int SHOP_INTERFACE_ID = 600;
    public static final int TOOLS_ITEM_CHILD_ID = 605;
    public static final int RESOURCE_ITEM_CHILD_ID = 603;
    public static final int FORM_PARTY_INTERFACE = 27224;
    public static final int PARTY_INTERFACE = 26224;

    public static final int[] MIN_CRIT_PATH =
            {5, 10, 19, 1}, MAX_CRIT_PATH =
            {7, 13, 23, 3};//Small used to be 5, 7

    public static final Position OUTSIDE = new Position(3460, 3720, 1);

    public static final int ROTATIONS_COUNT = 4;

    public static final Item TINDERBOX = new Item(17678);

    public static final int SMUGGLER = 11226;

    public static final int[] LADDERS = new int[]
            {49696, 49698, 49700, 53748, 55484};

    public static final int FLOORS_COUNT = 60;

    public static final Item GROUP_GATESTONE = new Item(18829), GATESTONE = new Item(17489);

    /*
     * objects handling
     */
    public static final int[] DUNGEON_DOORS = new int[]
            {50342, 50343, 50344, 53948, 55762};
    public static final int[] DUNGEON_BOSS_DOORS = new int[]
            {50350, 50351, 50352, 53950, 55764};

    public static final int[] DUNGEON_GUARDIAN_DOORS =
            {50346, 50347, 50348, 53949, 55763};

    /*
     * novite ores
     */
    public static final int[] MINING_RESOURCES =
            {49806, 49786, 49766, 53771, 55514};

    /*
     * Tangle gum tree
     */
    public static final int[] WOODCUTTING_RESOURCES =
            {49745, 49725, 49705, 53751, 55494};

    /*
     * Salve nettles
     */
    public static final int[] FARMING_RESOURCES =
            {49866, 49846, 49826, 53791, 55534};

    public static final int[] THIEF_CHEST_LOCKED =
            {49345, 49346, 49347, 54407, 32462};
    public static final int[] THIEF_CHEST_OPEN =
            {49348, 49349, 49350, 54408, 32679};
    public static final double[] THIEF_CHEST_XP =
            {25.5, 57, 115, 209, 331.5, 485, 661.5, 876, 1118, 1410.5};

    public static final int RUSTY_COINS = 18201, BANANA = 18199;

    public static final int[] CHARMS =
            {18017, 18018, 18019, 18020};

    public static final int[] ARROWS =
            {16427, 16432, 16437, 16442, 16447, 16452, 16457, 16462, 16467, 16472, 16477};

    public static final int[] RUNES =
            {17780, 17781, 17782, 17783, 17784, 17785, 17786, 17787, 17788, 17789, 17790, 17791, 17792, 17793};

    public static final int RUNE_ESSENCE = 17776, FEATHER = 17796;

    public static final int[] DUNGEON_KEY_DOORS =
            {50353, 50417, 50481, 53884, 55675};

    public static enum SkillDoors {
        RUNED_DOOR(Skill.RUNECRAFTING, new int[]
                {50278, 50279, 50280, 53953, 55741}, 791, 186, -1, -1, -1, -1, "You imbue the door with the wrong type of rune energy, and it reacts explosively."),
        BARRED_DOOR(Skill.STRENGTH, new int[]
                {50272, 50273, 50274, 53951, 55739}, new int[]
                {50275, 50276, 50277, 53952, 55740}, -1, -1, -1, -1, -1, -1, "You pull a muscle while attempting to move the plank."),
        PILE_OF_ROCKS(Skill.MINING, new int[]
                {50305, 50306, 50307, 53962, 55750}, -1, -1, 13559, -1, -1, 2420, "You fail to mine the obstruction, and are harmed by falling debris."),
        FLAMMABLE_DEBRIS(Skill.FIREMAKING, new int[]
                {50314, 50315, 50316, 53965, 55753}, 16700, -1, 13562, 13563, -1, -1, "The pile of debris fails to ignite. The same cannot be said for your clothes."),
        MAGICAL_BARRIER(Skill.MAGIC, new int[]
                {50329, 50330, 50331, 53970, 55758}, 4411, -1, 13551, 13550, -1, -1, "You fail to dispel the barrier and take a surge of magical energy to the face."),
        DARK_SPIRIT(Skill.PRAYER, new int[]
                {50332, 50333, 50334, 53971, 55759}, 645, -1, 13557, 13556, -1, -1, "The gods snub your prayer, and the dark spirit attacks you."),
        WOODEN_BARRICADE(Skill.WOODCUTTING, new int[]
                {50317, 50318, 50319, 53966, 55754}, new int[]
                {50320, 50321, 50322, 53967, 55755}, -1, -1, 13582, -1, -1, -1, "You swing the axe against the grain and are showered with sharp splinters of wood."),
        BROKEN_KEY_DOOR(Skill.SMITHING, new int[]
                {50308, 50309, 50310, 53963, 55751}, new int[]
                {50311, 50312, 50313, 53964, 55752}, 13759, -1, -1, -1, -1, -1, "You hit your hand with the hammer. Needless to say, the key is still broken."),
        BROKEN_PULLEY_DOOR(Skill.CRAFTING, new int[]
                {50299, 50300, 50301, 53960, 55748}, new int[]
                {50302, 50303, 50304, 53961, 55749}, 13547, -1, -1, -1, -1, -1, "The rope snaps again as you attempt to fix it, and you crush your hands in the mechanism."),
        LOCKED_DOOR(Skill.AGILITY, new int[]
                {-1, 50288, 50289, 53956, 55744}, new int[]
                {50290, 50291, 50292, 53957, 55745}, 14550, -1, -1, -1, -1, -1, "You miss the chain, and set off the trap."),
        PADLOCKED_DOOR(Skill.THIEVING, new int[]
                {-1, 50294, 50295, 53958, 55746}, new int[]
                {50296, 50297, 50298, 53959, 55747}, 14568, -1, -1, -1, 14569, -1, "You set off a booby trap inside the lock, and fail to pick it."),
        RAMOKEE_EXILE(Skill.SUMMONING, new int[]
                {-1, 50327, 50328, 53969, 55757}, 725, 1207, -1, -1, -1, -1, "You fail to dismiss the rogue familiar, and it punches you in anger."),
        LIQUID_LOCK_DOOR(Skill.HERBLORE, new int[]
                {-1, 50336, 50337, 53972, 55760}, new int[]
                {50338, 50339, 50340, 53973, 55761}, 14568, -1, -1, -1, -1, -1, "You incorrectly mix the ingredients, making it explode."),
        VINE_COVERED_DOOR(Skill.FARMING, new int[]
                {-1, 50324, 50325, 53968, 55756}, 2275, -1, 13572, -1, -1, -1, "You hurt your hands on the vicious thorns covering the vines."),
        COLLAPSING_DOORFRAME(Skill.CONSTRUCTION, new int[]
                {-1, 50282, 50283, 53954, 55742}, new int[]
                {50284, 50285, 50286, 53955, 55743}, 14566, -1, -1, -1, 14567, -1, "You dislodge some debris while attempting to fix the door, and it falls on you.");

        private SkillDoors(Skill skill, int[] closedThemeObjects, int openAnim, int openGfx, int openObjectAnim, int failObjectAnim, int failAnim, int failGfx, String failMessage) {
            this(skill, closedThemeObjects, null, openAnim, openGfx, openObjectAnim, failObjectAnim, failAnim, failGfx, failMessage);
        }

        private Skill skill;
        private int openAnim, openGfx, openObjectAnim, failObjectAnim, failAnim, failGfx;
        private int[] closedThemeObjects, openThemeObjects;
        private String failMessage;

        /*
         * set openThemeObjects if u want it to disappear after open
         */
        private SkillDoors(Skill skill, int[] closedThemeObjects, int[] openThemeObjects, int openAnim, int openGfx, int openObjectAnim, int failObjectAnim, int failAnim, int failGfx, String failMessage) {
            this.skill = skill;
            this.closedThemeObjects = closedThemeObjects;
            this.openThemeObjects = openThemeObjects;
            this.failMessage = failMessage;
            this.openAnim = openAnim;
            this.openGfx = openGfx;
            this.openObjectAnim = openObjectAnim;
            this.failObjectAnim = failObjectAnim;
            this.failGfx = failGfx;
            this.failAnim = failAnim;
        }

        public Skill getSkill() {
            return skill;
        }

        public int getFailGfx() {
            return failGfx;
        }

        public int getFailAnim() {
            return failAnim;
        }

        public int getFailObjectAnim() {
            return failObjectAnim;
        }

        public int getOpenObjectAnim() {
            return openObjectAnim;
        }

        public int getOpenAnim() {
            return openAnim;
        }

        public int getOpenGfx() {
            return openGfx;
        }

        public int getClosedObject(int type) {
            return closedThemeObjects[type];
        }

        public int getOpenObject(int type) {
            return openThemeObjects == null ? -1 : openThemeObjects[type];
        }

        public String getFailMessage() {
            return failMessage;
        }
    }

    /*
     * keydoors only require the unlock objectid and itemid
     */
    public static enum KeyDoors {
        ORANGE_TRIANGLE(0),
        ORANGE_DIAMOND(1),
        ORANGE_RECTANGLE(2),
        ORANGE_PENTAGON(3),
        ORANGE_CORNER(4),
        ORANGE_CRESCENT(5),
        ORANGE_WEDGE(6),
        ORANGE_SHIELD(7),
        SILVER_TRIANGLE(8),
        SILVER_DIAMOND(9),
        SILVER_RECTANGLE(10),
        SILVER_PENTAGON(11),
        SILVER_CORNER(12),
        SILVER_CRESCENT(13),
        SILVER_WEDGE(14),
        SILVER_SHIELD(15),
        YELLOW_TRIANGLE(16),
        YELLOW_DIAMOND(17),
        YELLOW_RECTANGLE(18),
        YELLOW_PENTAGON(19),
        YELLOW_CORNER(20),
        YELLOW_CRESCENT(21),
        YELLOW_WEDGE(22),
        YELLOW_SHIELD(23),
        GREEN_TRIANGLE(24),
        GREEN_DIAMOND(25),
        GREEN_RECTANGLE(26),
        GREEN_PENTAGON(27),
        GREEN_CORNER(28),
        GREEN_CRESCENT(29),
        GREEN_WEDGE(30),
        GREEN_SHIELD(31),
        BLUE_TRIANGLE(32),
        BLUE_DIAMOND(33),
        BLUE_RECTANGLE(34),
        BLUE_PENTAGON(35),
        BLUE_CORNER(36),
        BLUE_CRESCENT(37),
        BLUE_WEDGE(38),
        BLUE_SHIELD(39),
        PURPLE_TRIANGLE(40),
        PURPLE_DIAMOND(41),
        PURPLE_RECTANGLE(42),
        PURPLE_PENTAGON(43),
        PURPLE_CORNER(44),
        PURPLE_CRESCENT(45),
        PURPLE_WEDGE(46),
        PURPLE_SHIELD(47),
        CRIMSON_TRIANGLE(48),
        CRIMSON_DIAMOND(49),
        CRIMSON_RECTANGLE(50),
        CRIMSON_PENTAGON(51),
        CRIMSON_CORNER(52),
        CRIMSON_CRESCENT(53),
        CRIMSON_WEDGE(54),
        CRIMSON_SHIELD(55),
        GOLD_TRIANGLE(56),
        GOLD_DIAMOND(57),
        GOLD_RECTANGLE(58),
        GOLD_PENTAGON(59),
        GOLD_CORNER(60),
        GOLD_CRESCENT(61),
        GOLD_WEDGE(62),
        GOLD_SHIELD(63);
        private int index;

        private KeyDoors(int index) {
            this.index = index;
        }

        public int getObjectId() {
            return getLowestObjectId() + index;
        }

        public int getDoorId(int floorType) {
            return getLowestDoorId(floorType) + index;
        }

        public static int getLowestObjectId() {
            return 50208;
        }

        public static int getLowestDoorId(int floorType) {
            return DUNGEON_KEY_DOORS[floorType];
        }

        public static int getMaxObjectId() {
            return 50208 + KeyDoors.values().length;
        }

        public static int getMaxDoorId(int floorType) {
            return DUNGEON_KEY_DOORS[floorType] + KeyDoors.values().length;
        }

        public int getKeyId() {
            return getLowestKeyId() + (index * 2);
        }

        public static int getLowestKeyId() {
            return 18202;
        }

        public int getIndex() {
            return index;
        }
    }

    public static final int NORMAL_DOOR = 0, GUARDIAN_DOOR = 1, SKILL_DOOR = 2, CHALLENGE_DOOR = 3, KEY_DOOR = 4;

    public static final double UNBALANCED_PARTY_PENALTY = 0.4;
    public static final double[] DUNGEON_SIZE_BONUS = {0.04, 0.11, 0.19, 0};
    public static final double[] COMPLEXIYY_PENALTY_BASE = {0.32, 0.35, 0.37, 0};
    public static final double MAX_BONUS_ROOM = 0.13;
    public static final double[][] DUNGEON_DIFFICULTY_RATIO_BONUS = {
            //Team size x difficulty
            {0},
            {0, 0.12},
            {0, 0.16, 0.24},
            {0, 0.14, 0.27, 0.36},
            {0, 0.17, 0.33, 0.4, 0.48}
    };
    /*
     * floor types
     */
    public static final int FROZEN_FLOORS = 0, ABANDONED_FLOORS = 1, FURNISHED_FLOORS = 2, OCCULT_FLOORS = 3, WARPED_FLOORS = 4;

    /*
     * dungeon sizes
     */
    public static final int SMALL_DUNGEON = 0, MEDIUM_DUNGEON = 1, LARGE_DUNGEON = 2, TEST_DUNGEON = 3;

    // 4x4, 4x8 and 4x8, 2x1(just boss room for test)
    public static final int[][] DUNGEON_RATIO = new int[][] {
            new int[] {4, 4},
            new int[] {4, 8},
            new int[] {8, 8},
            new int[] {3, 1}
    };

    public static final int XP_RATE = 5;

    /*
     * door directions
     */
    public static final int EAST_DOOR = 2, WEST_DOOR = 0, NORTH_DOOR = 1, SOUTH_DOOR = 3;

    public static final int[] START_ROOM_MUSICS = new int[] {822, 797, 765, 884, 935};

    public static final int[][] DANGEROUS_MUSICS =
            {
                    {832, 834, 827, 811, 824, 808, 831, 810, 833, 837}, //frozen
                    {779, 781, 799, 802, 794, 780, 771, 790, 775, 801}, //abandoned
                    {748, 769, 741, 739, 742, 753, 761, 757, 747, 756}, //furnished
                    {873, 882, 880, 860, 886, 872, 885, 889, 870, 864}, //occult floor
                    {930, 914, 917, 911, 927, 939, 938, 920, 925, 936}, //warped floor
            };

    public static final int[][] SAFE_MUSICS =
            {
                    // FROZEN_FLOORS
                    {804, 805, 806, 807, 809, 812, 813, 814, 812, 813, 814, 821, 822}, //frozen
                    {772, 773, 775, 776, 777, 778, 788, 789, 791, 792, 793, 795, 796}, //abandoned
                    {740, 744, 745, 749, 750, 751, 752, 754, 755, 758, 760, 762, 764}, //furnished
                    {861, 862, 866, 867, 868, 869, 871, 874, 875, 878, 879, 881, 883}, //occult
                    {912, 915, 918, 919, 921, 923, 926, 928, 929, 931, 932, 933, 934} //warped floor
            };

    /*
     * Creatures
     */
    public static final int[][] GUARDIAN_CREATURES =
            {
                    //ALL FLOORs
                    {10831, 10906, 10910, 10821, 10630, 10680, 10693, 10364, /*10480,*/ 10726}
                    // FROZEN
                    ,
                    {10618, 10460, 10762, 10157, 10782, 10225, 10212, 10791, 10770}
                    // ABANDONED
                    ,
                    { /*10168,*/ 10496, /*10178,*/ 10469, 10604, 10706}
                    // Furnished
                    ,
                    {10797, 10496, 10776, 10706}
                    // Occult
                    ,
                    {10744, 10722, 10496, 10754, 10718, 10188, 10492, 10815}
                    // Warped
                    ,
                    {10744, 10736, 10722, 10219, 10496, 10754, 10718, 10492, 12941}};

    public static final int[][] FORGOTTEN_WARRIORS =
            {
                    //warrior
                    {10507, 10439, 10246, 10288, 10528},
                    //mage
                    {10560, 10570, 10575, 10580, 10585, 10590, 10595, 10600},
                    //range
                    {10320, 10325, 10330, 10335, 10340, 10345, 10350, 10355, 10360}
            };

    public static final int[] SLAYER_CREATURES =
            {10694, 10695, 10696, 10697, 10698, 10699, 10700, 10701, 10702, 10704, 10705};

    /*
     * id - lvl
     */
    public static final int[] HUNTER_CREATURES =
            {11086, 11087, 11088, 11089, 11090, 11091, 11092, 11093, 11094, 11095};

    public static boolean isDungeoneeringCreature(int id) {
        for (int i = 0; i < GUARDIAN_CREATURES.length; i++)
            for (int j = 0; j < GUARDIAN_CREATURES[i].length; j++)
                if (id == GUARDIAN_CREATURES[i][j])
                    return true;

        for (int i = 0; i < FORGOTTEN_WARRIORS.length; i++)
            for (int j = 0; j < FORGOTTEN_WARRIORS[i].length; j++)
                if (id == FORGOTTEN_WARRIORS[i][j])
                    return true;

        for (int i = 0; i < SLAYER_CREATURES.length; i++)
            if (id == SLAYER_CREATURES[i])
                return true;

        for (int i = 0; i < HUNTER_CREATURES.length; i++)
            if (id == HUNTER_CREATURES[i])
                return true;
        return false;
    }

    public static final StartRoom[] START_ROOMS = new StartRoom[]
            {
                    // FROZEN_FLOORS
                    new StartRoom(14, 632, EAST_DOOR, WEST_DOOR, NORTH_DOOR, SOUTH_DOOR),
                    new StartRoom(14, 624, SOUTH_DOOR),
                    new StartRoom(14, 626, WEST_DOOR, SOUTH_DOOR),
                    new StartRoom(14, 630, WEST_DOOR, NORTH_DOOR, SOUTH_DOOR)};

    public static final NormalRoom[] NORMAL_ROOMS = new NormalRoom[]
            {
                    new NormalRoom(8, 240, spot(3, 7), SOUTH_DOOR),
                    new NormalRoom(8, 242, spot(3, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(8, 244, spot(3, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(8, 246, spot(4, 13), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(8, 248, spot(3, 12), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(8, 256, spot(12, 12), SOUTH_DOOR),
                    new NormalRoom(8, 258, spot(12, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(8, 260, spot(11, 6), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(8, 262, spot(12, 6), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(8, 264, spot(12, 12), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(10, 240, spot(3, 3), SOUTH_DOOR),
                    new NormalRoom(10, 242, spot(7, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(10, 244, spot(2, 4), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(10, 246, spot(2, 12), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(10, 248, spot(2, 12), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(10, 256, spot(8, 12), SOUTH_DOOR),
                    new NormalRoom(10, 258, spot(12, 2), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(10, 260, spot(3, 4), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(10, 262, spot(7, 8), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(10, 264, spot(5, 10), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(12, 240, spot(3, 7), SOUTH_DOOR),
                    new NormalRoom(12, 242, spot(11, 7), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(12, 244, spot(3, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(12, 246, spot(10, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(12, 248, spot(10, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(12, 256, spot(9, 7), SOUTH_DOOR),
                    new NormalRoom(12, 258, spot(3, 6), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(12, 260, spot(4, 10), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(12, 262, spot(9, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(12, 264, spot(3, 6), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(14, 240, spot(7, 10), SOUTH_DOOR),

                    new NormalRoom(14, 256, spot(4, 13), SOUTH_DOOR),
                    new NormalRoom(14, 258, spot(12, 9), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(14, 260, spot(12, 13), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(14, 262, spot(7, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(14, 264, spot(11, 13), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(16, 240, spot(7, 10), SOUTH_DOOR),

                    new NormalRoom(16, 256, spot(4, 12), SOUTH_DOOR),
                    new NormalRoom(16, 258, spot(7, 11), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(16, 260, spot(4, 3), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(16, 262, spot(7, 8), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(16, 264, spot(7, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(18, 240, spot(7, 10), SOUTH_DOOR),
                    new NormalRoom(18, 260, spot(5, 12), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(18, 264, spot(3, 12), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(20, 240, spot(9, 13), SOUTH_DOOR),

                    new NormalRoom(20, 258, spot(11, 9), SOUTH_DOOR, WEST_DOOR),

                    new NormalRoom(22, 240, spot(8, 9), SOUTH_DOOR),

                    new NormalRoom(22, 256, spot(7, 10), SOUTH_DOOR),
                    new NormalRoom(22, 258, spot(11, 10), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(22, 260, spot(7, 8), SOUTH_DOOR, NORTH_DOOR),
                    new NormalRoom(22, 262, spot(7, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR),
                    new NormalRoom(22, 264, spot(7, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(24, 242, spot(7, 7), SOUTH_DOOR, WEST_DOOR),

                    new NormalRoom(24, 258, spot(12, 10), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(24, 264, spot(7, 7), SOUTH_DOOR, NORTH_DOOR, WEST_DOOR, EAST_DOOR),

                    new NormalRoom(26, 242, spot(9, 9), SOUTH_DOOR, WEST_DOOR),

                    new NormalRoom(28, 246, spot(11, 8), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(30, 244, spot(3, 6), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(32, 244, spot(3, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(34, 242, spot(12, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(36, 248, spot(2, 13), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(38, 246, spot(12, 10), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(40, 244, spot(3, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(42, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(44, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(46, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(48, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(50, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),

                    new NormalRoom(52, 240, spot(7, 11), SOUTH_DOOR),
                    new NormalRoom(54, 240, spot(8, 11), SOUTH_DOOR),
                    new NormalRoom(56, 240, spot(3, 10), SOUTH_DOOR),
                    new NormalRoom(58, 240, spot(3, 12), SOUTH_DOOR),
                    new NormalRoom(60, 240, spot(9, 12), SOUTH_DOOR),
                    new NormalRoom(62, 240, spot(12, 12), SOUTH_DOOR),
                    new NormalRoom(64, 240, spot(2, 12), SOUTH_DOOR),
                    new NormalRoom(66, 240, spot(8, 10), SOUTH_DOOR),
                    new NormalRoom(68, 240, spot(3, 7), SOUTH_DOOR),
                    new NormalRoom(70, 240, spot(8, 12), SOUTH_DOOR),
                    new NormalRoom(72, 240, spot(12, 7), SOUTH_DOOR),
                    new NormalRoom(74, 240, spot(4, 10), SOUTH_DOOR),
                    new NormalRoom(76, 240, spot(8, 7), SOUTH_DOOR),
                    new NormalRoom(78, 240, spot(3, 4), SOUTH_DOOR),

                    new NormalRoom(62, 242, spot(13, 11), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(62, 244, spot(2, 3), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(62, 246, spot(13, 11), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(62, 248, spot(2, 3), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(64, 242, spot(7, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(64, 244, spot(13, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(64, 246, spot(7, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(64, 248, spot(7, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(66, 242, spot(12, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(66, 244, spot(3, 4), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(66, 246, spot(12, 12), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(66, 248, spot(3, 3), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(68, 242, spot(12, 8), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(68, 244, spot(5, 8), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(68, 246, spot(5, 8), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(70, 242, spot(11, 10), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(70, 244, spot(8, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(70, 246, spot(8, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(72, 242, spot(9, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(72, 244, spot(10, 8), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(72, 246, spot(11, 8), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(72, 248, spot(8, 5), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(74, 242, spot(3, 6), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(74, 244, spot(7, 5), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(74, 246, spot(3, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(74, 248, spot(2, 6), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(76, 242, spot(11, 9), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(76, 244, spot(7, 7), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(76, 246, spot(7, 7), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(76, 248, spot(7, 11), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
                    new NormalRoom(78, 242, spot(12, 12), SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(78, 244, spot(12, 12), NORTH_DOOR, SOUTH_DOOR),
                    new NormalRoom(78, 246, spot(12, 3), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR),
                    new NormalRoom(78, 248, spot(2, 3), NORTH_DOOR, SOUTH_DOOR, WEST_DOOR, EAST_DOOR)};

    public static enum MapRoomIcon {

        MAP_1(2791, true, SOUTH_DOOR),
        MAP_2(2792, true, WEST_DOOR),
        MAP_3(2793, true, NORTH_DOOR),
        MAP_4(2794, true, EAST_DOOR),
        MAP_5(2795, true, WEST_DOOR, SOUTH_DOOR),
        MAP_6(2796, true, WEST_DOOR, NORTH_DOOR),
        MAP_7(2797, true, NORTH_DOOR, EAST_DOOR),
        MAP_8(2798, true, SOUTH_DOOR, EAST_DOOR),
        MAP_9(2799, true, SOUTH_DOOR, WEST_DOOR, NORTH_DOOR),
        MAP_10(2800, true, WEST_DOOR, NORTH_DOOR, EAST_DOOR),
        MAP_11(2801, true, SOUTH_DOOR, NORTH_DOOR, EAST_DOOR),
        MAP_12(2802, true, SOUTH_DOOR, WEST_DOOR, EAST_DOOR),
        MAP_13(2803, true, SOUTH_DOOR, NORTH_DOOR, EAST_DOOR, WEST_DOOR),
        MAP_14(2804, true, SOUTH_DOOR, NORTH_DOOR),
        MAP_15(2805, true, WEST_DOOR, EAST_DOOR),
        MAP_16(2787, false, SOUTH_DOOR),
        MAP_17(2788, false, WEST_DOOR),
        MAP_18(2789, false, NORTH_DOOR),
        MAP_19(2790, false, EAST_DOOR);

        private int[] doorsDirections;
        private int spriteId;
        private boolean open;

        private MapRoomIcon(int spriteId, boolean open, int... doorsDirections) {
            this.doorsDirections = doorsDirections;
            this.open = open;
            this.spriteId = spriteId;
        }

        public int[] getDoorDirections() {
            return doorsDirections;
        }

        public boolean hasSouthDoor() {
            return hasDoor(SOUTH_DOOR);
        }

        public boolean hasNorthDoor() {
            return hasDoor(NORTH_DOOR);
        }

        public boolean hasWestDoor() {
            return hasDoor(WEST_DOOR);
        }

        public boolean hasEastDoor() {
            return hasDoor(EAST_DOOR);
        }

        public boolean hasDoor(int direction) {
            for (int dir : doorsDirections)
                if (dir == direction)
                    return true;
            return false;
        }

        public int getSpriteId() {
            return spriteId;
        }

        public boolean isOpen() {
            return open;
        }
    }

    private static int[] spot(int x, int y) {
        return new int[]
                {x, y};
    }

    public static final BossRoom[][] BOSS_ROOMS =
            {
                    // FROZEN_FLOORS
                    {

                            //Icy Bones
                            new BossRoom(new RoomEvent() {
                                @Override
                                public void openRoom(DungeonManager dungeon, RoomReference reference) {
                                    dungeon.spawnNPC(reference, 10040, 7, 11, false, BOSS_NPC);
                                }
                            }, 815, 1, 24, 626)

                    },
                    //ABANDONED BOSSES
                    {

                            //Unholy cursebearer
                            new BossRoom(new RoomEvent() {
                                @Override
                                public void openRoom(DungeonManager dungeon, RoomReference reference) {
                                    dungeon.spawnNPC(reference, 10111, 6, 8, false, BOSS_NPC);
                                }
                            }, 784, 15, 26, 640),

                    },
                    //FURNISHED BOSSES
                    {
//                            //Rammernaut
                            new BossRoom(new RoomEvent() {
                                @Override
                                public void openRoom(DungeonManager dungeon, RoomReference reference) {
                                    //this one appears at middle of arena instead
                                    dungeon.spawnNPC(reference, 9767, 7, 7, false, BOSS_NPC);
                                }
                            }, 746, 18, 26, 656)

                    },
                    {
//                            //Runebound Behemoth
                            new BossRoom(new RoomEvent() {
                                @Override
                                public void openRoom(DungeonManager dungeon, RoomReference reference) {
                                    dungeon.spawnNPC(reference, 11812, 6, 8, false, BOSS_NPC);
                                }

                            }, 877, 36, 26, 674),

                    },

                    //201 5513 0 for blink
                    {

                            //Dreadnaut
                            new BossRoom(new RoomEvent() {
                                @Override
                                public void openRoom(DungeonManager dungeon, RoomReference reference) {
                                    dungeon.spawnNPC(reference, 12848, 7, 5, false, BOSS_NPC);
                                }

                            }, 913, 48, 28, 688),
                    }

            };

    public static final int[][] WALL_BASE_X_Y =
            {
                    {14, 1, 0, 14},
                    {1, 1, 14, 0},
                    {1, 1, 0, 14},
                    {1, 14, 14, 0}};

    public static final int SET_RESOURCES_MAX_TRY = 300;

    public static final int FISH_SPOT_OBJECT_ID = 49922, FISH_SPOT_NPC_ID = 2859;

    public static final long[] FARMING_CYCLES =
            {0, 30000, 60000, Integer.MAX_VALUE};
    public static final int FARMING_PATCH_BEGINING = 50040, FARMING_PATCH_END = 50189;
    public static final int EMPTY_FARMING_PATCH = 50038;

    public static final Item ESSENCE = new Item(17776);

    public static final int NORMAL_NPC = 0, GUARDIAN_NPC = 1, FISH_SPOT_NPC = 2, BOSS_NPC = 3, FORGOTTEN_WARRIOR = 4, SLAYER_NPC = 5, HUNTER_NPC = 6, PUZZLE_NPC = 7;

    public static SmeltingData[] SMELTING_BARS = new SmeltingData[]
            {
                    SmeltingData.NOVITE_BAR,
                    SmeltingData.BATHUS_BAR,
                    SmeltingData.MARMAROS_BAR,
                    SmeltingData.KRATONITE_BAR,
                    SmeltingData.FRACTITE_BAR,
                    SmeltingData.ZEPHYRIUM_BAR,
                    SmeltingData.ARGONITE_BAR,
                    SmeltingData.KATAGON_BAR,
                    SmeltingData.GORGONITE_BAR,
                    SmeltingData.PROMETHIUM_BAR};

    public static int[] GROUP_GATESTONE_RUNES =
            {17792, 3};

    public static final int[][] HOARDSTALKER_ITEMS =
            {
                    {17630, 17424, 17682, 17448, 17797},
                    {17632, 17426, 17684, 17450, 17799},
                    {17634, 17428, 17686, 17452, 17801},
                    {17636, 17430, 17688, 17454, 17803},
                    {17638, 17432, 17700, 17456, 17805},
                    {17640, 17434, 17702, 17458, 17807},
                    {17642, 17436, 17704, 17460, 17809},
                    {17644, 17438, 17706, 17462, 17811},
                    {17646, 17440, 17708, 17464, 17813},
                    {17648, 17442, 17710, 17466, 17815},};

    public static final int[] SLAYER_LEVELS =
            {5, 10, 17, 30, 41, 52, 63, 71, 80, 90, 99};
}
