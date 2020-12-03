package com.fury.game.entity.character.player.content;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.content.skill.Skill;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Handles a player's run energy
 * @author Gabriel Hannason & Greg
 * Thanks to Russian for formula!
 */
public class EnergyHandler {
	
	public static void processEnergyDepletion(Player player) {
		if(player.getSettings().getBool(Settings.RUNNING) && player.getMovement().hasWalkSteps()) {
			double energy = player.getSettings().getInt(Settings.RUN_ENERGY);
			if (energy > 0) {
				double depletion = 0.67;
				//Boots of lightness
				if(player.getEquipment().get(Slot.FEET).getId() == 88)
					depletion -= 0.12;
				//Spotted cape
				if(player.getEquipment().get(Slot.CAPE).getId() == 10069)
					depletion -= 0.025;
				//Spottier cape
				if(player.getEquipment().get(Slot.CAPE).getId() == 10071)
					depletion -= 0.05;

				energy = (energy - depletion);
				player.getSettings().set(Settings.RUN_ENERGY, (int) energy);
				player.getPacketSender().sendRunEnergy((int) energy);
			} else {
				player.getSettings().set(Settings.RUNNING, false);
				player.getPacketSender().sendRunStatus();
				player.getPacketSender().sendRunEnergy(0);
			}
		}
	}
	public static void processEnergyRestore(Player player) {
		if (player.getSettings().getInt(Settings.RUN_ENERGY) < 100) {
			if (System.currentTimeMillis() >= (restoreEnergyFormula(player) + player.getTimers().getLastRunRecovery().elapsed())) {
				double energy = player.getSettings().getInt(Settings.RUN_ENERGY) + 1;
				player.getSettings().set(Settings.RUN_ENERGY, energy);
				player.getPacketSender().sendRunEnergy((int) energy);
				player.getTimers().getLastRunRecovery().reset();
			}
		}
	}

	enum RestType {
		ARMS_BACK(new int[] { 5713, 1549, 5748 }), ARMS_CROSSED(new int[] { 11786, 11787, 11788 }), LEGS_STRAIGHT(new int[] {2921, -1, 2716});

		private final int[] spriteIDs;

		RestType(int[] spriteIDs) {
			this.spriteIDs = spriteIDs;
		}

		public int[] getSpriteIDs() {
			return spriteIDs;
		}
	}

	public static void standUp(Player player) {
		//Reset Standing Animation
		player.getCharacterAnimations().reset();
		WeaponAnimations.update(player);
		player.getPacketSender().sendOrb(4, false);
		player.getUpdateFlags().add(Flag.APPEARANCE);
		//Stand Up Animation
		player.animate(11788);
		//Unlock Walking
		player.getMovement().unlock();
	}

	public static void rest(Player player) {
		rest(player, false);
	}

	public static void rest(Player player, boolean listening) {
		if(player.busy() || player.getCombat().isInCombat()) {
			player.message("You cannot do this right now.");
			return;
		}
		player.getMovement().reset();
		player.getMovement().lock();
		player.getSettings().setRestingState(listening ? 2 : 1);
		if(listening)
			Achievements.finishAchievement(player, Achievements.AchievementData.LISTEN_TO_MUSICIAN);
		else
			Achievements.finishAchievement(player, Achievements.AchievementData.HAVE_A_REST);
		player.animate(11786);
		player.getCharacterAnimations().setStandingAnimation(11787);
		player.getPacketSender().sendOrb(listening ? 5 : 4, true);
		player.getUpdateFlags().add(Flag.APPEARANCE);
		player.message("You begin resting..", true);
	}
	
	public static double restoreEnergyFormula(Player player) {
		return 2260 - (player.getSkills().getLevel(Skill.AGILITY) * (player.getSettings().isResting() ? 13000 : 10));
	}
}
