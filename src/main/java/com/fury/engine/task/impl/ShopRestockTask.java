package com.fury.engine.task.impl;

import com.fury.core.task.Task;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.core.model.item.Item;

public class ShopRestockTask extends Task {

	public ShopRestockTask(Shop shop) {
		super(false, 15);
		this.shop = shop;
	}

	private final Shop shop;

	@Override
	public void run() {
		if(shop.fullyRestocked() || shop.getId() >= 500) {
			stop();
			return;
		}

		if(shop.getId() != ShopManager.GENERAL_STORE) {
			for(int shopItemIndex = 0; shopItemIndex < shop.getOriginalStock().length; shopItemIndex++) {

				Item item = shop.get(shopItemIndex);

				if(item == null)
					continue;

				int originalStockAmount = shop.getOriginalStock()[shopItemIndex].getAmount();
				int currentStockAmount = item.getAmount();

				if(originalStockAmount > currentStockAmount) {
					shop.add(new Item(item, 1));
				} else if(originalStockAmount == currentStockAmount) {
				} else if(originalStockAmount < currentStockAmount) {
					shop.delete(new Item(item, getDeleteRatio(shop.getItems()[shopItemIndex].getAmount() - shop.getOriginalStock()[shopItemIndex].getAmount())));
				}
				
			}
		} else {
			for(Item it : shop.getItems()) {
				if(it != null) {
					int delete = getDeleteRatio(it.getAmount());
					shop.delete(new Item(it, delete > 1 ? delete : 1));
				}
			}
		}
		shop.publicRefresh();
		shop.refresh();
		if(shop.fullyRestocked())
			stop();
	}

	@Override
	public void onStop() {
		shop.setRestockingItems(false);
	}

	public static int getRestockAmount(int amountMissing) {
		return (int)(Math.pow(amountMissing, 1.2)/30+1);
	}

	public static int getDeleteRatio(int x) {
		return (int)(Math.pow(x, 1.05)/50+1);
	}
}
