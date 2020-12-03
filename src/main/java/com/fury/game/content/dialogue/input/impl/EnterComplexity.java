package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 19/09/2016.
 */
public class EnterComplexity extends Input {

    @Override
    public void handleAmount(Player player, int complexity) {
        player.getPacketSender().sendInterfaceRemoval();

        if(complexity > 0 && complexity <= 6) {
            player.getDungManager().selectComplexity(complexity);
            player.getDungManager().refreshComplexity();
        } else {
            player.message("Invalid complexity number.");
        }
    }
}
