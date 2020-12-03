package com.fury.game.content.controller.impl;

import com.fury.cache.def.object.ObjectDefinition;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.global.WildernessObelisks;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.Flag;
import com.fury.util.Utils;

public class Wilderness extends Controller {

    private boolean showingSkull;

    @Override
    public void start() {
        moved();
        checkBoosts(player);
    }
    
    public static final boolean isAtWild(Position tile) {
       return (tile.getX() >= 2940 && tile.getX() <= 3395 && tile.getY() >= 3525 && tile.getY() <= 4000)
                || (tile.getX() >= 2756 && tile.getX() <= 2875 && tile.getY() >= 5512 && tile.getY() <= 5627)
                || (tile.getX() >= 3158 && tile.getX() <= 3181 && tile.getY() >= 3679 && tile.getY() <= 3697)
                || (tile.getX() >= 3280 && tile.getX() <= 3183 && tile.getY() >= 3883 && tile.getY() <= 3888)
               || (tile.getX() >= 3015 && tile.getX() <= 3133 && tile.getY() >= 10054 && tile.getY() <= 10172)//Forinthry dungeon
               ;
    }

    @Override
    public boolean login() {
        moved();
        return false;
    }

    @Override
    public boolean keepCombating(Figure target) {
        if (target.isNpc())
            return true;
        if (!canAttack(target))
            return false;
        if (target.getCombat().getAttackedBy() != player && player.getCombat().getAttackedBy() != target)
            CombatConstants.skullPlayer(player);
        return true;
    }


    @Override
    public boolean canAttack(Figure target) {
        if (target.isPlayer()) {
            Player p2 = (Player) target;
            if (player.isCanPvp() && !p2.isCanPvp()) {
                player.message("That player is not in the wilderness.");
                return false;
            }
            if (canHit(target))
                return true;

            // warning message here
            return false;
        }
        return true;
    }

