package com.fury.game.npc.familiar.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class KeenHoardstalker extends Familiar {
	private int forageTicks;

	public KeenHoardstalker(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public void processNpc() {
		super.processNpc();
		forageTicks++;
		if (forageTicks == 300) {
			forageTicks = 0;
			getBeastOfBurden().add(new Item(DungeonConstants.HOARDSTALKER_ITEMS[3][Misc.random(5)], 1));
		}
	}

	@Override
	public String getSpecialName() {
		return "Aptitude";
	}

	@Override
	public String getSpecialDescription() {
		return "Boosts all of your non-combat skills by 4.";
	}

	@Override
	public int getStoreSize() {
		return 30;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 20;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.graphic(1300);
		player.animate(7660);
		for (Skill skill : CombatConstants.NON_COMBAT_SKILLS)
			player.getSkills().boost(skill, 4);
		return true;
	}
}

