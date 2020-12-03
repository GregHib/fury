package com.fury.game.node.entity.actor.figure.mob.drops

import com.fury.cache.def.Loader

class DropLogEntry(var item: Int, var amount: Int) {
    var rareDrop: Boolean = Loader.getItem(item).value > 200000 || MobDropHandler.ANNOUNCE.contains(item)
}