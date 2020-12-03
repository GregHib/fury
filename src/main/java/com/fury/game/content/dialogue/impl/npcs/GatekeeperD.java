package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class GatekeeperD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(3321, Expressions.PLAIN_TALKING, "I am the Gatekeeper, protector of the prison.", "Any misbehaved players are sent here to do time.", "Enter the portal next to me to visit them.");
    }

    @Override
    public void run(int optionId) {
        end();
    }
}