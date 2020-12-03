package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.game.content.controller.impl.FirstAdventureController;
import com.fury.game.system.files.loaders.item.ItemExamines;
import com.fury.game.content.global.treasuretrails.ClueTiers;
import com.fury.game.content.global.treasuretrails.TreasureTrails;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.util.Misc;

public class ExamineItemPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int itemId = packet.readInt();
        Revision revision = Revision.values()[packet.readByte()];
        int widgetId = packet.readShort();

        if(player.getControllerManager().getController() instanceof FirstAdventureController)
            return;

        Item item = new Item(itemId, revision);
        if (itemId == 995 || itemId == 18201) {
            int amount = widgetId == 5382 ? player.getBank().getTab(item).getAmount(item) : player.getInventory().getAmount(item);
            if (amount < 99999) {
                player.message("Lovely money!");
            } else {
                player.message(Misc.insertCommasToNumber("" + amount) + "x Coins.");
            }
            return;
        }
        if (item.getName().toLowerCase().contains("clue scroll")) {
            ClueTiers tier = ClueTiers.get(itemId);
            if(tier == null)
                return;
            if (itemId == tier.getScrollId() && TreasureTrails.getActiveClues(player)[tier.ordinal()]) {
                switch (player.getClueScroll(tier.ordinal()).getType()) {
                    case NULL:
                    default:
                        player.message("A clue!");
                        break;
                    case MAP:
                        player.message("A piece of the world map, but where?");
                        break;
                    case SIMPLE:
                    case EMOTE:
                        player.message("A set of instructions to be followed.");
                        break;
                }
                return;
            } else if(itemId == tier.getScrollId() && !TreasureTrails.getActiveClues(player)[tier.ordinal()]) {
                player.message("A clue!");
                return;
            }
        }
        player.message(ItemExamines.getExamine(item));
    }

}
