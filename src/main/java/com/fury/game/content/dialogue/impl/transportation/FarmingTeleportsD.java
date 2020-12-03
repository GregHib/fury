package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/17/2016.
 */
public class FarmingTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Ardougne", "Falador", "Catherby", "Canafis", "Next");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2663, 3375), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Trees", "Patches", "Back");
                    stage = 1;
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2808, 3464), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3603, 3528), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Taverley", "Grand tree", "Tree gnome village", "Brimhaven", "Return");
                    stage = 0;
                }
                break;
            case 0:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2933, 3438), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Fruit", "Trees", "Back");
                    stage = 2;
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2490, 3181), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2765, 3211), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    player.getDialogueManager().startDialogue(new FarmingTeleportsD());
                }
                break;
            case 1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3002, 3374), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3052, 3304), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    player.getDialogueManager().startDialogue(new FarmingTeleportsD());
                }
                break;
            case 2:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2473, 3446), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2439, 3415), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Taverley", "Grand tree", "Tree gnome village", "Brimhaven", "Return");
                    stage = 0;
                }
                break;
        }
    }
}
