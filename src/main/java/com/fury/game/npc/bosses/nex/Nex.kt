package com.fury.game.npc.bosses.nex

import com.fury.core.model.node.entity.Entity
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.controller.impl.ZarosGodwars
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.mob.combat.NexCombat
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.HitMask
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Utils
import java.util.*

class Nex(id: Int, tile: Position, spawned: Boolean) : Mob(id, tile, spawned) {

    var isFollowTarget: Boolean = false
    var stage: Int = 0
    private var minionStage: Int = 0
    var flyTime: Int = 0
    var lastVirus: Long = 0
    var isEmbracedShadow: Boolean = false
    var isTrapsSetUp: Boolean = false
    var lastSiphon: Long = 0
    var isDoingSiphon: Boolean = false
    val bloodReavers: Array<Mob?>
    private var switchPrayersDelay: Int = 0

    override val magePrayerMultiplier: Double
        get() = 0.6

    override val rangePrayerMultiplier: Double
        get() = 0.6

    override val meleePrayerMultiplier: Double
        get() = 0.6

    override val possibleTargets: ArrayList<Figure>
        get() = ZarosGodwars.getPossibleTargets()

    val attacksStage: Int
        get() = stage

    init {
        setCantInteract(true)
        capDamage = 500
        lureDelay = 3000
        blockDeflections = true
        bloodReavers = arrayOfNulls(3)
        run = true
        isIntelligentRouteFinder = true
        isForceFollowClose = true
    }

    override fun processNpc() {
        synchronized(ZarosGodwars::javaClass) {
            if (flyTime > 0)
                flyTime--
            if (stage == 0 && minionStage == 0 && health.hitpoints <= 24000) {
                capDamage = 0
                forceChat("Fumus, don't fail me!")
                mobCombat!!.addCombatDelay(1)
                ZarosGodwars.breakFumusBarrier()
                //            playSound(3321, 2);
                minionStage = 1
            } else if (stage == 1 && minionStage == 1 && health.hitpoints <= 18000) {
                capDamage = 0
                forceChat("Umbra, don't fail me!")
                mobCombat!!.addCombatDelay(1)
                ZarosGodwars.breakUmbraBarrier()
                //            playSound(3307, 2);
                minionStage = 2
            } else if (stage == 2 && minionStage == 2 && health.hitpoints <= 12000) {
                capDamage = 0
                forceChat("Cruor, don't fail me!")
                mobCombat!!.addCombatDelay(1)
                ZarosGodwars.breakCruorBarrier()
                //            playSound(3298, 2);
                minionStage = 3
            } else if (stage == 3 && minionStage == 3 && health.hitpoints <= 6000) {
                capDamage = 0
                forceChat("Glacies, don't fail me!")
                mobCombat!!.addCombatDelay(1)
                ZarosGodwars.breakGlaciesBarrier()
                //            playSound(3327, 2);
                minionStage = 4
            } else if (stage == 4 && minionStage == 4) {
                if (switchPrayersDelay > 0)
                    switchPrayersDelay--
                else {
                    switchPrayers()
                    resetSwitchPrayersDelay()
                }
            }
            if (isDead || isDoingSiphon || isCantInteract)
                return
            if (!mobCombat!!.process())
                checkAggression()
        }
    }

    fun calculatePossibleTargets(current: Position, position: Position, northSouth: Boolean): ArrayList<Figure> {
        val list = ArrayList<Figure>()
        for (e in possibleTargets) {
            if (e !is Familiar && withinArea(e, current.x, current.y, position.x + if (northSouth) 2 else 0, position.y + if (!northSouth) 2 else 0) || withinArea(e, position.x, position.y,
                            current.x + if (northSouth) 2 else 0,
                            current.y + if (!northSouth) 2 else 0))
                list.add(e)
        }
        return list
    }

    fun moveNextStage() {
        if (stage == 0 && minionStage == 1) {
            capDamage = 500
            forceChat("Darken my shadow!")
            ProjectileManager.send(Projectile(ZarosGodwars.umbra, this, 2244, 18, 18, 60, 30, 0, 0))
            mobCombat!!.addCombatDelay(1)
            stage = 1
            //            playSound(3302, 2);
        } else if (stage == 1 && minionStage == 2) {
            capDamage = 500
            forceChat("Flood my lungs with blood!")
            ProjectileManager.send(Projectile(ZarosGodwars.cruor, this, 2244, 18, 18, 60, 30, 0, 0))
            mobCombat!!.addCombatDelay(1)
            stage = 2
            //            playSound(3306, 2);
        } else if (stage == 2 && minionStage == 3) {
            capDamage = 500
            killBloodReavers()
            forceChat("Infuse me with the power of ice!")
            ProjectileManager.send(Projectile(ZarosGodwars.glacies, this, 2244, 18, 18, 60, 30, 0, 0))
            mobCombat!!.addCombatDelay(1)
            stage = 3
            //            playSound(3303, 2);
        } else if (stage == 3 && minionStage == 4) {
            capDamage = 500
            forceChat("NOW, THE POWER OF ZAROS!")
            animate(6326)
            graphic(1204)
            mobCombat!!.addCombatDelay(1)
            health.heal(6000)
            stage = 4
            //            playSound(3312, 2);
        }
    }

    fun resetSwitchPrayersDelay() {
        switchPrayersDelay = 35 // 25sec
    }

    fun switchPrayers() {
        setTransformation(if (id == 13449) 13447 else id + 1)
    }

    override fun perform(nextAnimation: Animation) {
        if (isDoingSiphon)
            return
        super.perform(nextAnimation)
    }

    override fun perform(nextGraphic: Graphic) {
        if (isDoingSiphon)
            return
        super.perform(nextGraphic)
    }

    fun sendVirusAttack(hitEntitys: ArrayList<Figure>, possibleTargets: ArrayList<Figure>, infected: Entity) {
        for (t in possibleTargets) {
            if (hitEntitys.contains(t))
                continue
            if (Utils.getDistance(t.x, t.y, infected.x, infected.y) <= 1) {
                t.forceChat("*Cough*")
                t.combat.applyHit(Hit(this, Utils.getRandom(100), HitMask.RED, CombatIcon.NONE))
                hitEntitys.add(t)
                sendVirusAttack(hitEntitys, possibleTargets, infected)
            }
        }
        //        playSound(3296, 2);
    }

    fun killBloodReavers() {
        for (index in bloodReavers.indices) {
            if (bloodReavers[index] == null)
                continue
            val mob = bloodReavers[index]
            bloodReavers[index] = null
            if (mob == null || mob.isDead)
                return
            health.heal(mob.health.hitpoints)
            mob.sendDeath(this)
        }
    }

    companion object {

        fun withinArea(entity: Figure, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
            return entity.x >= x1 && entity.y >= y1 && entity.x <= x2 && entity.y <= y2
        }
    }
}
