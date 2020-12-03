package com.fury.game.content.dialogue.impl.skills.slayer;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.util.FontUtils;

public class SocialSlayerD extends Dialogue {
    @Override
    public void start() {
        if(player.getSlayerManager().getSocialPlayer() == null) {
            player.message(FontUtils.imageTags(535) + " Start social slayer by using your enchanted gem on another player.");
        } else {
            player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Leave slayer group", "Cancel");
        }
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1)
            player.getSlayerManager().resetSocialGroup(true);
        end();
    }

}

