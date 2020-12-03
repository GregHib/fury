package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class UriWisdomD extends Dialogue {
    @Override
    public void start() {
        int npcId = (int) parameters[0];
        String wisdom = (String) parameters[1];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.NORMAL, wisdom);
    }

    @Override
    public void run(int optionId) {
        end();
    }

}