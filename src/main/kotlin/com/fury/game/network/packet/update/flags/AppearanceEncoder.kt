package com.fury.game.network.packet.update.flags

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.equip.Equipment
import com.fury.game.container.impl.equip.Slot
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.network.packet.out.PlayerUpdate
import com.fury.game.network.packet.update.MaskEncoder
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.Appearance
import com.fury.game.world.update.flag.block.Gender
import com.fury.network.packet.PacketBuilder
import com.fury.network.packet.ValueType

object AppearanceEncoder : MaskEncoder {
    override fun encodable(player: Player, figure: Figure, state: PlayerUpdate.UpdateState?): Boolean {
        return figure.updateFlags.contains(Flag.APPEARANCE) || state == PlayerUpdate.UpdateState.ADD_LOCAL
    }

    override fun encode(player: Player, figure: Figure, builder: PacketBuilder) {
        if (figure is Player) {
            val appearance = figure.appearance
            val equipment = figure.equipment
            val properties = PacketBuilder()
            properties.put(appearance.gender.ordinal)
            properties.put(figure.prayer.prayerHeadIcon)
            properties.put(if (figure.isInWilderness) appearance.bountyHunterSkull else -1)
            properties.put(if (figure.effects.hasActiveEffect(Effects.SKULL)) 1 else -1)


            if (figure.transformation == null) {
                if (equipment.exists(Slot.HEAD)) {
                    properties.putShort(0x200 + equipment.get(Slot.HEAD).id)
                    properties.put(equipment.get(Slot.HEAD).revision.ordinal)
                } else {
                    properties.put(0)
                }
                if (equipment.exists(Slot.CAPE)) {
                    properties.putShort(0x200 + equipment.get(Slot.CAPE).id)
                    properties.put(equipment.get(Slot.CAPE).revision.ordinal)
                } else {
                    properties.put(0)
                }
                if (equipment.exists(Slot.AMULET)) {
                    properties.putShort(0x200 + equipment.get(Slot.AMULET).id)
                    properties.put(equipment.get(Slot.AMULET).revision.ordinal)
                } else {
                    properties.put(0)
                }
                if (equipment.exists(Slot.WEAPON)) {
                    properties.putShort(0x200 + equipment.get(Slot.WEAPON).id)
                    properties.put(equipment.get(Slot.WEAPON).revision.ordinal)
                } else {
                    properties.put(0)
                }
                if (equipment.exists(Slot.BODY)) {
                    properties.putShort(0x200 + equipment.get(Slot.BODY).id)
                    properties.put(equipment.get(Slot.BODY).revision.ordinal)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.CHEST])
                    properties.put(0)
                }
                if (equipment.exists(Slot.SHIELD)) {
                    properties.putShort(0x200 + equipment.get(Slot.SHIELD).id)
                    properties.put(equipment.get(Slot.SHIELD).revision.ordinal)
                } else {
                    properties.put(0)
                }

                if (disableArms(appearance, equipment) && !requiresArmChange(appearance, equipment)) {
                    properties.put(0)
                } else {
                    if (requiresArmChange(appearance, equipment)) {
                        properties.putShort(32768 + getArmChange(appearance))
                        properties.put(0)
                    } else {
                        properties.putShort(32768 + appearance.look[Appearance.ARMS])
                        properties.put(0)
                    }
                }

                if (equipment.exists(Slot.LEGS)) {
                    properties.putShort(0x200 + equipment.get(Slot.LEGS).id)
                    properties.put(equipment.get(Slot.LEGS).revision.ordinal)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.LEGS])
                    properties.put(0)
                }

                if (disableHead(equipment)) {
                    properties.put(0)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.HEAD])
                    properties.put(0)
                }

                if (equipment.exists(Slot.HANDS)) {
                    properties.putShort(0x200 + equipment.get(Slot.HANDS).id)
                    properties.put(equipment.get(Slot.HANDS).revision.ordinal)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.HANDS])
                    properties.put(0)
                }
                if (equipment.exists(Slot.FEET)) {
                    properties.putShort(0x200 + equipment.get(Slot.FEET).id)
                    properties.put(equipment.get(Slot.FEET).revision.ordinal)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.FEET])
                    properties.put(0)
                }

                if (disableJaw(appearance, equipment)) {
                    properties.put(0)
                } else {
                    properties.putShort(32768 + appearance.look[Appearance.BEARD])
                    properties.put(0)
                }

            } else {
                properties.putShort(-1)
                properties.put(0)
                val transform = figure.transformation
                if (transform != null) {
                    properties.putShort(transform.id)
                    properties.put(transform.revision.ordinal)
                }
            }

            if (figure.transformation == null) {
                val head = figure.equipment.get(Slot.HEAD).id
                val cape = figure.equipment.get(Slot.CAPE).id
                if (head == 20768 || head == 20770 || head == 20772 || cape == 20767 || cape == 20769 || cape == 20771) {
                    properties.put(1)
                    for (i in 0..6)
                        properties.putInt(figure.capeRecolours[i])
                } else {
                    properties.put(0)
                }
            } else {
                properties.put(0)
            }

            properties.put(appearance.look[Appearance.HAIR_COLOUR])
            properties.put(appearance.look[Appearance.SKIN_COLOUR])
            properties.put(appearance.look[Appearance.TORSO_COLOUR])
            properties.put(appearance.look[Appearance.LEG_COLOUR])
            properties.put(appearance.look[Appearance.FEET_COLOUR])

            val skillAnimation = figure.skillAnimation
            if (skillAnimation != null) {
                for (i in 0..6) {
                    properties.putShort(skillAnimation.id)
                    properties.put(skillAnimation.revision.ordinal)
                }
            } else {
                val high = figure.equipment.hasRevision(Revision.PRE_RS3) || figure.equipment.hasRevision(Revision.RS3)
                properties.putShort(figure.characterAnimations.standingAnimation)
                properties.put(if (high) Revision.PRE_RS3.ordinal else Revision.RS2.ordinal)//target.getCharacterAnimations().getStandingAnimationRevision().ordinal());
                properties.putShort(0x337)
                properties.put(0)
                properties.putShort(figure.characterAnimations.walkingAnimation)
                properties.put(if (high) Revision.PRE_RS3.ordinal else Revision.RS2.ordinal)//target.getCharacterAnimations().getWalkingAnimationRevision().ordinal());
                properties.putShort(0x334)
                properties.put(0)
                properties.putShort(0x335)
                properties.put(0)
                properties.putShort(0x336)
                properties.put(0)
                properties.putShort(figure.characterAnimations.runningAnimation)
                properties.put(if (high) Revision.PRE_RS3.ordinal else Revision.RS2.ordinal)//target.getCharacterAnimations().getRunningAnimationRevision().ordinal());
            }

            properties.putLong(figure.longUsername!!)
            properties.put(figure.skills.combatLevel)
            properties.putShort(figure.rights.ordinal)
            properties.putShort(figure.loyaltyTitle.id)


            builder.put(properties.buffer().writerIndex(), ValueType.C)
            builder.putBytes(properties.buffer())
        }
    }

    private fun disableJaw(appearance: Appearance, equipment: Equipment): Boolean {
        val head = equipment.get(Slot.HEAD) ?: return false

        if (appearance.look[Appearance.BEARD] <= 0 || appearance.gender == Gender.FEMALE || head.definitions.isFullHelm)
            return true

        return head.revision.ordinal >= Revision.PRE_RS3.ordinal && head.getDefinition().hideJaw()

    }

    private fun disableHead(equipment: Equipment): Boolean {
        val head = equipment.get(Slot.HEAD) ?: return false

        if (head.revision.ordinal >= Revision.PRE_RS3.ordinal && head.getDefinition().hideHair())
            return true

        return head.definitions.isFullHelm || head.definitions.isHelm

    }

    private fun disableArms(appearance: Appearance, equipment: Equipment): Boolean {
        val body = equipment.get(Slot.BODY) ?: return false

        if (body.revision.ordinal >= Revision.PRE_RS3.ordinal && body.getDefinition().hideArms())
            return true

        if (body.definitions.isFullBody)
            return true

        if (!equipment.get(Slot.BODY).definitions.isBody) {
            if (appearance.gender == Gender.MALE) {
                if (appearance.look[Appearance.CHEST] in 443..456)
                    return true
            } else if (appearance.look[Appearance.CHEST] in 501..564)
                return true
        }
        return false
    }

    private fun requiresArmChange(appearance: Appearance, equipment: Equipment): Boolean {
        if (equipment.get(Slot.BODY).definitions.isBody)
            (if (appearance.gender == Gender.MALE) maleArmFixes else femaleArmFixes)
                    .filter { appearance.look[Appearance.CHEST] == it[0] }
                    .forEach { return true }

        return false
    }


    private fun getArmChange(appearance: Appearance): Int {
        return (if (appearance.gender == Gender.MALE) maleArmFixes else femaleArmFixes)
                .firstOrNull { appearance.look[Appearance.CHEST] == it[0] }
                ?.let { it[1] }
                ?: 0
    }

    private val maleArmFixes = arrayOf(intArrayOf(443, 614), intArrayOf(444, 617), intArrayOf(445, 590), intArrayOf(446, 598), intArrayOf(447, 610), intArrayOf(448, 611), intArrayOf(449, 612), intArrayOf(450, 609), intArrayOf(451, 602), intArrayOf(452, 595), intArrayOf(453, 604), intArrayOf(454, 605), intArrayOf(455, 606), intArrayOf(456, 619))
    private val femaleArmFixes = arrayOf(intArrayOf(556, 424), intArrayOf(557, 419), intArrayOf(558, 426), intArrayOf(559, 415), intArrayOf(560, 409), intArrayOf(561, 402), intArrayOf(562, 411), intArrayOf(563, 412), intArrayOf(564, 413))

}