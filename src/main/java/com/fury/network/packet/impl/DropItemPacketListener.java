package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.hunter.HunterData;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.entity.character.player.content.Sounds.Sound;
import com.fury.core.model.item.Item;
import com.fury.game.entity.item.content.ItemDegrading;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

/**
 * This packet listener is called when a player drops an item they
 * have placed in their inventory.
 *
 * @author relex lawl
 */

public class DropItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int itemId = packet.readInt();
        int interfaceIndex = packet.readUnsignedShort();
        int itemSlot = packet.readUnsignedShortA();
        if (player.getHealth().getHitpoints() <= 0 || player.getInterfaceId() > 0 || player.getMovement().getTeleporting())
            return;

        if (!player.getInventory().validate(itemId, itemSlot))
            return;

        Item item = player.getInventory().get(itemSlot);

        player.getPacketSender().sendInterfaceRemoval();
        player.getActionManager().forceStop();

        if (player.isCommandViewing())
            return;

        if (!player.getControllerManager().canDropItem(item))
            return;

        if (player.getPetManager().spawnPet(itemId, true))
            return;

        if (item != null && item.getId() != -1 && item.getAmount() >= 1 && item.tradeable() && !(item.getId() >= 15775 && item.getId() <= 16272)) {
            player.getInventory().delete(itemSlot);
            player.getInventory().refresh();
            if (item.getId() == 4045) {
                player.getCombat().applyHit(new Hit((player.getHealth().getHitpoints() - 1) == 0 ? 1 : player.getHealth().getHitpoints() - 1, HitMask.CRITICAL, CombatIcon.BLUE_SHIELD));
                player.graphic(1750);
                player.message("The potion explodes in your face as you drop it!");
            } else if (item.getId() == 10034 || item.getId() == 10033) {
                player.message("You release the chinchompa" + (item.getAmount() > 1 ? "s" : "") + " into the wild.");
            } else if (HunterData.isSalamander(item.getId())) {
                player.message("You release the salamander and it darts away.");
            } else if (item.getId() >= 2412 && item.getId() <= 2414) {
                player.message("The cape vanishes as it touches the ground.");
            } else {
                if (ItemDegrading.degradesOnDrop(item.getId()))
                    item.setId(ItemDegrading.getDegradedItemId(item.getId()));

                FloorItemManager.addGroundItem(item, player.copyPosition(), player);
            }
            player.getLogger().addDrop(item);
            Sounds.sendSound(player, Sound.DROP_ITEM);
        } else {
            destroyItemInterface(player, item);
        }
    }

    public static void destroyItemInterface(Player player, Item item) {//Destroy item created by Remco
        player.setUntradeableDropItem(item);
        String[][] info = {//The info the dialogue gives
                {"Are you sure you want to discard this item?", "14174"},
                {"Yes.", "14175"}, {"No.", "14176"}, {"", "14177"},
                {"This item will vanish once it hits the floor.", "14182"}, {"You cannot get it back if discarded.", "14183"},
                {item.getName(), "14184"}};
        player.getPacketSender().sendItemOnInterface(14171, item.getId(), item.getRevision(), 0, item.getAmount());
        for (int i = 0; i < info.length; i++)
            player.getPacketSender().sendString(Integer.parseInt(info[i][1]), info[i][0]);
        player.getPacketSender().sendChatboxInterface(14170);
    }
}
