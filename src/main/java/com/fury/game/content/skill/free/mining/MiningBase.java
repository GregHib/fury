package com.fury.game.content.skill.free.mining;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;

import java.util.concurrent.TimeUnit;

public abstract class MiningBase extends Action {

	public static void prospect(final Player player, final String endMessage) {
		prospect(player, "You prospect the rock for ores....", endMessage);
	}

	public static void prospect(final Player player, String startMessage, final String endMessage) {
		player.message(startMessage);
		player.getMovement().lock(4);
		GameExecutorManager.slowExecutor.schedule(() -> player.message(endMessage), 4, TimeUnit.SECONDS);
	}

	public static Pickaxe getPickaxe(Player player, boolean dungeoneering) {
		for (int i = dungeoneering ? 10 : Pickaxe.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) {
			Pickaxe def = Pickaxe.values()[i];
			if (player.getInventory().contains(def.getPickAxe()) || player.getEquipment().get(Slot.WEAPON).isEqual(def.getPickAxe())) {
				if (player.getSkills().getLevel(Skill.MINING) >= def.getLevelRequired())
					return def;
			}
		}
		return null;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	public static void propect(final Player player, String startMessage, final String endMessage) {
		if(startMessage != null)
			player.message(startMessage, true);
		player.getMovement().lock(5);
		GameWorld.schedule(4, () -> player.message(endMessage));
	}
}
