package com.fury.game.content.skill.free.runecrafting;

import com.fury.cache.def.Loader;

public enum RuneData {
    AIR_RUNE(556, 17780, 1, 5, 2478, false),
    MIND_RUNE(558, 17784, 2, 5.5, 2479, false),
    WATER_RUNE(555, 17781, 5, 6, 2480, false),
    EARTH_RUNE(557, 17782, 9, 6.5, 2481, false),
    FIRE_RUNE(554, 17783, 14, 7, 2482, false),
    BODY_RUNE(559, 17788, 20, 7.5, 2483, false),
    COSMIC_RUNE(564, 17789, 27, 8, 2484, true),
    CHAOS_RUNE(562, 17785, 35, 8.5, 2487, true),
    ASTRAL_RUNE(9075, 17790, 40, 8.7, 17010, true),
    NATURE_RUNE(561, 17791, 44, 9, 2486, true),
    LAW_RUNE(563, 17792, 54, 9.5, 2485, true),
    DEATH_RUNE(560, 17786, 65, 10, 2488, true),
    BLOOD_RUNE(565, 17787, 75, 10.5, 30624, true),
    SOUL_RUNE(566, 17793, 90, 11, -1, true),
    ZMI(-1, -1, 1, 1, 26847, true);//,
    //ARMADYL_RUNE(21773, 77, 1410, 47120, true);

    RuneData(int rune, int dungeoneeringRune, int levelReq, double xpReward, int altarObjectID, boolean pureRequired) {
        this.runeID = rune;
        this.dungeoneeringRuneID = dungeoneeringRune;
        this.levelReq = levelReq;
        this.xpReward = xpReward;
        this.altarObjectID = altarObjectID;
        this.pureRequired = pureRequired;
    }

    private int runeID, dungeoneeringRuneID;
    private int levelReq;
    private double xpReward;
    private int altarObjectID;
    private boolean pureRequired;

    public int getRuneID() {
        return this.runeID;
    }

    public int getDungRuneID() {
        return this.dungeoneeringRuneID;
    }

    public int getLevelRequirement() {
        return this.levelReq;
    }

    public double getXP() {
        return this.xpReward;
    }

    public int getAltarID() {
        return this.altarObjectID;
    }

    public boolean pureRequired() {
        return this.pureRequired;
    }

    public String getName() {
        return Loader.getItem(runeID).getName();
    }

    public static RuneData forId(int objectId) {
        for(RuneData runes : RuneData.values()) {
            if(runes.getAltarID() == objectId) {
                return runes;
            }
        }
        return null;
    }
    public static RuneData forId(int runeId, boolean dungeoneering) {
        for(RuneData rune : RuneData.values()) {
            if((dungeoneering ? rune.getDungRuneID() : rune.getRuneID()) == runeId) {
                return rune;
            }
        }
        return null;
    }
}
