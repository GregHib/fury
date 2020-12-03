package com.fury.core.model.node.entity.actor.figure

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.npc.NpcDefinition
import com.fury.core.model.node.entity.actor.Actor
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.entity.character.combat.effects.EffectManager
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.movement.Movement
import com.fury.game.entity.character.player.actions.ForceMovement
import com.fury.game.node.entity.actor.figure.CharacterDirection
import com.fury.game.node.entity.actor.figure.CombatBuilder
import com.fury.game.node.entity.actor.figure.HealthHandler
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Area
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.Direction
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Utils
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

/**
 * A player or Mob
 *
 * @author Gabriel Hannason
 * @author Greg
 */

abstract class Figure : Actor {
    /**
     * Character update variables
     */

    var index = 0
    var lastKnownRegion: Position? = null
    val updateFlags: EnumSet<Flag> = EnumSet.noneOf(Flag::class.java)
    lateinit var movement: Movement
    var animation: Animation? = null
        private set
    var graphic: Graphic? = null
        private set
    var forcedChat: Optional<String> = Optional.empty()
    var forceMovement: ForceMovement? = null
        set(forceMovement) {
            if (forceMovement != null)
                updateFlags.add(Flag.FORCED_MOVEMENT)
            field = forceMovement
        }
    var transformation: NpcDefinition? = null
        private set
    var isResetMovementQueue: Boolean = false
    var isNeedsPlacement: Boolean = false

    val isUpdateRequired: Boolean
        get() = !updateFlags.isEmpty()

    val deathListener: Consumer<Figure>

    /**
     * Other updating related variables
     */
    private var animationEnd: Long = 0
    var lastRegionId = -1

    /**
     * Controllers & Handlers
     */

    open val combat = CombatBuilder(this).build()
    /*
     * Effects manager & effect based override functions
     */

    val effects = EffectManager()
    lateinit var temporaryAttributes: ConcurrentHashMap<Any, Any>
    val health = HealthHandler(this)
    val direction = CharacterDirection(this)

    open val isPoisonImmune: Boolean
        get() = false

    open val isBoundImmune: Boolean
        get() = effects.hasActiveEffect(Effects.BOUND_IMMUNITY) || getSize() > 2

    open val isStunImmune: Boolean
        get() = effects.hasActiveEffect(Effects.STUN_IMMUNITY) || getSize() > 2


    /**
     * Other figure specific variables
     */

    /** Whether or not the figure is registered on a world entity list  */
    var isRegistered: Boolean = false
    open var run: Boolean = false
    protected var multi: Boolean = false
    var isInDynamicRegion: Boolean = false
        private set

    /*** ABSTRACT METHODS  */

    abstract val maxConstitution: Int

    abstract val blockDeflections: Boolean

    abstract val magePrayerMultiplier: Double

    abstract val rangePrayerMultiplier: Double

    abstract val meleePrayerMultiplier: Double

    open val isDead: Boolean
        get() = health.hitpoints == 0

    override fun perform(animation: Animation) {
        if (GameSettings.DEBUG && animation.id == 65535) {
            println("Invalid animation id")
            animation.id = -1
        }
        if (animation.id != -1)
            this.animationEnd = Utils.currentTimeMillis() + Loader.getAnimation(animation.id, animation.revision).emoteTime
        this.animation = animation
        updateFlags.add(Flag.ANIMATION)
    }

    fun performAnimationNoPriority(nextAnimation: Animation) {
        if (animationEnd > Utils.currentTimeMillis())
            return
        perform(nextAnimation)
    }

    override fun perform(graphic: Graphic) {
        this.graphic = graphic
        updateFlags.add(Flag.GRAPHIC)
    }

    fun forceChat(message: String) {
        this.forcedChat = Optional.of(message)
        updateFlags.add(Flag.FORCED_CHAT)
    }

    @JvmOverloads
    fun setTransformation(id: Int, revision: Revision = Revision.RS2) {
        transformation = Loader.getNpc(id, revision)
        updateFlags.add(if (this is Player) Flag.APPEARANCE else Flag.TRANSFORM)
    }

    open fun resetTransformation() {
        transformation = null
        updateFlags.add(if (this is Player) Flag.APPEARANCE else Flag.TRANSFORM)
    }

    open fun checkMulti() {
        multi = Area.isMulti(this)
    }

    fun inMulti(): Boolean {
        return multi
    }

    protected fun setInMulti(multi: Boolean) {
        this.multi = multi
    }

    constructor(position: Position, deathListener: Consumer<Figure> = Consumer<Figure> { }) : super(position) {
        this.deathListener = deathListener
        lastKnownRegion = position
        init()
    }

    constructor(position: Position) : super(position) {
        this.deathListener = Consumer<Figure> { }
        lastKnownRegion = position
        init()
    }

    constructor(position: Position, revision: Revision, deathListener: Consumer<Figure> = Consumer<Figure> { }) : super(position, revision) {
        this.deathListener = deathListener
        lastKnownRegion = position
        init()
    }

    protected fun init() {
        temporaryAttributes = ConcurrentHashMap()
        movement = Movement(this)
        effects.setFigure(this)
    }

    fun sendDeath(source: Figure?) {
        deathListener.accept(this)
        combat.sendDeath(source)
    }

    /*
     * Region id's & updating
     */

    //TODO extract to CharacterViewport.kt
    fun needMapUpdate(): Boolean {
        val limit = GameSettings.MAP_SIZES[mapSize] / 8 - 2

        if (lastKnownRegion == null)
            return true

        if (this == lastKnownRegion)
            return false

        val offsetX = getChunkX() - (lastKnownRegion!!.getChunkX() - 6)
        val offsetY = getChunkY() - (lastKnownRegion!!.getChunkY() - 6)

        return offsetX < 2 || offsetX >= limit || offsetY < 2 || offsetY >= limit
    }

    open fun loadMapRegions() {
        val surrounding = GameWorld.regions.getSurroundingRegions(this)
        isInDynamicRegion = false
        for (region in surrounding) {
            if (this is Player)
                region.checkLoadMap()

            if (region.isDynamic)
                isInDynamicRegion = true
        }
    }

    /*
     * Getters and setters
	 * Also contains methods.
	 */

    open fun processCharacter() {
        combat.hits.process()
        effects.process()
    }

    open fun reset() {
        reset(true)
    }

    fun reset(attributes: Boolean) {
        health.hitpoints = maxConstitution
        combat.hits.resetReceivedHits()
        combat.resetCombat()
        movement.reset()
        effects.resetEffects()
        combat.hits.resetReceivedDamage()
        combat.attackingDelay = 0
        if (attributes)
            temporaryAttributes.clear()
    }

    @JvmOverloads
    fun moveTo(target: Position, delay: Int = 0, face: Direction? = null) {
        if(delay == 0)
            moveTo(target.x, target.y, target.z, face)
        else
            GameWorld.schedule(delay, { moveTo(target, face = face) })
    }

    @JvmOverloads
    fun moveTo(x: Int, y: Int, z: Int = 0, face: Direction? = null) {
        movement.reset()
        setPosition(x, y, z)
        isNeedsPlacement = true
        isResetMovementQueue = true
        movement.teleporting = true
        if (needMapUpdate())
            loadMapRegions()
        if(face != null)
            direction.direction = face
        World.updateEntityRegion(this)
    }
}