package com.fury.game.content.skill.member.summoning;

import com.fury.game.container.impl.equip.Slot;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * This class model acts as an interface for a player's familiar.
 * Class auto generated by Eclipse
 * @author Gabriel Hannason
 */
public class OldFamiliar {

	public OldFamiliar(Player owner, Mob summonMob, int deathTimer) {
		this.owner = owner;
		this.summonMob = summonMob;
		this.deathTimer = deathTimer;
	}
	
	public OldFamiliar(Player owner, Mob summonMob) {
		this.owner = owner;
		this.summonMob = summonMob;
		this.petNpc = true;
		this.deathTimer = -1;
	}
	
	public int getSpecialEnergy() {
		return specialEnergy;
	}
	
	public boolean hasSpecialEnergy(Player p, int depletion) {
		//Spirit Cape
		if(p.getEquipment().get(Slot.CAPE).getId() == 19893)
			depletion = depletion - ((depletion/100)*20);
		if(specialEnergy - depletion >= 0) {
			return true;
		}
		return false;
	}
	
	public void depleteSpecial(Player p, int depletion) {
		//Spirit Cape
		if(p.getEquipment().get(Slot.CAPE).getId() == 19893 || p.getEquipment().get(Slot.CAPE).getId() == 20769)
			depletion = depletion - ((depletion/100)*20);
		if(specialEnergy - depletion >= 0) {
			specialEnergy -= depletion;
		}
	}
	public void increaseSpecialEnergy(int amount) {
		if(this.specialEnergy + amount > 60) {
			this.specialEnergy = 60;
		} else {
			this.specialEnergy += amount;
		}
	}
	public void setSpecialEnergy(int specialEnergy) {
		this.specialEnergy = specialEnergy;
	}
	
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Mob getSummonMob() {
		return summonMob;
	}

	public void setSummonMob(Mob summonMob) {
		this.summonMob = summonMob;
	}

	public int getDeathTimer() {
		return deathTimer;
	}

	public void setDeathTimer(int deathTimer) {
		this.deathTimer = deathTimer;
	}

	public int getSpecialTimer() {
		return specialTimer;
	}

	public void setSpecialTimer(int specialTimer) {
		this.specialTimer = specialTimer;
	}
	
	public boolean isRespawnNeeded() {
		return respawnNeeded;
	}

	public void setRespawnNeeded(boolean respawnNeeded) {
		this.respawnNeeded = respawnNeeded;
	}
	
	public boolean isPet() {
		return petNpc;
	}
	
	private boolean petNpc;
	private Player owner;
	private Mob summonMob;
	private int deathTimer;
	private int specialEnergy;
	private int specialTimer;
	private boolean respawnNeeded;
}