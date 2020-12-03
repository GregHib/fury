package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.impl.misc.GamemodeD;
import com.fury.game.content.dialogue.impl.misc.ReferAFriendD;
import com.fury.game.content.dialogue.input.Input;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.util.Misc;

/**
 * Created by Greg on 19/09/2016.
 */
public class EnterFriendToRefer extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        if(Misc.playerExists(syntax)) {
            player.getReferAFriend().setReferral(syntax);
            PlayerLogs.log(player.getUsername(), "Just referred " + syntax);
            player.getDialogueManager().startDialogue(new GamemodeD());
        } else {
            player.getDialogueManager().startDialogue(new ReferAFriendD(), true);
        }
    }
}
