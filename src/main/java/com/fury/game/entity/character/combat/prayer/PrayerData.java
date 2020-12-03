package com.fury.game.entity.character.combat.prayer;

import com.fury.util.NameUtils;

import java.util.HashMap;

public enum PrayerData {
    THICK_SKIN(1, 1, 83),
    BURST_OF_STRENGTH(4, 1, 84),
    CLARITY_OF_THOUGHT(7, 1, 85),
    SHARP_EYE(8, 1, 601),
    MYSTIC_WILL(9, 1, 602),
    ROCK_SKIN(10, 2, 86),
    SUPERHUMAN_STRENGTH(13, 2, 87),
    IMPROVED_REFLEXES(16, 2, 88),
    RAPID_RESTORE(19, .4, 89),
    RAPID_HEAL(22, .6, 90),
    PROTECT_ITEM(25, .6, 91),
    HAWK_EYE(26, 1.5, 603),
    MYSTIC_LORE(27, 2, 604),
    STEEL_SKIN(28, 4, 92),
    ULTIMATE_STRENGTH(31, 4, 93),
    INCREDIBLE_REFLEXES(34, 4, 94),
    PROTECT_FROM_SUMMONING(35, 4, 631, 2),
    PROTECT_FROM_MAGIC(37, 4, 95, 2),
    PROTECT_FROM_MISSILES(40, 4, 96, 1),
    PROTECT_FROM_MELEE(43, 4, 97, 0),
    EAGLE_EYE(44, 4, 605),
    MYSTIC_MIGHT(45, 4, 606),
    RETRIBUTION(46, 1, 98, 4),
    REDEMPTION(49, 2, 99, 5),
    SMITE(52, 6, 100, 685, 6),
    CHIVALRY(60, 8, 607),
    RAPID_RENEWAL(65, 4, 632),
    PIETY(70, 10, 608),
    RIGOUR(74, 11, 609),
    AUGURY(77, 11, 610);

    PrayerData(int requirement, double drainRate, int configId, int... hint) {
        this.requirement = requirement;
        this.drainRate = drainRate;
        this.configId = configId;
        if (hint.length > 0)
            this.hint = hint[0];
        this.buttonId = 25000 + (ordinal() * 2);
    }


    private int requirement;
    private int buttonId;
    public int configId;
    private double drainRate;
    private int hint = -1;
    private String name;

    public final String getPrayerName() {
        if (name == null)
            return NameUtils.capitalizeWords(toString().toLowerCase().replaceAll("_", " "));
        return name;
    }

    public static HashMap<Integer, PrayerData> prayerData = new HashMap<>();

    public static HashMap<Integer, PrayerData> actionButton = new HashMap<>();

    static {
        for (PrayerData pd : PrayerData.values()) {
            prayerData.put(pd.ordinal(), pd);
            actionButton.put(pd.buttonId, pd);
        }
    }

    public int getRequirement() {
        return requirement;
    }

    public int getButtonId() {
        return buttonId;
    }

    public int getConfigId() {
        return configId;
    }

    public double getDrainRate() {
        return drainRate;
    }

    public int getHint() {
        return hint;
    }

    public String getName() {
        return name;
    }
}
