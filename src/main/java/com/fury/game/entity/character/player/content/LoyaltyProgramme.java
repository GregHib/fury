package com.fury.game.entity.character.player.content;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.Flag;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LoyaltyProgramme {

	public enum LoyaltyRecolours {
		RED_ROBIN(new Item(20949), 10000, 2581, 43522, false, false, 0),
		YELLOW_ROBIN(new Item(20950), 10000, 2581, 43533, false, false, 0),
		BLUE_ROBIN(new Item(20951), 10000, 2581, 43544, false, false, 0),
		WHITE_ROBIN(new Item(20952), 10000, 2581, 43555, false, false, 0),

		RED_SOL(new Item(22207, Revision.PRE_RS3), 15000, 15486, 43566, false, false, 0),
		YELLOW_SOL(new Item(22209, Revision.PRE_RS3), 15000, 15486, 43577, false, false, 0),
		BLUE_SOL(new Item(22211, Revision.PRE_RS3), 15000, 15486, 43588, false, false, 0),
		GREEN_SOL(new Item(22213, Revision.PRE_RS3), 15000, 15486, 43599, false, false, 0),
		
		YELLOW_WHIP(new Item(15441), 25000, 4151, 43610, false, false, 0),
		BLUE_WHIP(new Item(15442), 25000, 4151, 43621, false, false, 0),
		WHITE_WHIP(new Item(15443), 25000, 4151, 43632, false, false, 0),
		GREEN_WHIP(new Item(15444), 25000, 4151, 43643, false, false, 0);

		private Item item;
		private int requiredItem, cost, frame, amount;
		private boolean reqUnlock, discount;
		
		LoyaltyRecolours(Item item, int cost, int requiredItem, int frame, boolean reqUnlock, boolean discount, int amount) {
			this.item = item;
			this.cost = cost;
			this.frame = frame;
			this.requiredItem = requiredItem;
			this.reqUnlock = reqUnlock;
			this.discount = discount;
			this.amount = amount;
		}
		
		public Item getItem() {
			return item;
		}
		public int getCost() {
			return cost;
		}
		public int getFrame() {
			return frame;
		}
		public boolean reqUnlock() {
			return reqUnlock;
		}
		public boolean hasDiscount() {
			return discount;
		}
		public int getDiscount() {
			return amount;
		}
		public int getRequiredItem() {
			return requiredItem;
		}
		public void setDiscount(int amount) {
			this.discount = true;
			this.amount = amount;
		}
		public void clearDiscount() {
			this.discount = false;
			this.amount = 0;
		}
		public static LoyaltyRecolours getRecolour(int button) {
			for(LoyaltyRecolours r : LoyaltyRecolours.values()) {
				if(r.frame == button)
					return r;
			}
			return null;
		}
	}
	public enum LoyaltyItems {
		SPINNING_PLATE(4613, 5000, "Round and round it goes, will you drop it? Who knows.", 43302, false, false, 0),
		TOY_KITE(12844, 8000, "Don't let go or it'll fly away!", 43313, false, false, 0),
		REINDEER_HAT(10507, 15000, "Imitate everyone's favourite red-nosed reindeer.", 43324, false, false, 0),
		RUBBER_CHICKEN(4566, 20000, "Perhaps not the most powerful weapon, don't attempt to eat it!", 43335, false, false, 0),
		SLEEPING_CAP(10746, 20000, "Once worn makes you feel sleepy and enchances the yawn emote.", 43346, false, false, 0),
		PANTALOONS(10744, 20000, "Posh underwear which enhances the bow emote when worn.", 43357, false, false, 0),
		PRAYER_BOOK(10890, 25000, "One read of this book can cure even the strongest of posions.", 43368, false, false, 0),
		POWDERED_WIG(10740, 30000, "Be a judge and if people don't listen there's always the enhanced angry emote.", 43379, false, false, 0),
		YO_YO(4079, 35000, "Up and down, and up and down, and up and down.", 43390, false, false, 0),
		FLARED_TROUSERS(10742, 40000, "Super stylish, drop some moves in the club with the enhanced dance emote.", 43401, false, false, 0)
		;
		
		private int id, cost, frame, amount;
		private boolean reqUnlock, discount;
		private String desc;
		
		LoyaltyItems(int id, int cost, String description, int frame, boolean reqUnlock, boolean discount, int amount) {
			this.id = id;
			this.cost = cost;
			this.frame = frame;
			this.desc = description;
			this.reqUnlock = reqUnlock;
			this.discount = discount;
			this.amount = amount;
		}
		
		public int getId() {
			return id;
		}
		public int getCost() {
			return cost;
		}
		public int getFrame() {
			return frame;
		}
		public boolean reqUnlock() {
			return reqUnlock;
		}
		public boolean hasDiscount() {
			return discount;
		}
		public int getDiscount() {
			return amount;
		}
		public String getDescription() {
			return desc;
		}
		public static LoyaltyItems getItem(int button) {
			for(LoyaltyItems i : LoyaltyItems.values()) {
				if(i.frame == button)
					return i;
			}
			return null;
		}
		public void setDiscount(int amount) {
			this.discount = true;
			this.amount = amount;
		}
		public void clearDiscount() {
			this.discount = false;
			this.amount = 0;
		}
	}
	public enum LoyaltyTitles {

		NONE(0, -1, -1, false, false, 0) {//buy button =  +6,
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},		
		KILLER(1, 2000, 43052, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[1]) {
					if(sendMessage)
						p.message("To unlock this title, you must kill another player.");
					return false;
				}
				return true;
			}
		},
		SLAUGHTERER(2, 5000, 43085, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[2]) {
					if(sendMessage)
						p.message("To unlock this title, you must kill 20 other players.");
					return false;
				}
				return true;
			}
		},
		GENOCIDAL(3, 10000, 43140, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[3]) {
					if(sendMessage)
						p.message("To unlock this title, you must kill 50 other players.");
					return false;
				}
				return true;
			}
		},
		IMMORTAL(4, 15000, 43184, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[4]) {
					if(sendMessage)
						p.message("To unlock this title, you must kill 15 players without dying.");
					return false;
				}
				return true;
			}
		},
		SKILLER(5, 9000, 43129, true, false, 0) {
			@Override
			boolean canBuy(Player player, boolean sendMessage) {
				if(!player.getUnlockedLoyaltyTitles()[5]) {
					for(Skill skill : CombatConstants.NON_COMBAT_SKILLS) {
						if(!player.getSkills().hasLevel(skill, 99)) {
							if(sendMessage)
								player.message("You must be at least level 99 in every non-combat skill for this title.");
							return false;
						}
					}
				}
				unlock(player, this);
				return true;
			}
		},
		COMBATANT(6, 5000, 43096, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[6]) {
					for(int i = 0; i <= 6; i++) {
						Skill skill = Skill.forId(i);
						if(!p.getSkills().hasLevel(skill, 99)) {
							if(sendMessage)
								p.message("You must be at least level 99 in every combat skill for this title.");
							return false;
						}
					}
				}
				unlock(p, this);
				return true;
			}
		},
		MAXED(7, 10000, 43151, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[7]) {
					if(!p.getSkills().isMaxed()) {
						if(sendMessage)
							p.message("You must be at least level 99 in every skill for this title.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},
		GODSLAYER(8, 15000, 43217, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[8]) {
					for(boolean b : p.getAchievementAttributes().getGodsKilled()) {
						if(!b) {
							if(sendMessage)
								p.message("To unlock this title, you must slay all of the 5 gods in the Godwars Dungeon.");
							return false;
						}
					}
				}
				unlock(p, this);
				return true;
			}
		},
		LOYALIST(9, 25000, 43272, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[9]) {
					if(!p.getPoints().has(Points.LOYALTY, 100000)) {
						if(sendMessage)
							p.message("To unlock this title, you must gain 100,000 Loyalty Points simultaneously.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},
		VETERAN(10, 30000, 43283, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[10]) {
					if(p.getAchievementAttributes().getTotalLoyaltyPointsEarned() < 500000) {
						if(sendMessage)
							p.message("To unlock this title, you must have earned 500,000 Loyalty Points in total.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},
		GAMBLER(11, 15000, 43228, true, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				if(!p.getUnlockedLoyaltyTitles()[11]) {
					if(!p.getInventory().contains(new Item(15084))) {
						if(sendMessage)
							p.message("To unlock this title, you must have a Dice in your inventory.");
						return false;
					}
				}
				unlock(p, this);
				return true;
			}
		},


		KING(12, 25000, 43250, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		QUEEN(13, 25000,  43261, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		LORD(14, 20000,  43239, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		DUKE(15, 15000, 43195, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		DUCHESS(16, 15000,  43206, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		SIR(17, 8000, 43107, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		LADY(18, 8000, 43118, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		BARON(19, 10000, 43162, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		BARONESS(20, 10000, 43173, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		EVIL(21, 5000, 43074, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		GOOD(22, 5000, 43063, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return true;
			}
		},
		ADVISER(23, -1, -1, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return false;
			}
		},
		COMMUNITY_MANAGER(24, -1, -1, false, false, 0) {
			@Override
			boolean canBuy(Player p, boolean sendMessage) {
				return false;
			}
		};
		
		LoyaltyTitles(int id, int cost, int frame, boolean reqUnlock, boolean discount, int amount) {
			this.id = id;
			this.cost = cost;
			this.frame = frame;
			this.reqUnlock = reqUnlock;
			this.discount = discount;
			this.amount = amount;
		}

		private int id, cost, frame, amount;
		private boolean reqUnlock, discount;
		abstract boolean canBuy(Player p, boolean sendMessage);

		public int getId() {
			return id;
		}
		public int getCost() {
			return cost;
		}
		public int getFrame() {
			return frame;
		}
		public boolean reqUnlock() {
			return reqUnlock;
		}
		public boolean hasDiscount() {
			return discount;
		}
		public int getDiscount() {
			return amount;
		}
		public void setDiscount(int amount) {
			this.discount = true;
			this.amount = amount;
		}
		public void clearDiscount() {
			this.discount = false;
			this.amount = 0;
		}
		public static LoyaltyTitles getTitle(int button) {
			for(LoyaltyTitles t : LoyaltyTitles.values()) {
				if(t.frame == button)
					return t;
			}
			return null;
		}
		
	}
	
	public static boolean hasOffers() {
		for(LoyaltyTitles t : LoyaltyTitles.values()) {
			if(t.hasDiscount())
				return true;
		}
		for(LoyaltyItems i : LoyaltyItems.values()) {
			if(i.hasDiscount())
				return true;
		}
		for(LoyaltyRecolours i : LoyaltyRecolours.values()) {
			if(i.hasDiscount())
				return true;
		}
		return false;
	}
	
	public static void unlock(Player player, LoyaltyTitles title) {
		if(!title.reqUnlock())
			return;
		if(player.getUnlockedLoyaltyTitles()[title.getId()])
			return;

		player.setUnlockedLoyaltyTitle(title.getId());
		player.message("You've unlocked the "+Misc.formatText(title.name().toLowerCase())+" loyalty title!");
		if(title == LoyaltyTitles.VETERAN)
			player.message("Talk to Hans in Lumbridge about buying a Veteran Cape.");
	}
	
	public static boolean handleTitles(Player player, int button) {
		LoyaltyTitles info = LoyaltyTitles.getTitle(button);
		
		info = handleTitleOffersInfo(info, button);
		
		if(info != null) {
			if(info.reqUnlock()) {
				player.message("Once unlocked, this title can be purchased for " + (info.getCost() - info.getDiscount()) + " Loyalty Points.");
			} else {
				player.message("This title can be purchased for " + (info.getCost() - info.getDiscount()) + " Loyalty Points.");
			}
			player.message("When set titles will appear before your username for everyone to see.");
			return true;
		}
		
		LoyaltyTitles title = LoyaltyTitles.getTitle(button - 6);
		title = handleTitleOffers(title, button);
		
		if(title != null) {
			//If already bought
			if(player.getBoughtLoyaltyTitles()[title.getId()]) {
				if(player.getLoyaltyTitle() == title) {
					player.message("You are already using this title.");
					return true;
				}
				player.setLoyaltyTitle(title);
				player.message("You've changed your title.");
				player.getUpdateFlags().add(Flag.APPEARANCE);
				return true;
			} else {//If not bought
				//If can't buy
				if(!title.canBuy(player, true))
					return true;
				//Check purchase
				if(player.getPoints().has(Points.LOYALTY, title.getCost() - title.getDiscount())) {
					player.getPoints().remove(Points.LOYALTY, title.getCost() - title.getDiscount());
					player.setBoughtLoyaltyTitle(title.getId());
					player.getPointsHandler().refreshPanel();
					refresh(player, false);
					player.getPacketSender().sendString(title.getFrame()+9, "Set Title");
				} else {
					player.message("You need at least " + (title.getCost() - title.getDiscount()) + " Loyalty Points to buy this title.");
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleItems(Player player, int button) {
		LoyaltyItems info = LoyaltyItems.getItem(button);
		info = handleItemOffersInfo(info, button);
		if(info != null) {
			if(info.reqUnlock()) {
				player.message("Once unlocked, this item can be purchased for " + (info.getCost() - info.getDiscount()) + " Loyalty Points.");
			} else {
				player.message(info.getDescription());
			}
			return true;
		}
		
		LoyaltyItems item = LoyaltyItems.getItem(button - 6);
		item = handleItemOffers(item, button);
		if(item != null) {
			if(player.getInventory().getSpaces() <= 0) {
				player.message("You need a free inventory slot to be able to purchase that item.");
			}
			//Check purchase
			if(player.getPoints().has(Points.LOYALTY, item.getCost() - item.getDiscount())) {
				player.getPoints().remove(Points.LOYALTY, item.getCost() - item.getDiscount());
				player.getPointsHandler().refreshPanel();
				player.getInventory().add(new Item(item.getId()));
				refresh(player);
			} else {
				player.message("You need at least " + (item.getCost() - item.getDiscount()) + " Loyalty Points to buy this item.");
			}
			return true;
		}
		return false;
	}
	
	public static boolean handleRecolours(Player player, int button) {
		LoyaltyRecolours info = LoyaltyRecolours.getRecolour(button);
		info = handleRecolourOffersInfo(info, button);
		if(info != null) {
			if(info.reqUnlock()) {
				player.message("Once unlocked, this re-colour can be purchased for " + (info.getCost() - info.getDiscount()) + " Loyalty Points.");
			} else {
				player.message("Spice up your " + new Item(info.getRequiredItem()).getName().toLowerCase() + " with some colour.");
			}
			return true;
		}
		
		LoyaltyRecolours recolour = LoyaltyRecolours.getRecolour(button - 6);
		recolour = handleRecolourOffers(recolour, button);
		if(recolour != null) {
			//Check purchase
			 if(!player.getInventory().contains(new Item(recolour.getRequiredItem()))) {
				player.message("You need to have a " + new Item(recolour.getRequiredItem()).getName().toLowerCase() + " in your inventory to be able to do this.");
			} else if(player.getPoints().has(Points.LOYALTY, recolour.getCost() - recolour.getDiscount())) {
				player.getPoints().remove(Points.LOYALTY, recolour.getCost() - recolour.getDiscount());
				player.getPointsHandler().refreshPanel();
				player.getInventory().delete(new Item(recolour.getRequiredItem()));
				player.getInventory().add(recolour.getItem());
				refresh(player);
			} else {
				player.message("You need at least " + (recolour.getCost() - recolour.getDiscount()) + " Loyalty Points to buy this re-colour.");
			}
			return true;
		}
		return false;
	}
	
	public static boolean handleButton(Player player, int button) {
		switch(button) {
		case 43008://Home
			player.getPacketSender().sendInterface(HOME_PAGE);
			player.getPacketSender().sendString(43004, "Now Viewing: Home");
			return true;
		case 43013://Auras
		case 43018://Emotes
			return true;
		case HOME_PAGE+15://Home Button 3
		case 43023://Items
			player.getPacketSender().sendInterface(ITEMS_PAGE);
			player.getPacketSender().sendString(43004, "Now Viewing: Items");
			return true;
		case HOME_PAGE+8://Home Button 2
		case 43028://Titles
			player.getPacketSender().sendInterface(TITLES_PAGE);
			player.getPacketSender().sendString(43004, "Now Viewing: Titles");
			return true;
		case HOME_PAGE+1://Home Button 1
		case 43033://Re-colours
			player.getPacketSender().sendInterface(RECOLOUR_PAGE);
			player.getPacketSender().sendString(43004, "Now Viewing: Re-colours");
			return true;
		case 43038://Special Offers
			if(hasOffers()) {
				player.getPacketSender().sendInterface(OFFERS_PAGE);
				player.getPacketSender().sendString(43004, "Now Viewing: Special Offers");
			}
			return true;
		}
		
		if(handleTitles(player, button))
			return true;
		
		if(handleItems(player, button))
			return true;

        return handleRecolours(player, button);

    }
	
	public static int getOfferInfoButton(int button) {
		for(int i = 0; i <= 11; i++) {
			if((OFFERS_PAGE + 2 + (i * 13) + 1) == button) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getOfferBuyButton(int button) {
		for(int i = 0; i <= 11; i++) {
			if((OFFERS_PAGE + 2 + (i * 13) + 8) == button) {
				return i;
			}
		}
		return -1;
	}
	
	public static LoyaltyItems handleItemOffersInfo(LoyaltyItems item, int button) {
		int i = getOfferInfoButton(button);
		if(i == -1 || itemOffers[i] == null) {
			return item;
		} else {
			return itemOffers[i];
		}
	}
	
	public static LoyaltyRecolours handleRecolourOffersInfo(LoyaltyRecolours recolour, int button) {
		int i = getOfferInfoButton(button);
		if(i == -1 || recolourOffers[i] == null) {
			return recolour;
		} else {
			return recolourOffers[i];
		}
	}
	
	public static LoyaltyItems handleItemOffers(LoyaltyItems item, int button) {
		int i = getOfferBuyButton(button);
		if(i == -1 || itemOffers[i] == null) {
			return item;
		} else {
			return itemOffers[i];
		}
	}

	public static LoyaltyRecolours handleRecolourOffers(LoyaltyRecolours recolour, int button) {
		int i = getOfferBuyButton(button);
		if(i == -1 || recolourOffers[i] == null) {
			return recolour;
		} else {
			return recolourOffers[i];
		}
	}
	
	public static LoyaltyTitles handleTitleOffersInfo(LoyaltyTitles title, int button) {
		int i = getOfferInfoButton(button);
		if(i == -1 || titleOffers[i] == null) {
			return title;
		} else {
			return titleOffers[i];
		}
	}
	
	public static LoyaltyTitles handleTitleOffers(LoyaltyTitles title, int button) {
		int i = getOfferBuyButton(button);
		if(i == -1 || titleOffers[i] == null) {
			return title;
		} else {
			return titleOffers[i];
		}
	}
	
	public static void updateOffers(Player player) {
		int count = 0;
		for(LoyaltyTitles title : LoyaltyTitles.values()) {
			if(title.hasDiscount()) {
				sendLoyaltySlot(player, count, title);
				count++;
			}
		}
		for(LoyaltyItems item : LoyaltyItems.values()) {
			if(item.hasDiscount()) {
				sendLoyaltySlot(player, count, item);
				count++;
			}
		}
		for(LoyaltyRecolours recolour : LoyaltyRecolours.values()) {
			if(recolour.hasDiscount()) {
				sendLoyaltySlot(player, count, recolour);
				count++;
			}
		}
		if(count > 11)return;
		for(int i = count; i <= 11; i++)
			player.getPacketSender().sendLoyaltyInfo(OFFERS_PAGE + 2 + (i * 13), 4, true);
	}
	
	static LoyaltyTitles[] titleOffers = new LoyaltyTitles[12];
	static LoyaltyItems[] itemOffers = new LoyaltyItems[12];
	static LoyaltyRecolours[] recolourOffers = new LoyaltyRecolours[12];
	
	public static void sendLoyaltySlot(Player player, int count, LoyaltyTitles title) {
		if(count > 11)return;
		int frame = OFFERS_PAGE + 2 + (count * 13);
		
		//Send create button
		player.getPacketSender().sendLoyaltyInfo(frame, 9, true);
		
		//Send name & price
		player.getPacketSender().sendString(frame + 4, title.name().substring(0, 1) + title.name().substring(1).toLowerCase().replace("_", " "));
		player.getPacketSender().sendString(frame + 5, Misc.insertCommasToNumber(""+(title.getCost() - title.getDiscount())) + " Points");
		
		//set active with correct colour
		player.getPacketSender().sendLoyaltyInfo(frame, (player.getPoints().has(Points.LOYALTY, title.getCost() - title.getDiscount()) || player.getBoughtLoyaltyTitles()[title.getId()]) ? 7 : 6, title.reqUnlock());
		//Set sale sign
		player.getPacketSender().sendLoyaltyInfo(frame + 2, 5, true);
		//Send button
		player.getPacketSender().sendString(frame + 11, title.reqUnlock() ? "Locked" : "Buy");
		
		//If unlocked
		if(title.reqUnlock() && player.getUnlockedLoyaltyTitles()[title.getId()]) {
			player.getPacketSender().sendString(frame + 11, "Buy");
		}
		//If has bought
		if(player.getBoughtLoyaltyTitles()[title.getId()]) {
			player.getPacketSender().sendString(frame + 11, "Set Title");
			player.getPacketSender().sendString(frame + 5, "Bought");
		}
		
		//Send is item?
		player.getPacketSender().sendLoyaltyInfo(frame, 8, false);
		//Send item id
		//player.getPacketSender().sendLoyaltyItem(-1, 50 + count, true);
		player.getPacketSender().sendLoyaltyItem(OFFERS_PAGE + 2 + (count * 13) + 7, -1, Revision.RS2);
		
		
		titleOffers[count] = title;
	}
	
	public static void sendLoyaltySlot(Player player, int count, LoyaltyItems item) {
		if(count > 11)return;
		int frame = OFFERS_PAGE + 2 + (count * 13);
		//Send create button
		player.getPacketSender().sendLoyaltyInfo(frame, 9, true);
				
		//Send name & price
		player.getPacketSender().sendString(frame + 4, item.name().substring(0, 1) + item.name().substring(1).toLowerCase().replace("_", " "));
		player.getPacketSender().sendString(frame + 5, Misc.insertCommasToNumber(""+(item.getCost() - item.getDiscount())) + " Points");
		
		//set active with correct colour
		player.getPacketSender().sendLoyaltyInfo(frame, player.getPoints().has(Points.LOYALTY, item.getCost() - item.getDiscount()) ? 7 : 6, item.reqUnlock());
		//Set sale sign
		player.getPacketSender().sendLoyaltyInfo(frame + 2, 5, true);
		//Send button
		player.getPacketSender().sendString(frame + 11, item.reqUnlock() ? "Locked" : "Buy");
		//Send is item?
		player.getPacketSender().sendLoyaltyInfo(frame, 8, true);
		//Send item id
		player.getPacketSender().sendLoyaltyItem((OFFERS_PAGE + 2 + (count * 13)) + 7, item.getId(), Revision.RS2);
		
		itemOffers[count] = item;
	}
	
	public static void sendLoyaltySlot(Player player, int count, LoyaltyRecolours recolour) {
		if(count > 11)return;
		int frame = OFFERS_PAGE + 2 + (count * 13);
		//Send create button
		player.getPacketSender().sendLoyaltyInfo(frame, 9, true);
				
		//Send name & price
		player.getPacketSender().sendString(frame + 4, recolour.name().substring(0, 1) + recolour.name().substring(1).toLowerCase().replace("_", " "));
		player.getPacketSender().sendString(frame + 5, Misc.insertCommasToNumber(""+(recolour.getCost() - recolour.getDiscount())) + " Points");
		
		//set active with correct colour
		player.getPacketSender().sendLoyaltyInfo(frame, player.getPoints().has(Points.LOYALTY, recolour.getCost() - recolour.getDiscount()) ? 7 : 6, recolour.reqUnlock());
		//Set sale sign
		player.getPacketSender().sendLoyaltyInfo(frame + 2, 5, true);
		//Send button
		player.getPacketSender().sendString(frame + 11, recolour.reqUnlock() ? "Locked" : "Buy");
		//Send is item?
		player.getPacketSender().sendLoyaltyInfo(frame, 8, true);
		//Send item id
		player.getPacketSender().sendLoyaltyItem(OFFERS_PAGE + 2 + (count * 13) + 7, recolour.getItem().getId(), recolour.getItem().getRevision());
		recolourOffers[count] = recolour;
	}
	
	public static void open(Player player) {
		player.getPacketSender().sendInterface(HOME_PAGE);
		refresh(player);
	}

	public static void refresh(Player player) {
		refresh(player, true);
	}

	public static void refresh(Player player, boolean flicker) {
		boolean offers = hasOffers();
		
		//Refresh points display
		if(flicker)player.getPacketSender().sendString(43003, Misc.insertCommasToNumber(""+player.getPoints().getInt(Points.LOYALTY)));
				
		//Send special offers button toggle
		if(offers) {
			player.getPacketSender().sendLoyaltyInfo(43038, 1, true);
		} else {
			player.getPacketSender().sendLoyaltyInfo(43038, 0, true);
		}
		
		//Player save loyalty shop purchases
		//Saving using a list of purchased NAMES_ as array
		for(LoyaltyTitles title : LoyaltyTitles.values()) {
			if(title.getCost() <= 0)
				continue;
			//Send discounts - limit to boolean offers if causing lag
			player.getPacketSender().sendLoyaltyInfo(title.getFrame(), 5, (offers && title.hasDiscount()));
			
			//Send price
			//if(title.getDiscount() > 0) - add it if causing processing problems
			if(flicker)player.getPacketSender().sendString(title.getFrame() + 4, Misc.insertCommasToNumber(title.getCost() - title.getDiscount() + "") + " Points");
			
			//If unlocked
			if(flicker) {
				if(title.reqUnlock() && (!title.canBuy(player, false) || !player.getUnlockedLoyaltyTitles()[title.getId()])) {
					player.getPacketSender().sendString(title.getFrame() + 9, "Locked");
				} else {
					player.getPacketSender().sendString(title.getFrame() + 9, "Buy");
				}
			}
			//If has bought
			if(player.getBoughtLoyaltyTitles()[title.getId()]) {
				player.getPacketSender().sendString(title.getFrame() + 4, "Bought");
				player.getPacketSender().sendString(title.getFrame() + 9, "Set Title");
			}
			//Doesn't have enough to buy
			if(!player.getPoints().has(Points.LOYALTY, title.getCost() - title.getDiscount()) && !player.getBoughtLoyaltyTitles()[title.getId()]) {
				player.getPacketSender().sendLoyaltyInfo(title.getFrame(), 2, title.reqUnlock());
			} else {
				player.getPacketSender().sendLoyaltyInfo(title.getFrame(), 3, title.reqUnlock());
			}
		}
		//Items
		for(LoyaltyItems item : LoyaltyItems.values()) {
			//Send discounts
			player.getPacketSender().sendLoyaltyInfo(item.getFrame(), 5, (offers && item.hasDiscount()));
			
			//Send Price
			//if(item.getDiscount() > 0)
			if(flicker)player.getPacketSender().sendString(item.getFrame() + 4, Misc.insertCommasToNumber(item.getCost() - item.getDiscount()+"") + " Points");
			
			//Doesn't have enough to buy
			if(!player.getPoints().has(Points.LOYALTY, item.getCost() - item.getDiscount())) {
				player.getPacketSender().sendLoyaltyInfo(item.getFrame(), 2, item.reqUnlock());
			} else {
				player.getPacketSender().sendLoyaltyInfo(item.getFrame(), 3, item.reqUnlock());
			}
		}
		//Recolours
		for(LoyaltyRecolours recolour : LoyaltyRecolours.values()) {
			//Send discounts
			player.getPacketSender().sendLoyaltyInfo(recolour.getFrame(), 5, (offers && recolour.hasDiscount()));
			
			//Send Price
			//if(recolour.getDiscount() > 0)
			if(flicker)player.getPacketSender().sendString(recolour.getFrame() + 4, Misc.insertCommasToNumber(recolour.getCost() - recolour.getDiscount()+"") + " Points");
			
			//Doesn't have enough to buy
			if(!player.getPoints().has(Points.LOYALTY, recolour.getCost() - recolour.getDiscount())) {
				player.getPacketSender().sendLoyaltyInfo(recolour.getFrame(), 2, recolour.reqUnlock());
			} else {
				player.getPacketSender().sendLoyaltyInfo(recolour.getFrame(), 3, recolour.reqUnlock());
			}
		}
				
		//Send special offers
		if(offers) {
			updateOffers(player);
		}
	}

	public static void reset(Player player) {
		if(player.getLoyaltyTitle() == LoyaltyTitles.NONE) {
			player.message("You do not have a title set.");
		} else {
			player.message("Your loyalty title has been cleared.");
		}
		player.setLoyaltyTitle(LoyaltyTitles.NONE);
		player.getUpdateFlags().add(Flag.APPEARANCE);
	}

	public static void incrementPoints(Player player) {
		double pts = DonorStatus.get(player).getLoyaltyPointsGainModifier();
		
		/* Update in shop */
		if(player.getInterfaceId() == HOME_PAGE || player.getInterfaceId() == ITEMS_PAGE || player.getInterfaceId() == TITLES_PAGE || player.getInterfaceId() == RECOLOUR_PAGE || player.getInterfaceId() == OFFERS_PAGE) {
			int points = player.getPoints().getInt(Points.LOYALTY);
			for(LoyaltyTitles t : LoyaltyTitles.values()) {
				if(points < t.getCost()-t.getDiscount()) {
					if(points + pts >= t.getCost()-t.getDiscount()) {
						player.getPacketSender().sendLoyaltyInfo(t.getFrame(), 3, t.reqUnlock());
						if(t.hasDiscount()) {
							int c = 0;
							for(LoyaltyTitles o : titleOffers) {
								if(t == o)
									player.getPacketSender().sendLoyaltyInfo(OFFERS_PAGE + 2 + (c * 13), 7, t.reqUnlock());
								c++;
							}
						}
					}
				}
			}
			for(LoyaltyItems i : LoyaltyItems.values()) {
				if(points < i.getCost()-i.getDiscount()) {
					if(points + pts >= i.getCost()-i.getDiscount()) {
						player.getPacketSender().sendLoyaltyInfo(i.getFrame(), 3, i.reqUnlock());
						if(i.hasDiscount()) {
							int c = 0;
							for(LoyaltyItems o : itemOffers) {
								if(i == o)
									player.getPacketSender().sendLoyaltyInfo(OFFERS_PAGE + 2 + (c * 13), 7, i.reqUnlock());
								c++;
							}
						}
					}
				}
			}
			for(LoyaltyRecolours r : LoyaltyRecolours.values()) {
				if(points < r.getCost()-r.getDiscount()) {
					if(points + pts >= r.getCost()-r.getDiscount()) {
						player.getPacketSender().sendLoyaltyInfo(r.getFrame(), 3, r.reqUnlock());
						if(r.hasDiscount()) {
							int c = 0;
							for(LoyaltyRecolours o : recolourOffers) {
								if(r == o)
									player.getPacketSender().sendLoyaltyInfo(OFFERS_PAGE + 2 + (c * 13), 7, r.reqUnlock());
								c++;
							}
						}
					}
				}
			}
		}
		
		player.getPoints().add(Points.LOYALTY, pts);
		player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(pts);

		int totalPoints = player.getPoints().getInt(Points.LOYALTY);
		if(totalPoints >= 100000) {
			unlock(player, LoyaltyTitles.LOYALIST);
		}

		if(player.getInterfaceId() == HOME_PAGE || player.getInterfaceId() == ITEMS_PAGE || player.getInterfaceId() == TITLES_PAGE || player.getInterfaceId() == RECOLOUR_PAGE || player.getInterfaceId() == OFFERS_PAGE) {
			player.getPacketSender().sendString(43003, Misc.insertCommasToNumber(""+totalPoints));
		}
		player.getPacketSender().sendString(39185, "Loyalty Points: " + FontUtils.YELLOW + totalPoints + FontUtils.COL_END, Colours.ORANGE_2);
		
		if(player.getAchievementAttributes().getTotalLoyaltyPointsEarned() >= 500000) {
			unlock(player, LoyaltyTitles.VETERAN);
		}
	}

	private static void randomDeals() {
		int total = 0;
		//Loyalty Titles
		int random = Misc.random(Math.round(LoyaltyTitles.values().length/3));
		for(int t = 0; t < random; t++) {
			LoyaltyTitles title = LoyaltyTitles.values()[Misc.getRandom(LoyaltyTitles.values().length-1)];
			if(title.getCost() == -1 || total >= 12)
				continue;
			int highest = (int) Math.ceil((double) (title.getCost()/1000)/6);
			int discount = (Misc.random(highest * 4) + 1) * 250;
			title.setDiscount(discount);
			total++;
		}

		//Loyalty Items
		random = Misc.random(Math.round(LoyaltyTitles.values().length/3));
		for(int t = 0; t < random; t++) {
			LoyaltyItems item = LoyaltyItems.values()[Misc.getRandom(LoyaltyItems.values().length-1)];
			if(item.getCost() == -1 || total >= 12)
				continue;
			int highest = (int) Math.ceil((double) (item.getCost()/1000)/6);
			int discount = (Misc.random(highest * 4) + 1) * 250;
			item.setDiscount(discount);
			total++;
		}

		//Loyalty Recolours
		random = Misc.random(Math.round(LoyaltyTitles.values().length/3));
		for(int t = 0; t < random; t++) {
			LoyaltyRecolours recolour = LoyaltyRecolours.values()[Misc.getRandom(LoyaltyRecolours.values().length-1)];
			if(recolour.getCost() == -1 || total >= 12)
				continue;
			int highest = (int) Math.ceil((double) (recolour.getCost()/1000)/6);
			int discount = (Misc.random(highest * 4) + 1) * 250;
			recolour.setDiscount(discount);
			total++;
		}
	}

	private static void clearDeals() {
		for(LoyaltyTitles t : LoyaltyTitles.values()) {
			if(t.hasDiscount())
				t.clearDiscount();
		}
		for(LoyaltyItems i : LoyaltyItems.values()) {
			if(i.hasDiscount())
				i.clearDiscount();
		}
		for(LoyaltyRecolours r : LoyaltyRecolours.values()) {
			if(r.hasDiscount())
				r.clearDiscount();
		}
		//Clear offer's arrays
		titleOffers = new LoyaltyTitles[12];
		itemOffers = new LoyaltyItems[12];
		recolourOffers = new LoyaltyRecolours[12];
	}

	private static void liveRefresh() {
		//Effects all players using the interface
		for(Player p : GameWorld.getPlayers()) {
			if(p != null && (p.getInterfaceId() == LoyaltyProgramme.HOME_PAGE
					|| p.getInterfaceId() == LoyaltyProgramme.ITEMS_PAGE
					|| p.getInterfaceId() == LoyaltyProgramme.TITLES_PAGE
					|| p.getInterfaceId() == LoyaltyProgramme.RECOLOUR_PAGE
					|| p.getInterfaceId() == LoyaltyProgramme.OFFERS_PAGE)) {
				//If no offers and on offers page then move to home page
				if(!hasOffers() && p.getInterfaceId() == LoyaltyProgramme.OFFERS_PAGE) {
					p.getPacketSender().sendInterface(HOME_PAGE);
					p.getPacketSender().sendString(43004, "Now Viewing: Home");
				}
				refresh(p);
			}
		}
	}



	public static void init() {
		changeDiscounts();
	}

	public static void changeDiscounts() {
		long startup = System.currentTimeMillis();
		clearDeals();
		randomDeals();
		liveRefresh();
		if(GameSettings.DEBUG)
			System.out.println("Loaded loyalty shop " + (System.currentTimeMillis() - startup) + "ms");
	}

	public static void removeDiscounts() {
		clearDeals();
		liveRefresh();
	}

	/*
	 * Runs every 600ms (1 server tick)
	 * Checks if time to change loyalty shop discounts
	 */
	private static boolean loyaltyHasReset = false;

	public static void sequence() {
		boolean loyaltyReset = Misc.isStartOfDay();
		if(loyaltyReset && !loyaltyHasReset) {
			System.out.println("[World " + new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "] Reset Loyalty Shop Discounts.");
			LoyaltyProgramme.changeDiscounts();
			loyaltyHasReset = true;
		} else if(!loyaltyReset && loyaltyHasReset) {
			loyaltyHasReset = false;
		}
	}

	private static final int HOME_PAGE = 43700;
	private static final int ITEMS_PAGE = 43300;
	private static final int TITLES_PAGE = 43050;
	private static final int RECOLOUR_PAGE = 43520;
	private static final int OFFERS_PAGE = 43730;
}
