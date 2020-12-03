package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.task.TickableTask;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.skills.DungeoneeringTraps;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DungeonHunterMob extends DungeonMob {

    public DungeonHunterMob(int id, Position tile, DungeonManager manager, double multiplier) {
        super(id, tile, manager, multiplier, id > Loader.getTotalNpcs(Revision.RS2) ? Revision.PRE_RS3 : Revision.RS2); // remove revision argument once converted to Kotlin
    }

	@Override
	public void processNpc() {
		if (isCantInteract() || getId() >= 11096 || getManager() == null)
			return;
		super.processNpc();
		List<MastyxTrap> traps = getManager().getMastyxTraps();
		if (traps.isEmpty())
			return;
		final int tier = DungeoneeringTraps.getNPCTier(getId());
		for (final MastyxTrap trap : traps) {
			if (!isWithinDistance(trap, 3) || Misc.getRandom(3) != 0)
				continue;
			trap.setCantInteract(true);
			setCantInteract(true);
			getDirection().face(trap);
			getMovement().addWalkSteps(trap.getX() + 1, trap.getY() + 1);

            final int trap_tier = trap.getTier();
            double successRatio = getSuccessRatio(tier, trap_tier);
            final boolean failed = successRatio < Math.random();

            setCantInteract(true);
            if (failed) {
				GameWorld.schedule(new TickableTask(true, 1) {
					@Override
					public void tick() {
						if (getTick() == 4) {
							animate(13264);
						} else if (getTick() == 7) {
							trap.setTransformation(1957);
							trap.perform(new Graphic(2561 + trap_tier));
						} else if (getTick() == 15) {
							getManager().removeMastyxTrap(trap);
							setCantInteract(false);
							this.stop();
						}
					}
				});
			} else {
            	GameWorld.schedule(new TickableTask(true, 1) {
					@Override
					public void tick() {
						if (getTick() == 8) {
							trap.setTransformation(1957);
							trap.perform(new Graphic(2551 + trap_tier));
						} else if (getTick() == 12) {
							animate(13260);
						} else if (getTick() == 17) {
							setTransformation(getId() + 10);
						} else if (getTick() == 19) {
							setCantInteract(false);
							getManager().removeMastyxTrap(trap);
							this.stop();
						}
					}
				});
			}
		}
	}

    @Override
    public List<Drop> addDrops(Player kills) {
        List<Drop> drops = new ArrayList<>();
        drops.add(new Drop(532, 100.0, 1, 1, false));
        Drop[] originalDrops = MobDrops.getDrops(getId());
        List<Drop> dps = new ArrayList<>();
        if (originalDrops != null)
            for (Drop d : originalDrops)
                if (d.getRate() >= 80)
                    dps.add(d);

        Drop[] dropsA = dps.toArray(new Drop[dps.size()]);

        if (dropsA.length <= 0)
            return drops;

        Drop drop = RandomUtils.random(dropsA); //to make 100% chance

        if (drop == null) //shouldn't
            return drops;

        drops.add(drop);
        return drops;
    }

    private static double getSuccessRatio(int tier, int trapTier) {
        double successRatio = 0.0;
        int tierProduct = trapTier - tier;
        if (tierProduct == 0)
            successRatio = 0.5;
        else if (tierProduct > 0)
            successRatio = 0.5 + (tierProduct / 10.0);

        if (successRatio > 0.9)
            successRatio = 0.9;
        return successRatio;
    }
}
