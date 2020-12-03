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

public class SpiritGuthatrice extends Familiar {
	private int egg;

	public SpiritGuthatrice(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public void processNpc() {
		super.processNpc();
		egg++;
		if (egg == 500)
			addEgg();
	}

	private void addEgg() {
		getBeastOfBurden().add(new Item(12109, 1));
		egg = 0;
	}

	@Override
	public String getSpecialName() {
		return "Petrifying Gaze";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts damage and drains a combat stat, which varies according to the type of cockatrice.";
	}

	@Override
	public int getStoreSize() {
		return 30;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 5;
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
		animate(7766);
		perform(new Graphic(1467));
		ProjectileManager.send(new Projectile(this, target, 1468, 34, 16, 30, 35, 16, 0));
		if (target.isPlayer()) {
			Player playerTarget = (Player) target;
			playerTarget.getSkills().drain(Skill.ATTACK, 3);
		}
		GameWorld.schedule(2, () -> target.getCombat().applyHit(new Hit(getOwner(), Misc.random(100), HitMask.RED, CombatIcon.MELEE)));
		return true;
	}

}
