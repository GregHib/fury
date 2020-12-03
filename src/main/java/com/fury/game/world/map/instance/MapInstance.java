package com.fury.game.world.map.instance;

import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;

import java.util.concurrent.TimeUnit;

public class MapInstance {

    public static enum Stages {
        LOADING, RUNNING, DESTROYING
    }

    private Stages stage;
    private int[] instancePos;
    private int[] originalPos;
    private int ratioX, ratioY;

    public MapInstance(int x, int y) {
        this(x, y, 1, 1);
    }

    public MapInstance(int x, int y, int ratioX, int ratioY) {
        originalPos = new int[]{x, y};
        this.ratioX = ratioX;
        this.ratioY = ratioY;
    }


    public void load(final Runnable run) {
        stage = Stages.LOADING;
        GameExecutorManager.slowExecutor.execute(() -> {
            // finds empty map bounds
            if (instancePos == null)
                instancePos = MapBuilder.findEmptyChunkBound(ratioX * 8, ratioY * 8);
            // copies real map into the empty map
            MapBuilder.copyAllPlanesMap(originalPos[0], originalPos[1], instancePos[0], instancePos[1], ratioX * 8, ratioY * 8);
            if (run != null) {
                World.executeAfterLoadRegion(((instancePos[0] / 8) << 8) + (instancePos[0] / 8), () -> {
                    run.run();
                });
            }
            stage = Stages.RUNNING;
        });
    }

    public void destroy(final Runnable run) {
        stage = Stages.RUNNING;
        GameExecutorManager.slowExecutor.schedule(() -> {
            MapBuilder.destroyMap(instancePos[0], instancePos[1], ratioX * 8, ratioY * 8);
            if (run != null)
                run.run();
        }, 1800, TimeUnit.MILLISECONDS);
    }

    public Position getTile(int x, int y) {
        return new Position(instancePos[0] * 8 + x, instancePos[1] * 8 + y, 0);
    }

    public int[] getOriginalPos() {
        return originalPos;
    }

    public int[] getInstancePos() {
        return instancePos;
    }

    public int getRatioX() {
        return ratioX;
    }

    public int getRatioY() {
        return ratioY;
    }

    public Stages getStage() {
        return stage;
    }

}
