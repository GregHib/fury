package com.fury.game.system.mysql.impl;


import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ama
 *
 * @author famous
 * furyps-server
 */

public class DonationStore {

    public static void checkDonation(Player player) {
        if (!player.getTimers().getClickDelay().elapsed(15000)) {
            player.message("You can only do this once every 15 seconds.");
            return;
        }

        try {
            player.getTimers().getClickDelay().reset();
            String username = player.getUsername().replace(" ", "_");
            String checkUrl = "http://furyps.com/store/gateway/callback.php?username=" + username + "&secret=98uyhjoiuytfvhytre";
            URL url = new URL(checkUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("NO_RESULTS")) {
                    player.message(FontUtils.RED + "Could not validate your order." + FontUtils.COL_END);
                    return;
                }
                String[] parts = line.split(",");
                String itemName = parts[0];
                int itemAmount = Integer.valueOf(parts[1]);

                //Black friday sale stuff
                double value = Double.valueOf(parts[3]);
                int donated = (int) Math.round(value);
                /*if(donated >= 10) {
                    boolean bonus = donated >= 25;
                    double random = Math.random();
                    if (random < (bonus ? 0.15 : 0.1)) {
                        itemAmount *= 2;
                        World.sendMessage(player.getUsername() + " just received double donations in the " + FontUtils.BLACK + "black-friday" + FontUtils.COL_END + " weekend sale!", 0xffc66d);
                    }

                    if(donated / 10 >= 1) {
                        int boxes = Math.round(donated / 10);
                        if(boxes > 0) {
                            player.getBank().tab().add(new Item(6199, boxes));
                            World.sendMessage(player.getUsername() + " just got extra mystery boxes in the " + FontUtils.BLACK + "black-friday" + FontUtils.COL_END + " weekend sale!", 0x468fa9);
                        }
                    }
                }*/

                boolean add = true;
                if (itemName.contains("_set")) {
                    claimSet(player, itemName, itemAmount);
                } else {
                    int itemId = Integer.valueOf(parts[2]);
                    if (itemId == 6758 || itemId == 608 || itemId == 607 || itemId == 14808)//Scrolls
                        add = false;
                    claimItem(player, itemName, itemAmount, itemId);
                }

                if (add)
                    player.getPoints().add(Points.DONATED, donated);

                PlayerLogs.log(player.getUsername(), "Claimed " + parts[0] + ": " + parts[1] + " " + value);
                System.out.println(player.getUsername() + " claimed " + parts[0] + ": " + parts[1] + " " + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void claimItem(Player player, String itemName, int itemAmount, int itemId) {
        if (itemId == 0) {
            player.message(FontUtils.RED + "Invalid donation item. Please contact staff." + FontUtils.COL_END);
            return;
        }
        if(player.getBank().isFull())
            player.getInventory().add(new Item(itemId, itemAmount));
        else
            player.getBank().tab().add(new Item(itemId, itemAmount));
        if (itemId == 6199 || itemId == 6758 || itemId == 608 || itemId == 607 || itemId == 14808)
            GameWorld.sendBroadcast(player.getUsername() + " just donated for " + (itemAmount == 1 ? "a " : "") + FontUtils.RED + itemName.replace("_", " ") + (itemAmount == 1 ? "" : "'s") + FontUtils.COL_END + "!" + " Thank You!", 0);
    }

    public static void claimSet(Player player, String itemName, int itemAmount) {
        Item[] items = getSetItems(itemName);
        for(Item item : items) {
            if(player.getBank().isFull())
                player.getInventory().add(new Item(item, item.getAmount() * itemAmount));
            else
                player.getBank().tab().add(new Item(item, item.getAmount() * itemAmount));
        }
    }

    public static Item[] getSetItems(String itemName) {
        try {
            switch (itemName) {
                case "elite_melee_set":
                case "elite_void_melee_set":
                    return new Item[]{new Item(11665), new Item(19785), new Item(19786), new Item(8842)};
                case "elite_ranger_set":
                case "elite_void_range_set":
                    return new Item[]{new Item(11664), new Item(19787), new Item(19788), new Item(8842)};
                case "elite_mage_set":
                case "elite_void_mage_set":
                    return new Item[]{new Item(11663), new Item(19789), new Item(19790), new Item(8842)};
                case "dharok_set":
                    return new Item[]{new Item(11848)};
                case "guthan_set":
                    return new Item[]{new Item(11850)};
                case "verac_set":
                    return new Item[]{new Item(11856)};
                case "ahrim_set":
                    return new Item[]{new Item(11846)};
                case "torags_set":
                    return new Item[]{new Item(11854)};
                case "karil_set":
                    return new Item[]{new Item(11852)};
                case "super_flask_set":
                    return new Item[]{new Item(23256, 350, Revision.PRE_RS3), new Item(23280, 350, Revision.PRE_RS3), new Item(23292, 350, Revision.PRE_RS3)};
                case "skilling_set":
                    return new Item[]{new Item(2364, 500), new Item(2362, 500), new Item(1514, 1000), new Item(1516, 1000), new Item(1746, 1000), new Item(1632, 100), new Item(6571, 1)};
                case "prayer_set":
                    return new Item[]{new Item(537, 500), new Item(18831, 500)};
                case "flask_potion_set":
                    return new Item[]{new Item(23256, 350, Revision.PRE_RS3), new Item(23280, 350, Revision.PRE_RS3), new Item(23292, 350, Revision.PRE_RS3), new Item(23352, 1000, Revision.PRE_RS3), new Item(24000, 1000, Revision.PRE_RS3), new Item(3145, 300, Revision.PRE_RS3)};
                case "barrage_set":
                    return new Item[]{new Item(555, 30000), new Item(560, 20000), new Item(565, 10000)};
                case "vengence_set":
                    return new Item[]{new Item(9075, 20000), new Item(560, 10000), new Item(557, 50000)};
                case "charm_set":
                    return new Item[]{new Item(12158, 1000), new Item(12159, 1000), new Item(12160, 1000), new Item(12163, 1000)};
                case "nex_set_bundle":
                    return new Item[]{new Item(20135, 1), new Item(20139, 1), new Item(20143, 1), new Item(20147, 1), new Item(20151, 1), new Item(20155, 1), new Item(20159, 1), new Item(20163, 1), new Item(20167, 1)};
                case "torva_set":
                    return new Item[]{new Item(20135, 1), new Item(20139, 1), new Item(20143, 1)};
                case "pernix_set":
                    return new Item[]{new Item(20147, 1), new Item(20151, 1), new Item(20155, 1)};
                case "virtus_set":
                    return new Item[]{new Item(20159, 1), new Item(20163, 1), new Item(20167, 1)};
                case "barrow_armor_set_bundle":
                    return new Item[]{new Item(11846, 1), new Item(11848, 1), new Item(11850, 1), new Item(11852, 1), new Item(11854, 1), new Item(11856, 1)};
                case "armadyl_set":
                    return new Item[]{new Item(11718, 1), new Item(11722, 1), new Item(11720, 1)};
                case "spirit_shield_set":
                    return new Item[]{new Item(13734, 1), new Item(13736, 1), new Item(13738, 1), new Item(13740, 1), new Item(13742, 1), new Item(13744, 1), new Item(13754, 1)};
                case "boots_set":
                    return new Item[]{new Item(21787, 1, Revision.PRE_RS3), new Item(21790, 1, Revision.PRE_RS3), new Item(21793, 1, Revision.PRE_RS3)};
                case "chaotic_set":
                    return new Item[]{new Item(18349, 1), new Item(18357, 1), new Item(18355, 1)};
                case "party_hat_set":
                    return new Item[]{new Item(1038, 1), new Item(1040, 1), new Item(1042, 1), new Item(1044, 1), new Item(1046, 1), new Item(1048, 1)};
                case "halloween_set":
                    return new Item[]{new Item(1053, 1), new Item(1055, 1), new Item(1057, 1)};
                case "construction_set":
                    return new Item[]{new Item(8785, 10), new Item(8787, 10), new Item(8789, 10), new Item(9625, 1), new Item(961, 150), new Item(8781, 250), new Item(8782, 500)};
                default:
                    System.out.println("Donation Store Error: Could not find set '" + itemName + "'");
                    return null;

            }
        } catch (Exception e) {
            return null;
        }
    }
}
