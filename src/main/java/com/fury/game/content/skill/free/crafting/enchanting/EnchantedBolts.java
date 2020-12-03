package com.fury.game.content.skill.free.crafting.enchanting;

public enum EnchantedBolts {
    OPAL(879, 9236),
    SAPPHIRE(9337, 9240),
    JADE(9335, 9237),
    PEARL(880, 9238),
    EMERALD(9338, 9241),
    RED_TOPAZ(9336, 9239),
    RUBY(9339, 9242),
    DIAMOND(9340, 9243),
    DRAGONSTONE(9341, 9244),
    ONYX(9342, 9245);

    private int boltId;

    public int getBoltId() {
        return boltId;
    }

    public int getEnchantedBoltId() {
        return enchantedBoltId;
    }

    private int enchantedBoltId;

    EnchantedBolts(int bolt, int enchanted) {
        this.boltId = bolt;
        this.enchantedBoltId = enchanted;
    }

    public static EnchantedBolts forId(int bolt) {
        for(EnchantedBolts bolts : values())
            if(bolts.boltId == bolt)
                return bolts;
        return null;
    }
}
