package com.fury.game.entity.combat.scripts.glacor;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.bosses.glacors.Glacor;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;

public class GlacorCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 14301 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        Glacor glacor = (Glacor) mob;
        if (Misc.random(4) == 0)
            glacor.setRangeAttack(!glacor.isRangeAttack());
        if (target.isPlayer()) {
            switch (Misc.random(5)) {
                case 0:
                case 1:
                case 2:
                    sendDistancedAttack(glacor, target);
                    break;
                case 3://MELEE
                    if (Misc.isOnRange(mob.getX(), mob.getY(), mob.getSizeX(), mob.getSizeY(), target.getX(), target.getY(), target.getSizeX(), target.getSizeY(), 0)) {
                        mob.animate(9955);
                        delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
                    }
                    else
                        sendDistancedAttack(glacor, target);
                    break;
                case 4://SPECIAL
                    final Position tile = target;
                    mob.animate(9955);
                    Projectile projectile = new Projectile(mob, tile, 2314, Revision.PRE_RS3, 50, 0, 20, 1, 0, 0);
                    ProjectileManager.send(projectile);
                    glacor.setRangeAttack(true);
                    GameWorld.schedule(projectile.getTickDelay(), () -> {
                        for (Figure e : mob.getPossibleTargets()) {
                            if (e.isPlayer()) {
                                Player player = (Player) e;
                                if (player.sameAs(tile))
                                    player.getCombat().applyHit(new Hit(mob, player.getHealth().getHitpoints() / 2, HitMask.RED, CombatIcon.RANGED));
                                player.getPacketSender().sendGraphic(new Graphic(2315, Revision.PRE_RS3), tile);
                            }
                        }
                    });
                    break;
            }
        }
        return mob.getCombat().getAttackSpeed();
    }

    private void sendDistancedAttack(Glacor npc, final Figure target) {
        boolean isRangedAttack = npc.isRangeAttack();
        if (isRangedAttack) {

            Projectile projectile = new Projectile(npc, target, 962, Revision.PRE_RS3, 50, 30, 30, 1, 0, 0);
            ProjectileManager.send(projectile);
            delayHit(npc, projectile.getTickDelay(), target, getRangeHit(npc, getRandomMaxHit(npc, MobCombatDefinitions.RANGE, target)));
        }
        else {
            Projectile projectile = new Projectile(npc, target, 634, 50, 30, 30, 1, 5, 0);
            ProjectileManager.send(projectile);
            delayHit(npc, projectile.getTickDelay(), target, getMagicHit(npc, getRandomMaxHit(npc, MobCombatDefinitions.MAGE, target)));
            if (Misc.random(5) == 0) {
                GameWorld.schedule(projectile.getTickDelay(), () -> {
                    target.perform(new Graphic(369));
                    target.getCombat().addFreezeDelay(9000);
                });
            }
        }
        npc.perform(new Animation(isRangedAttack ? 9968 : 9967));
    }

}
