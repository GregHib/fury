package com.fury.game.content.skill.member.hunter;

import com.fury.core.model.item.Item;

public enum HunterData {
    //Object id's can't be the same on two
    //List of box trap id's
    //19189, 19190, 19191, 28557, 28558, 28567, 28906, 28913, 28920, 28921, 28922, 28923, 28924, 28925, 28926, 28927, 28928, 28929, 29887, 29888, 29897, 29898, 29899, 29900, 29901, 29902, 29903, 29904, 29905, 29910, 29911, 29912, 29913, 43470, 43471, 43472, 43473
    BARB_TAILED_KEBBIT(Traps.BOULDER_TRAP, 23, 168, 19207, 5275, 5277, new Item(526), new Item(10129)),
    GREY_CHINCHOMPA(Traps.BOX, 53, 198.4, 28557, 5184, -1, new Item(10033)),
    RED_CHINCHOMPA(Traps.BOX, 63, 265, 28558, 5184, -1, new Item(10034)),
    FERRET(Traps.BOX, 27, 115, 19189, 5191, 5192),
    GECKO(Traps.BOX, 27, 100, 19190, 8362, 8361),
    RACCOON(Traps.BOX, 27, 100, 19191, 7726, 7727),
    MONKEY(Traps.BOX, 27, 100, 28557, 8343, 8345),
    PAWYA(Traps.BOX, 66, 400, 28567, 8615, 8611, new Item(526), new Item(12535)),
    GRENWALL(Traps.BOX, 77, 1100, 28906, 8607, 8603, new Item(12539, 18)),
    CRIMSON_SWIFT(Traps.SNARE, 1, 34, 19180, 5171, 5172, new Item(10088), new Item(526), new Item(9978)),
    GOLDEN_WARBLER(Traps.SNARE, 5, 48, 19184, 5171, 5172, new Item(1583), new Item(526), new Item(9978)),
    COPPER_LONGTAIL(Traps.SNARE, 9, 61, 19186, 5171, 5172, new Item(10091), new Item(526), new Item(9978)),
    CERULEAN_TWITCH(Traps.SNARE, 11, 64.67, 19182, 5171, 5172, new Item(10089), new Item(526), new Item(9978)),
    TROPICAL_WAGTAIL(Traps.SNARE, 19, 95.2, 19178, 5171, 5172, new Item(10087), new Item(526), new Item(9978)),
    WIMPY_BIRD(Traps.SNARE, 39, 167, 28930, 5171, 5172, new Item(11525), new Item(526), new Item(9978)),
    SWAMP_LIZARD(Traps.SWAMP_NET, 29, 152, 19678, -1, 5270, new Item(10149)),
    ORANGE_SALAMANDER(Traps.ORANGE_NET, 47, 224, 19650, -1, 5270, new Item(10146)),
    RED_SALAMANDER(Traps.RED_NET, 59, 272, 19662, -1, 5270, new Item(10147)),
    BLACK_SALAMANDER(Traps.BLACK_NET, 67, 304, 19670, -1, 5270, new Item(10148));

    private final Traps trap;
    private final int lureLevel;
    private final double exp;
    private int objectId;
    private int catchAnimId;
    private int failAnimId;
    private Item[] loot;

    HunterData(Traps trap, int lureLevel, double exp, int objectId, int catchAnimId, int failAnimId, Item... loot) {
        this.trap = trap;
        this.lureLevel = lureLevel;
        this.exp = exp;
        this.objectId = objectId;
        this.catchAnimId = catchAnimId;
        this.failAnimId = failAnimId;
        this.loot = loot;
    }

    public Traps getTrap() {
        return trap;
    }

    public int getLureLevel() {
        return lureLevel;
    }

    public double getExp() {
        return exp;
    }

    public Item[] getLoot() {
        return loot;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getCatchAnimId() {
        return catchAnimId;
    }

    public int getFailAnimId() {
        return failAnimId;
    }


    public static final int[] salamanderIds = new int[]{10149, 10146, 10147, 10148};

    public static boolean isSalamander(int id) {
        for (int salamander : salamanderIds)
            if (id == salamander)
                return true;
        return false;
    }
}
