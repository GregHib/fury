package com.fury.game.content.global.treasuretrails;

import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 26/09/2016.
 */
public enum ClueTypes {
    NULL(),
    MAP(),
    SIMPLE(),
    EMOTE();

    public static ClueTypes getRandom(ClueTiers tier) {
        List<ClueTypes> allowed = new ArrayList<>();
        for(ClueTypes type : values()) {
                for(ClueTypes available : tier.getAvailableTypes())
                    if(type == available) {
                        allowed.add(type);
                        break;
                    }
        }

        if(allowed.size() == 0)
            return null;

        return values()[allowed.get(Misc.random(allowed.size() - 1)).ordinal()];
    }

    public static ClueTypes get(String name) {
        for(ClueTypes type : values()) {
            if(name.equals(type.name())) {
                return type;
            }
        }
        return null;
    }

    public static ClueTypes get(int id) {
        if(id >= 0 && id < values().length)
            return values()[id];
        return null;
    }
}
