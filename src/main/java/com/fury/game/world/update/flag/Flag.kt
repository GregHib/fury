package com.fury.game.world.update.flag

import com.fury.game.network.packet.update.MaskEncoder
import com.fury.game.network.packet.update.flags.*

/**
 * Represents a Flag that a figure figure can update.
 *
 * @author Greg
 */
enum class Flag(val playerMask: Int, val mobMask: Int, val encoder: MaskEncoder) {
    CHAT(0x80, -1, ChatEncoder),
    FORCED_CHAT(0x4, 0x1, ForcedChatEncoder),
    FORCED_MOVEMENT(0x400, 0x1000, ForcedMovementEncoder),
    ENTITY_INTERACTION(0x1, 0x20, EntityInteractionEncoder),
    FACE_POSITION(0x2, 0x4, FacePositionEncoder),
    APPEARANCE(0x10, -1, AppearanceEncoder),
    ANIMATION(0x8, 0x10, AnimationEncoder),
    GRAPHIC(0x100, 0x80, GraphicEncoder),
    HITS(0x20, 0x8, HitsEncoder),
    TRANSFORM(-1, 0x2, TransformEncoder),
    NAME_CHANGED(-1, 0x8000, NameChangeEncoder),
    COMBAT_CHANGED(-1, 0x400, CombatChangeEncoder);

    companion object {

        val playerFlags = arrayOf(GRAPHIC, ANIMATION, FORCED_CHAT, CHAT, ENTITY_INTERACTION, APPEARANCE, FACE_POSITION, HITS, FORCED_MOVEMENT)
        val mobFlags = arrayOf(ANIMATION, HITS, GRAPHIC, ENTITY_INTERACTION, FORCED_CHAT, TRANSFORM, FACE_POSITION, COMBAT_CHANGED, FORCED_MOVEMENT, NAME_CHANGED)

    }

}