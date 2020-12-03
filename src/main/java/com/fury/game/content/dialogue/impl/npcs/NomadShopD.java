package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class NomadShopD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(8528, Expressions.NORMAL, "You fought well.", "You have gained access to view my shop.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}