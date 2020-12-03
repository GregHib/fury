package com.fury.game.content.skill.free.mining.impl.essence;

public enum Essence {
    RUNE_ESSENCE(1, 5, 1436, 1, 1),
    PURE_ESSENCE(30, 5, 7936, 1, 1);
    private int level;
    private double xp;
    private int oreId;
    private int oreBaseTime;
    private int oreRandomTime;

    Essence(int level, double xp, int oreId, int oreBaseTime, int oreRandomTime) {
        this.level = level;
        this.xp = xp;
        this.oreId = oreId;
        this.oreBaseTime = oreBaseTime;
        this.oreRandomTime = oreRandomTime;
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public int getOreId() {
        return oreId;
    }

    public int getOreBaseTime() {
        return oreBaseTime;
    }

    public int getOreRandomTime() {
        return oreRandomTime;
    }

}
