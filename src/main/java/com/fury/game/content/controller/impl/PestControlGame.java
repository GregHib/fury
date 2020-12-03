package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.Entity;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.minigames.impl.Lander;
import com.fury.game.content.global.minigames.impl.PestControl;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.figure.player.PlayerDeath;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class PestControlGame extends Controller {

    private PestControl control;
    private double points;
    private int deaths = 0;

    @Override
    public void start() {
        control = (PestControl) getArguments()[0];
        setArguments(null);
        setPoints(0.0D);
        sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
        refreshDamagePoints();
        player.send(new WalkableInterface(21100));
    }

    private void refreshDamagePoints() {
        boolean isGreen = getPoints() > 500;
        player.getPacketSender().sendString(21116, FontUtils.colourTags(isGreen ? 0x00f800 : 0xf80000) + (int) getPoints() + FontUtils.COL_END);
    }

    @Override
    public void forceClose() {
        if (control != null) {
            if (control.getPortalCount() != 0)
                control.getPlayers().remove(player);
            ObjectHandler.useStairs(player, -1, Lander.getLanders()[control.getPestData().ordinal()].getLanderRequirement().getExitTile(), 1, 2);
        } else
            ObjectHandler.useStairs(player, -1, new Position(2657, 2639), 1, 2);
        player.send(new WalkableInterface(-1));
        player.reset();
    }

    @Override
    public void magicTeleported(int teleType) {
        player.getControllerManager().forceStop();
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        player.getDialogueManager().startDialogue(new SimpleMessageD(), "You can't leave the pest control area like this.");
        return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        player.getDialogueManager().startDialogue(new SimpleMessageD(), "You can't leave the pest control area like this.");
        return false;
    }

    @Override
    public boolean canMove(int dir) {
        Position toTile = new Position(player.getX() + Misc.DIRECTION_DELTA_X[dir],
                player.getY() + Misc.DIRECTION_DELTA_Y[dir], player.getZ());
        return !control.isBrawlerAt(toTile);
    }

    @Override
    public boolean login() { // shouldnt happen
        ObjectHandler.useStairs(player, -1, new Position(2657, 2639), 1, 2);
        return true;
    }

    @Override
    public boolean logout() {
        if (control != null) {
            player.reset();
            player.moveTo(2657, 2639);
            control.getPlayers().remove(player);
        }
        return true;
    }

    @Override
    public boolean canSummonFamiliar() {
        player.message("You feel it's best to keep your Familiar away during this game.");
        return false;
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        GameWorld.schedule(new PlayerDeath(player, player.copyPosition(), control.getWorldTile(35 - Misc.random(4), 54 - (Misc.random(3))), false, () -> deaths++));
        return false;
    }

    @Override
    public void processActualHit(Hit hit, Entity target) {
        if (hit.getCombatIcon() == CombatIcon.MELEE || hit.getCombatIcon() == CombatIcon.RANGED || hit.getCombatIcon() == CombatIcon.MAGIC) {
            int hit_damage = hit.getDamage();
            if (hit_damage > 0) {
                setPoints(getPoints() + hit.getDamage());
                refreshDamagePoints();
            }
        }
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getDeaths() {
        return deaths;
    }
}
