package com.fury.game.content.misc.items

import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.skill.Skill
import com.fury.core.model.item.Item
import com.fury.util.Misc

enum class StrangeRocks(val skill: Skill, val first: Int, val second: Int) {
    AGILITY(Skill.AGILITY, 15522, 15523),
    HERBLORE(Skill.HERBLORE, 15524, 15525),
    FARMING(Skill.FARMING, 15526, 15527),
    CRAFTING(Skill.CRAFTING, 15528, 15529),
    RUNECRAFTING(Skill.RUNECRAFTING, 15530, 15531),
    MINING(Skill.MINING, 15532, 15533),
    SMITHING(Skill.SMITHING, 15534, 15535),
    FISHING(Skill.FISHING, 15536, 15537),
    COOKING(Skill.COOKING, 15538, 15539),
    FIREMAKING(Skill.FIREMAKING, 15540, 15541),
    WOODCUTTING(Skill.WOODCUTTING, 15542, 15543),
    FLETCHING(Skill.FLETCHING, 15544, 15545),
    THIEVING(Skill.THIEVING, 15546, 15547),
    CONSTRUCTION(Skill.CONSTRUCTION, 15548, 15549),
    HUNTER(Skill.HUNTER, 15550, 15551);

    companion object {
        @JvmStatic
        fun handleStrangeRocks(player: Player, skill: Skill) {
            StrangeRocks.values().forEach { rock ->
                if(rock.skill == skill) {
                    if(!player.hasItem(rock.first) || !player.hasItem(rock.second)) {
                        if(Misc.random(100) == 0) {
                            player.message("You find a strange rock.")
                            player.inventory.addSafe(Item(if(player.hasItem(rock.first)) rock.second else rock.first))
                            return@forEach
                        }
                    }
                }
            }
        }
    }
}