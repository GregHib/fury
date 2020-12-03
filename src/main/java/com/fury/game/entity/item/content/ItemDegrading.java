package com.fury.game.entity.item.content;

import com.fury.cache.def.Loader;
import com.fury.game.container.impl.equip.Slot;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.Flag;

public class ItemDegrading {

	public static boolean handleItemDegrading(Player p, DegradingItem d) {
		int equipId = p.getEquipment().get(d.equipSlot).getId();
		if(equipId == d.nonDeg || equipId == d.deg) {
			int maxCharges = d.degradingCharges;
			int currentCharges = getAndIncrementCharge(p, d, false);
			boolean degradeCompletely = currentCharges >= maxCharges;
			if(equipId == d.deg && !degradeCompletely) {
				return true;
			}
			degradeCompletely = degradeCompletely && equipId == d.deg;
			p.getEquipment().delete(d.equipSlot.ordinal());
			if(!degradeCompletely)
				p.getEquipment().add(new Item(d.deg));
			p.getEquipment().refresh();
			getAndIncrementCharge(p, d, true);
			p.getUpdateFlags().add(Flag.APPEARANCE);
			String ext = !degradeCompletely ? "degraded slightly" : "turned into dust";
			p.message("Your "+ Loader.getItem(equipId).getName().replace(" (deg)", "")+" has "+ext+"!");
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("incomplete-switch")
	public static int getAndIncrementCharge(Player p, DegradingItem d, boolean reset) {
		switch(d) {
		case RING_OF_RECOIL:
			if(reset) {
				p.setRecoilCharges(0);
				return 0;
			} else {
				p.setRecoilCharges(p.getRecoilCharges() + 1);
				return p.getRecoilCharges() + 1;
			}
		}
		return d.degradingCharges;
	}

	/*
	 * The enum holding all degradeable equipment
	 */
	public enum DegradingItem {

		/*
		 * Recoil
		 */
		RING_OF_RECOIL(2550, 2550, Slot.RING, 100),
		
		 // Statius's equipment
		 
		CORRUPT_STATIUS_FULL_HELM(13920, 13922, Slot.HEAD, 200),
		CORRUPT_STATIUS_PLATEBODY(13908, 13910, Slot.BODY, 200),
		CORRUPT_STATIUS_PLATELEGS(13914, 13916, Slot.LEGS, 200),
		CORRUPT_STATIUS_WARHAMMER(13926, 13928, Slot.WEAPON, 200),

		
		 // Vesta's equipment
		 
		CORRUPT_VESTAS_CHAINBODY(13911, 13913, Slot.BODY, 200),
		CORRUPT_VESTAS_PLATESKIRT(13917, 13919, Slot.LEGS, 200),
		CORRUPT_VESTAS_LONGSWORD(13923, 13925, Slot.WEAPON, 160),
		CORRUPT_VESTAS_SPEAR(13929, 13931, Slot.WEAPON, 160),

		
		  // Zuriel's equipment
		 
		CORRUPT_ZURIELS_HOOD(13938, 13940, Slot.HEAD, 200),
		CORRUPT_ZURIELS_ROBE_TOP(13932, 13934, Slot.BODY, 200),
		CORRUPT_ZURIELS_ROBE_BOTTOM(13935, 13937, Slot.LEGS, 200),
		CORRUPT_ZURIELS_STAFF(13941, 13943, Slot.WEAPON, 200),

		
		 // Morrigan's equipment
		 
		CORRUPT_MORRIGANS_COIF(13950, 13952, Slot.HEAD, 200),
		CORRUPT_MORRIGANS_LEATHER_BODY(13944, 13946, Slot.BODY, 200),
		CORRUPT_MORRIGANS_LEATHER_CHAPS(13944, 13946, Slot.LEGS, 200),
		 
		/*
		 * Brawling gloves
		 */
//		BRAWLING_GLOVES_SMITHING(13855, 13855, Slot.HANDS, 600),
//		BRAWLING_GLOVES_PRAYER(13848, 13848, Slot.HANDS, 600),
//		BRAWLING_GLOVES_COOKING(13857, 13857, Slot.HANDS, 600),
//		BRAWLING_GLOVES_FISHING(13856, 13856, Slot.HANDS, 600),
//		BRAWLING_GLOVES_THIEVING(13854, 13854, Slot.HANDS, 600),
//		BRAWLING_GLOVES_HUNTER(13853, 13853, Slot.HANDS, 600),
//		BRAWLING_GLOVES_MINING(13852, 13852, Slot.HANDS, 600),
//		BRAWLING_GLOVES_FIREMAKING(13851, 13851, Slot.HANDS, 600),
//		BRAWLING_GLOVES_WOODCUTTING(13850, 13850, Slot.HANDS, 600),
//		BRAWLING_GLOVES_AGILITY(13849, 13849, Slot.HANDS, 600)
		;

		DegradingItem(int nonDeg, int deg, Slot equipSlot, int degradingCharges) {
			this.nonDeg = nonDeg;
			this.deg = deg;
			this.equipSlot = equipSlot;
			this.degradingCharges = degradingCharges;
		}

		private int nonDeg, deg;
		private Slot equipSlot;
		private int degradingCharges;
		
		public static DegradingItem forNonDeg(int item) {
			for(DegradingItem d : DegradingItem.values()) {
				if(d.nonDeg == item) {
					return d;
				}
			}
			return null;
		}
	}

	public static boolean degradesOnDrop(int item) {
		switch(item) {
			//Nex armour
			case 20135:
			case 20139:
			case 20143:
			case 20147:
			case 20151:
			case 20155:
			case 20159:
			case 20163:
			case 20167:
				//Pk armour
			case 13858:
			case 13861:
			case 13864:
			case 13867:
			case 13870:
			case 13873:
			case 13876:
			case 13884:
			case 13890:
			case 13896:
			case 13902:
			case 13887:
			case 13893:
			case 13899:
			case 13905:
				return true;
			default:
				return false;
		}
	}

	public static int getDegradedItemId(int item) {
		switch(item) {
			case 20135:
			case 20139:
			case 20143:
			case 20147:
			case 20151:
			case 20155:
			case 20159:
			case 20163:
			case 20167:
				return item + 3;
			case 13858:
			case 13861:
			case 13864:
			case 13867:
			case 13870:
			case 13873:
			case 13876:
			case 13884:
			case 13890:
			case 13896:
			case 13902:
			case 13887:
			case 13893:
			case 13899:
			case 13905:
				return item + 2;
			default:
				return item;
		}
	}
}
