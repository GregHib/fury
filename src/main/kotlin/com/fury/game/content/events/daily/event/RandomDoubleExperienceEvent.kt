package com.fury.game.content.events.daily.event

import com.fury.game.content.events.daily.DailyEvent
import com.fury.game.content.events.daily.DailyEventType
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.Skills
import com.fury.game.entity.character.combat.CombatConstants
import com.fury.game.world.GameWorld
import com.fury.util.Misc

class RandomDoubleExperienceEvent : DailyEvent {
    private val skill: Skill = getSkill()

    private fun getSkill(): Skill {
        var skill = CombatConstants.NON_COMBAT_SKILLS[Misc.random(CombatConstants.NON_COMBAT_SKILLS.size - 1)]
        while(skill == Skill.CONSTRUCTION)
            skill = CombatConstants.NON_COMBAT_SKILLS[Misc.random(CombatConstants.NON_COMBAT_SKILLS.size - 1)]
        return skill
    }

    override fun getEventType(): DailyEventType {
        return DailyEventType.RANDOM_DOUBLE_EXPERIENCE
    }

    override fun getExperienceAdded(skill: Skill, amount: Double): Double {ad
        return if(skill == this.skill) amount * 2.0 else super.getExperienceAdded(skill, amount)
    }

    override fun onStart(hours: Int) {
        GameWorld.sendBroadcast("A double ${skill.formatName} experience event has started and will end in $hours ${if(hours == 1) "hour" else "hours"}!", Skills.getColour(skill))
    }

    override fun onFinish() {
        GameWorld.sendBroadcast("Double ${skill.formatName} experience event has ended!", Skills.getColour(skill))
    }

}