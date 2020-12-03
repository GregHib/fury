package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;

/**
 * Created by Jon on 11/17/2016.
 */
public class AgilityTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Gnome Stronghold Agility Course", "Barbarian Outpost Course", "Wilderness Course (" + FontUtils.RED + "50 Wilderness" + FontUtils.COL_END + ")", "Cancel");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1) {
            end();
            TeleportHandler.teleportPlayer(player, new Position(2475, 3439), player.getSpellbook().getTeleportType());
        } else if (optionId == DialogueManager.OPTION_2) {
            end();
            TeleportHandler.teleportPlayer(player, new Position(2552, 3562), player.getSpellbook().getTeleportType());
        } else if (optionId == DialogueManager.OPTION_3) {
            end();
            TeleportHandler.teleportPlayer(player, new Position(2998, 3914), player.getSpellbook().getTeleportType());
        } else {
            end();
        }
    }
}
