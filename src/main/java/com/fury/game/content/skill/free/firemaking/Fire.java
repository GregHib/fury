package com.fury.game.content.skill.free.firemaking;

public enum Fire {
    NORMAL(1511, 1, 300, 70755, 40, 20),
    ACHEY(2862, 1, 300, 70756, 40, 1),
    OAK(1521, 15, 450, 70757, 60, 1),
    WILLOW(1519, 30, 450, 70758, 90, 1),
    TEAK(6333, 35, 450, 70759, 105, 1),
    ARCTIC_PINE(10810, 42, 500, 70760, 125, 1),
    MAPLE(1517, 45, 500, 70761, 135, 1),
    MAHOGANY(6332, 50, 700, 70762, 157.5, 1),
    EUCALYPTUS(12581, 58, 700, 70763, 193.5, 1),
    YEW(1515, 60, 800, 70764, 202.5, 1),
    MAGIC(1513, 75, 900, 70765, 303.8, 1),
    CURSED_MAGIC(13567, 82, 1000, 70766, 303.8, 1),
    TANGLE_GUM_BRANCHES(17682, 1, 300, 49940, 25, 1),
    SEEPING_ELM_BRANCHES(17684, 10, 375, 49941, 44.5, 1),
    BLOOD_SPINDLE_BRANCHES(17686, 20, 410, 49942, 65.6, 1),
    UTUKU_BRANCHES(17688, 30, 450, 49943, 88.3, 1),
    SPINEBEAM_BRANCHES(17690, 40, 500, 49944, 112.6, 1),
    BOVISTRANGLER_BRANCHES(17692, 50, 700, 49945, 138.5, 1),
    THIGAT_BRANCHES(17694, 60, 700, 49946, 166, 1),
    CORPSETHRON_BRANCHES(17696, 70, 850, 49947, 195.1, 1),
    ENTGALLOW_BRANCHES(17698, 80, 925, 49948, 225.8, 1),
    GRAVE_CREEPER_BRANCHES(17700, 90, 1000, 49949, 258.1, 1);

    private int logId;
    private int level;
    private int life;
    private int fireId;
    private int time;
    private double xp;

    Fire(int logId, int level, int life, int fireId, double xp, int time) {
        this.logId = logId;
        this.level = level;
        this.life = life;
        this.fireId = fireId;
        this.xp = xp;
        this.time = time;
    }

    public int getLogId() {
        return logId;
    }

    public int getLevel() {
        return level;
    }

    public int getLife() {
        return (life * 120);
    }

    public int getFireId() {
        return fireId;
    }

    public double getExperience() {
        return xp;
    }

    public int getTime() {
        return time;
    }

    public static Fire forLogId(int id) {
        for (Fire fire : values())
            if (fire.getLogId() == id)
                return fire;
        return null;
    }
}
