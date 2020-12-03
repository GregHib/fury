package com.fury.game.system.files.logs;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class LoggedPlayerItem extends LoggedItem {

    String username;
    boolean received;

    public LoggedPlayerItem(Item item, Player with, boolean received) {
        super(item);
        username = with == null ? null : with.getUsername();
        this.received = received;
    }

    public LoggedPlayerItem(int id, long timestamp, int amount, Revision revision, String username, boolean received) {
        super(id, timestamp, amount, revision);
        this.username = username;
        this.received = received;
    }

    public LoggedPlayerItem(int id, long timestamp, int amount, String username, boolean received) {
        super(id, timestamp, amount);
        this.username = username;
        this.received = received;
    }

    public String getUsername() {
        return username;
    }

    public boolean wasReceived() {
        return received;
    }
}
