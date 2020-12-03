package com.fury.game.npc.minigames.pest

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.global.minigames.impl.PestControl
import com.fury.game.node.entity.mob.combat.PestPortalCombat
import com.fury.game.world.map.Position
import com.fury.util.FontUtils
import com.fury.util.Misc
import java.util.function.Consumer

class PestPortal(id: Int, tile: Position, var control: PestControl, deathListener: Consumer<Figure>) : Mob(id, tile, true, deathListener) {
    private var nextWave: Int = 0
    private lateinit var portalDeath: Consumer<PestPortal>

    val isShieldDown: Boolean
        get() = id < 6146

    private val indexForId: Int
        get() {
            when (id) {
                6146, 6142 -> return 0
                6147, 6143 -> return 1
                6148, 6144 -> return 2
                6149, 6145 -> return 3
                3782, 3784, 3785 -> return 4
            }
            return -1
        }

    init {
        isCantFollowUnderCombat = true
    }

    fun dropShield() {
        if (id >= 6146) {
            control.sendTeamMessage(PORTAL_DESCRIPTIONS[id - 6146] + " portal shield has dropped!</span>")
            setTransformation(id - 4)
        }
    }

    override fun processNpc() {
        super.processNpc()
        nextWave++
        if (nextWave % 25 == 0) {
            if (control.createPestNpc(indexForId)) {
                if (Misc.random(10) == 0)
                // double spawn xD
                    control.createPestNpc(indexForId)
            }
        }
    }

    fun onDeath(portalDeath: Consumer<PestPortal>) {
        this.portalDeath = portalDeath
    }

    companion object {

        private val PORTAL_DESCRIPTIONS = arrayOf(FontUtils.add("The purple, western", 0xff00ff), FontUtils.add("The blue, eastern", 0x6666ff), FontUtils.add("The yellow, south-eastern", 0xffff00), FontUtils.add("The red, south-western", 0xff3333))
    }
}