package com.fury.core.engine.sync

import com.fury.core.engine.sync.task.*
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.EntityList
import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.Executors
import java.util.concurrent.Phaser

class ParallelClientSychronizer : ClientSynchronizer {

    companion object {
        private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), ThreadFactoryBuilder().setNameFormat("UpdateThread").setPriority(Thread.MAX_PRIORITY).build())
    }

    private val phaser = Phaser(1)

    override fun synchronize(players: EntityList<Player>, mobs: EntityList<Mob>) {

        // player movement
        phaser.bulkRegister(players.size())
        players.forEach { player -> executor.submit(PhasedUpdateTask(phaser, PlayerPreUpdateTask(player))) }
        phaser.arriveAndAwaitAdvance()

        // npc movement
        phaser.bulkRegister(mobs.size())
        mobs.forEach { npc -> executor.submit(PhasedUpdateTask(phaser, MobPreUpdateTask(npc))) }
        phaser.arriveAndAwaitAdvance()

        // player updating
        phaser.bulkRegister(players.size())
        players.forEach { player -> executor.submit(PhasedUpdateTask(phaser, PlayerUpdateTask(player))) }
        phaser.arriveAndAwaitAdvance()

        // npc updating
        phaser.bulkRegister(players.size())
        players.forEach { player -> executor.submit(PhasedUpdateTask(phaser, MobUpdateTask(player))) }
        phaser.arriveAndAwaitAdvance()

        // reset player
        phaser.bulkRegister(players.size())
        players.forEach { player -> executor.submit(PhasedUpdateTask(phaser, PlayerPostUpdateTask(player))) }
        phaser.arriveAndAwaitAdvance()

        // reset npc
        phaser.bulkRegister(mobs.size())
        mobs.forEach { npc -> executor.submit(PhasedUpdateTask(phaser, MobPostUpdateTask(npc))) }
        phaser.arriveAndAwaitAdvance()
    }

}