package com.fury.game.content.global.dnd.star

import com.fury.cache.Revision
import com.fury.game.entity.`object`.GameObject
import com.fury.game.world.map.Position
import com.fury.util.Misc
import com.fury.game.content.global.dnd.star.ShootingStar.STAR_HEALTH

class CrashedStar(position: Position, private val initialStarSize: Int) : GameObject(ShootingStar.STAR, position, revision = Revision.RS2) {
    constructor(position: Position) : this(position, Misc.inclusiveRandom(0, 8))

    val STAR_DUST_XP = intArrayOf(14, 25, 29, 32, 47, 71, 114, 145, 210)
    private val totalHealth: Int

    var isFirstClick: Boolean = false
        private set

    private var currentLayer: Int = initialStarSize

    var health: Int = 0
        private set

    val requiredMiningLevel: Int
        get() = getCurrentStarSize() * 10


    val percentage: Int
        get() {
            if(currentLayer == -1)
                return 0;

            var healthRemaining = totalHealth

            for (layer in 0 until initialStarSize)
                healthRemaining -= STAR_HEALTH[layer]

            for(layer in currentLayer + 1 until initialStarSize)
                healthRemaining -= STAR_HEALTH[layer]

            healthRemaining -= STAR_HEALTH[currentLayer] - health
            return ((healthRemaining / totalHealth).toDouble() * 100).toInt()
        }

    val layerPercentage: Int
        get() {
            val totalLayerHealth: Double = STAR_HEALTH[currentLayer].toDouble()
            val layerHealthLost: Double = totalLayerHealth - health
            val difference: Double = layerHealthLost / totalLayerHealth
            val expandBy = 100;
            val layerPercentRemaining = difference * expandBy;
            return layerPercentRemaining.toInt()
        }

    init {
        id = ShootingStar.STAR + (8 - initialStarSize)
        var total = 0
        for (i in 0 until getStarSize())
            total += STAR_HEALTH[i]
        totalHealth = total
        resetHealth()
    }

    fun setFirstClick() { isFirstClick = true }
    fun reduceStarLife() { health -= 1 }
    fun increaseStage() { currentLayer -= 1 }
    fun resetHealth() { health = ShootingStar.STAR_HEALTH[currentLayer] }

    fun getStarSize(): Int { return initialStarSize + 1 }
    fun getCurrentStarSize(): Int { return currentLayer + 1 }
    fun hasBeenMined(): Boolean { return currentLayer == -1 }
    fun getXP(): Int { return STAR_DUST_XP[currentLayer] }
}
