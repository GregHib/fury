package com.fury.game.entity.character.combat.equipment.weapon;

public enum WeaponInterface {
    STAFF(328, 331, 6, new FightType[]{FightType.STAFF_BASH, FightType.STAFF_POUND, FightType.STAFF_FOCUS}),
    WARHAMMER(425, 428, 6, new FightType[]{FightType.WARHAMMER_POUND, FightType.WARHAMMER_PUMMEL, FightType.WARHAMMER_BLOCK}, 7474, 7486),
    SCYTHE(776, 779, 6, new FightType[]{FightType.SCYTHE_REAP, FightType.SCYTHE_CHOP, FightType.SCYTHE_JAB, FightType.SCYTHE_BLOCK}),
    BATTLEAXE(1698, 1701, 6, new FightType[]{FightType.BATTLEAXE_CHOP, FightType.BATTLEAXE_HACK, FightType.BATTLEAXE_SMASH, FightType.BATTLEAXE_BLOCK}, 7499, 7511),
    CROSSBOW(1764, 1767, 5, new FightType[]{FightType.CROSSBOW_ACCURATE, FightType.CROSSBOW_RAPID, FightType.CROSSBOW_LONGRANGE}, 7524, 7536),
    SHORTBOW(1764, 1767, 5, new FightType[]{FightType.SHORTBOW_ACCURATE, FightType.SHORTBOW_RAPID, FightType.SHORTBOW_LONGRANGE}, 7549, 7561),
    LONGBOW(1764, 1767, 6, new FightType[]{FightType.LONGBOW_ACCURATE, FightType.LONGBOW_RAPID, FightType.LONGBOW_LONGRANGE}, 7549, 7561),
    DAGGER(2276, 2279, 4, new FightType[]{FightType.DAGGER_STAB, FightType.DAGGER_LUNGE, FightType.DAGGER_SLASH, FightType.DAGGER_BLOCK}, 7574, 7586),
    SWORD(2276, 2279, 5, new FightType[]{FightType.SWORD_STAB, FightType.SWORD_LUNGE, FightType.SWORD_SLASH, FightType.SWORD_BLOCK}, 7574, 7586),
    SCIMITAR(2423, 2426, 4, new FightType[]{FightType.SCIMITAR_CHOP, FightType.SCIMITAR_SLASH, FightType.SCIMITAR_LUNGE, FightType.SCIMITAR_BLOCK}, 7599, 7611),
    LONGSWORD(2423, 2426, 6, new FightType[]{FightType.LONGSWORD_CHOP, FightType.LONGSWORD_SLASH, FightType.LONGSWORD_LUNGE, FightType.LONGSWORD_BLOCK}, 7599, 7611),
    MACE(3796, 3799, 4, new FightType[]{FightType.MACE_POUND, FightType.MACE_PUMMEL, FightType.MACE_SPIKE, FightType.MACE_BLOCK}, 7624, 7636),
    KNIFE(4446, 4449, 4, new FightType[]{FightType.KNIFE_ACCURATE, FightType.KNIFE_RAPID, FightType.KNIFE_LONGRANGE}, 7649, 7661),
    SPEAR(4679, 4682, 6, new FightType[]{FightType.SPEAR_LUNGE, FightType.SPEAR_SWIPE, FightType.SPEAR_POUND, FightType.SPEAR_BLOCK}, 7674, 7686),
    TWO_HANDED_SWORD(4705, 4708, 6, new FightType[]{FightType.TWOHANDEDSWORD_CHOP, FightType.TWOHANDEDSWORD_SLASH, FightType.TWOHANDEDSWORD_SMASH, FightType.TWOHANDEDSWORD_BLOCK}, 7699, 7711),
    PICKAXE(5570, 5573, 6, new FightType[]{FightType.PICKAXE_SPIKE, FightType.PICKAXE_IMPALE, FightType.PICKAXE_SMASH, FightType.PICKAXE_BLOCK}, 7724, 7736),
    CLAWS(7762, 7765, 4, new FightType[]{FightType.CLAWS_CHOP, FightType.CLAWS_SLASH, FightType.CLAWS_LUNGE, FightType.CLAWS_BLOCK}, 7800, 7812),
    HALBERD(8460, 8463, 6, new FightType[]{FightType.HALBERD_JAB, FightType.HALBERD_SWIPE, FightType.HALBERD_FEND}, 8493, 8505),
    UNARMED(5855, 5857, 6, new FightType[]{FightType.UNARMED_PUNCH, FightType.UNARMED_KICK, FightType.UNARMED_BLOCK}),
    WHIP(12290, 12293, 4, new FightType[]{FightType.WHIP_FLICK, FightType.WHIP_LASH, FightType.WHIP_DEFLECT}, 12323, 12335),
    THROWNAXE(4446, 4449, 6, new FightType[]{FightType.THROWNAXE_ACCURATE, FightType.THROWNAXE_RAPID, FightType.THROWNAXE_LONGRANGE}, 7649, 7661),
    DART(4446, 4449, 4, new FightType[]{FightType.DART_ACCURATE, FightType.DART_RAPID, FightType.DART_LONGRANGE}, 7649, 7661),
    JAVELIN(4446, 4449, 6, new FightType[]{FightType.JAVELIN_ACCURATE, FightType.JAVELIN_RAPID, FightType.JAVELIN_LONGRANGE}, 7649, 7661),
    CHINCHOMPA(24055, 24056, 6, new FightType[]{FightType.CHINCHOMPA_SHORTFUSE, FightType.CHINCHOMPA_MEDIUMFUSE, FightType.CHINCHOMPA_LONGFUSE}),
    SALAMANDER(24074, 24075, 6, new FightType[]{FightType.SALAMANDER_SCORCH, FightType.SALAMANDER_FLARE, FightType.SALAMANDER_BLAZE});

