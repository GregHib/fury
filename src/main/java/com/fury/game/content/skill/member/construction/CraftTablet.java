package com.fury.game.content.skill.member.construction;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class CraftTablet extends Action {

    private SpellTablet tablet;
    private int amount = 1;

    public CraftTablet(SpellTablet tablet) {
        this.tablet = tablet;
    }

    @Override
    public boolean start(Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        return hasRequirements(player);
    }

    @Override
    public boolean process(Player player) {
        return hasRequirements(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if(tablet.getSpell().deleteRunes(player)) {
            player.animate(3645);
            player.getInventory().delete(new Item(1761));
            player.getInventory().addSafe(new Item(tablet.getItem()));
            player.getSkills().addExperience(Skill.MAGIC, tablet.getSpell().getExperience());
            return --amount > 0 ? 7 : 0;
        }
	    return -1;
    }

    @Override
    public void stop(Player player) {

    }

    public boolean hasRequirements(Player player) {
        if(!tablet.getSpell().hasRequiredLevel(player))
            return false;

        if(!player.getInventory().containsAmount(new Item(1761))) {
            player.message("You need a soft clay to craft a tablet.");
            return false;
        }

        if (!tablet.getSpell().canCast(player))
            return false;

        return true;
    }
}
