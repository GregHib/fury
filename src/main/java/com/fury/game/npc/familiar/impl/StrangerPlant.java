package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class StrangerPlant extends Familiar {

	private int forageTicks;

	public StrangerPlant(Player player, Summoning.Pouches pouch, Position tile) {
		super(player, pouch, tile);
		player.getSkills().boost(Skill.FARMING, 1, 0.04);
		player.message("You feel a sudden urge to plant flowers.");
	}

	@Override
	public void processNpc() {
		super.processNpc();
		forageTicks++;
		if (forageTicks == 750)
			addStrangeFruit();
	}

	private void addStrangeFruit() {
		getBeastOfBurden().add(new Item(464, 1));
		forageTicks = 0;
	}

	@Override
	public String getSpecialName() {
		return "Poisonous Blast";
	}

	@Override
	public String getSpecialDescription() {
		return "Attack with 50% chance of poisoning your opponent and inflicting 20 damage";
	}

	@Override
	public int getStoreSize() {
		return 30;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Figure target = (Figure) object;
		getOwner().graphic(1316);
		getOwner().animate(7660);
		animate(8211);
		ProjectileManager.send(new Projectile(this, target, 1508, 34, 16, 30, 35, 16, 0));
		GameWorld.schedule(2, () -> {
			target.getCombat().applyHit(new Hit(getOwner(), Misc.random(20), HitMask.RED, CombatIcon.MAGIC));
			if (Misc.random(1) == 0)
				target.getEffects().makePoisoned(60);
			target.perform(new Graphic(1511));
		});
		return true;
	}
}
