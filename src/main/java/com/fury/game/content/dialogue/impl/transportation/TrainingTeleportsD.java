package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;

/**
 * Created by Jon on 11/14/2016.
 */
public class TrainingTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Chickens", "Cows", "Chaos Druids", "Rock Crabs", "Next");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3235, 3296), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3257, 3267), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2933, 9848), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2678, 3716), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 0;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yaks", "Experiments", "Bandits", "Ghouls", "Next");
                }
                break;
            case 0:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2326, 3802), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3557, 9947), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3173, 2983), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3420, 3510), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 1;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Frost Dragons (" + FontUtils.RED + "L-47" + FontUtils.COL_END + ")", "Dust Devils", "Monkey Skeletons", "Monkey Guards", "Next");
                }
                break;
            case 1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2953, 3888), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3279, 2964), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2802, 9148), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2793, 2773), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 2;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Tzhaar", "Armoured Zombies", "Ogres", "", "Back");
                }
                break;
            case 2:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2480, 5175), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3085, 9672), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2495, 3096), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                } else if(optionId == DialogueManager.OPTION_5) {
                    player.getDialogueManager().startDialogue(new TrainingTeleportsD());
                }
                break;
        }
    }
}