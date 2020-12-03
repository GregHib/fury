package com.fury.game.world

import com.fury.Config
import com.fury.core.SequentialService
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.task.Task
import com.fury.core.task.TaskManager
import com.fury.game.GameSettings
import com.fury.game.content.events.daily.DailyEventManager
import com.fury.game.content.global.minigames.impl.AllFiredUp
import com.fury.game.content.global.randomevents.BonusWeekend
import com.fury.game.content.tasks.HitpointsRestoreTask
import com.fury.game.content.tasks.PlayerRemovalTask
import com.fury.game.content.tasks.SkillRestoreTask
import com.fury.game.content.tasks.SpecialEnergyRestoreTask
import com.fury.game.entity.character.player.PlayerHandler
import com.fury.game.entity.character.player.content.LoyaltyProgramme
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager
import com.fury.game.node.entity.EntityList
import com.fury.game.node.entity.actor.figure.mob.MobManager
import com.fury.game.system.mysql.impl.Voting
import com.fury.game.world.region.RegionManager
import com.google.common.base.Stopwatch
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.reflect.jvm.jvmName

object GameWorld : SequentialService {

    @JvmStatic
    val players = EntityList<Player>(Config.MAX_PLAYERS, true)

    @JvmStatic
    val mobs = MobManager()

    private val logins = ConcurrentLinkedQueue<Player>()

    private val logouts = ConcurrentLinkedQueue<Player>()

    private var id: Int = 0

    private val logger = Logger.getLogger(GameWorld::class.jvmName)

    private val taskManager = TaskManager()

    private val stopwatch = Stopwatch.createUnstarted()

    @JvmStatic
    val regions = RegionManager()

    override fun start() {
        stopwatch.start()
    }

    override fun execute() {
        World.init()
        DailyEventManager.events()//Not sure how to best init this
        taskManager.schedule(HitpointsRestoreTask())
        taskManager.schedule(SkillRestoreTask())
        taskManager.schedule(SpecialEnergyRestoreTask())
    }

    override fun end() {
        if (GameSettings.DEBUG)
            println("Started world $id in ${stopwatch.elapsed(TimeUnit.MILLISECONDS)} ms.")
    }

    fun process() {
        synchronized(World::class.java) { // synchronizes mutations between engine thread (this thread) & other threads
            try {
                taskManager.processTasks()
            } catch (ex: Exception) {
                logger.throwing("Error sequencing tasks.", "process", ex)
            }

            //Junk to sort out
            Voting.sequence()
            BonusWeekend.sequence()
            LoyaltyProgramme.sequence()
            AllFiredUp.sequence()
            PrivateObjectManager.sequence()
        }
    }

    @JvmStatic
    fun schedule(delay: Int, runnable: Runnable): Task {
        val task = object : Task(delay) {
            override fun run() {
                runnable.run()
                stop()
            }
        }
        schedule(task)
        return task
    }

    @JvmStatic
    fun schedule(delay: Int, runnable: () -> Unit): Task {
        val task = object : Task(delay) {
            override fun run() {
                runnable.invoke()
                stop()
            }
        }
        schedule(task)
        return task
    }

    @JvmStatic
    fun schedule(task: Task) {
        taskManager.schedule(task)
    }

    fun cancelTask(attachment: Any, logout: Boolean = false) {
        taskManager.cancel(attachment, logout)
    }

    @JvmStatic
    fun queueLogin(player: Player) {
        if (logins.contains(player))
            return

        logins.add(player)
    }

    @JvmStatic
    fun queueLogout(player: Player?) {
        if (player == null || logouts.contains(player))
            return

        logouts.add(player)
    }

    fun dequeLogins() {
        if (logins.isEmpty())
            return

        for (amount in 0 until GameSettings.LOGIN_THRESHOLD) {
            val player = logins.poll() ?: break
            PlayerHandler.handleLogin(player)
        }
    }

    fun dequeLogouts() {
        if (logouts.isEmpty())
            return

        for (index in 0 until GameSettings.LOGOUT_THRESHOLD) {
            val player = logouts.poll() ?: break

            schedule(PlayerRemovalTask(player))
        }
    }

    @JvmStatic
    @JvmOverloads
    fun sendBroadcast(message: String, colour: Int = 0x2c7526) {//Green
        players.stream().forEach { player ->
            player?.message(message, colour)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun sendStaffBroadcast(message: String, colour: Int = 0) {
        players.filter { it.rights.isStaff }
                .forEach { it.message(message, colour) }
    }


}