package com.fury.game.content.dialogue.chain.impl

import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.chain.Chainable

class StatementDialogue(vararg val lines : String) : Chainable {

    override fun accept(factory: DialogueFactory) {
        factory.sendStatement(this)
    }

}