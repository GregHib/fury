package com.fury.game.node.entity.actor.figure.mob

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.node.entity.actor.figure.FigureCombat
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.world.GameWorld
import com.fury.util.Utils

open class MobCombat(private val mob: Mob) : FigureCombat(mob) {
    override val attackSpeed: Int = mob.combatDefinition.attackDelay

    override fun sendDeath(source: Figure?) {
        GameWorld.schedule(MobDeath(mob, mob.combatDefinition.deathDelay))
    }

    override fun handleHit(hit: Hit) {
        if (mob.capDamage != -1 && hit.damage > mob.capDamage && !(hit.weapon != null && hit.weapon.id == 25202))
            hit.damage = mob.capDamage
        if (hit.combatIcon != CombatIcon.MELEE && hit.combatIcon != CombatIcon.RANGED && hit.combatIcon != CombatIcon.MAGIC)
            return
        val source = hit.source ?: return
        if (source.isPlayer()) {
            val p2 = source as Player
            if (p2.prayer.hasPrayersOn()) {
                if (p2.prayer.usingPrayer(1, Prayer.SOUL_SPLIT))
                    Prayer.sendSoulSplit(p2, mob, hit)
                if (hit.damage == 0)
                    return
                if (!p2.prayer.isBoostedLeech) {
                    if (hit.combatIcon == CombatIcon.MELEE) {
                        if (p2.prayer.usingPrayer(1, Prayer.TURMOIL)) {
                            p2.prayer.isBoostedLeech = true
                            return
                        } else if (p2.prayer.usingPrayer(1, Prayer.SAP_WARRIOR)) { // sap  att
                            if (Utils.getRandom(4) == 0) {
                                if (p2.prayer.reachedMax(0)) {
                                    p2.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                } else {
                                    p2.prayer.increaseLeechBonus(0)
                                    p2.message("Your curse drains Attack from the enemy, boosting your Attack.", true)
                                }
                                p2.animate(12569)
                                p2.graphic(2214)
                                p2.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(p2, mob, 2215, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { mob.graphic(2216) }
                                return
                            }
                        } else {
                            if (p2.prayer.usingPrayer(1, Prayer.LEECH_ATTACK)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.prayer.reachedMax(3)) {
                                        p2.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                    } else {
                                        p2.prayer.increaseLeechBonus(3)
                                        p2.message("Your curse drains Attack from the enemy, boosting your Attack.", true)
                                    }
                                    p2.animate(12575)
                                    p2.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(p2, mob, 2231, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { mob.graphic(2232) }
                                    return
                                }
                            }
                            if (p2.prayer.usingPrayer(1, Prayer.LEECH_STRENGTH)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (p2.prayer.reachedMax(7)) {
                                        p2.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                    } else {
                                        p2.prayer.increaseLeechBonus(7)
                                        p2.message("Your curse drains Strength from the enemy, boosting your Strength.", true)
                                    }
                                    p2.animate(12575)
                                    p2.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(p2, mob, 2248, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { mob.graphic(2250) }
                                    return
                                }
                            }

                        }
                    }
                    if (hit.combatIcon == CombatIcon.RANGED) {
                        if (p2.prayer.usingPrayer(1, Prayer.SAP_RANGER)) { // sap range
                            if (Utils.getRandom(4) == 0) {
                                if (p2.prayer.reachedMax(1)) {
                                    p2.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                } else {
                                    p2.prayer.increaseLeechBonus(1)
                                    p2.message("Your curse drains Range from the enemy, boosting your Range.", true)
                                }
                                p2.animate(12569)
                                p2.graphic(2217)
                                p2.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(p2, mob, 2218, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { mob.graphic(2219) }
                                return
                            }
                        } else if (p2.prayer.usingPrayer(1, Prayer.LEECH_RANGED)) {
                            if (Utils.getRandom(7) == 0) {
                                if (p2.prayer.reachedMax(4)) {
                                    p2.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                } else {
                                    p2.prayer.increaseLeechBonus(4)
                                    p2.message("Your curse drains Range from the enemy, boosting your Range.", true)
                                }
                                p2.animate(12575)
                                p2.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(p2, mob, 2236, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { mob.graphic(2238) }
                                return
                            }
                        }
                    }
                    if (hit.combatIcon == CombatIcon.MAGIC) {
                        if (p2.prayer.usingPrayer(1, Prayer.SAP_MAGE)) { // sap mage
                            if (Utils.getRandom(4) == 0) {
                                if (p2.prayer.reachedMax(2)) {
                                    p2.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                } else {
                                    p2.prayer.increaseLeechBonus(2)
                                    p2.message("Your curse drains Magic from the enemy, boosting your Magic.", true)
                                }
                                p2.animate(12569)
                                p2.graphic(2220)
                                p2.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(p2, mob, 2221, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { mob.graphic(2222) }
                                return
                            }
                        } else if (p2.prayer.usingPrayer(1, Prayer.LEECH_MAGIC)) {
                            if (Utils.getRandom(7) == 0) {
                                if (p2.prayer.reachedMax(5)) {
                                    p2.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                } else {
                                    p2.prayer.increaseLeechBonus(5)
                                    p2.message("Your curse drains Magic from the enemy, boosting your Magic.", true)
                                }
                                p2.animate(12575)
                                p2.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(p2, mob, 2240, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { mob.graphic(2242) }
                                return
                            }
                        }
                    }

                    // overall
                    if (p2.prayer.usingPrayer(1, Prayer.LEECH_DEFENCE)) { // leech defence
                        if (Utils.getRandom(10) == 0) {
                            if (p2.prayer.reachedMax(6)) {
                                p2.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                            } else {
                                p2.prayer.increaseLeechBonus(6)
                                p2.message("Your curse drains Defence from the enemy, boosting your Defence.", true)
                            }
                            p2.animate(12575)
                            p2.prayer.isBoostedLeech = true
                            ProjectileManager.send(Projectile(p2, mob, 2244, 35, 35, 20, 5, 0, 0))
                            GameWorld.schedule(1) { mob.graphic(2246) }
                        }
                    }
                }
            }
        }
    }
}