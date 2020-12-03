package com.fury.game.node.entity.actor.figure.player

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.combat.CombatType
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.member.slayer.Slayer
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial
import com.fury.game.entity.character.combat.equipment.weapon.FightType
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.entity.character.player.content.BonusManager
import com.fury.game.node.entity.actor.figure.FigureCombat
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.RandomUtils
import com.fury.util.Utils

class PlayerCombat(private val player: Player) : FigureCombat(player) {
    var target: Figure? = null
        private set

    @JvmOverloads
    fun target(target: Figure?, delay: Boolean = false) {
        if(target != null && !delay && !player.actionManager.hasAction())
            player.actionManager.action = PlayerCombatAction(target)
        else
            this.target = target
    }

    override/*if (getCurrentlyCasting() != null) {
            if (getCurrentlyCasting() == CombatSpells.BLOOD_BLITZ.getSpell() || getCurrentlyCasting() == CombatSpells.SHADOW_BLITZ.getSpell() || getCurrentlyCasting() == CombatSpells.SMOKE_BLITZ.getSpell() || getCurrentlyCasting() == CombatSpells.ICE_BLITZ.getSpell()) {
                return 5;
            } else {
                return 6;
            }
        }*/ val attackSpeed: Int
        get() {
            var speed = player.weapon!!.speed
            val item = player.equipment.get(Slot.WEAPON) ?: return -1
            val weapon = item.getDefinition().name
            val weaponId = item.id
            if (weaponId == 1419) {
                speed -= 2
            }
            if (weaponId == 20857) {
                speed = 4
            }
            if (weaponId == 24187) {
                speed -= 3
            }
            if (player.fightType == FightType.CROSSBOW_RAPID || player.fightType == FightType.LONGBOW_RAPID || weaponId == 6522 && player.fightType == FightType.KNIFE_RAPID || weapon.contains("rapier")) {
                if (weaponId != 11235) {
                    speed--
                }
            } else if (weaponId != 6522 && weaponId != 15241 && (player.fightType == FightType.SHORTBOW_RAPID || player.fightType == FightType.DART_RAPID || player.fightType == FightType.KNIFE_RAPID || player.fightType == FightType.THROWNAXE_RAPID || player.fightType == FightType.JAVELIN_RAPID) || weaponId == 11730) {
                speed -= 2
            }
            return speed
        }

    override fun applyHit(hit: Hit) {
        val source = hit.source
        if (source != null && source != player && source is Player && !player.isCanPvp)
            return
        super.applyHit(hit)
    }

    override fun negateDamage(source: Figure, type: CombatType, damage: Int): Boolean {
        //Protection prayers
        if(source !is Player) {
            if(
                    player.prayer.isMeleeProtecting && type == CombatType.MELEE ||
                    player.prayer.isRangeProtecting && type == CombatType.RANGED ||
                    player.prayer.isMageProtecting && type == CombatType.MAGIC ||
                    source is Familiar && player.prayer.isSummoningProtecting
            )
                return true
        }
        return super.negateDamage(source, type, damage)
    }

    override fun modifyDamage(source: Figure, type: CombatType, damage: Int): Int {
        var hit = damage.toDouble()
        if (player.isInvulnerable)
            return 0

        //Divine
        if (player.equipment.get(Slot.SHIELD).id == 13740)
            if (player.skills.getLevel(Skill.PRAYER) > Math.ceil((hit * 0.3) / 2.0))
                hit *= 0.7

        //Elysian
        if (player.equipment.get(Slot.SHIELD).id == 13742 && RandomUtils.success(0.7))
            hit *= 0.75

        //Staff of light
        if (type == CombatType.MELEE && player.effects.hasActiveEffect(Effects.STAFF_OF_LIGHT) && !(source is Mob && source.id == 2030))//Verac
            hit *= 0.5

        //Staff of light
        if (player.effects.hasActiveEffect(Effects.IGNEOUS_FRUIT))
            hit *= 0.98

        //Absorption
        if (damage >= 200) {
            hit -= when (type) {
                CombatType.MELEE -> hit * (player.bonusManager.defenceBonus[BonusManager.ABSORB_MELEE] / 100.0)
                CombatType.RANGED -> hit * (player.bonusManager.defenceBonus[BonusManager.ABSORB_RANGED] / 100.0)
                CombatType.MAGIC -> hit * (player.bonusManager.defenceBonus[BonusManager.ABSORB_MAGIC] / 100.0)
                else -> 0.0
            }
        }

        //Protection prayers against players
        if (source is Player) {
            if (player.prayer.isMeleeProtecting && type == CombatType.MELEE)
                hit *= source.magePrayerMultiplier//TODO change
            else if (player.prayer.isRangeProtecting && type == CombatType.RANGED)
                hit *= source.rangePrayerMultiplier//TODO change
            else if (player.prayer.isMageProtecting && type == CombatType.MAGIC)
                hit *= source.magePrayerMultiplier//TODO change
            /*if(
                    player.prayer.isMeleeProtecting && type == CombatType.MELEE ||
                    player.prayer.isRangeProtecting && type == CombatType.RANGED ||
                    player.prayer.isMageProtecting && type == CombatType.MAGIC
            )
                hit *= 0.6*/
        }
        if (source is Player && PlayerCombatAction.hasAhrimsSet(source) && RandomUtils.success(0.25)) {
            if(player.skills.drain(Skill.ATTACK, 5, true) > 0)
                player.graphic(400)
        }

        if (source.temporaryAttributes.getOrDefault("instakill", false) as Boolean) {
            hit = player.health.hitpoints.toDouble()
        }

        return hit.toInt()
    }

