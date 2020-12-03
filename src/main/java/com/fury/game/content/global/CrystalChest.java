package com.fury.game.content.global;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.util.Misc;

public class CrystalChest {

    public static void handleChest(final Player player, final GameObject chest) {
        if (!player.getTimers().getClickDelay().elapsed(3000))
            return;
        Item key = new Item(989);
        if (!player.getInventory().contains(key)) {
            player.message("This chest can only be opened with a Crystal key.");
            return;
        }

        if (player.getInventory().getSpaces() < 2) {
            player.message("You need at least 2 free inventory slots to open this.");
            return;
        }

        player.animate(827);
        player.getInventory().delete(key);
        player.message("You attempt to open the chest..");
        GameWorld.schedule(2, () -> {
            double r = Math.random();
            if (r < 0.9) {
                Item loot = new Item(newRewards[Misc.getRandom(newRewards.length - 1)], 1);
                player.getInventory().addSafe(loot);
                if (r < 0.6)
                    player.getInventory().addSafe(new Item(1631, 1));
                player.message("..and find " + (r < 0.6 ? "some items" : "an item") + "!");
                TempObjectManager.sendObjectTemporary(player, new GameObject(173, chest, 10, 3), 4000, false, true);
            } else {
                player.message("..but your key breaks and you loose part of it!");
                player.getInventory().addSafe(new Item(r < 0.975 ? 985 : 987, 1));
            }
        });
        player.getTimers().getClickDelay().reset();
    }

    private static final int[] newRewards = new int[]{
            10430, 10432, 10434, 10436, 10438, 10394, 10398, 10396, 13672, 13673, 13674, 13675, 2581,
            2583, 2585, 2587, 2589, 2599, 2601, 2603, 2605, 2623, 2625, 2627, 2629, 3472,
            10458, 10444, 10456, 10370, 10368, 10372, 10468, 10474, 10450, 10374, 10460, 19362, 19461, 19374, 19459,
            10400, 10402, 10404, 10406, 10408, 10410, 10412, 10414, 10416, 10418, 10420, 10422, 10424, 10426, 10428,
            3477, 3474, 7364, 7368, 7372, 7376, 7380, 7384, 7388, 7392, 7396, 2591, 2593, 2595, 2597, 2607, 2609, 2611,
            2613, 2615, 2617, 2619, 2621, 3473, 3475, 3476, 7362, 7366, 7370, 7374, 7378, 7382, 7386, 7390, 7394, 13095,
            7323, 7325, 7327, 10316, 10318, 10320, 10322, 10324, 2631, 13107, 13109, 13111, 13133, 13115, 3481, 3483,
            3485, 3486, 19281, 19284, 19287, 19290, 19293, 19296, 19299, 19302, 19305, 19364, 19451, 19376, 19455,
            13097, 13099, 2639, 2641, 2643, 11277, 2633, 2635, 2637, 19327, 19329, 19331, 19323, 19325, 7319, 7321,
            19388, 19453, 19394, 19370, 19457, 19384, 10386, 10452, 10440, 10384, 10388, 10464, 10470, 10446, 10390,
            19463, 19386, 19392, 19368, 19465, 19380
    };

