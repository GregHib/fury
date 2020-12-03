package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;

/**
 * Created by Greg on 23/11/2016.
 */
public class DungeonExit extends Dialogue {
    private DungeonController dungeon;

    @Override
    public void start() {
        dungeon = (DungeonController) parameters[0];
        player.getDialogueManager().sendDialogue("This ladder leads back to the surface. You will not be able", "to come back to this dungeon if you leave.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Leave the dungeon and return to the surface?", "Yes.", "No.");
            stage = 0;
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1)
                dungeon.leaveDungeon(player);
            end();
        }
    }
}