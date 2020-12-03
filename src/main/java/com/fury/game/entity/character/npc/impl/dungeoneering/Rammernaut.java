package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.core.model.node.entity.Entity;
import com.fury.core.task.TickableTask;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

@SuppressWarnings("serial")
public class Rammernaut extends DungeonBoss {

	private Player chargeTarget;
	private int count;
	private boolean requestSpecNormalAttack;

	public Rammernaut(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
		//setForceFollowClose(true);
	}

	public void fail() {
		animate(13707);
		forceChat("Oooof!!");
		count = -14;
	}

	public void success() {
		getMovement().reset();
		animate(13698);
		applyStunHit(chargeTarget, (int) (chargeTarget.getSkills().getMaxLevel(Skill.CONSTITUTION) * 0.6));
		requestSpecNormalAttack = true;
		count = -12;
	}

	public void applyStunHit(final Entity entity, int maxHit) {
		if(entity.isNpc()) {
			((Mob) entity).getCombat().applyHit(new Hit(Misc.random(maxHit) + 1, HitMask.RED, CombatIcon.NONE));
		} else if(entity.isPlayer()) {
			((Player) entity).getCombat().applyHit(new Hit(Misc.random(maxHit) + 1, HitMask.RED, CombatIcon.NONE));
		}
		//figure.setStunDelay(2);
		if (entity.isPlayer()) {
			Player player = (Player) entity;
			player.stopAll();
			player.message("You've been stunned.");
			player.setStunned(true);
			//player.setStunDelay(2);
			if (player.getPrayer().hasPrayersOn()) {
				player.message("Your prayers have been disabled.");
				player.getPrayer().closeAllPrayers();
				player.getEffects().startEffect(new Effect(Effects.PROTECTION_DISABLED, 8));
			}
			final Mob mob = this;
			GameWorld.schedule(new TickableTask(true) {
				private Position tile;
				@Override
				public void tick() {
					if (getTick() == 0) {
						byte[] dirs = Misc.getDirection(getDirection().getDirection().toInteger());
						for (int distance = 6; distance >= 0; distance--) {
							tile = new Position(entity.getX() + (dirs[0] * distance), entity.getY() + (dirs[1] * distance), entity.getZ());
							if ((World.getMask(tile.getX(), tile.getY(), tile.getZ()) & 0x1280120) == 0 && getManager().isAtBossRoom(tile))
								break;
							else if (distance == 0)
								tile = entity.copyPosition();
						}
						((Player) entity).getDirection().face(mob);
						((Player) entity).animate(10070);
						((Player) entity).setForceMovement(new ForceMovement(entity, 0, tile, 2, ((Player) entity).getDirection().getDirection().toInteger()));
					} else if (getTick() == 1) {
						((Player) entity).getDirection().face(tile);
						stop();
					}
				}
			});
		}
	}

	@Override
	public void processNpc() {
		if (isDead())
			return;
		if (chargeTarget != null) {
			getDirection().face(chargeTarget);
			if (count == 0) {
				forceChat("CHAAAAAARGE!");
				setRun(true);
			} else if (count == -10) {
				setRun(false);
				getMovement().reset();
				calcFollow(chargeTarget, true);
			} else if (count == -8) {
				setChargeTarget(null);
			} else if (count > 2) {
				getMovement().reset();
				/*
				 * skip first step else it's stuck ofc
				 */
				calcFollow(chargeTarget, true);

				if (count != 3 && (World.getMask(getX(), getY(), getZ()) & 0x1280120) != 0)
					fail();
				else if (Misc.isOnRange(getX(), getY(), getSizeX(), getSizeY(), chargeTarget.getX(), chargeTarget.getY(), chargeTarget.getSizeX(), chargeTarget.getSizeY(), 0))
					success();
				else if (!getMovement().hasWalkSteps())
					fail();
			}
			count++;
			return;
		}
		super.processNpc();
	}

	public void setChargeTarget(Player target) {
		this.chargeTarget = target;
		getMobCombat().removeTarget();
		count = 0;
	}

	public boolean isRequestSpecNormalAttack() {
		return requestSpecNormalAttack;
	}

	public void setRequestSpecNormalAttack(boolean requestSpecNormalAttack) {
		this.requestSpecNormalAttack = requestSpecNormalAttack;
	}
}
