package com.fury.game.content.skill.free.crafting.gems;

import com.fury.core.model.item.Item;


/**
 * Enum for gems
 *
 * @author Raghav
 *
 */
public enum Gem {
    OPAL(new Item(1625), new Item(1609), 15.0, 1, 886),
    JADE(new Item(1627), new Item(1611), 20, 13, 886),
    RED_TOPAZ(new Item(1629), new Item(1613), 25, 16, 887),
    SAPPHIRE(new Item(1623), new Item(1607), 50, 20, 888),
    EMERALD(new Item(1621), new Item(1605), 67, 27, 889),
    RUBY(new Item(1619), new Item(1603), 85, 34, 887),
    DIAMOND(new Item(1617), new Item(1601), 107.5, 43, 890),
    DRAGONSTONE(new Item(1631), new Item(1615), 137.5, 55, 885),
    ONYX(new Item(6571), new Item(6573), 167.5, 67, 2717),
    GRAY_SHELL_ROUND(new Item(3345), new Item(3327), 35.5, 15, -1),
    RED_AND_BLACK_SHELL_ROUND(new Item(3347), new Item(3329), 35.5, 15, -1),
    OCHRE_SHELL_ROUND(new Item(3349), new Item(3331), 35.5, 15, -1),
    BLUE_SHELL_ROUND(new Item(3351), new Item(3333), 35.5, 15, -1),
    BROKEN_SHELL_ROUND(new Item(3353), new Item(3335), 35.5, 15, -1),
    GRAY_SHELL_POINTY(new Item(3355), new Item(3337), 35.5, 15, -1),
    RED_AND_BLACK_SHELL_POINTY(new Item(3357), new Item(3339), 35.5, 15, -1),
    OCHRE_SHELL_POINTY(new Item(3359), new Item(3341), 35.5, 15, -1),
    BLUE_SHELL_POINTY(new Item(3361), new Item(3343), 35.5, 15, -1);

    private double experience;
    private int levelRequired;
    private Item uncut, cut;

    private int emote;

    private Gem(Item uncut, Item cut, double experience, int levelRequired, int emote) {
        this.uncut = uncut;
        this.cut = cut;
        this.experience = experience;
        this.levelRequired = levelRequired;
        this.emote = emote;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public double getExperience() {
        return experience;
    }

    public Item getUncut() {
        return uncut;
    }

    public Item getCut() {
        return cut;
    }

    public int getEmote() {
        return emote;
    }

}
