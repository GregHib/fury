package com.fury.network.packet.impl;

import com.fury.game.content.skill.member.construction.TemporaryAtributtes.Key;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * This packet manages the input taken from chat box interfaces that allow input,
 * such as withdraw worldX, bank worldX, enter name of friend, etc.
 *
 * @author Gabriel Hannason
 */

public class EnterInputPacketListener implements PacketListener {


    @Override
    public void handleMessage(Player player, Packet packet) {
        switch (packet.getOpcode()) {
            case PacketConstants.ENTER_SYNTAX_OPCODE:
                String name = packet.readString();
                if (name == null)
                    return;
                if (player.getInputHandling() != null)
                    player.getInputHandling().handleSyntax(player, name);
                player.setInputHandling(null);
                break;
            case PacketConstants.ENTER_AMOUNT_OPCODE:
                int amount = packet.readInt();
                if (amount <= 0)
                    return;
                if (player.getControllerManager().getController() != null && player.getTemporaryAttributes().get(Key.SERVANT_REQUEST_ITEM) != null) {
                    Integer type = (Integer) player.getTemporaryAttributes().remove(Key.SERVANT_REQUEST_TYPE);
                    Integer item = (Integer) player.getTemporaryAttributes().remove(Key.SERVANT_REQUEST_ITEM);
                    if (!player.getHouse().isLoaded() || !player.getHouse().getPlayers().contains(player) || type == null || item == null)
                        return;
                    player.getHouse().getServantInstance().requestType(item, amount, type.byteValue());
                }
                if (player.getInputHandling() != null)
                    player.getInputHandling().handleAmount(player, amount);
                player.setInputHandling(null);
                break;
        }
    }

}
