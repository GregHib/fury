package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.impl.skills.summoning.InfuseD;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class SumonningInfusionActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        Infusion.handleInfusionInterface(player, slot, id, 1);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        Infusion.handleInfusionInterface(player, slot, id, 5);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        Infusion.handleInfusionInterface(player, slot, id, 10);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        Infusion.handleInfusionInterface(player, slot, id, 28);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        player.getDialogueManager().startDialogue(new InfuseD(), id, slot);
    }
}