    /**
     * The interface that will be displayed on the sidebar.
     */
    private int interfaceId;

    /**
     * The line that the name of the item will be printed to.
     */
    private int nameLineId;

    /**
     * The attack speed of weapons using this interface.
     */
    private int speed;

    /**
     * The fight types that correspond with this interface.
     */
    private FightType[] fightTypes;

    /**
     * The id of the special bar for this interface.
     */
    private int specialBar;

    /**
     * The id of the special meter for this interface.
     */
    private int specialMeter;

    /**
     * Creates a new weapon interface.
     *
     * @param interfaceId  the interface that will be displayed on the sidebar.
     * @param nameLineId   the line that the name of the item will be printed to.
     * @param speed        the attack speed of weapons using this interface.
     * @param fightTypes    the fight types that correspond with this interface.
     * @param specialBar   the id of the special bar for this interface.
     * @param specialMeter the id of the special meter for this interface.
     */
    WeaponInterface(int interfaceId, int nameLineId, int speed,
                    FightType[] fightTypes, int specialBar, int specialMeter) {
        this.interfaceId = interfaceId;
        this.nameLineId = nameLineId;
        this.speed = speed;
        this.fightTypes = fightTypes;
        this.specialBar = specialBar;
        this.specialMeter = specialMeter;
    }

    /**
     * Creates a new weapon interface.
     *
     * @param interfaceId the interface that will be displayed on the sidebar.
     * @param nameLineId  the line that the name of the item will be printed to.
     * @param speed       the attack speed of weapons using this interface.
     * @param fightTypes   the fight types that correspond with this interface.
     */
    WeaponInterface(int interfaceId, int nameLineId, int speed,
                    FightType[] fightTypes) {
        this(interfaceId, nameLineId, speed, fightTypes, -1, -1);
    }

    /**
     * Gets the interface that will be displayed on the sidebar.
     *
     * @return the interface id.
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets the line that the name of the item will be printed to.
     *
     * @return the name line id.
     */
    public int getNameLineId() {
        return nameLineId;
    }

    /**
     * Gets the attack speed of weapons using this interface.
     *
     * @return the attack speed of weapons using this interface.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Gets the fight types that correspond with this interface.
     *
     * @return the fight types that correspond with this interface.
     */
    public FightType[] getFightTypes() {
        return fightTypes;
    }

    /**
     * Gets the id of the special bar for this interface.
     *
     * @return the id of the special bar for this interface.
     */
    public int getSpecialBar() {
        return specialBar;
    }

    /**
     * Gets the id of the special meter for this interface.
     *
     * @return the id of the special meter for this interface.
     */
    public int getSpecialMeter() {
        return specialMeter;
    }
}
