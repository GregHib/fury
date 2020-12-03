package com.fury;

import com.fury.game.GameLoader;
import com.fury.game.GameSettings;
import com.fury.network.NetworkConstants;
import com.fury.util.ShutdownHook;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The starting point of Fury.
 * @author Gabriel
 * @author Samy
 * @author Greg
 */
public class Main {

	private static final GameLoader loader = new GameLoader(NetworkConstants.GAME_PORT);
	private static final Logger logger = Logger.getLogger(GameSettings.NAME);

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		if(GameSettings.DEBUG) {
			System.out.println("User directory: " + System.getProperty("user.dir"));
			System.out.println("Saves path: " + GameSettings.SAVES);
			System.out.println("Resources path: " + GameSettings.RESOURCES);
			System.out.println("Server Hosted: " + GameSettings.HOSTED);
			System.out.println("MySQL Enabled: " + GameSettings.MYSQL_ENABLED);
		}

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			logger.info("Initializing the loader...");
			loader.init();
			loader.finish();
			logger.info("The loader has finished loading utility tasks. " + (System.currentTimeMillis() - start) + "ms");
			logger.info(GameSettings.NAME + " is now online on port " + NetworkConstants.GAME_PORT + "!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not start " + GameSettings.NAME + "! Program terminated.", ex);
			System.exit(1);
		}
	}

	public static GameLoader getLoader() {
		return loader;
	}

	public static Logger getLogger() {
		return logger;
	}
}