    private static final Item[][] itemRewards = {
            {new Item(1969, 1), new Item(995, 200000)}, //set 1 SPINACH ROLL
            {new Item(1631, 1)}, //set 2 Dragonstone only set
            {new Item(995, 100000), new Item(373, 1)}, //set 3 Swordfish set
            {new Item(554, 50), new Item(555, 50), new Item(556, 50), new Item(557, 50), new Item(558, 50), new Item(559, 50), new Item(560, 10), new Item(561, 10), new Item(562, 10), new Item(563, 10), new Item(564, 10)}, //set 4 Full rune set
            {new Item(1631, 1), new Item(454, 100)}, //set 5 Coal
            {new Item(1615, 1), new Item(1601, 1), new Item(1603, 1)}, //set 6 Cut gems
            {new Item(1631, 1), new Item(985, 1), new Item(995, 7500)}, //set 7 Crystal Key 1
            {new Item(1631, 1), new Item(2363, 1)}, //set 8 Dragon Sq Half
            {new Item(1631, 1), new Item(987, 1), new Item(995, 7500)}, //set 9 Crystal Key 2
            {new Item(1631, 1), new Item(441, 150)}, //set 10 Iron Ore
            {new Item(1631, 1), new Item(1185, 1)}, //set 11 Rune armor 1
            {new Item(1631, 1), new Item(1079, 1)}, //set 12 Rune armor 2
            {new Item(1631, 1), new Item(1093, 1)}, //set 13 Rune armor 3
            {new Item(11710, 1)}, //set 14 Godsword shard 1
            {new Item(11712, 1)}, //set 15 Godsword shard 2
            {new Item(11714, 1)}, //set 16 Godsword shard 3
            {new Item(11732, 1)}, //set 17 Dragon Boots
            {new Item(3486, 1)}, //set 18 Gilded Armor 1
            {new Item(3481, 1)}, //set 19 Gilded Armor 2
            {new Item(3483, 1)}, //set 20 Gilded Armor 3
            {new Item(3485, 1)}, //set 21 Gilded Armor 4
            {new Item(3488, 1)}, //set 22 Gilded Armor 5
            {new Item(15332, 1)}, //set 23 Overload
            {new Item(6918, 1)}, //set 24 Infinity Armor 1
            {new Item(6916, 1)}, //set 25 Infinity Armor 2
            {new Item(6924, 1)}, //set 26 Infinity Armor 3
            {new Item(6922, 1)}, //set 27 Infinity Armor 4
            {new Item(6920, 1)}, //set 28 Infinity Armor 5
            {new Item(2665, 1)}, //set 29 Saradomin Armor 1
            {new Item(2661, 1)}, //set 30 Saradomin Armor 1
            {new Item(2663, 1)}, //set 31 Saradomin Armor 1
            {new Item(2667, 1)}, //set 32 Saradomin Armor 1
            {new Item(2673, 1)}, //set 33 Guthix Armor 1
            {new Item(2669, 1)}, //set 34 Guthix Armor 1
            {new Item(2671, 1)}, //set 35 Guthix Armor 1
            {new Item(2675, 1)}, //set 36 Guthix Armor 1
            {new Item(2657, 1)}, //set 37 Zamorak Armor 1
            {new Item(2653, 1)}, //set 38 Zamorak Armor 1
            {new Item(2655, 1)}, //set 39 Zamorak Armor 1
            {new Item(2659, 1)}, //set 40 Zamorak Armor 1
            {new Item(2579, 1)}, //set 41 Ranger Boots
            {new Item(2581, 1)}, //set 42 Robin Hood Hat
            /*{new Item(4716, 1)}, //set 43 Dh piece helm
            {new Item(4720, 1)}, //set 44 dh piece body
            {new Item(4722, 1)}, //set 45 dh piece legs
            {new Item(4718, 1)}, //set 46 dh axe
            {new Item(4736, 1)}, //set 47 karil top
            {new Item(4738, 1)}, //set 48 karil bottom
            {new Item(4734, 1)}, //set 49 karil crossbow
            {new Item(4732, 1)}, //set 50 karil coif
            {new Item(4745, 1)}, //set 51 torag helm
            {new Item(4747, 1)}, //set 52 torag wep
            {new Item(4749, 1)}, //set 53 torag body
            {new Item(4751, 1)}, //set 54 torag bottom
            {new Item(4724, 1)}, //set 55 guthan helm
            {new Item(4726, 1)}, //set 56 guthan wep
            {new Item(4728, 1)}, //set 57 guthan body
            {new Item(4730, 1)}, //set 58 guthan bottom
            {new Item(4708, 1)}, //set 59 ahrims helm
            {new Item(4710, 1)}, //set 60 ahrims wep
            {new Item(4712, 1)}, //set 61 ahrims top
            {new Item(4714, 1)}, //set 62 ahrims bottom
            {new Item(4753, 1)}, //set 63 verac helm
            {new Item(4755, 1)}, //set 64 verac helm
            {new Item(4757, 1)}, //set 65 verac helm
            {new Item(4759, 1)}, //set 66 verac helm
            {new Item(4151, 1)}, //set 67 whip*/
            {new Item(3751, 1), new Item(1631, 1)}, //set 68 Berserker Helm
    };

}
