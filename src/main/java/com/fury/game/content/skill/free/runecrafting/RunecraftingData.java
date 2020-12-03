package com.fury.game.content.skill.free.runecrafting;

import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class RunecraftingData {

	public static int getMakeAmount(RuneData rune, Player player) {
		int amount = 1;
		switch(rune) {
		case AIR_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 11))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 22))
				amount = 3;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 33))
				amount = 4;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 44))
				amount = 5;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 55))
				amount = 6;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 66))
				amount = 7;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 77))
				amount = 8;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 88))
				amount = 9;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 99))
				amount = 10;
			break;
		case ASTRAL_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 82))
				amount = 2;
			break;
		case BLOOD_RUNE:
			break;
		case BODY_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 46))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 92))
				amount = 3;
			break;
		case CHAOS_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 74))
				amount = 2;
			break;
		case COSMIC_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 59))
				amount = 2;
			break;
		case DEATH_RUNE:
			break;
		case EARTH_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 26))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 52))
				amount = 3;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 78))
				amount = 4;
			break;
		case FIRE_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 35))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 70))
				amount = 3;
			break;
		case LAW_RUNE:
			break;
		case MIND_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 14))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 28))
				amount = 3;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 42))
				amount = 4;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 56))
				amount = 5;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 70))
				amount = 6;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 84))
				amount = 7;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 98))
				amount = 8;
			break;
		case NATURE_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 91))
				amount = 2;
			break;
		case WATER_RUNE:
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 19))
				amount = 2;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 38))
				amount = 3;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 57))
				amount = 4;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 76))
				amount = 5;
			if(player.getSkills().hasLevel(Skill.RUNECRAFTING, 95))
				amount = 6;
			break;
		default:
			break;
		}
		if(Equipment.wearingArdyCloak(player, 2))
			amount = (int) Math.floor(amount * 1.15);
		return amount;
	}
}
