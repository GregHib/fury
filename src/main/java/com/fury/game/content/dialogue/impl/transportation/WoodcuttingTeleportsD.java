package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/18/2016.
 */
public class WoodcuttingTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Draynor", "Seers' Village", "Tai Bwo Wannai Trio", "Isafdar", "Cancel");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3088, 3237), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2725, 3462), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2818, 3083), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2289, 3141), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    end();
                }
                break;
        }
    }
}
