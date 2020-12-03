package com.fury.game.node.entity.actor.figure.mob

import com.fury.cache.Revision
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.game.content.controller.impl.ZarosGodwars
import com.fury.game.content.skill.member.hunter.HunterData
import com.fury.game.content.skill.member.hunter.HunterTrapMob
import com.fury.game.npc.bosses.KingBlackDragon
import com.fury.game.npc.bosses.TormentedDemon
import com.fury.game.npc.bosses.bork.DagonHaiElite
import com.fury.game.npc.bosses.corp.CorporealBeast
import com.fury.game.npc.bosses.glacors.Glacor
import com.fury.game.npc.bosses.godwars.GodWarMinion
import com.fury.game.npc.bosses.godwars.armadyl.ArmadylMinion
import com.fury.game.npc.bosses.godwars.armadyl.KreeArra
import com.fury.game.npc.bosses.godwars.bandos.BandosMinion
import com.fury.game.npc.bosses.godwars.bandos.GeneralGraardor
import com.fury.game.npc.bosses.godwars.saradomin.CommanderZilyana
import com.fury.game.npc.bosses.godwars.saradomin.SaradominMinion
import com.fury.game.npc.bosses.godwars.zamorak.KrilTstsaroth
import com.fury.game.npc.bosses.godwars.zamorak.ZamorakMinion
import com.fury.game.npc.bosses.godwars.zaros.ZarosMinion
import com.fury.game.npc.bosses.kq.KalphiteQueen
import com.fury.game.npc.bosses.nex.Nex
import com.fury.game.npc.bosses.nex.NexMinion
import com.fury.game.npc.bosses.rfd.GelatinnothMother
import com.fury.game.npc.minigames.AnimatedArmour
import com.fury.game.npc.minigames.Cyclopse
import com.fury.game.npc.misc.EvilChicken
import com.fury.game.npc.misc.battlefield.BlackGuard
import com.fury.game.npc.misc.battlefield.RedAxe
import com.fury.game.npc.slayer.*
import com.fury.game.npc.slayer.polypore.PolyporeCreature
import com.fury.game.world.map.Position

