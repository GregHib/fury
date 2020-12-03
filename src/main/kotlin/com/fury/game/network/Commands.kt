package com.fury.game.network

import com.fury.cache.Revision
import com.fury.cache.def.Loader
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.content.combat.Combat
import com.fury.game.content.combat.magic.Magic
import com.fury.game.content.dialogue.impl.minigames.fightkiln.FightKilnD
import com.fury.game.content.global.quests.impl.FirstAdventure
import com.fury.game.content.skill.Skill
import com.fury.game.entity.`object`.GameObject
import com.fury.util.Misc
import com.google.gson.GsonBuilder

class Commands {
    companion object {

        @JvmStatic
        fun handle(player: Player, command: Array<out String>, wholeCommand: String): Boolean {
            return when(command[0]) {
                "tut" -> {
                    player.questManager.start(FirstAdventure())
                    true
                }
                "kotlin" -> {
                    val id = 1767
                    val revision = Revision.RS2
                    val obj = GameObject(id, player, if (Loader.getObject(id, revision).modelTypes == null) 10 else Loader.getObject(id, revision).modelTypes[0], 0, revision)
                    player.packetSender.sendObject(obj)
                    println("${obj.sizeX}x${obj.sizeY}")
                    true
                }
                "kiln" -> {
                    player.dialogueManager.startDialogue(FightKilnD())
                    true
                }
                "sav" -> {
//                    println(getJsonText(player as Position, true))
                    true
                }
                "hit" -> {
                    val spell = if(player.previousCastSpell != null) player.previousCastSpell else if(player.castSpell != null) player.castSpell else player.autoCastSpell
                    player.message("Mage max hit: ${Magic.getMaxHit(player, null, spell)} spell:${Misc.formatName(spell?.name)}")
                    player.message("Mage hit chance: ${Combat.getHitChance(player, skill = Skill.MAGIC)}")
                    true
                }
                "w2" -> {
                    val start = player.copyPosition()
//                    player.movement.setRunningToggled(true)
//                    player.movement.walk(Position(player.x, 3498))
//                    player.movement.dijkstraPath(Position(player.x - 5, player.y))
                    /*GameWorld.schedule(object : Task(2) {
                        override fun run() {
                            player.movement.teleportTarget = start
                            stop()
                        }

                    })*/
                    true
                }
                else -> {
                    false
                }
            }
        }

        private fun getJsonText(`object`: Any, collapse: Boolean): String {
            val builder = GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
            val gson = builder.create()

            var toJson = gson.toJson(`object`)
            if (collapse) {
                toJson = toJson.replace("\n".toRegex(), "")
            }
            return toJson
        }

    }

}