package com.fury.core.model.node.entity.actor.figure.mob

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.npc.NpcDefinition
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.Entity
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.engine.task.executor.GameExecutorManager
import com.fury.game.content.controller.impl.Wilderness
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.npc.NpcCombat
import com.fury.game.entity.character.npc.drops.Drop
import com.fury.game.node.entity.actor.figure.mob.drops.MobDropHandler
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.system.files.loaders.npc.MobBonuses
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions
import com.fury.game.system.files.loaders.npc.MobCombatDefinitionsLoader
import com.fury.game.world.FloorItemManager
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Area
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.EntityStrategy
import com.fury.game.world.map.path.FixedTileStrategy
import com.fury.game.world.map.path.ObjectStrategy
import com.fury.game.world.map.path.PathFinder
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.Direction
import com.fury.util.Misc
import com.fury.util.RandomUtils
import com.fury.util.Utils
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

/**
 * Represents a non-playable figure, which players can interact with.
 *
 * @author Gabriel Hannason
 * @author Greg
 */

open class Mob @JvmOverloads constructor(id: Int, position: Position, revision: Revision = if (id > Loader.getTotalNpcs(Revision.RS2)) Revision.PRE_RS3 else Revision.RS2, var isSpawned: Boolean = true, var privateUser: Player? = null, deathListener: Consumer<Figure> = Consumer {}) : Figure(position, revision, deathListener) {

    constructor(id: Int, position: Position, spawned: Boolean) : this(id, position, Revision.RS2, spawned)
    constructor(id: Int, position: Position, spawned: Boolean, deathListener: Consumer<Figure> = Consumer {}) : this(id, position, Revision.RS2, spawned, null, deathListener)

    val atomicPlayerCount = AtomicInteger(0)
    var respawnTile: Position? = null
    var spawnedFor: Player? = null
    var isVisible = true

    /*override var size = 1
        get() = definition.size*/
    override var sizeX = 1
        get() = definition.size
    override var sizeY = 1
        get() = definition.size

    //Updating
    var actualId: Int = 0
        private set
    var id: Int
        get() = if (transformation != null) transformation!!.id else actualId
        set(id) {
            this.actualId = id
        }
    var name: String
        get() = if (customName != null) customName!! else definition.name
        set(string) {
            this.customName = if (definition.name == string) null else string
            updateFlags.add(Flag.NAME_CHANGED)
        }
    //TODO remove?
    var customName: String? = null
        private set

    //Movement/walking
    var forceWalk: Position? = null
        set(value) {
            movement.reset()
            field = value?.copyPosition()
        }

    val isForceWalking: Boolean
        get() = forceWalk != null
    var walkType: Int = 0
    var isIntelligentRouteFinder: Boolean = false

    fun forceWalkHome() {
        forceWalk = respawnTile
    }

    val combatDefinition: MobCombatDefinitions
        get() = MobCombatDefinitionsLoader.forId(actualId, revision)

    val definition: NpcDefinition
        get() = Loader.getNpc(id, revision)

    //Combat
    var targetDistance = 7
    var combatLevel: Int = 0
        get() = if (field > 0) field else definition.combat
        set(value) {
            field = if (definition.combat == value) -1 else value
            updateFlags.add(Flag.COMBAT_CHANGED)
        }
    val customCombatLevel = -1
    val attackWeakened = BooleanArray(3)
    val defenceWeakened = BooleanArray(3)
    val strengthWeakened = BooleanArray(3)
    var isSummoningNpc: Boolean = false
    var isCantInteract: Boolean = false
        private set
    var forceAggressive: Boolean = false
    var isCantFollowUnderCombat: Boolean = false
    var isForceMultiAttacked: Boolean = false
    var isForceFollowClose: Boolean = false
    var isNoDistanceCheck: Boolean = false
    var lureDelay: Int = 0
        protected set
    var forceTargetDistance: Int = 0
        protected set
    var capDamage = -1
    var mobCombat: NpcCombat? = null
        private set
    var isCantSetTargetAutoRetaliate: Boolean = false
    private var lastAttackedByTarget: Long = 0
    var killer: Player? = null
        private set

