package com.fury.game.content.skill.member.construction;

import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Enchantment;
import com.fury.game.entity.character.player.content.TeleportSpells;
import com.fury.core.model.item.Item;

import java.util.ArrayList;
import java.util.List;

public enum SpellTablet {
    ENCHANT_SAPPHIRE(8016, MagicSpells.ENCHANT_SAPPHIRE.getSpell(), HouseConstants.HObject.OAK_LECTURN, HouseConstants.HObject.EAGLE_LECTURN, HouseConstants.HObject.DEMON_LECTURN, HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    BONES_TO_BANANAS(8014, MagicSpells.BONES_TO_BANANAS.getSpell(), HouseConstants.HObject.DEMON_LECTURN, HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    VARROCK_TELEPORT(8007, TeleportSpells.VARROCK_TELEPORT.getSpell(), HouseConstants.HObject.OAK_LECTURN, HouseConstants.HObject.EAGLE_LECTURN, HouseConstants.HObject.DEMON_LECTURN, HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    ENCHANT_EMERALD(8017, MagicSpells.ENCHANT_EMERALD.getSpell(), HouseConstants.HObject.DEMON_LECTURN, HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    LUMBRIDGE_TELEPORT(8008, TeleportSpells.LUMBRIDGE_TELEPORT.getSpell(), HouseConstants.HObject.EAGLE_LECTURN, HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    FALADOR_TELEPORT(8009, TeleportSpells.FALADOR_TELEPORT.getSpell(), HouseConstants.HObject.EAGLE_LECTURN, HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    HOUSE_TELEPORT(8013, TeleportSpells.TELEPORT_TO_HOUSE.getSpell(), HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    CAMELOT_TELEPORT(8010, TeleportSpells.CAMELOT_TELEPORT.getSpell(), HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    ENCHANT_RUBY(8018, MagicSpells.ENCHANT_RUBY.getSpell(), HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    ARDOUGNE_TELEPORT(8011, TeleportSpells.ARDOUGNE_TELEPORT.getSpell(), HouseConstants.HObject.TEAK_EAGLE_LECTURN, HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    ENCHANT_DIAMOND(8019, MagicSpells.ENCHANT_DIAMOND.getSpell(), HouseConstants.HObject.TEAK_DEMON_LECTURN, HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    WATCHTOWER_TELEPORT(8012, TeleportSpells.WATCHTOWER_TELEPORT.getSpell(), HouseConstants.HObject.MAHOGANY_EAGLE_LECTURN),
    BONES_TO_PEACHES(8015, MagicSpells.BONES_TO_PEACHES.getSpell(), HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    ENCHANT_DRAGONSTONE(8020, MagicSpells.ENCHANT_DRAGONSTONE.getSpell(), HouseConstants.HObject.MAHOGANY_DEMON_LECTURN),
    ENCHANT_ONYX(8021, MagicSpells.ENCHANT_DRAGONSTONE.getSpell(), HouseConstants.HObject.MAHOGANY_DEMON_LECTURN);

    private int item;
    private Spell spell;
    private HouseConstants.HObject[] lecterns;

    SpellTablet(int item, Spell spell, HouseConstants.HObject... lecterns) {
        this.item = item;
        this.spell = spell;
        this.lecterns = lecterns;
    }

    public int getItem() {
        return item;
    }

    public Spell getSpell() {
        return spell;
    }

    public static SpellTablet[] getTablets(HouseConstants.HObject object) {
        List<SpellTablet> tablets = new ArrayList<>();
        for(SpellTablet tablet : values()) {
            for(HouseConstants.HObject obj : tablet.lecterns) {
                if(obj == object) {
                    tablets.add(tablet);
                    break;
                }
            }
        }
        return tablets.toArray(new SpellTablet[tablets.size()]);
    }

    public static SpellTablet forId(int id) {
        for(SpellTablet tablet : values())
            if(tablet.getItem() == id)
                return tablet;
        return null;
    }

    public void use(Player player, Item item, int slot) {
        Enchantment enchantment = Enchantment.get(MagicSpells.forSpellId(getSpell().getId()), item.getId());

        if(enchantment == null) {
            player.message("This spell can not be cast on this item.");
            return;
        }

        player.getInventory().delete(new Item(getItem()));
        player.getInventory().set(new Item(enchantment.getEnchanted(), item.getAmount()), slot);
        player.perform(enchantment.getAnimation());
        player.perform(enchantment.getGraphic());
    }
}
