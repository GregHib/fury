package com.fury.game.content.tasks

import com.fury.core.task.Task
import com.fury.game.content.skill.Skill
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.world.GameWorld
import com.fury.util.Utils

class SkillRestoreTask : Task(false, 50) {//30 seconds

    override fun run() {
        GameWorld.players.forEach { player ->
            var times = if (player.prayer.usingPrayer(0, Prayer.RAPID_RESTORE)) 2 else 1
            if (player.settings.isResting)
                times += 1
            val berserker = player.prayer.usingPrayer(1, Prayer.BERSERKER)
            for (skill in Skill.values()) {
                if (skill == Skill.SUMMONING || skill == Skill.PRAYER || skill == Skill.CONSTITUTION)
                    continue
                for (time in 0 until times) {
                    val currentLevel = player.skills.getLevel(skill)
                    val normalLevel = player.skills.getMaxLevel(skill)
                    if (currentLevel > normalLevel) {
                        if (skill == Skill.ATTACK || skill == Skill.STRENGTH || skill == Skill.DEFENCE || skill == Skill.RANGED || skill == Skill.MAGIC) {
                            if (berserker && Utils.getRandom(100) <= 15)
                                continue
                        }
                        player.skills.drain(skill, 1)
                    } else if (currentLevel < normalLevel)
                        player.skills.restore(skill, 1)
                    else
                        break
                }
            }
        }
    }

}