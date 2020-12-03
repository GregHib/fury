/**
 * Credit: Greg
 */
package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class SellShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > Integer.MAX_VALUE-1)
			amount = Integer.MAX_VALUE-1;
		player.getPacketSender().sendInterfaceRemoval();

		Item shard = new Item(12183);
		int shards = player.getInventory().getAmount(shard);
		if(amount > shards)
			amount = shards;
		if(amount == 0) {
			return;
		} else {
			long rew = (long) shard.getDefinitions().getValue() * (long) amount;
			if(rew + (long) player.getInventory().getAmount(new Item(995)) > Integer.MAX_VALUE) {
				player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() + rew);
				player.getPacketSender().sendString(8135, ""+player.getMoneyPouch().getTotal());
			} else {
				player.getInventory().add(new Item(995, (int) rew));
			}
			player.getInventory().delete(new Item(shard, amount));
			player.message("You sold " + amount + " Spirit Shards for " + Misc.insertCommasToNumber("" + rew) + " coins.");
		}
	}

}
