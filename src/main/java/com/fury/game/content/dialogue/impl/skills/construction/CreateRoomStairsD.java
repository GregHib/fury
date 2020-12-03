package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.House.RoomReference;
import com.fury.game.content.skill.member.construction.HouseConstants.Builds;
import com.fury.game.content.skill.member.construction.HouseConstants.Room;


/**
 * Created by Jon on 2/9/2017.
 */
public class CreateRoomStairsD extends Dialogue {
    private RoomReference room;
    private boolean up;
    private boolean dungeonEntrance;

    @Override
    public void start() {
        this.room = (RoomReference) parameters[0];
        up = (boolean) parameters[1];
        dungeonEntrance = (boolean) parameters[2];
        player.getDialogueManager().sendOptionsDialogue("Do you want to build a room at the " + (up ? "top" : "bottom") + "?", "Yes.", "No.");
    }

    @Override
    public void run(int optionId) {
        if (stage == -1) {
            if (optionId == DialogueManager.OPTION_1) {
                stage = 0;
                if (room.getPlane() == 1 && !up)
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Skill hall", "Quest hall", "Dungeon stairs room");
                else
                    player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Skill hall", "Quest hall");
                return;
            }
        } else {
            Room r = room.getPlane() == 1 && !up && optionId == DialogueManager.OPTION_3 ? Room.DUNGEON_STAIRS : optionId == DialogueManager.OPTION_2 ? up ? Room.HALL_QUEST_DOWN : Room.HALL_QUEST : up ? Room.HALL_SKILL_DOWN : Room.HALL_SKILL;
            Builds stair = (room.getPlane() == 1 && !up && optionId == DialogueManager.OPTION_3) ? Builds.STAIRCASE : optionId == DialogueManager.OPTION_2 ? up ? Builds.STAIRCASE_DOWN_1 : Builds.STAIRCASE_1 : up ? Builds.STAIRCASE_DOWN : Builds.STAIRCASE;
            RoomReference newRoom = new RoomReference(r, room.getX(), room.getY(), room.getPlane() + (up ? 1 : -1), room.getRotation());
            int slot = dungeonEntrance ? 0 : room.getStaircaseSlot();
            if (slot != -1) {
                newRoom.addObject(stair, slot);
                player.getHouse().createRoom(newRoom);
            }
        }
        end();
    }
}