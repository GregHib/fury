package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.world.single.impl.Compensation;

public class ElfTrackerD extends Dialogue {
    @Override
    public void start() {
        if(Compensation.get().has(player.getLogger().getHardwareId())) {
            sendNpc(1199, "World looks much more 'elfy now you're playing it");
            stage = 0;
        } else
            sendNpc(1199, Expressions.LOOK_DOWN, "Eh the world wasn't very 'elfy as of recent", "please take these gifts as a reward for your patience");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendStatement("The elf hands you some items...");
            player.getInventory().addSafe(new Item(20712, 3), new Item(6199));
            Compensation.get().record(player.getLogger().getHardwareId(), 1);
            stage = 0;
        } else
            end();

    }
}
