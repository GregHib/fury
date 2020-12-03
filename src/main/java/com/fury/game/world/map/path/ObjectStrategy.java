package com.fury.game.world.map.path;

import com.fury.game.entity.object.GameObject;

public class ObjectStrategy extends RouteStrategy {

    private int x;
    private int y;
    private int routeType;
    private int type;
    private int rotation;
    private int sizeX;
    private int sizeY;
    private int accessBlockFlag;

    public ObjectStrategy(GameObject object) {
        this.x = object.getX();
        this.y = object.getY();
        this.routeType = getType(object);
        this.type = object.getType();
        this.rotation = object.getDirection();
        this.sizeX = rotation == 0 || rotation == 2 ? object.getDefinition().getSizeX() : object.getDefinition().getSizeY();
        this.sizeY = rotation == 0 || rotation == 2 ? object.getDefinition().getSizeY() : object.getDefinition().getSizeX();
        this.accessBlockFlag = object.getDefinition().plane;
        if (rotation != 0)
            accessBlockFlag = ((accessBlockFlag << rotation) & 0xF) + (accessBlockFlag >> (4 - rotation));
    }

    @Override
    public boolean canExit(int currentX, int currentY, int height, int sizeX, int sizeY, int clipBaseX, int clipBaseY) {
        switch (routeType) {
            case 0:
                return RouteStrategy.checkWallInteract(currentX - clipBaseX, currentY - clipBaseY, height, sizeX, sizeY, x - clipBaseX, y - clipBaseY, clipBaseX, clipBaseY, type, rotation);
            case 1:
                return RouteStrategy.checkWallDecorationInteract(currentX - clipBaseX, currentY - clipBaseY, height, sizeX, sizeY, x - clipBaseX, y - clipBaseY, clipBaseX, clipBaseY, type, rotation);
            case 2:
                return RouteStrategy.checkFilledRectangularInteract(currentX - clipBaseX, currentY - clipBaseY, height, sizeX, sizeY, x - clipBaseX, y - clipBaseY, clipBaseX, clipBaseY, this.sizeX, this.sizeY, accessBlockFlag);
            case 3:
                return currentX == x - clipBaseX && currentY == y - clipBaseY;
        }
        return false;
    }

    private int getType(GameObject object) {
        int type = object.getType();
        if ((type >= 0 && type <= 3) || type == 9)
            return 0; // wall
        else if (type < 9)
            return 1; // deco
        else if (type == 10 || type == 11 || type == 22)
            return 2; // ground
        else
            return 3; // misc
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
        if (!(other instanceof ObjectStrategy))
            return false;
        ObjectStrategy strategy = (ObjectStrategy) other;
        return x == strategy.x && y == strategy.y && routeType == strategy.routeType && type == strategy.type && rotation == strategy.rotation && sizeX == strategy.sizeX && sizeY == strategy.sizeY && accessBlockFlag == strategy.accessBlockFlag;
    }
}
