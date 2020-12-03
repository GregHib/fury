package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.misc.StartD;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.core.model.item.Item;

public class SkipTutorialD extends Dialogue {
    private int sage = 949;
    boolean newPlayer;
    @Override
    public void start() {
        newPlayer = (boolean) parameters[0];
        if(!newPlayer) {
            if(player.getQuestManager().hasFinished(Quests.FIRST_ADVENTURE)) {
                boolean lost = false;
                if(!player.hasItem(15596))
                    lost = player.getInventory().addSafe(new Item(15596));
                if(!player.hasItem(15597))
                    lost |= player.getInventory().addSafe(new Item(15597));
                if(!player.hasItem(15598))
                    lost |= player.getInventory().addSafe(new Item(15598));
                sendNpc(sage, lost ? "I found some items you lost, look after them this time." : "Hope you're getting along okay adventurer!");
                stage = -2;
            } else
                sendNpc(sage, "I have a quest explaining what " + GameSettings.NAME + " has to offer.", "Would you be interested with the promise of", "knowledge, items and wealth?");
        } else
            sendNpc(sage, "I have a quest suited for beginners just like yourself!", "Do you accept my challenge?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendOptions("I accept! (Rewards exp lamp & weapons)", "Thanks, but I already know what I'm doing.");
            stage = 0;
        } else if(stage == 0) {
            if(optionId == OPTION_1) {
                player.getQuestManager().start(new FirstAdventure());
            } else {
                end();

                if(newPlayer)
                    player.getDialogueManager().startDialogue(new StartD());
            }
        } else
            end();
    }
}