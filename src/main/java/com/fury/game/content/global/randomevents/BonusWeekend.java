package com.fury.game.content.global.randomevents;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.world.GameWorld;
import com.fury.util.Misc;

public class BonusWeekend {

	/*
	 * Runs every 600ms (1 server tick)
	 * Checks if double exp weekend.
	 */
	public static void sequence() {
		boolean weekend = Misc.isWeekend();
		if(GameSettings.BONUS_EXP != weekend) {
			GameSettings.BONUS_EXP = weekend;
			for(Player p : GameWorld.getPlayers()) {
				if(p != null)
					p.getPacketSender().updateBonusExp();
			}
		}
	}
}
