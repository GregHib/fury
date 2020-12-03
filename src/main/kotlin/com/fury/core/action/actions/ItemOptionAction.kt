package com.fury.core.action.actions

import com.fury.core.event.Event
import com.fury.core.model.item.Item

data class ItemOptionAction(val item: Item, val slot: Int, val option: Int) : Event