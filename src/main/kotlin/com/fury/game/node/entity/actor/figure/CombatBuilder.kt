package com.fury.game.node.entity.actor.figure

import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.entity.character.npc.impl.dungeoneering.*
import com.fury.game.entity.character.npc.impl.fightkiln.FightKilnMob
import com.fury.game.entity.character.npc.impl.fightkiln.HarAken
import com.fury.game.entity.character.npc.impl.fightkiln.HarAkenTentacle
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon
import com.fury.game.entity.character.npc.impl.queenblackdragon.TorturedSoul
import com.fury.game.node.entity.actor.figure.mob.MobCombat
import com.fury.game.node.entity.actor.figure.player.PlayerCombat
import com.fury.game.node.entity.mob.combat.*
import com.fury.game.npc.bosses.TormentedDemon
import com.fury.game.npc.bosses.bork.Bork
import com.fury.game.npc.bosses.bork.DagonHaiElite
import com.fury.game.npc.bosses.corp.CorporealBeast
import com.fury.game.npc.bosses.corp.DarkEnergyCore
import com.fury.game.npc.bosses.glacors.Glacor
import com.fury.game.npc.bosses.glacors.Glacyte
import com.fury.game.npc.bosses.godwars.armadyl.ArmadylMinion
import com.fury.game.npc.bosses.godwars.bandos.BandosMinion
import com.fury.game.npc.bosses.godwars.saradomin.SaradominMinion
import com.fury.game.npc.bosses.godwars.zamorak.ZamorakMinion
import com.fury.game.npc.bosses.godwars.zaros.ZarosMinion
import com.fury.game.npc.bosses.kq.KalphiteQueen
import com.fury.game.npc.bosses.nex.Nex
import com.fury.game.npc.bosses.nex.NexMinion
import com.fury.game.npc.bosses.rfd.GelatinnothMother
import com.fury.game.npc.familiar.Familiar
import com.fury.game.npc.minigames.Cyclopse
import com.fury.game.npc.minigames.barrows.BarrowsBrother
import com.fury.game.npc.minigames.pest.PestMonsters
import com.fury.game.npc.minigames.pest.PestPortal
import com.fury.game.npc.minigames.pest.Splatter
import com.fury.game.npc.misc.EvilChicken
import com.fury.game.npc.slayer.*
import com.fury.game.npc.slayer.polypore.PolyporeCreature

class CombatBuilder(val figure: Figure) {
    fun build(): FigureCombat {
        return when(figure) {
            is Nechryael -> NechryaelCombat(figure)
            is LivingRock -> LivingRockCombat(figure)
            is Kurask -> KuraskCombat(figure)
            is Jadinko -> JadinkoCombat(figure)
            is HoleInTheWall -> HoleInTheWallCombat(figure)
            is HarpieBug -> HarpieBugCombat(figure)
            is ConditionalDeath -> ConditionalDeathCombat(figure)
            is PolyporeCreature -> PolyporeCreatureCombat(figure)
            is EvilChicken -> EvilChickenCombat(figure)
            is Splatter -> SplatterCombat(figure)
            is PestPortal -> PestPortalCombat(figure)
            is PestMonsters -> PestMonstersCombat(figure)
            is BarrowsBrother -> BarrowsBrotherCombat(figure)
            is Cyclopse -> CyclopseCombat(figure)
            is GelatinnothMother -> GelatinnothMotherCombat(figure)
            is Nex -> NexCombat(figure)
            is KalphiteQueen -> KalphiteQueenCombat(figure)
            is NexMinion -> NexMinionCombat(figure)
            is ZarosMinion -> ZarosMinionCombat(figure)
            is ZamorakMinion -> ZamorakMinionCombat(figure)
            is SaradominMinion -> SaradominMinionCombat(figure)
            is BandosMinion -> BandosMinionCombat(figure)
            is ArmadylMinion -> ArmadylMinionCombat(figure)
            is DarkEnergyCore -> DarkEnergyCoreCombat(figure)
            is CorporealBeast -> CorporealBeastCombat(figure)
            is DagonHaiElite -> DagonHaiEliteCombat(figure)
            is Bork -> BorkCombat(figure)
            is TormentedDemon -> TormentedDemonCombat(figure)
            is TorturedSoul -> TorturedSoulCombat(figure)
            is QueenBlackDragon -> QueenBlackDragonCombat(figure)
            is HarAkenTentacle -> HarAkenTentacleCombat(figure)
            is HarAken -> HarAkenCombat(figure)
            is FightKilnMob -> FightKilnMobCombat(figure)
            is Glacor -> GlacorCombat(figure)
            is Glacyte -> GlacyteCombat(figure)
            is Guardian -> GuardianCombat(figure)
            is DungeonSkeletonBoss -> DungeonSkeletonBossCombat(figure)
            is RuneboundBehemoth -> RuneboundBehemothCombat(figure)
            is DungeonBoss -> DungeonBossCombat(figure)
            is DungeonMob -> DungeonMobCombat(figure)
            is Familiar -> FamiliarCombat(figure)
            is Mob -> MobCombat(figure)
            is Player -> PlayerCombat(figure)
            else -> FigureCombat(figure)
        }
    }
}