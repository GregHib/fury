package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;

/**
 * Created by Greg on 23/11/2016.
 */
public class SmugglerD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(DungeonConstants.SMUGGLER, Expressions.NORMAL, "Sorry, but I don't have anything to sell.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}
