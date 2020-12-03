package com.fury.game.content.misc.objects

import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.game.content.skill.Skill
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.combat.CombatConstants
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.player.content.BonusManager
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager
import com.fury.game.entity.character.player.content.objects.PrivateObjectProcessEvent
import com.fury.game.node.entity.actor.`object`.Cannon
import com.fury.game.node.entity.actor.`object`.ObjectManager
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Misc

object DwarfMultiCannon {

    enum class CannonType(val pieces: IntArray, val objects: IntArray, val stock: Int) {
        NORMAL(intArrayOf(6, 8, 10, 12), intArrayOf(7, 8, 9, 6), 30),
        GOLD(intArrayOf(20494, 20495, 20496, 20497), intArrayOf(29398, 29401, 29402, 29406), 60),
        ROYAL(intArrayOf(20498, 20499, 20500, 20501), intArrayOf(29403, 29404, 29405, 29408), 90),
    }

    private val CANNON_EMOTES = intArrayOf(303, 305, 307, 289, 184, 182, 178, 291)
    private val CANNONBALL = Item(2)

    @JvmStatic
    fun fire(player: Player, cannon: GameObject) {
        if (cannon is Cannon)
            stock(player, cannon, false)
    }

    private fun stock(player: Player, cannon: Cannon, auto: Boolean) {
        if (cannon.balls < cannon.cannonType.stock) {
            var amount = player.inventory.getAmount(CANNONBALL)
            if (amount == 0)
                player.message(if (auto) "You don't have any cannon balls in your inventory to load into the cannon." else "You need to load your cannon with cannon balls before firing it!")
            else {
                val add = cannon.cannonType.stock - cannon.balls
                if (amount > add)
                    amount = add
                cannon.balls += amount
                player.inventory.delete(Item(CANNONBALL, amount))
                player.message(if (auto) "$amount cannon balls were automatically loaded into the cannon." else "You load the cannon with $amount cannon balls.")
            }
        } else
            player.message("Your cannon is full.")
    }

    @JvmStatic
    fun pickupCannon(player: Player, stage: Int, cannon: GameObject) {
        if (!PrivateObjectManager.isPlayerObject(player, cannon))
            player.message("This is not your cannon.")
        else if (cannon is Cannon) {
            val space = if (cannon.balls > 0) stage + 1 else stage
            if (player.inventory.spaces < space) {
                player.message("You need at least $space inventory spots to pickup your cannon.")
                return
            }
            cannon.warned = false
            if (!PrivateObjectManager.deleteObject(player, cannon))
                return
            player.movement.lock(3)
            player.message("You pick up the cannon. It's really heavy.")
            for (i in 0 until stage)
                player.inventory.add(Item(cannon.cannonType.pieces[i]))
            if (cannon.balls > 0) {
                player.inventory.add(Item(CANNONBALL, cannon.balls))
                cannon.balls = 0
            }

        }
    }

    private fun getAngle(i: Int): Int {
        return i * 360 / CANNON_EMOTES.size
    }

