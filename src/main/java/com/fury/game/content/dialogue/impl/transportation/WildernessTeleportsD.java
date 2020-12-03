package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

/**
 * Created by Jon on 11/18/2016.
 */
public class WildernessTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Mage Bank", "West Dragons (" + FontUtils.RED + "L-10" + FontUtils.COL_END + ")", "Bounty Hunter", "East Dragons (" + FontUtils.RED + "L-23" + FontUtils.COL_END + ")", "Bandit Camp (" + FontUtils.RED + "L-23" + FontUtils.COL_END + ") (" + FontUtils.RED + "MULTI" + FontUtils.COL_END + ")");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2538, 4716), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2980 + Misc.getRandom(3), 3596 + Misc.getRandom(3)), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3187 + Misc.getRandom(2), 3690 + Misc.getRandom(2)), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3329 + Misc.getRandom(2), 3660 + Misc.getRandom(2), 0), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    int random = Misc.getRandom(4);
                    switch (random) {
                        case 0:
                            TeleportHandler.teleportPlayer(player, new Position(3035, 3701), player.getSpellbook().getTeleportType());
                            break;
                        case 1:
                            TeleportHandler.teleportPlayer(player, new Position(3036, 3694), player.getSpellbook().getTeleportType());
                            break;
                        case 2:
                            TeleportHandler.teleportPlayer(player, new Position(3045, 3697), player.getSpellbook().getTeleportType());
                            break;
                        case 3:
                            TeleportHandler.teleportPlayer(player, new Position(3043, 3691), player.getSpellbook().getTeleportType());
                            break;
                        case 4:
                            TeleportHandler.teleportPlayer(player, new Position(3037, 3687), player.getSpellbook().getTeleportType());
                            break;
                    }
                   }
                break;
        }
    }
}
