package com.fury.game.content.skill.free.firemaking.bonfire;

import com.fury.core.model.item.Item;

public enum BonfireLogs {
    LOG(new Item(1511), 3098, 1, 40, 6),
    OAK(new Item(1521), 3099, 15, 60, 12),
    WILLOW(new Item(1519), 3101, 30, 90, 18),
    TEAK(new Item(6333), 3103, 35, 105, 24),
    MAPLE(new Item(1517), 3100, 45, 135, 36),
    MAHOGANY(new Item(6332), 3102, 50, 157.5, 42),
    EUCALYPTUS(new Item(12581), 3112, 58, 193, 48),
    YEWS(new Item(1515), 3111, 60, 202.5, 54),
    MAGIC(new Item(1513), 3135, 75, 303.8, 60),
    BLISTERWOOD(new Item(21600), 3113, 76, 434, 60),
    CURSED_MAGIC(new Item(13567), 3116, 82, 434, 60);
    private Item log;
    private int gfxId, level, boostTime;
    private double xp;

    BonfireLogs(Item log, int gfxId, int level, double xp, int boostTime) {
        this.log = log;
        this.gfxId = gfxId;
        this.level = level;
        this.xp = xp;
        this.boostTime = boostTime;
    }

    public Item getLog() {
        return log;
    }

    public int getGfxId() {
        return gfxId;
    }

    public int getLevel() {
        return level;
    }

    public int getBoostTime() {
        return boostTime;
    }

    public double getXp() {
        return xp;
    }
}
