package com.fury.game.content.global.dnd.eviltree;

public enum EvilTreeTypes {
    //100%, 50%, 25%, death, fallen, reward
    NORMAL(new int[] {11434, 11435, 11436, 11925, 12713, 14839}, 1, 1, 20, 200, 3, 15.1, 6),
    OAK(new int[] {11437, 11438, 11439, 11926, 12714, 14840}, 15, 7, 45, 300, 6.4, 32.4, 10),
    WILLOW(new int[] {11440, 11441, 11442, 11927, 12715, 14841}, 30, 15, 66, 450, 9.1, 45.7, 14),
    MAPLE(new int[] {11443, 11444, 11915, 11928, 14835, 14842}, 45, 22, 121.5, 675, 11.1, 55.8, 18),
    YEW(new int[] {11916, 11917, 11918, 11929, 14836, 14843}, 60, 30, 172.5, 1012.5, 17.5, 87.5, 22),
    MAGIC(new int[] {11919, 11920, 11921, 12711, 14837, 14844}, 75, 37, 311.5, 1517.5, 25, 125, 26),
    ELDER(new int[] {11922, 11923, 11924, 12712, 14838, 14845}, 90, 42, 730, 2600.5, 32.5, 162.5, 30);

    private int[] ids;
    private int requiredLevel, requiredFarming, buff;

    public int getHealthy() {
        return ids[0];
    }

    public int getHalfHealth() {
        return ids[1];
    }

    public int getQuaterHealth() {
        return ids[2];
    }

    public int getDeath() {
        return ids[3];
    }

    public int getFallen() {
        return ids[4];
    }

    public int getReward() {
        return ids[5];
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getRequiredFarming() {
        return requiredFarming;
    }

    public int getSeedHeath() {
        return (ordinal() + 1) * 2;
    }

    public int getHeath() {
        return (ordinal() + 1) * 150;
    }

    public int getBuff() {
        return buff;
    }

    public double getNurture() {
        return nurture;
    }

    public double getBurn() {
        return burn;
    }

    public double getRoot() {
        return root;
    }

    public double getTree() {
        return tree;
    }

    private double nurture, burn, root, tree;
    EvilTreeTypes(int[] ids, int requiredLevel, int requiredFarming, double nurture, double burn, double root, double tree, int buff) {
        this.ids = ids;
        this.requiredLevel = requiredLevel;
        this.requiredFarming = requiredFarming;
        this.nurture = nurture;
        this.burn = burn;
        this.root = root;
        this.tree = tree;
        this.buff = buff;
    }

    public static EvilTreeTypes getTree(int id) {
        for(EvilTreeTypes type : values())
            for(int i : type.ids)
                if(i == id)
                    return type;
        return null;
    }
}
