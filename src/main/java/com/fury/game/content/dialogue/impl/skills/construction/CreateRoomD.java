package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.House;

/**
 * Created by Jon on 2/9/2017.
 */
public class CreateRoomD extends Dialogue {
    private House.RoomReference room;

    @Override
    public void start() {
        this.room = (House.RoomReference) parameters[0];
        sendPreview();
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_4) {
            end();
            return;
        }
        if (optionId == DialogueManager.OPTION_3) {
            end();
            player.getHouse().createRoom(room);
            return;
        }
        player.getHouse().previewRoom(room, true);
        room.setRotation((room.getRotation() + (optionId == DialogueManager.OPTION_1 ? 1 : -1)) & 0x3);
        sendPreview();
    }

    public void sendPreview() {
        player.getDialogueManager().sendOptionsDialogue("Select an Option", "Rotate clockwise", "Rotate anticlockwise.", "Build.", "Cancel");
        player.getHouse().previewRoom(room, false);
    }

    @Override
    public void finish() {
        player.getHouse().previewRoom(room, true);
    }

}
