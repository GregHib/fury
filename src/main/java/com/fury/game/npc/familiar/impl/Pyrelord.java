package com.fury.game.npc.familiar.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.Fire;
import com.fury.game.content.skill.free.firemaking.Firemaking;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class Pyrelord extends Familiar {

    public Pyrelord(Player owner, Summoning.Pouches pouch, Position tile) {
        super(owner, pouch, tile);
    }

    @Override
    public String getSpecialName() {
        return "Immense Heat";
    }

    @Override
    public String getSpecialDescription() {
        return "Craft a gold bar (and a gem if one wishes) into an item of Jewellery without using a furnace.";
    }

    @Override
    public int getStoreSize() {
        return 0;
    }

    @Override
    public int getSpecialAttackEnergy() {
        return 5;
    }

    @Override
    public SpecialAttack getSpecialAttack() {
        return SpecialAttack.ITEM;
    }

    @Override
    public boolean submitSpecial(Object object) {
        return false;
    }

    public static boolean lightLog(final Familiar familiar, final Item item) {
        final Player player = familiar.getOwner();
        final Fire fire = Firemaking.getFire(item.getId());
        if (fire == null)
            return false;
        Long time = (Long) familiar.getTemporaryAttributes().get("Fire");
        if (Firemaking.checkAll(player, fire, null, true) && (time != null && time < Misc.currentTimeMillis() || time == null)) {
            player.message("The pyrelord attempts to light the logs.");
            player.getInventory().delete(new Item(fire.getLogId(), 1));
            final Position tile = new Position(familiar);
            FloorItemManager.addGroundItem(new Item(fire.getLogId(), 1), tile, player);
            familiar.animate(8085);
            GameWorld.schedule(2, () -> {
                if (!familiar.getMovement().addWalkSteps(familiar.getX() - 1, familiar.getY(), 1))
                    if (!familiar.getMovement().addWalkSteps(familiar.getX() + 1, familiar.getY(), 1))
                        if (!familiar.getMovement().addWalkSteps(familiar.getX(), familiar.getY() + 1, 1))
                            familiar.getMovement().addWalkSteps(familiar.getX(), familiar.getY() - 1, 1);
                player.message("The pyrelord uses its intense heat to light the logs.");
                GameWorld.schedule(1, () -> {
                    final FloorItem floorItem = GameWorld.getRegions().get(tile.getRegionId()).getFloorItem(fire.getLogId(), tile, player);
                    if (floorItem == null)
                        return;
                    if (!FloorItemManager.removeGroundItem(player, floorItem, false))
                        return;
                    GameObject object = new GameObject(fire.getFireId(), tile, 10, 0, fire.getFireId() > Loader.getTotalObjects(Revision.RS2) ? Revision.PRE_RS3 : Revision.RS2);
                    ObjectHandler.spawnTempGroundObject(object, 592, fire.getLife());
                    player.getSkills().addExperience(Skill.FIREMAKING, fire.getExperience());
                    familiar.getDirection().face(tile);
                });
                familiar.getTemporaryAttributes().put("Fire", Misc.currentTimeMillis() + 1800);
            });
        }
        return true;
    }
}
