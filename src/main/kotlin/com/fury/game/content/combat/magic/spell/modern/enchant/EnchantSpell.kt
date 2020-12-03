package com.fury.game.content.combat.magic.spell.modern.enchant

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.entity.character.player.content.Enchantment
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import java.util.*

abstract class EnchantSpell : SkillSpell() {
    override val animation: Optional<Animation>
        get() = if(enchantment == null) Optional.empty() else Optional.of(enchantment!!.animation)
    override val graphic: Optional<Graphic>
        get() = if(enchantment == null) Optional.empty() else Optional.of(enchantment!!.graphic)

    private var enchantment: Enchantment? = null

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnItemContext) {
            enchantment = Enchantment.get(this, context.item.id)

            if (enchantment == null)
                figure.message("This spell can not be cast on this item.")

            return enchantment != null
        }
        return false
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnItemContext && enchantment != null) {
            figure.inventory.set(Item(enchantment!!.enchanted, 1), context.slot)
            figure.packetSender.sendTab(GameSettings.MAGIC_TAB)
        }
    }
}