    init {
        respawnTile = position.copyPosition()
        actualId = id
        health.hitpoints = maxConstitution
        setBonuses()
        mobCombat = NpcCombat(this)
        lureDelay = 12000
        walkType = definition.walkMask.toInt()
        init()
        GameWorld.mobs.add(this)
        World.updateEntityRegion(this)
        loadMapRegions()
        checkMulti()
    }

    fun deserialize() {
        if (mobCombat == null)
            mobCombat = NpcCombat(this)
        spawn()
    }

    fun checkAggression(): Boolean {
        if (!forceAggressive) {
            val defs = combatDefinition
            if (!defs.isAggressive)
                return false
        }
        val possibleTarget = possibleTargets
        if (!possibleTarget.isEmpty()) {
            val target = RandomUtils.random(possibleTarget)
            setTarget(target)
            target.combat.attackedBy = this
            target.combat.findTargetDelay = Misc.currentTimeMillis() + 10000
            return true
        }
        return false
    }

    fun removeTarget() {
        if (mobCombat!!.target == null)
            return
        mobCombat!!.removeTarget()
    }

    fun canBeAttackedByAutoRetaliate(): Boolean {
        return Utils.currentTimeMillis() - lastAttackedByTarget > lureDelay
    }


    fun setCantInteract(cantInteract: Boolean?) {
        this.isCantInteract = cantInteract!!
        if (cantInteract)
            mobCombat!!.reset()
    }

    fun calcFollow(target: Entity, inteligent: Boolean): Boolean {
        return calcFollow(target, -1, true, inteligent)
    }

    fun calcFollow(target: Entity, maxStepsCount: Int, calculate: Boolean, inteligent: Boolean): Boolean {
        if (inteligent) {
            val steps = PathFinder.findRoute(this, sizeX, sizeY, if (target is GameObject) ObjectStrategy(target) else if (target is Figure) EntityStrategy(target) else FixedTileStrategy(target.x, target.y), true)
            if (steps == -1)
                return false
            if (steps == 0)
                return true
            val bufferX = PathFinder.getLastPathBufferX()
            val bufferY = PathFinder.getLastPathBufferY()
            for (step in steps - 1 downTo 0) {
                if (!movement.addWalkSteps(bufferX[step], bufferY[step], 25, true))
                    break
            }
            return true
        }
        return findBasicRoute(this, target, maxStepsCount, true)
    }

    fun walkTo(x: Int, y: Int) {
        val steps = PathFinder.findRoute(this, sizeX, sizeY, FixedTileStrategy(x, y), true)
        val bufferX = PathFinder.getLastPathBufferX()
        val bufferY = PathFinder.getLastPathBufferY()
        for (i in steps - 1 downTo 0)
            if (!movement.addWalkSteps(bufferX[i], bufferY[i], 25, true))
                break
    }



    /**
     * Overrides
     */

    override var blockDeflections: Boolean = false

    override val magePrayerMultiplier: Double
        get() = 0.0

    override val rangePrayerMultiplier: Double
        get() = 0.0

    override val meleePrayerMultiplier: Double
        get() = 0.0

    override val maxConstitution: Int
        get() = combatDefinition.hitpoints

    override fun checkMulti() {
        multi = isInDynamicRegion || Area.isMulti(this)
    }

    override fun processCharacter() {
        super.processCharacter()
        processNpc()
    }

    override fun reset() {
        super.reset()
        if (respawnDirection != null)
            direction.direction = respawnDirection as Direction
        mobCombat!!.reset()
        forceWalk = null
        setBonuses()
    }

    override fun deregister() {
        if (finished)
            return
        finished = true
        World.updateEntityRegion(this)
        GameWorld.mobs.remove(this)
    }

    override fun register() {}

    override fun toString(): String {
        return "Npc: " + id + " " + super.toString()
    }


    /**
     * Open aka overridables
     */
    open val possibleTargets: ArrayList<Figure>
        get() = getPossibleTargets(false, true)
    open var respawnDirection: Direction? = null
    open var bonuses: IntArray? = null

    fun setBonuses() {
        bonuses = MobBonuses.getBonuses(actualId, revision) // back to real bonuses
    }

