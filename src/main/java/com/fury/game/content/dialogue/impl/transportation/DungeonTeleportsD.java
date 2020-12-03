package com.fury.game.content.dialogue.impl.transportation;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

/**
 * Created by Jon on 11/18/2016.
 */
public class DungeonTeleportsD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Polypore dungeon", "Ancient Cavern", "Living Rock Caverns", "Jadinko Lair", "Next");
    }

    @Override
    public void run(int optionId) {
        switch (stage) {
            case -1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(4620, 5458, 3), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(1745, 5325), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3651, 5122), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2948, 2955), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 0;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Smoke dungeon", "Brine rat cavern", "Slayer Tower", "Lumbridge swamp caves", "Next");
                }
                break;
            case 0:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3207, 9379), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2721, 10134), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3427, 3537), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3224, 9601), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 1;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Fremennik Slayer Dungeon", "Chaos Tunnels", "Lighthouse", "Taverley Dungeon", "Next");
                }
                break;
            case 1:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2805, 10002), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3183, 5470), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2515, 10008), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2884, 9799), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    stage = 2;
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Asgarnia Ice Dungeon", "Stronghold of Security", "Brimhaven Dungeon", "", "Back");
                }
                break;
            case 2:
                if(optionId == DialogueManager.OPTION_1) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(3042, 9582), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_2) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(1859, 5243), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_3) {
                    end();
                    TeleportHandler.teleportPlayer(player, new Position(2713, 9564), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_4) {
                    end();
//                    TeleportHandler.teleportPlayer(player, new Position(2884, 9799), player.getSpellbook().getTeleportType());
                } else if(optionId == DialogueManager.OPTION_5) {
                    player.getDialogueManager().startDialogue(new DungeonTeleportsD());
                }
                break;
        }
    }
}
