package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutHitD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        sendNpc(quest.sage, Expressions.SCARED, "ARE YOU OKAY!?!", "What was that!? Quickly let's get you to the nurse.");
    }

    @Override
    public void run(int optionId) {
        player.getMovement().unlock();
        quest.setStage(FirstAdventureController.FOLLOW_NURSE);
        quest.sage.forceChat("Come quick!");
        quest.sage.setRun(true);
        quest.sage.walkTo(3090, 3505);
        end();
    }
}
