package com.fury.game.content.skill.free.firemaking;

import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;

public enum FireStarter {
    TINDERBOX(590),
    DUNGEONEERING_TINDERBOX(DungeonConstants.TINDERBOX.getId()),
    RED_FIRELIGHTER(7329),
    GREEN_FIRELIGHTER(7330),
    BLUE_FIRELIGHTER(7331),
    PURPLE_FIRELIGHTER(10326),
    WHITE_FIRELIGHTER(10327);

    private int itemId;

    FireStarter(int itemId) {
        this.itemId = itemId;
    }

    public int getId() {
        return itemId;
    }

    public static FireStarter forId(int id) {
        for (FireStarter s : values()) {
            if (s.getId() == id)
                return s;
        }
        return null;
    }

    public boolean isFirelighter() {
        for (int i = 2; i < values().length; i++) {
            if (values()[i].getId() == this.getId())
                return true;
        }
        return false;
    }
}
