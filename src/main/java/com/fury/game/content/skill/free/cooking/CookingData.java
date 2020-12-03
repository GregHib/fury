package com.fury.game.content.skill.free.cooking;

import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.security.SecureRandom;

/**
 * Data for the cooking skill.
 *
 * @author Admin Gabriel
 */
public enum CookingData {

    CAKE(1889, 1891, 1903, 40, 180, 65, "cake"),
    BAKED_POTATO(1942, 6701, 6699, 7, 15, 40, "baked potato"),
    PLAIN_PIZZA(2287, 2289, 2305, 35, 143, 68, "plain pizza"),
    RAT_MEAT(2134, 2142, 2146, 1, 30, 30, "rat meat"),
    SARDINE(327, 325, 369, 1, 40, 37, "sardine"),
    BEEF(2132, 2142, 2146, 1, 30, 30, "beef"),
    YAK_MEAT(10816, 2142, 2146, 1, 30, 30, "meat"),
    SHRIMP(317, 315, 7954, 1, 30, 33, "shrimp"),
    ANCHOVIES(321, 319, 323, 1, 30, 34, "anchovies"),
    HERRING(345, 347, 357, 5, 50, 41, "herring"),
    MACKEREL(353, 355, 357, 10, 60, 45, "mackerel"),
    TROUT(335, 333, 343, 15, 77, 50, "trout"),
    COD(341, 339, 343, 18, 90, 54, "cod"),
    SALMON(331, 329, 343, 25, 70, 58, "salmon"),
    TUNA(359, 361, 367, 30, 100, 58, "tuna"),
    LOBSTER(377, 379, 381, 40, 120, 74, "lobster"),
    BASS(363, 365, 367, 40, 130, 75, "bass"),
    SWORDFISH(371, 373, 375, 45, 140, 86, "swordfish"),
    MONKFISH(7944, 7946, 7948, 62, 160, 91, "monkfish"),
    SHARK(383, 385, 387, 80, 220, 94, "shark"),
    SEA_TURTLE(395, 397, 399, 82, 221, 105, "sea turtle"),
    MANTA_RAY(389, 391, 393, 91, 226, 99, "manta ray"),
    ROCKTAIL(15270, 15272, 15274, 92, 235, 93, "rocktail"),


    HEIM_CRAB(17797, 18159, 18179, 5, 50, 40, "heim crab"),
    RED_EYE(17799, 18161, 18181, 10, 75, 45, "red-eye"),
    DUSK_EEL(17801, 18163, 18183, 12, 82, 47, "dusk eel"),
    GIANT_FLATFISH(17803, 18165, 18185, 15, 90, 50, "giant flatfish"),
    SHORT_FINNED_EEL(17805, 18167, 18187, 18, 114, 54, "short-finned eel"),
    WEB_SNIPPER(17807, 18169, 18189, 30, 211, 60, "web snipper"),
    BOULDABASS(17809, 18171, 18191, 40, 288, 75, "bouldabass"),
    SALVE_EEL(17811, 18173, 18193, 60, 415, 81, "salve eel"),
    BLUE_CRAB(17813, 18175, 18195, 75, 1066, 92, "blue crab");

    int rawItem, cookedItem, burntItem, levelReq, xp, stopBurn;
    String name;

    CookingData(int rawItem, int cookedItem, int burntItem, int levelReq, int xp, int stopBurn, String name) {
        this.rawItem = rawItem;
        this.cookedItem = cookedItem;
        this.burntItem = burntItem;
        this.levelReq = levelReq;
        this.xp = xp;
        this.stopBurn = stopBurn;
        this.name = name;
    }

    public int getRawItem() {
        return rawItem;
    }

    public int getCookedItem() {
        return cookedItem;
    }

