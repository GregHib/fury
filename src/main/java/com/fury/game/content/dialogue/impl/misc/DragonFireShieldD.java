package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 04/12/2016.
 */
public class DragonFireShieldD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(7959, Expressions.NORMAL, "The dragonfire shield is warn by only", "the mightiest of dragon slayers.", "I can make one for you if you would like?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            if (player.getInventory().contains(new Item(11286)) && player.getInventory().contains(new Item(1540)) && (player.getMoneyPouch().getTotal() >= 9400000 || player.getInventory().getAmount(new Item(995)) >= 9400000)) {
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes Please", "No, maybe later.");
                stage = 0;
            } else {
                stage = 1;
                player.getDialogueManager().sendNPCDialogue(7959, Expressions.NORMAL, "Bring me an anti-dragon shield and", "a draconic visage and I'll combine", "them together for only 9400k.");
                Achievements.finishAchievement(player, Achievements.AchievementData.TALK_TO_SMITHING_TUTOR);
            }
        } else if(stage == 0) {
            end();
            if(optionId == DialogueManager.OPTION_1) {
                if (player.getInventory().contains(new Item(11286)) && player.getInventory().contains(new Item(1540)) && (player.getMoneyPouch().getTotal() >= 9400000 || player.getInventory().getAmount(new Item(995)) >= 9400000)) {
                    if (player.getMoneyPouch().getTotal() >= 9400000) {
                        player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - 9400000);
                        player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal());

                    } else if (player.getInventory().getAmount(new Item(995)) >= 9400000) {
                        player.getInventory().delete(new Item(995, 9400000));
                    }
                    player.getInventory().delete(new Item(11286));
                    player.getInventory().delete(new Item(1540));
                    player.getInventory().addSafe(new Item(11283));
                }
            }
        } else
            end();
    }
}