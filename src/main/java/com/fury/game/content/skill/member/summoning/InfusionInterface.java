package com.fury.game.content.skill.member.summoning;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 23/11/2016.
 */
public class InfusionInterface {

    private Item[] scrollsOrPouches;
    private int[] reqLevels;
    private int[][] reqItems;
    private int[][] reqAmounts;
    private Revision[][] reqRevisions;
    private int[] prices;
    private SummoningType type;

    public InfusionInterface(Item[] scrollsOrPouches, int[] reqLevels, int[][] reqItems, Revision[][] reqRevisions, int[][] reqAmounts, int[] prices, SummoningType type) {
        this.scrollsOrPouches = scrollsOrPouches;
        this.reqLevels = reqLevels;
        this.reqItems = reqItems;
        this.reqAmounts = reqAmounts;
        this.reqRevisions = reqRevisions;
        this.prices = prices;
        this.type = type;
    }

    public int getAmount() {
        return scrollsOrPouches.length;
    }

    public Item[] getScrollsOrPouches() {
        return scrollsOrPouches;
    }

    public int[] getReqLevels() {
        return reqLevels;
    }

    public int[][] getReqItems() {
        return reqItems;
    }

    public int[][] getReqAmounts() {
        return reqAmounts;
    }

    public Revision[][] getReqRevisions() {
        return reqRevisions;
    }

    public int[] getPrices() {
        return prices;
    }

    public SummoningType getSummoningType() {
        return type;
    }
}
