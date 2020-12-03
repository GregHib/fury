package com.fury.game.content.skill.free.crafting.leather.tanning;

import com.fury.cache.def.Loader;
import com.fury.game.content.dialogue.input.impl.EnterAmountOfHidesToTan;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Colours;

public class Tanning {

	public static void selectionInterface(final Player player) {
		player.stopAll();
		player.getPacketSender().sendInterface(14670);
		for (final TanningData t : TanningData.values()) {
			player.getPacketSender().sendInterfaceModel(t.getItemFrame(), t.getLeather().getId(), t.getLeather().getRevision(), 250);
			player.getPacketSender().sendString(t.getNameFrame(), t.getName());
			if (player.getInventory().getAmount(new Item(995)) >= t.getPrice()) {
				player.getPacketSender().sendString(t.getCostFrame(), "Price: "+ t.getPrice(), Colours.GREEN);
			} else {
				player.getPacketSender().sendString(t.getCostFrame(), "Price: "+ t.getPrice(), Colours.RED);
			}
		}
	}

	public static void tanHide(final Player player, final int buttonId, int amount) {
		for (final TanningData t : TanningData.values()) {
			if (buttonId == t.getButtonId(buttonId)) {
				int invAmt = player.getInventory().getAmount(t.getHide());
				if(amount > invAmt)
					amount = invAmt;
				if(amount == 0) {
					player.message("You do not have any "+ Loader.getItem(t.getHide().getId()).getName()+" to tan.");
					return;
				}
				if (amount > t.getAmount(buttonId))
					amount = t.getAmount(buttonId);
				int price = (amount * t.getPrice());
				boolean usePouch = player.getMoneyPouch().getTotal() > price;
				int coins = usePouch ? player.getMoneyPouch().getAmountAsInt() : player.getInventory().getAmount(new Item(995));
				if (coins == 0) {
					player.message("You do not have enough coins to tan this hide.");
					return;
				}
				amount = (price / t.getPrice());
				final Item hide = t.getHide();
				final Item leather = t.getLeather();
				if (coins >= price) {
					if (player.getInventory().contains(hide)) {
						player.getInventory().delete(new Item(hide, amount));
						if(usePouch) {
							player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - price);
							player.getPacketSender().sendString(8135, ""+player.getMoneyPouch().getTotal()); //Update the money pouch
						} else
							player.getInventory().delete(new Item(995, price));
						if(t == TanningData.BLUE_DRAGON_LEATHER)
							Achievements.finishAchievement(player, Achievements.AchievementData.TAN_DRAGONHIDE);
						player.getInventory().add(new Item(leather, amount));
					} else {
						player.message("You do not have any hides to tan.");
						return;
					}
				} else {
					player.message("You do not have enough coins to tan this hide.");
					return;
				}
			}
		}
	}
	
	public static boolean handleButton(Player player, int id) {
		for (TanningData t : TanningData.values()) {
			if (id == t.getButtonId(id)) {
				if(t.getAmount(id) == 29) {
					player.setInputHandling(new EnterAmountOfHidesToTan(id));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to tan?");
					return true;
				}
				Tanning.tanHide(player, id, player.getInventory().getAmount(t.getHide()));
				return true;
			}
		}
		return false;
	}
}
