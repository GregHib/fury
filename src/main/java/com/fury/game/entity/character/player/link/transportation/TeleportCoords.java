package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.world.map.Position;

/**
 * Created by Greg on 12/10/2016.
 */
public enum TeleportCoords {
    DEMONIC_GORILLA("demonic gorillas", new Position(2124, 5660)),
    QBD("qbd", new Position(1441, 6366, 1)),
    QBD_REWARDS("qbd rewards", new Position(1308, 6107)),
    JADINKO("jadinko lair", new Position(3012, 9275)),
    CERBERUS("cerberus", new Position(1240, 1251)),
    KRACKEN("kracken", new Position(2280, 10031)),
    ;

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    private String name;
    private Position position;


    TeleportCoords(String text, Position position) {
        this.name = text;
        this.position = position;
    }

    public static Position get(String text) {
        for(TeleportCoords places : values())
            if(text.equalsIgnoreCase(places.getName()))
                return places.getPosition();
        return null;
    }
}
