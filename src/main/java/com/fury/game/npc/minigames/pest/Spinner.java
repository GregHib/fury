package com.fury.game.npc.minigames.pest;

import com.fury.game.content.global.minigames.impl.PestControl;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;

public class Spinner extends PestMonsters {

    private byte healTicks;

    public Spinner(int id, Position tile, boolean spawned, int index, PestControl manager) {
        super(id, tile, spawned, index, manager);
    }

    @Override
    public void processNpc() {
        PestPortal portal = getManager().getPortals()[getPortalIndex()];
        if (portal.isDead()) {
            explode();
            return;
        }
        if (!portal.getMovement().isLocked()) {
            healTicks++;
            if (!isWithinDistance(portal, 1))
                this.getMovement().addWalkSteps(portal.getX(), portal.getY());
            else if (healTicks % 6 == 0)
                healPortal(portal);
        }
    }

    private void healPortal(final PestPortal portal) {
        getDirection().face(portal);
        GameWorld.schedule(1, () -> {
            animate(3911);
            perform(new Graphic(658, 0, 96 << 16));
            if (portal.getHealth().getHitpoints() != 0)
                portal.getHealth().heal((portal.getMaxConstitution() / portal.getHealth().getHitpoints()) * 45);
        });
    }

    private void explode() {
        final Mob mob = this;
        GameWorld.schedule(1, () -> {
            for (Player player : getManager().getPlayers()) {
                if (!isWithinDistance(player, 7))
                    continue;
                player.getEffects().makePoisoned(50);
                player.getCombat().applyHit(new Hit(mob, 50, HitMask.RED, CombatIcon.NONE));
                mob.reset();
                mob.deregister();
            }
        });
    }
}