object MobBuilder {
    fun spawnNpc(id: Int, revision: Revision, tile: Position, spawned: Boolean): Mob {
        val n: Mob
        if (id == 5079) {
            n = HunterTrapMob(HunterData.GREY_CHINCHOMPA, id, tile, spawned)
        } else if (id == 5080) {
            n = HunterTrapMob(HunterData.RED_CHINCHOMPA, id, tile, spawned)
        } else if (id == 5081) {
            n = HunterTrapMob(HunterData.FERRET, id, tile, spawned)
        } else if (id == 6916) {
            n = HunterTrapMob(HunterData.GECKO, id, tile, spawned)
        } else if (id == 7272) {
            n = HunterTrapMob(HunterData.MONKEY, id, tile, spawned)
        } else if (id == 7272) {
            n = HunterTrapMob(HunterData.RACCOON, id, tile, spawned)
        } else if (id == 5073) {
            n = HunterTrapMob(HunterData.CRIMSON_SWIFT, id, tile, spawned)
        } else if (id == 5075) {
            n = HunterTrapMob(HunterData.GOLDEN_WARBLER, id, tile, spawned)
        } else if (id == 5076) {
            n = HunterTrapMob(HunterData.COPPER_LONGTAIL, id, tile, spawned)
        } else if (id == 5074) {
            n = HunterTrapMob(HunterData.CERULEAN_TWITCH, id, tile, spawned)
        } else if (id == 5072) {
            n = HunterTrapMob(HunterData.TROPICAL_WAGTAIL, id, tile, spawned)
        } else if (id == 7031) {
            n = HunterTrapMob(HunterData.WIMPY_BIRD, id, tile, spawned)
        } else if (id == 5088) {
            n = HunterTrapMob(HunterData.BARB_TAILED_KEBBIT, id, tile, spawned)
        } else if (id == 5117) {
            n = HunterTrapMob(HunterData.SWAMP_LIZARD, id, tile, spawned)
        } else if (id == 5114) {
            n = HunterTrapMob(HunterData.ORANGE_SALAMANDER, id, tile, spawned)
        } else if (id == 5115) {
            n = HunterTrapMob(HunterData.RED_SALAMANDER, id, tile, spawned)
        } else if (id == 5116) {
            n = HunterTrapMob(HunterData.BLACK_SALAMANDER, id, tile, spawned)
        } else if (id == 7012) {
            n = HunterTrapMob(HunterData.PAWYA, id, tile, spawned)
        } else if (id == 7010) {
            n = HunterTrapMob(HunterData.GRENWALL, id, tile, spawned)
        } else if (id == 13820 || id == 13821 || id == 13822) {
            n = Jadinko(id, tile, spawned)
        } else if (id == 3375) {
            n = EvilChicken(id, tile, spawned)
        } else if (id == 1880) {
            n = Bandits(id, tile, spawned)
        } else if (id == 3497) {
            n = GelatinnothMother(id, tile, spawned)
        } else if (id == 8349 || id == 8450 || id == 8451) {
            n = TormentedDemon(id, tile, spawned)
        } else if (id in 13465..13481) {
            n = Revenant(id, tile, spawned)
        } else if (id == 2058) {
            n = HoleInTheWall(id, tile, spawned)
        } else if (id == 1608 || id == 1609) {
            n = Kurask(id, tile, spawned)
        } else if (id == 1615) {
            n = AbyssalDemon(id, tile, spawned)
        } else if (id == 1613 || id == 10702) {
            n = Nechryael(id, tile, spawned)
        } else if (id == 3153) {
            n = HarpieBug(id, tile, spawned)
        } else if (id in 9462..9467) {
            n = Strykewyrm(id, tile)
        } else if (id in 8832..8834) {
            n = LivingRock(id, tile, spawned)
        } else if (id in 14688..14701) {
            n = PolyporeCreature(id, tile, revision, spawned)
        } else if (id == 50) {
            n = KingBlackDragon(id, tile, spawned)
        } else if (id == 8772 || id == 8781 || id == 8783) {
            n = BlackGuard(id, tile, spawned)
        } else if (id == 8771 || id == 8777 || id == 8778) {
            n = RedAxe(id, tile, spawned)
        } else if (id == 1158 || id == 1160) {
            n = KalphiteQueen(id, tile, spawned)
        } else if (id in 4278..4284) {
            n = AnimatedArmour(id, tile, spawned)
        } else if (id == 6260) {
            n = GeneralGraardor(id, tile, spawned)
        } else if (id == 6261 || id == 6263 || id == 6265) {
            n = GodWarMinion(id, tile, spawned)
            GeneralGraardor.minions[(id - 6261) / 2] = n
        } else if (id == 6222) {
            n = KreeArra(id, tile, spawned)
        } else if (id == 6223 || id == 6225 || id == 6227) {
            KreeArra.minions[(id - 6223) / 2] = GodWarMinion(id, tile, spawned)
            n = KreeArra.minions[(id - 6223) / 2]
        } else if (id == 6203) {
            n = KrilTstsaroth(id, tile, spawned)
        } else if (id == 6204 || id == 6206 || id == 6208) {
            KrilTstsaroth.minions[(id - 6204) / 2] = GodWarMinion(id, tile, spawned)
            n = KrilTstsaroth.minions[(id - 6204) / 2]
        } else if (id == 6247) {
            n = CommanderZilyana(id, tile, spawned)
        } else if (id == 6248 || id == 6250 || id == 6252) {
            CommanderZilyana.minions[(id - 6248) / 2] = GodWarMinion(id, tile, spawned)
            n = CommanderZilyana.minions[(id - 6248) / 2]
        } else if (id in 6210..6221) {
            n = ZamorakMinion(id, tile, spawned)
        } else if (id in 6254..6259 || id == 3406) {
            n = SaradominMinion(id, tile, spawned)
        } else if (id in 6268..6283) {
            n = BandosMinion(id, tile, spawned)
        } else if (id in 6228..6246) {
            n = ArmadylMinion(id, tile, spawned)
        } else if (id == 4291) {
            n = Cyclopse(id, tile, spawned)
        } else if (id == 7137) {
            n = DagonHaiElite(id, tile, spawned)
        } else if (id == 8133) {
            n = CorporealBeast(id, tile, spawned)
        } else if (id == 13456 || id == 13457 || id == 13458 || id == 13459) {
            n = ZarosMinion(id, tile)
        } else if (id == 13447) {
            n = Nex(id, tile, spawned)
        } else if (id == 13451 || id == 13452 || id == 13453 || id == 13454) {
            n = NexMinion(id, tile)
        } else if (id == 14301) {
            n = Glacor(id, tile)
        } else if (id == 1631 || id == 1632) {
            n = ConditionalDeath(4161, "The rockslug shrivels and dies.", true, id, tile, spawned)
        } else if (id == 1610) {
            n = ConditionalDeath(4162, "The gargoyle breaks into pieces as you slam the hammer onto its head.", true, id, tile, spawned)
        } else if (id == 14849) {//Gelatinous abomination
            n = ConditionalDeath(23035, null, false, id, tile, spawned)
        } else if (id == 1627 || id == 1628 || id == 1629 || id == 1630) {//Turoth
            n = ConditionalDeath(intArrayOf(13290, 4158), null, false, id, tile, spawned)
        } else if (id in 2803..2808) {//Lizards
            n = ConditionalDeath(6696, null, true, id, tile, spawned)
        } else {
            n = Mob(id, tile, revision, spawned)
            if (id == 1131 || id == 1132 || id == 1133 || id == 1134)
                n.forceAggressive = true
        }
        return n
    }
}