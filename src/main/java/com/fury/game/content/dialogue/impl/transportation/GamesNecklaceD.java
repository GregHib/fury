package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.JewelryTeleporting;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;

/**
 * Created by Greg on 03/12/2016.
 */
public class GamesNecklaceD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Burthorpe Games Room", "Barbarian Outpost", "Clan Wars", "Wilderness Volcano (" + FontUtils.RED + "L-53" + FontUtils.COL_END + ")", "Nowhere");
    }

    @Override
    public void run(int optionId) {
        end();
        if(optionId == DialogueManager.OPTION_1) {
            JewelryTeleporting.teleport(player, new Position(2898, 3562));
        } else if(optionId == DialogueManager.OPTION_2) {
            JewelryTeleporting.teleport(player, new Position(2520, 3571));
        } else if(optionId == DialogueManager.OPTION_3) {
            JewelryTeleporting.teleport(player, new Position(2992, 9679));
        } else if(optionId == DialogueManager.OPTION_4) {
            JewelryTeleporting.teleport(player, new Position(3373, 3936));
        }
    }
}