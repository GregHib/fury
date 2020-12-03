package com.fury.game.content.combat.magic.spell.modern

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.Projectile
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.combat.magic.SpellContext
import com.fury.core.model.node.entity.actor.figure.combat.magic.spell.CombatNormalSpell
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.global.Achievements
import com.fury.game.entity.character.player.content.BonusManager
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import java.util.*

class DragonFire : CombatNormalSpell() {
    override val maxHit = 200
    override val animation = Optional.of(Animation(6696))
    override val castGraphic = Optional.of(Graphic(1165))
    override val hitGraphic = Optional.of(Graphic(1167, height = GraphicHeight.HIGH))
    override val projectile = Optional.of(arrayOf(SpeedProjectile(1166, 30, 26, 52, 0, 0) as Projectile))
    override val id = -1
    override val experience = 0.0
    override fun startCast(figure: Figure, context: SpellContext) {
        super.startCast(figure, context)
        if(figure is Player) {
            figure.incrementDfsCharges(-20)
            BonusManager.update(figure)//TODO Is this needed?
            Achievements.finishAchievement(figure, Achievements.AchievementData.ATTACK_USING_A_DFS)
        }
    }
}