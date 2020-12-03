package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/17/2016.
 */
public class SmithingTeleportsD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Varrock", "Shilo village", "Ghost town", "Falador", "Cancel");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                end();
                if (optionId == DialogueManager.OPTION_1) {
                    TeleportHandler.teleportPlayer(player, new Position(3186, 3425), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    TeleportHandler.teleportPlayer(player, new Position(2856, 2964), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    TeleportHandler.teleportPlayer(player, new Position(3684, 3479), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    TeleportHandler.teleportPlayer(player, new Position(2973, 3369), player.getSpellbook().getTeleportType());
                }
                break;
        }
    }
}