package com.fury.core.action.actions

import com.fury.core.event.Event
import com.fury.game.entity.`object`.GameObject
import com.fury.core.model.item.Item

data class ItemOnObjectAction(val item: Item, val itemSlot: Int, val gameObject: GameObject) : Event