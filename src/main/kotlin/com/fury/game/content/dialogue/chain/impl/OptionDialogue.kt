package com.fury.game.content.dialogue.chain.impl

import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.chain.Chainable

class OptionDialogue(val title: String, vararg val options: String) : Chainable {

    override fun accept(factory: DialogueFactory) {
        factory.sendOptions(this)
    }
}