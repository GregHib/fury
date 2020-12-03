package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

/**
 * Created by Jon on 11/14/2016.
 */
public class CityTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Varrock", "Lumbridge", "Falador", "Camelot", "Next");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3211 + Misc.getRandom(2), 3432 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3221 + (Misc.getRandom(3)), 3218 + Misc.getRandom(1)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2964 + (Misc.getRandom(2)), 3378 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2756 + (Misc.getRandom(2)), 3477 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    stage = 0;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Ardougne", "Nardah", "Taverley", "Trollheim", "Next");
                }
                break;
            case 0:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2661 + Misc.getRandom(2), 3303 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3433 + Misc.getRandom(2), 2891 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2922, 3444), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2891, 3674), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    stage = 1;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Shilo Village", "Miscellania", "Yanille", "Mos Le'Harmless", "Next");
                }
                break;
            case 1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2852, 2960), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2534, 3864), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2603, 3090), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3676, 2983), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    stage = 2;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Lunar Isle", "", "", "", "Back");
                }
                break;
            case 2:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2111, 3915), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    //TeleportHandler.teleportPlayer(player, new Position(3655, 3349), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
//                    TeleportHandler.teleportPlayer(player, new Position(2603, 3090), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
//                    TeleportHandler.teleportPlayer(player, new Position(3676, 2983), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    player.getDialogueManager().startDialogue(new CityTeleportsD());
                }
                break;
        }
    }
}