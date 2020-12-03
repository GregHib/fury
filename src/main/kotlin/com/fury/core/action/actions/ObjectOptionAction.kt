package com.fury.core.action.actions

import com.fury.core.event.Event
import com.fury.game.entity.`object`.GameObject

data class ObjectOptionAction(val gameObject: GameObject, val option: Int) : Event