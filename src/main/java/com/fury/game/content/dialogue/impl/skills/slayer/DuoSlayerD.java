package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 04/12/2016.
 */
public class DuoSlayerD extends Dialogue {
    Player inviteOwner;
    @Override
    public void start() {
        String invite = (String) parameters[0];
        inviteOwner = (Player) parameters[1];
        player.getDialogueManager().sendStatement(invite);
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Accept " + inviteOwner.getUsername() + "'s invitation",
                    "Decline " + inviteOwner.getUsername() + "'s invitation");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                inviteOwner.getSlayerManager().createSocialGroup(true);
                player.setCloseInterfacesEvent(null);
            }
            end();
        }
    }
}