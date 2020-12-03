package com.fury.game.content.dialogue.impl.objects;

import com.fury.game.GameSettings;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.game.content.combat.magic.MagicSpellBook;
import com.fury.game.entity.character.player.info.PlayerRights;

public class SpellBookD extends Dialogue {
    boolean usingObject;

    @Override
    public void start() {
        usingObject = (boolean) parameters[0];
        if(player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            player.getDialogueManager().sendOptionsDialogue("Change spell book", "Regular", "Ancient", "Lunar", "Dungeoneering");
        else
            player.getDialogueManager().sendOptionsDialogue("Change spell book", "Regular", "Ancient", "Lunar");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            player.setSpellBook(MagicSpellBook.NORMAL, !usingObject);
        } else if(optionId == DialogueManager.OPTION_2) {
            player.setSpellBook(MagicSpellBook.ANCIENT, !usingObject);
        } else if(optionId == DialogueManager.OPTION_3) {
            player.setSpellBook(MagicSpellBook.LUNAR, !usingObject);
        } else if(optionId == DialogueManager.OPTION_4 && player.getRights().isOrHigher(PlayerRights.DEVELOPER)) {
            player.setSpellBook(MagicSpellBook.DUNGEONEERING, !usingObject);
        }

        if(usingObject)
            player.animate(645);

        player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
        player.message("Your magic spell book has been changed.", true);
        Autocasting.resetAutoCast(player, true);
        end();
    }
}