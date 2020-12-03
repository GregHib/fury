package com.fury.game.content.dialogue.chain.impl

import com.fury.game.content.dialogue.Expressions
import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.chain.Chainable

class PlayerDialogue(val expression: Expressions, vararg val lines: String) : Chainable {

    override fun accept(factory: DialogueFactory) {
        factory.sendPlayerChat(this)
    }

}