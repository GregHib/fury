package com.fury.game.entity.character.combat;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.HitMask;

/**
 * A class that represents a hit inflicted on an figure.
 * 
 * @author lare96
 */
public class Hit {

    /** The amount of damage inflicted in this hit. */
    private int damage;
    
    /** The amount of damage absorbed in this hit */
    private int absorb;

    private HitMask hitMask;
    
    private CombatIcon combatIcon;


    private Item weapon;

    /**
     * Create a new {@link Hit}.
     * 
     * @param damage
     *            the amount of damage in this hit.
     * @param type
     *            the type of hit this is.
     */
    public Hit(int damage, HitMask hitMask, CombatIcon combatIcon) {
        this.damage = damage;
        this.hitMask = hitMask;
        this.combatIcon = combatIcon;
        this.absorb = 0;
        this.modify();
    }
    private Figure source;

    public Hit(Figure source, int damage, HitMask hitMask, CombatIcon combatIcon) {
        this.damage = damage;
        this.hitMask = hitMask;
        this.combatIcon = combatIcon;
        this.absorb = 0;
        this.modify();
        this.source = source;
    }

    public Hit(Figure source, Item weapon, int damage, HitMask hitMask, CombatIcon combatIcon) {
        this.damage = damage;
        this.weapon = weapon;
        this.hitMask = hitMask;
        this.combatIcon = combatIcon;
        this.absorb = 0;
        this.modify();
        this.source = source;
    }
    /**
     * Create a new {@link Hit} with a default {@link HitType} of
     * <code>NORMAL</code>.
     * 
     * @param damage
     *            the amount of damage in this hit.
     */

    @Override
    public Hit clone() {
        return new Hit(source, damage, hitMask, combatIcon);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Hit)) {
            return false;
        }

        Hit hit = (Hit) o;
        return (hit.damage == damage && hit.hitMask == hitMask && hit.combatIcon == combatIcon);
    }

    private void modify() {
        if (this.damage == 0 && this.combatIcon != CombatIcon.BLOCK) {
            this.combatIcon = CombatIcon.BLOCK;
        } else if (this.damage < 0) {
            this.damage = 0;
            this.combatIcon = CombatIcon.BLOCK;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
        this.modify();
    }
    
    public void incrementAbsorbedDamage(int absorb) {
    	this.damage -= absorb;
    	this.absorb += absorb;
    	this.modify();
    }

    public Item getWeapon() {
        return weapon;
    }

    public HitMask getHitMask() {
        return hitMask;
    }

    public void setHitMask(HitMask hitMask) {
        this.hitMask = hitMask;
    }
    
    public CombatIcon getCombatIcon() {
    	return combatIcon;
    }

    public void setCombatIcon(CombatIcon combatIcon) {
        this.combatIcon = combatIcon;
    }
    
    public int getAbsorb() {
    	return absorb;
    }
    
    public void setAbsorb(int absorb) {
    	this.absorb = absorb;
    }

    public Figure getSource() {
        return source;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public void setSource(Figure source) {
        this.source = source;
    }
}
