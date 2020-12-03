package com.fury.game.content.skill.member.thieving;

public enum Stalls {
    VEGETABLE(4706, 2, new int[]{1957, 1965, 1942, 1982, 1550}, 1, 2, 10, 4276),
    CAKE(34384, 5, new int[]{1891, 1897, 2309, 1973}, 1, 2.5, 16, 34381),
    TEA_STALL(635, 5, new int[]{712}, 1, 7, 16, 634),
    SILK_STALL(34383, 20, new int[]{950}, 1, 8, 24, 34381),
    WINE_STALL(14011, 22, new int[]{1937, 1993, 1987, 1935, 7919}, 1, 16, 27, 14012),
    SEED_STALL(7053, 27, new int[]{5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5105}, 30, 11, 10, 14012),
    GREY_FUR_STALL(34387, 35, new int[]{958}, 1, 15, 36, 34381),
    FUR_STALL(4278, 35, new int[]{6814}, 1, 15, 36, 4276),
    FISH_STALL(4277, 42, new int[]{331, 359, 377}, 1, 16, 42, 4276),
    //CROSSBOW_STALL(0, 49, new int[] { 877, 9420, 9440 }, 1, 11, 52, 34381),
    SILVER_STALL(34382, 50, new int[]{442}, 1, 30, 54, 34381),
    SPICE_STALL(34386, 65, new int[]{2007}, 1, 80, 81, 34381),
    CRAFTING(4874, 1, new int[]{1755, 1592, 1597, 1635}, 1, 7, 34, -1),
    MONKEY_FOOD(4875, 25, new int[]{1963}, 1, 7, 44, -1),
    MONKEY_GENERAL(4876, 45, new int[]{1931, 2347, 590, 1415}, 1, 7, 66, -1),
    MAGIC_STALL(4877, 65, new int[]{556, 557, 554, 555, 563, 1436, 7936}, 30, 80, 88, -1),
    SCIMITAR_STALL(4878, 85, new int[]{1323, 6721}, 1, 80, 106, -1),
    GEM_STALL(34385, 75, new int[]{1623, 1621, 1619, 1617}, 1, 180, 16, 34381);
    private int[] item;
    private int level;
    private int amount;
    private int objectId;
    private int replaceObject;
    private double experience;
    private double seconds;

    Stalls(int objectId, int level, int[] item, int amount, double seconds, double experience, int replaceObject) {
        this.objectId = objectId;
        this.level = level;
        this.item = item;
        this.amount = amount;
        this.seconds = seconds;
        this.experience = experience;
        this.replaceObject = replaceObject;
    }

    public int getReplaceObject() {
        return replaceObject;
    }

    public int getObjectId() {
        return objectId;
    }

    public int[] getItems() {
        return item;
    }

    public int getItem(int count) {
        return item[count];
    }

    public int getAmount() {
        return amount;
    }

    public int getLevel() {
        return level;
    }

    public double getTime() {
        return seconds;
    }

    public double getExperience() {
        return experience;
    }
}
