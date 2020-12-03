package com.fury.game.content.global;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.concurrent.TimeUnit;

public class LivingRockCavern {
    private static enum Rocks {
        COAL_ROCK_1(new GameObject(5999, new Position(3690, 5146), 10, 1)),
        COAL_ROCK_2(new GameObject(5999, new Position(3690, 5125), 10, 2)),
        COAL_ROCK_3(new GameObject(5999, new Position(3687, 5107), 10, 0)),
        COAL_ROCK_4(new GameObject(5999, new Position(3674, 5098), 10, 1)),
        COAL_ROCK_5(new GameObject(5999, new Position(3664, 5090), 10, 2)),
        COAL_ROCK_6(new GameObject(5999, new Position(3615, 5090), 10, 3)),
        COAL_ROCK_7(new GameObject(5999, new Position(3625, 5107), 10, 1)),
        COAL_ROCK_8(new GameObject(5999, new Position(3647, 5142), 10, 3)),
        GOLD_ROCK_1(new GameObject(45076, new Position(3667, 5075), 10, 1)),
        GOLD_ROCK_2(new GameObject(45076, new Position(3637, 5094), 10, 0)),
        GOLD_ROCK_3(new GameObject(45076, new Position(3677, 5160), 10, 0)),
        GOLD_ROCK_4(new GameObject(45076, new Position(3629, 5148), 10, 1));

        private Rocks(GameObject rock) {
            this.rock = rock;
        }

        private GameObject rock;

    }

    private static void respawnRock(final Rocks rock) {
        ObjectManager.spawnObject(rock.rock);
        GameExecutorManager.slowExecutor.schedule(() -> removeRock(rock), Misc.random(8) + 3, TimeUnit.MINUTES);
    }
    private static void removeRock(final Rocks rock) {
        ObjectManager.removeObject(rock.rock);
        GameExecutorManager.slowExecutor.schedule(() -> respawnRock(rock), 3, TimeUnit.MINUTES);
    }

    public static void init() {
        for (Rocks rock : Rocks.values())
            respawnRock(rock);
    }
}
