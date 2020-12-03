package com.fury.game.content.skill.member.summoning;

/**
 * Created by Greg on 23/11/2016.
 */
public enum SummoningType {
    NORMAL(0, 78),
    DUNGEONEERING(78, 138),
    STEALING_CREATION(139, 144);

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    private int start, end;
    SummoningType(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
