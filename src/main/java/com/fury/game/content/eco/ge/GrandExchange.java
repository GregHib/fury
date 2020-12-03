package com.fury.game.content.eco.ge;

import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.game.content.dialogue.input.impl.EnterGePricePerItem;
import com.fury.game.content.dialogue.input.impl.EnterGeQuantity;
import com.fury.game.content.eco.ge.GrandExchangeOffer.OfferType;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.loaders.item.ItemConstants;
import com.fury.game.system.files.loaders.item.ItemExamines;
import com.fury.game.world.World;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.ArrayList;

/**
 * Handles the Grand Exchange system.
 *
 * @author Gabriel Hannason
 */
public class GrandExchange {

    public static boolean handleButton(Player player, int id) {
        if (player.getInterfaceId() == MAIN_INTERFACE) {
            switch (id) {
                case 24505:
                case 24511:
                case 24523:
                case 24526:
                case 24514:
                case 24529:
                case 24508:
                case 24532:
                case 24517:
                case 24535:
                case 24520:
                case 24538:
                    int slot = getSlotForButton(id);
                    if (slot == -1) {
                        return true;
                    }
                    if (player.getGrandExchangeSlots()[slot] == null) {
                        return true;
                    }
                    if (player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.EMPTY) {
                        reset(player);
                        if (id == 24505 || id == 24523 || id == 24514 || id == 24508 || id == 24517 || id == 24520) {
                            updateBuyInterface(player);
                            player.getPacketSender().sendInterface(BUY_INTERFACE);
                        } else {
                            updateSellInterface(player);
                            player.getPacketSender().sendInterface(SELL_INTERFACE);
                        }
                        player.setSelectedGeSlot(slot);
                    }
                    return true;
                case 24541:
                case 24545:
                case 24549:
                case 24553:
                case 24557:
                case 24561:
                    slot = getSlotForButton(id);
                    if (player.getGrandExchangeSlots()[slot] == null || !player.getTimers().getClickDelay().elapsed(1000)) {
                        return true;
                    }
                    if (player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.PENDING_PURCHASE || player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.PENDING_SALE) {
                        final GrandExchangeOffer offer = player.getGrandExchangeSlots()[slot].getOffer();
                        if (offer.getAmountFinished() < offer.getAmount()) {
                            if (offer.getType() == OfferType.BUYING) {
                                offer.setCoinsCollect(offer.getTotalCost() - (offer.getAmountFinished() * offer.getPricePerItem()));
                            } else {
                                offer.setItemCollect(offer.getAmount() - offer.getAmountFinished());
                            }
                            GrandExchangeOffers.setOffer(offer.getIndex(), null);
                            player.getGrandExchangeSlots()[slot].setState(GrandExchangeSlotState.ABORTED);
                            updateSlotStates(player);
                            player.save();
                            player.getTimers().getClickDelay().reset();
                        }
                    }
                    return true;
                case 24543:
                case 24547:
                case 24551:
                case 24555:
                case 24559:
                case 24563:
                    slot = getSlotForButton(id);
                    player.getGrandExchangeSlots()[slot] = player.getGrandExchangeSlots()[slot];
                    if (player.getGrandExchangeSlots()[slot] == null) {
                        return true;
                    }
                    if (player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.PENDING_PURCHASE || player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.PENDING_SALE || player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.ABORTED || player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.FINISHED_PURCHASE || player.getGrandExchangeSlots()[slot].getState() == GrandExchangeSlotState.FINISHED_SALE) {
                        GrandExchangeOffer offer = player.getGrandExchangeSlots()[slot].getOffer();
                        if (offer.getType() == OfferType.BUYING) {
                            updateViewPurchaseInterface(player, slot);
                            player.getPacketSender().sendInterface(VIEW_PURCHASE_INTERFACE);
                        } else if (offer.getType() == OfferType.SELLING) {
                            updateViewSaleInterface(player, slot);
                            player.getPacketSender().sendInterface(VIEW_SALE_INTERFACE);
                        }
                        player.setSelectedGeSlot(slot);
                    }
                    return true;
            }
        } else if (player.getInterfaceId() == BUY_INTERFACE || player.getInterfaceId() == SELL_INTERFACE) {
            if (player.getSelectedGeSlot() >= 0 && player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getState() == GrandExchangeSlotState.EMPTY) {
                switch (id) {
                    case 24606:
                    case 24706:
                        setQuantity(player, player.getGeQuantity() - 1);
                        return true;
                    case 24610:
                    case 24614:
                    case 24710:
                    case 24714:
                        setQuantity(player, player.getGeQuantity() + 1);
                        return true;
                    case 24618:
                    case 24718:
                        setQuantity(player, player.getGeQuantity() + 10);
                        return true;
                    case 24622:
                    case 24722:
                        setQuantity(player, player.getGeQuantity() + 100);
                        return true;
                    case 24626:
                        setQuantity(player, player.getGeQuantity() + 1000);
                        return true;
                    case 24726:
                        if (player.getSelectedGeItem() == null) {
                            player.message("You must choose an item before changing these settings.");
                        } else {
                    /*	int amt = 0;
                        for(Item t : player.getInventory().getValidItems()) {
							if(t.getScrollId() == Item.getNoted(player.getSelectedGeItem()) || t.getScrollId() == Item.getUnNoted(player.getSelectedGeItem())) {
								amt += t.getAmount();
							}
						}
						if(amt > Integer.MAX_VALUE) {
							amt = 0;
						}*/
                            setQuantity(player, player.getInventory().getAmount(player.getSelectedGeItem()));
                        }
                        return true;
                    case 24630:
                    case 24730:
                        player.setInputHandling(new EnterGeQuantity());
                        player.getPacketSender().sendEnterAmountPrompt("Please enter the amount you wish to buy below.");
                        return true;
                    case 24662:
                    case 24762:
                        setPricePerItem(player, player.getGePricePerItem() - 1);
                        return true;
                    case 24665:
                    case 24765:
                        setPricePerItem(player, player.getGePricePerItem() + 1);
                        return true;
                    case 24634:
                    case 24734:
                        setPricePerItem(player, ((int) (player.getGePricePerItem() * 0.95)) == player.getGePricePerItem() ? player.getGePricePerItem() - 1 : (int) (player.getGePricePerItem() * 0.95));
                        return true;
                    case 24646:
                    case 24746:
                        setPricePerItem(player, ((int) (player.getGePricePerItem() * 1.05)) == player.getGePricePerItem() ? player.getGePricePerItem() + 1 : (int) (player.getGePricePerItem() * 1.05));
                        return true;
                    case 24642:
                    case 24742:
                        player.setInputHandling(new EnterGePricePerItem());
                        player.getPacketSender().sendEnterAmountPrompt("Please enter the amount you wish to pay per item.");
                        return true;
                    case 24638:
                    case 24738:
                        if (player.getSelectedGeItem() == null) {
                            player.message("Please select an item first.");
                            return true;
                        }
                        if (ItemConstants.isTradeable(player.getSelectedGeItem())) {
                            setPricePerItem(player, player.getSelectedGeItem().getDefinition().getValue());
                        } else {
                            player.message(FontUtils.imageTags(535) + " This item does not have a base price set.", 0x996633);
                        }
                        return true;
                    case 24602:
                    case 24702:
                        reset(player);
                        player.getPacketSender().sendInterfaceRemoval();
                        return true;
                    case 24658:
                    case 24758:
                        reset(player);
                        player.getPacketSender().sendInterface(MAIN_INTERFACE);
                        return true;
                    case 24650:
                    case 24750:
                        if (player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getOffer() == null && player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getState() == GrandExchangeSlotState.EMPTY) {
                            OfferType offerType = id == 24650 ? OfferType.BUYING : OfferType.SELLING;
                            if (player.getGeQuantity() <= 0) {
                                player.message("Please select a proper quantity before confirming your offer.");
                                return true;
                            }
                            if (player.getGePricePerItem() <= 0) {
                                player.message("Please select a proper price per item before confirming your offer.");
                                return true;
                            }
                            if (!player.getTimers().getClickDelay().elapsed(1000)) {
                                return true;
                            }
                            for (GrandExchangeOffer offer : getOffers(player)) {
                                if (offer == null)
                                    continue;
                                Item item = Item.Companion.getUnNoted(player.getSelectedGeItem());
                                Item item2 = Item.Companion.getUnNoted(offer);
                                if (item2.isEqual(item)) {
                                    player.message("You already have an offer set for this item.");
                                    return true;
                                }
                            }
                            int index = GrandExchangeOffers.findIndex();
                            if (index == -1) {
                                player.message("The Grand Exchange is currently too busy to accept your offer.");
                                return true;
                            }
                            Item item = player.getSelectedGeItem();
                            if (offerType == OfferType.BUYING) {
                                int cost = calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity());
                                if (cost <= 0 || cost > Integer.MAX_VALUE) {
                                    player.message("Invalid cost.");
                                    return true;
                                }
                                boolean usePouch = player.getMoneyPouch().getTotal() >= cost;
                                if (!usePouch) {
                                    if (player.getInventory().getAmount(new Item(995)) < cost) {
                                        player.message("You do not have enough coins to make this purchase.");
                                        return true;
                                    }
                                    player.getInventory().delete(new Item(995, cost));
                                } else {
                                    player.message("" + Misc.insertCommasToNumber("" + cost + "") + " coins have been taken from your money pouch.");
                                    player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - cost);
                                    player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal());
                                }
                            } else if (offerType == OfferType.SELLING) {
                                int cost = calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity());
                                if (cost <= 0 || cost > Integer.MAX_VALUE) {
                                    player.message("Invalid cost.");
                                    return true;
                                }
                                if (player.getInventory().getAmount(player.getSelectedGeItem()) < player.getGeQuantity()) {
                                    player.message("You do not have that quantity of the item in your inventory.");
                                    return true;
                                }
                                ItemDefinition def = item.getDefinition();
                                if (!def.isNoted() && !def.isStackable() && player.getGeQuantity() > 1) {
                                    item = Item.Companion.getNoted(item);
                                } else if (def.isNoted() && player.getGeQuantity() == 1) {
                                    item = Item.Companion.getUnNoted(item);
                                }
                                player.getInventory().delete(new Item(player.getSelectedGeItem(), player.getGeQuantity()));
                            }
                            if (GrandExchangeOffers.getOffer(index) != null) { // double check
                                return true;
                            }
                            player.getGrandExchangeSlots()[player.getSelectedGeSlot()].setOffer(new GrandExchangeOffer(item, player.getGeQuantity(), player.getUsername(), index, player.getGePricePerItem(), player.getSelectedGeSlot(), offerType));
                            player.getGrandExchangeSlots()[player.getSelectedGeSlot()].setState(id == 24650 ? GrandExchangeSlotState.PENDING_PURCHASE : GrandExchangeSlotState.PENDING_SALE);
                            GrandExchangeOffers.add(player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getOffer());
                            player.getTimers().getClickDelay().reset();
                            open(player);
                        }
                        return true;
                }
            }
        } else if (player.getInterfaceId() == VIEW_SALE_INTERFACE || player.getInterfaceId() == VIEW_PURCHASE_INTERFACE) {
            switch (id) {
                case 54758:
                case 53758:
                    open(player);
                    return true;
                case 54800:
                case 53800:
                    if (player.getSelectedGeSlot() == -1 || player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getOffer() == null || !player.getTimers().getClickDelay().elapsed(1000)) {
                        return true;
                    }
                    if (player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getState() == GrandExchangeSlotState.PENDING_PURCHASE || player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getState() == GrandExchangeSlotState.PENDING_SALE) {
                        final GrandExchangeOffer offer = player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getOffer();
                        if (offer.getAmountFinished() < offer.getAmount()) {
                            if (offer.getType() == OfferType.BUYING) {
                                offer.setCoinsCollect(offer.getTotalCost() - (offer.getAmountFinished() * offer.getPricePerItem()));
                            } else {
                                offer.setItemCollect(offer.getAmount() - offer.getAmountFinished());
                            }
                            GrandExchangeOffers.setOffer(offer.getIndex(), null);
                            player.getGrandExchangeSlots()[player.getSelectedGeSlot()].setState(GrandExchangeSlotState.ABORTED);
                            updateSlotStates(player);
                            if (offer.getType() == OfferType.BUYING) {
                                updateViewPurchaseInterface(player, player.getSelectedGeSlot());
                            } else if (offer.getType() == OfferType.SELLING) {
                                updateViewSaleInterface(player, player.getSelectedGeSlot());
                            }
                            player.save();
                        }
                        player.getTimers().getClickDelay().reset();
                    }
                    return true;
                case 54702:
                case 53702:
                    reset(player);
                    player.getPacketSender().sendInterfaceRemoval();
                    return true;
            }
        }
        return false;
    }

    public static void open(Player player) {
        if (player.getGameMode().isIronMan()) {
            player.message("You cannot use the Grand Exchange.");
            return;
        }
        updateSlotStates(player);
        player.getPacketSender().sendInterface(MAIN_INTERFACE);
    }

    public static void updateSlotStates(Player player) {
        for (int i = 0; i < player.getGrandExchangeSlots().length; i++) {

            if (player.getGrandExchangeSlots()[i] == null) {
                player.setGrandExchangeSlot(i, new GrandExchangeSlot());
            }

            GrandExchangeSlot slot = player.getGrandExchangeSlots()[i];

            if (slot.getState() == null) {
                slot.setState(GrandExchangeSlotState.EMPTY);
            }

            switch (slot.getState()) {
                case EMPTY:
                    slot.getState().update(player, i + 1, -1, -1, null);
                    break;
                case PENDING_PURCHASE:
                    slot.getState().update(player, i + 1, 2, slot.getOffer().getPercent(), slot.getOffer());
                    break;
                case FINISHED_PURCHASE:
                    slot.getState().update(player, i + 1, 3, 100, slot.getOffer());
                    break;
                case PENDING_SALE:
                    slot.getState().update(player, i + 1, 2, slot.getOffer().getPercent(), slot.getOffer());
                    break;
                case FINISHED_SALE:
                    slot.getState().update(player, i + 1, 3, 100, slot.getOffer());
                    break;
                case ABORTED:
                    if (slot.getOffer().getType() == OfferType.SELLING) {
                        GrandExchangeSlotState.PENDING_SALE.update(player, i + 1, 2, -1, slot.getOffer());
                    } else if (slot.getOffer().getType() == OfferType.BUYING) {
                        GrandExchangeSlotState.PENDING_PURCHASE.update(player, i + 1, 2, -1, slot.getOffer());
                    }
                    slot.getState().update(player, i + 1, -1, -1, slot.getOffer());
                    break;
            }
        }
    }

    public static boolean updateState(GrandExchangeOffer offer, GrandExchangeSlotState state) {
        Player p = World.getPlayerByName(offer.getOwner());
        if (p != null) {
            p.message("One or more of your Grand Exchange offers have been updated.");
            p.getGrandExchangeSlots()[offer.getBox()].setOffer(offer);
            p.getGrandExchangeSlots()[offer.getBox()].setState(state);
            updateSlotStates(p);
            if (state == GrandExchangeSlotState.FINISHED_SALE)
                Achievements.finishAchievement(p, Achievements.AchievementData.SELL_AN_ITEM_GE);
            if (p.getInterfaceId() == VIEW_PURCHASE_INTERFACE) {
                updateViewPurchaseInterface(p, offer.getBox());
            } else if (p.getInterfaceId() == VIEW_SALE_INTERFACE) {
                updateViewSaleInterface(p, offer.getBox());
            }
            return true;
        } else {
            offer.setUpdateState(state);
        }
        return false;
    }

    public static void onLogin(Player player) {
        boolean messageNeeded = false;
        ArrayList<GrandExchangeOffer> offers = getOffers(player);
        for (GrandExchangeOffer o : offers) {
            GrandExchangeOffer o2 = GrandExchangeOffers.getOffer(o.getIndex());
            if (o2 == null || !(o.getId() == o2.getId() && o.getAmount() == o2.getAmount() && o.getBox() == o2.getBox() && o.getType() == o2.getType() && o.getOwner().equals(o2.getOwner()))) {
                continue;
            }
            if (o2.getAmountFinished() != o.getAmountFinished() || o2.getCoinsCollect() != o.getCoinsCollect() || o2.getItemCollect() != o.getItemCollect()) {
                messageNeeded = true;
            }
            player.getGrandExchangeSlots()[o2.getBox()].setOffer(o2);
			
			if(o2.getUpdateState() != null) {
				player.getGrandExchangeSlots()[o2.getBox()].setState(o2.getUpdateState());
				o2.setUpdateState(null);
				if(o2.getUpdateState() == GrandExchangeSlotState.FINISHED_PURCHASE || o2.getUpdateState() == GrandExchangeSlotState.FINISHED_SALE) {
					GrandExchangeOffers.setOffer(o2.getIndex(), null);
				}
				if(o2.getFailAttempts() >= 3) {
//					player.essage("<img=535> <col=996633>Perhaps you should try lowering the price on your "+ItemDefinition.forId(o2.getScrollId()).getName()+" offer").sendMessage("<col=996633>in the Grand Exchange. People are currently paying less for that item.");
					o2.setFailAttempts(0);
				}
			}
        }
        updateSlotStates(player);
        if (messageNeeded) {
            player.message("One or more of your Grand Exchange offers have been updated.");
        }
    }

    public static void setSelectedItem(Player player, Item item) {
        player.setSelectedGeItem(item);
        if (ItemConstants.isTradeable(item)) {
            player.setGePricePerItem(item.getDefinition().getValue());
        }
        player.setGeQuantity(1);
        if (player.getInterfaceId() == BUY_INTERFACE) {
            int cheapest = GrandExchangeOffers.getGoodOffer(item, OfferType.BUYING);
            player.message(cheapest == -1 ? FontUtils.imageTags(535) + " Note: There are currently no players selling that item." : FontUtils.imageTags(535) + " The cheapest offer found is currently selling at: " + Misc.insertCommasToNumber(cheapest) + " gp per item.", 0x996633);
            if (cheapest >= 0 && player.getGePricePerItem() > cheapest) {
                player.setGePricePerItem(cheapest);
            }
            updateBuyInterface(player);
        } else if (player.getInterfaceId() == SELL_INTERFACE) {
            if (item.getDefinition().isStackable()) {
                player.setGeQuantity(player.getInventory().getAmount(item));
            }
            int mostExpensive = GrandExchangeOffers.getGoodOffer(item, OfferType.SELLING);
            player.message(mostExpensive == -1 ? FontUtils.imageTags(535) + " Note: There are currently no players buying that item." : FontUtils.imageTags(535) + " The most expensive offer found is currently buying at: " + Misc.insertCommasToNumber(mostExpensive) + " gp per item.", 0x996633);
            if (mostExpensive >= 0 && player.getGePricePerItem() < mostExpensive) {
                player.setGePricePerItem(mostExpensive);
            }
            updateSellInterface(player);
        } else {
            player.getPacketSender().sendInterfaceRemoval();
            reset(player);
        }
    }

    public static void setQuantity(Player player, int amount) {
        if (player.getSelectedGeItem() == null) {
            player.message("You must choose an item before changing these settings.");
            return;
        }

        if (amount < 0) {
            amount = 0;
        }

        player.setGeQuantity(amount);

        if (player.getInterfaceId() == BUY_INTERFACE) {
            updateBuyInterface(player);
        } else if (player.getInterfaceId() == SELL_INTERFACE) {
            updateSellInterface(player);
        }
    }

    public static void setPricePerItem(Player player, int pricePerItem) {
        if (player.getSelectedGeItem() == null) {
            player.message("You must choose an item before changing these settings.");
            return;
        }

        if (pricePerItem < 0) {
            pricePerItem = 0;
        }

        player.setGePricePerItem(pricePerItem);

        if (player.getInterfaceId() == BUY_INTERFACE) {
            updateBuyInterface(player);
        } else if (player.getInterfaceId() == SELL_INTERFACE) {
            updateSellInterface(player);
        }
    }

    public static void updateBuyInterface(Player player) {
        player.getPacketSender().
                sendString(24671, "" + Misc.insertCommasToNumber("" + player.getGeQuantity() + "") + ""). //Quantity
                sendString(24672, "" + Misc.insertCommasToNumber("" + player.getGePricePerItem() + "") + " gp"). // Price per item
                sendString(24673, calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity()) == -1 ? ("Too high!") : Misc.insertCommasToNumber("" + calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity())) + " gp"); // Total amount
        if (player.getSelectedGeItem() == null) {
            player.getPacketSender().
                    sendItemOnWidget(24680, null).
                    sendString(24669, "Choose an item to exchange").
                    sendString(24670, "Click the box to the left to search for items.");
        } else {
            ItemDefinition def = player.getSelectedGeItem().getDefinition();
            player.getPacketSender().sendItemOnWidget(24680, player.getSelectedGeItem()).
                    sendString(24669, def.getName()).
                    sendString(24670, ItemExamines.getExamine(player.getSelectedGeItem()));
        }
    }

    public static void updateViewPurchaseInterface(Player player, int boxIndex) {
        player.getPacketSender().sendGrandExchangeUpdate("slotselected <" + (boxIndex + 1) + ">");
        GrandExchangeSlot geSlot = player.getGrandExchangeSlots()[boxIndex];
        ItemDefinition def = geSlot.getOffer().getDefinition();
        player.getPacketSender().
                sendItemOnWidget(53780, new Item(geSlot.getOffer().getId())).
                sendItemOnWidget(53781, null).
                sendItemOnWidget(53782, null).
                sendString(53769, def.getName()).
                sendString(53770, ItemExamines.getExamine(new Item(geSlot.getOffer().getId()))).
                sendString(53771, geSlot.getOffer().getAmountFinished() > 0 ? ((geSlot.getOffer().getAmountFinished() >= geSlot.getOffer().getAmount() ? FontUtils.GREEN : FontUtils.YELLOW) + Misc.insertCommasToNumber("" + geSlot.getOffer().getAmountFinished() + "") + " / " + Misc.insertCommasToNumber("" + geSlot.getOffer().getAmount() + "") + FontUtils.COL_END) : (Misc.insertCommasToNumber("" + geSlot.getOffer().getAmount() + ""))).
                sendString(53772, "" + Misc.insertCommasToNumber("" + geSlot.getOffer().getPricePerItem() + "") + " gp").
                sendString(53773, "" + Misc.insertCommasToNumber("" + geSlot.getOffer().getTotalCost() + "") + " gp");
        if (geSlot.getState() == GrandExchangeSlotState.PENDING_PURCHASE || geSlot.getState() == GrandExchangeSlotState.FINISHED_PURCHASE || geSlot.getState() == GrandExchangeSlotState.ABORTED) {
            if (geSlot.getOffer().getAmountFinished() == 0) {
                if (geSlot.getOffer().getCoinsCollect() > 0) {
                    player.getPacketSender().sendItemOnWidget(53781, new Item(995, geSlot.getOffer().getTotalCost()));
                }
            } else {
                if (geSlot.getOffer().getItemCollect() > 0) {
                    Item item = geSlot.getOffer();
                    if (item.getAmount() > 1 && !item.getDefinition().isStackable() && !item.getDefinition().isNoted()) {
                        if (!def.isNoted() && !def.isStackable()) {
                            item = Item.Companion.getNoted(item);
                        } else if (def.isNoted()) {
                            item = Item.Companion.getUnNoted(item);
                        }
                    }
                    player.getPacketSender().sendItemOnWidget(53781, item);
                    if (geSlot.getOffer().getCoinsCollect() > 0) {
                        player.getPacketSender().sendItemOnWidget(53782, new Item(995, geSlot.getOffer().getCoinsCollect()));
                    }
                } else {
                    if (geSlot.getOffer().getCoinsCollect() > 0) {
                        player.getPacketSender().sendItemOnWidget(53781, new Item(995, geSlot.getOffer().getCoinsCollect()));
                    }
                }
            }
        }
    }

    public static void updateSellInterface(Player player) {
        player.getPacketSender().
                sendString(24771, "" + Misc.insertCommasToNumber("" + player.getGeQuantity() + "") + ""). //Quantity
                sendString(24772, "" + Misc.insertCommasToNumber("" + player.getGePricePerItem() + "") + " gp"). // Price per item
                sendString(24773, calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity()) == -1 ? ("Too high!") : Misc.insertCommasToNumber("" + calculateTotalCost(player.getGePricePerItem(), player.getGeQuantity())) + " gp"); // Total amount
        if (player.getSelectedGeItem() == null) {
            player.getPacketSender().
                    sendItemOnWidget(24780, null).
                    sendString(24769, "Choose an item to exchange").
                    sendString(24770, "Select an item from your inventory to sell.");
        } else {
            ItemDefinition def = player.getSelectedGeItem().getDefinition();
            Item item = player.getSelectedGeItem();
            if (def.isNoted())
                item = Item.Companion.getUnNoted(item);

            player.getPacketSender().
                    sendItemOnWidget(24780, item).
                    sendString(24769, def.getName()).
                    sendString(24770, ItemExamines.getExamine(item));
        }
    }

    public static void updateViewSaleInterface(Player player, int boxIndex) {
        player.getPacketSender().sendGrandExchangeUpdate("slotselected <" + (boxIndex + 1) + ">");
        GrandExchangeOffer offer = player.getGrandExchangeSlots()[boxIndex].getOffer();
        ItemDefinition def = Loader.getItem(offer.getId());
        Item item = offer;
        if (def.isNoted()) {
            item = Item.Companion.getUnNoted(item);
        }
        player.getPacketSender().
                sendItemOnWidget(54780, item).
                sendItemOnWidget(54781, null).
                sendItemOnWidget(54782, null).
                sendString(54769, def.getName()).
                sendString(54770, ItemExamines.getExamine(new Item(offer.getId()))).
                sendString(54771, offer.getAmountFinished() > 0 ? ((offer.getAmountFinished() >= offer.getAmount() ? FontUtils.GREEN : FontUtils.YELLOW) + Misc.insertCommasToNumber("" + offer.getAmountFinished() + "") + " / " + Misc.insertCommasToNumber("" + offer.getAmount() + "") + FontUtils.COL_END) : (Misc.insertCommasToNumber("" + offer.getAmount() + ""))).
                sendString(54772, "" + Misc.insertCommasToNumber("" + offer.getPricePerItem() + "") + " gp").
                sendString(54773, "" + Misc.insertCommasToNumber("" + offer.getTotalCost() + "") + " gp");
        if (player.getGrandExchangeSlots()[boxIndex].getState() == GrandExchangeSlotState.PENDING_SALE || player.getGrandExchangeSlots()[boxIndex].getState() == GrandExchangeSlotState.FINISHED_SALE || player.getGrandExchangeSlots()[boxIndex].getState() == GrandExchangeSlotState.ABORTED) {
            if (offer.getAmountFinished() == 0) {
                if (offer.getItemCollect() > 0) {
                    player.getPacketSender().sendItemOnWidget(54781, new Item(offer.getId(), offer.getItemCollect()));
                }
            } else {
                if (offer.getItemCollect() > 0) {
                    player.getPacketSender().sendItemOnWidget(54781, new Item(offer.getId(), offer.getItemCollect()));
                    if (offer.getCoinsCollect() > 0) {
                        player.getPacketSender().sendItemOnWidget(54782, new Item(995, offer.getCoinsCollect()));
                    }
                } else {
                    if (offer.getCoinsCollect() > 0) {
                        player.getPacketSender().sendItemOnWidget(54781, new Item(995, offer.getCoinsCollect()));
                    }
                }
            }
        }
    }

    private static boolean canCollect(Player player, GrandExchangeOffer offer, OfferType type, Item returned) {
        boolean selling = offer.getAmountFinished() == 0;
        int sellAmount = type == OfferType.SELLING ? offer.getAmount() : offer.getTotalCost();
        int collectAmount = offer.getItemCollect();
        if(returned.getId() == 995) {
            if(player.getInventory().contains(new Item(995)))
                return true;
            return player.getInventory().getSpaces() >= 1;
        } else {
            if(returned.getDefinition().isStackable())
                return player.getInventory().contains(returned) || player.getInventory().getSpaces() >= 1;

            if(selling)
                return player.getInventory().getSpaces() >= sellAmount;
            return player.getInventory().getSpaces() >= collectAmount;
        }
    }

    public static void collectItem(Player player, int id, int itemBox, OfferType type) {
        if (player.getInterfaceId() != VIEW_PURCHASE_INTERFACE && player.getInterfaceId() != VIEW_SALE_INTERFACE) {
            return;
        }
        if (!player.getTimers().getClickDelay().elapsed(500)) {
            return;
        }
        int slot = player.getSelectedGeSlot();
        if (slot == -1) {
            return;
        }
        GrandExchangeSlotState state = player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getState();
        if (state == GrandExchangeSlotState.EMPTY) {
            return;
        }
        GrandExchangeOffer offer = player.getGrandExchangeSlots()[player.getSelectedGeSlot()].getOffer();
        if (offer.getType() != type) {
            return;
        }
        Item proper = offer;
        if (offer.getAmount() > 1 && !offer.getDefinition().isStackable() && !offer.getDefinition().isNoted()) {
            proper = Item.Companion.getNoted(proper);
        }
        Item returned = null;
        if (offer.getAmountFinished() == 0) {
            if (itemBox == 0) {
                returned = type == OfferType.BUYING ? new Item(995) : proper;
            }
        } else {
            if (offer.getItemCollect() > 0) {
                if (itemBox == 0) {
                    returned = proper;
                }
                if (itemBox == 1) {
                    returned = new Item(995);
                }
            } else {
                if (itemBox == 0) {
                    returned = new Item(995);
                }
            }
        }
        if (offer.getItemCollect() <= 0 && offer.getCoinsCollect() <= 0 || returned == null || returned.getId() != id) {
            return;
        }
        boolean reset = false;
        switch (type) {
            case SELLING:
            case BUYING:
                if (!canCollect(player, offer, type, returned)) {
                    player.getInventory().full();
                   // return;
                }

                if (offer.getAmountFinished() == 0) {
                    player.getInventory().addSafe(new Item(returned, type == OfferType.SELLING ? offer.getAmount() : offer.getTotalCost()));
                    offer.setItemCollect(0);
                    offer.setCoinsCollect(0);
                    reset = true;
                } else {
                    player.getInventory().addSafe(new Item(returned, returned.getId() == 995 ? offer.getCoinsCollect() : offer.getItemCollect()));
                }
                if (returned.getId() == 995) {
                    offer.setCoinsCollect(0);
                } else {
                    offer.setItemCollect(0);
                }
                if (type == OfferType.SELLING) {
                    updateViewSaleInterface(player, slot);
                } else if (type == OfferType.BUYING) {
                    updateViewPurchaseInterface(player, slot);
                }
                if (offer.getItemCollect() <= 0 && offer.getCoinsCollect() <= 0) {
                    if (offer.getAmountFinished() >= offer.getAmount() || state == GrandExchangeSlotState.ABORTED) {
                        reset = true;
                    }
                }
                break;
        }
        if (reset) {
            GrandExchangeOffers.setOffer(offer.getIndex(), null);
            player.getGrandExchangeSlots()[player.getSelectedGeSlot()].setOffer(null);
            player.getGrandExchangeSlots()[player.getSelectedGeSlot()].setState(GrandExchangeSlotState.EMPTY);
            open(player);
        }
        player.getTimers().getClickDelay().reset();
    }

    public static void reset(Player player) {
        player.setGePricePerItem(0);
        player.setGeQuantity(0);
        player.setSelectedGeItem(null);
        player.setSelectedGeSlot(0);
    }

    public static int getSlotForButton(int id) {
        int slot = -1;
        switch (id) {
            case 24541:
            case 24543:
            case 24505:
            case 24511:
                slot = 0;
                break;
            case 24545:
            case 24547:
            case 24523:
            case 24526:
                slot = 1;
                break;
            case 24549:
            case 24551:
            case 24514:
            case 24529:
                slot = 2;
                break;
            case 24553:
            case 24555:
            case 24508:
            case 24532:
                slot = 3;
                break;
            case 24557:
            case 24559:
            case 24517:
            case 24535:
                slot = 4;
                break;
            case 24561:
            case 24563:
            case 24520:
            case 24538:
                slot = 5;
                break;
        }
        return slot;
    }

    public static final ArrayList<GrandExchangeOffer> getOffers(Player player) {
        ArrayList<GrandExchangeOffer> list = new ArrayList<GrandExchangeOffer>();
        for (int i = 0; i < player.getGrandExchangeSlots().length; i++) {
            GrandExchangeSlot slot = player.getGrandExchangeSlots()[i];
            if (slot == null)
                continue;
            if (slot.getOffer() == null)
                continue;
            list.add(slot.getOffer());
        }
        return list;
    }

    public static int calculateTotalCost(int cost, int amount) {
        if (amount > 0 ? cost > Integer.MAX_VALUE / amount || cost < Integer.MIN_VALUE / amount : (amount < -1 ? cost > Integer.MIN_VALUE / amount || cost < Integer.MAX_VALUE / amount : amount == -1 && cost == Integer.MIN_VALUE)) {
            return -1;
        }
        return cost * amount;
    }

    private static final int MAIN_INTERFACE = 24500;
    private static final int BUY_INTERFACE = 24600;
    private static final int SELL_INTERFACE = 24700;
    private static final int VIEW_PURCHASE_INTERFACE = 53700;
    private static final int VIEW_SALE_INTERFACE = 54700;

    public static final int COLLECT_ITEM_PURCHASE_INTERFACE = 2901;
    public static final int COLLECT_ITEM_SALE_INTERFACE = 2902;
}
