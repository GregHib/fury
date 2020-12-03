package com.fury.game.content.misc.objects

import com.fury.game.content.global.ListWidget
import com.fury.game.content.global.config.ConfigConstants
import com.fury.game.content.misc.items.StrangeRocks
import com.fury.game.content.skill.Skill
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.player.content.PlayerPanel
import com.fury.core.model.item.Item
import com.fury.game.node.entity.actor.figure.player.Points
import com.fury.game.system.files.world.increment.timer.impl.WeeklyStatue
import com.fury.util.Misc

class StatueHandler(val player: Player) {

    private val skills = mutableListOf<Skill>()

    fun getSkills(): Array<Skill> {
        return skills.toTypedArray()
    }

    fun setSkills(skills: Array<Skill>) {
        this.skills.addAll(skills)

        if(WeeklyStatue.get().get(player.username) == 0 && skills.isNotEmpty())
            clearSkills()
    }

    fun clearSkills() {
        skills.clear()
    }

    fun click() {
        if(WeeklyStatue.get().get(player.username) == 15) {
            player.message("Your statue is complete.")
            return
        }

        var rewarded = false

        for(rock in StrangeRocks.values()) {
            if(skills.contains(rock.skill))
                continue

            if(player.inventory.contains(Item(rock.first), Item(rock.second))) {
                player.inventory.delete(Item(rock.first), Item(rock.second))
                skills.add(rock.skill)
                val level = player.skills.getMaxLevel(rock.skill)
                val calc = level * level - ((level * 2.0) + 100.0)
                player.skills.addExperience(rock.skill, calc/2)
                player.points.add(Points.STRANGE_SKILL, level + Misc.random(1, 40))
                WeeklyStatue.get().record(player.username)
                player.config.send(ConfigConstants.DAHMAROC_STATUE, WeeklyStatue.get().get(player.username), true)
                rewarded = true
            }
        }

        PlayerPanel.refreshPanel(player)

        if(rewarded)
            player.message("You have been rewarded experience & skill points.")
        else
            player.message("You don't have any pairs of strange skilling rocks in your inventory.")
    }

    fun check() {
        if(WeeklyStatue.get().get(player.username) == 15)
            player.dialogueFactory.sendStatement("Your statue is complete.")
        else {
            val list = mutableListOf<String>()
            StrangeRocks.values().forEach { rock ->
                if(!skills.contains(rock.skill)) {
                    list.add(rock.skill.formatName)
                }
            }
            ListWidget.display(player, "Missing Strange Rocks", "${WeeklyStatue.get().get(player.username)} rocks found", list.toTypedArray())
        }
    }

    /*
    Dwarven army axe
    2789 - mining
    2794 - woodcutting
    2831 - mining
    2833 - woodcutting
     */
}