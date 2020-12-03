package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.character.player.link.transportation.JewelryTeleporting;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;

public class EquipmentActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        player.getEquipment().handleUnequip(id, slot);
    }


    @Override
    public void third(Player player, int widget, int id, int slot) {
        if(!player.getEquipment().validate(id, slot))
            return;

        Item item = player.getEquipment().get(slot);

        if(player.getEmotesManager().handleItem(item.getId()))
            return;

        switch (item.getId()) {
            case 13281:
            case 13282:
            case 13283:
            case 13284:
            case 13285:
            case 13286:
            case 13287:
            case 13288://Ring of slayer
                player.getSlayerManager().slayerRingTeleport(item);
                break;
            case 15707://Kinship
                TeleportHandler.teleportPlayer(player, new Position(3450, 3715), TeleportType.KINSHIP_TELE);
                break;
            case 20769://Comp cape
            case 20767://Max cape
                if(player.openInterface(19109))
                    player.getPacketSender().sendCompCapeInterfaceColours();
                break;
            case 1712://Glory
            case 1710:
            case 1708:
            case 1706:
            case 11118://Combat Bracelet
            case 11120:
            case 11122:
            case 11124:
            case 2552://Ring of dueling
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
            case 3853://Games Necklace
            case 3855:
            case 3857:
            case 3859:
            case 3861:
            case 3863:
            case 3865:
            case 3867:
                JewelryTeleporting.rub(player, item);
                break;
            case 1704:
                player.message("Your amulet has run out of charges.");
                break;
            case 11126:
                player.message("Your bracelet has run out of charges.");
                break;
            case 11283:
                int charges = player.getDfsCharges();
                if (charges >= 20 || player.getRights() == PlayerRights.DEVELOPER) {
                    CombatConstants.handleDragonFireShield(player);
                } else
                    player.message("Your shield doesn't have enough power yet. It has " + player.getDfsCharges() + "/20 dragon-fire charges.");
                break;
        }
    }
}
