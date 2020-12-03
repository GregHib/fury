package com.fury.game.content.skill.member.herblore;

import com.fury.core.model.item.Item;

public enum RawIngredients {
    UNICORN_HORN(237, new Item(235, 1)),
    CHOCOLATE_BAR(1973, new Item(1975, 1)),
    KEBBIT_TEETH(10109, new Item(10111, 1)),
    GORAK_CLAW(9016, new Item(9018, 1)),
    BIRDS_NEST(5075, new Item(6693, 1)),
    DESERT_GOAT_HORN(9735, new Item(9736, 1)),
    BLUE_DRAGON_SCALES(243, new Item(241, 1)),
    SPRING_SQ_IRK(10844, new Item(10848, 1)),
    SUMMER_SQ_IRK(10845, new Item(10849, 1)),
    AUTUMN_SQ_IRK(10846, new Item(10850, 1)),
    WINTER_SQ_IRK(10847, new Item(10851, 1)),
    CHARCOAL(973, new Item(704, 1)),
    RUNE_SHARDS(6466, new Item(6467, 1)),
    ASHES(592, new Item(8865, 1)),
    POISON_KARAMBWAN(3146, new Item(3152, 1)),
    SUQAH_TOOTH(9079, new Item(9082, 1)),
    FISHING_BAIT(313, new Item(12129, 1)),
    DIAMOND_ROOT(14703, new Item(14704, 1)),
    BLACK_MUSHROOM(4620, new Item(4622, 1)),
    MUD_RUNES(4698, new Item(9594, 1)),
    WYVERN_BONES(6812, new Item(6810, 1)),
    ARMADYL_DUST(21776, new Item(21774, 8));

    public static RawIngredients forId(int itemId) {
        for (RawIngredients rawIngredients : RawIngredients.values()) {
            if (itemId == rawIngredients.rawId)
                return rawIngredients;
        }
        return null;
    }

    private final short rawId;
    private final Item crushedItem;

    RawIngredients(int rawId, Item crushedItem) {
        this.rawId = (short) rawId;
        this.crushedItem = crushedItem;
    }

    public short getRawId() {
        return rawId;
    }

    public Item getCrushedItem() {
        return crushedItem;
    }
}
