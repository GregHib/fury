package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;

/**
 * Created by Greg on 23/11/2016.
 */
public class DungeonClimbLadder extends Dialogue {
    private DungeonController dungeon;

    @Override
    public void start() {
        dungeon = (DungeonController) parameters[0];
        player.getDialogueManager().sendOptionsDialogue("Do you wish to proceed with your party?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1)
            dungeon.voteToMoveOn(player);
        end();
    }
}