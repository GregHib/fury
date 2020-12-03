package com.fury.game.world.map.path;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.clip.Flags;
import com.fury.util.Utils;

public abstract class RouteStrategy {

    public abstract boolean canExit(int currentX, int currentY, int height, int sizeX, int sizeY, int clipBaseX, int clipBaseY);

    public abstract int getApproxDestinationX();

    public abstract int getApproxDestinationY();

    public abstract int getApproxDestinationSizeX();

    public abstract int getApproxDestinationSizeY();

    public abstract boolean equals(Object other);


    /**
     * Check's if we can interact wall decoration from current position.
     */
    protected static boolean checkWallDecorationInteract(int currentX, int currentY, int height, int sizeX, int sizeY, int targetX, int targetY, int baseX, int baseY, int targetType, int targetRotation) {
        // TODO, include additional checks for size's bigger than 1.
        if (currentX == targetX && currentY == targetY)
            return true;
        if (targetType == 6 || targetType == 7) {
            if (targetType == 7)
                targetRotation = targetRotation + 2 & 0x3;
            if (targetRotation == 0) {
                if (currentX == (targetX + 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_WEST) == 0)
                    return true;
                if (currentX == targetX && currentY == (targetY - 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_NORTH) == 0)
                    return true;
            } else if (targetRotation == 1) {
                if (currentX == (targetX - 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_EAST) == 0)
                    return true;
                if (currentX == targetX && currentY == (targetY - 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_NORTH) == 0)
                    return true;
            } else if (targetRotation == 2) {
                if (currentX == (targetX - 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_EAST) == 0)
                    return true;
                if (currentX == targetX && currentY == (targetY + 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_SOUTH) == 0)
                    return true;
            } else if (targetRotation == 3) {
                if (currentX == (targetX + 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_WEST) == 0)
                    return true;
                if (currentX == targetX && currentY == (targetY + 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_SOUTH) == 0)
                    return true;
            }
        } else if (targetType == 8) {
            if (currentX == targetX && currentY == (targetY + 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_SOUTH) == 0)
                return true;
            if (currentX == targetX && currentY == (targetY - 1) && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_NORTH) == 0)
                return true;
            if (currentX == (targetX - 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_EAST) == 0)
                return true;
            if (currentX == (targetX + 1) && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & Flags.WALLOBJ_WEST) == 0)
                return true;
        }
        return false;
    }

    /**
     * Check's if we can interact wall object from current position.
     */
    protected static boolean checkWallInteract(int currentX, int currentY, int height, int sizeX, int sizeY, int targetX, int targetY, int baseX, int baseY, int targetType, int targetRotation) {
        // TODO refactor
        if (sizeX == 1 && sizeY == 1) {
            if (currentX == targetX && currentY == targetY)
                return true; // we are inside the object
        } else if (targetX >= currentX && targetX <= currentX + sizeX - 1 && targetY <= targetY + sizeY - 1)
            return true; // we are inside the object bounds , though no y check?
        if (sizeX == 1 && sizeY == 1) {
            if (targetType == 0) {
                if (targetRotation == 0) {
                    if (targetX - 1 == currentX && currentY == targetY)
                        return true;
                    if (currentX == targetX && targetY + 1 == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX == currentX && currentY == targetY - 1 && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 1) {
                    if (currentX == targetX && targetY + 1 == currentY)
                        return true;
                    if (currentX == targetX - 1 && targetY == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX + 1 == currentX && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0180) == 0)
                        return true;
                } else if (targetRotation == 2) {
                    if (targetX + 1 == currentX && currentY == targetY)
                        return true;
                    if (targetX == currentX && currentY == targetY + 1 && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX == currentX && currentY == targetY - 1 && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 3) {
                    if (currentX == targetX && targetY - 1 == currentY)
                        return true;
                    if (targetX - 1 == currentX && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX + 1 == currentX && targetY == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0180) == 0)
                        return true;
                }
            }
            if (targetType == 2) {
                if (targetRotation == 0) {
                    if (currentX == targetX - 1 && currentY == targetY)
                        return true;
                    if (targetX == currentX && targetY + 1 == currentY)
                        return true;
                    if (currentX == targetX + 1 && targetY == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0180) == 0)
                        return true;
                    if (targetX == currentX && targetY - 1 == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 1) {
                    if (targetX - 1 == currentX && targetY == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX == currentX && targetY + 1 == currentY)
                        return true;
                    if (targetX + 1 == currentX && currentY == targetY)
                        return true;
                    if (targetX == currentX && currentY == targetY - 1 && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 2) {
                    if (targetX - 1 == currentX && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0108) == 0)
                        return true;
                    if (currentX == targetX && currentY == targetY + 1 && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (currentX == targetX + 1 && targetY == currentY)
                        return true;
                    if (currentX == targetX && targetY - 1 == currentY)
                        return true;
                } else if (targetRotation == 3) {
                    if (targetX - 1 == currentX && currentY == targetY)
                        return true;
                    if (targetX == currentX && targetY + 1 == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (currentX == targetX + 1 && targetY == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2c0180) == 0)
                        return true;
                    if (currentX == targetX && targetY - 1 == currentY)
                        return true;
                }
            }
            if (targetType == 9) {
                if (targetX == currentX && targetY + 1 == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x20) == 0)
                    return true;
                if (currentX == targetX && targetY - 1 == currentY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x2) == 0)
                    return true;
                if (currentX == targetX - 1 && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x8) == 0)
                    return true;
                if (currentX == targetX + 1 && currentY == targetY && (World.getMask(baseX + currentX, baseY + currentY, height) & 0x80) == 0)
                    return true;
            }
        } else {
            int checkX = currentX + sizeX - 1;
            int checkY = currentY + sizeY - 1;
            if (targetType == 0) {
                if (targetRotation == 0) {
                    if (targetX - sizeX == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY + 1 && (World.getMask(baseX + targetX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && targetY - sizeY == currentY && (World.getMask(baseX + targetX, baseY + checkY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 1) {
                    if (targetX >= currentX && targetX <= checkX && targetY + 1 == currentY)
                        return true;
                    if (currentX == targetX - sizeX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + checkX, baseY + targetY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + currentX, baseY + targetY, height) & 0x2c0180) == 0)
                        return true;
                } else if (targetRotation == 2) {
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY + 1 && (World.getMask(baseX + targetX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY - sizeY && (World.getMask(baseX + targetX, baseY + checkY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 3) {
                    if (targetX >= currentX && targetX <= checkX && targetY - sizeY == currentY)
                        return true;
                    if (currentX == targetX - sizeX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + checkX, baseY + targetY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + currentX, baseY + targetY, height) & 0x2c0180) == 0)
                        return true;
                }
            }
            if (targetType == 2) {
                if (targetRotation == 0) {
                    if (targetX - sizeX == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && targetY + 1 == currentY)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + currentX, baseY + targetY, height) & 0x2c0180) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && targetY - sizeY == currentY && (World.getMask(baseX + targetX, baseY + checkY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 1) {
                    if (currentX == targetX - sizeX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + checkX, baseY + targetY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY + 1)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY - sizeY && (World.getMask(baseX + targetX, baseY + checkY, height) & 0x2c0102) == 0)
                        return true;
                } else if (targetRotation == 2) {
                    if (currentX == targetX - sizeX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + checkX, baseY + targetY, height) & 0x2c0108) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && targetY + 1 == currentY && (World.getMask(baseX + targetX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY - sizeY)
                        return true;
                } else if (targetRotation == 3) {
                    if (targetX - sizeX == currentX && targetY >= currentY && targetY <= checkY)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY + 1 && (World.getMask(baseX + targetX, baseY + currentY, height) & 0x2c0120) == 0)
                        return true;
                    if (targetX + 1 == currentX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + currentX, baseY + targetY, height) & 0x2c0180) == 0)
                        return true;
                    if (targetX >= currentX && targetX <= checkX && currentY == targetY - sizeY)
                        return true;
                }
            }
            if (targetType == 9) {
                if (targetX >= currentX && targetX <= checkX && targetY + 1 == currentY && (World.getMask(baseX + targetX, baseY + currentY, height) & 0x2c0120) == 0)
                    return true;
                if (targetX >= currentX && targetX <= checkX && targetY - sizeY == currentY && (World.getMask(baseX + targetX, baseY + checkY, height) & 0x2c0102) == 0)
                    return true;
                if (targetX - sizeX == currentX && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + checkX, baseY + targetY, height) & 0x2c0108) == 0)
                    return true;
                if (currentX == targetX + 1 && targetY >= currentY && targetY <= checkY && (World.getMask(baseX + currentX, baseY + targetY, height) & 0x2c0180) == 0)
                    return true;
            }
        }
        return false;
    }

    /**
     * Check's if we can interact filled rectangular (Might be ground object or
     * npc or player etc) from current position.
     */
    protected static boolean checkFilledRectangularInteract(int currentX, int currentY, int height, int sizeX, int sizeY, int targetX, int targetY, int baseX, int baseY, int targetSizeX, int targetSizeY, int accessBlockFlag) {
        // TODO refactor
        int srcEndX = currentX + sizeX;
        int srcEndY = currentY + sizeY;
        int destEndX = targetX + targetSizeX;
        int destEndY = targetY + targetSizeY;
        if (destEndX == currentX && (accessBlockFlag & 0x2) == 0) { // can we enter from east ?
            int checkY = currentY > targetY ? currentY : targetY;
            for (int i_13_ = srcEndY < destEndY ? srcEndY : destEndY; checkY < i_13_; checkY++) {
                if (((World.getMask(baseX + destEndX - 1, baseY + checkY, height)) & 0x8) == 0)
                    return true;
            }
        } else if (srcEndX == targetX && (accessBlockFlag & 0x8) == 0) { // can we enter from west ?
            int checkY = currentY > targetY ? currentY : targetY;
            for (int i_15_ = srcEndY < destEndY ? srcEndY : destEndY; checkY < i_15_; checkY++) {
                if (((World.getMask(baseX + targetX, baseY + checkY, height)) & 0x80) == 0)
                    return true;
            }
        } else if (currentY == destEndY && (accessBlockFlag & 0x1) == 0) { // can we enter from north?
            int checkX = currentX > targetX ? currentX : targetX;
            for (int i_17_ = srcEndX < destEndX ? srcEndX : destEndX; checkX < i_17_; checkX++) {
                if (((World.getMask(baseX + checkX, baseY + destEndY - 1, height)) & 0x2) == 0)
                    return true;
            }
        } else if (targetY == srcEndY && (accessBlockFlag & 0x4) == 0) { // can we enter from south?
            int checkX = currentX > targetX ? currentX : targetX;
            for (int i_19_ = srcEndX < destEndX ? srcEndX : destEndX; checkX < i_19_; checkX++) {
                if (((World.getMask(baseX + checkX, baseY + targetY, height)) & 0x20) == 0)
                    return true;
            }
        }
        return false;
    }

    public static boolean clippedProjectile(Position start, Position end, boolean checkClose, int size) {
        int myX = start.getX();
        int myY = start.getY();
        if(start instanceof Mob) {
            Position centre = ((Mob) start).getCentredPosition();
            myX = centre.getX();
            myY = centre.getY();
        }
        int destX = end.getX();
        int destY = end.getY();
        if (myX == destX && destY == myY)
            return true;
        int lastTileX = myX;
        int lastTileY = myY;
        while (true) {
            if (myX < destX)
                myX++;
            else if (myX > destX)
                myX--;
            if (myY < destY)
                myY++;
            else if (myY > destY)
                myY--;
            int dir = Utils.getMoveDirection(myX - lastTileX, myY - lastTileY);
            if (dir == -1)
                return false;
            if (checkClose) {
                if (!World.checkWalkStep(lastTileX, lastTileY, start.getZ(), dir, size))
                    return false;
            } else if (!World.checkProjectileStep(lastTileX, lastTileY, start.getZ(), dir, size))
                return false;
            lastTileX = myX;
            lastTileY = myY;
            if (lastTileX == destX && lastTileY == destY)
                return true;
        }
    }

    public static boolean clippedProjectile(Position start, Position end, boolean checkClose, int sizeX, int sizeY) {
        int myX = start.getX();
        int myY = start.getY();
        if(start instanceof Mob) {
            Position centre = ((Mob) start).getCentredPosition();
            myX = centre.getX();
            myY = centre.getY();
        }
        int destX = end.getX();
        int destY = end.getY();
        if (myX == destX && destY == myY)
            return true;
        int lastTileX = myX;
        int lastTileY = myY;
        while (true) {
            if (myX < destX)
                myX++;
            else if (myX > destX)
                myX--;
            if (myY < destY)
                myY++;
            else if (myY > destY)
                myY--;
            int dir = Utils.getMoveDirection(myX - lastTileX, myY - lastTileY);
            if (dir == -1)
                return false;
            if (checkClose) {
                if (!World.checkWalkStep(lastTileX, lastTileY, start.getZ(), dir, sizeX, sizeY))
                    return false;
            } else if (!World.checkProjectileStep(lastTileX, lastTileY, start.getZ(), dir, sizeX, sizeY))
                return false;
            lastTileX = myX;
            lastTileY = myY;
            if (lastTileX == destX && lastTileY == destY)
                return true;
        }
    }
}
