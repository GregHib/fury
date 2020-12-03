package com.fury.game.entity.character.npc.impl.fightkiln


import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.controller.impl.FightKiln
import com.fury.game.node.entity.mob.combat.HarAkenCombat
import com.fury.game.world.map.Position
import com.fury.util.Utils
import java.util.*

class HarAken(id: Int, tile: Position, var controller: FightKiln) : Mob(id, tile, Revision.PRE_RS3) {

    var time: Long = 0
    private var spawnTentacleTime: Long = 0
    private var underLava: Boolean = false
    private val tentacles: MutableList<HarAkenTentacle>

    fun resetTimer() {
        underLava = !underLava
        if (time == 0L)
            spawnTentacleTime = Utils.currentTimeMillis() + 9000
        time = Utils.currentTimeMillis() + if (underLava) 45000 else 30000
    }

    init {
        tentacles = ArrayList()
        //setForceMultiArea(true);
    }

    override fun processNpc() {
        super.processNpc()
        if (health.hitpoints <= 0)
            return
        //cancelFaceEntityNoCheck();
    }

    fun process() {
        if (health.hitpoints <= 0)
            return
        if (time != 0L) {
            if (time < Utils.currentTimeMillis()) {
                if (underLava) {
                    controller.showHarAken()
                    resetTimer()
                } else
                    controller.hideHarAken()
            }
            if (spawnTentacleTime < Utils.currentTimeMillis())
                spawnTentacle()

        }
    }

    fun spawnTentacle() {
        tentacles.add(HarAkenTentacle(if (Utils.random(2) == 0) 15209 else 15210, controller.tentacleTile, this))
        spawnTentacleTime = Utils.currentTimeMillis() + Utils.random(15000, 25000)
    }

    fun removeTentacles() {
        for (t in tentacles)
            t.deregister()
        tentacles.clear()
    }

    fun removeTentacle(tentacle: HarAkenTentacle) {
        tentacles.remove(tentacle)

    }


}
