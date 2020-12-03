package com.fury.core.engine

import com.fury.Config
import com.fury.Stopwatch
import com.fury.core.engine.sync.SequentialClientSynchronizer
import com.fury.game.world.GameWorld
import com.fury.network.PlayerSession
import com.fury.util.Logger
import com.google.common.util.concurrent.AbstractScheduledService
import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.*

class GameEngine : AbstractScheduledService() {

    private val logicService = createLogicService()

    fun submit(t: Runnable) {
        try {
            logicService.execute(t)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private val synchronizer = SequentialClientSynchronizer()

    override fun scheduler(): Scheduler {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(600, 600, TimeUnit.MILLISECONDS)
    }

    companion object {
        @Volatile
        var cycle: Long = 0

        fun createLogicService(): ScheduledExecutorService {
            val executor = ScheduledThreadPoolExecutor(1)
            executor.rejectedExecutionHandler = ThreadPoolExecutor.CallerRunsPolicy()
            executor.threadFactory = ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build()
            executor.setKeepAliveTime(45, TimeUnit.SECONDS)
            executor.allowCoreThreadTimeOut(true)
            return Executors.unconfigurableScheduledExecutorService(executor)
        }
    }

    var elapsed: Long = 0
    val stopwatch = Stopwatch.start()

    private fun run(task: () -> Unit, name: String) {
        task()
        elapsed = stopwatch.elapsed()
        if (elapsed > 100 && Config.DEBUG)
            println("$name: $elapsed ms")
        stopwatch.reset()
    }

    override fun runOneIteration() {
        cycle++
        try {
            stopwatch.reset()
            val cycleWatch = Stopwatch.start()

            val world = GameWorld
            val players = world.players
            val mobs = world.mobs.mobs

            run(world::dequeLogins, "world.dequeLogins()")

            run(world::dequeLogouts, "world.dequeLogouts()")

            run(world::process, "world.process()")

            run({
                players.forEach { player ->
                    player.session.ifPresent(PlayerSession::handleQueuedMessages)
                    try {
                        player.processCharacter()
                    } catch (ex: Exception) {
                        Logger.handle("error player.sequence(): $player", ex)
                    }
                }
            }, "player.sequence()")


            run({ mobs.forEach { mob -> mob.processCharacter() } }, "npc.sequence()")

            run({
                try {
                    synchronizer.synchronize(players, mobs)
                } catch (ex: Exception) {
                    Logger.handle("Error in the main game sequencer.", ex)
                }
            }, "synchronizer.synchronize()")

            if (cycleWatch.elapsed(100) && Config.DEBUG)
                println(String.format("CYCLE END: %d ms", cycleWatch.elapsed()))

        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

}