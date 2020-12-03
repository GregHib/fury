package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class AbyssalLurker extends Familiar {

	public AbyssalLurker(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Abyssal Stealth";
	}

	@Override
	public String getSpecialDescription() {
		return "Temporarily increases a player's Agility and Thieving by 4 levels.";
	}

	@Override
	public int getStoreSize() {
		return 7;
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
		final Player player = (Player) object;
		graphic(1336);
		animate(7682);
		player.animate(7660);
		GameWorld.schedule(3, () -> player.graphic(1300));
		player.getSkills().boost(Skill.THIEVING, 4);
		player.getSkills().boost(Skill.AGILITY, 4);
		return false;
	}
}
