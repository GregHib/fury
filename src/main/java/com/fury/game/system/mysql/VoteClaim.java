package com.fury.game.system.mysql;

import com.fury.Main;
import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.files.world.increment.timer.impl.DailyVotes;
import com.fury.core.model.item.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class VoteClaim {

    public static void claim(Player player) {
        if(!GameSettings.MYSQL_ENABLED)
            return;

        MySQLDatabase vote = MySQLController.getController().getDatabase(MySQLController.Database.VOTE);
        if(!vote.active || vote.getConnection() == null)
            return;

        Main.getLoader().getEngine().submit(() -> {
            try {
                String name = player.getUsername().toLowerCase().replaceAll(" ", "_");
                PreparedStatement preparedStatement = vote.getConnection().prepareStatement("SELECT * FROM vote_claims WHERE username='" + name + "' AND claimed=0", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();

                if (!resultSet.next()) {
                    player.message("No votes found, please try again shortly.");
                    return;
                }

                resultSet.beforeFirst();
                while (resultSet.next()) {
                    if(DailyVotes.get().canClaim(player.getLogger().getHardwareId())) {
                        if(player.isInDungeoneering() || !player.getInventory().add(new Item(20712, 1))) {
                            player.message("Your vote book has been placed in your bank.");
                            player.getBank().tab().add(new Item(20712, 1));
                        }
                        player.message("Thank you for supporting the server!");
                        PlayerLogs.log(player.getUsername(), "Vote claimed by " + name + ". (time: " + new Date().toString() + ")");
                    } else {
                        player.message("You have already claimed your 4 rewards for today, try again tomorrow.");
                        PlayerLogs.log(player.getUsername(), "Vote claim failed by " + name + ". (time: " + new Date().toString() + ")");
                    }
                    DailyVotes.get().record(player.getLogger().getHardwareId());

                    resultSet.updateInt("claimed", 1);
                    resultSet.updateInt("ingame_claimed_time", (int) (System.currentTimeMillis() / 1000));
                    resultSet.updateRow();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
