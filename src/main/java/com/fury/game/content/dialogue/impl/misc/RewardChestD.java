package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Handles the Queen Black Dragon reward chest dialogue.
 * @author Emperor
 *
 */
public class RewardChestD extends Dialogue {
    private QueenBlackDragon npc;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void start() {
        npc = (QueenBlackDragon) parameters[0];
        player.getDialogueManager().sendDialogue("This strange device is covered in indecipherable script. It opens for you,", "giving you only a small sample of the objects it contains.");
    }

    @Override
    public void run(int optionId) {
        npc.claimRewards(player);
        end();
    }
}