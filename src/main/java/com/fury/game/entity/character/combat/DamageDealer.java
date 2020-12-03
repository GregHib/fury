package com.fury.game.entity.character.combat;

import com.fury.core.model.node.entity.actor.figure.player.Player;

public class DamageDealer {
	
	public DamageDealer(Player p, int damage) {
		this.p = p;
		this.damage = damage;
	}
	
	private Player p;
	private int damage;
	
	public Player getPlayer() {
		return this.p;
	}
	
	public int getDamage() {
		return this.damage;
	}
}