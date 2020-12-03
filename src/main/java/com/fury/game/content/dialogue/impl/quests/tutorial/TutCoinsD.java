package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.item.Item;

public class TutCoinsD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        boolean first = player.getInventory().getAmount(new Item(995)) == 5282;
        sendNpc(quest.sage, "Wow! You have made " + (first ? "your first" : "a") + " stack of coins.", "Congratulations you're moving up in the world!");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            sendNpc(quest.sage, "If you look to the left of the mini map just above the globe", "you'll see a blue button with coins on.", "This is your money pouch.");
            stage = 0;
        } else if (stage == 0) {
            sendNpc(quest.sage, "Your money pouch will keep your money safe", "even when you die in dangerous locations.", "Let's add your new found wealth to your money pouch.");
            stage = 1;
        } else if (stage == 1) {
            player.getMovement().unlock();
            sendStatement("Right click the coins in your inventory and select", "'Add-to-pouch' to safely store them in your money pouch.");
        } else
            end();
    }
}
