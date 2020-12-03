package com.fury.game.content.dialogue.chain.impl

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.npc.NpcDefinition
import com.fury.game.content.dialogue.Expressions
import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.chain.Chainable

class NpcDialogue(val def: NpcDefinition, val expression: Expressions, vararg val lines: String) : Chainable {

    constructor(id: Int, revision: Revision = Revision.RS2, vararg lines: String) : this(Loader.getNpc(id, revision), Expressions.NORMAL, *lines)

    override fun accept(factory: DialogueFactory) {
        factory.sendNpcChat(this)
    }

}