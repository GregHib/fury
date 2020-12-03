package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.util.Misc;

public class ChangeGamemodeD extends Dialogue {
    @Override
    public void start() {
        if(player.getGameMode() == GameMode.REGULAR)
            player.getDialogueManager().sendNPCDialogue(6782, Expressions.PLAIN_TALKING, "You already are playing on the regular game-mode!");
        else {
            stage = 0;
            player.getDialogueManager().sendNPCDialogue(6782, Expressions.PLAIN_TALKING, "If I change your game mode to regular", "there is no going back.", "Are you sure you want to do this?");
        }
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            end();
        } else if(stage == 0) {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Yes I am sure.", "No I like being " + Misc.uppercaseFirst(player.getGameMode().name()));
            stage = 1;
        } else if(stage == 1) {
            if(optionId == DialogueManager.OPTION_1) {
                player.setGameMode(GameMode.REGULAR);
                PlayerPanel.refreshPanel(player);
                player.message("You gamemode was successfully changed to regular.");
            }
            end();
        }

    }
}