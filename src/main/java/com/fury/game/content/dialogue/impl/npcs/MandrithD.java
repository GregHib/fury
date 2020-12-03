package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Artifacts;

/**
 * Created by Greg on 14/11/2016.
 */
public class MandrithD extends Dialogue {
    int mandrith = 6537;

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Sell Bounty Hunter Artifacts", "Show PvP Stats", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                Artifacts.sellArtifacts(player);
            } else if (optionId == DialogueManager.OPTION_2) {
                String KDR = "N/A";
                int kc = player.getPlayerKillingAttributes().getPlayerKills();
                int dc = player.getPlayerKillingAttributes().getPlayerDeaths();
                if (kc >= 5 && dc >= 5)
                    KDR = String.valueOf((double) (kc / dc));
                player.getDialogueManager().sendNPCDialogue(mandrith, Expressions.NORMAL, "You have killed " + player.getPlayerKillingAttributes().getPlayerKills() + " players. You have died " + player.getPlayerKillingAttributes().getPlayerDeaths() + " times.", "You currently have a killstreak of " + player.getPlayerKillingAttributes().getPlayerKillStreak() + " and your", "KDR is currently " + KDR + ".");
                stage = 0;
            } else
                end();
        } else
            end();
    }
}
