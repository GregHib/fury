package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.JewelryTeleporting;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 03/12/2016.
 */
public class AmuletOfGloryD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Edgeville", "Karamja", "Draynor Village", "Al kharid", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            JewelryTeleporting.teleport(player, new Position(3088, 3506));
        } else if(optionId == DialogueManager.OPTION_2) {
            JewelryTeleporting.teleport(player, new Position(2913, 3170));
        } else if(optionId == DialogueManager.OPTION_3) {
            JewelryTeleporting.teleport(player, new Position(3093, 3236));
        } else if(optionId == DialogueManager.OPTION_4) {
            JewelryTeleporting.teleport(player, new Position(3291, 3220));
        }
    }
}