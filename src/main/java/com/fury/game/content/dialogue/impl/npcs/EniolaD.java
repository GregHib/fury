package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class EniolaD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(6362, Expressions.NORMAL, "You'll need to pay 20 runes to access your bank account,", "What type of runes would you like to use?");
    }

    @Override
    public void run(int optionId) {
        player.getDialogueManager().startDialogue(new ZMIBankerD());
    }
}
