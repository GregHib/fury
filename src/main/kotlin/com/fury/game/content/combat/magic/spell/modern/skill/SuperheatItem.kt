package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants
import com.fury.game.content.skill.free.smithing.Smelting
import com.fury.game.content.skill.free.smithing.SmeltingData
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class SuperheatItem : SkillSpell() {
    override val id = 61693
    override val level = 43
    override val experience = 53.0
    override val animation = Optional.of(Animation(725))
    override val graphic = Optional.of(Graphic(148, height = GraphicHeight.HIGH))
    override val items = Optional.of(arrayOf(Item(554, 4), Item(561)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnItemContext) {
            val bar = SmeltingData.forOre(context.item.id)
            if (bar == null) {
                figure.message("This spell can only be cast on ores.")
                return false
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnItemContext) {

            val bar = SmeltingData.forOre(context.item.id)
            if (bar == null) {
                figure.message("This spell can only be cast on ores.")
                return
            }

            if (bar.canSmelt(figure)) {
                Smelting.handleBarCreation(figure, bar)
                figure.packetSender.sendTab(GameSettings.MAGIC_TAB)
            }
        }
    }
}