    public int getBurntItem() {
        return burntItem;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public int getXp() {
        return xp;
    }

    public int getStopBurn() {
        return stopBurn;
    }

    public String getName() {
        return name;
    }

    public static CookingData forFish(int fish) {
        for (CookingData data : CookingData.values()) {
            if (data.getRawItem() == fish) {
                return data;
            }
        }
        return null;
    }

    public static final int[] cookingRanges = {21302, 12269, 2732, 114};

    public static boolean isRange(int object) {
        for (int i : cookingRanges)
            if (object == i)
                return true;
        return false;
    }

    /**
     * Get's the rate for burning or successfully cooking food.
     *
     * @param player    Player cooking.
     * @param burnBonus Burn chance
     * @param levelReq  Level required to cook
     * @param stopBurn  Level when stop burning
     * @return Successfully cook food.
     */
    public static boolean success(Player player, int burnBonus, int levelReq, int stopBurn) {
        if (player.getSkills().getLevel(Skill.COOKING) >= stopBurn) {
            return true;
        }
        double burn_chance = (45.0 - burnBonus);
        double cook_level = player.getSkills().getLevel(Skill.COOKING);
        double lev_needed = (double) levelReq;
        double burn_stop = (double) stopBurn;
        double multi_a = (burn_stop - lev_needed);
        double burn_dec = (burn_chance / multi_a);
        double multi_b = (cook_level - lev_needed);
        burn_chance -= (multi_b * burn_dec);
        double randNum = cookingRandom.nextDouble() * 100.0;
        return burn_chance <= randNum;
    }

    private static SecureRandom cookingRandom = new SecureRandom(); // The random factor

    public static boolean canCookFish(Player player, int id) {
        CookingData fish = forFish(id);
        if (fish == null)
            return false;

        if(!player.getSkills().hasRequirement(Skill.COOKING, fish.getLevelReq(), "cook this"))
            return false;

        if (!player.getInventory().contains(new Item(id))) {
            player.message("You have run out of fish.");
            return false;
        }
        return true;
    }

    public static boolean canCookPie(Player player, int id) {
        Pies pie = Pies.forPie(id);
        if (pie == null)
            return false;
        if(!player.getSkills().hasRequirement(Skill.COOKING, pie.getLevelReq(), "cook this"))
            return false;
        if (!player.getInventory().contains(new Item(id))) {
            player.message("You have run out of pies.");
            return false;
        }
        return true;
    }

    public static boolean handlePieItems(Player player, int item1, int item2) {
        int[][] recipes = new int[][]{
                {1953, 2313, 2315},//Pie shell
                {1951, 2315, 2321},//Uncooked berry pie
                {2142, 2315, 2319},//Uncooked meat pie
                {2140, 2315, 2319},//Uncooked meat pie - chicken
                {6032, 2315, 7164},//Mud pie part 1
                {1912, 7164, 7166},//Mud pie part 2
                {434, 7166, 7168},//Raw mud pie
                {1955, 2315, 2317},//Uncooked apple pie
                {1982, 2315, 7172},//Garden pie part 1
                {1957, 7172, 7174},//Garden pie part 2
                {1965, 7174, 7176},//Raw garden pie
                {333, 2315, 7182},//Fish pie part 1
                {339, 7182, 7184},//Fish pie part 2
                {1942, 7184, 7186},//Raw fish pie
                {329, 2315, 7192},//Admiral pie part 1
                {361, 7192, 7194},//Admiral pie part 2
                {1942, 7194, 7196},//Raw admiral pie
                {2136, 2315, 7202},//Wild pie part 1
                {2876, 7202, 7204},//Wild pie part 2
                {3226, 7204, 7206},//Raw wild pie
                {5504, 2315, 7212},//Summer pie part 1
                {5982, 7212, 7214},//Summer pie part 2
                {1955, 7214, 7216}//Raw summer pie
        };
        for (int[] items : recipes) {
            if (item1 == items[0] && item2 == items[1]) {
                player.getInventory().delete(new Item(item1), new Item(item2));
                if (item1 == 6032 || item1 == 1912) player.getInventory().add(new Item(1925));//Empty bucket
                player.getInventory().add(new Item(items[2]));
            }
        }
        return false;
    }
}
