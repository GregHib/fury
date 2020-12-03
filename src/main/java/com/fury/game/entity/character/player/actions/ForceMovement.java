package com.fury.game.entity.character.player.actions;

import com.fury.game.world.map.Position;

public class ForceMovement {
    public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

    private Position firstTile;
    private Position secondTile;
    private int firstTileDelay;
    private int secondTileDelay;
    protected int direction;


    public ForceMovement(Position firstTile, int firstTileDelay, int direction) {
        this(firstTile, firstTileDelay, null, 0, direction);
    }

    public ForceMovement(Position firstTile, int firstTileDelay, Position secondTile, int secondTileDelay, int direction) {
        this.firstTile = firstTile;
        this.firstTileDelay = firstTileDelay;
        this.secondTile = secondTile;
        this.secondTileDelay = secondTileDelay;
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
        /*switch (direction) {
            case NORTH:
                return Utils.getAngle(0, 1);
            case EAST:
                return Utils.getAngle(1, 0);
            case SOUTH:
                return Utils.getAngle(0, -1);
            case WEST:
            default:
                return Utils.getAngle(-1, 0);
        }*/
    }

    public Position getFirstTile() {
        return firstTile;
    }

    public Position getSecondTile() {
        return secondTile;
    }

    public int getFirstTileDelay() {
        return firstTileDelay;
    }

    public int getSecondTileDelay() {
        return secondTileDelay;
    }
}
