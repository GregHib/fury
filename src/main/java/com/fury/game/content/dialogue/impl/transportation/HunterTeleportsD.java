package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.controller.impl.PuroPuro;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/17/2016.
 */
public class HunterTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Puro-Puro", "Feldip Hills", "Karamja", "Hunter Areas", "Cancel");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2589, 4319), TeleportType.PURO_PURO);
                    player.getControllerManager().startController(new PuroPuro());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2525, 2916), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2902, 2897), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Canifis", "Uzer", "Ourania", "Isafdar");
                    stage = 0;
                } else if (optionId == DialogueManager.OPTION_5) {
                    end();
                }
                break;
            case 0:
                if(optionId == DialogueManager.OPTION_1) {
                    TeleportHandler.teleportPlayer(player, new Position(3538, 3449), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    TeleportHandler.teleportPlayer(player, new Position(3407, 3090), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    TeleportHandler.teleportPlayer(player, new Position(2472, 3243), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    TeleportHandler.teleportPlayer(player, new Position(2254, 3254), player.getSpellbook().getTeleportType());
                }
                end();
                break;
        }
    }
}

