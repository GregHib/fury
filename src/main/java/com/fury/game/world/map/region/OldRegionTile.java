package com.fury.game.world.map.region;

import com.fury.game.entity.object.GameObject;

/**
 * Created by Greg on 24/12/2016.
 */
public class OldRegionTile {

    public OldRegionTile() {}

    private int mask;
    private GameObject wallObject;
    private GameObject wallDecoration;
    private GameObject floorObject;
    private GameObject floorDecoration;

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

    public GameObject getWallObject() {
        return wallObject;
    }

    public GameObject getWallDecoration() {
        return wallDecoration;
    }

    public GameObject getFloorObject() {
        return floorObject;
    }

    public GameObject getFloorDecoration() {
        return floorDecoration;
    }

    public void setWallObject(GameObject obj) {
        wallObject = obj;
    }

    public void setWallDecorationObject(GameObject obj) {
        wallDecoration = obj;
    }

    public void setFloorObject(GameObject floorObject) {
        this.floorObject = floorObject;
    }

    public void setFloorDecorationObject(GameObject obj) {
        floorDecoration = obj;
    }

    public void setObjectWithType(GameObject obj) {
        switch (ObjectType.getType(obj.getType())) {
            case WALL:
                setWallObject(obj);
                break;
            case WALL_DECORATION:
                setWallDecorationObject(obj);
                break;
            case FLOOR:
                setFloorObject(obj);
                break;
            case FLOOR_DECORATION:
                setFloorDecorationObject(obj);
                break;
        }
    }

    public GameObject getObjectWithType(int type) {
        switch (ObjectType.getType(type)) {
            case WALL:
                return getWallObject();
            case WALL_DECORATION:
                return getWallDecoration();
            case FLOOR:
                return getFloorObject();
            case FLOOR_DECORATION:
                return getFloorDecoration();
        }
        return null;
    }

    public GameObject getObjectWithSlot(int slot) {
        switch (ObjectType.getTypeWithSlot(slot)) {
            case WALL:
                return getWallObject();
            case WALL_DECORATION:
                return getWallDecoration();
            case FLOOR:
                return getFloorObject();
            case FLOOR_DECORATION:
                return getFloorDecoration();
        }
        return null;
    }

    public enum ObjectType {
        WALL(0),
        WALL_DECORATION(1),
        FLOOR(2),
        FLOOR_DECORATION(3);

        private int slot;

        ObjectType(int slot) {
            this.slot = slot;
        }

        public static ObjectType getType(int t) {
            int slot = 0;
            if(t >= 22)
                slot = 3;
            else if(t >= 9)
                slot = 2;
            else if(t >= 4)
                slot = 1;
            for(ObjectType type : values()) {
                if(type.slot == slot)
                    return type;
            }
            return null;
        }
        public static ObjectType getTypeWithSlot(int slot) {
            for(ObjectType type : values()) {
                if(type.slot == slot)
                    return type;
            }
            return null;
        }
    }
}
