package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 17/04/2017.
 */
public class SkillTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Agility", "Thieving", "Dungeoneering", "Runecrafting", "Next");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new AgilityTeleportsD());
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().startDialogue(new ThievingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_3) {
                end();
                TeleportHandler.teleportPlayer(player, new Position(3450, 3715), TeleportType.KINSHIP_TELE);
            } else if(optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().startDialogue(new RunecraftingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Farming", "Fishing", "Cooking", "Crafting", "Next");
                stage = 0;
            }
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new FarmingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().startDialogue(new FishingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().startDialogue(new CookingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_4) {
                player.getDialogueManager().startDialogue(new CraftingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Mining", "Smithing", "Woodcutting", "Firemaking", "Next");
                stage = 1;
            }
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new MiningTeleportsD());
            } else if(optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().startDialogue(new SmithingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().startDialogue(new WoodcuttingTeleportsD());
            } else if(optionId == DialogueManager.OPTION_4) {
                TeleportHandler.teleportPlayer(player, new Position(2710, 3438), TeleportType.KINSHIP_TELE);
                end();
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Hunter", "Summoning", "", "", "Back");
                stage = 2;
            }
        } else if(stage == 2) {
            if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().startDialogue(new HunterTeleportsD());
            } else if(optionId == DialogueManager.OPTION_2) {
                end();
                TeleportHandler.teleportPlayer(player, new Position(2209, 5348), player.getSpellbook().getTeleportType());
            } else if(optionId == DialogueManager.OPTION_3) {
            } else if(optionId == DialogueManager.OPTION_4) {
            } else if(optionId == DialogueManager.OPTION_5) {
                player.getDialogueManager().startDialogue(new SkillTeleportsD());
            }
        }

    }
}