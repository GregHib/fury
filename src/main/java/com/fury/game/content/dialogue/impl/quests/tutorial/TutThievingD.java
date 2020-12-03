package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutThievingD extends Dialogue {
    FirstAdventure quest;
    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Right I'm going to teach you how to be a thief.", "Remember stealing is bad, however is it one of ", "the fastest ways of getting money when starting.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "All you do is *grab* something", "and hope you don't get caught...");
            quest.sage.getDirection().face(quest.hint);
            quest.sage.animate(832);
            stage = 0;
        } else if(stage == 0) {
            quest.sage.getDirection().face(player);
            sendNpc(quest.sage, "Doesn't look like anyone is watching", "why don't you give it a try?");
            stage = 1;
        } else if(stage == 1) {
            player.getMovement().unlock();
            player.getPacketSender().sendEntityHint(quest.hint);
            quest.setStage(FirstAdventureController.STALL);
            sendStatement("Click on the stall to steal a gold ring.");
            stage = -2;
        } else
            end();
    }
}
