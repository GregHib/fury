package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.events.christmas.ChristmasPartyCharacters;
import com.fury.core.model.item.Item;

public class SantaD extends Dialogue {

    private static int santa = 8540;

    @Override
    public void start() {
        if(player.getChristmasEventStage() == 3) {
            ShopManager.getShops().get(123).open(player);
        } else if(player.getChristmasEventStage() == 0) {
            player.getDialogueManager().sendNPCDialogue(santa, Expressions.SAD, "*sobs*");
        } else if(!player.hasItem(new Item(11949))) {
            player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "Looks like you lost your snow globe,", "luckily I have another one for you.");
            player.getInventory().addSafe(new Item(11949));
            stage = 100;
        } else if(player.getChristmasCharactersFound() == 0) {
            player.getDialogueManager().sendNPCDialogue(santa, Expressions.SAD, "I just want people to have fun");
        } else if(player.getChristmasCharactersFound() == ChristmasPartyCharacters.values().length) {
            player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "Thank you so much!");
            player.setChristmasEventStage(2);
        } else if(player.getChristmasCharactersFound() >= 1) {
            player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "I hear some people might be coming, yey!");
        }
    }

    @Override
    public void run(int optionId) {
        if(player.getChristmasEventStage() == 0) {
            if (stage == -1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What's up Santa?");
                stage = 0;
            } else if (stage == 0) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.SAD_TWO, "Nobody turned up to my party...");
                stage = 1;
            } else if (stage == 1) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.SAD, "... and everyone still expects me", "to deliver presents to them...");
                stage = 2;
            } else if (stage == 2) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.SAD, "How am I supposed to deliver", "Christmas cheer if I'm not happy?");
                stage = 3;
            } else if (stage == 3) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Don't worry I can help", "Sorry, I'm busy");
                stage = 4;
            } else if (stage == 4) {
                if (optionId == DialogueManager.OPTION_1) {
                    player.getDialogueManager().sendNPCDialogue(santa, Expressions.NORMAL, "Thanks, take this snow globe.", "Snow always changes peoples minds!");
                    player.getInventory().addSafe(new Item(11949));
                    player.message("There are " + ChristmasPartyCharacters.values().length + " npc's throughout the world who, if pelted will come to santa's party!");
                    player.setChristmasEventStage(1);
                    stage = 5;
                } else
                    end();
            } else if(stage == 5) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, ChristmasPartyCharacters.BARBARIAN.getClue());
                stage = 6;
            } else
                end();
        } else if(player.getChristmasEventStage() == 1) {
            if(stage == -1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Have you got any clue's to help me?");
                stage = 0;
            } else if(stage == 0) {
                for(ChristmasPartyCharacters character : ChristmasPartyCharacters.values()) {
                    if(!player.getChristmasCharacter(character.ordinal())) {
                        player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, character.getClue());
                        stage = 1;
                        return;
                    }
                }
            } else
                end();
        } else if(player.getChristmasEventStage() == 2) {
            if(stage == -1) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "Here take this outfit and have some fun!");
                player.getInventory().addSafe(new Item(14595));
//                player.getInventory().addSafe(new Item(14600));
                player.getInventory().addSafe(new Item(14602));
                player.getInventory().addSafe(new Item(14603));
//                player.getInventory().addSafe(new Item(14604));
                player.getInventory().addSafe(new Item(14605));
                player.setChristmasEventStage(3);
                stage = 0;
            } else if(stage == 0) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "In the spirit of Christmas any snowflakes you gather", "I'll trade from some more gifts!");
                stage = 1;
            } else if(stage == 1) {
                player.getDialogueManager().sendNPCDialogue(santa, Expressions.PLAIN_TALKING, "Ho ho ho!", "Happy Holidays!");
                stage = 2;
            } else
                end();
        } else
            end();
    }
}