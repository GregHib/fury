package com.fury.game.content.controller.impl;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.Controller;
import com.fury.game.entity.character.player.content.Jail;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class JailController extends Controller {

    Position cell;

    @Override
    public void start() {
        cell = Jail.getJail(player);
        player.moveTo(cell);

        long minutes = (long) getArguments()[0];
        if (player.getVars().getLong(Variables.JAIL_TIME) != -1L) {
            player.message("You have been jailed for " + minutes + " minutes.");
        } else {
            player.message("You have been jailed.");
        }
    }

    public void unjail() {
        player.getVars().set(Variables.JAIL_TIME, 0L);
        Jail.unjail(player);
        player.moveTo(3030, 2978);
        player.save();
        player.message("Your sentence has been served and you are free to go.");
        removeController();
    }

    @Override
    public void process() {
        //If isn't jailed
        if (player.getVars().getLong(Variables.JAIL_TIME) <= Misc.currentTimeMillis()) {
            unjail();
        } else if(!player.isWithinDistance(cell, 5))
            player.moveTo(cell);
    }

    @Override
    public boolean login() {
        if (player.getVars().getLong(Variables.JAIL_TIME) > 0L) {
            long difference = player.getVars().getLong(Variables.JAIL_TIME) - Misc.currentTimeMillis();

            if(difference < 0) {
                unjail();
            } else {
                int minutes = (int) (player.getVars().getLong(Variables.JAIL_TIME) - Misc.currentTimeMillis()) / (60 * 1000);
                cell = Jail.getJail(player);
                player.message("You have " + minutes + " minute" + (minutes == 1 ? "" : "s") + " of your sentence left to serve.");
            }
        }
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean sendDeath() {
        player.message("No even death releases you from jail.");
        player.getHealth().setHitpoints(player.getMaxConstitution());
        return false;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        player.message("You can't teleport out of jail!");
        return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        player.message("You can't teleport out of jail!");
        return false;
    }

    @Override
    public boolean processObjectTeleport(Position toTile) {
        player.message("You can't teleport out of jail!");
        return false;
    }

    private void handle(GameObject object) {
        if(object == null)
            return;
        if(object.getDefinition().getName().startsWith("Door"))
            player.message("The door is locked.");
        else
            player.message("You can't reach that.");

    }

    /*
     * Overkill
     */

    @Override
    public boolean processObjectClick1(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean processUntouchableObjectClick1(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean processObjectClick2(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean processObjectClick3(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean processObjectClick4(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean processObjectClick5(GameObject object) {
        handle(object);
        return true;
    }

    @Override
    public boolean handleItemOnObject(GameObject object, Item item) {
        handle(object);
        return true;
    }

    @Override
    public boolean canCast(Player player, Spell combatSpell) {
        return false;
    }

    public static boolean isJail(Position position) {
        return (position.getX() >= 3027 && position.getX() <= 3040 && position.getY() >= 2972 && position.getY() <= 2989);
    }
}
