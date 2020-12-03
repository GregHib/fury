package com.fury.game.content.dialogue.impl.npcs

import com.fury.core.model.item.Item
import com.fury.game.content.dialogue.Dialogue
import com.fury.game.content.dialogue.Expressions
import com.fury.game.content.global.Achievements
import com.fury.game.node.entity.actor.figure.player.Variables
import com.fury.game.node.entity.actor.figure.player.misc.HourlyDelay

/**
 * Created by Greg on 07/02/2017.
 */
class WizardCrompertyD : Dialogue() {
    override fun start() {
        val med = Achievements.hasFinishedAll(player, Achievements.Difficulty.MEDIUM)
        val hard = Achievements.hasFinishedAll(player, Achievements.Difficulty.HARD)
        var amount = 0
        if (med)
            amount += 1000
        if (hard)
            amount += 500

        if (amount == 0 || !HourlyDelay.fireOnce(player, Variables.CROMPERTY_ESSENCE_CLAIMED, 24) {
                    player.dialogueManager.sendNPCDialogue(2328, Expressions.NORMAL, "You've done so well at completing achievements,", "I'll give you $amount essence. Happy runecrafting!")
                    player.vars.set(Variables.CROMPERTY_ESSENCE_CLAIMED, System.currentTimeMillis())
                    player.inventory.add(Item(7937, amount))
                    true
                })
            player.dialogueManager.sendNPCDialogue(2328, Expressions.NORMAL, "You're a wizard " + player.username + "!")
    }

    override fun run(optionId: Int) {
        end()
    }
}