package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 26/11/2016.
 */
public class ItemRepairVoidKnightD extends Dialogue {
    @Override
    public void start() {
        int npc = (int) parameters[0];
        player.getDialogueManager().sendNPCDialogue(npc, Expressions.NORMAL, "I can repair any broken or degraded equipment for a fee.", "Use an item on me and I'll fix it for you.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

}