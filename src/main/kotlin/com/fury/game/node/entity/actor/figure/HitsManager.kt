package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.Hit
import com.fury.game.npc.familiar.Familiar
import com.fury.game.world.update.flag.Flag
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue

class HitsManager(private val figure: Figure) {

    private lateinit var receivedHits: ConcurrentLinkedQueue<Hit>
    lateinit var receivedDamage: ConcurrentHashMap<Figure, Int>
    lateinit var nextHits: ArrayList<Hit>

    fun init() {
        receivedHits = ConcurrentLinkedQueue()
        receivedDamage = ConcurrentHashMap()
        nextHits = ArrayList()
    }

    /**
     * Received
     */
    fun process() {
        if (figure is Player && figure.emotesManager.isDoingEmote)
            return

        var count = 0
        while (count++ < 10) {
            val hit = receivedHits.poll() ?: break
            figure.combat.processHit(hit)
        }
    }

    fun addHit(hit: Hit) {
        receivedHits.add(hit)
    }

    fun resetReceivedHits() {
        receivedHits.clear()
    }

    /**
     * Damage
     */

    fun resetReceivedDamage() {
        receivedDamage.clear()
    }

    fun addReceivedDamage(source: Figure?, amount: Int) {
        if (source == null)
            return
        var damage: Int? = receivedDamage[source]
        damage = if (damage == null) amount else damage + amount
        receivedDamage.put(source, damage)
    }

    fun getDamageReceived(source: Figure): Int {
        var total = 0

        val damage = receivedDamage[source]
        if (damage != null)
            total += damage

        if (source is Player && source.familiar != null) {
            val familiar: Familiar = source.familiar!!
            val dmg = receivedDamage[familiar]
            if (dmg != null)
                total += dmg
        }

        if (source.finished) {
            receivedDamage.remove(source)
            return -1
        }

        return total
    }

    fun removeDamage(entity: Figure) {
        receivedDamage.remove(entity)
    }

    fun getMostDamageReceivedSourcePlayer(): Player? {
        var player: Player? = null
        var damage = -1
        for (source in receivedDamage.keys) {
            if (source !is Player && source !is Familiar)
                continue
            var src = source

            var d: Int? = receivedDamage[src]
            if (d == null || source.finished) {
                receivedDamage.remove(src)
                continue
            }
            if (src is Familiar) {
                d += receivedDamage[src.owner as Player]?: 0
                src = src.owner
            }

            if (d > damage) {
                damage = d
                player = src as Player
            }
        }
        return player
    }

    /**
     * Next hits (display hits) needs a rename really
     */

    fun showHit(hit: Hit) {
        nextHits.add(hit)
        figure.updateFlags.add(Flag.HITS)
    }
}