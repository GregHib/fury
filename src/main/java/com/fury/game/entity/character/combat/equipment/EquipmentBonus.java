package com.fury.game.entity.character.combat.equipment;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.member.slayer.Slayer;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.content.combat.CombatType;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EquipmentBonus {

	public static boolean hasSlayerHelmetBonus(Player player, Figure victim) {
		if(!victim.isNpc())
			return false;
		Mob mob = (Mob) victim;
		if(Slayer.hasSlayerHelmet(player))
			return player.getSlayerManager().isCurrentTask(mob.getName());
		return false;
	}

	public static boolean hasObsidianEffect(Player player) {
		if (player.getEquipment().get(Slot.AMULET).getId() != 11128)
			return false;

		for (int weapon : obsidianWeapons)
			if (player.getEquipment().get(Slot.WEAPON).getId() == weapon)
				return true;

		return false;
	}

	public static boolean wearingVoid(Player player, CombatType attackType) {
		int correctEquipment = 0;
		int helmet = attackType == CombatType.MAGIC ? MAGE_VOID_HELM : attackType == CombatType.RANGED ? RANGED_VOID_HELM : MELEE_VOID_HELM;
		for (int armour[] : VOID_ARMOUR)
			if ((player.getEquipment().get(armour[0]).getId() == armour[1]) ||
					(player.getEquipment().get(armour[0]).getId() == ELITE_VOID_ARMOUR[0]))
				correctEquipment++;

		if (player.getEquipment().get(Slot.SHIELD).getId() == VOID_KNIGHT_DEFLECTOR)
			correctEquipment++;

		return correctEquipment >= 3 && player.getEquipment().get(Slot.HEAD).getId() == helmet;
	}

	public static final int MAGE_VOID_HELM = 11663;
	
	public static final int RANGED_VOID_HELM = 11664;
	
	public static final int MELEE_VOID_HELM = 11665;
	
	private static final int VOID_KNIGHT_DEFLECTOR = 19712;
	
	public static final int[][] VOID_ARMOUR = {
		{Slot.BODY.ordinal(), 8839},
		{Slot.LEGS.ordinal(), 8840},
		{Slot.HANDS.ordinal(), 8842}
	};
	
	public static final int[] ELITE_VOID_ARMOUR = {
		19785,
		19786,
		8842
	};

	/**
	 * Obsidian items
	 */

	public static final int[] obsidianWeapons = {
			746, 747, 6523, 6525, 6526, 6527, 6528
	};

}