    override fun afterEffects(source: Figure, type: CombatType, damage: Int) {

        //Divine
        if (player.equipment.get(Slot.SHIELD).id == 13740)
            player.skills.drain(Skill.PRAYER, Math.ceil((damage * 0.3) / 2.0))

        //Deflection
        if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MELEE) && type == CombatType.MELEE && !source.blockDeflections) {
            source.combat.applyHit(Hit(player, (damage * 0.1).toInt(), HitMask.RED, CombatIcon.DEFLECT))
            player.graphic(2230)
            player.animate(12573)
        } else if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MISSILES) && type == CombatType.RANGED && !source.blockDeflections) {
            source.combat.applyHit(Hit(player, (damage * 0.1).toInt(), HitMask.RED, CombatIcon.DEFLECT))
            player.graphic(2229)
            player.animate(12573)
        } else if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MAGIC) && type == CombatType.MAGIC && !source.blockDeflections) {
            source.combat.applyHit(Hit(player, (damage * 0.1).toInt(), HitMask.RED, CombatIcon.DEFLECT))
            player.graphic(2228)
            player.animate(12573)
        }

        //Vengeance
        if (player.hasVengeance && damage >= 4) {
            player.hasVengeance = false
            player.forceChat("Taste vengeance!")
            source.combat.applyHit(Hit(player, (damage * 0.75).toInt(), HitMask.DARK_RED, CombatIcon.NONE))
        }

        //Smite
        if (source is Player && source.prayer.usingPrayer(0, Prayer.SMITE))
            player.skills.drain(Skill.PRAYER, damage / 4)

        //Soul split
