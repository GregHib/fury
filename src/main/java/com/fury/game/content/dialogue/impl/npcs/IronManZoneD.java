package com.fury.game.content.dialogue.impl.npcs;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.world.map.Position;

public class IronManZoneD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendNPCDialogue(792, Expressions.PLAIN_TALKING, player.getGameMode().isIronMan() ? "Shh... follow me" : "You're no Ironman! I only help the self made.");
    }

    @Override
    public void run(int optionId) {
        if(player.getGameMode().isIronMan())
            TeleportHandler.teleportPlayer(player, new Position(3038, 5346));
        else
            end();
    }
}