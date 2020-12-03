package com.fury.game.content.global.dnd.eviltree;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.FireStarter;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class LightKindling extends Action {
    private EvilTree evilTree = EvilTree.get();
    private EvilTreeFire fire;

    @Override
    public boolean start(Player player) {
        if (!player.getInventory().contains(EvilTree.KINDLING))
            return false;

        if (!player.getInventory().contains(new Item(FireStarter.TINDERBOX.getId()))) {
            player.message("You need a tinderbox in order to light a fire.");
            return false;
        }

        if (!player.getSkills().hasRequirement(Skill.FIREMAKING, evilTree.getType().getRequiredLevel(), "set fire to this evil tree"))
            return false;

        fire = evilTree.getFire(player);

        if (fire == null)
            return false;

        if (fire.isAlight()) {
            player.message("That part of the tree is already on fire!");
            return false;
        }

        player.message("You crouch to light the kindling");
        return true;
    }

    @Override
    public boolean process(Player player) {
        player.animate(9561);
        return evilTree.getHealth() > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        if (Misc.random(evilTree.getType().ordinal() + 1) == 0) {
            player.getSkills().addExperience(Skill.FIREMAKING, evilTree.getType().getBurn());
            player.getInventory().delete(EvilTree.KINDLING);
            fire.spawn(player);
            evilTree.performAnimation();
            return -1;
        } else
            return 3;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }
}
