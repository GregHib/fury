package com.fury.game.entity.character.npc;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.GameSettings;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.combat.CombatScriptsHandler;
import com.fury.game.npc.bosses.nex.Nex;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitions;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class NpcCombat {

    private Mob mob;
    private int combatDelay;
    private Figure target;

    public NpcCombat(Mob mob) {
        this.mob = mob;
    }

    public int getCombatDelay() {
        return combatDelay;
    }

    /*
     * returns if under combat
     */
    public boolean process() {
        if (combatDelay > 0)
            combatDelay--;
        if (target != null) {
            if (!checkAll()) {
                removeTarget();
                return false;
            }
            if (combatDelay <= 0)
                combatDelay = combatAttack();
            return true;
        }
        return false;
    }
    /*
     * return combatDelay
     */
    private int combatAttack() {
        Figure target = this.target; // prevents multithread issues
        if (target == null)
            return 0;
        // if hes frozen not gonna attack
        if (mob.getCombat().getFreezeDelay() >= Utils.currentTimeMillis())
            return 0;
        // check if close to target, if not let it just walk and dont attack
        // this gameticket
        MobCombatDefinitions defs = mob.getCombatDefinition();
        int attackStyle = defs.getAttackStyle();
        int maxDistance = mob.isCantFollowUnderCombat() ? 16 : attackStyle == MobCombatDefinitions.MELEE || attackStyle == MobCombatDefinitions.SPECIAL2 ? 0 : 7;
        if (!(mob instanceof Nex) && !mob.getCombat().clippedProjectile(target, maxDistance == 0))
            return 0;
        int distanceX = target.getX() - mob.getX();
        int distanceY = target.getY() - mob.getY();
        /*
         * if(npc.hasWalkSteps()) maxDistance += 1;
		 */
        if (distanceX > mob.getSizeX() + maxDistance || distanceX < -1 - maxDistance
                || distanceY > mob.getSizeY() + maxDistance
                || distanceY < -1 - maxDistance) {
            return 0;
        }
        addAttackedByDelay(target);
        return CombatScriptsHandler.attack(mob, target);
    }

    public void performDefenceAnimation(Figure target) {
        target.performAnimationNoPriority(new Animation(CombatConstants.getDefenceEmote(target)));
    }

    public Figure getTarget() {
        return target;
    }

    public void addAttackedByDelay(Figure target) { // prevents multithread  issues
        target.getCombat().setAttackedBy(mob);
        target.getCombat().setAttackedByDelay(mob.getCombatDefinition().getAttackDelay() * 1000);
        mob.getCombat().setAttackingDelay(GameSettings.COMBAT_DELAY);
    }

    public void setTarget(Figure target) {
        this.target = target;
        mob.getDirection().setInteracting(target);
        if (!checkAll())
            removeTarget();
    }

    public boolean checkAll() {
        Figure target = this.target; // prevents multithread issues
        if (target == null)
            return false;
        if (mob.isDead() || mob.getFinished() || mob.isForceWalking() || target.isDead() || target.getFinished())
            return false;
        if (mob.getCombat().getFreezeDelay() >= Utils.currentTimeMillis())
            return true; // if freeze cant move ofc
        int distanceX = mob.getX() - mob.getRespawnTile().getX();
        int distanceY = mob.getY() - mob.getRespawnTile().getY();
        int sizeX = mob.getSizeX();
        int sizeY = mob.getSizeY();
        int maxDistance = 32;
        if (!mob.isNoDistanceCheck() && !mob.isCantFollowUnderCombat() && !mob.isFamiliar()) {
            /*if (npc.getMapAreaNameHash() != -1) {
                // if out his area
                if (!MapAreas.isAtArea(npc.getMapAreaNameHash(), npc)
                        || (!npc.canBeAttackFromOutOfArea() && !MapAreas
                        .isAtArea(npc.getMapAreaNameHash(), target))) {
                    npc.forceWalkHome();
                    return false;
                }
            } else*/
            if (distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance) {
                // if more than 64 distance from respawn place
                mob.forceWalkHome();
                return false;
            }

            maxDistance = 16;
            distanceX = target.getX() - mob.getX();
            distanceY = target.getY() - mob.getY();
            if (mob.getZ() != target.getZ() || distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance) {
                return false; // if target distance higher 16
            }
        } else {
            distanceX = target.getX() - mob.getX();
            distanceY = target.getY() - mob.getY();
        }

        // checks for no multi area :)
        if (mob.isFamiliar()) {
            Familiar familiar = (Familiar) mob;
            if (!familiar.canAttack(target))
                return false;
        } else {
            if (!mob.isForceMultiAttacked()) {
                if (!target.inMulti() || !mob.inMulti()) {
                    if (mob.getCombat().getAttackedBy() != target && mob.getCombat().getAttackedByDelay() > System.currentTimeMillis())
                        return false;
                    if (target.getCombat().getAttackedBy() != mob && target.getCombat().getAttackedByDelay() > System.currentTimeMillis())
                        return false;
                }
            }
        }
        if (!mob.isCantFollowUnderCombat()) {
            int targetSizeX = target.getSizeX();
            int targetSizeY = target.getSizeY();

            //If under step away
            if (!target.getMovement().hasWalkSteps() && Misc.colides(mob.getX(), mob.getY(), sizeX, sizeY, target.getX(), target.getY(), target.getSizeX(), target.getSizeY())) {
                mob.getMovement().reset();
                if (!mob.getMovement().addWalkSteps(target.getX() + targetSizeX, mob.getY())) {
                    mob.getMovement().reset();
                    if (!mob.getMovement().addWalkSteps(target.getX() - sizeX, mob.getY())) {
                        mob.getMovement().reset();
                        if (!mob.getMovement().addWalkSteps(target.getX(), mob.getY() + targetSizeY)) {
                            mob.getMovement().reset();
                            if (!mob.getMovement().addWalkSteps(target.getX(), mob.getY() - sizeY)) {
                                return true;
                            }
                        }
                    }
                }
                return true;
            }

            int attackStyle = mob.getCombatDefinition().getAttackStyle();
            if (mob instanceof Nex) {
                Nex nex = (Nex) mob;
                maxDistance = nex.isFollowTarget() ? 0 : 7;
                if (nex.getFlyTime() == 0 && (distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)) {
                    mob.getMovement().reset();
                    mob.getMovement().addWalkStepsInteract(target.getX(), target.getY(), 2, sizeX, sizeY, true);
                    if (!mob.getMovement().hasWalkSteps()) {
                        int[][] dirs = Utils.getCoordOffsetsNear(Math.max(sizeX, sizeY));
                        for (int dir = 0; dir < dirs[0].length; dir++) {
                            final Position tile = new Position(target.getX() + dirs[0][dir], target.getY() + dirs[1][dir], target.getZ());
                            if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), sizeX, sizeY)) {
                                nex.setFlyTime(4);
                                mob.setForceMovement(new ForceMovement(mob, 0, tile, 1, Utils.getMoveDirection(tile.getX() - mob.getX(), tile.getY() - mob.getY())));
                                mob.animate(6985);
                                mob.moveTo(tile);
                                return true;
                            }
                        }
                    }
                    return true;
                } else
                    mob.getMovement().reset();
            } else {
                maxDistance = mob.isForceFollowClose() ? 0 : (attackStyle == MobCombatDefinitions.MELEE || attackStyle == MobCombatDefinitions.SPECIAL2) ? 0 : 7;

                // is far from target, moves to it till can attack
                if ((!mob.getCombat().clippedProjectile(target, maxDistance == 0)) || distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance) {
                    mob.getMovement().reset();
                    mob.getMovement().addWalkStepsInteract(target.getX(), target.getY(), 2, sizeX, sizeY, true);
                    return true;
                } else
                    // if doesnt need to move more stop moving
                    mob.getMovement().reset();
                // if under target, moves
            }
        }
        return true;
    }

    public void addCombatDelay(int delay) {
        combatDelay += delay;
    }

    public void setCombatDelay(int delay) {
        combatDelay = delay;
    }

    public boolean underCombat() {
        return target != null;
    }

    public void removeTarget() {
        this.target = null;
        mob.getDirection().setInteracting(null);
    }

    public void reset() {
        combatDelay = 0;
        target = null;
    }
}
