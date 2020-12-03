package com.fury.game.entity.character.player.content;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.util.Misc;

public class MemberScrolls {
	
	public static void checkForRankUpdate(Player player, DonorStatus before) {
		if(player.getRights().isStaff())
			return;

		if(before != DonorStatus.get(player)) {
			player.message("You've become a " + Misc.formatText(DonorStatus.get(player).toString().toLowerCase()) + "! Congratulations!");
			player.getPacketSender().sendRights();
		}
	}

	public static boolean handleScroll(Player player, Item item) {
		switch(item.getId()) {
		case 6758:
		case 608:
		case 607:
		case 14808:
			int funds = item.getId() == 6758 ? 10 : item.getId() == 608 ? 25 : item.getId() == 607 ? 50 : item.getId() == 14808 ? 150 : 0;
			DonorStatus before = DonorStatus.get(player);
			player.getInventory().delete(new Item(item, 1));
			player.getPoints().add(Points.DONATED, funds);
			player.getPoints().add(Points.DONOR, funds * 100);
			player.getPoints().add(Points.MEMBER, funds);
			player.message("Your account has gained funds worth $" + funds + ". Your total is now at $" + player.getPoints().get(Points.DONATED) + ".");
			checkForRankUpdate(player, before);
			PlayerPanel.refreshPanel(player);
			break;
		}
		return false;
	}
}
