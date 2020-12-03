package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.network.packet.out.Interface;
import com.fury.game.network.packet.out.InterfaceString;

public class GuildMasterD extends Dialogue {
    private static final int guildMaster = 198;
    @Override
    public void start() {
        sendNpc(guildMaster, "You look like quite the knowledgeable adventurer", "Are you interested in giving up some of your skills?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendStatement("The guild master can reset any skill to level 1 again.", "Costs 1m per skill.", "Select a skill to reset...");
            stage = 0;
        } else if(stage == 0) {
            if(player.getEquipment().getSpaces() != player.getEquipment().capacity()) {
                sendStatement("You must un-equip all items before resetting any skills.");
                stage = -2;
            } else {
                end();
                player.send(new InterfaceString(38059, "Choose the stat you wish to be reset"));
                player.send(new Interface(38000));
                Object[] arr = new Object[3];
                arr[0] = "reset";
                player.setUsableObject(arr);
            }
        } else
            end();
    }
}
