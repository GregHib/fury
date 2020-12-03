package com.fury.game.content.skill.member.summoning;

import com.fury.cache.def.Loader;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.FontUtils;

/**
 * Charming imp
 *
 * @author Kova+
 * Redone by Gabbe
 */
public class CharmingImp {

    public static final int GREEN_CHARM = 12159;
    public static final int GOLD_CHARM = 12158;
    public static final int CRIM_CHARM = 12160;
    public static final int BLUE_CHARM = 12163;

    public static void changeConfig(Player player, int index, int config) {
        player.getSummoning().setCharmImpConfig(index, config);
        player.getPacketSender().sendInterfaceRemoval().sendMessage(FontUtils.imageTags(535) + " Your configuration for " + Loader.getItem(getCharmForIndex(index)).getName() + "s has been saved.", 0x996633);
    }

    public static boolean handleCharmDrop(Player player, int itemId, int amount) {
        int index = getIndexForCharm(itemId);
        if (index == -1) {
            return false;
        }
        switch (player.getSummoning().getCharmImpConfig(index)) {
            case 0:
                return sendToInv(player, itemId, amount);
            case 1:
                turnIntoXp(player, itemId, amount);
                return true;
        }
        return false;
    }


    private static boolean sendToInv(Player player, int itemId, int amount) {
        if (!player.getInventory().contains(new Item(itemId)) && player.getInventory().getSpaces() == 0) {
            player.message("Your inventory is full, the charming imp is unable to pick up any charms!");
            return false;
        }
        sendMessage(player, 0, itemId, amount);
        player.getInventory().add(new Item(itemId, amount));
        return true;
    }

    private static void turnIntoXp(Player player, int itemId, int amount) {
        switch (itemId) {
            case GOLD_CHARM:
                player.getSkills().addExperience(Skill.SUMMONING, 230 * amount);
                break;
            case GREEN_CHARM:
                player.getSkills().addExperience(Skill.SUMMONING, 282 * amount);
                break;
            case CRIM_CHARM:
                player.getSkills().addExperience(Skill.SUMMONING, 380 * amount);
                break;
            case BLUE_CHARM:
                player.getSkills().addExperience(Skill.SUMMONING, 480 * amount);
                break;
        }
        sendMessage(player, 1, itemId, amount);
    }

    private static void sendMessage(Player player, int config, int itemId, int amount) {
        String itemName = Loader.getItem(itemId).getName();
        if (amount > 1) {
            itemName += "s";
        }
        switch (config) {
            case 0:
                player.message("Your charming imp has found " + FontUtils.colourTags(0xff0000) + amount + FontUtils.COL_END + " " + itemName + " and placed it in your inventory.", true);
                break;
            case 1:
                player.message("Your charming imp has found " + FontUtils.colourTags(0xff0000) + amount + FontUtils.COL_END + " " + itemName + " and turned it into experience.", true);
                break;
        }
    }

    public static void sendConfig(Player player) {
        for (int i = 0; i < 4; i++) {
            int state = player.getSummoning().getCharmImpConfig(i);
            int charm = getCharmForIndex(i);
            switch (state) {
                case 0:
                    player.message(FontUtils.imageTags(535) + " Your charming imp is placing all " + Loader.getItem(charm).getName() + "s it finds in your inventory.", 0x996633);
                    break;
                case 1:
                    player.message(FontUtils.imageTags(535) + " Your charming imp is turning all " + Loader.getItem(charm).getName() + "s it finds into experience.", 0x996633);
                    break;
            }
        }
    }

    private static int getIndexForCharm(int charm) {
        switch (charm) {
            case GOLD_CHARM:
                return 0;
            case GREEN_CHARM:
                return 1;
            case CRIM_CHARM:
                return 2;
            case BLUE_CHARM:
                return 3;
        }
        return -1;
    }

    private static int getCharmForIndex(int index) {
        switch (index) {
            case 0:
                return GOLD_CHARM;
            case 1:
                return GREEN_CHARM;
            case 2:
                return CRIM_CHARM;
            case 3:
                return BLUE_CHARM;
        }
        return -1;
    }
}
