package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class StartTutorialD extends Dialogue {

    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE);

        //Start
        player.animate(866);
        player.graphic(312);
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Let me introduce myself...", "My name is The Fury Sage, I've lived", "around these parts for hundreds of years.", "Now it's your turn to learn the ways adventurer...");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "Right let's get started", "Follow me!");
            stage = 0;
        } else if(stage == 0) {
            quest.setStage(FirstAdventureController.FOLLOW);
            player.getMovement().unlock();
            quest.sage.forceChat("Follow me!");
            quest.sage.walkTo(3089, 3498);
        }
    }
}