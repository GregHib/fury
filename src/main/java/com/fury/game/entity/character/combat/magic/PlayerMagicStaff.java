package com.fury.game.entity.character.combat.magic;

import com.fury.game.entity.character.combat.equipment.weapon.WeaponInterface;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * A set of constants representing the staves that can be used in place of
 * runes.
 * 
 * @author lare96
 */
public enum PlayerMagicStaff {

    AIR(new Item[] { new Item(1381), new Item(1397), new Item(1405)}, new int[] { 556 }),
    WATER(new Item[] { new Item(1383), new Item(1395), new Item(1403)}, new int[] { 555 }),
    EARTH(new Item[] { new Item(1385), new Item(1399), new Item(1407)}, new int[] { 557 }),
    FIRE(new Item[] { new Item(1387), new Item(1393), new Item(1401)}, new int[] { 554 }),
    MUD(new Item[] { new Item(6562), new Item(6563)}, new int[] { 555, 557 }),
    LAVA(new Item[] { new Item(3053), new Item(3054)}, new int[] { 554, 557 }),
    DUNG_WATER(new Item[] { new Item(16997), new Item(16999)}, new int[] { 17781 }),
    DUNG_EARTH(new Item[] { new Item(17001), new Item(17003)}, new int[] { 17782 }),
    DUNG_FIRE(new Item[] { new Item(17005), new Item(17007)}, new int[] { 17783 }),
    DUNG_AIR(new Item[] { new Item(17009), new Item(17011)}, new int[] { 17780 }),
    CATALYTIC(new Item[] { new Item(17013), new Item(17015)}, new int[] { 17780, 17781, 17782, 17783 });

    /** The staves that can be used in place of runes. */
    private Item[] staves;

    /** The runes that the staves can be used for. */
    private int[] runes;

    /**
     * Create a new {@link PlayerMagicStaff}.
     * 
     * @param itemIds
     *            the staves that can be used in place of runes.
     * @param runeIds
     *            the runes that the staves can be used for.
     */
    PlayerMagicStaff(Item[] itemIds, int[] runeIds) {
        this.staves = itemIds;
        this.runes = runeIds;
    }

    /**
     * Suppress items in the argued array if any of the items match the runes
     * that are represented by the staff the argued player is wielding.
     * 
     * @param player
     *            the player to suppress runes for.
     * @param runesRequired
     *            the runes to suppress.
     * @return the new array of items with suppressed runes removed.
     */
    public static Item[] suppressRunes(Player player, Item[] runesRequired) {
        if (player.getWeapon() == WeaponInterface.STAFF) {
            for (PlayerMagicStaff m : values()) {
                if (player.getEquipment().contains(m.staves)) {
                    for (int id : m.runes) {
                        for (int i = 0; i < runesRequired.length; i++) {
                            if (runesRequired[i] != null && runesRequired[i].getId() == id) {
                                runesRequired[i] = null;
                            }
                        }
                    }
                }
            }
            return runesRequired;
        }

        return runesRequired;
    }
}
