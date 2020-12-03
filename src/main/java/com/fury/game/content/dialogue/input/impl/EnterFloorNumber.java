package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.dialogue.input.Input;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 19/09/2016.
 */
public class EnterFloorNumber extends Input {

    @Override
    public void handleAmount(Player player, int floor) {
        player.getPacketSender().sendInterfaceRemoval();

        if(floor > 0 && floor <= 60) {
            int lowestLevel = 121;
            for(Player member: player.getDungManager().getParty().getTeam()) {
                if(!member.getSkills().hasLevel(Skill.DUNGEONEERING, lowestLevel))
                    lowestLevel = member.getSkills().getMaxLevel(Skill.DUNGEONEERING);
            }
            if(((floor * 2) - 1) > lowestLevel) {
                if(player.getDungManager().getParty().getTeam().size() == 1) {
                    player.message("You do not have the dungeoneering level required to select this floor.");
                } else  {
                    player.message("A member of your party does not have the dungeoneering level required to complete this floor.");
                }
                return;
            }
            player.getDungManager().selectFloor(floor);
            player.getDungManager().refreshFloor();
        } else {
            player.message("Invalid floor number.");
        }
    }
}
