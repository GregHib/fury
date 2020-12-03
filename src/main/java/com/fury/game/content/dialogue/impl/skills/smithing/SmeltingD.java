package com.fury.game.content.dialogue.impl.skills.smithing;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.smithing.Smelting;
import com.fury.game.content.skill.free.smithing.SmeltingData;

/**
 * Created by Greg on 11/12/2016.
 */
public class SmeltingD extends Dialogue {

    @Override
    public void start() {
        for (int i = 0; i < 8; i++) {
            SmeltingData bar = SmeltingData.values()[i];
            player.getPacketSender().sendInterfaceModel(bar.getFrameId(), bar.getBar().getId(), bar.getBar().getRevision(), 150);
        }
        player.getPacketSender().sendChatboxInterface(2400);
    }

    @Override
    public void run(int optionId) {
        if(parameters.length > 0 && parameters[0] instanceof Integer) {
            int amount = (int) parameters[0];
            SmeltingData bar = SmeltingData.values()[optionId];
            if (bar == null) {
                end();
                return;
            }

            Smelting.smeltBar(player, bar.getBar(), amount);
        }
        end();
    }
}