package com.fury.game.content.actions.item

import com.fury.core.action.ItemActionEvent
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.misc.items.random.impl.BasicItemGenerator
import com.fury.game.content.misc.items.random.impl.imps.*
import com.fury.core.model.item.Item
import com.fury.game.system.communication.commands.impl.regular.DropsCommand

class ImplingJarsPlugin : ItemActionEvent() {

    enum class ImplingJar(val id: Int, val gen: BasicItemGenerator) {
        BABY_IMPLING(11238, BabyImplingJarGen),
        YOUNG_IMPLING(11240, YoungImplingJarGen),
        GOURMET_IMPLING(11242, GourmetImplingJarGen),
        EARTH_IMPLING(11244, EarthImplingJarGen),
        ESSENCE_IMPLING(11246, EssenceImplingJarGen),
        ECLECTIC_IMPLING(11248, EclecticImplingJarGen),
        NATURE_IMPLING(11250, NatureImplingJarGen),
        MAGPIE_IMPLING(11252, MagpieImplingJarGen),
        NINJA_IMPLING(11254, NinjaImplingJarGen),
        DRAGON_IMPLING(11256, DragonImplingJarGen),
        KINGLY_IMPLING(15517, KinglyImplingJarGen);

        val simpleName = name.toLowerCase().replace("_", " ")

        companion object {
            fun getJar(id: Int): ImplingJar? {
                return values().firstOrNull { it.id == id }
            }

            fun getJar(name: String): ImplingJar? {
                return values().firstOrNull { it.simpleName.equals(name, true) }
            }
        }
    }

    override fun thirdOption(player: Player, item: Item, slot: Int): Boolean {
        val jar = ImplingJar.getJar(item.id) ?: return false
        jar.gen.reward(player, item)
        return true
    }

    companion object {
        @JvmStatic
        fun handleDropsCommand(player: Player, query: String): Boolean {
            val jar = ImplingJar.getJar(query) ?: return false

            DropsCommand.showDrops(player, jar.gen, jar.simpleName, jar.id)
            return true
        }
    }
}