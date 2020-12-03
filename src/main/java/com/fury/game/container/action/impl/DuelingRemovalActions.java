package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToRemoveFromStake;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class DuelingRemovalActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        if(player.getDuelConfigurations() != null)
            player.getDuelConfigurations().removeStake(player, slot, 1);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        if(player.getDuelConfigurations() != null)
            player.getDuelConfigurations().removeStake(player, slot, 5);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        if(player.getDuelConfigurations() != null)
            player.getDuelConfigurations().removeStake(player, slot, 10);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        Item item = player.getInventory().get(slot);
        if (item == null)
            return;
        if(player.getDuelConfigurations() != null)
            player.getDuelConfigurations().removeStake(player, slot, player.getInventory().getAmount(item));
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if(player.getDuelConfigurations() != null) {
            player.setInputHandling(new EnterAmountToRemoveFromStake(id));
            player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
        }
    }
}
