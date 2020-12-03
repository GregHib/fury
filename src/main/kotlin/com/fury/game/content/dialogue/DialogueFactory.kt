package com.fury.game.content.dialogue

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.cache.def.npc.NpcDefinition
import com.fury.game.content.dialogue.chain.impl.*
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.core.model.item.Item
import com.fury.game.network.packet.out.*
import java.util.*

class DialogueFactory(val player: Player) {

    companion object {
        private const val MAXIMUM_LENGTH = 100
        private const val DEFAULT_OPTIONS_TITLE = "Select an option"
    }

    private val npcDialogueIds = intArrayOf(4885, 4890, 4896, 4903)
    private val playerDialogueIds = intArrayOf(971, 976, 982, 989)
    private val optionDialogueIds = intArrayOf(13760, 2461, 2471, 2482, 2494)
    private val statementDialogueIds = intArrayOf(356, 359, 363, 368, 374)

    private fun validateLength(vararg text: String) {
        if (Arrays.stream(text).filter({ Objects.nonNull(it) }).anyMatch { s -> s.length > MAXIMUM_LENGTH }) {
            throw IllegalStateException("Dialogue length too long, maximum length is: " + MAXIMUM_LENGTH)
        }
    }

    fun sendInfo(title: String, vararg lines: String) {
        sendInfoBox(InfoDialogue(title, *lines))
    }

    @JvmOverloads fun sendNpc(definition: NpcDefinition, expression: Expressions = Expressions.NORMAL, vararg lines: String) {
        sendNpcChat(NpcDialogue(definition, expression, *lines))
    }

    fun sendItem(title: String, context: String, item: Item) {
        sendItem(ItemDialogue(title, context, item))
    }

    fun sendStatement(vararg options: String) {
        sendStatement(StatementDialogue(*options))
    }

    fun sendOptions(title: String = DEFAULT_OPTIONS_TITLE, vararg options: String) {
        sendOptions(OptionDialogue(title, *options))
    }

    fun sendPlayerChat(expression: Expressions = Expressions.NORMAL, vararg text: String) {
        sendPlayerChat(PlayerDialogue(expression, *text))
    }

    fun sendPlayerChat(dialogue: PlayerDialogue) {
        val lines = dialogue.lines
        validateLength(*lines)

        val startDialogueChildId = playerDialogueIds[lines.size - 1]
        val headChildId = startDialogueChildId - 2
        player.send(PlayerHeadOnInterface(headChildId))
        player.send(InterfaceAnimation(headChildId, dialogue.expression.animationId, Revision.RS2))
        player.send(InterfaceString(startDialogueChildId - 1, player.username))
        for (i in lines.indices)
            player.send(InterfaceString(startDialogueChildId + i, lines[i]))
        player.dialogueId = startDialogueChildId - 3
        player.send(ChatboxInterface(startDialogueChildId - 3))
    }

    fun sendOptions(dialogue: OptionDialogue) {
        val lines = dialogue.options
        val title = dialogue.title
        validateLength(*lines)

        val firstChildId = optionDialogueIds[lines.size - 1]
        player.send(InterfaceString(firstChildId - 1, title))
        for (i in lines.indices)
            player.send(InterfaceString(firstChildId + i, lines[i]))
        player.dialogueId = firstChildId - 2
        player.send(ChatboxInterface(firstChildId - 2))
        player.send(InterfaceDisplayState(firstChildId - 2 + 1 + lines.size + 3, if(title.length > 16) true else title.isEmpty()))
        player.send(InterfaceDisplayState(firstChildId - 2 + 1 + lines.size + 3 + if (lines.size >= 4) 1 else 3, title.length <= 16))
    }

    fun sendStatement(dialogue: StatementDialogue) {
        val lines = dialogue.lines
        validateLength(*lines)

        val firstChildId = statementDialogueIds[lines.size - 1]
        for (i in lines.indices)
            player.send(InterfaceString(firstChildId + i + 1, lines[i]))
        player.send(InterfaceString(firstChildId + lines.size + 1, "Click here to continue"))
        player.dialogueId = statementDialogueIds[lines.size - 1]
        player.send(ChatboxInterface(statementDialogueIds[lines.size - 1]))
    }

    fun sendItem(dialogue: ItemDialogue) {
        player.send(InterfaceAnimation(4883, 591, dialogue.item.revision))
        player.send(InterfaceItemModel(4883, dialogue.item.id, dialogue.item.revision, 180))
        player.send(InterfaceString(4884, dialogue.title))
        player.send(InterfaceString(4885, dialogue.context))
        player.dialogueId = 4882
        player.send(ChatboxInterface(4882))
    }

    fun sendNpcChat(dialogue: NpcDialogue) {
        val lines = dialogue.lines
        val entityId = dialogue.def.id
        val revision = dialogue.def.revision
        validateLength(*lines)

        val startDialogueChildId = npcDialogueIds[lines.size - 1]
        val headChildId = startDialogueChildId - 2
        player.send(NpcHeadOnInterface(entityId, revision, headChildId))
        player.send(InterfaceAnimation(headChildId, dialogue.expression.animationId, Revision.RS2))
        val npcDefinition = Loader.getNpc(entityId, revision)
        player.send(InterfaceString(startDialogueChildId - 1, npcDefinition.getName().replace("_", " ")))
        for (i in lines.indices)
            player.send(InterfaceString(startDialogueChildId + i, lines[i]))
        player.dialogueId = startDialogueChildId - 3
        player.send(ChatboxInterface(startDialogueChildId - 3))
    }

    fun sendInfoBox(dialogue: InfoDialogue) {
        val lines = dialogue.lines
        validateLength(*lines)

        val interfaceId = 6179
        player.send(InterfaceString(interfaceId + 1, dialogue.title))
        for (i in 0..4)
            player.send(InterfaceString(interfaceId + 2 + i, if(i < lines.size - 1) lines[i] else ""))
        player.dialogueId = interfaceId
        player.send(ChatboxInterface(interfaceId))
    }
}