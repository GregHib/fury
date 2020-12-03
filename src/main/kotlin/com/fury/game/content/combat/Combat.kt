package com.fury.game.content.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.CombatDefinitions
import com.fury.game.entity.character.combat.equipment.weapon.FightStyle
import com.fury.game.entity.character.player.actions.PlayerCombatAction.fullVoidEquipped
import com.fury.game.entity.character.player.content.BonusManager

object Combat {

    fun getHitChance(figure: Figure, target: Figure? = null, skill: Skill): Double {
        val attackRoll = calculateRoll(figure, skill, true)
        val defenceRoll = calculateRoll(target ?: figure, skill, false)

        val accuracy: Double
        accuracy = if (attackRoll > defenceRoll)
            1.0 - (defenceRoll + 2.0) / (2.0 * (attackRoll + 1))
        else
            attackRoll / (2.0 * (defenceRoll + 1.0))

        return accuracy
    }

    private fun calculateRoll(figure: Figure, skill: Skill, attack: Boolean): Double {
        val equipmentBonus = getEquipmentBonus(figure, skill, attack)

        return when (figure) {
            is Player -> {
                var effectiveLevel = calculateEffectLevel(figure, skill)
                if (skill == Skill.MAGIC && !attack) {
                    var effectiveDefence = calculateEffectLevel(figure, Skill.DEFENCE)
                    effectiveDefence *= 0.30

                    var magicDefenceBonus = figure.skills.getLevel(Skill.MAGIC).toDouble()
                    magicDefenceBonus *= figure.prayer.mageMultiplier.toInt()
                    magicDefenceBonus *= 0.70

                    effectiveLevel = Math.floor(effectiveDefence + magicDefenceBonus)
                }

                effectiveLevel * (equipmentBonus + 64)
            }
            is Mob -> (9.0 + equipmentBonus) * 28//Custom formula
            else -> 0.0
        }
    }

    private fun getEquipmentBonus(figure: Figure, skill: Skill, attack: Boolean): Double {
        return if(figure is Player) {
            if (attack)
                figure.bonusManager.attackBonus[if (skill == Skill.MAGIC) BonusManager.ATTACK_MAGIC else if (skill == Skill.RANGED) BonusManager.ATTACK_RANGE else figure.fightType.bonusType]
            else
                figure.bonusManager.defenceBonus[if (skill == Skill.MAGIC) BonusManager.DEFENCE_MAGIC else if (skill == Skill.RANGED) BonusManager.DEFENCE_RANGE else figure.fightType.correspondingBonus]
        } else if(figure is Mob) {
            when {
                figure.bonuses == null -> 0.0
                attack -> figure.bonuses!![if(skill == Skill.MAGIC) CombatDefinitions.MAGIC_ATTACK else if(skill == Skill.RANGED) CombatDefinitions.RANGE_ATTACK else CombatDefinitions.STAB_ATTACK].toDouble()
                else -> figure.bonuses!![if(skill == Skill.MAGIC) CombatDefinitions.MAGIC_DEF else if(skill == Skill.RANGED) CombatDefinitions.RANGE_DEF else CombatDefinitions.STAB_DEF].toDouble()
            }
        } else
            0.0
    }

    private fun calculateEffectLevel(player: Player, skill: Skill): Double {
        var effectiveLevel = player.skills.getLevel(skill).toDouble()

        when (skill) {
            Skill.ATTACK -> effectiveLevel = Math.floor(effectiveLevel * player.prayer.attackMultiplier)
            Skill.DEFENCE -> effectiveLevel = Math.floor(effectiveLevel * player.prayer.defenceMultiplier)
            Skill.STRENGTH -> effectiveLevel = Math.floor(effectiveLevel * player.prayer.strengthMultiplier)
            Skill.RANGED -> effectiveLevel = Math.floor(effectiveLevel * player.prayer.rangeMultiplier)
            Skill.MAGIC -> effectiveLevel = Math.floor(effectiveLevel * player.prayer.mageMultiplier)
            else -> {}
        }

        val style = player.fightType.style
        when (skill) {
            Skill.ATTACK, Skill.STRENGTH -> effectiveLevel += (if (style === FightStyle.ACCURATE) 3 else if (style === FightStyle.CONTROLLED) 1 else 0).toDouble()
            Skill.DEFENCE -> effectiveLevel += (if (style === FightStyle.DEFENSIVE) 3 else if (style === FightStyle.CONTROLLED) 1 else 0).toDouble()
            Skill.RANGED -> effectiveLevel += (if (style === FightStyle.ACCURATE) 3 else 0).toDouble()
            else -> {}
        }

        effectiveLevel += 8.0

        when (skill) {
            Skill.ATTACK, Skill.STRENGTH -> if (fullVoidEquipped(player, 11665, 11676))
                effectiveLevel = Math.floor(effectiveLevel * 1.10)
            Skill.RANGED -> if (fullVoidEquipped(player, 11664, 11675))
                effectiveLevel = Math.floor(effectiveLevel * 1.10)
            Skill.MAGIC -> if (fullVoidEquipped(player, 11663, 11674))
                effectiveLevel = Math.floor(effectiveLevel * 1.45)
            else -> {}
        }

        return Math.floor(effectiveLevel)
    }
}