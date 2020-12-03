package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class DungeonDifficultyD extends Dialogue {
    @Override
    public void start() {
        int partySize = (int) parameters[0];
        String[] options = new String[partySize];
        for (int i = 0; i < partySize; i++)
            options[i] = "" + (i + 1);
        options[(partySize / 2)] += " (recommended)";
        player.getDialogueManager().sendOptionsDialogue("What difficulty of dungeon would you like?", options);
    }

    @Override
    public void run(int optionId) {
        player.getDungManager().setDificulty(optionId + 1);
        player.getDungManager().enterDungeon(false);
        end();
        /*if(stage == -1) {
        } else if(stage == 0) {
        }
        if(optionId == DialogueManager.OPTION_1) {
        } else if(optionId == DialogueManager.OPTION_2) {
        } else if(optionId == DialogueManager.OPTION_3) {
        } else if(optionId == DialogueManager.OPTION_4) {
        } else if(optionId == DialogueManager.OPTION_5) {
        }*/
    }
}