package com.fury.game.entity.character.player.actions;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.global.events.christmas.ChristmasPartyCharacters;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.bosses.nex.NexMinion;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Snowball extends Action {
    private Figure target;

    public Snowball(Figure target) {
        this.target = target;
    }

    public static void pelt(Player player, Figure target) {
        player.stopAll(false);

        if(target.isNpc()) {
            Mob mob = (Mob) target;
            ChristmasPartyCharacters character = ChristmasPartyCharacters.getCharacter(mob.getId());
            if(character == null)
                return;

            if(player.getChristmasCharacter(character.ordinal()) && !(player.getControllerManager().getController() instanceof ChristmasEvent)) {
                player.message("You've already invited this character to santa's party.");
                return;
            }
        }

        boolean lineOfSight = player.getCombat().clippedProjectile(target, false);
        player.getDirection().setInteracting(target);
        if(lineOfSight) {
            player.getActionManager().setAction(new Snowball(target));
        } else {
            player.setRouteEvent(new RouteEvent(target,() -> player.getActionManager().setAction(new Snowball(target)), !lineOfSight));
        }
    }

    @Override
    public boolean start(Player player) {
        player.getDirection().setInteracting(target);
        if (checkAll(player))
            return true;
        player.getDirection().setInteracting(null);
        return false;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        int maxDistance = 7;
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int sizeX = target.getSizeX();
        int sizeY = target.getSizeY();
        if (!player.getCombat().clippedProjectile(target, target instanceof NexMinion ? false : maxDistance == 0))
            return 0;
        player.setRouteEvent(null);
        if (player.getMovement().hasWalkSteps())
            maxDistance += 1;
        if (distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)
            return 0;

        if (target.isDead())
            return -1;

        return rangeAttack(player);
    }

    private int rangeAttack(final Player player) {
        final Item weapon = player.getEquipment().get(Slot.WEAPON);
        if (weapon.getId() == 11951) {
            player.animate(7530);
            ProjectileManager.send(new Projectile(player, target, 1281, Revision.RS2, 41, 16, 60, 30, 16, 0));
            GameWorld.schedule(1, () -> target.graphic(1282));
            dropAmmo(player);
        }

        if(target.isNpc()) {
            Mob mob = (Mob) target;
            ChristmasPartyCharacters character = ChristmasPartyCharacters.getCharacter(mob.getId());
            if(character == null)
                return -1;

            if(player.getControllerManager().getController() instanceof ChristmasEvent) {
                target.forceChat(ChristmasPartyCharacters.randomParty[Misc.random(ChristmasPartyCharacters.randomParty.length - 1)]);
            } else {
                player.setChristmasCharacter(character.ordinal(), true);
                target.forceChat(ChristmasPartyCharacters.randomCatch[Misc.random(ChristmasPartyCharacters.randomCatch.length - 1)]);
                player.message("You invited " + mob.getName() + " to santa's party.");
            }
        }

        return -1;
    }

    public void dropAmmo(Player player) {
        player.getEquipment().delete(new Item(11951));
        Equipment.resetWeapon(player);
    }

    @Override
    public void stop(Player player) {
    }

    private boolean checkAll(Player player) {
        if (player.isDead() || player.getFinished() || target.isDead() || target.getFinished())
            return false;
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int sizeX = target.getSizeX();
        int sizeY = target.getSizeY();
        int maxDistance = 16;
        if (player.getZ() != target.getZ() || distanceX > sizeX + maxDistance || distanceX < -1 - maxDistance || distanceY > sizeY + maxDistance || distanceY < -1 - maxDistance)
            return false;
        if (player.getCombat().getFreezeDelay() >= Utils.currentTimeMillis()) {
            if (player.isWithinDistance(target, 0))// done
                return false;
            return true;
        }

        if (player.getX() == target.getX() && player.getY() == target.getY() && target.getSize() == 1 && !target.getMovement().hasWalkSteps()) {
            if (!player.getMovement().addWalkSteps(target.getX() + 1, target.getY(), 1))
                if (!player.getMovement().addWalkSteps(target.getX() - 1, target.getY(), 1))
                    if (!player.getMovement().addWalkSteps(target.getX(), target.getY() + 1, 1))
                        if (!player.getMovement().addWalkSteps(target.getX(), target.getY() - 1, 1))
                            return false;
        }

        if(player.getEquipment().get(Slot.WEAPON).getId() != 11951) {
            player.message("You don't have anything equipped that you can throw.");
            return false;
        }

        maxDistance = 7;
        if ((!player.getCombat().clippedProjectile(target, target instanceof NexMinion ? false : maxDistance == 0))
                || distanceX > sizeX + maxDistance
                || distanceX < -1 - maxDistance
                || distanceY > sizeY + maxDistance
                || distanceY < -1 - maxDistance) {
            if (!player.getMovement().hasWalkSteps()) {
                player.getMovement().reset();
                player.getMovement().addWalkStepsInteract(target.getX(), target.getY(), player.getSettings().getBool(Settings.RUNNING) ? 2 : 1, sizeX, sizeY, true);
            }
            return true;
        } else {
            player.getMovement().reset();
        }
        return true;
    }
}
