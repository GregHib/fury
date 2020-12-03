package com.fury.game.content.skill.free.cooking;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;

import java.util.HashMap;
import java.util.Map;

public enum Food {
    //Meats
    MEAT(new Item(2142), 30, FoodEffect.MEAT),
    SHRIMP(new Item(315), 30),
    CRAYFISH(new Item(13433), 20),
    CHICKEN(new Item(2140), 30),
    RABBIT(new Item(3228), 50),
    ANCHOVIES(new Item(319), 10),
    SARDINE(new Item(325), 30),
    KARAMBWANJI(new Item(3151), 30),
    KARAMBWAN(new Item(3144), 180),
    POISON_KARAMBWAN(new Item(3146), 180, FoodEffect.POISON_KARAMBWAN),
    UGTHANKI_KEBAB(new Item(1883), 190),
    HERRING(new Item(347), 50),
    MACKEREL(new Item(355), 60),
    ROAST_BIRD_MEAT(new Item(9980), 60),
    THIN_SNAIL_MEAT(new Item(3369), 50, 20),
    TROUT(new Item(333), 70),
    SPIDER_ON_STICK(new Item(6297), 70, 30),
    ROAST_RABBIT(new Item(7223), 70),
    LEAN_SNAIL_MEAT(new Item(3371), 70),
    COD(new Item(339), 70),
    PIKE(new Item(351), 80),
    ROAST_BEAST_MEAT(new Item(9988), 80),
    CRAB_MEAT(new Item(7521), 20),//TODO 7521-7526
    FAT_SNAIL_MEAT(new Item(3373), 70, 20),
    SALMON(new Item(329), 90, FoodEffect.SALMON),
    SLIMY_EEL(new Item(3381), 60, 40),
    TUNA(new Item(361), 100),
    CHOMPY(new Item(2878), 100),
    FISHCAKE(new Item(7530), 110),
    RAINBOW_FISH(new Item(10136), 110),
    CAVE_EEL(new Item(5003), 70, 40),
    LOBSTER(new Item(379), 120),
    JUBBLY(new Item(7568), 150),
    BASS(new Item(365), 130),
    SWORDFISH(new Item(373), 140),
    OOMLIE_WRAP(new Item(2343), 140),//Normally depends on hp level
    LAVA_EEL(new Item(2149), 110),
    MONKFISH(new Item(7946), 160),
    SHARK(new Item(385), 200),
    SEA_TURTLE(new Item(397), 210),
    CAVEFISH(new Item(15266), 200),
    MANTA_RAY(new Item(391), 220),
    ROCKTAIL(new Item(15272), 230, FoodEffect.ROCKTAIL),
    FURY_SHARK(new Item(20429), 247),
    TIGER_SHARK(new Item(21521, Revision.PRE_RS3), 250, FoodEffect.TIGER_SHARK),

    //Bread
    BREAD(new Item(2309), 50),

    //Pies
    REDBERRY_PIE(new Item(2325), 50, 0, 2333),
    HALF_REDBERRY_PIE(new Item(2333), 50, 0, 2313),
    MEAT_PIE(new Item(2327), 60, 0, 2327),
    HALF_MEAT_PIE(new Item(2331), 60, 0, 2313),
    APPLE_PIE(new Item(2323), 70, 0, 2335),
    HALF_APPLE_PIE(new Item(2335), 70, 0, 2313),
    GARDEN_PIE(new Item(7178), 60, 0, 7180, FoodEffect.GARDEN_PIE),
    HALF_GARDEN_PIE(new Item(7180), 60, 0, 2313, FoodEffect.GARDEN_PIE),
    FISH_PIE(new Item(7188), 60, 0, 7190, FoodEffect.FISH_PIE),
    HALF_FISH_PIE(new Item(7190), 60, 0, 2313, FoodEffect.FISH_PIE),
    ADMIRAL_PIE(new Item(7198), 80, 0, 7200, FoodEffect.ADMIRAL_PIE),
    HALF_ADMIRAL_PIE(new Item(7200), 80, 0, 2313, FoodEffect.ADMIRAL_PIE),
    WILD_PIE(new Item(7208), 110, 0, 7210, FoodEffect.WILD_PIE),
    HALF_WILD_PIE(new Item(7210), 110, 0, 2313, FoodEffect.WILD_PIE),
    SUMMER_PIE(new Item(7218), 110, 0, 7220, FoodEffect.SUMMER_PIE),
    HALF_SUMMER_PIE(new Item(7220), 110, 0, 2313, FoodEffect.SUMMER_PIE),

