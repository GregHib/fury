package com.fury.game.content.skill.free.woodcutting;

import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectDirection;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.RandomUtils;

public class Vines extends Action {

    private GameObject vine;
    private Hatchet hatchet;

    public Vines(GameObject vine) {
        this.vine = vine;
    }

    @Override
    public boolean start(Player player) {
        if (!checkAll(player))
            return false;
        player.message("You swing your hatchet at the vines.");
        setActionDelay(player,1);
        return true;
    }

    private boolean checkAll(Player player) {
        hatchet = getHatchet(player, false);
        if (hatchet == null) {
            player.message("You don't have a hatchet that you can use.");
            return false;
        }
        if (!player.getSkills().hasRequirement(Skill.WOODCUTTING, 30, "chop down this vine"))
            return false;
        if (player.getInventory().getSpaces() <= 0) {
            player.getInventory().full();
            return false;
        }
        return true;
    }

    public static Hatchet getHatchet(Player player, boolean dungeoneering) {
        //Check equipment
        for (int i = dungeoneering ? 10 : Hatchet.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) { //from best to worst
            Hatchet def = Hatchet.values()[i];
            if (player.getEquipment().get(Slot.WEAPON).getId() == def.getItemId() && player.getSkills().hasLevel(Skill.WOODCUTTING, def.getLevelRequired()))
                return def;
        }

        //Check inventory
        for (int i = dungeoneering ? 10 : Hatchet.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) { //from best to worst
            Hatchet def = Hatchet.values()[i];
            if (player.getInventory().contains(new Item(def.getItemId())) && player.getSkills().hasLevel(Skill.WOODCUTTING, def.getLevelRequired()))
                return def;
        }
        return null;
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(hatchet.getEmoteId()));
        return ObjectManager.containsObjectWithId(vine, vine.getId());
    }

    @Override
    public int processWithDelay(Player player) {
        if(RandomUtils.success(0.25)) {
            ObjectHandler.removeObjectTemporary(vine, 2000);
            player.message("You chop down the vines..", true);
            player.getSkills().addExperience(Skill.WOODCUTTING, 45);
            if(vine.getFace() == ObjectDirection.SOUTH || vine.getFace() == ObjectDirection.NORTH)
                player.getMovement().addWalkSteps(vine.getX() + (player.getX() > vine.getX() ? -1 : 1), vine.getY(), -1, false);
            else
                player.getMovement().addWalkSteps(vine.getX(), vine.getY() + player.getY() > vine.getY() ? -1 : 1, -1, false);
            return -1;
        }
        return 1;
    }


    @Override
    public void stop(Player player) {
        player.animate(-1);
    }

}
