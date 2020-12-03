package com.fury.game.content.skill.free.firemaking;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public enum Logs {
    LOG(1511, 1, 40, 30),
    ACHEY(2862, 1, 40, 30),
    OAK(1521, 15, 60, 40),
    WILLOW(1519, 30, 90, 45),
    TEAK(6333, 35, 105, 45),
    ARCTIC_PINE(10810, 42, 125, 45),
    MAPLE(1517, 45, 143, 45),
    MAHOGANY(6332, 50, 158, 45),
    EUCALYPTUS(12581, 58, 194, 45),
    YEW(1515, 60, 203, 50),
    MAGIC(1513, 75, 304, 50);

    private int logId, level, burnTime;
    private int xp;

    private Logs(int logId, int level, int xp, int burnTime) {
        this.logId = logId;
        this.level = level;
        this.xp = xp;
        this.burnTime = burnTime;
    }

    public int getLogId() {
        return logId;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public static Logs getLogData(Player p, int log) {
        for (final Logs l : Logs.values()) {
            if (log == l.getLogId() || log == -1 && p.getInventory().contains(new Item(l.getLogId()))) {
                return l;
            }
        }
        return null;
    }

}