    //Stews
    STEW(new Item(2003), 110, 0),
    SPICY_STEW(new Item(7479), 110, FoodEffect.SPICY_STEW),
    CURRY(new Item(2011), 190),

	//Pizzas
    PLAIN_PIZZA(new Item(2289), 70, 0, 2291),
    HALF_PLAIN_PIZZA(new Item(2291), 70),
    MEAT_PIZZA(new Item(2293), 80, 0, 2295),
    HALF_MEAT_PIZZA(new Item(2295), 80),
    ANCHOVY_PIZZA(new Item(2297), 90, 0, 2299),
    HALF_ANCHOVY_PIZZA(new Item(2299), 90),
    PINEAPPLE_PIZZA(new Item(2301), 110, 0, 2303),
    HALF_PINEAPPLE_PIZZA(new Item(2303), 110),

    //Cakes
    CAKE(new Item(1891), 40, 0, 1893),
    SECOND_CAKE_SLICE(new Item(1893), 40, 0, 1895),
    THIRD_CAKE_SLICE(new Item(1895), 40),
    CHOCOLATE_CAKE(new Item(1897), 50, 0, 1899),
    SECOND_CHOCOLATE_CAKE_SLICE(new Item(1899), 50, 0, 1901),
    CHOCOLATE_SLICE(new Item(1901), 50),

    //Potatoes & Toppings
    POTATO(new Item(1942), 10),
    BAKED_POTATO(new Item(6701), 40),
    SPICY_SAUCE(new Item(7072), 20),
    CHILLI_CON_CARNE(new Item(7062), 140),
    SCRAMBLED_EGG(new Item(7078), 50),
    EGG_AND_TOMATO(new Item(7064), 80),
    SWEETCORN(new Item(5988), 10),
    POTATO_WITH_BUTTER(new Item(6703), 140),
    CHILLI_POTATO(new Item(7054), 140),
    FRIED_ONIONS(new Item(7084), 50),
    FRIED_MUSHROOMS(new Item(7082), 50),
    POTATO_WITH_CHEESE(new Item(6705), 160),
    EGG_POTATO(new Item(7056), 160),
    MUSHROOM_AND_ONION(new Item(7066), 110),
    MUSHROOM_POTATO(new Item(7058), 200),
    TUNA_AND_SWEETCORN(new Item(7068), 130),
    TUNA_POTATO(new Item(7060), 220),

    //Dairy
    CHEESE(new Item(1985), 20),
    POT_OF_CREAM(new Item(2130), 10),

    //Gnome
    TOAD_CRUNCHIES(new Item(2217), 80),//Also 9538
    SPICY_CRUNCHIES(new Item(9540), 70),
    WORM_CRUNCHIES(new Item(9542), 80),
    CHOCCHIP_CRUNCHIES(new Item(9542), 70),
    FRUIT_BATTA(new Item(2277), 110),//9527
    TOAD_BATTA(new Item(2255), 110),//9529
    WORM_BATTA(new Item(2253), 110),//9531
    VEGETABLE_BATTA(new Item(2281), 110),//9533
    CHEESE_AND_TOMATO_BATTA(new Item(2259), 110),//9535
    WORM_HOLE(new Item(2191), 120),//9547
    VEGETABLE_BALL(new Item(2195), 120),//9549
    TANGLED_TOADS_LEGS(new Item(2187), 150),//9551
    CHOCOLATE_BOMB(new Item(2185), 150),//9553

