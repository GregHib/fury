package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 07/02/2017.
 */
public class ArdougneCloakD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Monastery", "Watchtower");
    }

    @Override
    public void run(int optionId) {
        end();
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(2606, 3210), TeleportType.ARDOUGNE_CLOAK);
            } else if(optionId == DialogueManager.OPTION_2) {
                TeleportHandler.teleportPlayer(player, new Position(2548, 3114), TeleportType.ARDOUGNE_CLOAK);
            }
        }
    }
}