package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;

public class TutMerchantD extends Dialogue {
    private FirstAdventure quest;
    private int merchant = 2292;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "This is the merchant...");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(merchant, "Hello!", "I buy and sell common items with the owners", "of these stalls. I only buy items from legitimate sellers.", "But I honestly can't tell the difference.");
            stage = 0;
        } else if(stage == 0) {
            quest.setStage(FirstAdventureController.MERCHANT_TRADING);
            player.getDialogueManager().startDialogue(new TutSellD(), quest);
        }
    }
}
