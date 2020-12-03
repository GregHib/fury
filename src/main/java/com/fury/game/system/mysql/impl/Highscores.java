package com.fury.game.system.mysql.impl;

import com.fury.Main;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.mysql.MySQLController;
import com.fury.game.system.mysql.MySQLController.Database;
import com.fury.game.system.mysql.MySQLDatabase;

import java.sql.PreparedStatement;

public class Highscores {

	public static void save(Player player) {
		if(!GameSettings.MYSQL_ENABLED) {
			return;
		}
		if(player.getRights() == PlayerRights.DEVELOPER || player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.ADMINISTRATOR)
			return;
		if(player.getSkills().getTotalLevel() <= 34)
			return;	
		MySQLDatabase highscores = MySQLController.getController().getDatabase(Database.HIGHSCORES);
		if(!highscores.active || highscores.getConnection() == null) {
			return;
		}
		Main.getLoader().getEngine().submit(() -> {
			try {
				PreparedStatement preparedStatement = highscores.getConnection().prepareStatement("DELETE FROM hs_users WHERE USERNAME = ?");
				preparedStatement.setString(1, player.getUsername());
				preparedStatement.executeUpdate();
				preparedStatement = highscores.getConnection().prepareStatement("INSERT INTO hs_users (username,rights,game_mode,overall_xp,attack_xp,defence_xp,strength_xp,constitution_xp,ranged_xp,prayer_xp,magic_xp,cooking_xp,woodcutting_xp,fletching_xp,fishing_xp,firemaking_xp,crafting_xp,smithing_xp,mining_xp,herblore_xp,agility_xp,thieving_xp,slayer_xp,farming_xp,runecrafting_xp,hunter_xp,construction_xp,summoning_xp,dungeoneering_xp,kills,deaths) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				preparedStatement.setString(1, player.getUsername());
				int rights = player.getRightsId();
				preparedStatement.setInt(2, rights);
				preparedStatement.setInt(3, player.getGameMode().ordinal());
				preparedStatement.setLong(4, player.getSkills().getTotalExp());
				for (int i = 5; i <= 29; i++) {//TODO this shouldn't be like this
                    preparedStatement.setInt(i, (int) player.getSkills().getExperience(Skill.forId(i - 5)));
                }
				preparedStatement.setInt(30, player.getPlayerKillingAttributes().getPlayerKills());
				preparedStatement.setInt(31, player.getPlayerKillingAttributes().getPlayerDeaths());
				preparedStatement.executeUpdate();
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
