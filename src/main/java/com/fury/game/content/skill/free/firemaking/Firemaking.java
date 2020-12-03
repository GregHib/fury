package com.fury.game.content.skill.free.firemaking;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.Inventory;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.Region;
import com.fury.util.Utils;

/**
 * The Firemaking skill
 *
 * @author Greg
 */

public class Firemaking extends Action {

    private Fire fire;
    private FloorItem floorItem;
    private FireStarter starter;

    public Firemaking(Fire fire, FireStarter starter) {
        this(fire, starter, null);
    }

    public Firemaking(Fire fire, FireStarter starter, FloorItem floorItem) {
        this.fire = fire;
        this.starter = starter;
        this.floorItem = floorItem;
    }

    @Override
    public boolean start(Player player) {
        if (starter == null)
            for (FireStarter s : FireStarter.values())
                if (player.getInventory().contains(new Item(s.getId()))) {
                    starter = FireStarter.forId(s.getId());
                    break;
                }

        if (!checkAll(player, fire, starter, false))
            return false;

        player.message("You attempt to light the logs.", true);

        if (floorItem == null) {
            player.getInventory().delete(new Item(fire.getLogId()));
            FloorItemManager.addGroundItem(new Item(fire.getLogId()), player.copyPosition(), player, false, 60, false, 120);
        }

        Long time = (Long) player.getTemporaryAttributes().remove("Fire");
        boolean quickFire = time != null && time > Utils.currentTimeMillis();
        setActionDelay(player, quickFire ? 1 : 2);
        if (!quickFire)
            player.animate(733);
        player.getMovement().lock();
        return true;
    }

    public static boolean isFiremaking(Player player, Item item1, Item item2) {
        for (FireStarter starter : FireStarter.values()) {
            Item log = Inventory.contains(starter.getId(), item1, item2);
            if (log == null)
                continue;
            return isFiremaking(player, starter, log.getId());
        }
        return false;
    }

    public static FireStarter getFireStarter(Item item) {
        FireStarter starter = FireStarter.forId(item.getId());
        return starter;
    }

    public static boolean isFireStarter(Item item) {
        FireStarter starter = FireStarter.forId(item.getId());
        return starter != null;
    }

    public static boolean isFiremaking(Player player, FireStarter starter, int logId) {
        return isFiremaking(player, starter, null, logId);
    }

    public static boolean isFiremaking(Player player, FireStarter starter, FloorItem floor, int logId) {
        for (Fire fire : Fire.values()) {
            if (fire.getLogId() == logId) {
                player.getActionManager().setAction(new Firemaking(fire, starter, floor));
                return true;
            }
        }
        return false;
    }

    public static boolean checkAll(Player player, Fire fire, FireStarter starter, boolean usingPyre) {
        if (!usingPyre) {
            if (starter == null || !player.getInventory().contains(new Item(starter.getId()))) {
                player.message("You do not have the required items to light this.");
                return false;
            }
        }
        if (player.getSkills().getLevel(Skill.FIREMAKING) < fire.getLevel()) {
            player.message("You must have a firemaking level of at least " + fire.getLevel() + " to light this.");
            return false;
        }
        if (!World.isTileFree(player.getX(), player.getY(), player.getZ(), 1) // clipped
                || ObjectManager.getObjectWithSlot(usingPyre ? player.getFamiliar() : player, Region.OBJECT_SLOT_FLOOR) != null) {
            player.message("You can't light a fire here.");
            return false;
        }
        return true;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player, fire, starter, false);
    }

    public static double increasedExperience(Player player, double totalXp) {
        if (player.getEquipment().get(Slot.HANDS).getId() == 13660)
            totalXp *= 1.025;
        if (player.getEquipment().get(Slot.RING).getId() == 13659)
            totalXp *= 1.025;
        if(player.getEquipment().get(Slot.SHIELD).getId() == 15439)
            totalXp *= 1.005;
        return totalXp;
    }

    @Override
    public int processWithDelay(Player player) {
        player.message("The fire catches and the logs begin to burn.", true);

        boolean useGround = floorItem != null;
        Position tile = useGround ? floorItem.getTile().copyPosition() : player.copyPosition();
        final FloorItem item = useGround ? floorItem : GameWorld.getRegions().get(tile.getRegionId()).getFloorItem(fire.getLogId(), tile, player);

        if (item == null) {
            player.getMovement().unlock();
            return -1;
        }

        if (!FloorItemManager.removeGroundItem(player, item, false)) {
            player.getMovement().unlock();
            return -1;
        }

        //Step away
        if (!player.getMovement().addWalkSteps(player.getX() - 1, player.getY(), 1))
            if (!player.getMovement().addWalkSteps(player.getX() + 1, player.getY(), 1))
                if (!player.getMovement().addWalkSteps(player.getX(), player.getY() + 1, 1))
                    player.getMovement().addWalkSteps(player.getX(), player.getY() - 1, 1);

        if (starter.isFirelighter())
            player.getInventory().delete(new Item(starter.getId()));

        int fireId = handleFireColour(starter, fire.getFireId());
        GameObject object = new GameObject(fireId, tile, 10, 0, fireId > Loader.getTotalObjects(Revision.RS2) ? Revision.PRE_RS3 : Revision.RS2);
        ObjectHandler.spawnTempGroundObject(object, 592, fire.getLife());

        player.getSkills().addExperience(Skill.FIREMAKING, increasedExperience(player, fire.getExperience()));
        player.getDirection().face(tile);
        if (fire == Fire.YEW)
            Achievements.doProgress(player, Achievements.AchievementData.BURN_5_YEW_LOGS);
        else if (fire == Fire.MAGIC)
            Achievements.doProgress(player, Achievements.AchievementData.BURN_2500_MAGIC_LOGS);
        if (starter.isFirelighter())
            Achievements.finishAchievement(player, Achievements.AchievementData.PRETTY_FIRE);
        Achievements.finishAchievement(player, Achievements.AchievementData.LIGHT_A_FIRE);

        StrangeRocks.handleStrangeRocks(player, Skill.FIREMAKING);
        ChristmasEvent.giveSnowflake(player);

        player.getTemporaryAttributes().put("Fire", Utils.currentTimeMillis() + 1800);
        player.getMovement().unlock();
        return -1;
    }

    private int handleFireColour(FireStarter starter, int fireId) {
        switch (starter) {
            case BLUE_FIRELIGHTER:
                return 70767;
            case GREEN_FIRELIGHTER:
                return 70768;
            case RED_FIRELIGHTER:
                return 70769;
            case PURPLE_FIRELIGHTER:
                return 70770;
            case WHITE_FIRELIGHTER:
                return 70771;
            default:
                return fireId;
        }
    }

    @Override
    public void stop(final Player player) {
        setActionDelay(player, 3);
    }

    public static Fire getFire(int logId) {
        for (Fire fire : Fire.values()) {
            if (fire.getLogId() == logId) {
                return fire;
            }
        }
        return null;
    }
}