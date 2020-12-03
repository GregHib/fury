package com.fury.game.content.skill.member.herblore;

import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 12/07/2016.
 */
public class WaterFilling extends Action {

    private Fill fill;
    private int quantity;

    public WaterFilling(Fill fill, int quantity) {
        this.fill = fill;
        this.quantity = quantity;
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            setActionDelay(player, 1);
            player.animate(832);
            String name = fill.full.getName();
            if (name.contains(" ("))
                name = name.substring(0, name.indexOf(" ("));
            player.message("You fill the " + name + ".");
            return true;
        }
        return false;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        player.getInventory().delete(fill.empty);
        player.getInventory().add(fill.full);
        quantity--;
        if (quantity <= 0)
            return -1;
        player.animate(832);
        return fill.ordinal() == 5 ? 3 : 0;
    }

    @Override
    public void stop(Player player) {

    }

    public boolean checkAll(Player player) {
        if (!player.getInventory().containsAmount(fill.empty)) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You don't have any " + fill.empty.getName().toLowerCase() + " to fill.");
            return false;
        }
        return true;
    }

    public enum Fill {
        VIAL(229, 227),
        BOWL(1923, 1921),
        BUCKET(1925, 1929),
        JUG(1935, 1937),
        FISHBOWL(6667, 6668),
        VASE(3734, 3735),
        PLANT_POT(5350, 5354),
        CLAY(434, 1761),
        WATERING_CAN_0(5331, 5340),
        WATERING_CAN_1(5333, 5340),
        WATERING_CAN_2(5334, 5340),
        WATERING_CAN_3(5335, 5340),
        WATERING_CAN_4(5336, 5340),
        WATERING_CAN_5(5337, 5340),
        WATERING_CAN_6(5338, 5340),
        WATERING_CAN_7(5339, 5340),
        WATERSKIN_0(1831, 1823),
        WATERSKIN_1(1829, 1823),
        WATERSKIN_2(1827, 1823),
        WATERSKIN_3(1825, 1823),
        DUNGEONEERING_VIAL(17490, 17492);

        private Item empty, full;

        Fill(int empty, int full) {
            this.empty = new Item(empty);
            this.full = new Item(full);
        }

        public Item getEmpty() {
            return empty;
        }

        public Item getFull() {
            return full;
        }

    }

    public static Fill getFillByProduce(int produce) {
        for (Fill fill : Fill.values()) {
            if (fill.full.getId() == produce)
                return fill;
        }
        return null;
    }

    public static boolean isFilling(Player player, int empty) {
        for (Fill fill : Fill.values()) {
            if (fill.empty.getId() == empty) {
                fill(player, fill);
                return true;
            }
        }
        return false;
    }

    public static void fill(Player player, Fill fill) {
        if (player.getInventory().getAmount(fill.empty) < 1) {
            player.message("You do not have anything in your inventory to fill.");
            return;
        } else {
            player.getActionManager().setAction(new WaterFilling(fill, player.getInventory().getAmount(fill.empty)));
        }
        if (fill == Fill.VIAL || fill == Fill.DUNGEONEERING_VIAL)
            Achievements.finishAchievement(player, Achievements.AchievementData.FILL_VIAL);
    }

}

