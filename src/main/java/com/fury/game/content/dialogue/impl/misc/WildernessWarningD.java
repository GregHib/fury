package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 03/12/2016.
 */
public class WildernessWarningD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendDialogue("Warning! This teleport leads to a deep", "Wilderness level.", "You cannot teleport above level 20 Wilderness!", "Are you sure you wish to continue anyway?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            player.getDialogueManager().sendOptionsDialogue("Continue", "Cancel");
        } else if(stage == 0) {
            if(optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(2961, 3882), player.getSpellbook().getTeleportType());
            }
        }
    }
}