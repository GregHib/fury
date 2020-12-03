package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.global.minigames.impl.WarriorsGuild;
import com.fury.game.entity.character.player.link.transportation.JewelryTeleporting;
import com.fury.game.world.map.Position;

/**
 * Created by Greg on 03/12/2016.
 */
public class CombatBraceletD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Warrior's Guild", "Champions' Guild", "Monastery", "Ranging Guild", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            JewelryTeleporting.teleport(player, new Position(2855, 3543));
            player.getControllerManager().startController(new WarriorsGuild());
        } else if(optionId == DialogueManager.OPTION_2) {
            JewelryTeleporting.teleport(player, new Position(3191, 3361));
        } else if(optionId == DialogueManager.OPTION_3) {
            JewelryTeleporting.teleport(player, new Position(3052, 3499));
        } else if(optionId == DialogueManager.OPTION_4) {
            JewelryTeleporting.teleport(player, new Position(2659, 3437));
        }
    }
}