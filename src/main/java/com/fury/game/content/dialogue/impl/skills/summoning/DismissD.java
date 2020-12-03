package com.fury.game.content.dialogue.impl.skills.summoning;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Achievements;

/**
 * Created by Greg on 15/11/2016.
 */
public class DismissD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, player.getPet() != null ? "Free pet" : "Dismiss Familiar", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            if(player.getFamiliar() != null) {
                player.getFamiliar().sendDeath(player);
                Achievements.finishAchievement(player, Achievements.AchievementData.DISMISS_A_FAMILIAR);
                end();
            } else if(player.getPet() != null) {
                stage = 0;
                player.getDialogueManager().sendPlayerDialogue(Expressions.SAD, "Run along; I'm setting you free.");
            }
        } else if(stage == 0 && player.getPet() != null) {
            player.getPetManager().setNpcId(-1);
            player.getPetManager().setItemId(-1);
            player.getPetManager().removeDetails(player.getPet().getItemId());
            player.getPet().switchOrb(false);
            player.getPacketSender().sendTabInterface(GameSettings.SUMMONING_TAB, -1);
            player.getPacketSender().sendTab(GameSettings.INVENTORY_TAB);
            player.getPet().deregister();
            player.setPet(null);
            player.message("Your pet runs off until it's out of sight.");
            end();
        }
    }
}