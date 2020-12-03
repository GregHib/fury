package com.fury.game.entity.character.player.link.transportation;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.dialogue.impl.transportation.AmuletOfGloryD;
import com.fury.game.content.dialogue.impl.transportation.CombatBraceletD;
import com.fury.game.content.dialogue.impl.transportation.GamesNecklaceD;
import com.fury.game.content.dialogue.impl.transportation.RingOfDuelingD;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;

public class JewelryTeleporting {

    public static void rub(Player player, Item item) {
        if(player.getMovement().isLocked() || player.getEmotesManager().isDoingEmote())
            return;
        if (player.getInterfaceId() > 0)
            player.getPacketSender().sendInterfaceRemoval();
        switch (item.getId()) {
            case 1712://Glory
            case 1710:
            case 1708:
            case 1706:
                player.getDialogueManager().startDialogue(new AmuletOfGloryD());
                break;
            case 11118://Combat Bracelet
            case 11120:
            case 11122:
            case 11124:
                player.getDialogueManager().startDialogue(new CombatBraceletD());
                break;
            case 2552://Ring of dueling
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
                player.getDialogueManager().startDialogue(new RingOfDuelingD());
                break;
            case 3853:
            case 3855:
            case 3857:
            case 3859:
            case 3861:
            case 3863:
            case 3865:
            case 3867://Games Necklace
                player.getDialogueManager().startDialogue(new GamesNecklaceD());
                break;
        }
        player.setSelectedSkillingItem(item);
    }

    public static void teleport(Player player, Position location) {
        if (!TeleportHandler.checkReqs(player, location)) {
            return;
        }
        if (!player.getTimers().getClickDelay().elapsed(4500) || player.getMovement().isLocked())
            return;
        Item item = player.getSelectedSkillingItem();
        if(item == null)
            return;
        int id = item.getId();
        if (!player.getInventory().contains(item) && !player.getEquipment().contains(item))
            return;

        if(!TeleportHandler.teleportPlayer(player, location, TeleportType.RING_TELE))
            return;

        boolean inventory = !player.getEquipment().contains(new Item(id));
        boolean bracelet = id >= 11118 && id <= 11124;
        boolean ring = id >= 2552 && id <= 2566;
        boolean games = id >= 3853 && id <= 3867 && ((id - 3853) % 2 == 0);
        if (id >= 1706 && id <= 1712 || bracelet || ring || games) {
            int newItem = bracelet || games ? (id + 2) : (id - 2);
            newItem = ring ? (id + 2) : newItem;

            if (id == 2566)
                newItem = -1;
            if (inventory) {
                player.getInventory().delete(item);
                player.getInventory().add(new Item(newItem));
                player.getInventory().refresh();
            } else {
                int slot = bracelet ? Slot.HANDS.ordinal() : Slot.AMULET.ordinal();
                slot = ring ? Slot.RING.ordinal() : slot;
                player.getEquipment().delete(slot);
                player.getEquipment().add(new Item(newItem));
                player.getEquipment().refresh();
            }
            if (newItem == 1704 || newItem == 11126) {
                String word = bracelet ? "bracelet" : "amulet";
                player.message("Your " + word + " has run out of charges.");
            } else if (newItem == -1) {
                player.message("Your ring of duelling crumbles to dust as you use its last charge.");
            }
        }
        player.setSelectedSkillingItem(null);
        player.getPacketSender().sendInterfaceRemoval();
    }
}
