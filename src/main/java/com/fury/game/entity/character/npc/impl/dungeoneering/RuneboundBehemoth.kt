package com.fury.game.entity.character.npc.impl.dungeoneering

import com.fury.game.content.skill.free.dungeoneering.DungeonManager
import com.fury.game.content.skill.free.dungeoneering.RoomReference
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.node.entity.actor.`object`.TempObjectManager
import com.fury.game.world.map.Position

class RuneboundBehemoth(id: Int, tile: Position, manager: DungeonManager, reference: RoomReference) : DungeonBoss(id, tile, manager, reference) {
    private val artifacts: Array<BehemothArtifact?>?

    val npcId: Int
        get() {
            val melee = artifacts!![0]!!.isPrayerEnabled
            val range = artifacts[1]!!.isPrayerEnabled
            val magic = artifacts[2]!!.isPrayerEnabled
            if (melee && magic && range)
                return 11767
            else if (melee && range)
                return 11842
            else if (melee && magic)
                return 11827
            else if (magic && range)
                return 11857
            else if (melee)
                return 11797
            else if (range)
                return 11782
            else if (magic)
                return 11752
            return 11812
        }

    init {
        artifacts = arrayOfNulls(3)
        for (idx in artifacts.indices)
            artifacts[idx] = BehemothArtifact(idx)
    }

    override fun processNpc() {
        if (isDead)
            return
        if (artifacts != null)
            for (artifact in artifacts)
                artifact?.cycle()
        super.processNpc()
    }

    fun reduceHit(hit: Hit) {
        if (hit.combatIcon == CombatIcon.MELEE && artifacts!![0]!!.isPrayerEnabled || hit.combatIcon == CombatIcon.RANGED && artifacts!![1]!!.isPrayerEnabled || hit.combatIcon == CombatIcon.MAGIC && artifacts!![2]!!.isPrayerEnabled)
            return
        hit.damage = 0
    }

    fun activateArtifact(`object`: GameObject, type: Int) {
        val artifact = artifacts!![type]
        if (artifact!!.isActive)
            return
        TempObjectManager.spawnObjectTemporary(GameObject(if (type == 0) 53980 else if (type == 1) 53982 else 53981, `object`), 30000, false, true)
        artifact.setActive(true, true)
        sendNPCTransformation()
    }

    override fun resetTransformation() {
        for (artifact in artifacts!!)
            artifact!!.setActive(false, false)
        sendNPCTransformation()
    }

    private fun sendNPCTransformation() {
        setTransformation(npcId)
    }

    internal inner class BehemothArtifact(val type: Int) {
        var isActive: Boolean = false
            private set
        private var cycle: Int = 0

        val isPrayerEnabled: Boolean
            get() = cycle < 25 && isActive

        fun cycle() {
            if (isActive) {
                cycle++
                if (cycle == 50)
                    setActive(false, true)
                else if (cycle == 25)
                    sendNPCTransformation()
            }
        }

        fun setActive(active: Boolean, message: Boolean) {
            this.isActive = active
            if (!active)
                cycle = 0
            if (message) {
                for (p2 in manager.party.team) {
                    if (manager.isAtBossRoom(p2))
                        continue
                    p2.message("The " + ARTIFACT_TYPE[type] + " artifact has been " + (if (active) "desactivated" else "re-charged") + "!")
                }
            }
        }
    }

    companion object {

        private val ARTIFACT_TYPE = arrayOf("Melee", "Range", "Magic")
    }
}
