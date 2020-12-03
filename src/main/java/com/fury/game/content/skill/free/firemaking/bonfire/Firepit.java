package com.fury.game.content.skill.free.firemaking.bonfire;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.Firemaking;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.entity.object.TempObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.update.flag.block.Animation;

/**
 * Created by Greg on 29/11/2016.
 */
public class Firepit extends Action {
    private GameObject object;

    public Firepit(GameObject object) {
        this.object = object;
    }

    private boolean checkAll(Player player) {
        if (!ObjectManager.containsObjectWithId(object, object.getId()))
            return false;
        if (!player.getInventory().contains(new Item(21350)))
            return false;
        if (!player.getSkills().hasRequirement(Skill.FIREMAKING, 83, "burn roots"))
            return false;
        return true;
    }


    public static boolean addRoot(Player player, GameObject object, Item item) {
        if (item.getId() == 21350) {
            player.getActionManager().setAction(new Firepit(object));
            return true;
        }
        return false;
    }

    public static void addRoots(Player player, GameObject object) {
        if(!player.getInventory().contains(new Item(21350)))
            player.message("You do not have any roots to add to this fire.");
        else
            player.getActionManager().setAction(new Firepit(object));
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            player.getDirection().face(object);
            return true;
        }
        return false;

    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if(object instanceof TempObject)
            ((TempObject) object).addTime(30000);
        player.getInventory().delete(new Item(21350, 1));
        player.getSkills().addExperience(Skill.FIREMAKING, Firemaking.increasedExperience(player, 378.7));
        player.perform(new Animation(16699, Revision.PRE_RS3));
        player.message("You successfully refuel the fire.", true);
        JadinkoLair.addPoints(player, player.getEffects().hasActiveEffect(Effects.AQUATIC_FRUIT) ? 4 : 2);
        return 4;
    }

    @Override
    public void stop(final Player player) {
    }

}