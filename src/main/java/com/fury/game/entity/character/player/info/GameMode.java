package com.fury.game.entity.character.player.info;

public enum GameMode {
    REGULAR(200, 40, 1, 10),
    EXTREME(25, 10, 1.05, 12),//5%
    LEGEND(5, 1, 1.10, 14),//10%
    IRONMAN(50, 20, 1.025, 16);//2.5%

    public double getCombatRate() {
        return cmbRate;
    }

    public double getSkillRate() {
        return skillRate;
    }

    public double getDropRate() {
        return dropRate;
    }

    public int getSpecRestore() {
        return specRestore;
    }

    private double cmbRate, skillRate, dropRate;
    private int specRestore;

    GameMode(double cmbRate, double skillRate, double dropRate, int specRestore) {
        this.cmbRate = cmbRate;
        this.skillRate = skillRate;
        this.dropRate = dropRate;
        this.specRestore = specRestore;
    }

    public boolean isIronMan() {
        return this == IRONMAN;
    }
}
