package com.fury.game.system.mysql.impl;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Store {

    private static final String SECRET = "52ab66e4f8ff2"; //YOUR SECRET KEY!

    @SuppressWarnings("deprecation")
    public void claim(Player player) {
        if (player.getInventory().getSpaces() < 10) {
            player.message("You need at least 10 free inventory slots to claim purchased items.");
            return;
        }

        if (player.getTimers().getStoreClaim().elapsed() <= 30000) {
            player.message("You can only do this once every 30 seconds.");
            return;
        }

        if(!GameSettings.MYSQL_ENABLED) {
            player.message("Claim is not currently active at the moment.");
            return;
        }

        try {
            player.getTimers().getStoreClaim().reset();
            URL url = new URL(GameSettings.WEBSITE + "/store/callback.php?secret=" + SECRET + "&username=" + URLEncoder.encode(player.getUsername().toLowerCase().replaceAll(" ", "_")));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String string = null;
            loop:
            while ((string = reader.readLine()) != null) {
                switch (string.toUpperCase()) {
                    case "ERROR":
                        player.message("An error occurred while claiming your items.");
                        break loop;
                    case "NO_RESULTS":
                        player.message("There were no results found while claiming items.");
                        break loop;
                    default:
                        String[] split = string.split(",");
                        int quantity = Integer.parseInt(split[1]);
                        switch (split[0]) {
                            case "$10_scroll":
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "$50_scroll":
                                player.getInventory().add(new Item(10944, quantity));
                                break;
                            case "$100_scroll":
                                player.getInventory().add(new Item(12502, quantity));
                                break;
                            /**
                             * Armour
                             */
                            case "chaotic_kiteshield":
                                player.getInventory().add(new Item(18359, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "eagle-eye_kiteshield":
                                player.getInventory().add(new Item(18361, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "farseer_kiteshield":
                                player.getInventory().add(new Item(18363, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "warrior_ring_i":
                                player.getInventory().add(new Item(15020, quantity));
                                break;
                            case "berserker_ring_i":
                                player.getInventory().add(new Item(15220, quantity));
                                break;
                            case "archer_ring_i":
                                player.getInventory().add(new Item(15019, quantity));
                                break;
                            case "seer_ring_i":
                                player.getInventory().add(new Item(15018, quantity));
                                break;

                            /** Weapons **/

                            case "chaotic_rapier":
                                player.getInventory().add(new Item(18349, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "chaotic_longsword":
                                player.getInventory().add(new Item(18351, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "chaotic_maul":
                                player.getInventory().add(new Item(18353, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "chaotic_crossbow":
                                player.getInventory().add(new Item(18357, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "chaotic_staff":
                                player.getInventory().add(new Item(18355, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;


                            /** Rares **/
                            case "christmas_cracker":
                                player.getInventory().add(new Item(962, quantity));

                                player.getInventory().add(new Item(12502, quantity));
                                player.getInventory().add(new Item(12502, quantity));
                                break;
                            case "blue_partyhat":
                                player.getInventory().add(new Item(1042, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "white_partyhat":
                                player.getInventory().add(new Item(1048, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "red_partyhat":
                                player.getInventory().add(new Item(1038, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "green_partyhat":
                                player.getInventory().add(new Item(1044, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "yellow_partyhat":
                                player.getInventory().add(new Item(1040, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "purple_partyhat":
                                player.getInventory().add(new Item(1046, quantity));

                                player.getInventory().add(new Item(10944, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "santa_hat":
                                player.getInventory().add(new Item(1050, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "red_halloween_mask":
                                player.getInventory().add(new Item(1057, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "blue_halloween_mask":
                                player.getInventory().add(new Item(1055, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;
                            case "green_halloween_mask":
                                player.getInventory().add(new Item(1053, quantity));

                                player.getInventory().add(new Item(10942, quantity));
                                player.getInventory().add(new Item(10942, quantity));
                                break;

                            /** Bundles **/
                            case "partyhat_set":
                                for (int phat : new int[]{1048, 1038, 1044, 1040, 1046, 1042}) {
                                    player.getInventory().add(new Item(phat, quantity));
                                }
                                player.getInventory().add(new Item(12502, quantity));
                                player.getInventory().add(new Item(12502, quantity));
                                player.getInventory().add(new Item(12502, quantity));
                                break;
                            case "third-age_melee_set":
                                player.getInventory().add(new Item(11858, quantity));

                                player.getInventory().add(new Item(12502, quantity));
                                player.getInventory().add(new Item(10943, quantity));
                                break;
                            case "third-age_mage_set":
                                player.getInventory().add(new Item(11862, quantity));

                                player.getInventory().add(new Item(12502, quantity));
                                break;
                            case "third-age_range_set":
                                player.getInventory().add(new Item(11860, quantity));

                                player.getInventory().add(new Item(12502, quantity));
                                break;
                            case "third-age_druidic_set":
                                player.getInventory().add(new Item(19580, quantity));

                                player.getInventory().add(new Item(12502, quantity));
                                player.getInventory().add(new Item(10944, quantity));
                                break;

                        }
                        continue loop;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            player.message("Currently not available.");
        }
    }
}