    //Stealing creation
    CLASS_1_FOOD(new Item(14162), 40),
    CLASS_2_FOOD(new Item(14164), 80),
    CLASS_3_FOOD(new Item(14166), 120),
    CLASS_4_FOOD(new Item(14168), 160),
    CLASS_5_FOOD(new Item(14170), 200),

    //Fruit & berries
    BANANA(new Item(1963), 20),
    BANANA_(new Item(18199), 20),
    DWELLBERRIES(new Item(2126), 20),
    LEMON(new Item(2102), 20),
    LEMON_CHUNKS(new Item(2104), 20),
    LEMON_SLICES(new Item(2106), 20),
    LIME(new Item(2120), 20),
    LIME_CHUNKS(new Item(2122), 20),
    LIME_SLICES(new Item(2124), 20),
    ORANGE(new Item(2108), 20),
    ORANGE_CHUNKS(new Item(2110), 20),
    ORANGE_SLICES(new Item(2112), 20),
    PAPAYA_FRUIT(new Item(5972), 50),
    PINEAPPLE_CHUNKS(new Item(2116), 20),
    PINEAPPLE_RINGS(new Item(2118), 20),
    STRAWBERRY(new Item(5504), 60),
    TOMATO(new Item(1982), 20),
    WATERMELON_SLICE(new Item(5984), 50),
    CABBAGE(new Item(1965), 20),
    PEACH(new Item(6883), 80),

    //Dungeoneering
    BAKED_CAVE_POTATO(new Item(18093), 20),
    GISSEL_POTATO(new Item(18095), 60),
    EDICAP_POTATO(new Item(18097), 120),
    HEIM_CRAB(new Item(18159), 20),
    HEIM_CRAB_POTATO(new Item(18099), 50),
    HEIM_CRAB_GISSEL_POTATO(new Item(18119), 80),
    HEIM_CRAB_EDICAP_POTATO(new Item(18139), 140),
    RED_EYE(new Item(18161), 50),
    RED_EYE_POTATO(new Item(18101), 80),
    RED_EYE_GISSEL_POTATO(new Item(18121), 110),
    RED_EYE_EDICAP_POTATO(new Item(18141), 170),
    DUSK_EEL(new Item(18163), 70),
    DUSK_EEL_POTATO(new Item(18103), 100),
    DUSK_EEL_GISSEL_POTATO(new Item(18123), 130),
    DUSK_EEL_EDICAP_POTATO(new Item(18144), 130),
    GIANT_FLATFISH(new Item(18165), 100),
    FLATFISH_POTATO(new Item(18105), 130),
    FLATFISH_GISSEL_POTATO(new Item(18125), 160),
    FLATFISH_EDICAP_POTATO(new Item(18145), 220),
    SHORT_FINNED_EEL(new Item(18167), 120),
    SHORT_FIN_EEL_POTATO(new Item(18107), 150),
    SHORT_FIN_GISSEL_POTATO(new Item(18127), 180),
    SHORT_FIN_EDICAP_POTATO(new Item(18147), 240),
    WEB_SNIPPER(new Item(18169), 150),
    WEB_SNIPPER_POTATO(new Item(18109), 180),
    WEB_SNIPPER_GISSEL_POTATO(new Item(18129), 210),
    WEB_SNIPPER_EDICAP_POTATO(new Item(18149), 270),
    BOULDABASS(new Item(18171), 170),
    BOULDABASS_POTATO(new Item(18111), 200),
    BOULDABASS_GISSEL_POTATO(new Item(18131), 230),
    BOULDABASS_EDICAP_POTATO(new Item(18151), 290),
    SALVE_EEL(new Item(18173), 200),
    SALVE_EEL_POTATO(new Item(18113), 230),
    SALVE_EEL_GISSEL_POTATO(new Item(18133), 260),
    SALVE_EEL_EDICAP_POTATO(new Item(18153), 320),
    BLUE_CRAB(new Item(18175), 220),
    BLUE_CRAB_POTATO(new Item(18115), 250),
    BLUE_CRAB_GISSEL_POTATO(new Item(18135), 280),
    BLUE_CRAB_EDICAP_POTATO(new Item(18155), 340),
    CAVE_MORAY(new Item(18177), 250),
    CAVE_MORAY_POTATO(new Item(18117), 280),
    CAVE_MORAY_GISSEL_POTATO(new Item(18137), 310),
    CAVE_MORAY_EDICAP_POTATO(new Item(18157), 370),

