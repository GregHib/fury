package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

/**
 * Created by Greg on 03/12/2016.
 */
public class WelcomeD extends Dialogue {
    private int sage = 949;

    @Override
    public void start() {
        sendNpc(sage, Expressions.NORMAL, "Welcome to " + GameSettings.NAME + " " + player.getUsername() + "!");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                sendNpc(sage, Expressions.NORMAL, "Before you start do you have a friend to refer?");
                stage = 0;
                break;
            case 0:
                sendOptions("Yes, a friend invited me", "No, I'm here to make friends!");
                stage = 1;
                break;
            case 1:
                if(optionId == DialogueManager.OPTION_1) {
                    player.getDialogueManager().startDialogue(new ReferAFriendD(), false);
                } else {
                    player.getDialogueManager().startDialogue(new GamemodeD());
                }
                break;
        }
    }
}