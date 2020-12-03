package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutBetterD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Feeling better?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendPlayerChat(Expressions.PLAIN_TALKING, "Yes, good as new thanks.");
            stage = 0;
        } else if(stage == 0) {
            sendNpc(quest.sage, "Great to hear, if you ever get close to death again,", "you can teleport home and heal using the nurse.");
            stage = 1;
        } else if(stage == 1) {
            sendNpc(quest.sage, "Speaking of teleporting, on your magic spell book tab", "there are several teleports all over the world.", "Let's give teleporting home a go.");
            stage = 2;
        } else if(stage == 2) {
            quest.setStage(FirstAdventureController.TELEPORT);
            player.getPacketSender().sendTab(GameSettings.MAGIC_TAB);
            player.getMovement().unlock();
            sendStatement("Click the blue circular 'H' button", "to teleport you to home.");
            stage = 3;
        }
    }
}
