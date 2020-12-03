package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.free.firemaking.Fire;
import com.fury.game.content.skill.free.firemaking.Firemaking;
import com.fury.core.model.item.FloorItem;
import com.fury.game.system.files.loaders.item.ItemSpawns;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener is used to pick up ground items
 * that exist in the world.
 * 
 * @author relex lawl
 */

public class GroundItemActionPacketListener implements PacketListener {

	public void pickupGroundItem(final Player player, Packet packet) {
        if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || !player.getTimers().getItemPickup().elapsed(500))
            return;
		final int y = packet.readLEShort();
		final int id = packet.readInt();
		final int x = packet.readLEShort();
        Revision revision = Revision.values()[packet.readUnsignedByte()];

        final Position position = new Position(x, y, player.getZ());

        final FloorItem floorItem = GameWorld.getRegions().getRegion(position).getFloorItem(id, position, player);

        if (floorItem == null || !floorItem.visibleFor(player))
            return;

        player.stopAll(false, true, !(player.getActionManager().getAction() instanceof Firemaking));
        player.setRouteEvent(new RouteEvent(floorItem, () -> {
            player.getDirection().face(position);
            if(player.getControllerManager().canTakeItem(floorItem)) {
                Achievements.finishAchievement(player, Achievements.AchievementData.GET_OFF_THE_FLOOR);
                FloorItemManager.removeGroundItem(player, floorItem);

                ItemSpawns.scheduleRespawnIfNeeded(floorItem.getItem(), floorItem.getTile());
            }
        }));
	}

	@Override
	public void handleMessage(final Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case PacketConstants.PICK_UP_ITEM_ACTION_OPCODE:
				pickupGroundItem(player, packet);
				break;
			case PacketConstants.SECOND_PICK_UP_ITEM_ACTION_OPCODE:
				handleSecondClick(player, packet);
				break;
		}
	}

	private void handleSecondClick(Player player, Packet packet) {
		final int x = packet.readLEShort();
		final int y = packet.readLEShortA();
		final int itemId = packet.readInt();
        Revision revision = Revision.values()[packet.readUnsignedByte()];

        final Position position = new Position(x, y, player.getZ());

        final FloorItem item = player.getRegion().getFloorItem(itemId, position, player);
        if (item == null)
            return;

		if(!player.getTimers().getItemPickup().elapsed(500))
			return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(item, () -> {
            for (Fire fire : Fire.values())
                if (fire.getLogId() == itemId) {
                    player.getActionManager().setAction(new Firemaking(fire, null, item));
                    return;
                }
        }));
	}
}
