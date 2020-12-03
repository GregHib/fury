package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.container.impl.equip.Slot
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.CombatIcon
import com.fury.game.entity.character.combat.Hit
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.entity.character.player.link.transportation.TeleportHandler
import com.fury.game.entity.character.player.link.transportation.TeleportType
import com.fury.core.model.item.Item
import com.fury.game.entity.item.content.ItemDegrading
import com.fury.game.world.update.flag.block.HitMask
import com.fury.util.Utils

class HealthHandler(private val figure: Figure) {

    var hitpoints = 100
    get() {
        return if(figure is Player)
            figure.skills.getLevel(Skill.CONSTITUTION)
        else
            field
    }
    set(value) {
        if(figure is QueenBlackDragon && figure.attacker != null && figure.lastHitpoints != value) {//TODO needs to be handled better
            //attacker.getPacketSender().sendCSVarInteger(1923, getMaxConstitution() - hitpoints);
            figure.lastHitpoints = value
        }

        if(figure is Player)
            figure.skills.setLevel(Skill.CONSTITUTION, value)
        else
            field = value
    }

    @JvmOverloads fun heal(amount: Int, extra: Int = 0, delay: Int = 0, displayMark: Boolean = false) {
        if (figure.isDead)
            return
        val aboveMaxHP = hitpoints + amount >= figure.maxConstitution + extra
        val hp = if (aboveMaxHP) figure.maxConstitution + extra else hitpoints + amount
        if (hitpoints > hp)
            return
        if (displayMark) {
            val damage = hp - hitpoints
            if (damage > 0) {
                figure.combat.applyHit(Hit(figure, damage, HitMask.PURPLE, CombatIcon.NONE))
                return
            }
        }
        hitpoints = hp
    }

    fun removeConstitution(hit: Hit) {
        if (figure.isDead || hit.combatIcon == CombatIcon.BLUE_SHIELD)
            return

        if (hit.hitMask == HitMask.PURPLE) {
            heal(hit.damage)
            return
        }

        if (hit.damage > hitpoints)
            hit.damage = hitpoints

        val source = hit.source

        figure.combat.hits.addReceivedDamage(source, hit.damage)
        hitpoints -= hit.damage
        if (source != null && source is Player)
            source.controllerManager.processActualHit(hit, figure)

        if (hitpoints <= 0)
            figure.sendDeath(source)
        else if (figure is Player) {
            if (figure.equipment.get(Slot.RING).id == 2550 && hit.combatIcon != CombatIcon.NONE && hit.combatIcon != CombatIcon.BLOCK && hit.combatIcon != CombatIcon.DEFLECT) {
                if (source != null && source != figure) {
                    source.combat.applyHit(Hit(figure, (hit.damage * 0.1).toInt(), HitMask.RED, CombatIcon.DEFLECT))
                    ItemDegrading.handleItemDegrading(figure, ItemDegrading.DegradingItem.RING_OF_RECOIL)
                }
            }
            if (figure.prayer.hasPrayersOn()) {
                if (hitpoints < figure.maxConstitution * 0.1 && figure.prayer.usingPrayer(0, Prayer.REDEMPTION)) {
                    figure.graphic(436)
                    // heal((figure.skills.getLevel(Skill.PRAYER) * 2.5).toInt()) // TODO fix bug
                    figure.skills.setLevel(Skill.PRAYER, 0)
                } else if (figure.equipment.get(Slot.AMULET).id != 11090 && figure.equipment.get(Slot.RING).id == 11090 && hitpoints <= figure.maxConstitution * 0.1) {
                    TeleportHandler.teleportPlayer(figure, GameSettings.DEFAULT_POSITION, TeleportType.RING_TELE)
                    figure.equipment.delete(Item(11090, 1))
                    figure.message("Your ring of life saves you, but is destroyed in the process.")
                }
            }
            if (figure.equipment.get(Slot.AMULET).id == 11090 && hitpoints <= figure.maxConstitution * 0.2) {// priority over ring of life
                heal((figure.maxConstitution * 0.3).toInt())
                figure.equipment.delete(Item(11090, 1))
                figure.message("Your phoenix necklace heals you, but is destroyed in the process.")
            }
        }
    }

    fun restoreHitPoints(): Boolean {
        val maxHp = figure.maxConstitution
        if (hitpoints > maxHp) {
            if (figure is Player) {
                if (figure.prayer.usingPrayer(1, Prayer.BERSERKER) && Utils.getRandom(100) <= 15)
                    return false
            }
            hitpoints -= 1
            return true
        } else if (hitpoints < maxHp) {
            hitpoints += 1
            if (figure is Player) {
                if (figure.prayer.usingPrayer(0, Prayer.RAPID_HEAL) && hitpoints < maxHp)
                    hitpoints += 1
                else if (figure.prayer.usingPrayer(0, Prayer.RAPID_RENEWAL) && hitpoints < maxHp)
                    hitpoints = if (hitpoints + 4 > maxHp) maxHp else hitpoints + 4

                if (figure.equipment.get(Slot.HANDS).id == 11133 && figure.actionManager.hasAction() && figure.actionManager.action is PlayerCombatAction && figure.combat.attackedBy == null && figure.combat.attackedByDelay < Utils.currentTimeMillis() && hitpoints < maxHp)
                    hitpoints += 1

                if (figure.settings.isListening)
                    hitpoints += 1
                else if (figure.inDream && hitpoints < maxHp)
                    hitpoints = if (hitpoints + 4 > maxHp) maxHp else hitpoints + 4
            }
            return true
        }
        return false
    }
}