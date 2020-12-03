/**
 * Credit: Greg
 */
package com.fury.game.content.dialogue.input.impl;

import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

public class BuyShards extends EnterAmount {

	@Override
	public void handleAmount(Player player, int amount) {
		if(amount > Integer.MAX_VALUE-1)
			amount = Integer.MAX_VALUE-1;
		player.getPacketSender().sendInterfaceRemoval();
		int price = Loader.getItem(12183).getValue();
		long cost = (long) price * (long) amount;
		boolean usePouch = player.getMoneyPouch().getTotal() >= cost;
		Item item = new Item(12183);
		Item coins = new Item(995);
		if((long) amount + (long) player.getInventory().getAmount(item) > Integer.MAX_VALUE-1) {
			player.message("You do not have enough inventory space to buy that amount.");
			return;
		}
		if((player.getInventory().getAmount(coins) < cost && !usePouch) || !usePouch) {
			player.message("You do not have enough money to buy that amount.");
			return;
		}
		if(usePouch) {
			player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - cost);
			player.getPacketSender().sendString(8135, ""+player.getMoneyPouch().getTotal());
		} else {
			player.getInventory().delete(new Item(coins, (int) cost));
		}
		player.getInventory().add(new Item(item, amount));
		player.message("You buy "+amount+" Spirit Shards for "+Misc.insertCommasToNumber(""+cost)+" coins.");
		
	}

}
