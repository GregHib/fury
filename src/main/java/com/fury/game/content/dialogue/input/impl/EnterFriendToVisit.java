package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.Input;
import com.fury.game.content.skill.member.construction.House;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.util.Misc;

/**
 * Created by Greg on 19/09/2016.
 */
public class EnterFriendToVisit extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        if(Misc.playerExists(syntax)) {
            if (player.getTemporaryAttributes().remove("enterhouse") != null)
                House.enterHouse(player, syntax);
        }
    }
}