    open fun processNpc() {
        if (isDead || movement.isLocked())
            return

        if (!mobCombat!!.process()) {
            if (!isForceWalking) {
                if (!isCantInteract) {
                    if (!checkAggression()) {
                        if (combat.freezeDelay < Utils.currentTimeMillis()) {
                            if (!movement.hasWalkSteps() && walkType and NORMAL_WALK != 0) {
                                val can = Math.random() > 0.9
                                if (can) {
                                    val moveX = Math.round(Math.random() * 10.0 - 5.0).toInt()
                                    val moveY = Math.round(Math.random() * 10.0 - 5.0).toInt()
                                    val defaultX = respawnTile!!.x
                                    val defaultY = respawnTile!!.y
                                    movement.reset()
                                    if (walkType and FLY_WALK != 0)
                                        movement.addWalkSteps(defaultX + moveX, defaultY + moveY, 10, false)
                                    else
                                        movement.addWalkSteps(defaultX + moveX, defaultY + moveY, 5)
                                }
                            }
                        }
                    }
                }
            }
        }
        if (isForceWalking) {
            if (combat.freezeDelay < Utils.currentTimeMillis()) {
                if (x != forceWalk!!.x || y != forceWalk!!.y) {
                    if (!movement.hasWalkSteps()) {
                        val steps = PathFinder.findRoute(this, sizeX, sizeY, FixedTileStrategy(forceWalk!!.x, forceWalk!!.y), true)
                        val bufferX = PathFinder.getLastPathBufferX()
                        val bufferY = PathFinder.getLastPathBufferY()
                        for (i in steps - 1 downTo 0) {
                            if (!movement.addWalkSteps(bufferX[i], bufferY[i], 25, true))
                                break
                        }
                    }
                    if (!movement.hasWalkSteps()) { // failing finding route
                        moveTo(Position(forceWalk!!)) // force tele to the force walk place
                        forceWalk = null // so ofc reached force walk place
                    }
                } else
                // walked till force walk place
                    forceWalk = null
            }
        }
    }

    open fun addDrops(killer: Player): List<Drop>? {
        return null
    }

    open fun drop() {
        killer = combat.hits.getMostDamageReceivedSourcePlayer()
        if (killer == null)
            return
        MobDropHandler.drop(killer!!, this)
    }

    open fun drop(player: Player, item: Item) {
        val stackable = item.getDefinition().isStackable
        val position = centredPosition
        if (!stackable && item.amount > 1) {
            for (i in 0 until item.amount)
                FloorItemManager.addGroundItem(Item(item.id, 1), position, player)
        } else
            FloorItemManager.addGroundItem(item, position, player)
    }

    open fun setRespawnTask() {
        if (!finished) {
            reset()
            moveTo(respawnTile!!)
            deregister()
        }
        GameExecutorManager.slowExecutor.schedule({ this.spawn() }, (if (id == 1265) 500 else combatDefinition.respawnDelay * 1000).toLong(), TimeUnit.MILLISECONDS)
    }


    open fun spawn() {
        finished = false
        GameWorld.mobs.add(this)
        lastRegionId = 0
        //        World.updateEntityRegion(this);
        //        loadMapRegions();
        //        checkMulti();
    }

    open fun setTarget(entity: Figure) {
        if (isForceWalking)
        // if force walk not gonna get target
            return
        mobCombat!!.target = entity
        lastAttackedByTarget = Utils.currentTimeMillis()
    }

