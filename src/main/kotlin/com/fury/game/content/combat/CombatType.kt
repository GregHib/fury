package com.fury.game.content.combat

enum class CombatType(val swing: CombatSwing? = null) {
    MELEE,
    RANGED,
    MAGIC(MagicCombatSwing()),
    DRAGON_FIRE,
    MIXED
}