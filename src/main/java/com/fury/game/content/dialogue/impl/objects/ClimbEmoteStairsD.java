package com.fury.game.content.dialogue.impl.objects;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.map.Position;

public class ClimbEmoteStairsD extends Dialogue {
    private Position upTile;
    private Position downTile;
    private int emoteId;

    @Override
    public void start() {
        upTile = (Position) parameters[0];
        downTile = (Position) parameters[1];
        emoteId = (Integer) parameters[4];
        player.getDialogueManager().sendOptionsDialogue("What would you like to do?", (String) parameters[2], (String) parameters[3], "Never mind.");
    }

    @Override
    public void run(int optionId) {
        if(optionId == DialogueManager.OPTION_1) {
            ObjectHandler.useStairs(player, emoteId, upTile, 2, 3);
        } else if(optionId == DialogueManager.OPTION_2) {
            ObjectHandler.useStairs(player, emoteId, downTile, 2, 2);
        }
        end();
    }
}