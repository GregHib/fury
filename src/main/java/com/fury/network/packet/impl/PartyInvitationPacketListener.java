package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.Daemonheim;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

/**
 * Created by Greg on 21/09/2016.
 */
public class PartyInvitationPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        if (player.getMovement().getTeleporting())
            return;

        if (!(player.getControllerManager().getController() instanceof Daemonheim)) {
            player.message("You must be in Daemonheim to join a party.");
            return;
        }
        int index = packet.readLEShort();
        if (index < 0 || index > GameWorld.getPlayers().capacity())
            return;
        Player target = GameWorld.getPlayers().get(index);
        if (target == null || !player.isWithinDistance(target, 13))
            return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(target, () -> {
            if (target.getIndex() != player.getIndex())
                player.getDungManager().acceptInvite(target);
        }));
    }

}
