package com.fury.game.content.dialogue.impl.skills.construction;

import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.EnterFriendToVisit;
import com.fury.game.content.skill.member.construction.House;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Jon on 2/9/2017.
 */
public class EnterHouseD extends Dialogue {
    @Override
    public void start() {
        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Go to your house.", "Go to your house (building mode).", "Go to a friend's house.", "Never mind.");
    }

    @Override
    public void run(int optionId) {
        if (optionId == DialogueManager.OPTION_1 || optionId == DialogueManager.OPTION_2) {
            end();
            enterHouse(player, optionId == DialogueManager.OPTION_2);
        } else if (optionId == DialogueManager.OPTION_3) {
            end();
            enterFriendsHouse(player);
        }
    }

    public static void enterHouse(Player player, boolean buildMode) {
        player.getHouse().setBuildMode(buildMode);
        House.enterHouse(player);
    }

    public static void enterFriendsHouse(Player player) {
        player.getTemporaryAttributes().put("enterhouse", Boolean.TRUE);
        player.setInputHandling(new EnterFriendToVisit());
        player.getPacketSender().sendEnterInputPrompt("Enter the name of the house you'd like to enter:");
    }
}
