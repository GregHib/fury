package com.fury.game.content.global.treasuretrails;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;

public class ClueScroll {

    private ClueTypes type;
    private int index, remainingClues;
    private boolean emoteOneComplete;
    private boolean emoteTwoComplete;
    private boolean doubleAgentDead;
    private boolean uriSpawned;

    private Mob uri;
    private DoubleAgent doubleAgent;

    public ClueTypes getType() {
        return type;
    }

    public void setType(ClueTypes type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getRemainingClues() {
        return remainingClues;
    }

    public void setRemainingClues(int remainingClues) {
        this.remainingClues = remainingClues;
    }

    public boolean isEmoteOneComplete() {
        return emoteOneComplete;
    }

    public void setEmoteOneComplete(boolean emoteOneComplete) {
        this.emoteOneComplete = emoteOneComplete;
    }

    public boolean isEmoteTwoComplete() {
        return emoteTwoComplete;
    }

    public void setEmoteTwoComplete(boolean emoteTwoComplete) {
        this.emoteTwoComplete = emoteTwoComplete;
    }

    public boolean isDoubleAgentDead() {
        return doubleAgentDead;
    }

    public void setDoubleAgentDead(boolean doubleAgentDead) {
        this.doubleAgentDead = doubleAgentDead;
    }

    public boolean isUriSpawned() {
        return uriSpawned;
    }

    public void setUriSpawned(boolean uriSpawned) {
        this.uriSpawned = uriSpawned;
    }

    public DoubleAgent getDoubleAgent() {
        return doubleAgent;
    }

    public void setDoubleAgent(DoubleAgent doubleAgent) {
        this.doubleAgent = doubleAgent;
    }

    public Mob getUri() {
        return uri;
    }

    public void setUri(Mob uri) {
        this.uri = uri;
    }

    public String getIndexName() {
        switch (getType()) {
            case MAP:
                return ClueConstants.Maps.values()[getIndex()].name();
            case SIMPLE:
                return ClueConstants.SimpleClues.values()[getIndex()].name();
            case EMOTE:
                return ClueConstants.Emotes.values()[getIndex()].name();
        }
        return null;
    }
}
