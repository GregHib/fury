package com.fury.game.world.map.path;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.world.map.Position;

public class CombatStrategy extends RouteStrategy {
    private int x;
    private int y;
    private int sizeX;
    private int sizeY;

    public CombatStrategy(Figure entity) {
        this.x = entity.getX();
        this.y = entity.getY();
        this.sizeX = entity.getSizeX();
        this.sizeY = entity.getSizeY();
    }

    @Override
    public boolean canExit(int currentX, int currentY, int height, int sizeX, int sizeY, int clipBaseX, int clipBaseY) {
        return RouteStrategy.clippedProjectile(new Position(currentX, currentY, height), new Position(x, y), true, sizeX, sizeY);
    }

    @Override
    public int getApproxDestinationX() {
        return x;
    }

    @Override
    public int getApproxDestinationY() {
        return y;
    }

    @Override
    public int getApproxDestinationSizeX() {
        return sizeX;
    }

    @Override
    public int getApproxDestinationSizeY() {
        return sizeY;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CombatStrategy))
            return false;
        CombatStrategy strategy = (CombatStrategy) other;
        return x == strategy.x && y == strategy.y && sizeX == strategy.sizeX && sizeY == strategy.sizeY;
    }
}
