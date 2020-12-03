package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.cache.Revision;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.SlayerMaster;
import com.fury.util.Misc;

public class SlayerMasterD extends Dialogue {

    private int npcId;
    private SlayerMaster master;
    private Revision revision;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        master = (SlayerMaster) parameters[1];
        revision = master == SlayerMaster.DEATH || master == SlayerMaster.RAPTOR ? Revision.PRE_RS3 : Revision.RS2;
        player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.NORMAL, "'Ello and what are you after then?");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (player.getSlayerManager().getCurrentMaster() != master || player.getSlayerManager().getCurrentTask() == null) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Please give me a task.", "What do you have in your shop?", "Nothing, Nevermind.");
                stage = 1;
            } else {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "How many monsters do I have left?", "What do you have in your shop?", "Give me a tip.", "Nothing, Nevermind.");
                stage = 0;
            }
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getSlayerManager().checkKillsLeft();
                end();
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Only the best slayer equipment money could buy.", "Come check it out.");
                stage = 5;
            } else if (optionId == DialogueManager.OPTION_3) {
                stage = 6;
                if (player.getSlayerManager().getCurrentTask() == null) {
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "You currently don't have a task.");
                    return;
                }
                String[] tips = player.getSlayerManager().getCurrentTask().getTips();
                if (tips != null && tips.length != 0) {
                    String chosen = tips[Misc.random(tips.length)];
                    if (chosen == null || chosen.equals(""))
                        player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "I don't have any tips for you currently.");
                    else
                        player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, chosen);
                } else
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "I don't have any tips for you currently.");
            } else
                end();
        } else if (stage == 1) {
            if (optionId == DialogueManager.OPTION_1) {
                if (player.getSlayerManager().getCurrentMaster() != master && master != SlayerMaster.TURAEL && player.getSlayerManager().getCurrentTask() != null) {
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "You're still hunting " + player.getSlayerManager().getCurrentTask().getName() + ";", "come back when you've finished your task.");
                } else if (master == SlayerMaster.DEATHWILD) {
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Easy", "Hard", "Nothing, Nevermind.");
                    stage = 9;
                } else if (player.getSkills().getCombatLevel() < master.getRequiredCombatLevel())
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your too weak overall,", "come back when you've become stronger.");
                else if (player.getSkills().getLevel(Skill.SLAYER) < master.getRequiredSlayerLevel()) {
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your Slayer level is too low to take on my challenges,", "come back when you have a level of", "at least " + master.getRequiredSlayerLevel() + " slayer.");
                } else {
                    if (master == SlayerMaster.TURAEL && player.getSlayerManager().getCurrentTask() != null)
                        player.getSlayerManager().skipCurrentTask(true);
                    player.getSlayerManager().setCurrentTask(true, master);
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your new assignment is: " + player.getSlayerManager().getCurrentTask().getName() + "; only " + player.getSlayerManager().getCount() + " more to go.");
                    stage = 6;
                }
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Only the best slayer equipment money could buy.", "Come check it out.");
                stage = 5;
            } else
                end();
        } else if (stage == 5) {
            end();
            ShopManager.getShops().get(40).open(player);
        } else if (stage == 8) {
            if (player.getSlayerManager().getCurrentTask() != null) {
                player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "You're still hunting " + player.getSlayerManager().getCurrentTask().getName(), " come back when you've finished your task.");
            }
        } else if (stage == 6) {
            end();
        } else if (stage == 9) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getSlayerManager().setDifficultyWildernessSlayer(0);
                player.getSlayerManager().setCurrentTask(true, master, "easy");
                player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your new assignment is: " + player.getSlayerManager().getCurrentTask().getName(), " only " + player.getSlayerManager().getCount() + " more to go.");
            } else if (optionId == DialogueManager.OPTION_2) {
                if (player.getSkills().getCombatLevel() < 70) {
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your too weak overall,", "come back when you have 70 combat.");
                } else {
                    player.getSlayerManager().setDifficultyWildernessSlayer(1);
                    player.getSlayerManager().setCurrentTask(true, master, "hard");
                    player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your new assignment is: " + player.getSlayerManager().getCurrentTask().getName(), " only " + player.getSlayerManager().getCount() + " more to go.");
                }
            } else {
                end();
            }
        }
    }
}