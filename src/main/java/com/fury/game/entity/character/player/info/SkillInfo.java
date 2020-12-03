package com.fury.game.entity.character.player.info;

import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 17/04/2017.
 */
public class SkillInfo {

    private static final int widget = 20000;

    private static int[] buttons = {8654, 8657, 8660, 8663, 8666, 8669, 8672, 28177, 28180,
            8655, 8658, 8861, 8664, 8667, 8670, 12162, 28178,
            8656, 8659, 8662, 8665, 8668, 8671, 13928, 28179};

    public static boolean handleButtons(Player player, int buttonId) {
        for(int i = 0; i < buttons.length; i++) {
            if(buttonId == buttons[i]) {
                player.openInterface(widget);
                return true;
            }
        }
        return false;
    }
}
