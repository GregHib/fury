package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class Dreadnaut extends DungeonBoss {

	private List<GassPuddle> puddles;

	private int ticks;
	private boolean reduceMagicLevel;

	public Dreadnaut(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		//setForceFollowClose(true);
		//setRun(true);
		//setLureDelay(6000);//6 seconds
		puddles = new CopyOnWriteArrayList<>();
	}

	@Override
	public void processNpc() {
		if (puddles == null) //still loading
			return;
		super.processNpc();
		if (!reduceMagicLevel) {
			if (getCombat().isInCombat()) {
				for (Figure t : getPossibleTargets(false, true)) {
					if (!t.isWithinDistance(this, 1)) {
						ticks++;
						break;
					}
				}
			}
			if (ticks == 25) {
				reduceMagicLevel = true;
				forceChat("You cannot run from me forever!");
			}
		}

		for (GassPuddle puddle : puddles) {
			puddle.cycles++;
			if (puddle.canDestroyPuddle()) {
				puddles.remove(puddle);
				continue;
			} else if (puddle.cycles % 2 != 0)
				continue;
			if (puddle.cycles % 2 == 0)
				puddle.refreshGraphics();
			List<Figure> targets = getPossibleTargets(false, true);
			for (Figure t : targets) {
				if (!t.sameAs(puddle.tile))
					continue;
				if(t.isNpc())
					t.getCombat().applyHit(new Hit(Misc.random((int) (((Mob) t).getCombatDefinition().getHitpoints() * 0.25)) + 1, HitMask.NONE, CombatIcon.MELEE));
			}
		}
	}

	public double getMeleePrayerMultiplier() {
		return 0.60;
	}

	public boolean canReduceMagicLevel() {
		return reduceMagicLevel;
	}

	public void setReduceMagicLevel(boolean reduceMagicLevel) {
		this.reduceMagicLevel = reduceMagicLevel;
	}

	public void addSpot(Position tile) {
		GassPuddle puddle = new GassPuddle(this, tile);
		puddle.refreshGraphics();
		puddles.add(puddle);
	}

	private static class GassPuddle {
		final Dreadnaut boss;
		final Position tile;
		int cycles;

		public GassPuddle(Dreadnaut boss, Position tile) {
			this.tile = tile;
			this.boss = boss;
		}

		public void refreshGraphics() {
			for(Player p : GameWorld.getPlayers()) {
				if(p == null || p.getFinished())
					continue;
				if(this.tile.isViewableFrom(p)) {
					p.getPacketSender().sendGraphic(new Graphic(2859, 0, GraphicHeight.HIGH), tile);
				}
			}
		}

		public boolean canDestroyPuddle() {
			return cycles == 50;
		}
	}
}
