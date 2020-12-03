package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.skill.free.crafting.enchanting.BoltEnchanting;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 22/02/2017.
 */
public class EnterAmountToEnchant extends EnterAmount {

    @Override
    public void handleAmount(Player player, int amount) {
        int bolt = (int) player.getTemporaryAttributes().get("bolt_enchant_selection");
        player.getTemporaryAttributes().remove("bolt_enchant_selection");

        BoltEnchanting.enchant(player, bolt, amount);
    }
}