//        if (source is Player && source.prayer.usingPrayer(1, Prayer.SOUL_SPLIT))
//            Prayer.sendSoulSplit(source, player, damage)

        super.afterEffects(source, type, damage)
    }

    override fun handleHit(hit: Hit) {
        if (player.isInvulnerable && hit.hitMask != HitMask.PURPLE) {
            hit.damage = 0
            return
        }

        if (hit.combatIcon != CombatIcon.MELEE && hit.combatIcon != CombatIcon.RANGED && hit.combatIcon != CombatIcon.MAGIC)
            return

        if (player.auraManager.usingPenance()) {
            val amount = (hit.damage * 0.2).toInt()
            if (amount > 0)
                player.skills.restore(Skill.PRAYER, amount)
        }

        val source = hit.source ?: return

        if (source is Mob) {
            if (!player.skills.hasLevel(Skill.SLAYER, Slayer.getLevelRequirement(source.name))) {
                player.message("This monster requires " + Slayer.getLevelRequirement(source.name) + " to slay.")
                return
            }
        }

        val shield = player.equipment.get(Slot.SHIELD)
        if (shield.id == 13742) { // elysian
            if (Utils.getRandom(100) <= 70)
                hit.damage = (hit.damage * 0.75).toInt()
        } else if (shield.id == 13740) { // divine
            val drain = (Math.ceil(hit.damage * 0.3) / 2).toInt()
            if (player.skills.getLevel(Skill.PRAYER) >= drain) {
                hit.damage = (hit.damage * 0.70).toInt()
                player.skills.drain(Skill.PRAYER, drain)
            }
        }
        if (player.effects.hasActiveEffect(Effects.STAFF_OF_LIGHT)) {
            hit.damage = (hit.damage * 0.5).toInt()
        }

        if (player.effects.hasActiveEffect(Effects.IGNEOUS_FRUIT)) {
            println("Hit reduced from " + hit.damage)
            hit.damage = (hit.damage * 0.98).toInt()
            println("to " + hit.damage)
        }

        if (player.prayer.hasPrayersOn() && hit.damage != 0) {
            if (hit.combatIcon == CombatIcon.MAGIC) {
                if (player.prayer.usingPrayer(0, Prayer.PROTECT_FROM_MAGIC))
                    hit.damage = (hit.damage.toDouble() * source.magePrayerMultiplier).toInt()
                else if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MAGIC)) {
                    val deflectedDamage = if (source.blockDeflections) 0 else (hit.damage * 0.1).toInt()
                    hit.damage = (hit.damage.toDouble() * source.magePrayerMultiplier).toInt()
                    if (deflectedDamage > 0) {
                        source.combat.applyHit(Hit(player, deflectedDamage, HitMask.RED, CombatIcon.DEFLECT))
                        player.graphic(2228)
                        player.animate(12573)
                    }
                }
            } else if (hit.combatIcon == CombatIcon.RANGED) {
                if (player.prayer.usingPrayer(0, Prayer.PROTECT_FROM_MISSILES))
                    hit.damage = (hit.damage.toDouble() * source.rangePrayerMultiplier).toInt()
                else if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MISSILES)) {
                    val deflectedDamage = if (source.blockDeflections) 0 else (hit.damage * 0.1).toInt()
                    hit.damage = (hit.damage.toDouble() * source.rangePrayerMultiplier).toInt()
                    if (deflectedDamage > 0) {
                        source.combat.applyHit(Hit(player, deflectedDamage, HitMask.RED, CombatIcon.DEFLECT))
                        player.graphic(2229)
                        player.animate(12573)
                    }
                }
            } else if (hit.combatIcon == CombatIcon.MELEE) {
                if (player.prayer.usingPrayer(0, Prayer.PROTECT_FROM_MELEE))
                    hit.damage = (hit.damage.toDouble() * source.meleePrayerMultiplier).toInt()
                else if (player.prayer.usingPrayer(1, Prayer.DEFLECT_MELEE)) {
                    val deflectedDamage = if (source.blockDeflections) 0 else (hit.damage * 0.1).toInt()
                    hit.damage = (hit.damage.toDouble() * source.meleePrayerMultiplier).toInt()
                    if (deflectedDamage > 0) {
                        source.combat.applyHit(Hit(player, deflectedDamage, HitMask.RED, CombatIcon.DEFLECT))
                        player.graphic(2230)
                        player.animate(12573)
                    }
                }
            }
        }
        if (hit.damage >= 200) {
            if (hit.combatIcon == CombatIcon.MELEE) {
                val reducedDamage = (hit.damage * player.bonusManager.defenceBonus[BonusManager.ABSORB_MELEE] / 100).toInt()
                if (reducedDamage > 0) {
                    hit.damage = hit.damage - reducedDamage
                    //					hit.setSoaking(new Hit(source, reducedDamage, HitMask.BLUE, CombatIcon.BLUE_SHIELD));
                }
            } else if (hit.combatIcon == CombatIcon.RANGED) {
                val reducedDamage = (hit.damage * player.bonusManager.defenceBonus[BonusManager.ABSORB_RANGED] / 100).toInt()
                if (reducedDamage > 0) {
                    hit.damage = hit.damage - reducedDamage
                    //					hit.setSoaking(new Hit(source, reducedDamage, HitMask.BLUE, CombatIcon.BLUE_SHIELD));
                }
            } else if (hit.combatIcon == CombatIcon.MAGIC) {
                val reducedDamage = (hit.damage * player.bonusManager.defenceBonus[BonusManager.ABSORB_MAGIC] / 100).toInt()
                if (reducedDamage > 0) {
                    hit.damage = hit.damage - reducedDamage
                    //					hit.setSoaking(new Hit(source, reducedDamage, HitMask.BLUE, CombatIcon.BLUE_SHIELD));
                }
            }
        }
        if (player.hasVengeance && hit.damage >= 4) {
            player.hasVengeance = false
            player.forceChat("Taste vengeance!")
            source.combat.applyHit(Hit(player, (hit.damage * 0.75).toInt(), HitMask.DARK_RED, CombatIcon.NONE))
        }
        if (source is Player) {
            if (source.prayer.hasPrayersOn()) {
                if (source.prayer.usingPrayer(0, Prayer.SMITE)) { // smite
                    val drain = hit.damage / 4
                    if (drain > 0)
                        player.skills.drain(Skill.PRAYER, drain)
                } else {
                    if (source.prayer.usingPrayer(1, Prayer.SOUL_SPLIT))
                        Prayer.sendSoulSplit(source, player, hit)
                    if (hit.damage == 0)
                        return
                    if (!source.prayer.isBoostedLeech) {
                        if (hit.combatIcon == CombatIcon.MELEE) {
                            if (source.prayer.usingPrayer(1, Prayer.TURMOIL)) {
                                if (Utils.getRandom(4) == 0) {
                                    source.prayer.increaseTurmoilBonus(player)
                                    source.prayer.isBoostedLeech = true
                                    return
                                }
                            } else if (source.prayer.usingPrayer(1, Prayer.SAP_WARRIOR)) { // sap att
                                if (Utils.getRandom(4) == 0) {
                                    if (source.prayer.reachedMax(0)) {
                                        source.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                    } else {
                                        source.prayer.increaseLeechBonus(0)
                                        source.message("Your curse drains Attack from the enemy, boosting your Attack.", true)
                                    }
                                    source.animate(12569)
                                    source.graphic(2214)
                                    source.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(source, player, 2215, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { player.graphic(2216) }
                                    return
                                }
                            } else {
                                if (source.prayer.usingPrayer(1, Prayer.LEECH_ATTACK)) {
                                    if (Utils.getRandom(7) == 0) {
                                        if (source.prayer.reachedMax(3)) {
                                            source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                        } else {
                                            source.prayer.increaseLeechBonus(3)
                                            source.message("Your curse drains Attack from the enemy, boosting your Attack.", true)
                                        }
                                        source.animate(12575)
                                        source.prayer.isBoostedLeech = true
                                        ProjectileManager.send(Projectile(source, player, 2231, 35, 35, 20, 5, 0, 0))
                                        GameWorld.schedule(1) { player.graphic(2232) }
                                        return
                                    }
                                }
                                if (source.prayer.usingPrayer(1, Prayer.LEECH_STRENGTH)) {
                                    if (Utils.getRandom(7) == 0) {
                                        if (source.prayer.reachedMax(7)) {
                                            source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                        } else {
                                            source.prayer.increaseLeechBonus(7)
                                            source.message("Your curse drains Strength from the enemy, boosting your Strength.", true)
                                        }
                                        source.animate(12575)
                                        source.prayer.isBoostedLeech = true
                                        ProjectileManager.send(Projectile(source, player, 2248, 35, 35, 20, 5, 0, 0))
                                        GameWorld.schedule(1) { player.graphic(2250) }
                                        return
                                    }
                                }

                            }
                        }
                        if (hit.combatIcon == CombatIcon.RANGED) {
                            if (source.prayer.usingPrayer(1, Prayer.SAP_RANGER)) { // sap range
                                if (Utils.getRandom(4) == 0) {
                                    if (source.prayer.reachedMax(1)) {
                                        source.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                    } else {
                                        source.prayer.increaseLeechBonus(1)
                                        source.message("Your curse drains Range from the enemy, boosting your Range.", true)
                                    }
                                    source.animate(12569)
                                    source.graphic(2217)
                                    source.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(source, player, 2218, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { player.graphic(2219) }
                                    return
                                }
                            } else if (source.prayer.usingPrayer(1, Prayer.LEECH_RANGED)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (source.prayer.reachedMax(4)) {
                                        source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                    } else {
                                        source.prayer.increaseLeechBonus(4)
                                        source.message("Your curse drains Range from the enemy, boosting your Range.", true)
                                    }
                                    source.animate(12575)
                                    source.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(source, player, 2236, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { player.graphic(2238) }
                                    return
                                }
                            }
                        }
                        if (hit.combatIcon == CombatIcon.MAGIC) {
                            if (source.prayer.usingPrayer(1, Prayer.SAP_MAGE)) { // sap mage
                                if (Utils.getRandom(4) == 0) {
                                    if (source.prayer.reachedMax(2)) {
                                        source.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                    } else {
                                        source.prayer.increaseLeechBonus(2)
                                        source.message("Your curse drains Magic from the enemy, boosting your Magic.", true)
                                    }
                                    source.animate(12569)
                                    source.graphic(2220)
                                    source.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(source, player, 2221, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { player.graphic(2222) }
                                    return
                                }
                            } else if (source.prayer.usingPrayer(1, Prayer.LEECH_MAGIC)) {
                                if (Utils.getRandom(7) == 0) {
                                    if (source.prayer.reachedMax(5)) {
                                        source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                    } else {
                                        source.prayer.increaseLeechBonus(5)
                                        source.message("Your curse drains Magic from the enemy, boosting your Magic.", true)
                                    }
                                    source.animate(12575)
                                    source.prayer.isBoostedLeech = true
                                    ProjectileManager.send(Projectile(source, player, 2240, 35, 35, 20, 5, 0, 0))
                                    GameWorld.schedule(1) { player.graphic(2242) }
                                    return
                                }
                            }
                        }

                        // overall

                        if (source.prayer.usingPrayer(1, Prayer.LEECH_DEFENCE)) { // leech defence
                            if (Utils.getRandom(10) == 0) {
                                if (source.prayer.reachedMax(6)) {
                                    source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                } else {
                                    source.prayer.increaseLeechBonus(6)
                                    source.message("Your curse drains Defence from the enemy, boosting your Defence.", true)
                                }
                                source.animate(12575)
                                source.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(source, player, 2244, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { player.graphic(2246) }
                                return
                            }
                        }

                        if (source.prayer.usingPrayer(1, Prayer.LEECH_ENERGY)) {
                            if (Utils.getRandom(10) == 0) {
                                if (player.settings.getInt(Settings.RUN_ENERGY) <= 0) {
                                    source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                } else {
                                    source.settings.set(Settings.RUN_ENERGY, if (source.settings.getInt(Settings.RUN_ENERGY) > 90) 100 else source.settings.getInt(Settings.RUN_ENERGY) + 10)
                                    player.settings.set(Settings.RUN_ENERGY, if (source.settings.getInt(Settings.RUN_ENERGY) > 10) player.settings.getInt(Settings.RUN_ENERGY) - 10 else 0)
                                }
                                source.animate(12575)
                                source.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(source, player, 2256, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { player.graphic(2258) }
                                return
                            }
                        }

                        if (source.prayer.usingPrayer(1, Prayer.LEECH_SPECIAL_ATTACK)) {
                            if (Utils.getRandom(10) == 0) {
                                if (player.settings.getInt(Settings.SPECIAL_ENERGY) <= 0) {
                                    source.message("Your opponent has been weakened so much that your leech curse has no effect.", true)
                                } else {
                                    CombatSpecial.restore(source, 10, false)
                                    CombatSpecial.drain(player, 10)
                                }
                                source.animate(12575)
                                source.prayer.isBoostedLeech = true
                                ProjectileManager.send(Projectile(source, player, 2252, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { player.graphic(2254) }
                                return
                            }
                        }

                        if (source.prayer.usingPrayer(1, Prayer.SAP_SPIRIT)) { // sap spec
                            if (Utils.getRandom(10) == 0) {
                                source.animate(12569)
                                source.graphic(2223)
                                source.prayer.isBoostedLeech = true
                                if (player.settings.getInt(Settings.SPECIAL_ENERGY) <= 0) {
                                    source.message("Your opponent has been weakened so much that your sap curse has no effect.", true)
                                } else {
                                    CombatSpecial.drain(player, 10)
                                }
                                ProjectileManager.send(Projectile(source, player, 2224, 35, 35, 20, 5, 0, 0))
                                GameWorld.schedule(1) { player.graphic(2225) }
                                return
                            }
                        }
                    }
                }
            }
        } else {
            val n = source as Mob
            if (n.id == 13448)
                Prayer.sendSoulSplit(n, player, hit)
        }

        player.controllerManager.processOutgoingHit(hit)
    }

    override fun sendDeath(source: Figure?) {
        if (player.prayer.hasPrayersOn() && player.temporaryAttributes["startedDuel"] is Boolean && player.temporaryAttributes["startedDuel"] != true) {
            if (player.prayer.usingPrayer(0, Prayer.RETRIBUTION)) {
                player.graphic(437)
                val target = player
                if (player.inMulti()) {
                    for (player in GameWorld.regions.getLocalPlayers(player)) {
                        if (!player.started || player.isDead || player.finished || !player.isWithinDistance(player, 1) || !target.controllerManager.canHit(player))
                            continue
                        player.combat.applyHit(Hit(target, Utils.getRandom((player.skills.getLevel(Skill.PRAYER) * 0.25).toInt()), HitMask.RED, CombatIcon.NONE))
                    }

                    for (npc in GameWorld.regions.getLocalNpcs(player)) {
                        if (npc.isDead || npc.finished || !npc.isWithinDistance(player, 1) || !npc.definition.hasAttackOption() || !target.controllerManager.canHit(npc))
                            continue
                        npc.combat.applyHit(Hit(target, Utils.getRandom((player.skills.getLevel(Skill.PRAYER) * 0.25).toInt()), HitMask.RED, CombatIcon.NONE))
                    }

                } else {
                    if (source != null && source != player && !source.isDead && !source.finished && source.isWithinDistance(player, 1))
                        source.combat.applyHit(Hit(target, Utils.getRandom((player.skills.getLevel(Skill.PRAYER) * 0.25).toInt()), HitMask.RED, CombatIcon.NONE))
                }

                GameWorld.schedule(1) {
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x - 1, target.y, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x + 1, target.y, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x, target.y - 1, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x, target.y + 1, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x - 1, target.y - 1, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x - 1, target.y + 1, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x + 1, target.y - 1, target.z))
                    Graphic.sendGlobal(target, Graphic(438), Position(target.x + 1, target.y + 1, target.z))
                }
            } else if (player.prayer.usingPrayer(1, Prayer.WRATH)) {
                ProjectileManager.send(Projectile(player, Position(player.x + 2, player.y + 2, player.z), 2260, 24, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(player, Position(player.x + 2, player.y, player.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(player, Position(player.x + 2, player.y - 2, player.z), 2260, 41, 0, 41, 35, 30, 0))

                ProjectileManager.send(Projectile(player, Position(player.x - 2, player.y + 2, player.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(player, Position(player.x - 2, player.y, player.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(player, Position(player.x - 2, player.y - 2, player.z), 2260, 41, 0, 41, 35, 30, 0))

                ProjectileManager.send(Projectile(player, Position(player.x, player.y + 2, player.z), 2260, 41, 0, 41, 35, 30, 0))
                ProjectileManager.send(Projectile(player, Position(player.x, player.y - 2, player.z), 2260, 41, 0, 41, 35, 30, 0))
                val target = player
                GameWorld.schedule(1) {
                    player.graphic(2259)

                    if (player.inMulti()) {
                        for (player in GameWorld.regions.getLocalPlayers(player)) {
                            if (!player.started || player.isDead || player.finished || !player.isWithinDistance(target, 2) || !target.controllerManager.canHit(player))
                                continue
                            player.combat.applyHit(Hit(target, Utils.getRandom(player.skills.getLevel(Skill.PRAYER) / 10 * 3), HitMask.RED, CombatIcon.NONE))
                        }
                        for (npc in GameWorld.regions.getLocalNpcs(player)) {
                            if (npc.isDead || npc.finished || !npc.isWithinDistance(target, 2) || !npc.definition.hasAttackOption() || !target.controllerManager.canHit(npc))
                                continue
                            npc.combat.applyHit(Hit(target, Utils.getRandom(player.skills.getLevel(Skill.PRAYER) / 10 * 3), HitMask.RED, CombatIcon.NONE))
                        }
                    } else {
                        if (source != null && source != target && !source.isDead && !source.finished && source.isWithinDistance(target, 2))
                            source.combat.applyHit(Hit(target, Utils.getRandom(player.skills.getLevel(Skill.PRAYER) / 10 * 3), HitMask.RED, CombatIcon.NONE))
                    }

                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x + 2, player.y + 2, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x + 2, player.y, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x + 2, player.y - 2, player.z))

                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x - 2, player.y + 2, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x - 2, player.y, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x - 2, player.y - 2, player.z))

                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x, player.y + 2, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x, player.y - 2, player.z))

                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x + 1, player.y + 1, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x + 1, player.y - 1, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x - 1, player.y + 1, player.z))
                    Graphic.sendGlobal(target, Graphic(2260), Position(player.x - 1, player.y - 1, player.z))
                }
            }
        }
        player.animate(-1)
        if (!player.controllerManager.sendDeath())
            return
        player.addStopDelay(7)
        player.stopAll()
        GameWorld.schedule(PlayerDeath(player))
    }
}