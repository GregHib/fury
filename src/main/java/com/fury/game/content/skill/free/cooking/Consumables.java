package com.fury.game.content.skill.free.cooking;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.core.model.item.Item;

/**
 * Consumables are items that players can use to restore stats/points.
 * Examples of Consumable items: Food, Potions
 *
 * @author Gabriel Hannason
 */

public class Consumables {

    public static boolean handleFood(Player player, Item item, int slot) {
        return handleFood(player, player, item, slot);
    }

    public static boolean handleFood(Player player, Player target, Item item, int slot) {
        Food food = getFood(item.getId());
        if (food != null) {
            eat(player, target, item, slot);
            return true;
        }
        return false;
    }

    public static Food getFood(int item) {
        Food food = Food.types.get(item);
        if (food != null) {
            return food;
        }
        return null;
    }

    public static boolean isFood(int item) {
        Food food = getFood(item);
        return food != null;
    }

    public static int getNextFoodSlot(Player player) {
        Item[] inv = player.getInventory().getItems();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null && isFood(inv[i].getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * The heal option on the Health Orb
     *
     * @param player The player to heal
     */
    public static void handleHealAction(Player player) {
        for (Item item : player.getInventory().getItems()) {
            if (item != null) {
                if (handleFood(player, item, player.getInventory().indexOf(item))) {
                    return;
                }
            }
        }
        player.message("You do not have any items that can heal you in your inventory.");
    }

    public static void handleHealOtherAction(Player player, Player target, Item item, int slot) {
        if (Consumables.isFood(item.getId()))
            Consumables.eat(player, target, item, slot);

    }

    public static int getHealAmount(Item item) {
        Food type = Food.forId(item.getId());
        int heal = type.getHeal();
        return heal;
    }

    public static boolean eat(Player player, Player target, Item item, int slot) {
        Food food = Food.forId(item.getId());
        if (food == null)
            return false;
        if (!target.getTimers().getFoodDelay().elapsed(1800))
            return true;
        if (!target.getControllerManager().canEat(food))
            return true;
        target.stopAll(false, false);
        target.animate(829);

        target.getTimers().getFoodDelay().reset();

        if (target.getActionManager().getActionDelay() < 3)
            target.getActionManager().setActionDelay(3);

        //Message
        int nextId = food.getNewId();
        String verb = food == Food.BANDAGES ? "use" : "eat";
        int pieces = getPieces(food);
        String amount = pieces > 1 ? nextId == -1 ? "the remaining" : pieces == 2 ? "half of the" : "part of the" : "the";
        String name = item.getName().toLowerCase(), message = "You " + verb + " " + amount + " " + name + ".";
        player.message(message);

        //Remove/replace
        if (food.getNewId() == -1)
            player.getInventory().delete(slot);
        else
            player.getInventory().set(new Item(food.getNewId()), slot);
        player.getInventory().refresh();


        //Effect
        int modifier = 0;
        FoodEffect effect = food.getEffect();
        if (effect != null) {
            effect.activateEffect(target);
            modifier = effect.getHitpointsModification(target);
        }

        target.getHealth().heal(food.getHeal(), modifier);
        Sounds.sendSound(target, Sounds.Sound.EAT_FOOD);
        return true;
    }

    private static int getPieces(Food food) {
        switch (food) {
            case REDBERRY_PIE:
            case HALF_REDBERRY_PIE:
            case MEAT_PIE:
            case HALF_MEAT_PIE:
            case APPLE_PIE:
            case HALF_APPLE_PIE:
            case GARDEN_PIE:
            case HALF_GARDEN_PIE:
            case FISH_PIE:
            case HALF_FISH_PIE:
            case ADMIRAL_PIE:
            case HALF_ADMIRAL_PIE:
            case WILD_PIE:
            case HALF_WILD_PIE:
            case SUMMER_PIE:
            case HALF_SUMMER_PIE:
            case PLAIN_PIZZA:
            case HALF_PLAIN_PIZZA:
            case MEAT_PIZZA:
            case HALF_MEAT_PIZZA:
            case ANCHOVY_PIZZA:
            case HALF_ANCHOVY_PIZZA:
            case PINEAPPLE_PIZZA:
            case HALF_PINEAPPLE_PIZZA:
                return 2;
            case CAKE:
            case SECOND_CAKE_SLICE:
            case THIRD_CAKE_SLICE:
            case CHOCOLATE_CAKE:
            case SECOND_CHOCOLATE_CAKE_SLICE:
            case CHOCOLATE_SLICE:
                return 3;
        }
        return 1;
    }
}
