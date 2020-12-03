package com.fury.game.content.dialogue.impl.skills.dungeoneering;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 06/12/2016.
 */
public class DungeoneeringTutorD extends Dialogue {
    private static final int DUNGEON_TUTOR = 9712;

    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Greetings, adventurer!");
        if (!player.hasItem(15707))
            stage = -1;
        else
            stage = 2;
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Before we carry on, let me give you this.");
            if (player.getInventory().getSpaces() >= 1)
                stage = 0;
            else
                stage = 1;
        } else if (stage == 0) {
            player.getDialogueManager().sendDialogue("He hands you a ring.");
            player.getInventory().add(new Item(15707));
            stage = 2;
        } else if (stage == 1) {
            player.getDialogueManager().sendDialogue("Your inventory is currently full!");
            stage = 2;
        } else if (stage == 2) {
            player.getDialogueManager().sendOptionsDialogue("Select an Option.", "What is this place?", "What can I do here?", "What does this ring do?");
            stage = 3;
        } else if (stage == 3) {
            if (optionId == DialogueManager.OPTION_1) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "What is this place?");
                stage = 4;
            } else if (optionId == DialogueManager.OPTION_2) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "What can I do here?");
                stage = 8;
            } else if (optionId == DialogueManager.OPTION_3) {
                player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "What does this ring do?");
                stage = 10;
            }
        } else if (stage == 4) {
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_DUNG_TUTOR);
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "This is a place of treasures, fierce battles and bitter defeats.");
            stage = 5;
        } else if (stage == 5) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "We fought our way into the dungeons beneath this place.");
            stage = 6;
        } else if (stage == 6) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Those of us who made it out alive...");
            stage = 7;
        } else if (stage == 7) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "...called this place Daemonhiem.");
            stage = 100;
        } else if (stage == 8) {
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_DUNG_TUTOR);
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Beneath these ruins you will find a multitude of dungeons,", "filled with strange creatures and resources.");
            stage = 9;
        } else if (stage == 9) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Unfortunately, due to the taint that permiates this place,", "we cannot risk you taking items in or out of Daemonhiem.");
            stage = 100;
        } else if (stage == 10) {
            Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_DUNG_TUTOR);
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "Raiding these foresaken dungeons can be alot more", "rewarding if you're fighting alongside friends and allies.", "It should be more fun and you gain experience faster.");
            stage = 11;
        } else if (stage == 11) {
            player.getDialogueManager().sendNPCDialogue(DUNGEON_TUTOR, Expressions.NORMAL, "The ring shows others you are interested", "in raiding a dungeon. It allowes you to form,", "join, and manage a raiding party.");
            stage = 100;
        } else if (stage == 100) {
            end();
        }
    }
}