    //Jadinko fruit
    COMMON_FRUIT(new Item(21376), 150),
    SHADOW_FRUIT(new Item(21377), 150, FoodEffect.SHADOW_FRUIT),
    IGNEOUS_FRUIT(new Item(21378), 150, FoodEffect.IGNEOUS_FRUIT),
    CANNIBAL_FRUIT(new Item(21379), 150, FoodEffect.CANNIBAL_FRUIT),
    AQUATIC_FRUIT(new Item(21380), 150, FoodEffect.AQUATIC_FRUIT),
    AMPHIBIOUS_FRUIT(new Item(21381), 150, FoodEffect.AMPHIBIOUS_FRUIT),
    CARRION_FRUIT(new Item(21382), 150, FoodEffect.CARRION_FRUIT),
    DISEASED_FRUIT(new Item(21383), 150, FoodEffect.DISEASED_FRUIT),
    CAMOUFLAGED_FRUIT(new Item(21384), 150, FoodEffect.CAMOUFLAGED_FRUIT),
    DRACONIC_FRUIT(new Item(21385), 150, FoodEffect.DRACONIC_FRUIT),
    SARADOMIN_FRUIT(new Item(21386), 200, FoodEffect.SARADOMIN_FRUIT),
    GUTHIX_FRUIT(new Item(21387), 200, FoodEffect.GUTHIX_FRUIT),
    ZAMORAK_FRUIT(new Item(21388), 200, FoodEffect.ZAMORAK_FRUIT),

    //Misc
    SPINACH_ROLL(new Item(1969), 10),
    FROG_SPAWN(new Item(5004), 30, 30),
    ONION(new Item(1957), 20),
    KEBAB(new Item(1971), 40),
    CHOCOLATE_BAR(new Item(1973), 20, FoodEffect.CHOCOLATE_BAR),
    BANDAGES(new Item(14640), 120),
    JANGERBERRIES(new Item(247), 20),
    EDIBLE_SEAWEED(new Item(403), 40),
    PURPLE_SWEETS(new Item(4561), 30),
    PURPLE_SWEETS_2(new Item(10476), 30),
    OKTOBERTFEST_PRETZEL(new Item(19778), 120);

    Food(Item item, int heal) {
        this(item, heal, 0, -1, null);
    }

    Food(Item item, int heal, int random) {
        this(item, heal, random, -1, null);
    }

    Food(Item item, int heal, int random, int replace) {
        this(item, heal, random, replace, null);
    }

    Food(Item item, int heal, FoodEffect effect) {
        this(item, heal, 0, -1, effect);
    }

    Food(Item item, int heal, int replace, FoodEffect effect) {
        this(item, heal, 0, replace, effect);
    }

    Food(Item item, int heal, int random, int newId, FoodEffect effect) {
        this.item = item;
        this.heal = heal;
        this.random = random;
        this.newId = newId;
        this.effect = effect;
        this.name = (toString().toLowerCase().replaceAll("__", "-").replaceAll("_", " "));
    }

   private Item item;
   private int heal;
   private int random;
   private String name;
   private int newId;
   private FoodEffect effect;

    static Map<Integer, Food> types = new HashMap<>();

    static {
        for (Food type : Food.values()) {
            types.put(type.item.getId(), type);
        }
    }

    public static Food forId(int itemId) {
        return types.get(itemId);
    }

    public int getNewId() {
        return newId;
    }

    public int getHeal() {
        return heal;
    }

    public FoodEffect getEffect() {
        return effect;
    }

    public Item getItem() {
        return item;
    }
}
