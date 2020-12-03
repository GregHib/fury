package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.dnd.star.ShootingStar;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 03/12/2016.
 */
public class StarSpriteD extends Dialogue {
    private int sprite = 8091;

    @Override
    public void start() {
        if (player.getInventory().getAmount(new Item(13727)) >= 200) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.NORMAL, "Thank you for helping me out of here.");
            stage = 0;
        } else {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.NORMAL, "Hello, strange creature.");
            stage = 1;
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == 0) {
            end();
            ShootingStar.reward(player);
        } else if(stage == 1) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "What are you? Where did you come from?", "Hello, strange glowing creature.", "I'm not strange.");
            stage = 2;
        } else if(stage == 2) {
           if(optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "What are you? Where did you come from?");
                stage = 3;
           } else if(optionId == DialogueManager.OPTION_2) {
               player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "Hello, strange glowing creature.");
               stage = 6;
            } else if(optionId == DialogueManager.OPTION_3) {
               player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "I'm not strange.");
               stage = 7;
            }
        } else if(stage == 3) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "I'm a star sprite! I was in my star in the sky, when it", "lost control and crashed into the ground. With half my", "star sticking in the ground, I became stuck. Fortunately,", "I was mined out by the kind creatures");
            stage = 4;
        } else if(stage == 4) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "of your race.");
            stage = 5;
        } else if(stage == 5) {
            player.getDialogueManager().startDialogue(new StarSpriteQD());
        } else if(stage == 6) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.PLAIN_TALKING, "Isn't that funny? One of the things I find odd", "about you is that you DON'T glow.");
            stage = 8;
        } else if(stage == 7) {
            player.getDialogueManager().sendNPCDialogue(sprite, Expressions.GOOFY_LAUGH, "Hehe. If you say so.");
            stage = 9;
        } else
            end();
    }
}