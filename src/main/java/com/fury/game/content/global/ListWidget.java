package com.fury.game.content.global;

import com.fury.core.model.node.entity.actor.figure.player.Player;

public class ListWidget {

    public static void reset(Player player) {
        for (int i = 8145; i < 8196; i++)
            player.getPacketSender().sendString(i, "");
        for (int i = 12174; i < 12224; i++)
            player.getPacketSender().sendString(i, "");
        player.getPacketSender().sendString(8136, "Close window");
    }

    public static void display(Player player, String title, String subtitle, String[] lines) {
        player.getPacketSender().sendString(637, title).sendInterface(8134);
        player.getPacketSender().sendString(8145, subtitle).sendInterfaceComponentScrollPosition(8143, 0);

        for(int i = 0; i <= 100; i++) {
            int toSend = 8147 + i > 8195 ? 12174 + i - 49 : 8147 + i;
            player.getPacketSender().sendString(toSend, i >= lines.length ? "" : lines[i]);
        }
    }
}
