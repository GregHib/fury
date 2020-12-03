package com.fury.game.content.misc.objects

import com.fury.core.model.item.Item
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight

enum class ElementalOrb(val item: Item, val graphic: Graphic, val objectId: Int) {
    WATER_ORB(Item(571), Graphic(149, height = GraphicHeight.HIGH), 2151),
    EARTH_ORB(Item(575), Graphic(151, height = GraphicHeight.HIGH), 29415),
    FIRE_ORB(Item(569), Graphic(152, height = GraphicHeight.HIGH), 2153),
    AIR_ORB(Item(573), Graphic(150, height = GraphicHeight.HIGH), 2152);
}