package com.fury.game.system.mysql;

import com.fury.engine.task.impl.BonusExperienceTask;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.core.model.item.Item;
import com.fury.util.Colours;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmailPromo {

    public static void check(Player player, String code) {
        if (!player.getTimers().getClickDelay().elapsed(15000)) {
            player.message("You can only do this once every 15 seconds.");
            return;
        }

        try {
            player.getTimers().getClickDelay().reset();
            String checkUrl = "http://furyps.com/store/gateway/promo_email.php?code=" + code + "&secret=98uyhjoiuytfvhytre";
            URL url = new URL(checkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("DENIED")) {
                    player.message("Could not validate your code.", Colours.RED);
                    return;
                }

                if (line.contains("CONFIRMED")) {
                    if(code.startsWith("mb-")) {
                        //mystery box
                        if(player.getBank().isFull())
                            player.getInventory().add(new Item(6199));
                        else
                            player.getBank().tab().add(new Item(6199));
                        player.message("Enjoy your mystery box!");
                        return;
                    } else if(code.startsWith("xp-")) {
                        //12 hours of bonus experience
                        BonusExperienceTask.addBonusXp(player, 60 * 12);
                        player.message("12 hours of bonus exp has been added to your acc!");
                        return;
                    }
                } else {
                    player.message("Error validating. please contact staff", Colours.RED);
                }

                PlayerLogs.log(player.getUsername(), "Redeemed email promo " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
