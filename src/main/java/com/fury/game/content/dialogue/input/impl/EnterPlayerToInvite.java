package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 19/09/2016.
 */
public class EnterPlayerToInvite extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        player.getPacketSender().sendInterfaceRemoval();
        player.getDungManager().invite(syntax);
    }
}
