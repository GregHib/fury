package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.game.content.skill.member.summoning.ScrollMaking;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class EnterAmountToTransform extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(player.getInterfaceId() != 64200) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		ScrollMaking.transformScrolls(player, amount);
	}

}
