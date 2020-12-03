package com.fury.game.content.global.quests;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;

public abstract class Quest implements QuestInterface {
    private int stage;

    public boolean hasRequirements(Player player) {
        return true;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public boolean isObjectOption1(Player player, GameObject object) {
        return false;
    }

    public boolean isNpcOption1(Player player, Mob mob) {
        return false;
    }

    public boolean isButtonPressed(Player player, int button) {
        return false;
    }

    public boolean hasFinished() {
        return getStage() >= getStages();
    }

    public abstract void start(Player player);
}
