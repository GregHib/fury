package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.entity.object.GameObject;

/**
 * Created by Jon on 2/9/2017.
 */
public class ClimbHouseStairD extends Dialogue {
    private GameObject object;
    private House house;

    @Override
    public void start() {
        this.object = (GameObject) parameters[0];
        this.house = (House) parameters[1];
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Climb up.", "Climb down.", "Cancel");
    }

    @Override
    public void run(int optionId) {
        end();
        if (optionId != DialogueManager.OPTION_3 && house.containsPlayer(player)) //cuz player might have left with dialogue open
            house.climbStaircase(player, object, optionId == DialogueManager.OPTION_1, false);
    }
}