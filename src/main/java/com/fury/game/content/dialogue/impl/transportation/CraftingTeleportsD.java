package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/17/2016.
 */
public class CraftingTeleportsD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Al Kharid", "Flax Field", "Cancel");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3273, 3191), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2742, 3443), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                }
                break;
        }
    }
}