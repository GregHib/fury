package com.fury.game.content.dialogue.impl.items;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.impl.objects.PrayerBookD;
import com.fury.game.content.dialogue.impl.objects.SpellBookD;

public class ModPotatoD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue("Mod Potato", "Change prayer book", "Change spell book");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1) {
            player.getDialogueManager().startDialogue(new PrayerBookD(), false);
        } else if (optionId == DialogueManager.OPTION_2) {
            player.getDialogueManager().startDialogue(new SpellBookD(), false);
        } else
            end();
    }
}