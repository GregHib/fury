package com.fury.game.content.dialogue.impl.skills.agility;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class CourseFailD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(437, Expressions.ANGRY, "Hey you! Yes, you!", "Finish the whole course next time!");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}