package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.game.entity.character.player.link.transportation.FairyRingTeleport;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 26/10/2016.
 */
public class EnterFairyRingCode extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        player.getPacketSender().sendInterfaceRemoval();
        FairyRingTeleport.fairyPort(player, syntax);
    }
}
