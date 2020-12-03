package com.fury.game.entity.character.player.content;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.core.model.item.Item;

import java.text.DecimalFormat;

public class BonusManager {

	public static void update(Player player) {
		double[] bonuses = new double[18];
		for (Slot slot : Slot.values()) {
			Item item = player.getEquipment().get(slot);
			if(item == null || item.getId() == -1)
				continue;
			if(slot == Slot.ARROWS && PlayerCombatAction.isWieldingThrown(player))
				continue;
			int[] bonus = item.getDefinition().getBonuses();

			if(bonus == null)
				continue;

			for (int i = 0; i < bonus.length; i++)
				bonuses[i] += Equipment.isPvpArmour(item) && !player.isCanPvp() && bonus[i] > 0 ? bonus[i]/2 : bonus[i];
		}
		for (int i = 0; i < BONUS_NAMES.length; i++) {
			if (i <= 4) {
				player.getBonusManager().attackBonus[i] = bonuses[i];
			} else if (i <= 13) {
				int index = i - 5;
				player.getBonusManager().defenceBonus[index] = bonuses[i];
				if(player.getEquipment().get(Slot.SHIELD).getId() == 11283 && !BONUS_NAMES[i].contains("Magic")) {
					if(player.getDfsCharges() > 0) {
						player.getBonusManager().defenceBonus[index] += player.getDfsCharges();
						bonuses[i] += player.getDfsCharges();
					}
				}
			} else if (i <= 17) {
				int index = i - 14;
				player.getBonusManager().otherBonus[index] = bonuses[i];
			}
			if(player.getInterfaceId() == 21172)
				player.getPacketSender().sendString(21190 + i, BONUS_NAMES[i] + ": " + bonuses[i]);
		}
		player.getSettings().updateWeight();
		if(player.getInterfaceId() == 21172)
			player.getPacketSender().sendString(21210, new DecimalFormat("####0.0").format(player.getSettings().getWeight()) + "kg");
	}

	public double[] getAttackBonus() {
		return attackBonus;
	}

	public double[] getDefenceBonus() {
		return defenceBonus;
	}

	public double[] getOtherBonus() {
		return otherBonus;
	}

	private double[] attackBonus = new double[5];

	private double[] defenceBonus = new double[9];

	private double[] otherBonus = new double[4];

	private  static final String[] BONUS_NAMES = new String[] {
			"Stab", "Slash", "Crush", "Magic", "Ranged",
			"Stab", "Slash", "Crush", "Magic", "Ranged", "Summoning",
			"Absorb Melee", "Absorb Magic", "Absorb Ranged",
			"Strength", "Ranged Strength", "Prayer", "Magic Damage"
	};

	public static final int 
	ATTACK_STAB = 0, 
	ATTACK_SLASH = 1,
	ATTACK_CRUSH = 2, 
	ATTACK_MAGIC = 3, 
	ATTACK_RANGE = 4, 

	DEFENCE_STAB = 0, 
	DEFENCE_SLASH = 1, 
	DEFENCE_CRUSH = 2, 
	DEFENCE_MAGIC = 3,
	DEFENCE_RANGE = 4, 
	DEFENCE_SUMMONING = 5,
	ABSORB_MELEE = 6, 
	ABSORB_MAGIC = 7, 
	ABSORB_RANGED = 8,

	BONUS_STRENGTH = 0, 
	RANGED_STRENGTH = 1,
	BONUS_PRAYER = 2,
	MAGIC_DAMAGE = 3;

	/** CURSES **/

	public static void sendPrayerBonuses(final Player p) {
		if(p.hasStarted()) {
//			sendAttackBonus(p);
//			sendDefenceBonus(p);
//			sendStrengthBonus(p);
//			sendRangedBonus(p);
//			sendMagicBonus(p);
		}
	}
}
