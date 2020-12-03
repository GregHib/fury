package com.fury.game.content.skill;

import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;


/**
 * This enum contains data used as constants for skill configurations
 * such as experience rates, string id's for interface updating.
 * @author Gabriel Hannason
 */
public enum Skill {
	ATTACK(6247, true),
	DEFENCE(6253, true),
	STRENGTH(6206, true),
	CONSTITUTION(6216, true),
	RANGED(4443, true),
	PRAYER(6242, true),
	MAGIC(6211, true),
	COOKING(6226, false),
	WOODCUTTING(4272, false),
	FLETCHING(6231, false),
	FISHING(6258, false),
	FIREMAKING(4282, false),
	CRAFTING(6263, false),
	SMITHING(6221, false),
	MINING(4416, false),
	HERBLORE(6237, false),
	AGILITY(4277, false),
	THIEVING(4261, false),
	SLAYER(12122, false),
	FARMING(5267, false),
	RUNECRAFTING(4267, false),
	HUNTER(8267, false),
	CONSTRUCTION(7267, false),
	SUMMONING(9267, true),
	DUNGEONEERING(10267, false);

	public static List<Skill> combatSkills = new ArrayList<>();
	static {
		for(Skill skill : Skill.values())
			if(skill.isCombat())
				combatSkills.add(skill);
	}

	Skill(int chatboxInterface, boolean combat) {
		this.chatboxInterface = chatboxInterface;
		this.combat = combat;
	}

	private int chatboxInterface;
	private boolean combat;

	public int getChatboxInterface() {
		return chatboxInterface;
	}

	public String getName() {
		return toString().toLowerCase();
	}

	public String getFormatName() {
		return Misc.formatText(getName());
	}

	public boolean isNewSkill() {
		return this == CONSTITUTION || this == PRAYER;
	}

	public static Skill forId(int id) {
		for (Skill skill : Skill.values()) {
			if (skill.ordinal() == id) {
				return skill;
			}
		}
		return null;
	}

	public static Skill forName(String name) {
		for (Skill skill : Skill.values()) {
			if (skill.toString().equalsIgnoreCase(name)) {
				return skill;
			}
		}
		return null;
	}

	public boolean isCombat() {
		return combat;
	}
}