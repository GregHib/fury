package com.fury.game.entity.combat.scripts.slayer;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.combat.CombatScript;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.slayer.polypore.PolyporeCreature;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class PolyporeCreatureCombat extends CombatScript {

    @Override
    public Object[] getKeys() {
        return new Object[] { 14688, 14689, 14690, 14691, 14692, 14693, 14694, 14695, 14696, 14697, 14698, 14699, 14700, 14701 };
    }

    @Override
    public int attack(final Mob mob, final Figure target) {
        MobCombatDefinitions def = mob.getCombatDefinition();
        final PolyporeCreature creature = (PolyporeCreature) mob;
        if (Utils.isOnRange(target, mob, 0) && creature.canInfect()) {
            int infectEmote = creature.getInfectEmote();
            if (infectEmote != -1) {
                mob.perform(new Animation(infectEmote, Revision.PRE_RS3));
                if (target.isPlayer()) {
                    Player player = (Player) target;
                    if (Misc.random(5) == 0) {
                        player.getSkills().drain(Skill.PRAYER,10);
                        mob.forceChat("Krrr!");
                    }
                    player.message("The creature infests you with its toxic fungus.");
                }
                int base = mob.getId() >= 14696 && mob.getId() <= 14699 ? 10 : 2;
                delayHit(mob, 0, target, new Hit(mob, base + Misc.random(2), HitMask.YELLOW, CombatIcon.NONE));
                return 1;
            }
        }
        if ((mob.getId() == 14700 || mob.getId() == 14701) && Misc.random(5) == 0) {
            int size = mob.getSize();
            int sizeX = mob.getSizeX();
            int sizeY = mob.getSizeY();
            if (Utils.isOnRange(mob, target, 6)) {
                int[][] dirs = Utils.getCoordOffsetsNear(size);
                for (int dir = 0; dir < dirs[0].length; dir++) {
                    final Position tile = new Position(target.getX() + dirs[0][dir], target.getY() + dirs[1][dir], target.getZ());
                    if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), sizeX, sizeY)) {
                        mob.forceChat("Hup!");
                        Position middle = mob.getCentredPosition();
                        mob.setForceMovement(new ForceMovement(mob.copyPosition(), 0, tile, 2, Utils.getAngle(tile.getX() - middle.getX(), tile.getY() - middle.getY())));
                        mob.moveTo(tile);
                        mob.perform(new Animation(15491, Revision.PRE_RS3));
                        return 8;
                    }
                }
            }
        } else if ((mob.getId() == 14688 || mob.getId() == 14689) && Misc.random(5) == 0) {
            mob.getMovement().reset();
            mob.calcFollow(target, false);
            mob.setForceFollowClose(true);
            mob.forceChat("Raargh!");
            GameWorld.schedule(5, () -> {
                if (!Utils.isOnRange(target, mob, 0))
                    mob.forceChat("*Sigh*");
                mob.setForceFollowClose(false);
            });
            return 5;
        }
        if (mob.getCombatDefinition().getAttackStyle() != MobCombatDefinitions.MELEE) {
            if (mob.getId() == 14701 || mob.getId() == 14689) {
                delayHit(mob, 2, target, getRangeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.RANGE, target)));
                if (mob.getId() == 14689) {
                    mob.perform(new Animation(15487, Revision.PRE_RS3));
                    return mob.getCombat().getAttackSpeed();
                }
            } else {
                Projectile projectile = new Projectile(mob, target, 2035, Revision.PRE_RS3, 41, 16, 20, 2, 10, 0);
                ProjectileManager.send(projectile);
                delayHit(mob, projectile.getTickDelay() - 1, target, getMagicHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MAGE, target)));
            }
        }
        else
            delayHit(mob, 0, target, getMeleeHit(mob, getRandomMaxHit(mob, MobCombatDefinitions.MELEE, target)));
        if (def.getAttackGraphic() != -1)
            mob.perform(new Graphic(def.getAttackGraphic(), Revision.PRE_RS3));
        mob.perform(new Animation(def.getAttackAnim(), Revision.PRE_RS3));
        return mob.getCombat().getAttackSpeed();
    }
}
