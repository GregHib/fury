package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 12/11/2017.
 */
public class CookingTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Catherby", "Edgeville", "Rogues' Den", "Cancel");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                end();
                if (optionId == DialogueManager.OPTION_1) {
                    TeleportHandler.teleportPlayer(player, new Position(2814, 3436), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    TeleportHandler.teleportPlayer(player, new Position(3079, 3496), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    TeleportHandler.teleportPlayer(player, new Position(3046, 4970, 1), player.getSpellbook().getTeleportType());
                }
                break;
        }
    }
}
