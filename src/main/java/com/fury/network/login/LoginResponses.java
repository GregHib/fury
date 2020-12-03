package com.fury.network.login;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.player.PlayerLoading;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.network.security.ConnectionHandler;
import com.fury.network.security.PBKDF2;
import com.fury.util.Misc;
import com.fury.util.NameUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public final class LoginResponses {
	
	public static int getResponse(Player player, LoginDetailsMessage msg) {
		if (GameWorld.getPlayers().isFull()) {
			return LOGIN_WORLD_FULL;
		} 
		if(World.isUpdating()) {
			return LOGIN_GAME_UPDATING;
		}
		if (player == null || !NameUtils.isValidName(player.getUsername())) {
			return LOGIN_INVALID_CREDENTIALS;
		} 
		if(player.getUsername().startsWith(" ")) {
			return LOGIN_INVALID_CREDENTIALS;
		}
		if(msg.getClientVersion() != GameSettings.GAME_VERSION || msg.getUid() != GameSettings.GAME_UID) {
			return OLD_CLIENT_VERSION;
		}
		if(World.getPlayerByName(player.getUsername()) != null) {
			return LOGIN_ACCOUNT_ONLINE;
		}
		int playerLoadingResponse;

		/* BANS AND ACCESS LIMITS */
		int hostHandlerResponse = ConnectionHandler.getResponse(player, msg);

		if(hostHandlerResponse != LOGIN_SUCCESSFUL)
			return hostHandlerResponse;

		boolean newAccount = !Misc.playerExists(player.getUsername());
		if(newAccount) {
			/* PREVENTING IMPERSONATING */
//			if(msg.getUsername().toLowerCase().equalsIgnoreCase("greg"))
//				return LOGIN_INVALID_CREDENTIALS;
			try {
				player.setPasswordHash(PBKDF2.generatePasswordHash(msg.getPassword()));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return LOGIN_REJECT_SESSION;
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
				return LOGIN_REJECT_SESSION;
			}
			player.getSave().setDisabled(true);
			player.setNewPlayer(true);
			return LOGIN_SUCCESSFUL;
		} else {
			/* CHAR FILE LOADING */
			playerLoadingResponse = PlayerLoading.getResult(player);
			if (playerLoadingResponse != LOGIN_SUCCESSFUL && playerLoadingResponse != NEW_ACCOUNT) {
				return playerLoadingResponse;
			}

			if(player.getPasswordHash() == null)
				return LOGIN_REJECT_SESSION;

			/* Password Validation */
			try {
				if(!PBKDF2.validatePassword(msg.getPassword(), player.getPasswordHash()))
					return LOGIN_INVALID_CREDENTIALS;
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
				return LOGIN_REJECT_SESSION;
			}
        }

		return playerLoadingResponse;
	}
	/**
	 * This login opcode signifies a successful login.
	 */
	public static final int LOGIN_SUCCESSFUL = 43;
	
	/**
	 * This login opcode is used when the player
	 * has entered an invalid username and/or password.
	 */
	public static final int LOGIN_INVALID_CREDENTIALS = 3;
	
	/**
	 * This login opcode is used when the account
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_ACCOUNT = 4;
	
	/**
	 * This login opcode is used when the player's IP/Computer
	 * has been disabled.
	 */
	public static final int LOGIN_DISABLED_IP = 26;
	public static final int LOGIN_DISABLED_COMPUTER = 26;
	
	/**
	 * This login opcode is used when the account
	 * attempting to connect is already online in the server.
	 */
	public static final int LOGIN_ACCOUNT_ONLINE = 5;
	
	/**
	 * This login opcode is used when the game has been or
	 * is being updated.
	 */
	public static final int LOGIN_GAME_UPDATE = 6;
	
	/**
	 * This login opcode is used when server is launching.
	 */
	public static final int SERVER_LAUNCHING = 23;
	
	/**
	 * This login opcode is used when the world being
	 * connected to is full.
	 */
	public static final int LOGIN_WORLD_FULL = 7;
	
	/**
	 * This login opcode is used when server is offline.
	 */
	public static final int LOGIN_SERVER_OFFLINE = 8;
	
	/**
	 * This login opcode is used when the connections
	 * from an ip address has exceeded {@link CONNECTION_AMOUNT}.
	 */
	public static final int LOGIN_CONNECTION_LIMIT = 9;
	
	/**
	 * This login opcode is used when a connection
	 * has received a bad session id.
	 */
	public static final int LOGIN_BAD_SESSION_ID = 10;
	
	/**
	 * This login opcode is used when the login procedure
	 * has rejected the session.
	 */
	public static final int LOGIN_REJECT_SESSION = 0;
	
	/**
	 * This login opcode is used when a non-member player
	 * is attempting to login to a members world.
	 */
	public static final int LOGIN_WORLD_MEMBER_ACCOUNT_REQUIRED = 12;
	
	/**
	 * This login opcode is used when the login procedure 
	 * could not be completed.
	 */
	public static final int LOGIN_COULD_NOT_COMPLETE = 13;
	
	/**
	 * This login opcode is used when the game is being updated.
	 */
	public static final int LOGIN_WORLD_UPDATE = 14;
	
	/**
	 * This login opcode is received upon a reconnection
	 * so that chat messages will not dissappear.
	 */
	public static final int LOGIN_RECONNECTION = 15;
	
	/**
	 * This login opcode is used when a player
	 * has exceeded their attempts to login.
	 */
	public static final int LOGIN_EXCESSIVE_ATTEMPTS = 16;
	
	/**
	 * This login opcode is used when a member is attempting
	 * to login to a non-members world in a members-only area.
	 */
	public static final int LOGIN_AREA_MEMBER_ACCOUNT_REQUIRED = 17;
	
	/**
	 * This login opcode is used when the login server
	 * is invalid.
	 */
	public static final int LOGIN_INVALID_LOGIN_SERVER = 20;
	
	/**
	 * This login opcode is used when a player has just
	 * left another world and has to wait a delay before
	 * entering a new world.
	 */
	public static final int LOGIN_WORLD_DELAY = 21;
	
	/**
	 * This login opcode is used when a player has
	 * attempted to login with a old client.
	 */
	public static final int OLD_CLIENT_VERSION = 6;
	
	/**
	 * 
	 * The new connection login request
	 * byte data sent by the client.
	 */
	public static final int NEW_CONNECTION_LOGIN_REQUEST = 16;
	
	/**
	 * The reconnection login request
	 * byte data sent by the client.
	 */
	public static final int RECONNECTING_LOGIN_REQUEST = 18;


	public static final int LOGIN_GAME_UPDATING = 48;
	
	/**
	 * New account
	 */
	public static final int NEW_ACCOUNT = -1;
	
}
