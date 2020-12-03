package com.fury.game.content.skill.free.runecrafting;

public enum StaffImbuing {
    WATER_STAFF(16977, 16997, 10, 5.5),
    EARTH_STAFF(16979, 17001, 20, 12),
    FIRE_STAFF(16981, 17005, 30, 20.5),
    AIR_STAFF(16983, 17009, 40, 29),
    CATALYTIC(16985, 17013, 50, 39.5),
    EMPOWERED_WATER_STAFF(16987, 16999, 60, 51),
    EMPOWERED_EARTH_STAFF(16989, 17003, 70, 63.5),
    EMPOWERED_FIRE_STAFF(16991, 17007, 80, 76),
    EMPOWERED_AIR_STAFF(16993, 17011, 90, 90.5),
    EMPOWERED_CATALYTIC(16995, 17015, 99, 106);

    StaffImbuing(int staffId, int imbuedId, int levelReq, double exp) {
        this.staffId = staffId;
        this.imbuedId = imbuedId;
        this.levelReq = levelReq;
        this.exp = exp;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getImbuedId() {
        return imbuedId;
    }

    public int getLevelReq() {
        return levelReq;
    }

    public double getExp() {
        return exp;
    }

    private int staffId, imbuedId, levelReq;
    private double exp;
}
