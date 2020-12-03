package com.fury.game.content.dialogue.impl.items;

import com.fury.game.content.dialogue.Dialogue;

public class ItemMessageD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendItemDialogue((Integer) parameters[1], (String) parameters[0]);
    }

    @Override
    public void run(int optionId) {
        end();
    }
}