package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;

/**
 * Created by Greg on 09/02/2017.
 */
public class BuyDungeoneeringExp extends EnterAmount {
    @Override
    public void handleAmount(Player player, int amount) {
        DungeoneeringRewards reward = DungeoneeringRewards.DUNGEONEERING_EXPERIENCE;
        if(player.getDungManager().getTokens() < amount * reward.getCost()) {
            player.message("You do not have enough tokens to buy this many");
            return;
        }
        //Charge
        player.getDungManager().addTokens(-(reward.getCost() * amount));

        //Give Reward
        player.getSkills().addExperience(Skill.DUNGEONEERING, 10 * amount, true);

        //Refresh
        DungeoneeringRewards.refreshTokens(player);
        player.getTimers().getClickDelay().reset();
    }

}