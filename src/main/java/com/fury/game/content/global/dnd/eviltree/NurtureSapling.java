package com.fury.game.content.global.dnd.eviltree;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class NurtureSapling extends Action {
    private int start;
    private EvilTree evilTree = EvilTree.get();

    public NurtureSapling() {
        this.start = evilTree.getStage();
    }

    @Override
    public boolean start(Player player) {
        if (!player.getSkills().hasRequirement(Skill.FARMING, evilTree.getType().getRequiredFarming(), "help this sapling grow"))
            return false;

        player.message("You begin tending to the sapling", true);
        return true;
    }

    @Override
    public boolean process(Player player) {
        if (!player.getSkills().hasRequirement(Skill.FARMING, evilTree.getType().getRequiredFarming(), "help this sapling grow"))
            return false;

        player.animate(3114);//9561, 3114 - neither correct but close
        return evilTree.getHealth() > 0 && start == evilTree.getStage();
    }

    @Override
    public int processWithDelay(Player player) {
        player.getSkills().addExperience(Skill.FARMING, evilTree.getType().getNurture());
        evilTree.depleteHealth(player, 1);
        return 3;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }
}
