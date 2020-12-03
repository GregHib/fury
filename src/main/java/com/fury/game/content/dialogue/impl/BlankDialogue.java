package com.fury.game.content.dialogue.impl;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.Expressions;

/**
 * Created by Greg on 14/11/2016.
 */
public class BlankDialogue extends Dialogue {
    @Override
    public void start() {
        sendNpc(418, Expressions.PLAIN_TALKING, "");
    }

    @Override
    public void run(int optionId) {
        /*if(stage == -1) {
        } else if(stage == 0) {
        }
        if(optionId == OPTION_1) {
        } else if(optionId == OPTION_2) {
        } else if(optionId == OPTION_3) {
        } else if(optionId == OPTION_4) {
        } else if(optionId == OPTION_5) {
        }*/
    }
}