package com.fury.game.entity.character.player.content;

import com.fury.game.content.global.minigames.impl.Barrows;
import com.fury.game.content.global.treasuretrails.TreasureTrails;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class Digging {
	
	public static void dig(final Player player) {
		if(!player.getTimers().getClickDelay().elapsed(2000))
			return;
		player.getMovement().reset();
		player.getPacketSender().sendInterfaceRemoval();
		player.message("You start digging..", true);
		player.animate(830);
		GameWorld.schedule(2, () -> {
			/**
			 * Clue scrolls
			 */
			if(TreasureTrails.dig(player))
				return;

			if(player.sameAs(new Position(2984, 3387)) || player.sameAs(new Position(2987, 3387)) || player.sameAs(new Position(2989, 3378)) || player.sameAs(new Position(2996, 3377)) || player.sameAs(new Position(2999, 3375)) || player.sameAs(new Position(3005, 3376))) {
				player.moveTo(new Position(new Position(1753, 5238), 2));
				return;
			}

			Position targetPosition = null;
			/**
			 * Barrows
			 */
			if (inArea(player, 3553, 3301, 3561, 3294))
				targetPosition = new Position(3578, 9706, 3);
			else if (inArea(player, 3550, 3287, 3557, 3278))
				targetPosition = new Position(3568, 9683, 3);
			else if (inArea(player, 3561, 3292, 3568, 3285))
				targetPosition = new Position(3557, 9703, 3);
			else if (inArea(player, 3570, 3302, 3579, 3293))
				targetPosition = new Position(3556, 9718, 3);
			else if (inArea(player, 3571, 3285, 3582, 3278))
				targetPosition = new Position(3534, 9704, 3);
			else if (inArea(player, 3562, 3279, 3569, 3273))
				targetPosition = new Position(3546, 9684, 3);
			else if (inArea(player, 2986, 3370, 3013, 3388))
				targetPosition = new Position(3546, 9684, 3);

			if(targetPosition != null) {
				player.moveTo(targetPosition);
				player.getControllerManager().startController(new Barrows());
			} else
				player.message("You find nothing of interest.", true);
		});
		player.getTimers().getClickDelay().reset();
	}

	private static boolean inArea(Position pos, int x, int y, int x1, int y1) {
		return pos.getX() > x && pos.getX() < x1 && pos.getY() < y && pos.getY() > y1;
	}
}
