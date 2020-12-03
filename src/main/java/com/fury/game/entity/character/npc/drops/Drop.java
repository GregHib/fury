package com.fury.game.entity.character.npc.drops;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;

/**
 * Created by Greg on 01/04/2017.
 */
public class Drop {
    private int itemId, minAmount, maxAmount;
    private Revision revision;
    private double rate;
    @SuppressWarnings("unused")
    private boolean rare;

    public static Drop create(int itemId, double rate, int minAmount,
                              int maxAmount, boolean rare) {
        return new Drop((short) itemId, rate, minAmount, maxAmount, rare);
    }

    public Drop(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
        this.itemId = itemId;
        this.rate = rate;
        this.revision = itemId >= Loader.getTotalItems(Revision.RS2) ? itemId >= Loader.getTotalItems(Revision.PRE_RS3) ? Revision.RS3 : Revision.PRE_RS3 : Revision.RS2;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rare = rare;
    }

    public Drop(int itemId, Revision revision, double rate, int minAmount, int maxAmount, boolean rare) {
        this.itemId = itemId;
        this.revision = revision;
        this.rate = rate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.rare = rare;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getExtraAmount() {
        return maxAmount - minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getItemId() {
        return itemId;
    }

    public Revision getRevision() {
        return revision;
    }

    public double getRate() {
        return rate;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setMinAmount(int amount) {
        this.minAmount = amount;
    }

    public void setMaxAmount(int amount) {
        this.maxAmount = amount;
    }

    public boolean isFromRareTable() {
        return rare;
    }
}