    open fun getPossibleTargets(checkNPCs: Boolean, checkPlayers: Boolean): ArrayList<Figure> {
        val sizeX = sizeX
        val sizeY = sizeY
        val agroRatio = 2
        val possibleTarget = ArrayList<Figure>()
        if (checkPlayers) {
            for (player in GameWorld.regions.getLocalPlayers(this)) {
                if (!player.clientLoadedMapRegion
                        || player.z != z
                        || player.isDead
                        || player.finished
                        || !player.settings.getBool(Settings.RUNNING)
                        /*|| player.getAppearence().isHidden()*/
                        || !player.isWithinDistance(this, if (forceTargetDistance > 0) forceTargetDistance else if (combatDefinition.attackStyle == MobCombatDefinitions.MELEE) 4 else if (combatDefinition.attackStyle == MobCombatDefinitions.SPECIAL) 64 else 8)
                        || !isForceMultiAttacked && (!inMulti() || !player.inMulti()) && player.combat.attackedBy !== this && (player.combat.attackedByDelay > System.currentTimeMillis() || player.combat.findTargetDelay > System.currentTimeMillis())
                        || !combat.clippedProjectile(player, false)
                        || !forceAggressive && !Wilderness.isAtWild(this) && player.skills.combatLevel >= combatLevel * 2) {
                    continue
                }
                possibleTarget.add(player)
                if (checkNPCs) {
                    val familiar = player.familiar
                    if (familiar == null || familiar.isDead || familiar.finished || !Misc.isOnRange(x, y, sizeX, sizeY, familiar.x, familiar.y, familiar.sizeX, familiar.sizeY, if (forceTargetDistance > 0) forceTargetDistance else agroRatio) || !combat.clippedProjectile(familiar, false))
                        continue
                    possibleTarget.add(familiar)
                }
            }
        }
        if (checkNPCs) {
            for (npc in GameWorld.regions.getLocalNpcs(this)) {
                if (npc.isFamiliar || npc.z != z || npc == this || npc.isDead || npc.finished || !Misc.isOnRange(x, y, sizeX, sizeY, npc.x, npc.y, npc.sizeX, npc.sizeY, if (forceTargetDistance > 0) forceTargetDistance else agroRatio) || !npc.definition.hasAttackOption() || /*((!inMulti() || !npc.inMulti()) && npc.getCombat().getAttackedBy() != this && npc.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) ||*/ !combat.clippedProjectile(npc, false) || npc.isCantInteract)
                    continue
                possibleTarget.add(npc)
            }
        }
        return possibleTarget
    }

    companion object {

        var NORMAL_WALK = 0x2
        var WATER_WALK = 0x4
        var FLY_WALK = 0x8

        fun findBasicRoute(src: Figure, dest: Entity, maxStepsCount: Int, calculate: Boolean): Boolean {
            var maxStepsCount = maxStepsCount
            val srcPos = src.movement.lastWalkTile
            val destPos = intArrayOf(dest.x, dest.y)
            val srcSizeX = src.sizeX
            val srcSizeY = src.sizeY
            //set destSize to 0 to walk under it else follows
            val destSizeX = dest.sizeX
            val destSizeY = dest.sizeY
            val destScenePos = intArrayOf(destPos[0] + destSizeX - 1, destPos[1] + destSizeY - 1)
            while (maxStepsCount-- != 0) {
                val srcScenePos = intArrayOf(srcPos[0] + srcSizeX - 1, srcPos[1] + srcSizeY - 1)
                if (!Misc.isOnRange(srcPos[0], srcPos[1], srcSizeX, srcSizeY, destPos[0], destPos[1], destSizeX, destSizeY, 0)) {
                    if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] < destScenePos[1] && src.movement.addWalkStep(srcPos[0] + 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
                        srcPos[0]++
                        srcPos[1]++
                        continue
                    }
                    if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] > destScenePos[1] && src.movement.addWalkStep(srcPos[0] - 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
                        srcPos[0]--
                        srcPos[1]--
                        continue
                    }
                    if (srcScenePos[0] < destScenePos[0] && srcScenePos[1] > destScenePos[1] && src.movement.addWalkStep(srcPos[0] + 1, srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
                        srcPos[0]++
                        srcPos[1]--
                        continue
                    }
                    if (srcScenePos[0] > destScenePos[0] && srcScenePos[1] < destScenePos[1] && src.movement.addWalkStep(srcPos[0] - 1, srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
                        srcPos[0]--
                        srcPos[1]++
                        continue
                    }
                    if (srcScenePos[0] < destScenePos[0] && src.movement.addWalkStep(srcPos[0] + 1, srcPos[1], srcPos[0], srcPos[1], true)) {
                        srcPos[0]++
                        continue
                    }
                    if (srcScenePos[0] > destScenePos[0] && src.movement.addWalkStep(srcPos[0] - 1, srcPos[1], srcPos[0], srcPos[1], true)) {
                        srcPos[0]--
                        continue
                    }
                    if (srcScenePos[1] < destScenePos[1] && src.movement.addWalkStep(srcPos[0], srcPos[1] + 1, srcPos[0], srcPos[1], true)) {
                        srcPos[1]++
                        continue
                    }
                    if (srcScenePos[1] > destScenePos[1] && src.movement.addWalkStep(srcPos[0], srcPos[1] - 1, srcPos[0], srcPos[1], true)) {
                        srcPos[1]--
                        continue
                    }
                    return false
                }
                break //for now nothing between break and return
            }
            return true
        }
    }
}
