package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.dnd.eviltree.EvilTree;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;

/**
 * Created by Greg on 03/12/2016.
 */
public class EvilTreeD extends Dialogue {
    @Override
    public void start() {
        if(EvilTree.get().isDead())
            player.getDialogueManager().sendDialogue("The taint of the evil tree is not currently on the land.");
        else {
            player.getDialogueManager().sendNPCDialogue(3636, Expressions.PLAIN_TALKING, "Would you like me to teleport you directly to the evil tree?");
            stage = 0;
        }
    }

        /*
        Or perhaps you are here to help dispatch the evil ree.
         */

        /*
        Travel.
        Evil tree.
        Nothing.
         */

    //Would you like me to teleport you directly there?

    /*
    Yes please.
    What is this 'evil tree'?
    I've changed my mind, I want to stay here.
     */
    @Override
    public void run(int optionId) {
        if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes please.", "No thanks");
            stage = 1;
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, EvilTree.get().getLocation().getPosition().copyPosition().add(-1, -1), TeleportType.SPIRIT_TREE);
            } else
                end();
        } else
            end();
    }
}