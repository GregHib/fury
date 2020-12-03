package com.fury.game.content.skill.free.crafting.glass;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Jon on 10/2/2016.
 */
public class GlassBlowing extends Action {

    private static final Item MOLTEN_GLASS = new Item(1775);
    private Glass glass;
    private int amount;

    private GlassBlowing(Glass glass, int amount) {
        this.glass = glass;
        this.amount = amount;
    }

    public static boolean createGlass(final Player player, final int buttonId) {
        for (final Glass glass : Glass.values()) {
            if (buttonId == glass.getButtonId(buttonId)) {
                int amount = glass.getAmount(buttonId);
                player.getActionManager().setAction(new GlassBlowing(glass, amount));
                return true;
            }
        }
        return false;
    }


    public static boolean makeGlass(final Player player) {
        player.stopAll();
        player.getPacketSender().sendInterface(11462);
        return false;
    }

    private boolean hasReq(Player player) {
        if (!player.getSkills().hasRequirement(Skill.CRAFTING, glass.getLevelReq(), "make this")) {
            player.getPacketSender().sendInterfaceRemoval();
            return false;
        }

        if (!player.getInventory().contains(MOLTEN_GLASS)) {
            player.message("You have run out of molten glass.");
            return false;
        }

        return amount != 0;
    }

    @Override
    public boolean start(Player player) {
        if(!hasReq(player))
            return false;


        player.animate(884);
        player.getPacketSender().sendInterfaceRemoval();
        return true;
    }

    @Override
    public boolean process(Player player) {
        return hasReq(player);
    }

    @Override
    public int processWithDelay(Player player) {
        player.getInventory().delete(MOLTEN_GLASS);
        player.getInventory().add(new Item(glass.getNewId()));
        player.message("You make a " + glass.getName() + ".", true);
        player.getSkills().addExperience(Skill.CRAFTING, (int) glass.getXP());
        player.animate(884);
        amount--;
        return 5;
    }

    @Override
    public void stop(Player player) {

    }
}
