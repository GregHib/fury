package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

import java.util.Collection;

public class GiantChinchompa extends Familiar {

	public GiantChinchompa(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Explode";
	}

	@Override
	public String getSpecialDescription() {
		return "Explodes, damaging nearby enemies.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		animate(7750);
		graphic(1310);
		forceChat("Squeek!");
		Player player = getOwner();
		Collection<Player> players = getRegion().getPlayers(getZ());
		for(Player p2: players) {
			if (p2 == null || p2.isDead() || p2 != player || !p2.getSettings().getBool(Settings.RUNNING) || !p2.isWithinDistance(player, 2))
				continue;
			p2.getCombat().applyHit(new Hit(this, Misc.random(130), HitMask.RED, CombatIcon.MAGIC));
		}
		return true;
	}
}
