package com.fury.game.content.skill.free.crafting.leather;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class CraftLeather extends Action {
    private static Item thread = new Item(1734);

    private LeatherData leather;
    private int amount;

    public CraftLeather(LeatherData leather, int amount) {
        this.leather = leather;
        this.amount = amount;
    }

    @Override
    public boolean start(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        return hasReq(player);
    }

    @Override
    public boolean process(Player player) {
        return hasReq(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if(Misc.getRandom(5) <= 3)
            player.getInventory().delete(thread);
        player.getInventory().delete(new Item(leather.getLeather(), leather.getLeatherAmount()));
        player.getInventory().addSafe(new Item(leather.getProduct()));
        player.getSkills().addExperience(Skill.CRAFTING, leather.getXP());
//					if(l == LeatherData.LEATHER_BOOTS) {
//						Achievements.finishAchievement(player, AchievementData.CRAFT_A_PAIR_OF_LEATHER_BOOTS);
//					} else if(l == LeatherData.BLACK_DHIDE_BODY) {
//						Achievements.doProgress(player, AchievementData.CRAFT_20_BLACK_DHIDE_BODIES);
//					}
        player.animate(1249);
        amount--;
        if (amount <= 0) {
            stop(player);
            return -1;
        }
        return 2;
    }

    @Override
    public void stop(Player player) {

    }

    private boolean hasReq(Player player) {
        if (!player.getSkills().hasRequirement(Skill.CRAFTING, leather.getLevelReq(), "make this"))
            return false;

        if (!player.getInventory().contains(new Item(1734))) {
            player.message("You need some thread to make this.");
            return false;
        }

        if (player.getInventory().getAmount(leather.getLeather()) < leather.getLeatherAmount()) {
            player.message("You need " + leather.getLeatherAmount() + " " + leather.getLeather().getDefinition().getName().toLowerCase() +" to make this item.");
            return false;
        }

        Item mat = new Item(1734);
        if (!player.getInventory().contains(mat) || !player.getInventory().contains(new Item(1733)) || player.getInventory().getAmount(leather.getLeather()) < leather.getLeatherAmount()) {
            player.message("You have run out of materials.");
            return false;
        }
        return true;
    }
}