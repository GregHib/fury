package com.fury.game.content.skill.free.woodcutting;

public enum Trees {
    NORMAL(1, 25, 1511, 20, 4, 1342, 8, 0),
    DRAMEN(36, 0, 771, 20, 4, 1342, 8, 0),
    EVERGREEN(1, 25, 1511, 20, 4, 1342, 8, 0),
    DEAD(1, 25, 1511, 20, 4, 1342, 8, 0),
    OAK(15, 37.5, 1521, 30, 4, 1356, 15, 15),
    WILLOW(30, 67.5, 1519, 45, 4, 7399, 51, 15),
    TEAK(35, 85, 6333, 50, 4, 9037, 60, 10),
    MAPLE(45, 100, 1517, 60, 5, 1343, 72, 10),
    MAHOGANY(50, 125, 6332, 70, 5, 9035, 83, 10),
    YEW(60, 175, 1515, 85, 6, 7402, 94, 10),
    IVY(68, 332.5, null, 85, 6, 46319, 58, 15),
    MAGIC(75, 250, 1513, 95, 7, 7401, 121, 10),
    CURSED_MAGIC(82, 250, 1513, 90, 9, 37822, 121, 10),
    FRUIT_TREES(1, 25, null, 20, 4, 1341, 8, 0),
    MUTATED_ROOT(83, 140, 21358, 83, 8, 5, -1, 8),
    CURLY_ROOT(83, 140, null, 83, 8, 12279, 72, 5),
    CURLY_ROOT_CUT(83, 140, new int[] {21350, 21350, 21350, 21350}, 83, 8, 12283, 72, 1),
    STRAIT_ROOT(83, 140, null, 83, 8, 12277, 72, 5),
    STRAIT_ROOT_CUT(83, 140, new int[] {21349, 21349, 21349, 21349}, 83, 8, 12283, 72, 1),
    TANGLE_GUM_VINE(1, 35, 17682, 20, 4, 49706, 8, 5),
    SEEPING_ELM_TREE(10, 60, 17684, 25, 4, 49708, 12, 5),
    BLOOD_SPINDLE_TREE(20, 85, 17686, 35, 4, 49710, 16, 5),
    UTUKU_TREE(30, 115, 17688, 40, 3, 49712, 51, 5),
    SPINEBEAM_TREE(40, 145, 17690, 50, 3, 49714, 68, 5),
    BOVISTRANGLER_TREE(50, 175, 17692, 60, 4, 49716, 75, 5),
    THIGAT_TREE(60, 210, 17694, 70, 5, 49718, 83, 10),
    CORPESTHORN_TREE(70, 245, 17696, 80, 6, 49720, 90, 10),
    ENTGALLOW_TREE(80, 285, 17698, 90, 7, 49722, 94, 10),
    GRAVE_CREEPER_TREE(90, 330, 17700, 95, 8, 49724, 121, 10);

    private int level;
    private double xp;
    private int[] logsId;
    private int logBaseTime;
    private int logRandomTime;
    private int stumpId;
    private int respawnDelay;
    private int randomLifeProbability;

    Trees(int level, double xp, int[] logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
        this.level = level;
        this.xp = xp;
        this.logsId = logsId;
        this.logBaseTime = logBaseTime;
        this.logRandomTime = logRandomTime;
        this.stumpId = stumpId;
        this.respawnDelay = respawnDelay;
        this.randomLifeProbability = randomLifeProbability;
    }

    Trees(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
        this(level, xp, new int[]{logsId}, logBaseTime, logRandomTime, stumpId, respawnDelay, randomLifeProbability);
    }

    public int getLevel() {
        return level;
    }

    public double getXp() {
        return xp;
    }

    public int[] getLogsId() {
        return logsId;
    }


    /*
     * Approx the level should be easy to cut
     */
    public int getLogBaseTime() {
        return logBaseTime;
    }

    /*
     * If base time < 0 use this cut time
     */
    public int getLogRandomTime() {
        return logRandomTime;
    }

    public int getStumpId() {
        return stumpId;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getRandomLifeProbability() {
        return randomLifeProbability;
    }
}
