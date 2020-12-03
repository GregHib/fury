package com.fury.game.npc.familiar.impl;


import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class ObsidianGolem extends Familiar {

	public ObsidianGolem(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile); // TODO invisible mining boost
	}

	@Override
	public String getSpecialName() {
		return "Volcanic Strength";
	}

	@Override
	public String getSpecialDescription() {
		return "Gives +9 strength levels to the player.";
	}

	@Override
	public int getStoreSize() {
		return 0;
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
		player.getSkills().boost(Skill.STRENGTH, 9);
		player.animate(7660);
		player.perform(new Graphic(1300));
		perform(new Graphic(1465));
		animate(8053);
		return true;
	}
}
