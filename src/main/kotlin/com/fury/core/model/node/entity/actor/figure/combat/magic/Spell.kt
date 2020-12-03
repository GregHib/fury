package com.fury.core.model.node.entity.actor.figure.combat.magic

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.BlankSpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.magic.Autocasting
import com.fury.game.entity.character.combat.magic.PlayerMagicStaff
import java.util.*

/**
 * A parent class represented by any generic spell able to be cast by an
 * [Figure].
 *
 * @author lare96
 * @author Greg
 */
abstract class Spell {

    companion object {
        val BLANK_CONTEXT = BlankSpellContext()
    }

    abstract val id: Int

    open val level: Int = 0

    open fun hasRequiredLevel(figure: Figure): Boolean {
        return (figure as? Player)?.skills?.hasRequirement(Skill.MAGIC, level, "cast this spell") ?: true
    }

    open val experience: Double = 0.0

    open val items: Optional<Array<Item>> = Optional.empty()

    open fun hasRequiredItems(player: Player, delete: Boolean): Boolean {
        if (items.isPresent) {

            var req = items.get()

            //Convert to dungeoneering runes if in a dungeon
            if (player.dungManager.isInside)
                req = player.dungManager.handleDungRunes(player, req)

            // Suppress the runes based on the staff, we then use the new array
            // of items that don't include suppressed runes.
            val items = PlayerMagicStaff.suppressRunes(player, req.copyOf())


            // Now check if we have all of the runes.
            if (!player.inventory.containsAmountNonNull(*items)) {

                // We don't, so we can't cast.
                player.message("You do not have the required items to cast this spell.")
                Autocasting.resetAutoCast(player, true)
                player.actionManager.forceStop()
                return false
            }

            // We've made it through the checks, so we have the items and can
            // remove them now.
            if (delete)
                player.inventory.delete(*items)
        }

        return true
    }

    open val equipment: Optional<Array<Item>> = Optional.empty()

    open fun hasRequiredEquipment(player: Player): Boolean {
        return !(equipment.isPresent && !player.equipment.containsAll(*equipment.get()))
    }

    open fun removeRunes(player: Player): Boolean {
        return true
    }

    open fun startCast(figure: Figure, context: SpellContext) {}//TODO make abstract? - move to interface?

    open fun finishCast(figure: Figure, context: SpellContext) {}//TODO make abstract? - move to interface?

    open fun canCast(figure: Figure, context: SpellContext = BLANK_CONTEXT): Boolean {//TODO make abstract?
        if(figure is Player) {
            // We first check the level required.
            if (!hasRequiredLevel(figure)) {
                figure.actionManager.forceStop()
                return false
            }

            // Then we check controllers.
            if (!figure.controllerManager.canCast(this))
                return false

            // Then we check the items required.
            if (!hasRequiredItems(figure, false))
                return false

            // Finally, we check the equipment required.
            if(!hasRequiredEquipment(figure)) {
                figure.message("You do not have the required equipment to cast this spell.")
                Autocasting.resetAutoCast(figure, true)
                figure.actionManager.forceStop()
                return false
            }

            return true
        }
        return true
    }

    @JvmOverloads
    fun cast(figure: Figure, context: SpellContext = BLANK_CONTEXT): Boolean {
        if (!canCast(figure, context))
            return false

        if (figure is Player && removeRunes(figure))
            deleteRunes(figure)

        startCast(figure, context)
        return true
    }

    fun canCast(player: Player): Boolean {
        return canCast(player as Figure)
    }

    fun deleteRunes(player: Player): Boolean {
        return hasRequiredItems(player, true)
    }

}
