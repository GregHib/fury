package com.fury.game.node.entity.actor.figure.mob.extension.misc

import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.world.update.flag.block.Animation
import com.fury.util.Misc

class Death(player: Player) : Mob(2862, player.copyPosition().add(1, 1)) {

    private val format = Misc.formatText(player.username)
    private val strings = arrayOf(
            "There is no escape, $format...",
            "Muahahahaha!",
            "You belong to me!",
            "Beware mortals, $format travels with me!",
            "Your time here is over, $format!",
            "Now is the time you die, $format!",
            "I claim $format as my own!",
            "$format is mine!",
            "Let me escort you back to Edgeville, $format!",
            "I have come for you, $format!"
    )

    init {
        direction.interacting = player
        perform(Animation(401))
        forceChat(strings[Misc.random(strings.size)])
    }
}