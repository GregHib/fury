package com.fury.game.content.skill.member.herblore;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.HashMap;

public class WeaponPoison {

	private enum Weapon {
		DRAGON_DAGGER(1215, new int[][] { { 5940, 5698 }, { 5937, 5680 } }),
		RUNE_DAGGER(1213, new int[][] { { 5940, 5696 }, { 5937, 5678 } }),
		ADAMANT_DAGGER(1211, new int[][] { { 5940, 5694 }, { 5937, 5676 } }),
		MITHRIL_DAGGER(1209, new int[][] { { 5940, 5692 }, { 5937, 5674 } }),
		BLACK_DAGGER(1217, new int[][] { { 5940, 5700 }, { 5937, 5682 } }),
		STEEL_DAGGER(1207, new int[][] { { 5940, 5690 }, { 5937, 5672 } }),
		IRON_DAGGER(1203, new int[][] { { 5940, 5686 }, { 5937, 5668 } }),
		BRONZE_DAGGER(1205, new int[][] { { 5940, 5688 }, { 5937, 5670 } });

        Weapon(int itemId, int[][] newItemId) {
			this.itemId = itemId;
			this.newItemId = newItemId;
		}

		public int getItemId() {
			return itemId;
		}

		private int itemId;
		private int[][] newItemId;
		public static HashMap<Integer, Weapon> weapon = new HashMap<Integer, Weapon>();
		public static Weapon forId(int id) {
			return weapon.get(id);
		}
		public int[][] getNewItemId() {
			return newItemId;
		}


		static {
			for (Weapon w : Weapon.values())
				weapon.put(w.getItemId(), w);
		}
	}

	public static void execute(final Player player, Item itemUse, Item useWith) {
		final Weapon weapon = Weapon.weapon.get(useWith);
		if (weapon != null) {
			for (int element[] : weapon.getNewItemId())
				if (itemUse.getId() == element[0] && player.getInventory().contains(itemUse)) {
					player.message("You poison your weapon..");
					player.getInventory().delete(new Item(element[0]));
					player.getInventory().delete(new Item(weapon.getItemId()));
					player.getInventory().add(new Item(229));
					player.getInventory().add(new Item(element[1]));
				}
		}
	}
}
