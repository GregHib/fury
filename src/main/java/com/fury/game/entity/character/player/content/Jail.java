package com.fury.game.entity.character.player.content;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.HashMap;

public class Jail {

    private static HashMap<String, Byte> JAILED_PLAYERS = new HashMap<>();

    public static int getCellNumber() {
        return findSlot();
    }

    public static Position getCellLocation(int cell) {
        Position pos;
        switch (cell) {
            case 0:
                pos = new Position(3037, 2977);
                break;
            case 1:
                pos = new Position(3038, 2981);
                break;
            case 2:
                pos = new Position(3037, 2985);
                break;
            case 3:
                pos = new Position(3030, 2985);
                break;
            case 4:
                pos = new Position(3030, 2978, 1);
                break;
            case 5:
                pos = new Position(3031, 2984, 1);
                break;
            default:
                pos = new Position(3030, 2978, 1);
                break;
        }
        return pos;
    }

    public static boolean jailPlayer(Player player) {
        int cell = getCellNumber();
        Position pos = getCellLocation(cell);

        if(player.getControllerManager().getController() != null)
            player.getControllerManager().forceStop();

        player.moveTo(pos);
        jail(player);
        return true;
    }

    private static void jail(Player player) {
        JAILED_PLAYERS.put(player.getUsername(), findSlot());
    }

    public static Position getJail(Player player) {
        int cell = getIndex(player);

        if(cell < 0) {
            jail(player);
            cell = getIndex(player);
        }

        return getCellLocation(cell);
    }

    public static void unjail(Player player) {
        JAILED_PLAYERS.remove(player.getUsername());
    }

    public static int getIndex(Player player) {
        return JAILED_PLAYERS.containsKey(player.getUsername()) ? JAILED_PLAYERS.get(player.getUsername()) : -1;
    }

    public static byte findSlot() {
        return (byte) Misc.random(0, 5);
    }

}
