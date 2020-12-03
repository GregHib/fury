package com.fury.game.world.map.path;

import com.fury.core.model.item.FloorItem;

public class FloorItemStrategy extends RouteStrategy {

    private int x;
    private int y;

    public FloorItemStrategy(FloorItem entity) {
        this.x = entity.getTile().getX();
        this.y = entity.getTile().getY();
    }

    @Override
    public boolean canExit(int currentX, int currentY, int height, int sizeX, int sizeY, int clipBaseX, int clipBaseY) {
        return RouteStrategy.checkFilledRectangularInteract(currentX - clipBaseX, currentY - clipBaseY, height, sizeX, sizeY, x - clipBaseX, y - clipBaseY, clipBaseX, clipBaseY, 1, 1, 0);
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
        return 1;
    }

    @Override
    public int getApproxDestinationSizeY() {
        return 1;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FloorItemStrategy))
            return false;
        FloorItemStrategy strategy = (FloorItemStrategy) other;
        return x == strategy.x && y == strategy.y;
    }
}
