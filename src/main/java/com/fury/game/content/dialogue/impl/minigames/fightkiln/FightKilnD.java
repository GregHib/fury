package com.fury.game.content.dialogue.impl.minigames.fightkiln;

import com.fury.game.content.controller.impl.FightKiln;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Jon on 2/12/2017.
 */
public class FightKilnD extends Dialogue {
    @Override
    public void start() {
        player.getMovement().lock();
        player.getDialogueManager().sendDialogue("You journey directly to the Kiln.");
    }

    @Override
    public void run(int optionId) {
        end();
    }

    @Override
    public void finish() {
        player.getControllerManager().startController(new FightKiln(), 0);
    }

}
