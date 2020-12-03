package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class BullAnt extends Familiar {

	public BullAnt(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Unburden";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores the owner's run energy by half of their Agility level.";
	}

	@Override
	public int getStoreSize() {
		return 9;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 12;
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
		int agilityLevel = getOwner().getSkills().getLevel(Skill.AGILITY);
		int runEnergy = (int) player.getSettings().getInt(Settings.RUN_ENERGY) + (Math.round(agilityLevel / 2));
		player.graphic(1300);
		player.animate(7660);
		animate(7895);
		perform(new Graphic(1382));
		player.getSettings().set(Settings.RUN_ENERGY, runEnergy > 100 ? 100 : runEnergy);
		return true;
	}
}
