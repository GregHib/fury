package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.util.Misc;

/**
 * Created by Greg on 04/12/2016.
 */
public class EnchantedGemD extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (int) this.parameters[0];
        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "'Ello and what are you after then?");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            stage = 0;
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "How many monsters do I have left?", "Give me a tip.", "Nothing, Nevermind.");
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getSlayerManager().checkKillsLeft();
                end();
            } else if (optionId == DialogueManager.OPTION_2) {
                stage = 1;
                if (player.getSlayerManager().getCurrentTask() == null) {
                    player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "You currently don't have a task.");
                    return;
                }
                String[] tips = player.getSlayerManager().getCurrentTask().getTips();
                if (tips != null && tips.length != 0) {
                    String chosen = tips[Misc.random(tips.length)];
                    if (chosen == null || chosen.equals(""))
                        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "I don't have any tips for you currently.");
                    else
                        player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, chosen);
                } else
                    player.getDialogueManager().sendNPCDialogue(npcId, Expressions.PLAIN_TALKING, "I don't have any tips for you currently.");
            } else
                end();
        } else if (stage == 1) {
            end();
        }
    }
}