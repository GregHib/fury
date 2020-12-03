package com.fury.game.content.dialogue.chain.impl

import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.chain.Chainable
import com.fury.core.model.item.Item

class ItemDialogue(val title: String, val context: String, val item: Item) : Chainable {

    override fun accept(factory: DialogueFactory) {
        factory.sendItem(this)
    }

}