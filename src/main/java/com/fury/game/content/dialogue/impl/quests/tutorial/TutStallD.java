package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutStallD extends Dialogue {
    FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        player.getPacketSender().sendTab(GameSettings.INVENTORY_TAB);
        sendNpc(quest.sage, "Great work!", "If you look in your inventory you'll see the ring", "You're going to be rich in no time!");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "Now we don't want to be caught with it on us, so", "let's go talk to the merchant about selling it.");
            stage = 0;
        } else if(stage == 0) {
            quest.setStage(FirstAdventureController.FOLLOW_MERCHANT);
            player.getMovement().unlock();
            quest.sage.forceChat("This way!");
            quest.sage.walkTo(3089, 3491);
            stage = 1;
        } else
            end();
    }
}
