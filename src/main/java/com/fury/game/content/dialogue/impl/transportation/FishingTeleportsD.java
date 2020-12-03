package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

/**
 * Created by Jon on 11/18/2016.
 */
public class FishingTeleportsD extends Dialogue {

    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Barbarian Village", "Entrana", "Fishing Guild", "Catherby", "Fishing Colony");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if (optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3097 + Misc.randomMinusOne(3), 3424 + Misc.randomMinusOne(3)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2875 + Misc.randomMinusOne(3), 3335 + Misc.randomMinusOne(3)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2592 + Misc.randomMinusOne(3), 3414 + Misc.randomMinusOne(3)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2826 + Misc.randomMinusOne(3), 3437 + Misc.randomMinusOne(3)), player.getSpellbook().getTeleportType());
                } else if (optionId == DialogueManager.OPTION_5) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2336 + Misc.randomMinusOne(3), 3696 + Misc.randomMinusOne(3)), player.getSpellbook().getTeleportType());
                }
                break;
        }
    }
}