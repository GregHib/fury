package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.House.RoomReference;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.content.skill.member.construction.HouseConstants.Room;

/**
 * Created by Jon on 2/8/2017.
 */
public class CreateOublietteD extends Dialogue {
    private RoomReference room;

    @Override
    public void start() {
        this.room = (RoomReference) parameters[0];
        player.getDialogueManager().sendOptionsDialogue("Do you want to build a oubliette at the bottom?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1) {
            int slot = room.getTrapdoorSlot();
            if (slot != -1) {
                RoomReference newRoom = new RoomReference(Room.OUTBLIETTE, room.getX(), room.getY(), 0, room.getRotation());
                newRoom.addObject(HouseConstants.Builds.LADDER, slot);
                player.getHouse().createRoom(newRoom);
            }
        }
        end();
    }
}
