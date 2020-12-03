package com.fury.network.packet.impl;

import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFloorItemContext;
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.world.map.Position;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * Handles magic on items.
 *
 * @author Gabriel Hannason
 */
public class MagicOnItemsPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int opcode = packet.getOpcode();
        switch (opcode) {
            case PacketConstants.MAGIC_ON_GROUNDITEMS_OPCODE:
                handleGroundItems(player, packet);
                break;
            case PacketConstants.MAGIC_ON_ITEMS_OPCODE:
                handleInventoryItems(player, packet);
                break;
        }
    }

    @SuppressWarnings("unused")
    private void handleInventoryItems(Player player, Packet packet) {
        int slot = packet.readShort();
        int itemId = packet.readInt();
        int widget = packet.readShort();
        int spellId = packet.readInt();

        if (!player.getTimers().getClickDelay().elapsed(1300))
            return;

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);

        MagicSpells magicSpell = MagicSpells.forSpellId(spellId);
        if (magicSpell == null)
            return;

        if(magicSpell.getSpell().cast(player, new SpellOnItemContext(item, slot)))
            player.getTimers().getClickDelay().reset();
    }

    public void handleGroundItems(Player player, Packet packet) {
        final int y = packet.readLEShort();
        final int id = packet.readInt();
        final int x = packet.readLEShort();
        final int spellId = packet.readUnsignedShortA();

        final MagicSpells spell = MagicSpells.forSpellId(spellId);
        if (spell == null)
            return;

        if (!spell.getSpell().canCast(player))
            return;

        Position position = new Position(x, y, player.getZ());

        final FloorItem item = player.getRegion().getFloorItem(id, position, player);

        if (item == null)
            return;

        if(spell.getSpell().cast(player, new SpellOnFloorItemContext(item)))
            player.getTimers().getClickDelay().reset();
    }

}
