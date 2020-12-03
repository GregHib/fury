package com.fury.game.content.eco.ge;

import com.fury.Main;
import com.fury.game.GameSettings;
import com.fury.game.content.eco.ge.GrandExchangeOffer.OfferType;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.game.world.World;
import com.fury.util.FontUtils;

import java.io.*;

public class GrandExchangeOffers {

	private static final GrandExchangeOffer[] OFFERS = new GrandExchangeOffer[5000];

	public static void init() {
		long startup = System.currentTimeMillis();
		try {
			File file = new File(Resources.getSaveFile("offers"));
			if (!file.exists()) {
				file.createNewFile();
				return;
			}

			if(file.length() == 0)
			    return;

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int count = in.readInt();
			if (count > 0) {
				for (int i = 0; i < count; i++) {
					int id = in.readInt();
					int quantity = in.readInt();
					String listedBy = in.readUTF();
					int arrayIndex = in.readInt();
					int pricePerItem = in.readInt();
					int box = in.readInt();
					int amountFinished = in.readInt();
					int coinsCollect = in.readInt();
					int itemCollect = in.readInt();
					int failedAttempts = in.readInt();
					int updateStateOrdinal = in.readInt();
					OfferType type = OfferType.valueOf(in.readUTF());
//					System.out.println(id + " " + quantity + " " + listedBy);
					if(id >= 0)
						OFFERS[arrayIndex] = new GrandExchangeOffer(id, quantity, listedBy, arrayIndex, pricePerItem, box, amountFinished, coinsCollect, itemCollect, failedAttempts, type, updateStateOrdinal);
				}
			}

			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if(GameSettings.DEBUG)
			System.out.println("Loaded grand exchange " + (System.currentTimeMillis() - startup) + "ms");
	}

	public static void add(GrandExchangeOffer offer) {
		setOffer(offer.getIndex(), offer);
		Main.getLoader().getEngine().submit(() -> {
			for(GrandExchangeOffer o : OFFERS) {
				if(o == null || o == offer)
					continue;

				Item item1 = offer;
				if(offer.getDefinition().isNoted()) {
					item1 = Item.Companion.getUnNoted(item1);
				}

				Item item2 = o;
				if(o.getDefinition().isNoted()) {
					item2 = Item.Companion.getUnNoted(item2);
				}

				if(item1.isEqual(item2)) {
					if(o.getType() == OfferType.SELLING && offer.getType() == OfferType.BUYING) {
						if(o.getPricePerItem() > offer.getPricePerItem() && o.getAmountFinished() < o.getAmount()) {
							o.incrementFailAttempts();
							if(o.getFailAttempts() >= 3) {
								Player p = World.getPlayerByName(o.getOwner());
								if(p != null) {
									p.message("");
									p.message(FontUtils.imageTags(535) + " Perhaps you should try lowering the price on your " + item2.getName() + " offer", 0x996633);
									p.message("in the Grand Exchange. People are currently paying less for that item.", 0x996633);
									o.setFailAttempts(0);
								}
							}
							continue;
						}
					}

					int remainingOfferAmount = offer.getAmount() - offer.getAmountFinished();

					if(offer.getType() == OfferType.BUYING && o.getType() == OfferType.SELLING) {
						final int difference = offer.getPricePerItem() - o.getPricePerItem();
						if(difference >= 0) {
							boolean updatePlrs = false;

							/** Handle the actual purchase **/
							for(int i = 0; i <= remainingOfferAmount; i++) {
								if(o.getAmountFinished() >= o.getAmount() || offer.getAmountFinished() >= offer.getAmount()) {
									break;
								}
								o.incrementAmountFinished(1);
								o.incrementCoinsCollect(o.getPricePerItem());
								offer.incrementAmountFinished(1);
								offer.incrementCoinsCollect(difference);
								offer.incrementItemCollect(1);
								updatePlrs = true;
							}

							/** Handle the update/notification for Player 2 **/
							if(o.getAmountFinished() >= o.getAmount()) {
								o.setAmountFinished(o.getAmount());
								if(GrandExchange.updateState(o, GrandExchangeSlotState.FINISHED_SALE)) {
									GrandExchangeOffers.setOffer(o.getIndex(), null);
								}
							} else {
								if(updatePlrs) {
									GrandExchange.updateState(o, GrandExchangeSlotState.PENDING_SALE);
								}
							}

							/** Handle the update/notification for Player 1**/
							if(offer.getAmountFinished() >= offer.getAmount()) {
								offer.setAmountFinished(offer.getAmount());
								if(GrandExchange.updateState(offer, GrandExchangeSlotState.FINISHED_PURCHASE)) {
									GrandExchangeOffers.setOffer(offer.getIndex(), null);
								}
							} else {
								if(updatePlrs) {
									GrandExchange.updateState(offer, GrandExchangeSlotState.PENDING_PURCHASE);
								}
							}

						}

					} else if(offer.getType() == OfferType.SELLING && o.getType() == OfferType.BUYING) {
						final int difference = o.getPricePerItem() - offer.getPricePerItem();
						if(difference >= 0) {
							boolean updatePlrs = false;

							/** Handle the actual purchase **/
							for(int i = 0; i <= remainingOfferAmount; i++) {
								if(o.getAmountFinished() >= o.getAmount() || offer.getAmountFinished() >= offer.getAmount()) {
									break;
								}
								offer.incrementAmountFinished(1);
								offer.incrementCoinsCollect(offer.getPricePerItem());
								o.incrementAmountFinished(1);
								o.incrementCoinsCollect(difference);
								o.incrementItemCollect(1);
								updatePlrs = true;
							}

							/** Handle the update/notification for Player 2 **/
							if(offer.getAmountFinished() >= offer.getAmount()) {
								offer.setAmountFinished(offer.getAmount());
								if(GrandExchange.updateState(offer, GrandExchangeSlotState.FINISHED_SALE)) {
									GrandExchangeOffers.setOffer(offer.getIndex(), null);
								}
							} else {
								if(updatePlrs) {
									GrandExchange.updateState(offer, GrandExchangeSlotState.PENDING_SALE);
								}
							}

							/** Handle the update/notification for Player 1**/
							if(o.getAmountFinished() >= o.getAmount()) {
								o.setAmountFinished(o.getAmount());
								if(GrandExchange.updateState(o, GrandExchangeSlotState.FINISHED_PURCHASE)) {
									GrandExchangeOffers.setOffer(o.getIndex(), null);
								}
							} else {
								if(updatePlrs) {
									GrandExchange.updateState(o, GrandExchangeSlotState.PENDING_PURCHASE);
								}
							}
						}
					}
				}
			}
		});
	}

	public static int getGoodOffer(Item item, OfferType type) {
		int k = type == OfferType.BUYING ? Integer.MAX_VALUE : -1;
		for(GrandExchangeOffer o : OFFERS) {
			if(o == null || o.getType() == type || o.getAmountFinished() >= o.getAmount())
				continue;
			if(item.getDefinition().isNoted()) {
				item = Item.Companion.getUnNoted(item);
			}
			Item offerItem = o;
			if(o.getDefinition().isNoted()) {
				offerItem = Item.Companion.getUnNoted(offerItem);
			}
			if(item.isEqual(offerItem)) {
				if(type == OfferType.BUYING) {
					if(o.getPricePerItem() < k) {
						k = o.getPricePerItem();
					}
				} else if(type == OfferType.SELLING) {
					if(o.getPricePerItem() > k) {
						k = o.getPricePerItem();
					}
				}
			}
		}
		return k == Integer.MAX_VALUE ? -1 : k;
	}

	public static void save() {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getSaveFile("offers"), false));
			out.writeInt(getCount());
			for (GrandExchangeOffer l : OFFERS) {
				if(l == null) {
					continue;
				}
				l.save(out);
			}
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static int findIndex() {
		for(int i = 0; i < OFFERS.length; i++) {
			if(OFFERS[i] == null) {
				return i;
			}
		}
		return -1;
	}

	private static int getCount() {
		int count = 0;
		for(int i = 0; i < OFFERS.length; i++) {
			if(OFFERS[i] == null) {
				continue;
			}
			count++;
		}
		return count;
	}

	public static GrandExchangeOffer getOffer(int index) {
		return OFFERS[index];
	}

	public static void setOffer(int index, GrandExchangeOffer offer) {
		OFFERS[index] = offer;
	}
}
