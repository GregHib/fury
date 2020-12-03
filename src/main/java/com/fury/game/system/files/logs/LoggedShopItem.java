package com.fury.game.system.files.logs;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;

public class LoggedShopItem extends LoggedItem {
    int shop;
    long price;

    public LoggedShopItem(Item item, int shopId, long price) {
        super(item);
        shop = shopId;
        this.price = price;
    }

    public LoggedShopItem(int id, long timestamp, int amount, Revision revision, int shop, long price) {
        super(id, timestamp, amount, revision);
        this.shop = shop;
        this.price = price;
    }

    public LoggedShopItem(int id, long timestamp, int amount, int shop, long price) {
        super(id, timestamp, amount);
        this.shop = shop;
        this.price = price;
    }

    public int getShop() {
        return shop;
    }

    public long getPrice() {
        return price;
    }
}
