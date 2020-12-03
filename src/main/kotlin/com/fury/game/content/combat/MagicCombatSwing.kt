package com.fury.game.content.combat

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatAncientSpell
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.combat.magic.Magic
import com.fury.game.content.global.Achievements
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.magic.Autocasting
import com.fury.game.entity.character.combat.magic.CombatSpells
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.RandomUtils

class MagicCombatSwing : CombatSwing {

    override fun run(figure: Figure, target: Figure): Boolean {
        if(figure is Player) {
            val spell = figure.castSpell ?: figure.autoCastSpell ?: if(figure.equipment.get(Slot.WEAPON).id == 22494) CombatSpells.POLYPORE.spell else return false
            val context = SpellOnFigureContext(target)

            if (!figure.isAutoCast) {
                figure.actionManager.forceStop()
                figure.castSpell = null
            }

            if (!spell.deleteRunes(figure) || !spell.canCast(figure, context)) {
                if (figure.isAutoCast)
                    Autocasting.resetAutoCast(figure, true)
                return false // stops
            }

            //Send animations, graphic & projectiles
            spell.startCast(figure, context)

            val multiTarget =  if (target.inMulti()) spell is CombatAncientSpell && spell.spellRadius != 0 else false
            //Deal damage
            var damage = 0
            for (mark in getTargets(figure, target, multiTarget))
                damage += sendSpell(figure, mark, spell)

            //TODO if in pest control experience /2?
            //Send experience
            if (figure.isDefensiveCasting) {
                figure.skills.addExperience(Skill.MAGIC, spell.experience + damage/7.5)
                figure.skills.addExperience(Skill.DEFENCE, damage/10.0)
                figure.skills.addExperience(Skill.CONSTITUTION, damage/7.5)
            } else {
                figure.skills.addExperience(Skill.MAGIC, spell.experience + damage/5)
                figure.skills.addExperience(Skill.CONSTITUTION, damage/3.0)
            }

            figure.auraManager.checkSuccessfulHits(damage)

            if (figure.isAutoCast && figure.autoCastSpell != null)
                figure.castSpell = figure.autoCastSpell
            figure.previousCastSpell = spell

        }
        return true
    }

    private fun sendSpell(figure: Figure, target: Figure, spell: CombatSpell): Int {
        //Calculate hits
        val chance = Combat.getHitChance(figure, target, skill = Skill.MAGIC)
        val accurate = RandomUtils.success(chance)
        val maxHit = Magic.getMaxHit(figure, target, spell)
        val minHit = spell.getMinHit(figure, target)
        var damage = if(maxHit > minHit) if(maxHit == 0) 0 else RandomUtils.inclusive(0, maxHit) else RandomUtils.inclusive(minHit, maxHit)

        //Modify based on targets defences
        damage = target.combat.modifyDamage(figure, CombatType.MAGIC, damage)

        //Create hit
        var hit: Hit? = if(maxHit == 0) null else Hit(figure, if(accurate) damage else 0, if(damage >= maxHit * 0.90) HitMask.CRITICAL else HitMask.RED, if(accurate) CombatIcon.MAGIC else CombatIcon.BLOCK)

        addAttackedByDelay(figure, target)

        //Delay
        GameWorld.schedule(ProjectileManager.getProjectileDelay(figure, target)) {
            if (figure.isDead || figure.finished || target.isDead || target.finished || (target is Mob && target.isCantInteract))
                return@schedule

            if(target.combat.negateDamage(figure, CombatType.MAGIC, damage)) {
                hit = null
                damage = 0
            }

            if(hit != null)
                target.combat.applyHit(hit!!)
//                target.combat.hits.addHit(hit!!)

            if(figure is Player && damage > 0)
                Achievements.doProgress(figure, Achievements.AchievementData.DEAL_15M_DAMAGE, damage)

            //Emotes/graphics and spell effects
            spell.finishCast(figure, SpellOnFigureContext(target, accurate, damage))

            doDefenceEmote(target)

            if(damage > 0)
                target.combat.afterEffects(figure, CombatType.MAGIC, damage)

            autoRetaliate(figure, target)
        }

        return if(accurate) damage else 0
    }
}