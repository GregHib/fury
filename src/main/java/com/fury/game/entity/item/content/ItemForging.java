package com.fury.game.entity.item.content;

import com.fury.cache.def.Loader;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.Misc;

/**
 * Handles item forging, such as Spirit shields making etc.
 * @author Gabriel Hannason and Samy
 */
public class ItemForging {

	public static void forgeItem(final Player p, final Item item1, final Item item2) {
		if(item1.isEqual(item2))
			return;
		ItemForgeData data = ItemForgeData.getDataForItems(item1.getId(), item2.getId());
		if(data == null || !p.getInventory().contains(item1) || !p.getInventory().contains(item2))
			return;
		if(data == ItemForgeData.SLAYER_HELMET && !p.getSlayerManager().getLearnt()[2]) {
			p.message("You must have unlocked the ability to make a Slayer helmet.");
			return;
		}
		if(!p.getTimers().getClickDelay().elapsed(3000))
			return;
		if(p.getInterfaceId() > 0) {
			p.message("Please close the interface you have open before doing this.");
			return;
		}
		Skill skill = Skill.forId(data.skillRequirement[0]);
		int skillReq = data.skillRequirement[1];
		if(p.getSkills().getLevel(skill) >= skillReq) {
            final String itemName = Misc.formatText(Loader.getItem(data.product.getId()).getName().toLowerCase());
			for(Item reqItem : data.requiredItems) {
				if(!p.getInventory().contains(reqItem) || p.getInventory().getAmount(reqItem) < reqItem.getAmount()) {
					p.message("You need " + Misc.anOrA(reqItem.getName()) + " " + reqItem.getName() + " to make " + Misc.anOrA(itemName) + " " + itemName + ".");
					return;
				}
			}
			for(Item reqItem : data.requiredItems) {
				if(reqItem.getId() == 1755 || reqItem.getId() == 1595)
					continue;
				p.getInventory().delete(reqItem);
			}
			p.getInventory().add(data.product);
			p.getInventory().refresh();
			p.message("You make " + Misc.anOrA(itemName) + " " + itemName + ".");
			p.getTimers().getClickDelay().reset();
			p.getSkills().addExperience(skill, data.skillRequirement[2], false);
			if(data == ItemForgeData.AMULET_OF_FURY) {
				Achievements.finishAchievement(p, Achievements.AchievementData.CREATE_A_FURY);
			}
			/*if(data == ItemForgeData.ARMADYL_GODSWORD || data == ItemForgeData.BANDOS_GODSWORD || data == ItemForgeData.ZAMORAK_GODSWORD || data == ItemForgeData.SARADOMIN_GODSWORD) {
				Achievements.finishAchievement(p, Achievements.AchievementData.ASSEMBLE_A_GODSWORD);
				Achievements.doProgress(p, Achievements.AchievementData.ASSEMBLE_5_GODSWORDS);
			}*/
			return;
		} else {
			p.message("You need "+Misc.anOrA(skill.getFormatName())+" "+skill.getFormatName()+" level of at least "+skillReq+" to make this item.");
			return;
		}
	}

	/**
	 ** The enum holding all our data
	 */
	private enum ItemForgeData {
		BLESSED_SPIRIT_SHIELD(new Item[] {new Item(13754), new Item(13734)}, new Item(13736), new int[] {1, -1, 0}),
		SPECTRAL_SPIRIT_SHIELD(new Item[] {new Item(13752), new Item(13736)}, new Item(13744), new int[] {13, 85, 40000}),
		ARCANE_SPIRIT_SHIELD(new Item[] {new Item(13746), new Item(13736)}, new Item(13738), new int[] {13, 85, 40000}),
		ELYSIAN_SPIRIT_SHIELD(new Item[] {new Item(13750), new Item(13736)}, new Item(13742), new int[] {13, 85, 40000}),
		DIVINE_SPIRIT_SHIELD(new Item[] {new Item(13748), new Item(13736)}, new Item(13740), new int[] {13, 85, 40000}),

		AMULET_OF_GLORY(new Item[] {new Item(2357), new Item(1615)}, new Item(1704), new int[] {12, 80, 10000}),
		AMULET_OF_STRENGTH(new Item[] {new Item(2357), new Item(1603)}, new Item(1725), new int[] {12, 50, 3500}),
		AMULET_OF_MAGIC(new Item[] {new Item(2357), new Item(1607)}, new Item(1727), new int[] {12, 24, 1400}),
		AMULET_OF_POWER(new Item[] {new Item(2357), new Item(1691)}, new Item(1731), new int[] {12, 70, 4500}),
		AMULET_OF_DEFENCE(new Item[] {new Item(2357), new Item(1605)}, new Item(1729), new int[] {12, 31, 2100}),
		AMULET_OF_FURY(new Item[] {new Item(2357), new Item(6573)}, new Item(6585), new int[] {12, 90, 175000}),
		
