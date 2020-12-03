package com.fury.game.entity.character.player;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.files.plugin.loader.StorageFileLoader;
import com.fury.network.login.LoginResponses;
import com.fury.util.Logger;
import com.google.gson.JsonSyntaxException;

public class PlayerLoading {

	public static int getResult(Player player) {
		try {
			if (StorageFileLoader.load(player))
				return LoginResponses.LOGIN_SUCCESSFUL;
		} catch (JsonSyntaxException e) {
			System.err.println("Error loading player files: " + player.getUsername());
			e.printStackTrace();
		} catch(Throwable e) {
			Logger.handle(e);
		}

		return LoginResponses.LOGIN_COULD_NOT_COMPLETE;
	}
}