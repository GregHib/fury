package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.content.global.Achievements
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

class HighAlchemy : SkillSpell() {
    override val id = 61790
    override val level = 55
    override val experience = 65.0
    override val animation = Optional.of(Animation(713))
    override val graphic = Optional.of(Graphic(113))
    override val staffAnimation = Optional.of(Animation(9633))
    override val staffGraphic = Optional.of(Graphic(1693))
    override val items = Optional.of(arrayOf(Item(554, 5), Item(561)))

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player) {
            if (!figure.inventory.hasRoom(if (figure.isInDungeoneering) DungeonConstants.RUSTY_COINS else 995, true)) {
                figure.message("You do not have enough room in your inventory.")
                return false
            }
            if (context is SpellOnItemContext && (context.item.getDefinition().isDestroyItem || context.item.id == if (figure.isInDungeoneering) DungeonConstants.RUSTY_COINS else 995)) {
                figure.message("This spell can not be cast on this item.")
                return false
            }
            if (context is SpellOnItemContext && (context.item.id == 18648 || context.item.id == 5561)) {
                figure.message("You can not alch this item, It must be sold to an NPC.")
                return false
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnItemContext) {
            val item = context.item
            val value = item.definitions.value
            val delete = Item(item, 1)
            if(figure.inventory.delete(delete)) {
                figure.inventory.add(Item(if (figure.isInDungeoneering) DungeonConstants.RUSTY_COINS else 995, value))
                figure.logger.addAlch(delete, value)
                Achievements.doProgress(figure, Achievements.AchievementData.ALCH_100M, value)
                figure.packetSender.sendTab(GameSettings.MAGIC_TAB)
            }
        }
    }
}