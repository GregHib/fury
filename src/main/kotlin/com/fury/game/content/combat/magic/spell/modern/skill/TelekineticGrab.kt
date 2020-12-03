package com.fury.game.content.combat.magic.spell.modern.skill

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SkillSpell
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnFloorItemContext
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.world.FloorItemManager
import com.fury.game.world.GameWorld
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class TelekineticGrab : SkillSpell() {
    override val id = 61638
    override val level = 33
    override val experience = 43.0
    override val animation = Optional.of(Animation(711))
    override val graphic = Optional.of(Graphic(142, height = GraphicHeight.MIDDLE))
    override val items = Optional.of(arrayOf(Item(563), Item(556)))
    val hitGraphic = Graphic(144)

    override fun canCast(figure: Figure, context: SpellContext): Boolean {
        if(figure is Player && context is SpellOnFloorItemContext) {
            val position = context.floorItem.tile
            val maxDistance = 7
            val distanceX = figure.x - position.x
            val distanceY = figure.y - position.y
            return if (!figure.combat.clippedProjectile(position, false)
                    || distanceX > 1 + maxDistance
                    || distanceX < -1 - maxDistance
                    || distanceY > 1 + maxDistance
                    || distanceY < -1 - maxDistance) {
                if (!figure.movement.hasWalkSteps()) {
                    figure.movement.reset()
                    figure.movement.addWalkStepsInteract(position.x, position.y, if (figure.settings.getBool(Settings.RUNNING)) 2 else 1, 1, true)
                }
                false
            } else {
                figure.movement.reset()
                true
            }
        }
        return super.canCast(figure, context)
    }

    override fun spellEffect(figure: Figure, context: SpellContext) {
        if(figure is Player && context is SpellOnFloorItemContext) {
            val position = context.floorItem.tile
            val id = context.floorItem.id
            val projectile = Projectile(figure, position, 143, 40, 10, 40, 30, 0, 0)
            figure.direction.face(position)
            ProjectileManager.send(projectile)
            GameWorld.schedule(projectile.getTickDelay(), {
                val floorItem = position.getRegion().getFloorItem(id, position, figure) ?: return@schedule
                Graphic.sendGlobal(figure, hitGraphic, position)
                FloorItemManager.removeGroundItem(figure, floorItem)
            })
        }
    }
}