package com.fury.core.model.node.entity.actor.figure.combat.magic.spell

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.actions.PlayerCombatAction

/**
 * A [Spell] implementation primarily used for spells that have no effects
 * at all when they hit the player.
 *
 * @author lare96
 */
abstract class CombatNormalSpell : CombatSpell() {

    override fun finishCast(figure: Figure, target: Figure, accurate: Boolean, damage: Int) {
        if (accurate) {
            hitGraphic.ifPresent(target::perform)
        } else {
            splashGraphic.ifPresent(target::perform)
            if(figure is Player)
                PlayerCombatAction.playSound(227, figure, target)
        }
    }
}