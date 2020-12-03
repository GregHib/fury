package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutCantBuyD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        sendNpc(quest.sage, "Now is no time to be on a shopping spree,", "sell that ring and let's get out of here.");
    }

    @Override
    public void run(int optionId) {
        player.getMovement().unlock();
        end();
    }
}
