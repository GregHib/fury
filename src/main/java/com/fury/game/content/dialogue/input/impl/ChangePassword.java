package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.network.security.PBKDF2;
import com.fury.util.NameUtils;
import com.fury.core.model.node.entity.actor.figure.player.Player;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ChangePassword extends Input {

	@Override
	public void handleSyntax(Player player, String syntax) {
		player.getPacketSender().sendInterfaceRemoval();
		/*if(!GameSettings.MYSQL_ENABLED) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}*/
		if(syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
			player.message("That password is invalid. Please try another password.");
			return;
		}
		if(syntax.contains("_")) {
			player.message("Your password can not contain underscores.");
			return;
		}
		if(player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
			player.message("Please visit the nearest bank and enter your pin before doing this.");
			return;
		}
/*
		MySQLDatabase recovery = MySQLController.getController().getDatabase(Database.RECOVERY);
		if(!recovery.active || recovery.getConnection() == null) {
			player.getPacketSender().sendMessage("This service is currently unavailable.");
			return;
		}
		boolean success = false;
		try {
			PreparedStatement preparedStatement = recovery.getConnection().prepareStatement("DELETE FROM users WHERE USERNAME = ?");
			preparedStatement.setString(1, player.getUsername());
			preparedStatement.executeUpdate();
			preparedStatement = recovery.getConnection().prepareStatement("INSERT INTO users (username,password,email) VALUES (?, ?, ?)");
			preparedStatement.setString(1, player.getUsername());
			preparedStatement.setString(2, player.getPasswordHash());
			preparedStatement.setString(3, syntax);
			preparedStatement.executeUpdate();
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
			success = false;
		}*/
		String passwordHash = null;
		try {
			passwordHash = PBKDF2.generatePasswordHash(syntax);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		if(passwordHash != null) {
			player.setPasswordHash(passwordHash);
			player.message("Your account's password has been successfully changed.");
		} else {
			player.message("An error occurred. Please try again.");
		}
	}
}
