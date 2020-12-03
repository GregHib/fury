package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.global.Scoreboards;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 04/12/2016.
 */
public class Scoreboard extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "View Live Scoreboard: Top Killstreaks", "View Live Scoreboard: Top Pkers (KDR)", "View Live Scoreboard: Top Total Exp", "View Live Scoreboard: Top Achiever", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            Scoreboards.open(player, Scoreboards.TOP_KILLSTREAKS);
        } else if(optionId == DialogueManager.OPTION_2) {
            Scoreboards.open(player, Scoreboards.TOP_PKERS);
        } else if(optionId == DialogueManager.OPTION_3) {
            Scoreboards.open(player, Scoreboards.TOP_TOTAL_EXP);
        } else if(optionId == DialogueManager.OPTION_4) {
            Scoreboards.open(player, Scoreboards.TOP_ACHIEVER);
        }
    }

}