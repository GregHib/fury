package com.fury.game.entity.character.player.content;

import com.fury.game.container.impl.Inventory;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class OrnamentKits {

    private OrnamentKits() {
        //this way you cant construct this class by mistake
    }

    private static enum Kits {
        FURY_ORNAMENT(6585, 19333, 19335),
        DRAGON_PLATEBODY_OR(14479, 19350, 19337),
        DRAGON_PLATELEGS_OR(4087, 19348, 19338),
        DRAGON_PLATESKIR_OR(4585, 19348, 19339),
        DRAGON_SQ_SHIELD_OR(1187, 19352, 19340),
        DRAGON_FULL_HELM_OR(11335, 19346, 19336),
        DRAGON_KITESHIELD_OR(24365, 25312, 25320),

        DRAGON_FULL_HELM_SP(11335, 19354, 19341),
        DRAGON_PLATEBODY_SP(14479, 19358, 19342),
        DRAGON_PLATELEGS_SP(4087, 19356, 19343),
        DRAGON_PLATESKIR_SP(4585, 19356, 19344),
        DRAGON_SQ_SHIELD_SP(1187, 19360, 19345),
        DRAGON_KITESHIELD_SP(24365, 25314, 25321),

        DRAGONBONE_MAGE_HAT(6918, 24352, 24354),
        DRAGONBONE_MAGE_TOP(6916, 24352, 24355),
        DRAGONBONE_MAGE_BOTTOMS(6924, 24352, 24356),
        DRAGONBONE_MAGE_GLOVES(6922, 24352, 24357),
        DRAGONBONE_MAGE_BOOTS(6920, 24352, 24358),

        DRAGONBONE_FULL_HELM(11335, 24352, 24359),
        DRAGONBONE_PLATEBODY(14479, 24352, 24360),
        DRAGONBONE_GLOVES(7461, 24352, 24361),
        DRAGONBONE_BOOTS(11732, 24352, 24362),
        DRAGONBONE_PLATELEGS(4087, 24352, 24363),
        DRAGONBONE_PLATESKIRT(4585, 24352, 24364);


        private int kitId, fromId, toId;

        Kits(int fromId, int kitId, int toId) {
            this.fromId = fromId;
            this.kitId = kitId;
            this.toId = toId;
        }
    }

    private static Kits getKit(Item item1, Item item2) {
        for (Kits kit : Kits.values()) {
            if (Inventory.contains(kit.kitId, kit.fromId, item1, item2))
                return kit;
        }
        return null;
    }

    public static Kits getKit(Item item) {
        for (Kits kit : Kits.values()) {
            if (kit.toId == item.getId())
                return kit;
        }
        return null;
    }

    public static boolean attachKit(Player player, Item item1, Item item2, int slot1, int slot2) {
        Kits kit = getKit(item1, item2);
        if (kit == null)
            return false;
        player.getInventory().delete(slot1);
        player.getInventory().set(new Item(kit.toId), slot2);
        player.getInventory().refresh();
        player.message("You attach the kit to the " + item1.getDefinition().getName() + ".");
        return true;
    }

    public static boolean splitKit(Player player, Item item) {
        Kits kit = getKit(item);
        if (kit == null)
            return false;
        if (!player.getInventory().hasRoom()) {
            player.message("Not enough space in your inventory.");
            return true;
        }
        player.getInventory().delete(item);
        player.getInventory().add(new Item(kit.fromId, 1));
        player.getInventory().add(new Item(kit.kitId, 1));
        player.message("You split the " + item.getName() + ".");
        return true;
    }

}