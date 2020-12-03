package com.fury.network.packet.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.container.action.impl.*;
import com.fury.game.container.impl.BeastOfBurden;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.PriceChecker;
import com.fury.game.container.impl.Trade;
import com.fury.game.container.impl.bank.Bank;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.global.minigames.impl.OldDueling;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

import java.util.HashMap;
import java.util.Map;

public class ItemContainerActionPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)//Shouldn't this be in a packet check?
            return;

        int widget = packet.readInt();
        int slot = packet.readShort();
        int id = packet.readInt();
        int option = packet.readByte();

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Container action [widget, id, slot, option]:[" + widget + ", " + id + ", " + slot + ", " + option + "]");

        ContainerActions action = get(widget);
        if(action != null)
            run(player, action, option, widget, id, slot);
    }

    static Map<int[], ContainerActions> actions = new HashMap<>();

    private static void put(ContainerActions action, int... widgets) {
        actions.put(widgets, action);
    }

    private static ContainerActions get(int widget) {
        for(int[] ids : actions.keySet()) {
            for(int id : ids) {
                if(widget == id)
                    return actions.get(ids);
            }
        }
        return null;
    }

    static {
        put(new DungeoneeringRewardActions(), DungeoneeringRewards.REWARDS_ITEM_CONTAINER_INTERFACE);
        put(new SumonningInfusionActions(), Infusion.POUCH_ITEMS_INTERFACE_ID, Infusion.SCROLL_ITEMS_INTERFACE_ID);
        put(new GECollectBuyActions(), GrandExchange.COLLECT_ITEM_PURCHASE_INTERFACE);
        put(new GECollectSellActions(), GrandExchange.COLLECT_ITEM_SALE_INTERFACE);
        put(new TradeInventoryActions(), Trade.INTERFACE_ID);
        put(new TradeActions(), Trade.INTERFACE_REMOVAL_ID);
        put(new DuelingRemovalActions(), OldDueling.INTERFACE_REMOVAL_ID);
        put(new EquipmentActions(), Equipment.INVENTORY_INTERFACE_ID);
        put(new BankActions(), Bank.INTERFACE_ID);
        put(new BankInventoryActions(), Bank.INVENTORY_INTERFACE_ID);
        put(new ShopActions(), DungeonConstants.RESOURCE_ITEM_CHILD_ID, DungeonConstants.TOOLS_ITEM_CHILD_ID, Shop.ITEM_CHILD_ID);
        put(new ShopInventoryActions(), Shop.INVENTORY_INTERFACE_ID);
        put(new BeastOfBurdenInventoryActions(), BeastOfBurden.INVENTORY_INTERFACE_ID);
        put(new BeastOfBurdenActions(), BeastOfBurden.INTERFACE_ID);
        put(new PriceCheckerInventoryActions(), PriceChecker.INVENTORY_INTERFACE_ID);
        put(new PriceCheckerActions(), PriceChecker.CONTAINER_ID);
        put(new SmithingActions(), 1119, 1120, 1121, 1122, 1123);
    }

    private void run(Player player, ContainerActions action, int option, int widget, int id, int slot) {
        switch (option) {
            case 0:
                action.first(player, widget, id, slot);
                break;
            case 1:
                action.second(player, widget, id, slot);
                break;
            case 2:
                action.third(player, widget, id, slot);
                break;
            case 3:
                action.fourth(player, widget, id, slot);
                break;
            case 4:
                action.fifth(player, widget, id, slot);
                break;
            case 5:
                action.sixth(player, widget, id, slot);
                break;
            case 6:
                action.seventh(player, widget, id, slot);
                break;
            case 7:
                action.eighth(player, widget, id, slot);
                break;
        }
    }
}
