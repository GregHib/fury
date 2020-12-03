package com.fury.core.model.node.entity.actor.figure.combat.magic.spell

import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell

/**
 * A [CombatSpell] implementation that is primarily used for spells that
 * are a part of the ancients spellbook.
 *
 * @author lare96
 */
abstract class CombatAncientSpell : CombatEffectSpell() {

    open val spellRadius = 0

    override val attackDelay = 4
}
