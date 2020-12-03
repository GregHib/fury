package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.minigames.impl.WarriorsGuild;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

/**
 * Created by Greg on 14/11/2016.
 */
public class MinigameTeleportD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Barrows", "Pest Control", "Warriors Guild", "Fight Caves", "Next page");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(3565, 3308), player.getSpellbook().getTeleportType());
            } else if (optionId == DialogueManager.OPTION_2) {
                TeleportHandler.teleportPlayer(player, new Position(2659, 2659), player.getSpellbook().getTeleportType());
            } else if (optionId == DialogueManager.OPTION_3) {
                if (WarriorsGuild.hasRequirements(player)) {
                    TeleportHandler.teleportPlayer(player, new Position(2855, 3543), player.getSpellbook().getTeleportType());
                    player.getControllerManager().startController(new WarriorsGuild());
                } else
                    end();
            } else if (optionId == DialogueManager.OPTION_4) {
                TeleportHandler.teleportPlayer(player, new Position(2439 + Misc.getRandom(2), 5171 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
            }
            if (optionId == DialogueManager.OPTION_5) {
                stage = 0;
                player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Clan Wars", "", "", "", "Previous page");
            } else {
                end();
            }
        } else if (stage == 0) {
            if (optionId == DialogueManager.OPTION_1) {
                TeleportHandler.teleportPlayer(player, new Position(2996 + Misc.getRandom(2), 9678 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
            } else if (optionId == DialogueManager.OPTION_2) {
            } else if (optionId == DialogueManager.OPTION_3) {
            } else if (optionId == DialogueManager.OPTION_4) {
            }

            if (optionId == DialogueManager.OPTION_5) {
                stage = -1;
                player.getDialogueManager().startDialogue(new MinigameTeleportD());
            } else {
                end();
            }
        }
    }
}
