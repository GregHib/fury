package com.fury.game.entity.character.player.content;

import com.fury.game.content.combat.magic.spell.modern.enchant.EnchantSpell;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;

public enum Enchantment {
    SAPPHIRE_RING(MagicSpells.ENCHANT_SAPPHIRE, 1637, 2550, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    SAPPHIRE_NECKLACE(MagicSpells.ENCHANT_SAPPHIRE, 1656, 3853, new Animation(719), new Graphic(114, GraphicHeight.HIGH)),
    SAPPHIRE_BRACELET(MagicSpells.ENCHANT_SAPPHIRE, 11072, 11074, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    SAPPHIRE_AMULET(MagicSpells.ENCHANT_SAPPHIRE, 1694, 1727, new Animation(719), new Graphic(114, GraphicHeight.HIGH)),
    EMERALD_RING(MagicSpells.ENCHANT_EMERALD, 1639, 2552, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    EMERALD_NECKLACE(MagicSpells.ENCHANT_EMERALD, 1658, 5521, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    EMERALD_BRACELET(MagicSpells.ENCHANT_EMERALD, 11076, 11079, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    EMERALD_AMULET(MagicSpells.ENCHANT_EMERALD, 1696, 1729, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    EMERALD_SICKLE(MagicSpells.ENCHANT_EMERALD, 13155, 13156, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    RUBY_RING(MagicSpells.ENCHANT_RUBY, 1641, 2568, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    RUBY_NECKLACE(MagicSpells.ENCHANT_RUBY, 1660, 11194, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    RUBY_BRACELET(MagicSpells.ENCHANT_RUBY, 11085, 11088, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    RUBY_AMULET(MagicSpells.ENCHANT_RUBY, 1698, 1725, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    DIAMOND_RING(MagicSpells.ENCHANT_DIAMOND, 1643, 2570, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    DIAMOND_NECKLACE(MagicSpells.ENCHANT_DIAMOND, 1662, 11090, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    DIAMOND_BRACELET(MagicSpells.ENCHANT_DIAMOND, 11092, 11095, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    DIAMOND_AMULET(MagicSpells.ENCHANT_DIAMOND, 1700, 1731, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    DRAGONSTONE_RING(MagicSpells.ENCHANT_DRAGONSTONE, 1645, 2572, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    DRAGONSTONE_NECKLACE(MagicSpells.ENCHANT_DRAGONSTONE, 1664, 11105, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    DRAGONSTONE_BRACELET(MagicSpells.ENCHANT_DRAGONSTONE, 11115, 11118, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    DRAGONSTONE_AMULET(MagicSpells.ENCHANT_DRAGONSTONE, 1702, 1712, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    ONYX_RING(MagicSpells.ENCHANT_ONYX, 6575, 6583, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    ONYX_NECKLACE(MagicSpells.ENCHANT_ONYX, 6577, 11128, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    ONYX_BRACELET(MagicSpells.ENCHANT_ONYX, 11130, 11133, new Animation(712), new Graphic(238, GraphicHeight.HIGH)),
    ONYX_AMULET(MagicSpells.ENCHANT_ONYX, 6581, 6585, new Animation(719), new Graphic(115, GraphicHeight.HIGH)),
    ;
    private MagicSpells spell;
    private int item, enchanted;
    private Animation animation;
    private Graphic graphic;
    Enchantment(MagicSpells spell, int item, int enchanted, Animation animation, Graphic graphic) {
        this.spell = spell;
        this.item = item;
        this.enchanted = enchanted;
        this.animation = animation;
        this.graphic = graphic;
    }

    public static Enchantment get(MagicSpells spell, int id) {
        for(Enchantment enchantment : values())
            if(enchantment.item == id && enchantment.spell == spell)
                return enchantment;
        return null;
    }

    public static Enchantment get(EnchantSpell spell, int id) {
        for(Enchantment enchantment : values())
            if(enchantment.item == id && enchantment.spell.getSpell() == spell)
                return enchantment;
        return null;
    }

    public int getEnchanted() {
        return enchanted;
    }

    public Animation getAnimation() {
        return animation;
    }

    public Graphic getGraphic() {
        return graphic;
    }
}
