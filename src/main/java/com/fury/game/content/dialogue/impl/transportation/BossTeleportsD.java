package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.controller.impl.GodWars;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.config.ConfigConstants;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/15/2016.
 */
public class BossTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Queen Black Dragon", "Bork", "Glacors", "Nex", "More");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(1186, 6499), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3144, 5545), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(4181, 5723), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2903, 5204), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 0;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Dagannoth Kings", "Tormented Demons", "Godwars Dungeon", "Corporeal Beast", "More");
                }
                break;
            case 0:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(1908, 4367), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2569, 5735), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    if(player.getConfig().get(ConfigConstants.GODWARS_ENTRANCE) != 1) {
                        player.message("You need to have tied a rope to the entrance to completely unlock this teleport.");
                        TeleportHandler.teleportPlayer(player, new Position(2891, 3674), player.getSpellbook().getTeleportType());
                    } else {
                        TeleportHandler.teleportPlayer(player, new Position(2882, 5311, 2), player.getSpellbook().getTeleportType());
                        player.getControllerManager().startController(new GodWars());
                    }
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2885, 4374), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 1;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Giant Mole", "Kalphite Queen", ""/*"Bandos Avatar"*/, "Return");
                }
                break;
            case 1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2998, 3375), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3507, 9493), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
//                    TeleportHandler.teleportPlayer(player, new Position(2891, 4767), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    player.getDialogueManager().startDialogue(new BossTeleportsD());
                }
                break;
        }
    }
}