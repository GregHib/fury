package com.fury.game.content.skill.free.crafting.spinning;

import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.crafting.SpinningD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 29/12/2016.
 */
public class Spinning extends Action {
    SpinData data;
    int quantity;

    public static void openInterface(Player player, boolean dung) {
        List<Item> items = new ArrayList<>();
        for(int i = dung ? 6 : 0; i < (dung ? SpinData.values().length : 6); i++) {
            SpinData s = SpinData.values()[i];
            if(player.getInventory().contains(s.getIngredient()))
                items.add(s.getProduct());
        }
        Item[] arr = items.toArray(new Item[items.size()]);
        if(arr.length <= 0) {
            player.message("You don't have anything in your inventory that you can spin.");
            return;
        }
        player.getDialogueManager().startDialogue(new SpinningD(), (Object[]) arr);
    }

    public Spinning(SpinData data, int quantity) {
        this.data = data;
        this.quantity = quantity;
    }

    public boolean checkAll(Player player) {
        if (!player.getInventory().contains(data.getIngredient())) {
            player.message("You have ran out of " + data.getIngredient().getDefinition().getName().toLowerCase() + " to spin.");
            return false;
        }
        if (player.getSkills().getLevel(Skill.CRAFTING) < data.getLevel()) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(),"You need a Crafting level of " + data.getLevel() + " in order to spin a " + data.getProduct().getDefinition().getName().toLowerCase() + ".");
            return false;
        }
        if (player.getInventory().getAmount(data.getIngredient()) <= 0) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You don't have any " + data.getIngredient().getDefinition().getName().toLowerCase() + " to spin.");
            return false;
        }
        return true;
    }

    @Override
    public boolean start(final Player player) {
        if (checkAll(player)) {
            setActionDelay(player, 3);
            player.animate(896);
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
        player.getInventory().delete(data.getIngredient());
        player.getInventory().add(data.getProduct());
        player.getSkills().addExperience(Skill.CRAFTING, data.getExperience());
        player.message("You spin the " + data.getIngredient().getDefinition().getName().toLowerCase() + ".", true);
        if(data == SpinData.BOW_STRING)
            Achievements.finishAchievement(player, Achievements.AchievementData.SPIN_BOW_STRING);
        quantity--;
        if (quantity <= 0)
            return -1;
        player.animate(896);
        return Equipment.wearingSeersHeadband(player, 2) ? 3 : 4;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }
}