    @Override
    public boolean canHit(Figure target) {
        if (target.isNpc())
            return true;
        Player p2 = (Player) target;
        if (Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) > getWildLevel())
            return false;
        return true;
    }

    private boolean canTele() {
        if(player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR)) {
            if(player.getRights() == PlayerRights.ADMINISTRATOR) {
                player.getPacketSender().sendMessage("You have teleported out of the wilderness, logs have been written.", 0xff0000);
            }
            PlayerLogs.log(player.getUsername(), "Teleported out of wilderness, hp: " + player.getHealth().getHitpoints() + " under attack: " + player.getCombat().isUnderAttack());
            return true;
        }
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        if (getWildLevel() > 20 && !canTele()) {
            player.message("A mysterious force prevents you from teleporting.");
            return false;
        }
        if (player.getEffects().hasActiveEffect(Effects.TELEPORT_BLOCK)) {
            player.message("A mysterious force prevents you from teleporting.");
            return false;
        }
        return true;

    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        if (getWildLevel() > 20 && !canTele()) {
            player.message("A mysterious force prevents you from teleporting.");
            return false;
        }

        if (player.getEffects().hasActiveEffect(Effects.TELEPORT_BLOCK)) {
            player.message("A mysterious force prevents you from teleporting.");
            return false;
        }
        return true;
    }

    @Override
    public boolean processObjectTeleport(Position toTile) {
        Long teleblock = (Long) player.getTemporaryAttributes().get("TeleBlocked");
        if (teleblock != null && teleblock > Utils.currentTimeMillis()) {
            player.message("A mysterious force prevents you from teleporting.");
            return false;
        }
        return true;
    }

    @Override
    public void sendInterfaces() {
        if (isAtWild(player))
            showSkull();
    }

    @Override
    public boolean sendDeath() {
        removeIcon();
        return true; // TODO custom dead without graves
    }

    @Override
    public void moved() {
        boolean isAtWild = isAtWild(player);
        boolean isAtWildSafe = isAtWildSafe();
        if (!showingSkull && isAtWild && !isAtWildSafe) {
            showingSkull = true;
            player.setCanPvp(true);
            showSkull();
        } else if (showingSkull && (isAtWildSafe || !isAtWild)) {
            removeIcon();
        } else if (!isAtWildSafe && !isAtWild) {
            player.setCanPvp(false);
            removeIcon();
            removeController();
        } else if(showingSkull && prevLevel != getWildLevel()) {
            player.getPacketSender().sendString(42023, String.valueOf(getWildLevel()));
            prevLevel = getWildLevel();
        } else if (player.getX() >= 3385 && player.getX() <= 3388 && player.getY() <= 3617 && player.getY() >= 3613) {
            removeIcon();
            player.setCanPvp(false);
            removeController();
            player.getControllerManager().startController(new Daemonheim());
        }
    }

    private int prevLevel = 0;

    public void removeIcon() {
        if (showingSkull) {
            showingSkull = false;
            player.setCanPvp(false);
            player.send(new WalkableInterface(-1));
            player.getEquipment().refresh();
        }

        player.getPacketSender().sendInteractionOption("null", 2, false);
        player.getPacketSender().sendString(19000, "Combat level: " + player.getSkills().getCombatLevel());
        player.getUpdateFlags().add(Flag.APPEARANCE);
    }

    public void showSkull() {
        player.send(new WalkableInterface(42020));
        player.getPacketSender().sendString(19000, "Combat level: " + player.getSkills().getCombatLevel());
        prevLevel = getWildLevel();
        player.getPacketSender().sendString(42023, String.valueOf(getWildLevel()));

        if(player.getRights() == PlayerRights.ADMINISTRATOR) {
            player.setInvulnerable(false);
            player.message("Invulnerability: Disabled");

        }
    }

    @Override
    public boolean logout() {
        return false; // so doesn't remove script
    }

    @Override
    public void forceClose() {
        removeIcon();
    }


    public boolean isAtWildSafe() {
        return (player.getX() >= 2940 && player.getX() <= 3395 && player.getY() <= 3524 && player.getY() >= 3523)
                || (player.getX() >= 3177 && player.getX() <= 3195 && player.getY() >= 3678 && player.getY() <= 3703)
                || (player.getX() >= 3169 && player.getX() <= 3176 && player.getY() >= 3671 && player.getY() <= 3692)
                || (player.getX() >= 3164 && player.getX() <= 3169 && player.getY() >= 3666 && player.getY() <= 3692)
                || (player.getX() >= 3153 && player.getX() <= 3163 && player.getY() >= 3660 && player.getY() <= 3685)
                || (player.getX() >= 3142 && player.getX() <= 3152 && player.getY() >= 3654 && player.getY() <= 3674)
                || (player.getX() >= 3140 && player.getX() <= 3141 && player.getY() >= 3653 && player.getY() <= 3657)
                || (player.getX() >= 3177 && player.getX() <= 3179 && player.getY() >= 3676 && player.getY() <= 3677)
                || (player.getX() >= 3045 && player.getX() <= 3060 && player.getY() >= 10090 && player.getY() <= 10106)
        ;
    }

    public int getWildLevel() {
        int y = player.getY();
        return (y > 9000 ? ((y - 6410) - 3520) : (y - 3520)) / 8 + 1;
    }

    public static void checkBoosts(Player player) {
        boolean changed = false;
        int level = player.getSkills().calcBuff(Skill.ATTACK, 5, 0.15);
        changed |= player.getSkills().cap(Skill.ATTACK, level);

        level = player.getSkills().calcBuff(Skill.STRENGTH, 5, 0.15);
        changed |= player.getSkills().cap(Skill.STRENGTH, level);

        level = player.getSkills().calcBuff(Skill.DEFENCE, 5, 0.15);
        changed |= player.getSkills().cap(Skill.DEFENCE, level);

        level = player.getSkills().calcBuff(Skill.RANGED, 5, 0.10);
        changed |= player.getSkills().cap(Skill.RANGED, level);

        level = player.getSkills().calcBuff(Skill.MAGIC, 5, 0.0);
        changed |= player.getSkills().cap(Skill.MAGIC, level);

        if (changed)
            player.message("Your extreme potion bonus has been reduced.");
    }

    public static boolean isDitch(int id) {
        return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
    }

    public static void crossDitch(Player player, GameObject ditch) {
        player.stopAll();
        player.addStopDelay(4);
        player.animate(6132);
        final Position toTile = new Position(player.getX(), ditch.getY() + 2, ditch.getZ());
        player.setForceMovement(new ForceMovement(player, 1, toTile, 2, 0));
        final ObjectDefinition objectDef = ditch.getDefinition();
        GameWorld.schedule(2, () -> {
            player.getDirection().face(new Position(ditch.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getFace().getId()), ditch.getCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getFace().getId()), ditch.getZ()));
            player.moveTo(toTile);
            player.getControllerManager().startController(new Wilderness());
        });
    }

    @Override
    public boolean processObjectClick1(final GameObject object) {
        if (isDitch(object.getId())) {
            player.setStopAttackDelay(Long.MAX_VALUE);
            player.animate(6132);
            final Position toTile = new Position(player.getX(),
                    object.getY() - 1, object.getZ());
            player.setForceMovement(new ForceMovement(player, 1, toTile, 2, 2));
            final ObjectDefinition objectDef = object.getDefinition();
            GameWorld.schedule(2, () -> {
                player.moveTo(toTile);
                player.getDirection().face(new Position(
                        object.getCoordFaceX(objectDef.getSizeX(),
                                objectDef.getSizeY(), object.getDirection()),
                        object.getCoordFaceY(objectDef.getSizeX(),
                                objectDef.getSizeY(), object.getDirection()),
                        object.getZ()));
                removeIcon();
                removeController();
                player.setStopAttackDelay(0);
            });
            return false;
        }
        if(WildernessObelisks.handleObelisk(object.getId()))
            return false;

        return true;
    }

    public static Item[] getItemsKeptOnDeath(Player player, Player killer) {
        return null;
    }
}
