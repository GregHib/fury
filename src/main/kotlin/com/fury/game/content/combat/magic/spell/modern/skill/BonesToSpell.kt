package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.skill.free.cooking.Food
import com.fury.game.content.skill.free.prayer.Bone
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

abstract class BonesToSpell(private val banana: Boolean) : SkillSpell() {

    companion object {
        val bones = Item(Bone.NORMAL.id)
        val bigBones = Item(Bone.BIG.id)
    }

    override val animation = Optional.of(Animation(722))
    override val graphic = Optional.of(Graphic(141, height = GraphicHeight.HIGH))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            val can = figure.inventory.contains(bones) || figure.inventory.contains(bigBones)
            if (!can)
                figure.message("You don't have any bones to cast this spell on.")
            return can
        }
        return true
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if (figure is Player) {
            figure.inventory.notNullItems
                    .filter { it.isEqual(bones) || it.isEqual(bigBones) }
                    .forEach { bone ->
                        figure.inventory.delete(bone)
                        figure.inventory.add(if(banana) Food.BANANA.item else Food.PEACH.item)
                    }
            figure.packetSender.sendTab(GameSettings.INVENTORY_TAB)
        }
    }
}