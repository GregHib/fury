package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

public class CentaurD extends Dialogue {

    private int npc;

    @Override
    public void start() {
        npc = (int) parameters[0];
        if(npc == 4439)
            player.getDialogueManager().sendNPCDialogue(npc, Expressions.PLAIN_TALKING, "What a funny creature you are! Why, you only have", "2 legs!");
        else
            player.getDialogueManager().sendNPCDialogue(npc, Expressions.PLAIN_TALKING, "Hello human, welcome to our valley.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

}