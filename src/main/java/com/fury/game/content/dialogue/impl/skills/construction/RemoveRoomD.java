package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.House.RoomReference;
import com.fury.util.Misc;

/**
 * Created by Jon on 8/2/2017.
 */
public class RemoveRoomD extends Dialogue {
    private RoomReference room;

    @Override
    public void start() {
        this.room = (RoomReference) parameters[0];
        player.getDialogueManager().sendOptionsDialogue("Remove the " + Misc.formatPlayerNameForDisplay(room.getRoom().toString()) + "?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1)
            player.getHouse().removeRoom(room);
        end();
    }
}