		//ONYX_BOLTS(new Item[] {new Item(2363), new Item(6573)}, new Item(9245, 15), new int[] {9, 90, 20000}),
		//DRAGONSTONE_BOLTS(new Item[] {new Item(2363), new Item(1615)}, new Item(9244, 15), new int[] {9, 80, 10000}),
		//DIAMOND_BOLTS(new Item[] {new Item(2361), new Item(1601)}, new Item(9243, 15), new int[] {9, 70, 8000}),
		//RUBY_BOLTS(new Item[] {new Item(2361), new Item(1603)}, new Item(9242, 15), new int[] {9, 60, 6000}),
		//EMERALD_BOLTS(new Item[] {new Item(2359), new Item(1605)}, new Item(9241, 15), new int[] {9, 40, 2000}),
		//SAPPHIRE_BOLTS(new Item[] {new Item(2359), new Item(1607)}, new Item(9240, 15), new int[] {9, 30, 1000}),
		
		//TOKKUL(new Item[] {new Item(6570), new Item(6522)}, new Item(6529, 1000), new int[] {12, 95, 10000}),
		
		//ARDOUGNE_CLOAK_4(new Item[] {new Item(15103), new Item(15104), new Item(15105), new Item(15106)}, new Item(19748), new int[] {5, 94, 200000}),
		
		DRAGON_SQ_SHIELD(new Item[] {new Item(2368), new Item(2366)}, new Item(1187), new int[] {13, 60, 10000}),
		DRAGON_PLATEBODY(new Item[] {new Item(14472), new Item(14474), new Item(14476)}, new Item(14479), new int[] {13, 92, 120000}),
		DRAGONFIRE_SHIELD(new Item[] {new Item(11286), new Item(1540)}, new Item(11283), new int[] {13, 82, 36000}),
        POLYPORE_STAFF(new Item[] {new Item(22498), new Item(22448, 3000)}, new Item(22494), new int[] {1, -1, 0}),
		CRYSTAL_KEY(new Item[] {new Item(985), new Item(987)}, new Item(989), new int[] {1, -1, 0}),

		GODSWORD_BLADE(new Item[] {new Item(11710), new Item(11712), new Item(11714)}, new Item(11690), new int[] {1, -1, 0}),
		ARMADYL_GODSWORD(new Item[] {new Item(11702), new Item(11690)}, new Item(11694), new int[] {1, -1, 0}),
		BANDOS_GODSWORD(new Item[] {new Item(11704), new Item(11690)}, new Item(11696), new int[] {1, -1, 0}),
		SARADOMIN_GODSWORD(new Item[] {new Item(11706), new Item(11690)}, new Item(11698), new int[] {1, -1, 0}),
		ZAMORAK_GODSWORD(new Item[] {new Item(11708), new Item(11690)}, new Item(11700), new int[] {1, -1, 0}),

//		AMULET_OF_FURY_ORNAMENT(new Item[] {new Item(19333), new Item(6585)}, new Item(19335), new int[] {1, -1, 0}),
//		DRAGON_FULL_HELM_SPIKE(new Item[] {new Item(19354), new Item(11335)}, new Item(19341), new int[] {1, -1, 0}),
//		DRAGON_PLATELEGS_SPIKE(new Item[] {new Item(19356), new Item(4087)}, new Item(19343), new int[] {1, -1, 0}),
//		DRAGON_PLATEBODY_SPIKE(new Item[] {new Item(19358), new Item(14479)}, new Item(19342), new int[] {1, -1, 0}),
//		DRAGON_SQUARE_SHIELD_SPIKE(new Item[] {new Item(19360), new Item(1187)}, new Item(19345), new int[] {1, -1, 0}),
//		DRAGON_FULL_HELM_GOLD(new Item[] {new Item(19346), new Item(11335)}, new Item(19336), new int[] {1, -1, 0}),
//		DRAGON_PLATELEGS_GOLD(new Item[] {new Item(19348), new Item(4087)}, new Item(19338), new int[] {1, -1, 0}),
//		DRAGON_PLATEBODY_GOLD(new Item[] {new Item(19350), new Item(14479)}, new Item(19337), new int[] {1, -1, 0}),
//		DRAGON_SQUARE_SHIELD_GOLD(new Item[] {new Item(19352), new Item(1187)}, new Item(19340), new int[] {1, -1, 0}),

		SLAYER_HELMET(new Item[] {new Item(8921), new Item(4166), new Item(4164), new Item(4168), new Item(4551)}, new Item(13263), new int[] {12, 55, 0}),
		FULL_SLAYER_HELMET(new Item[] {new Item(13263), new Item(15490), new Item(15488)}, new Item(15492), new int[] {18, 75, 0}),

		;


		ItemForgeData(Item[] requiredItems, Item product, int[] skillRequirement) {
			this.requiredItems = requiredItems;
			this.product = product;
			this.skillRequirement = skillRequirement;
		}

		private Item[] requiredItems;
		private Item product;
		private int[] skillRequirement;

		public static ItemForgeData getDataForItems(int item1, int item2) {
			for(ItemForgeData shieldData : ItemForgeData.values()) {
				int found = 0;
				for(Item it : shieldData.requiredItems) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return shieldData;
			}
			return null;
		}
	}
}
