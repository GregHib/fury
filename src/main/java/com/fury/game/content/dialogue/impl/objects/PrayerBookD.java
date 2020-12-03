package com.fury.game.content.dialogue.impl.objects;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.free.prayer.Prayerbook;
import com.fury.game.world.update.flag.block.Animation;

public class PrayerBookD extends Dialogue {
    boolean usingObject;

    @Override
    public void start() {
        usingObject = (boolean) parameters[0];
        player.getDialogueManager().sendOptionsDialogue("Change prayer book", "Normal", "Curses");
    }

    @Override
    public void run(int optionId) {
        player.getPrayer().closeAllPrayers();
        if(optionId == DialogueManager.OPTION_1) {
            player.setPrayerbook(Prayerbook.NORMAL);
        } else if(optionId == DialogueManager.OPTION_2) {
            player.setPrayerbook(Prayerbook.CURSES);
        }

        if(usingObject)
            player.animate(645);

        player.getPacketSender().sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
        end();
    }
}