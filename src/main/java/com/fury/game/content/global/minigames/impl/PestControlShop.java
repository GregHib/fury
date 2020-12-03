package com.fury.game.content.global.minigames.impl;

import com.fury.cache.def.Loader;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

public class PestControlShop {

    public static boolean handleInterface(Player player, int id) {
        if (player.getInterfaceId() == 18730 || player.getInterfaceId() == 18852 || player.getInterfaceId() == 19500) {
            switch (id) {
                case 18857://Experience Tab
                case 19505:
                    player.getPacketSender().sendInterface(18730);
                    break;
                case 18758://Equipment Tab
                case 19509:
                    player.getPacketSender().sendInterface(18852);
                    break;
                case 18762://Consumables Tab
                case 18865:
                    player.getPacketSender().sendInterface(19500);
                    break;
                /**
                 * Pest control reward interface
                 */
                //Experience Tab
                case 18774:
                    buyFromShop(player, false, Skill.ATTACK.ordinal(), 385, 1);
                    return true;//Attack 1x
                case 18776:
                    buyFromShop(player, false, Skill.ATTACK.ordinal(), 385, 10);
                    return true;//Attack 10x
                case 18778:
                    buyFromShop(player, false, Skill.ATTACK.ordinal(), 385, 100);
                    return true;//Attack 100x

                case 18786:
                    buyFromShop(player, false, Skill.STRENGTH.ordinal(), 420, 1);
                    return true;//Strength 1x
                case 18788:
                    buyFromShop(player, false, Skill.STRENGTH.ordinal(), 420, 10);
                    return true;//Strength 10x
                case 18790:
                    buyFromShop(player, false, Skill.STRENGTH.ordinal(), 420, 100);
                    return true;//Strength 100x

                case 18798:
                    buyFromShop(player, false, Skill.DEFENCE.ordinal(), 385, 1);
                    return true;//Defense 1x
                case 18800:
                    buyFromShop(player, false, Skill.DEFENCE.ordinal(), 385, 10);
                    return true;//Defense 10x
                case 18802:
                    buyFromShop(player, false, Skill.DEFENCE.ordinal(), 385, 100);
                    return true;//Defense 100x

                case 18810:
                    buyFromShop(player, false, Skill.CONSTITUTION.ordinal(), 385, 1);
                    return true;//Constitution 1x
                case 18812:
                    buyFromShop(player, false, Skill.CONSTITUTION.ordinal(), 385, 10);
                    return true;//Constitution 10x
                case 18814:
                    buyFromShop(player, false, Skill.CONSTITUTION.ordinal(), 385, 100);
                    return true;//Constitution 100x

                case 18822:
                    buyFromShop(player, false, Skill.RANGED.ordinal(), 288, 1);
                    return true;//Ranged 1x
                case 18824:
                    buyFromShop(player, false, Skill.RANGED.ordinal(), 288, 10);
                    return true;//Ranged 10x
                case 18826:
                    buyFromShop(player, false, Skill.RANGED.ordinal(), 288, 100);
                    return true;//Ranged 100x

                case 18834:
                    buyFromShop(player, false, Skill.MAGIC.ordinal(), 320, 1);
                    return true;//Magic 1x
                case 18836:
                    buyFromShop(player, false, Skill.MAGIC.ordinal(), 320, 10);
                    return true;//Magic 10x
                case 18838:
                    buyFromShop(player, false, Skill.MAGIC.ordinal(), 320, 100);
                    return true;//Magic 100x

                case 18846:
                    buyFromShop(player, false, Skill.PRAYER.ordinal(), 144, 1);
                    return true;//Prayer 1x
                case 18848:
                    buyFromShop(player, false, Skill.PRAYER.ordinal(), 144, 10);
                    return true;//Prayer 10x
                case 18850:
                    buyFromShop(player, false, Skill.PRAYER.ordinal(), 144, 100);
                    return true;//Prayer 100x
                //Equipment
                case 18877:
                    buyFromShop(player, true, 11665, 1, 200);
                    return true;//Void Melee Helm
                case 18885:
                    buyFromShop(player, true, 11664, 1, 200);
                    return true;//Void Range Helm
                case 18893:
                    buyFromShop(player, true, 11663, 1, 200);
                    return true;//Void Mage Helm
                case 18901:
                    buyFromShop(player, true, 8839, 1, 250);
                    return true;//Void top
                case 18909:
                    buyFromShop(player, true, 8840, 1, 250);
                    return true;//Void robe
                case 18917:
                    buyFromShop(player, true, 8842, 1, 150);
                    return true;//Void gloves
                case 18925:
                    buyFromShop(player, true, 19712, 1, 125);
                    return true;//Void Melee Helm
                case 18933:
                    buyFromShop(player, true, 19785, 1, 300);
                    return true;//Elite void top
                case 18941:
                    buyFromShop(player, true, 19786, 1, 200);
                    return true;//Elite void robes
                //Consumables
                case 19525:
                    buyFromShop(player, "Herb Pack", new int[]{270, 268, 2482, 266, 3001}, new int[]{Misc.getRandomGaussianDistribution(1, 15), Misc.getRandomGaussianDistribution(1, 15), Misc.getRandomGaussianDistribution(1, 15), Misc.getRandomGaussianDistribution(1, 15), Misc.getRandomGaussianDistribution(1, 15)}, 30);
                    return true;//Herb pack
                case 19533:
                    buyFromShop(player, "Mineral Pack", new int[]{452, 450, 448, 445, 454}, new int[]{Misc.getRandomGaussianDistribution(1, 25), Misc.getRandomGaussianDistribution(1, 25), Misc.getRandomGaussianDistribution(1, 25), Misc.getRandomGaussianDistribution(1, 25), Misc.getRandomGaussianDistribution(25, 200)}, 15);
                    return true;//Mineral Pack
                case 19541:
                    buyFromShop(player, "Seed Pack", new int[]{5304, 5315, 5303, 5300, 5100, 5321, 5323}, new int[]{Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5), Misc.getRandomGaussianDistribution(1, 5)}, 15);
                    return true;//Seed Pack

                case 19549:
                    buyFromShop(player, true, 12166, 1, 2);
                    return true;//Spinner Charm 1x
                case 19551:
                    buyFromShop(player, true, 12166, 10, 20);
                    return true;//Spinner Charm 10x
                case 19553:
                    buyFromShop(player, true, 12166, 100, 200);
                    return true;//Spinner Charm 100x

                case 19561:
                    buyFromShop(player, true, 12167, 1, 2);
                    return true;//Torcher Charm 1x
                case 19563:
                    buyFromShop(player, true, 12167, 10, 20);
                    return true;//Torcher Charm 10x
                case 19565:
                    buyFromShop(player, true, 12167, 100, 200);
                    return true;//Torcher Charm 100x

                case 19573:
                    buyFromShop(player, true, 12164, 1, 2);
                    return true;//Ravager Charm 1x
                case 19575:
                    buyFromShop(player, true, 12164, 10, 20);
                    return true;//Ravager Charm 10x
                case 19577:
                    buyFromShop(player, true, 12164, 100, 200);
                    return true;//Ravager Charm 100x

                case 19585:
                    buyFromShop(player, true, 12165, 1, 2);
                    return true;//Shifter Charm 1x
                case 19587:
                    buyFromShop(player, true, 12165, 10, 20);
                    return true;//Shifter Charm 10x
                case 19589:
                    buyFromShop(player, true, 12165, 100, 200);
                    return true;//Shifter Charm 100x
            }
        }
        return false;
    }

    /**
     * Handles the shop
     *
     * @param p      The player buying something from the shop
     * @param item   The item which the player is buying
     * @param id     The id of the item/skill which the player is buying
     * @param amount The amount of the item/skill xp which the player is buying
     * @param cost   The amount it costs to buy this item
     */
    public static void buyFromShop(Player p, boolean item, int id, int amount, int cost) {
        if (!p.getPoints().has(Points.COMMENDATIONS, cost)) {
            p.message("You don't have enough Commendations to purchase this.");
            return;
        }
        if (!p.getTimers().getClickDelay().elapsed(500))
            return;
        String name = Loader.getItem(id).getName();
        final String comm = cost > 1 ? "Commendations" : "Commendation";
        if (!item) {
            p.getPoints().remove(Points.COMMENDATIONS, cost);
            Skill skill = Skill.forId(id);
            int xp = amount * cost;
            p.getSkills().addExperience(skill, xp, true);
            p.message("You have purchased " + xp + " " + Misc.formatText(skill.toString().toLowerCase()) + " XP.");
        } else {
            if (p.getInventory().getSpaces() == 0) {
                p.getInventory().full();
                return;
            }
            p.getPoints().remove(Points.COMMENDATIONS, cost);
            p.getInventory().add(new Item(id, amount));
            p.getPointsHandler().refreshPanel();
            p.message("You have purchased " + Misc.anOrA(name) + " " + name + " for " + cost + " " + comm + ".");
        }
        updateCommendations(p);
        p.getTimers().getClickDelay().reset();
    }

    public static void buyFromShop(Player p, String name, int[] id, int[] amount, int cost) {
        if (!p.getPoints().has(Points.COMMENDATIONS, cost)) {
            p.message("You don't have enough Commendations to purchase this.");
            return;
        }
        if (p.getInventory().getSpaces() < id.length) {
            p.message("You need to have at least " + id.length + " free inventory slots to purchase this.");
            return;
        }
        if (!p.getTimers().getClickDelay().elapsed(500))
            return;

        if (p.getInventory().getSpaces() == 0) {
            p.getInventory().full();
            return;
        }
        p.getPoints().remove(Points.COMMENDATIONS, cost);
        for (int i = 0; i < id.length; i++)
            p.getInventory().add(new Item(id[i], amount[i]));

        p.getPointsHandler().refreshPanel();
        p.message("You have purchased " + name + " for " + cost + " Commendations.");

        updateCommendations(p);
        p.getTimers().getClickDelay().reset();
    }

    public static void updateCommendations(Player p) {
        p.getPacketSender().sendString(18767, Integer.toString(p.getPoints().getInt(Points.COMMENDATIONS)));
        p.getPacketSender().sendString(18870, Integer.toString(p.getPoints().getInt(Points.COMMENDATIONS)));
        p.getPacketSender().sendString(19518, Integer.toString(p.getPoints().getInt(Points.COMMENDATIONS)));
    }
}
