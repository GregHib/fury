package com.fury.game.content.global.quests;

import com.fury.core.model.item.Item;

public abstract class QuestReward {

    public abstract void giveReward();

    //Used to display on interface
    public Item questItem() {
        return null;
    }

    public Item[] items() {
        return null;
    }

    public String[] lines() {
        return null;
    }

    public int questPoints() {
        return 1;
    }
}
