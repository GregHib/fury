package com.fury.game.npc.slayer

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.world.map.Position

class Jadinko(id: Int, position: Position, spawned: Boolean) : Mob(id, position, spawned) {

    override fun drop(player: Player, item: Item) {

        //Double mutated vine drops
        if (player.effects.hasActiveEffect(Effects.CARRION_FRUIT))
            if (item.id == 21358)
                item.amount = item.amount * 2

        //Double excrescence drops
        if (player.effects.hasActiveEffect(Effects.DISEASED_FRUIT))
            if (item.id == 21359)
                item.amount = item.amount * 2

        super.drop(player, item)
    }
}
