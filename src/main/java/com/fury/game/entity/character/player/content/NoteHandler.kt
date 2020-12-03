package com.fury.game.entity.character.player.content

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.network.packet.out.NoteReset
import com.fury.game.network.packet.out.Notes
import java.util.*

class NoteHandler(private val player: Player) {
    var notes: MutableList<String> = ArrayList()
    var noteColours: MutableList<Int> = ArrayList()

    fun login() {
        //Send blank reset
        player.send(NoteReset())
        //Send notes
        player.send(Notes(notes, noteColours))
    }

    fun add(index: Int, text: String) {
        if (notes.size - 1 < index) {
            notes.add(text)
            noteColours.add(DEFAULT_COLOUR)
        } else {
            notes[index] = text
            noteColours[index] = DEFAULT_COLOUR
        }
    }

    fun delete(index: Int) {
        notes.removeAt(index)
        noteColours.removeAt(index)
    }

    fun deleteAll() {
        for (i in notes.size - 1 downTo 0) {
            notes.removeAt(i)
            noteColours.removeAt(i)
        }
    }

    fun setColour(index: Int, colour: Int) {
        noteColours[index] = colour
    }

    companion object {
        private const val DEFAULT_COLOUR = 1
    }
}
