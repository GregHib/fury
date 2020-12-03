package com.fury.game.content.dialogue.impl.skills.summoning;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.world.map.Position;

public class FamiliarInspectionD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue("Teleport to " + parameters[0] + "?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1)
            TeleportHandler.teleportPlayer(player, (Position) parameters[1], TeleportType.NORMAL);
        end();
    }
}