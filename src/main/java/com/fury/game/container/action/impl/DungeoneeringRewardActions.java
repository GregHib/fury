package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class DungeoneeringRewardActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        DungeoneeringRewards.handleRewardsInterface(player, slot, id);
    }
}
