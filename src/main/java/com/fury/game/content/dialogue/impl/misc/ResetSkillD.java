package com.fury.game.content.dialogue.impl.misc;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.skill.Skill;

public class ResetSkillD extends Dialogue {
    private Skill skill;
    @Override
    public void start() {
        skill = (Skill) parameters[0];
        sendNpc(198, "It will cost 1m to reset " + skill.getFormatName(), "Are you sure?");
    }

    @Override
    public void run(int optionId) {
        if(stage == -1) {
            sendOptions("Yes reset " + skill.getFormatName() + " to level 1.", "No I'm fine thanks");
            stage = 0;
        } else if(stage == 0) {
            end();
            if(optionId == OPTION_1) {
                if(player.getInventory().removeCoins(1000000, "reset this skill.")) {
                    player.getSkills().reset(skill);
                    player.getSkills().refresh(skill);
                    player.message("Your " + skill.getFormatName() + " level has been reset.");
                }
            }
        }
    }
}
