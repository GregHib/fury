package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;

public class WoodDryadD extends Dialogue {

    private int woodDryad = 4441;

    @Override
    public void start() {
        player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Hi, why do you have twigs growing out of you?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendNPCDialogue(woodDryad, Expressions.GOOFY_LAUGH, "Heehee, what a strange question, that's because I'm a", "Wood Dryad.");
            stage = 0;
        } else if(stage == 0) {
            end();
        }
    }
}