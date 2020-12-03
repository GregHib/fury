package com.fury.game.content.skill.free.mining.data;

public enum Ores {
    CLAY(1, 5, 434, 10, 1, 11552, 5, 0),
    COPPER(1, 17.5, 436, 10, 1, 11552, 5, 0),
    TIN(1, 17.5, 438, 15, 1, 11552, 5, 0),
    BLURITE(10, 17.5, 668, 15, 1, 11552, 7, 0),
    IRON(15, 35, 440, 15, 1, 11552, 10, 0),
    SANDSTONE(35, 30, 6971, 30, 1, 11552, 10, 0),
    SILVER(20, 40, 442, 25, 1, 11552, 20, 0),
    COAL(30, 50, 453, 40, 10, 11552, 30, 0),
    GRANITE(45, 50, 6979, 50, 10, 11552, 20, 0),
    GOLD(40, 60, 444, 80, 20, 11554, 40, 0),
    MITHRIL(55, 80, 447, 100, 20, 11552, 60, 0),
    ADAMANT(70, 95, 449, 130, 25, 11552, 180, 0),
    RUNITE(85, 125, 451, 150, 30, 11552, 360, 0),
    LRC_COAL(77, 50, 453, 50, 10, -1, -1, -1),
    LRC_GOLD(80, 60, 444, 40, 10, -1, -1, -1),
    RED_SANDSTONE(81, 70, 23194, 50, 10, -1, -1, -1);
    private int level;
    private double xp;
    private int oreId;
    private int oreBaseTime;
    private int oreRandomTime;
    private int emptySpot;
    private int respawnDelay;
    private int randomLifeProbability;

    Ores(int level, double xp, int oreId, int oreBaseTime, int oreRandomTime, int emptySpot, int respawnDelay, int randomLifeProbability) {
        this.level = level;
        this.xp = xp;
        this.oreId = oreId;
        this.oreBaseTime = oreBaseTime;
        this.oreRandomTime = oreRandomTime;
        this.emptySpot = emptySpot;
        this.respawnDelay = respawnDelay;
        this.randomLifeProbability = randomLifeProbability;
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

    public int getEmptyId() {
        return emptySpot;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getRandomLifeProbability() {
        return randomLifeProbability;
    }

    public int getEmptySpot() {
        return emptySpot;
    }
}
