package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

public class SimpleNpcMessageD extends Dialogue {
    @Override
    public void start() {
        int npcId = (Integer) parameters[0];
        String[] messages = new String[parameters.length - 1];
        for (int i = 0; i < messages.length; i++)
            messages[i] = (String) parameters[i + 1];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.NORMAL, messages);
    }

    @Override
    public void run(int optionId) {
        end();
    }
}
