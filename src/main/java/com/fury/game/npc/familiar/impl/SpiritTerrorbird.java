package com.fury.game.npc.familiar.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class SpiritTerrorbird extends Familiar {

	public SpiritTerrorbird(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Tireless Run";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores the player's run energy, by half the players agility level rounded up.";
	}

	@Override
	public int getStoreSize() {
		return 12;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 8;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getSettings().getInt(Settings.RUN_ENERGY) == 100) {
			player.message("This wouldn't effect you at all.");
			return false;
		}
		animate(8229);
		perform(new Graphic(1521));
		player.perform(new Graphic(1300));
		player.animate(7660);
		player.getSkills().boost(Skill.AGILITY, 2);
		int runEnergy = (int) (player.getSettings().getInt(Settings.RUN_ENERGY) + Math.round(getOwner().getSkills().getLevel(Skill.AGILITY) / 2.0));
		player.getSettings().set(Settings.RUN_ENERGY, runEnergy > 100 ? 100 : runEnergy);
		return true;
	}
}
