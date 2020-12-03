package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Jon on 2/9/2017.
 */
public class BuildD extends Dialogue {
    @Override
    public void start() {
    }

    @Override
    public void run(int optionId) {
        if (optionId == 55) { //close
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        player.getHouse().build(optionId - 38333); //this one calls close interface
    }
}
