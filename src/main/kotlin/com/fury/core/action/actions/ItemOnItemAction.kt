package com.fury.core.action.actions

import com.fury.core.event.Event
import com.fury.core.model.item.Item

data class ItemOnItemAction(val itemUsed: Item, val itemOn: Item, val usedSlot: Int, val onSlot: Int) : Event