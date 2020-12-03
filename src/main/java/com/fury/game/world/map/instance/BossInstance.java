package com.fury.game.world.map.instance;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.BossInstanceController;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.map.Position;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class BossInstance {

    private MapInstance map;
    private List<Player> players;
    private Player enter;

    public BossInstance() {
        players = new CopyOnWriteArrayList<>();
        init();
    }

    public int getPlayersCount() {
        return players.size();
    }

    abstract public Position getInside();

    abstract public Position getOutside();

    abstract public int[] getMap();

    public int[] getSize() {
        return new int[] {1, 1};
    }

    abstract public void load();

    public void init() {
        if (map == null) {
            int[] pos = getMap();
            int[] size = getSize();
            map = new MapInstance(pos[0], pos[1], size[0], size[1]);
            map.load(() -> {
                load();
                if(enter != null)
                    enter(enter);
            });
        }
    }

    public void enter(Player player) {
        ObjectHandler.useStairs(player,-1, getTile(getInside()), 0, 1);
        players.add(player);
        player.getControllerManager().startController(new BossInstanceController(), this);
    }

    public static final int TELEPORTED = 0, LOGGED_OUT = 1, EXITED = 2, DIED = 3;

    public void leave(Player player, int type) {
        if (type == EXITED)
            ObjectHandler.useStairs(player,-1, getOutside(), 0, 2);
        else if (type == LOGGED_OUT)
            player.moveTo(getOutside());
        players.remove(player);
    }

    public Position getTile(Position tile) {
        return getTile(tile.getX(), tile.getY(), tile.getZ());
    }

    private Position getTile(int x, int y, int plane) {
        Position tile;
        int[] originalPos = map.getOriginalPos();
        tile = map.getTile((x - originalPos[0] * 8) % (map.getRatioX() * 64), (y - originalPos[1] * 8) % (map.getRatioY() * 64));
        tile.add(0, 0, plane);
        return tile;
    }

    public void setEnter(Player enter) {
        this.enter = enter;
    }
}
