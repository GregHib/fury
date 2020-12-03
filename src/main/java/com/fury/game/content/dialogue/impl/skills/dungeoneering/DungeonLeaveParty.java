package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class DungeonLeaveParty extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Abandon Dungeon", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            if (player.getTimers().getClickDelay().elapsed(1000)) {
                player.getTimers().getClickDelay().reset();
                player.getDungManager().leaveParty();
            }
        }
    }
}