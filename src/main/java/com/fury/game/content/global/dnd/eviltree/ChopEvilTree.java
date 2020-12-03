package com.fury.game.content.global.dnd.eviltree;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.woodcutting.Hatchet;
import com.fury.game.content.skill.free.woodcutting.Woodcutting;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Utils;

public class ChopEvilTree extends Action {
    private Hatchet hatchet;
    private EvilTree evilTree = EvilTree.get();
    private boolean roots;
    private GameObject object;

    public ChopEvilTree(GameObject object, boolean roots) {
        this.roots = roots;
        this.object = object;
    }

    private boolean checkAll(Player player) {
        hatchet = Woodcutting.getHatchet(player, false);
        if (hatchet == null) {
            player.message("You don't have a hatchet that you can use.");
            return false;
        }

        if(evilTree == null || evilTree.getType() == null) {
            return false;
        }

        if (!player.getSkills().hasRequirement(Skill.WOODCUTTING, evilTree.getType().getRequiredLevel(), "chop down " + (roots ? "these roots" : "this tree")))
            return false;

        if (!player.getInventory().contains(EvilTree.KINDLING) && player.getInventory().getSpaces() <= 0) {
            player.getInventory().full();
            return false;
        }
        return true;
    }

    private int getWoodcuttingDelay(Player player) {
        int summoningBonus = player.getFamiliar() != null && player.getFamiliar().getId() == 6808 ? 2 : 0;
        int wcTimer = ((evilTree.getType().ordinal() + 1) * (roots ? 20 : 15)) - (player.getSkills().getLevel(Skill.WOODCUTTING) + summoningBonus) - Utils.random(hatchet.getAxeTime());
        if (wcTimer < 5)
            wcTimer = 1 + Utils.random(4);
        wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
        return wcTimer;
    }

    @Override
    public boolean start(Player player) {
        if (!checkAll(player))
            return false;
        player.message("You swing your hatchet at the evil " + (roots ? "roots" : "tree") + ".");
        setActionDelay(player, getWoodcuttingDelay(player));
        return true;
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(hatchet.getEmoteId()));
        return evilTree.getHealth() > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        Woodcutting.addExperience(player, roots ?  evilTree.getType().getRoot() : evilTree.getType().getTree());
        if (roots) {
            if (((EvilRoot) object).cut()) {
                ((EvilRoot) object).kill(player);
                player.graphic(319);
                player.getInventory().addSafe(EvilTree.KINDLING);
                return -1;
            }
        } else
            evilTree.depleteHealth(player, 1);
        return getWoodcuttingDelay(player);
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

    public boolean isRoots() {
        return roots;
    }
}
