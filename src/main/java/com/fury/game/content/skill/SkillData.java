package com.fury.game.content.skill;

public class SkillData {

    public SkillData() {
        level = new int[Skill.values().length];
        maxLevel = new int[Skill.values().length];
        experience = new double[Skill.values().length];
    }

    private int[] level;
    private int[] maxLevel;
    private double[] experience;

    public int getLevel(Skill skill) {
        return level[skill.ordinal()];
    }

    public void setLevel(Skill skill, int level) {
        this.level[skill.ordinal()] = level;
    }

    public int getMaxLevel(Skill skill) {
        return maxLevel[skill.ordinal()];
    }

    public void setMaxLevel(Skill skill, int maxLevel) {
        this.maxLevel[skill.ordinal()] = maxLevel;
    }

    public double getExperience(Skill skill) {
        return experience[skill.ordinal()];
    }

    public void setExperience(Skill skill, double experience) {
        this.experience[skill.ordinal()] = experience;
    }
}
