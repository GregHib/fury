package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.item.Item;

public class TutCowsD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "Cows are a great place to start training.");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendNpc(quest.sage, "Take these weapons and see which ones you prefer.");
            stage = 0;
        } else if(stage == 0) {
            sendStatement("The sage hands you a bunch of different weapons...");
            if(!player.hasItem(15596))
                player.getInventory().addSafe(new Item(15596), new Item(15598), new Item(15597), new Item(556, 30), new Item(558, 30));
            stage = 1;
        } else if(stage == 1) {
            sendNpc(quest.sage, "Why don't you try them out, go kill a cow!");
            stage = 2;
        } else if(stage == 2) {
            sendStatement("Click on a weapon to wield it.", "Click a cow to start attacking.", "(No combat experience will be given)");
            player.setExperienceLocked(true);
            player.setInvulnerable(true);
            player.getMovement().unlock();
            stage = 3;
        } else
            end();
    }
}
