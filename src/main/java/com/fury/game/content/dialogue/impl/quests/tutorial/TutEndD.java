package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutEndD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "I think I've covered all of the basics now.", "It is time for you to journey off on your own.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "I wish you all the best on your travels adventurer.");
            stage = 0;
        } else if(stage == 0) {
            sendStatement("Thank you for choosing " + GameSettings.NAME + ", we hope you enjoy your stay.");
            stage = 1;
        } else {
            end();
            player.getMovement().unlock();
            quest.setStage(FirstAdventureController.COMPLETE);
            quest.finish(player);
        }
    }
}
