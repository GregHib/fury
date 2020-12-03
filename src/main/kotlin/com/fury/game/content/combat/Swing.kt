package com.fury.game.content.combat

import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFigureContext
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.global.Achievements
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.CombatConstants
import com.fury.game.entity.character.combat.CombatDefinitions
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.combat.equipment.weapon.FightStyle
import com.fury.game.entity.character.combat.equipment.weapon.FightType
import com.fury.game.entity.character.player.actions.PlayerCombatAction.*
import com.fury.game.node.entity.actor.figure.player.PlayerCombat
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Misc
import com.fury.util.Utils

object Swing {

    private fun addAttackedByDelay(player: Figure, target: Figure) {
        target.combat.attackedBy = player
        target.combat.attackedByDelay = GameSettings.COMBAT_DELAY//8 seconds
        player.combat.attackingDelay = GameSettings.COMBAT_DELAY
    }

    private fun doDefenceEmote(target: Figure) {
        target.performAnimationNoPriority(Animation(CombatConstants.getDefenceEmote(target)))
    }

    @JvmStatic
    fun delayHit(target: Figure, delay: Int, weapon: Item, type: FightType, vararg hits: Hit) {
        addAttackedByDelay(hits[0].source, target)
        for (hit in hits) {
            val player = hit.source as Player
            var damage = if (hit.damage > target.health.hitpoints) target.health.hitpoints else hit.damage

            if (player.temporaryAttributes.getOrDefault("instakill", false) as Boolean) {
                hit.weapon = Item(25202, Revision.PRE_RS3)
                if (hit.combatIcon == CombatIcon.MAGIC || hit.combatIcon == CombatIcon.NONE)
                    hit.combatIcon = CombatIcon.RANGED
                damage = target.health.hitpoints
                hit.damage = damage
            }

            if (hit.combatIcon == CombatIcon.RANGED || hit.combatIcon == CombatIcon.MELEE) {
                val combatXp = damage / 10.0 * 4.2342
                val instaKill = hit.weapon != null && hit.weapon.id == 25202
                if (combatXp > 0 && !instaKill) {
                    player.auraManager.checkSuccessfulHits(hit.damage)
                    if (hit.combatIcon == CombatIcon.RANGED) {
                        if (type.style === FightStyle.DEFENSIVE) {
                            player.skills.addExperience(Skill.RANGED, combatXp / 2.0)
                            player.skills.addExperience(Skill.DEFENCE, combatXp / 2.0)
                        } else
                            player.skills.addExperience(Skill.RANGED, combatXp)
                    } else {
                        val xpStyle = CombatDefinitions.getXpStyle(weapon, player.combatDefinitions.attackStyle)
                        if (xpStyle != null) {
                            player.skills.addExperience(xpStyle, combatXp)
                        } else {
                            player.skills.addExperience(Skill.ATTACK, combatXp / 3.0)
                            player.skills.addExperience(Skill.STRENGTH, combatXp / 3.0)
                            player.skills.addExperience(Skill.DEFENCE, combatXp / 3.0)
                        }
                    }
                    val hpXp = damage / 10.0 * 1.2342
                    if (hpXp > 0)
                        player.skills.addExperience(Skill.CONSTITUTION, hpXp)
                }
            } else if (hit.combatIcon == CombatIcon.MAGIC) {
                var combatXp = player.temporaryAttributes["base_mage_xp"] as Double * 1.0 + damage / 5.0
                if (combatXp > 0) {
                    player.auraManager.checkSuccessfulHits(hit.damage)
                    if (player.isDefensiveCasting) {
                        val defenceXp = (damage / 7.5).toInt()
                        if (defenceXp > 0) {
                            combatXp -= defenceXp.toDouble()
                            player.skills.addExperience(Skill.DEFENCE, defenceXp / 7.5)
                        }
                    }
                    player.skills.addExperience(Skill.MAGIC, combatXp)
                    val hpXp = damage / 7.5
                    if (hpXp > 0)
                        player.skills.addExperience(Skill.CONSTITUTION, hpXp)
                }
            }
        }
        GameWorld.schedule(delay) {
            for (hit in hits) {
                val player = hit.source as Player
                if (player.isDead || player.finished || target.isDead || target.finished || (target is Mob && target.isCantInteract))
                    return@schedule

                target.combat.applyHit(hit) // also reduces damage if needed, pray and special items affect here

                doDefenceEmote(target)
                val damage = if (hit.damage > target.health.hitpoints) target.health.hitpoints else hit.damage
                Achievements.doProgress(player, Achievements.AchievementData.DEAL_15M_DAMAGE, damage)
                if (player.temporaryAttributes.containsKey("max_hit") && damage >= player.temporaryAttributes["max_hit"] as Int * 0.90 && (hit.combatIcon == CombatIcon.MAGIC || hit.combatIcon == CombatIcon.RANGED || hit.combatIcon == CombatIcon.MELEE))
                    hit.hitMask = HitMask.CRITICAL
                if (hit.combatIcon == CombatIcon.RANGED || hit.combatIcon == CombatIcon.MELEE) {
                    val combatXp = damage / 2.5
                    if (combatXp > 0) {
                        if (hit.combatIcon == CombatIcon.RANGED) {
                            if (weapon.id != -1) {
                                val name = weapon.name
                                if (name.contains("(p++)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(48)
                                } else if (name.contains("(p+)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(38)
                                } else if (name.contains("(p)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(28)
                                }
                            }
                        } else {
                            if (weapon.id != -1) {
                                val name = weapon.name
                                if (name.contains("(p++)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(68)
                                } else if (name.contains("(p+)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(58)
                                } else if (name.contains("(p)")) {
                                    if (Utils.getRandom(8) == 0)
                                        target.effects.makePoisoned(48)
                                }
                                if (target.effects.hasActiveEffect(Effects.STAFF_OF_LIGHT))
                                    target.perform(Graphic(2320))
                            }
                        }
                    }

                    if (damage > 0 && weapon.id == 21365 && Misc.random(2) == 0) {
                        target.perform(Graphic(469))
                        target.combat.addFreezeDelay(10000)
                    }

                    if (hasGuthansSet(player) && Misc.random(4) == 0) {
                        player.health.heal(hit.damage)
                        target.perform(Graphic(398))
                    }

                    if (hasToragsSet(player) && Misc.random(4) == 0) {
                        if (target.isPlayer()) {
                            val energy = (target as Player).settings.getInt(Settings.RUN_ENERGY)
                            target.settings.set(Settings.RUN_ENERGY, (energy * 0.8).toInt())
                        }
                        target.perform(Graphic(399))
                    }
                    if (hasKarilsSet(player) && Misc.random(4) == 0) {
                        if (target.isPlayer())
                            (target as Player).skills.drain(Skill.AGILITY, 0.2)
                        target.perform(Graphic(401))
                    }
                    if (player.temporaryAttributes["range_hit_gfx"] != null)
                        target.perform(Graphic(player.temporaryAttributes["range_hit_gfx"] as Int, 0, 100, Revision.RS2))
                } else if (hit.combatIcon == CombatIcon.MAGIC) {
                    when {
                        player.temporaryAttributes["mage_spell"] != null -> (player.temporaryAttributes["mage_spell"] as CombatSpell).finishCast(player, SpellOnFigureContext(target, damage = damage))
                        hit.damage == 0 -> {
                            target.perform(Graphic(85, 0, 100))
                            playSound(227, player, target)
                        }
                        player.temporaryAttributes["mage_hit_gfx"] != null -> target.perform(Graphic(player.temporaryAttributes["mage_hit_gfx"] as Int, 0, 0, player.temporaryAttributes["mage_hit_gfx_revision"] as Revision))
                    // target.perform(new Graphic(mage_hit_gfx, 0, mage_hit_gfx == 369 || mage_hit_gfx == 1843 ? 0 : 100, mage_hit_gfx_revision));
                    }

                    if (hasAhrimsSet(player) && Misc.random(4) == 0) {
                        if (target.isPlayer())
                            (target as Player).skills.drain(Skill.ATTACK, 5)
                        target.perform(Graphic(400))
                    }
                }
                autoRetaliate(player, target)
            }
        }
    }


    @JvmStatic
    fun autoRetaliate(figure: Figure, target: Figure) {
        if (target is Player) {
            target.packetSender.sendInterfaceRemoval()
            if (target.isAutoRetaliate && !target.actionManager.hasAction() && !target.movement.hasWalkSteps())
                if (target.combat is PlayerCombat)
                    (target.combat as PlayerCombat).target(figure, true)
        } else if(target is Mob) {
            if (!target.combat.isInCombat() || target.canBeAttackedByAutoRetaliate())
                target.setTarget(figure)
        }
    }
}