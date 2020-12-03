package com.fury.util;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.entity.character.player.PlayerHandler;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;

import java.util.logging.Logger;

public class ShutdownHook extends Thread {

	/**
	 * The ShutdownHook logger to print out information.
	 */
	private static final Logger logger = Logger.getLogger(ShutdownHook.class.getName());

	@Override
	public void run() {
		logger.info("The shutdown hook is processing all required actions...");
		GameExecutorManager.shutdown();
		World.setUpdateTime(0);
		for (Player player : GameWorld.getPlayers()) {
			if (player != null)
				PlayerHandler.handleLogout(player);
		}
		World.saveAll(false);
		logger.info("The shudown hook actions have been completed, shutting the server down...");
	}
}
