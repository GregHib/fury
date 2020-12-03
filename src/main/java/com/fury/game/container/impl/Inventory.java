package com.fury.game.container.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.bank.BankTab;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.types.StackContainer;
import com.fury.core.model.item.Item;
import com.fury.game.world.FloorItemManager;
import com.fury.util.Misc;

public class Inventory extends StackContainer {

    public static final int INTERFACE_ID = 3214;
    private static Item COINS = new Item(995);

    public Inventory(Player player, Inventory inventory) {
        super(player, inventory);
    }

    public Inventory(Player player) {
        super(player, 28);
    }

    @Override
    public boolean add(Item item) {
        boolean add = super.add(item);
        if(add)
            refresh();
        return add;
    }

    @Override
    public boolean set(Item item, int index) {
        boolean set = super.set(item, index);
        if(set)
            refresh();
        return set;
    }

    @Override
    public boolean delete(int index) {
        boolean delete = super.delete(index);
        if(delete)
            refresh();
        return delete;
    }

    public Item get(Item item) {
        return get(indexOf(item));
    }

    public boolean add(Item... items) {
        boolean success = false;
        for(Item item : items)
            success |= super.add(item);
        refresh();
        return success;
    }

    public boolean delete(Item... items) {
        boolean success = false;
        for(Item item : items)
            success |= super.delete(item);
        refresh();
        return success;
    }

    public boolean contains(Item... items) {
        for(Item item : items)
            if(item != null && !super.contains(item))
                return false;
        return true;
    }

    public boolean containsAmount(Item... items) {
        for(Item item : items)
            if(item != null && getAmount(item) < item.getAmount())
                return false;
        return true;
    }

    public boolean containsAmountNonNull(Item... items) {
        for(Item item : items) {
            if(item == null)
                continue;

            if (getAmount(item) < item.getAmount())
                return false;
        }
        return true;
    }

    public boolean handleItemClick(int id, int slot) {
        if (validate(id, slot))
            return player.getEquipment().handleEquip(get(slot));
        return false;
    }

    public boolean use(Item with, Item on) {
        //TODO check both items exist where they say they do?
        return true;
    }

    @Override
    public void refresh() {
        player.getPacketSender().sendItemContainer(this, 3214);
    }

    @Override
    public String getFullMessage() {
        return player.getBank().banking() ? "You don't have enough inventory space to withdraw that many." : "You don't have enough space in your inventory.";
    }

    @Override
    public boolean move(Item item, StackContainer to) {
        if(to instanceof BankTab || to instanceof Shop) {
            //Deposit noted item
            Item unnoted = null;
            if (item.getDefinition().isNoted())
                unnoted = new Item(item.getDefinition().noteId, item);
            if(unnoted != null) {
                boolean added = to.add(unnoted);
                if(added)
                    delete(item);
                return added;
            }
        }
        return super.move(item, to);
    }

    public static Item contains(int id1, Item item1, Item item2) {
        if (item1.getId() == id1)
            return item2;
        if (item2.getId() == id1)
            return item1;
        return null;
    }

    public static boolean contains(int id1, int id2, Item... items) {
        boolean containsId1 = false;
        boolean containsId2 = false;
        for (Item item : items) {
            if (item.getId() == id1)
                containsId1 = true;
            else if (item.getId() == id2)
                containsId2 = true;
        }
        return containsId1 && containsId2;
    }

    /**
     * Convenience methods
     */

    public boolean addSafe(Item...items) {
        for(Item item : items) {
            boolean success = add(item);

            if (!success)
                FloorItemManager.addGroundItem(item, player.copyPosition(), player);
        }
        return true;
    }

    public boolean addCoins(int amount) {
        return addCoins(amount, !hasRoom() && !player.isInWilderness() && !player.isInDungeoneering());
    }

    public boolean addCoins(int amount, boolean pouch) {
        if(!pouch) {
            return add(new Item(995, amount));
        } else {
            player.message(Misc.insertCommasToNumber("" + amount + "") + " coins have been added to your money pouch.");
            player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() + amount);
            player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal());
            return true;
        }
    }

    public boolean removeCoins(int cost) {
        return removeCoins(cost, "make this purchase");
    }

    public boolean removeCoins(int cost, String ending) {
        if(cost <= 0)
            return true;
        boolean usePouch = player.getMoneyPouch().getTotal() >= cost;
        if (!usePouch) {
            if (getAmount(COINS) < cost) {
                player.message("You do not have enough coins to " + ending + ".");
                return false;
            }
            delete(new Item(995, cost));
        } else {
            player.message(Misc.insertCommasToNumber("" + cost + "") + " coins have been taken from your money pouch.");
            player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - cost);
            player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal());
        }
        return true;
    }

    public boolean hasHolyWrench() {
        return contains(new Item(6714));
    }

    public boolean hasCoins(int price) {
        if(getAmount(COINS) >= price)
            return true;
        if(player.getMoneyPouch().getTotal() >= price)
            return true;
        return false;
    }

    public boolean hasRoom(int item, boolean beingRemoved) {
        return hasRoom(new Item(item), beingRemoved);
    }

    public boolean hasRoom(Item item, boolean beingRemoved) {
        if(item.getDefinition().isStackable() && contains(items))
            return true;
        else if(item.getDefinition().isStackable() && getSpaces() >= (beingRemoved ? 0 : 1))
            return true;
        else if(!item.getDefinition().isStackable() && getSpaces() >= item.getAmount())
            return true;
        return false;
    }
}
