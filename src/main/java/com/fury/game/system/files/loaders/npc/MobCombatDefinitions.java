package com.fury.game.system.files.loaders.npc;

public class MobCombatDefinitions {
    public static final int MELEE = 0;
    public static final int RANGE = 1;
    public static final int MAGE = 2;
    public static final int SPECIAL = 3;
    public static final int SPECIAL2 = 4; // follows no distance
    public static final int PASSIVE = 0;
    public static final int AGGRESSIVE = 1;

    private int hitpoints;
    private int attackAnim;
    private int defenceAnim;
    private int deathAnim;
    private int attackDelay;
    private int deathDelay;
    private int respawnDelay;
    private int maxHit;
    private int attackStyle;
    private int attackGraphic;
    private int attackProjectile;
    private int agressivenessType;

    public MobCombatDefinitions(int hitpoints, int attackAnim, int defenceAnim,
                                int deathAnim, int attackDelay, int deathDelay, int respawnDelay,
                                int maxHit, int attackStyle, int attackGraphic, int attackProjectile,
                                int agressivenessType) {
        this.hitpoints = hitpoints;
        this.attackAnim = attackAnim;
        this.defenceAnim = defenceAnim;
        this.deathAnim = deathAnim;
        this.attackDelay = attackDelay;
        this.deathDelay = deathDelay;
        this.respawnDelay = respawnDelay;
        this.maxHit = maxHit;
        this.attackStyle = attackStyle;
        this.attackGraphic = attackGraphic;
        this.attackProjectile = attackProjectile;
        this.agressivenessType = agressivenessType;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getDeathAnim() {
        return deathAnim;
    }

    public int getDefenceAnim() {
        return defenceAnim;
    }

    public int getAttackAnim() {
        return attackAnim;
    }

    public int getAttackGraphic() {
        return attackGraphic;
    }

    public int getAgressivenessType() {
        return agressivenessType;
    }

    public int getAttackProjectile() {
        return attackProjectile;
    }

    public int getAttackStyle() {
        return attackStyle;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public int getDeathDelay() {
        return deathDelay;
    }

    public boolean isAggressive() {
        return agressivenessType == AGGRESSIVE;
    }
}
