package com.fury.game.npc.bosses

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.node.entity.mob.combat.TormentedDemonCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Utils
import java.util.concurrent.TimeUnit

class TormentedDemon(id: Int, tile: Position, spawned: Boolean) : Mob(id, tile, Revision.RS2, spawned) {

    var demonPrayer: BooleanArray
    var fixedCombatType: Int = 0
    var cachedDamage: IntArray
    var shieldTimer: Int = 0
    var fixedAmount: Int = 0
    private var prayerTimer: Int = 0

    init {
        demonPrayer = BooleanArray(3)
        cachedDamage = IntArray(3)
        shieldTimer = 0
        switchPrayers(0)
        targetDistance = 10
    }

    fun switchPrayers(type: Int) {
        setTransformation(8349 + type)
        demonPrayer[type] = true
        resetPrayerTimer()
    }

    private fun resetPrayerTimer() {
        prayerTimer = 16
    }

    override fun processNpc() {
        super.processNpc()
        if (isDead)
            return
        if (Utils.getRandom(40) <= 2)
            sendRandomProjectile()
        if (mobCombat!!.process()) {// no point in processing
            if (shieldTimer > 0)
                shieldTimer--
            if (prayerTimer > 0)
                prayerTimer--
            if (fixedAmount >= 5)
                fixedAmount = 0
            if (prayerTimer == 0) {
                for (i in cachedDamage.indices) {
                    if (cachedDamage[i] >= 310) {
                        demonPrayer = BooleanArray(3)
                        switchPrayers(i)
                        cachedDamage = IntArray(3)
                    }
                }
            }
            for (i in cachedDamage.indices) {
                if (cachedDamage[i] >= 310) {
                    demonPrayer = BooleanArray(3)
                    switchPrayers(i)
                    cachedDamage = IntArray(3)
                }
            }
        }
    }

    private fun sendRandomProjectile() {
        val tile = Position(x + Utils.random(7), y + Utils.random(7), z)
        animate(10918)
        ProjectileManager.send(Projectile(this, tile, 1887, 34, 16, 30, 35, 16, 0))
        for (player in GameWorld.regions.getLocalPlayers(this)) {
            if (player == null || player.isDead || player.finished || !player.settings.getBool(Settings.RUNNING) || !player.isWithinDistance(tile, 3) || !player.combat.clippedProjectile(this, false))
                continue
            player.message("The demon's magical attack splashes on you.")
            player.combat.applyHit(Hit(this, 281, HitMask.RED, CombatIcon.MAGIC))
        }
    }

    override fun setRespawnTask() {
        if (!finished) {
            reset()
            setPosition(respawnTile!!)
            deregister()
        }
        val mob = this
        GameExecutorManager.slowExecutor.schedule({
            finished = false
            GameWorld.mobs.add(mob)
            mob.lastRegionId = 0
            World.updateEntityRegion(mob)
            loadMapRegions()
            checkMulti()
            shieldTimer = 0
            fixedCombatType = 0
            fixedAmount = 0
        }, (combatDefinition.respawnDelay * 600).toLong(), TimeUnit.MILLISECONDS)
    }

}
