package com.fury.game.content.dialogue.impl.skills.agility;

import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.Achievements;
import com.fury.util.Misc;

/**
 * Created by Greg on 04/12/2016.
 */
public class CourseCompletionD extends Dialogue {
    @Override
    public void start() {
        int ran = Misc.random(2);
        String[] arr = ran == 0 ? new String[] {"Good job on completing the course!", "Take these tickets as reward."} :
                ran == 1 ? new String[] {"Keep going friend! You're doing great.", "Take these tickets as reward and motivation!"} :
                new String[] {"Moving through pipes and climbing trees is not easy!", "Take these tickets as reward!"};
        Achievements.doProgress(player, Achievements.AchievementData.COMPLETE_1000_AGILITY_COURSE_LAPS);
        player.getDialogueManager().sendNPCDialogue(437, Expressions.NORMAL, arr);
    }

    @Override
    public void run(int optionId) {
        end();
    }
}