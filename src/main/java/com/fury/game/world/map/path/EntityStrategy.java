package com.fury.game.world.map.path;

import com.fury.core.model.node.entity.Entity;

public class EntityStrategy extends RouteStrategy {
    private int x;
    private int y;
    private int sizeX;
    private int sizeY;
    private int accessBlockFlag;

    public EntityStrategy(Entity entity) {
        this.x = entity.getX();
        this.y = entity.getY();
        this.sizeX = entity.getSizeX();
        this.sizeY = entity.getSizeY();
        this.accessBlockFlag = 0;
    }

    @Override
    public boolean canExit(int currentX, int currentY, int height, int sizeX, int sizeY, int clipBaseX, int clipBaseY) {
        return RouteStrategy.checkFilledRectangularInteract(currentX - clipBaseX, currentY - clipBaseY, height, sizeX, sizeY, x - clipBaseX, y - clipBaseY, clipBaseX, clipBaseY, this.sizeX, this.sizeY, accessBlockFlag);
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
        if (!(other instanceof EntityStrategy))
            return false;
        EntityStrategy strategy = (EntityStrategy) other;
        return x == strategy.x && y == strategy.y && sizeX == strategy.sizeY && sizeY == strategy.sizeY && accessBlockFlag == strategy.accessBlockFlag;
    }
}
