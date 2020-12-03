package com.fury.game.content.global.dnd.eviltree;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.concurrent.TimeUnit;

public class EvilTreeFire extends GameObject {

    private boolean alight = false;
    private int damage = 0;
    private Player player;

    public EvilTreeFire(int id, Position position, int face) {
        super(id, position, 0, face, Revision.RS2);
    }

    public void despawn() {
        ObjectManager.removeObject(this);
        alight = false;
        player = null;
    }

    public void spawn(Player player) {
        ObjectManager.spawnObject(this);
        alight = true;
        damage = Misc.random(45, 80);
        this.player = player;
        GameExecutorManager.slowExecutor.schedule(() -> dealDamage(), 1, TimeUnit.SECONDS);
    }

    private void dealDamage() {
        if(damage > 0 && player != null) {
            damage--;
            EvilTree.get().depleteHealth(player, 1);
            GameExecutorManager.slowExecutor.schedule(() -> dealDamage(), 1, TimeUnit.SECONDS);
        } else {
            despawn();
        }
    }

    public boolean isAlight() {
        return alight;
    }
}
