package com.fury.game.content.skill.free.cooking;

public enum Pies {
    REDBERRY_PIE(2321, 2325, 10, 78, 50, "redberry pie"),
    MEAT_PIE(2319, 2327, 20, 110, 55, "meat pie"),
    MUD_PIE(7168, 7170, 29, 128, 60, "mud pie"),
    APPLE_PIE(2317, 2323, 30, 130, 65, "apple pie"),
    GARDEN_PIE(7176, 7178, 34, 138, 70, "garden pie"),
    FISH_PIE(7186, 7188, 47, 164, 75, "fish pie"),
    ADMIRAL_PIE(7196, 7198, 70, 210, 80, "admiral pie"),
    WILD_PIE(7206, 7208, 85, 240, 90, "wild pie"),
    SUMMER_PIE(7216, 7218, 95, 264, 99, "summer pie");

    private int id, req, anim;
    private double speed;

    int rawItem, cookedItem, levelReq, xp, stopBurn; String name;

    Pies(int rawItem, int cookedItem, int levelReq, int xp, int stopBurn, String name) {
        this.rawItem = rawItem;
        this.cookedItem = cookedItem;
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
        return 2329;
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

    public static Pies forPie(int pie) {
        for(Pies data: Pies.values()) {
            if(data.getRawItem() == pie) {
                return data;
            }
        }
        return null;
    }
}
