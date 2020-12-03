package com.fury.game.content.skill.member.herblore;

import com.fury.game.content.dialogue.impl.skills.herblore.FlaskDecantingD;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drinkables {

    public static Drink getDrink(int id) {
        for (Drink pot : Drink.values())
            for (int potionId : pot.getId()) {
                if (id == potionId)
                    return pot;
            }
        return null;
    }

    public static int getDoses(Drink pot, Item item) {
        for (int i = pot.getId().length - 1; i >= 0; i--) {
            if (pot.getId()[i] == item.getId())
                return pot.getId().length - i;
        }
        return 0;
    }

    public static boolean mixFlask(Player player, Item used, Item with, int usedSlot, int withSlot) {

        Drink usedDrink = Drinkables.getDrink(used.getId());
        Drink withDrink = Drinkables.getDrink(with.getId());

        boolean flask = used.isEqual(Herblore.POTION_FLASK) || with.isEqual(Herblore.POTION_FLASK);

        if ((usedDrink == null && withDrink == null) || ((usedDrink == null || withDrink == null) && !flask))
            return false;

        if (flask) {
            if(usedDrink != null && usedDrink.isPotion()) {
                player.getActionManager().setAction(new FlaskDecantingD(usedDrink, usedSlot, null, withSlot));
                return true;
            } else if(withDrink != null && withDrink.isPotion()) {
                player.getActionManager().setAction(new FlaskDecantingD(withDrink, withSlot, null, usedSlot));
                return true;
            }
        } else {
            if(withDrink.isPotion() && usedDrink.isFlask()) {
                player.getActionManager().setAction(new FlaskDecantingD(withDrink, withSlot, usedDrink, usedSlot));
                return true;
            } else if(usedDrink.isPotion() && withDrink.isFlask()) {
                player.getActionManager().setAction(new FlaskDecantingD(usedDrink, usedSlot, withDrink, withSlot));
                return true;
            }
        }
        return false;
    }

    private static int getTotalDoses(Player player, Drink potion) {
        int total = 0;
        for(Item item: player.getInventory().getItems()) {
            if(item == null)
                continue;

            Drink drink = Drinkables.getDrink(item.getId());

            if(drink == potion)
                total += getDoses(drink, item);
        }

        return total;
    }

    private static int getTotalPotions(Player player, Drink potion) {
        int total = 0;
        for(Item item : player.getInventory().getItems()) {

            if(item == null)
                continue;

            Drink drink = Drinkables.getDrink(item.getId());

            if(drink == potion)
                total++;
        }

        return total;
    }

    private static boolean hasInventorySpace(Player player, Drink potion, int size, boolean includePots) {
        int totalDoses = getTotalDoses(player, potion);
        int extra = totalDoses % size;
        int spaces = (totalDoses - extra)/size + (extra > 0 ? 1 : 0);
        if(includePots && spaces > 0)
            spaces -= getTotalPotions(player, potion);
        if(player.getInventory().getSpaces() < spaces)
            return false;
        return true;
    }

    private static Drink[] getPotions(Player player, int size) {
        List<Drink> drinks = new ArrayList<>();
        for(Item item : player.getInventory().getItems()) {
            if(item == null)
                continue;

            Drink drink = getDrink(item.getId());
            if(drink != null) {
                if (!drinks.contains(drink) && size <= drink.getMaxDoses())
                    drinks.add(drink);
            }
        }
        return drinks.toArray(new Drink[drinks.size()]);
    }

    public static boolean hasSpaceToDecant(Player player, int size) {
        Drink[] potions = getPotions(player, size);
        for(Drink potion : potions)
            if(!hasInventorySpace(player, potion, size, true))
                return false;

        return true;
    }

    public static void decantDoses(Player player, int size) {
        Drink[] potions = getPotions(player, size);
        Map<Drink, Integer> list = new HashMap<>();

        for(Drink drink : potions)
            list.put(drink, getTotalDoses(player, drink));

        for(Item item : player.getInventory().getItems()) {
            if(item == null)
                continue;

            Drink drink = Drinkables.getDrink(item.getId());

            if(drink == null || size > drink.getMaxDoses())
                continue;

            player.getInventory().delete(item);
        }

        for(Drink potion : potions) {
            int totalDoses = list.get(potion);

            int extra = totalDoses % size;
            int amount = (totalDoses - extra)/size;

            for(int i = 0; i < amount; i++)
                player.getInventory().addSafe(new Item(potion.getIdForDoses(size)));
            if(extra > 0)
                player.getInventory().addSafe(new Item(potion.getIdForDoses(extra)));
        }
    }

    public static int mixPot(Player player, Item fromItem, Item toItem, int fromSlot, int toSlot, boolean single) {
        if (single) {
            if (fromItem.isEqual(Herblore.EMPTY_VIAL) || toItem.isEqual(Herblore.EMPTY_VIAL)) {
                Drink pot = getDrink(fromItem.isEqual(Herblore.EMPTY_VIAL) ? toItem.getId() : fromItem.getId());
                if (pot == null || pot.isFlask())
                    return -1;
                int doses = getDoses(pot, fromItem.isEqual(Herblore.EMPTY_VIAL) ? toItem : fromItem);
                if (doses == 1) {
                    player.getInventory().swap(fromSlot, toSlot);
                    if (single)
                        player.message("You pour from one container into the other.", true);
                    return 1;
                }
                int vialDoses = doses / 2;
                doses -= vialDoses;
                player.getInventory().set(new Item(pot.getIdForDoses(doses), 1), fromItem.isEqual(Herblore.EMPTY_VIAL) ? toSlot : fromSlot);
                player.getInventory().set(new Item(pot.getIdForDoses(vialDoses), 1), fromItem.isEqual(Herblore.EMPTY_VIAL) ? fromSlot : toSlot);
                player.getInventory().refresh();
                if (single)
                    player.message("You split the potion between the two vials.", true);
                return 2;
            }
        }
        Drink pot = getDrink(fromItem.getId());
        if (pot == null)
            return -1;
        int doses2 = getDoses(pot, toItem);
        if (doses2 == 0 || doses2 == pot.getMaxDoses()) // not same pot type or full already
            return -1;
        int doses1 = getDoses(pot, fromItem);
        doses2 += doses1;
        doses1 = doses2 > pot.getMaxDoses() ? doses2 - pot.getMaxDoses() : 0;
        doses2 -= doses1;
        if (doses1 == 0 && pot.isFlask())
            player.getInventory().delete(fromSlot);
        else {
            player.getInventory().set(new Item(doses1 > 0 ? pot.getIdForDoses(doses1) : Herblore.EMPTY_VIAL.getId(), 1), fromSlot);
        }
        player.getInventory().set(new Item(pot.getIdForDoses(doses2), 1), toSlot);
        player.getInventory().refresh();
        if (single)
            player.message("You pour from one container into the other" + (pot.isFlask() && doses1 == 0 ? " and the glass shatters to pieces." : "."));
        return 3;
    }

    public static boolean emptyPot(Player player, Item item, int slot) {
        Drink pot = getDrink(item.getId());
        if (pot == null || pot.isFlask())
            return false;
        item.setId(Herblore.EMPTY_VIAL.getId());
        player.getInventory().refresh();
        player.message("You empty the vial.", true);
        return true;
    }

    public static boolean drink(Player player, Item item, int slot) {
        player.stopAll(false, true, false);
        Drink drink = getDrink(item.getId());
        if (drink == null)
            return false;
        if (!player.getTimers().getPotDelay().elapsed(1300))
            return true;
        if (!player.getControllerManager().canDrink(drink))
            return true;
        if (!drink.getEffect().canDrink(player))
            return true;
        player.getTimers().getPotDelay().reset();
        drink.getEffect().extra(player);
        int dosesLeft = getDoses(drink, item) - 1;
        if (dosesLeft == 0 && (drink.isFlask() || drink.isMix()))
            player.getInventory().delete(slot);
        else {
            player.getInventory().set(new Item(dosesLeft > 0 ? drink.getIdForDoses(dosesLeft) : player.isInDungeoneering() ? Herblore.DUNGEONEERING_VIAL.getId() : drink.isPotion() ? Herblore.EMPTY_VIAL.getId() : getReplacedId(drink), 1), slot);
            player.getInventory().refresh();
        }
        for (Skill skill : drink.getEffect().getAffectedSkills())
            player.getSkills().setLevel(skill, drink.getEffect().getAffectedSkill(player, skill, player.getSkills().getLevel(skill), player.getSkills().getMaxLevel(skill)));
        player.performAnimationNoPriority(new Animation(829));
        if (drink.isFlask() || drink.isPotion()) {
            player.message(drink.getEffect().getDrinkMessage() != null ? drink.getEffect().getDrinkMessage() : "You drink some of your " + item.getName().toLowerCase().replace(" (1)", "").replace(" (2)", "").replace(" (3)", "").replace(" (4)", "").replace(" (5)", "").replace(" (6)", "") + ".", true);
            player.message(dosesLeft == 0 ? "You have finished your " + (drink.isFlask() ? "flask and the glass shatters to pieces." : drink.isPotion() ? "potion." : item.getName().toLowerCase() + ".") : "You have " + dosesLeft + " dose of potion left.", true);
        }
        return true;
    }

    public static void decantSizePots(Player player, int size) {
        for(int index = 0; index < 28; index++) {
            Item item = player.getInventory().get(index);
            Drink pot = getDrink(item.getId());
            if(pot == null)
                continue;

            if(getDoses(pot, item) > size)
                player.getInventory().set(new Item(pot.getIdForDoses(size), item), index);
        }
    }

    public static void decantPotsInv(Player player) {//Cheepo Hax way, probs terrible performance but whatever xD
        int count = 0;
        outLoop:
        for (int fromSlot = 0; fromSlot < 28; fromSlot++) {
            Item fromItem = player.getInventory().get(fromSlot);
            if (fromItem == null)
                continue outLoop;
            innerLoop:
            for (int toSlot = 0; toSlot < 28; toSlot++) {
                Item toItem = player.getInventory().get(toSlot);
                if (toItem == null || fromSlot == toSlot)
                    continue innerLoop;
                if (mixPot(player, fromItem, toItem, fromSlot, toSlot, false) != -1) {
                    count++;
                    break innerLoop;
                }
            }
        }

        if (count != 0) {
            for (Item item : player.getInventory().getItems()) {//because i know someone will bitch about order.
                if (item == null || !item.isEqual(Herblore.EMPTY_VIAL))
                    continue;
                player.getInventory().delete(item);
                player.getInventory().sort();
                player.getInventory().add(item);
            }
            player.getInventory().refresh();
        }
    }

    @SuppressWarnings("incomplete-switch")
    private static int getReplacedId(Drink drink) {
        switch (drink) {
            case JUG:
                return 1935;
            case BEER:
            case TANKARD:
            case GREENMANS_ALE:
            case AXEMANS_ALE:
            case SLAYER_RESPITE:
            case RANGERS_AID:
            case MOONLIGHT_MEAD:
            case DRAGON_BITTER:
            case ASGARNIAN_ALE:
            case CHEF_DELIGHT:
            case CHEF_DELIGHT_KEG:
            case CIDER:
            case CIDER_KEG:
            case WIZARD_MIND_BOMB:
            case DWARVEN_STOUT:
            case GROG:
            case BANDIT_BREW:
                return 1919;
            case GREENMANS_ALE_KEG:
            case AXEMANS_ALE_KEG:
            case SLAYER_RESPITE_KEG:
            case MOONLIGHT_MEAD_KEG:
            case DRAGON_BITTER_KEG:
            case ASGARNIAN_ALE_KEG:
            case DWARVEN_STOUT_KEG:
            case KEG_OF_BEER:
                return 10885;
        }
        return 0;
    }

    public static void resetOverLoadEffect(Player player) {
        if (!player.isDead()) {
            player.getSkills().reduce(Skill.ATTACK);
            player.getSkills().reduce(Skill.STRENGTH);
            player.getSkills().reduce(Skill.DEFENCE);
            player.getSkills().reduce(Skill.MAGIC);
            player.getSkills().reduce(Skill.RANGED);
            player.getHealth().heal(500);
        }
        player.message("The effects of overload have worn off and you feel normal again.", 0x480000);
    }

    public static void applyOverLoadEffect(Player player) {
        if(player.isInWilderness()) {
            player.getSkills().boost(Skill.ATTACK, 5, 0.15);
            player.getSkills().boost(Skill.STRENGTH, 5, 0.15);
            player.getSkills().boost(Skill.DEFENCE, 5, 0.15);
            player.getSkills().boost(Skill.MAGIC, 5);
            player.getSkills().boost(Skill.RANGED, 5, 0.10);
        } else {
            player.getSkills().boost(Skill.ATTACK, 5, 0.22);
            player.getSkills().boost(Skill.STRENGTH, 5, 0.22);
            player.getSkills().boost(Skill.DEFENCE, 5, 0.22);
            player.getSkills().boost(Skill.MAGIC, 7);
            player.getSkills().boost(Skill.RANGED, 4, 0.1923);
        }
    }

    private Drinkables() {

    }
}
