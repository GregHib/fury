package com.fury.game.content.controller.impl;

import com.fury.game.container.impl.equip.Slot;
import com.fury.util.NameUtils;

public enum Rule {
    NO_RANGED,
    NO_MELEE,
    NO_MAGIC,
    NO_SPECIAL_ATTACK,
    LOCK_WEAPONS,
    NO_FORFEIT,
    NO_DRINKS,
    NO_FOOD,
    NO_PRAYER,
    NO_MOVEMENT,
    OBSTACLES,
    NO_HEAD,
    NO_CAPE,
    NO_AMULET,
    NO_ARROWS,
    NO_WEAPON,
    NO_BODY,
    NO_SHIELD,
    NO_LEGS,
    NO_HANDS,
    NO_FEET,
    NO_RING;

    @Override
    public String toString() {
        return NameUtils.capitalize(name().toLowerCase().replaceAll("_", " "));
    }

    public Slot getSlot() {
        switch (this) {
            case NO_HEAD:
                return Slot.HEAD;
            case NO_CAPE:
                return Slot.CAPE;
            case NO_AMULET:
                return Slot.AMULET;
            case NO_ARROWS:
                return Slot.ARROWS;
            case NO_WEAPON:
                return Slot.WEAPON;
            case NO_BODY:
                return Slot.BODY;
            case NO_SHIELD:
                return Slot.SHIELD;
            case NO_LEGS:
                return Slot.LEGS;
            case NO_HANDS:
                return Slot.HANDS;
            case NO_FEET:
                return Slot.FEET;
            case NO_RING:
                return Slot.RING;
        }

        return null;
    }
}
