package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.slayer.SlayerMaster;

public class QuickTaskD extends Dialogue {
    private int npcId;
    private SlayerMaster master;
    private Revision revision;

    @Override
    public void start() {
        master = (SlayerMaster) parameters[0];
        revision = master == SlayerMaster.DEATH || master == SlayerMaster.RAPTOR ? Revision.PRE_RS3 : Revision.RS2;
        npcId = master.getNpcId();
        if (player.getSlayerManager().getCurrentTask() != null) {
            if (master == SlayerMaster.TURAEL && player.getSlayerManager().getCurrentMaster() != SlayerMaster.TURAEL) {
                //skip
            } else {
                player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "You're still hunting " + player.getSlayerManager().getCurrentTask().getName(), " come back when you've finished your task.");
                return;
            }
        }
        if (master == SlayerMaster.DEATHWILD) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Easy", "Hard", "Nothing, Nevermind.");
            stage = 0;
        } else
        if (player.getSkills().getCombatLevel() < master.getRequiredCombatLevel())
            player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your too weak overall,", "come back when you have " + master.getRequiredCombatLevel() + " combat.");
        else if (player.getSkills().getLevel(Skill.SLAYER) < master.getRequiredSlayerLevel()) {
            player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your Slayer level is too low to take on my challenges,", "come back when you have a level of", "at least " + master.getRequiredSlayerLevel() + " slayer.");
        } else {
            if (master == SlayerMaster.TURAEL && player.getSlayerManager().getCurrentTask() != null)
                player.getSlayerManager().skipCurrentTask(true);
            player.getSlayerManager().setCurrentTask(true, master);
            player.getDialogueManager().sendNPCDialogue(npcId, revision, Expressions.PLAIN_TALKING, "Your new assignment is: " + player.getSlayerManager().getCurrentTask().getName(), " only " + player.getSlayerManager().getCount() + " more to go.");
        }
    }

    @Override
    public void run(int optionId) {
        if (stage == 0) {
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
        } else {
            end();
        }

    }

}