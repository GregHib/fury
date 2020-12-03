package com.fury.game.content.global;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.dialogue.impl.misc.gambling.FlowerD;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.world.GameWorld;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class Gambling {

    public static void rollDice(Player player) {
        if (player.getClanChatName() == null) {
            player.message("You need to be in a clanchat channel to roll a dice.");
            return;
        } else if (player.getClanChatName().equalsIgnoreCase("help")) {
            player.message("You can't roll a dice in this clanchat channel!");
            return;
        }
        if (!player.getTimers().getClickDelay().elapsed(5000)) {
            player.message("You must wait 5 seconds between each dice cast.");
            return;
        }
        player.getMovement().reset();
        player.animate(11900);
        player.graphic(2075);
        ClanChatManager.sendMessage(player.getCurrentClanChat(), FontUtils.BLACK + "[ClanChat] " + FontUtils.WHITE + player.getUsername() + " just rolled " + FontUtils.COL_END + Misc.getRandom(100) + FontUtils.WHITE + " on the percentile dice." + FontUtils.COL_END + FontUtils.COL_END);
        player.getTimers().getClickDelay().reset();
    }

    public static void plantSeed(Player player) {
        if (!player.getTimers().getClickDelay().elapsed(3000))
            return;
        for (Mob mob : GameWorld.getRegions().getLocalNpcs(player)) {
            if (mob != null && mob.sameAs(player)) {
                player.message("You cannot plant a seed right here.");
                return;
            }
        }
        if (ObjectManager.getStandardObject(player) != null) {
            player.message("You cannot plant a seed right here.");
            return;
        }
        FlowersData flowers = FlowersData.generate();
        final GameObject flower = new GameObject(flowers.objectId, player.copyPosition());
        player.getMovement().reset();
        player.getInventory().delete(new Item(299));
        player.animate(827);
        player.message("You plant the seed..", true);
        player.getMovement().reset();
        player.setInteractingObject(flower);
        player.getDialogueManager().startDialogue(new FlowerD());
        player.getMovement().stepAway();
        TempObjectManager.spawnObjectTemporary(flower, 90000, false, true);
        player.getDirection().face(flower);
        player.getTimers().getClickDelay().reset();
    }

    public enum FlowersData {
        PASTEL_FLOWERS(2980, 2460),
        RED_FLOWERS(2981, 2462),
        BLUE_FLOWERS(2982, 2464),
        YELLOW_FLOWERS(2983, 2466),
        PURPLE_FLOWERS(2984, 2468),
        ORANGE_FLOWERS(2985, 2470),
        RAINBOW_FLOWERS(2986, 2472),

        WHITE_FLOWERS(2987, 2474),
        BLACK_FLOWERS(2988, 2476);

        FlowersData(int objectId, int itemId) {
            this.objectId = objectId;
            this.itemId = itemId;
        }

        public int objectId;
        public int itemId;

        public static FlowersData forObject(int object) {
            for (FlowersData data : FlowersData.values()) {
                if (data.objectId == object)
                    return data;
            }
            return null;
        }

        public static FlowersData generate() {
            double RANDOM = (Math.random() * 100);
            if (RANDOM >= 1) {
                return values()[Misc.getRandom(6)];
            } else {
                return Misc.getRandom(3) == 1 ? WHITE_FLOWERS : BLACK_FLOWERS;
            }
        }
    }
}
