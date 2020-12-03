package com.fury.game.content.combat.magic

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.member.slayer.Slayer
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.player.content.BonusManager

object Magic {

    @JvmStatic
    fun getMaxHit(figure: Figure, target: Figure?, spell: CombatSpell?): Int {
        return Math.floor(
                ((spell?.getMaxHit(figure, target) ?: 0) * (1.0 + getStaffMultiplier(figure) + getAmuletBonus(figure)))
                        * getLevelMultiplier(figure)
                        * getHexcrestMultiplier(figure, target)
                        * getCastleWarsBraceMultiplier(figure)
                        + getFerociousRingBonus(figure)
                        + getStrykewyrmBonus(figure, target, spell)
                        * getStrykewyrmMultiplier(figure, target, spell)
        ).toInt()
    }

    private fun getLevelMultiplier(figure: Figure): Double {
        return if (figure is Player) (1 + (figure.skills.getLevel(Skill.MAGIC) - figure.skills.getMaxLevel(Skill.MAGIC)) * 0.03) else 1.0
    }

    private fun getAmuletBonus(figure: Figure): Double {
        return if (figure is Player && figure.equipment.exists(Slot.AMULET) && figure.equipment.get(Slot.AMULET).id != -1) figure.equipment.get(Slot.AMULET).getDefinition().bonuses[BonusManager.MAGIC_DAMAGE] / 100.0 else 0.0
    }

    private fun getStaffMultiplier(figure: Figure): Double {
        return if (figure is Player && figure.equipment.exists(Slot.WEAPON) && figure.equipment.get(Slot.WEAPON).id != -1) (figure.equipment.get(Slot.WEAPON).getDefinition().bonuses[BonusManager.MAGIC_DAMAGE] / 100.0) else 0.0
    }

    private fun getFerociousRingBonus(figure: Figure): Double {
        return if (figure is Player && figure.equipment.get(Slot.RING).id in 15398..15402 && figure.x in 1601..1663 && figure.y in 5249..5310) 40.0 else 0.0
    }

    private fun getCastleWarsBraceMultiplier(figure: Figure): Double {
        return if (figure.effects.hasActiveEffect(Effects.CASTLE_WARS_BRACE)) 1.2 else 1.0//and target is flag bearer
    }

    private fun getHexcrestMultiplier(figure: Figure, target: Figure?): Double {
        return if (figure is Player && target is Mob && figure.slayerManager.isCurrentTask(target.name) && (Slayer.hasHexcrest(figure) || Slayer.hasFullSlayerHelmet(figure)))
            7.0 / 6.0
        else
            1.0
    }

    private fun getStrykewyrmBonus(figure: Figure, target: Figure?, spell: CombatSpell?): Int {
        return if (spell != null && isIceStrykewyrm(target) && !spell.name.contains("Ice") && hasFirecape(figure))
            40
        else
            0
    }

    private fun getStrykewyrmMultiplier(figure: Figure, target: Figure?, spell: CombatSpell?): Double {
        return if (spell != null && isIceStrykewyrm(target) && !spell.name.contains("Ice"))
            1.0 + (if (spell.name.contains("Fire")) 0.5 else 0.0) + (if (hasFirecape(figure)) 0.5 else 0.0)
        else
            1.0
    }

    private fun isIceStrykewyrm(target: Figure?): Boolean {
        return target is Mob && target.name == "Ice strykewyrm"
    }

    private fun hasFirecape(figure: Figure): Boolean {
        return figure is Player && (figure.equipment.get(Slot.CAPE).id == 6570 || figure.equipment.get(Slot.CAPE).id == 20769 || figure.equipment.get(Slot.CAPE).id == 20771)
    }
}