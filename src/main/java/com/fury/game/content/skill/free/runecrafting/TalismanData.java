package com.fury.game.content.skill.free.runecrafting;

import com.fury.game.world.map.Position;

public enum TalismanData {

    AIR_TALISMAN(1438, 5527, 2452, 1, new Position(2841, 4829), new Position(3129, 3406)),
    MIND_TALISMAN(1448, 5529, 2453, 2, new Position(2793, 4828), new Position(2984, 3516)),
    WATER_TALISMAN(1444, 5531, 2454, 5, new Position(3494, 4832), new Position(3183, 3163)),
    EARTH_TALISMAN(1440, 5535, 2455, 9, new Position(2655, 4830), new Position(3305, 3472)),
    FIRE_TALISMAN(1442, 5537, 2456, 14, new Position(2577, 4846), new Position(3311, 3253)),
    BODY_TALISMAN(1446, 5533, 2457, 20, new Position(2521, 4834), new Position(3051, 3444)),
    COSMIC_TALISMAN(1454, 5539, 2458, 27, new Position(2162, 4833), null),
    CHAOS_TALISMAN(1452, 5543, 2461, 35, new Position(2281, 4837), new Position(3059, 3589)),
    ASTRAL_TALISMAN(-1, 9106, -1, 40, null, null),
    NATURE_TALISMAN(1462, 5541, 2460, 44, new Position(2400, 4835), new Position(2867, 3020)),
    LAW_TALISMAN(1458, 5545, 2459, 54, new Position(2464, 4818), new Position(2859, 3379)),
    DEATH_TALISMAN(1456, 5547, 2462, 65, new Position(2208, 4830), new Position(1863, 4639)),
    BLOOD_TALISMAN(1450, 5549, 2464, 77, new Position(2468, 4889, 1), null),
    SOUL_TALISMAN(1460, 5551, -1, 90, null, null),
    ARMADYL_TALISMAN(-1, -1, -1, 77, null, null);

    TalismanData(int talismanId, int tiaraId, int alterObjectId, int levelReq, Position portal, Position ruins) {
        this.talismanId = talismanId;
        this.tiaraId = tiaraId;
        this.alterObjectId = alterObjectId;
        this.levelReq = levelReq;
        this.location = portal;
        this.ruins = ruins;
    }

    private int talismanId, tiaraId, alterObjectId, levelReq;
    private Position location, ruins;

    public int getTalismanId() {
        return talismanId;
    }

    public int getTiaraId() {
        return tiaraId;
    }

    public int getObjectId() {
        return alterObjectId;
    }

    public int getLevelRequirement() {
        return levelReq;
    }

    public Position getLocation() {
        return location;
    }

    public Position getRuinsLocation() {
        return ruins;
    }

    public static TalismanData forObjectId(int id) {
        for(TalismanData data : TalismanData.values()) {
            if(data.getObjectId() == id) {
                return data;
            }
        }
        return null;
    }

    public static TalismanData forId(int talismanId) {
        for(TalismanData data : TalismanData.values()) {
            if(data.getTalismanId() == talismanId) {
                return data;
            }
        }
        return null;
    }
}
