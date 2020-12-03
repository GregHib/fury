package com.fury.network.packet.impl;

import com.fury.core.action.PlayerActionBus;
import com.fury.core.action.actions.ItemOptionAction;
import com.fury.game.container.impl.Inventory;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener manages the equip action a player
 * executes when wielding or equipping an item.
 *
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        int id = packet.readInt();
        int slot = packet.readShortA();
        int interfaceId = packet.readShortA();

        if (player.getRights().isOrHigher(PlayerRights.DEVELOPER))
            player.getPacketSender().sendConsoleMessage("Item [option, id, slot] [" + 4 + ", " + id + ", " + slot + "]");

        if(player.getMovement().isLocked() || player.getEmotesManager().isDoingEmote())
            return;

        if (player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */ && player.getInterfaceId() != 19109) {
            player.getPacketSender().sendInterfaceRemoval();
            //return;
        }

        if(player.isCommandViewing()) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        if (!player.getInventory().validate(id, slot))
            return;

        Item item = player.getInventory().get(slot);

        if (item != null && PlayerActionBus.publish(player, new ItemOptionAction(item, slot, 4)))
            return;

        switch (interfaceId) {
            case Inventory.INTERFACE_ID:
                player.getInventory().handleItemClick(id, slot);
                break;
        }
    }

}