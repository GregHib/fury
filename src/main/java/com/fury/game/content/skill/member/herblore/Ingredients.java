package com.fury.game.content.skill.member.herblore;

import java.util.HashMap;
import java.util.Map;

public enum Ingredients {
    GUAM(249, new short[] { (short) (short) Herblore.VIAL_OF_WATER.getId(), (short) (short) Herblore.SWAMP_TAR.getId() }, new short[] { 91, 10142 }, new byte[] { 0, 19 }, new double[] { 0, 30 }),
    MARRENTILL(251, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), (short) Herblore.SWAMP_TAR.getId() }, new short[] { 93, 10143 }, new byte[] { 0, 31 }, new double[] { 0, 42.5 }),
    TARROMIN(253, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), (short) Herblore.SWAMP_TAR.getId() }, new short[] { 95, 10144 }, new byte[] { 0, 39 }, new double[] { 0, 55 }),
    HARRALANDER(255, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), (short) Herblore.SWAMP_TAR.getId() }, new short[] { 97, 10145 }, new byte[] { 0, 44 }, new double[] { 0, 72.5 }),
    RANARR(257, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 99 }, new byte[] { 0 }, new double[] { 0 }),
    TOADFLAX(2998, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), (short) Herblore.COCONUT_MILK.getId() }, new short[] { 3002, 5942 }, new byte[] { 0, 0 }, new double[] { 0, 0 }),
    SPIRIT_WEED(12172, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 12181 }, new byte[] { 0 }, new double[] { 0 }),
    WERGALI(14854, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 14856 }, new byte[] { 0 }, new double[] { 0 }),
    IRIT(259, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), (short) Herblore.COCONUT_MILK.getId() }, new short[] { 101, 5951 }, new byte[] { 0, 0 }, new double[] { 0, 0 }),
    AVANTOE(261, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), 2436, 145, 147, 149 }, new short[] { 103, 15308, 15309, 15310, 15311 }, new byte[] { 0, 88, 88, 88, 88 }, new double[] { 0, 220, 220, 220, 220 }),
    FELLSTALK(21624, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 21628 }, new byte[] { 94 }, new double[] { 0 }),
    KWUARM(263, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 105 }, new byte[] { 0 }, new double[] { 0 }),
    STARFLOWER(9017, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 9019 }, new byte[] { 0 }, new double[] { 0 }),
    SNAPDRAGON(3000, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 3004 }, new byte[] { 0 }, new double[] { 0 }),
    CADANTINE(265, new short[] { (short) Herblore.VIAL_OF_WATER.getId() }, new short[] { 107 }, new byte[] { 0 }, new double[] { 0 }),
    LANTADYME(2481, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), 2442, 163, 165, 167 }, new short[] { 2483, 15316, 15317, 15318, 15319 }, new byte[] { 0, 90, 90, 90, 90 }, new double[] { 0, 240, 240, 240, 240 }),
    DWARF_WEED(267, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), 2440, 157, 159, 161 }, new short[] { 109, 15312, 15313, 15314, 15315 }, new byte[] { 0, 89, 89, 89, 89 }, new double[] { 0, 230, 230, 230, 230 }),
    CACTUS_SPINE(6016, new short[] { (short) Herblore.COCONUT_MILK.getId() }, new short[] { 5936 }, new byte[] { 0 }, new double[] { 0 }),
    TORSTOL(269, new short[] { (short) Herblore.VIAL_OF_WATER.getId(), 15309, 15313, 15317, 15321, 15325 }, new short[] { 111, 15333, 15333, 15333, 15333, 15333 }, new byte[] { 0, 96, 96, 96, 96, 96 }, new double[] { 0, 1000, 1000, 1000, 1000, 1000 }),
    CAVE_NIGHTSHADE(2398, new short[] { (short) Herblore.COCONUT_MILK.getId() }, new short[] { 5939 }, new byte[] { 0 }, new double[] { 0 }),
    EYE_OF_NEWT(221, new short[] { 91, 101 }, new short[] { 121, 145 }, new byte[] { 0, 45 }, new double[] { 25, 100 }),
    UNICORN_HORN_DUST(235, new short[] { 93, 101, 3024, 3026, 3028, 3030 }, new short[] { 175, 181, 10909, 10911, 10913, 10915 }, new byte[] { 5, 48, 65, 65, 65, 65 }, new double[] { 37.5, 106.3, 53.33, 53.33, 53.33, 53.33 }),
    MIXTURE_STEP_2(1526, new short[] { 10909, 10911, 10913, 10915 }, new short[] { 10915, 10917, 10919, 10921 }, new byte[] { 65, 65, 65, 65 }, new double[] { 53.33, 53.33, 53.33, 53.33 }),
    SANFEW_SYRUM(10937, new short[] { 10915, 10917, 10919, 10921 }, new short[] { 10925, 10927, 10929, 10931 }, new byte[] { 65, 65, 65, 65 }, new double[] { 53.33, 53.33, 53.33, 53.33 }),
    LIMPWURT_ROOT(225, new short[] { 95, 105 }, new short[] { 115, 157 }, new byte[] { 12, 55 }, new double[] { 50, 125 }),
    RED_SPIDER_EGGS(223, new short[] { 97, 3004, 5936 }, new short[] { 127, 3026, 5937 }, new byte[] { 22, 63, 73 }, new double[] { 62.5, 142.5, 165 }),
    BLAMISH_SNAIL_SLIME(1581, new short[] { 97 }, new short[] { 1582 }, new byte[] { 25 }, new double[] { 80 }),
    CHOCOLATE_DUST(1975, new short[] { 97 }, new short[] { 3010 }, new byte[] { 26 }, new double[] { 67.5 }),
    WHITE_BERRIES(239, new short[] { 99, 107 }, new short[] { 133, 163 }, new byte[] { 30, 66 }, new double[] { 75, 150 }),
    RUBIUM(12630, new short[] { 91 }, new short[] { 12633 }, new byte[] { 31 }, new double[] { 55 }),
    TOAD_LEGS(2152, new short[] { 3002 }, new short[] { 3034 }, new byte[] { 34 }, new double[] { 80 }),
    GOAT_HORN_DUST(9736, new short[] { 97 }, new short[] { 9741 }, new byte[] { 36 }, new double[] { 84 }),
    PHARMAKOS_BERRIES(11807, new short[] { 3002 }, new short[] { 11810 }, new byte[] { 37 }, new double[] { 85 }),
    SNAPE_GRASS(231, new short[] { 99, 103 }, new short[] { 139, 151 }, new byte[] { 38, 50 }, new double[] { 87.5, 112.5 }),
    COCKATRICE_EGG(12109, new short[] { 12181 }, new short[] { 12142 }, new byte[] { 40 }, new double[] { 92 }),
    FROGSPAWN(10961, new short[] { 14856 }, new short[] { 14840 }, new byte[] { 40 }, new double[] { 92 }),
    CHOPPED_ONION(1871, new short[] { 101 }, new short[] { 18661 }, new byte[] { 46 }, new double[] { 0 }),
    MORT_MYRE_FUNGUS(2970, new short[] { 103 }, new short[] { 3018 }, new byte[] { 52 }, new double[] { 117.5 }),
    SHRUNK_OGLEROOT(11205, new short[] { 95 }, new short[] { 11204 }, new byte[] { 52 }, new double[] { 6 }),
    KEBBIT_TEETH_DUST(10111, new short[] { 103 }, new short[] { 10000 }, new byte[] { 53 }, new double[] { 120 }),
    CRUSHED_GORAK_CLAW(9018, new short[] { 9019 }, new short[] { 9022 }, new byte[] { 57 }, new double[] { 130 }),
    WIMPY_FEATHER(11525, new short[] { 14856 }, new short[] { 14848 }, new byte[] { 58 }, new double[] { 132 }),
    DRAGON_SCALE_DUST(241, new short[] { 105, 2483 }, new short[] { 187, 2454 }, new byte[] { 60, 69 }, new double[] { 137.5, 157.5 }),
    YEW_ROOTS(6049, new short[] { 5942 }, new short[] { 5945 }, new byte[] { 68 }, new double[] { 155 }),
    WINE_OF_ZAMORAK(245, new short[] { 109 }, new short[] { 169 }, new byte[] { 72 }, new double[] { 162.5 }),
    POTATO_CACTUS(3138, new short[] { 2483 }, new short[] { 3042 }, new byte[] { 76 }, new double[] { 172.5 }),
    JANGERBERRIES(247, new short[] { 111 }, new short[] { 189 }, new byte[] { 78 }, new double[] { 175 }),
    MAGIC_ROOTS(6051, new short[] { 5951 }, new short[] { 5954 }, new byte[] { 79 }, new double[] { 177.5 }),
    CRUSHED_BIRD_NEST(6693, new short[] { 3002 }, new short[] { 6687 }, new byte[] { 81 }, new double[] { 180 }),
    POISON_IVY_BERRIES(6018, new short[] { 5939 }, new short[] { 5940 }, new byte[] { 82 }, new double[] { 190 }),
    PAPAYA_FRUIT(5972, new short[] { 3018 }, new short[] { 15301 }, new byte[] { 84 }, new double[] { 200 }),
    PHOENIX_FEATHER(4621, new short[] { 2452, 2454, 2456, 2458 }, new short[] { 15304, 15305, 15306, 15307 }, new byte[] { 85, 85, 85, 85 }, new double[] { 210, 210, 210, 210 }),
    GROUND_MUD_RUNES(9594, new short[] { 3040, 3042, 3044, 3046 }, new short[] { 15320, 15321, 15322, 15323 }, new byte[] { 91, 91, 91, 91 }, new double[] { 250, 250, 250, 250 }),
    GRENWALL_SPIKES(12539, new short[] { 2444, 169, 171, 173 }, new short[] { 15324, 15325, 15326, 15327 }, new byte[] { 92, 92, 92, 92 }, new double[] { 260, 260, 260, 260 }),
    MORCHELLA_MUSHROOMS(21622, new short[] { 21628 }, new short[] { 21632 }, new byte[] { 94 }, new double[] { 190 }),
    BONEMEAL(6810, new short[] { 2434, 139, 141, 143 }, new short[] { 15328, 15329, 15330, 15331 }, new byte[] { 94, 94, 94, 94 }, new double[] { 270, 270, 270, 270 }),
    //Dung UNF
    SAGEWORT(17512, new short[] { 17492 }, new short[] { 17538 }, new byte[] { 3 }, new double[] { 0.0 }),
    VALERIAN(17514, new short[] { 17492 }, new short[] { 17540 }, new byte[] { 4 }, new double[] { 0.0 }),
    ALOE(17516, new short[] { 17492 }, new short[] { 17542 }, new byte[] { 8 }, new double[] { 0.0 }),
    WORMWOOD_LEAF(17518, new short[] { 17492 }, new short[] { 17544 }, new byte[] { 34 }, new double[] { 0.0 }),
    MAGEBANE(17520, new short[] { 17492 }, new short[] { 17546 }, new byte[] { 37 }, new double[] { 0.0 }),
    FEATHER_FOIL(17522, new short[] { 17492 }, new short[] { 17548 }, new byte[] { 41 }, new double[] { 0.0 }),
    GRIMY_WINTERS_GRIP(17524, new short[] { 17492 }, new short[] { 17550 }, new byte[] { 67 }, new double[] { 0.0 }),
    LYCOPUS(17526, new short[] { 17492 }, new short[] { 17552 }, new byte[] { 70 }, new double[] { 0.0 }),
    BUCKTHORN(17528, new short[] { 17492 }, new short[] { 17554 }, new byte[] { 74 }, new double[] { 0.0 }),
    VOID_DUST(17530, new short[] { 17538, 17540, 17542, 17544, 17546, 17548, 17550, 17552, 17554 }, new short[] { 17556, 17558, 17562, 17582, 17584, 17588, 17606, 17608, 17612 }, new byte[] { 3, 5, 9, 36, 38, 42, 69, 71, 75 }, new double[] { 21, 34, 41, 79.5, 83, 89, 155.5, 160, 170.5 }),
    MISHAPEN_CLAWS(17532, new short[] { 17540, 17542, 17538, 17546, 17548, 17544, 17552, 17554, 17550 }, new short[] { 17560, 17570, 17578, 17586, 17594, 17602, 17610, 17618, 17626 }, new byte[] { 7, 18, 30, 40, 51, 63, 73, 84, 96 }, new double[] { 37.5, 57, 72, 86.5, 105.5, 139.5, 164, 189.5, 279 }),
    RED_MOSS(17534, new short[] { 17542, 17538, 17540, 17548, 17544, 17546, 17554, 17550, 17552 }, new short[] { 17564, 17574, 17576, 17590, 17598, 17600, 17614, 17622, 17624 }, new byte[] { 12, 24, 27, 45, 57, 60, 78, 90, 93 }, new double[] { 41, 65, 68.5, 93, 123.5, 131, 173.5, 234, 253 }),
    FIREBREATH_WHISKEY(17536, new short[] { 17542, 17538, 17540, 17548, 17544, 17546, 17554, 17550, 17552 }, new short[] { 17568, 17572, 17580, 17592, 17596, 17604, 17616, 17620, 17628 }, new byte[] { 15, 21, 33, 48, 54, 66, 81, 87, 99 }, new double[] { 53.5, 61, 75, 98.5, 114, 147, 178, 205.5, 315.5 });

    private static Map<Short, Ingredients> ingredients = new HashMap<Short, Ingredients>();

    public static Ingredients forId(int itemId) {
        return ingredients.get((short) itemId);
    }

    static {
        for (Ingredients ingredient : Ingredients.values()) {
            ingredients.put(ingredient.itemId, ingredient);
        }
    }

    private final short itemId;
    private final short[] otherItems;
    private final short[] rewards;
    private final byte[] levels;
    private final double[] experience;

    Ingredients(int itemId, short[] otherItems, short[] rewards, byte[] levels, double[] experience) {
        this.itemId = (short) itemId;
        this.otherItems = otherItems;
        this.rewards = rewards;
        this.levels = levels;
        this.experience = experience;
    }

    public short getItemId() {
        return itemId;
    }

    public short[] getOtherItems() {
        return otherItems;
    }

    public short[] getRewards() {
        return rewards;
    }

    public byte[] getLevels() {
        return levels;
    }

    public double[] getExperience() {
        return experience;
    }

    public byte getSlot(int itemId) {
        for (byte i = 0; i < otherItems.length; i++) {
            if (itemId == otherItems[i]) {
                return i;
            }
        }
        return -1;
    }

}