    @JvmStatic
    fun setUp(player: Player, type: CannonType) {
        if (PrivateObjectManager.containsObjectValue(player, *type.objects)) {
            player.message("You can only have one cannon set up at the same time.")
            return
        }
        if (player.isInWilderness) {
            player.message("You can't place your cannon here.")
            return
        }
        val count = type.pieces
                .takeWhile { player.inventory.contains(Item(it)) }
                .count()
        val pos = player.transform(-3, -3, 0)
        if (!World.isTileFree(pos.x, pos.y, pos.z, 3) || ObjectManager.getStandardObject(player) != null || ObjectManager.getStandardObject(pos) != null) {
            player.message("There isn't enough space to setup here.")
            return
        }
        val objects = arrayOfNulls<Cannon>(count)
        for (i in 0 until count)
            objects[i] = getObject(i, pos, type)
        val cycles = LongArray(count)
        for (i in 0 until count - 1)
            cycles[i] = 1200
        //Milliseconds
        cycles[count - 1] = when (type) {
            CannonType.ROYAL -> 2100000
            CannonType.GOLD -> 1800000
            else -> 1500000
        }
        player.movement.reset()
        player.movement.lock()
        player.direction.face(pos)
        PrivateObjectManager.addPrivateObject(player, objects, cycles, object : PrivateObjectProcessEvent {
            private var step: Int = 0
            private var rotation: Int = 0
            private var decayTime: Long = 0

            override fun spawnObject(player: Player, cannon: GameObject) {
                player.perform(Animation(827))
                when (step) {
                    0 -> player.message("You place the cannon base on the ground.", true)
                    1 -> player.message("You add the stand.", true)
                    2 -> player.message("You add the barrels.", true)
                    3 -> {
                        player.message("You add the furnace.", true)
                        decayTime = Misc.currentTimeMillis() + cycles[cycles.size - 1]
                        Achievements.finishAchievement(player, Achievements.AchievementData.SETUP_DWARF_CANNON)
                    }
                }
                player.inventory.delete(Item(type.pieces[step]))
                if (step++ == cycles.size - 1)
                    player.movement.lock(1)
            }

            override fun process(player: Player, cannon: GameObject) {
                if (cannon !is Cannon)
                    return

                if (step != type.pieces.size || !player.clientLoadedMapRegion || player.finished)
                    return
                if (!cannon.warned && decayTime - Misc.currentTimeMillis() < 5 * 1000 * 60) {
                    player.message("Your cannon is about to decay!", 0x480000)
                    cannon.warned = true
                }
                if (cannon.balls == 0)
                    return
                rotation++
                if (rotation == CANNON_EMOTES.size * 2)
                    rotation = 0
                if (rotation % 2 == 0)
                    return
                World.sendObjectAnimation(player, cannon, Animation(CANNON_EMOTES[rotation / 2]))
                var target: Mob? = null
                var lastD = Integer.MAX_VALUE.toDouble()
                val angle = getAngle(rotation / 2)
                val objectSizeX = cannon.definition.sizeX
                val objectSizeY = cannon.definition.sizeY
                for (npc in GameWorld.regions.getLocalNpcs(cannon)) {
                    if (npc == player.familiar || npc.isDead || npc.finished || npc.z != cannon.z || !Misc.isOnRange(npc.x, npc.y, npc.sizeX, npc.sizeY, cannon.x, cannon.y, objectSizeX, 8) || !npc.definition.hasAttackOption() || !npc.combat.clippedProjectile(cannon, false) || npc.isCantInteract)
                        continue
                    if (!player.controllerManager.canHit(npc))
                        continue

                    if (!player.inMulti() || !npc.inMulti()) {
                        if (npc.combat.attackedBy != player && npc.combat.attackedByDelay > System.currentTimeMillis())
                            continue
                        if (player.combat.attackedBy != npc && player.combat.attackedByDelay > System.currentTimeMillis())
                            continue
                    }

                    val xOffset = npc.x + npc.sizeX / 2.0 - (cannon.x + objectSizeX / 2.0)
                    val yOffset = npc.y + npc.sizeY / 2.0 - (cannon.y + objectSizeY / 2.0)
                    val distance = Math.hypot(xOffset, yOffset)
                    var targetAngle = Math.toDegrees(Math.atan2(xOffset, yOffset))
                    if (targetAngle < 0)
                        targetAngle += 360.0
                    val ratioAngle = 22.5//Math.toDegrees(Math.atan(distance)) / 2;
                    if (targetAngle < angle - ratioAngle || targetAngle > angle + ratioAngle || lastD <= distance)
                        continue
                    lastD = distance
                    target = npc
                }
                if (target != null) {
                    val bonuses = target.bonuses
                    val hitChance = player.skills.getLevel(Skill.RANGED) - if (bonuses != null) bonuses[BonusManager.DEFENCE_RANGE] else 0
                    val projectile = Projectile(cannon.transform(objectSizeX / 2, objectSizeY / 2, 0), target, 53, 38, 38, 30, 40, 0, 0)
                    ProjectileManager.send(projectile)
                    cannon.balls -= 1
                    Achievements.doProgress(player, Achievements.AchievementData.FIRE_50000_CANNON_BALLS)
                    val twoBalls = cannon.balls > 0 && Misc.random(4) == 0

                    GameWorld.schedule(projectile.getTickDelay(), {
                        val damage = if (hitChance < Misc.random(100)) 0 else Misc.random(300)
                        Achievements.doProgress(player, Achievements.AchievementData.DEAL_15M_DAMAGE, damage)
                        target.combat.applyHit(Hit(player, damage, HitMask.RED, CombatIcon.CANNON))
                        player.skills.addExperience(Skill.RANGED, damage / 5.0)
                    })

                    if (twoBalls) {
                        cannon.balls -= 1
                        Achievements.doProgress(player, Achievements.AchievementData.FIRE_50000_CANNON_BALLS)
                        val proj = Projectile(cannon.transform(objectSizeX / 2, objectSizeY / 2, 0), target, 53, 38, 38, 30, 40, 0, 0)
                        ProjectileManager.send(proj)
                        GameWorld.schedule(proj.getTickDelay(), {
                            val damage = if (hitChance < Misc.random(100)) 0 else Misc.random(300)
                            Achievements.doProgress(player, Achievements.AchievementData.DEAL_15M_DAMAGE, damage)
                            target.combat.applyHit(Hit(player, damage, HitMask.RED, CombatIcon.CANNON))
                            player.skills.addExperience(Skill.RANGED, damage / 5.0)
                        })
                    }

                    target.setTarget(player)
                    if (target is Familiar)
                        CombatConstants.skullPlayer(player)

                    if (cannon.balls == 0) {
                        if (cannon.cannonType == CannonType.ROYAL && player.isWithinDistance(cannon, 15)) {
                            stock(player, cannon, true)
                        } else
                            player.message("Your cannon has ran out of ammo!")
                    }
                }
            }

            override fun destroyObject(player: Player, `object`: GameObject) {
                if (`object` is Cannon && `object`.warned) {
                    player.message("Your cannon has decayed!", 0x480000)
                    player.decayedCannons.add(type)
                }
            }
        })
    }

    private fun getObject(i: Int, tile: Position, cannonType: CannonType): Cannon {
        return Cannon(cannonType.objects[i], tile, 10, 0, Revision.RS2, cannonType)
    }
}