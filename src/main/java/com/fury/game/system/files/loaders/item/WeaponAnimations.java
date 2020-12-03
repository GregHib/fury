package com.fury.game.system.files.loaders.item;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.entity.character.CharacterAnimations;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * A static utility class that contains methods for changing the appearance
 * animation for a player whenever a new weapon is equipped or an existing item
 * is unequipped.
 *
 * @author lare96
 */
public final class WeaponAnimations {

    /**
     * Executes an animation for the argued player based on the animation of the
     * argued item.
     *
     * @param player the player to animate.
     */
    public static void update(Player player) {
        //player.getCharacterAnimations().reset();
        player.setCharacterAnimations(getUpdateAnimation(player));
    }

    public static CharacterAnimations getUpdateAnimation(Player player) {
        Item weapon = player.getEquipment().get(Slot.WEAPON);
        return new CharacterAnimations(ItemAnimations.getStandAnimation(weapon), ItemAnimations.getWalkAnimation(weapon), ItemAnimations.getRunAnimation(weapon));
    }

    public static int getBlockAnimation(Player player) {
        Item shield = player.getEquipment().get(Slot.SHIELD);
        return ItemAnimations.getBlockAnimation(shield);
    }

    public static int getAttackAnimation(Item weapon, int attackStyle) {
        if (weapon.getId() != -1)
            return ItemAnimations.getAttackAnimation(weapon, attackStyle);
        return attackStyle == 2 ? 423 : 422;
    }
}
