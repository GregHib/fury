package com.fury.game.content.skill.member.hunter;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

import java.util.LinkedHashSet;

public class TrapAction extends Action {

    private Traps trap;
    private Position tile;

    public TrapAction(Traps trap, Position tile) {
        this.trap = trap;
        this.tile = tile;
    }

    public static Position getNetPosition(Position treePos, int rotation) {
        int x = 0;
        int y = 0;
        if(rotation == 0)y = 1;
        if(rotation == 2)y = -1;
        if(rotation == 1)x = 1;
        if(rotation == 3)x = -1;
        return treePos.copyPosition().add(x, y);
    }

    public static Position getTreePosition(Position netPos, int rotation) {
        int x = 0;
        int y = 0;
        if(rotation == 0)y = -1;
        if(rotation == 2)y = 1;
        if(rotation == 1)x = -1;
        if(rotation == 3)x = 1;
        return netPos.copyPosition().add(x, y);
    }

    @Override
    public boolean start(Player player) {
        boolean isItem = trap.isItem();
        int levelRequirement = trap.getRequirementLevel(), currentLevel = player.getSkills().getLevel(Skill.HUNTER);
        if (currentLevel < levelRequirement) {
            player.message("You need a Hunter level of " + levelRequirement + " in order to place this trap.");
            return false;
        } else {
            if (isItem) {
                if (ObjectManager.getObjectWithSlot(tile, Region.OBJECT_SLOT_FLOOR) != null) {
                    player.message("You cannot place a trap here!");
                    return false;
                }
            } else {
                Item[] items = trap.getRequiredItems();
                if (items != null && !player.getInventory().containsAmount(items)) {
                    player.message("You don't have the necessary supplies to place this trap.");
                    return false;
                }
            }
            int maxAmount = getMaximumTrap(trap, currentLevel);
            if (getTrapsCount(player, isItem) == maxAmount) {
                player.message("You cannot place more than " + maxAmount + " traps at once.");
                return false;
            }
        }
        player.getMovement().lock(3);
        player.perform(new Animation(trap.getStartAnimId()));
        player.message("You begin setting up the trap.", true);
        if (isItem)
            for (Item item : trap.getRequiredItems())
                FloorItemManager.addGroundItem(item, tile, player);
        Item[] toDelete = trap.getRequiredItems();
        if (toDelete != null)
            player.getInventory().delete(toDelete);
        setActionDelay(player, trap.isNet() ? 2 : 4);
        return true;
    }

    @Override
    public boolean process(Player player) {
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
        boolean isItem = trap.isItem();
        if (isItem) {
            if (!player.getMovement().addWalkSteps(player.getX() - 1, player.getY(), 1))
                if (!player.getMovement().addWalkSteps(player.getX() + 1, player.getY(), 1))
                    if (!player.getMovement().addWalkSteps(player.getX(), player.getY() + 1, 1))
                        player.getMovement().addWalkSteps(player.getX(), player.getY() - 1, 1);
            for (Item remove : trap.getRequiredItems()) {
                final FloorItem item = GameWorld.getRegions().get(tile.getRegionId()).getFloorItem(remove.getId(), tile, player);
                if (item == null)
                    return -1;
                else if (!FloorItemManager.removeGroundItem(player, item, false))
                    return -1;
            }
        }
        if(trap.isNet()) {
            GameObject object = ObjectManager.getStandardObject(tile);
            if(object == null)
                return -1;
            PrivateObjectManager.spawnPrivateObject(player, new GameObject(trap.getObjectId(), tile, 10, object.getDirection()), 300000);
            PrivateObjectManager.spawnPrivateObject(player, new GameObject(19651, getNetPosition(tile, object.getDirection()), 10, object.getDirection()), 300000);
        } else {
            PrivateObjectManager.spawnPrivateObject(player, new GameObject[] {
                    new GameObject(trap.getObjectId(), tile, 10, 0)
            }, new long[] {300000});
        }
        return -1;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

    private static int getMaximumTrap(Traps trap, int currentLevel) {
        if (trap.isItem())
            return 1 + (currentLevel / 20);
        return 3;
    }

    public static boolean isTrap(Player player, Position tile, int id) {
        for (Traps trap : Traps.values()) {
            boolean matches = false;

            if(trap.getStartObjectId() == id)
                matches = true;

            for (int i = 0; i < trap.getRequiredItems().length; i++) {
                Item required = trap.getRequiredItems()[i];
                if (required.getId() == id) {
                    matches = true;
                    break;
                }
            }
            if (!matches)
                continue;
            player.getActionManager().setAction(new TrapAction(trap, tile));
            return true;
        }
        return false;
    }

    public static boolean isTrap(Player player, GameObject object) {
        Traps trap = null;
        for (Traps t : Traps.values()) {
            boolean matches = false;
            if (object.getId() == t.getObjectId())
                matches = true;

            if (object.getId() == t.getFailObjectId())
                matches = true;

            if(t.isNet() && object.getId() == 19651)
                matches = true;

            if (matches) {
                trap = t;
                break;
            }
        }
        HunterData captured = null;
        if (trap == null) {
            for (HunterData npc : HunterData.values()) {
                if (object.getId() == npc.getObjectId() || (npc.getTrap().isNet() && object.getId() == 19651)) {
                    captured = npc;
                    trap = captured.getTrap();
                    break;
                }
            }
        }

        if (trap == null)
            return false;
        else if (!PrivateObjectManager.isPlayerObject(player, object)) {
            player.message("This isn't your trap!");
            return true;
        }

        if(trap.isNet() && object.getId() == 19651) {
            GameObject obj = ObjectManager.getStandardObject(getTreePosition(object, object.getDirection()));
            if(obj == null)
                return false;
            object = obj;
        }

        sendTrapAction(player, object, trap, captured);
        return true;
    }

    private static void sendTrapAction(final Player player, final GameObject o, final Traps trap, final HunterData captured) {
        if (player.getMovement().isLocked())
            return;
        player.getMovement().lock(3);
        player.perform(new Animation(trap.getEndAnimId()));
        GameWorld.schedule(trap.isNet() ? 1 : 2, () -> sendTrapRewards(player, o, trap, captured, true));
    }

    public static void sendTrapRewards(Player player, GameObject o, Traps trap, HunterData captured, boolean reward) {
        if(trap.isNet())
            PrivateObjectManager.deleteObject(player, getNetPosition(o, o.getDirection()));
        PrivateObjectManager.deleteObject(player, o);
        if (trap.giveItemsBack())
            player.getInventory().addSafe(trap.getRequiredItems());
        if(trap.getStartObjectId() != -1)
            ObjectManager.spawnObject(new GameObject(trap.getStartObjectId(), o.copyPosition(), o.getType(), o.getDirection()));
        if (captured != null && reward) {
            if(!player.isInDungeoneering())
                for (Item item : captured.getLoot())
                    player.getInventory().addSafe(item);
            player.getSkills().addExperience(Skill.HUNTER, captured.getExp());
        }
        if(reward && captured != null) {
            ChristmasEvent.giveSnowflake(player);
            StrangeRocks.handleStrangeRocks(player, Skill.HUNTER);

            if(captured == HunterData.SWAMP_LIZARD || captured == HunterData.ORANGE_SALAMANDER || captured == HunterData.RED_SALAMANDER || captured == HunterData.BLACK_SALAMANDER) {
                Achievements.finishAchievement(player, Achievements.AchievementData.CATCH_A_SALAMANDER);
            } else if(captured == HunterData.CRIMSON_SWIFT) {
                Achievements.finishAchievement(player, Achievements.AchievementData.CATCH_A_CRIMSON_SWIFT);
            } else if(captured == HunterData.GRENWALL) {
                Achievements.doProgress(player, Achievements.AchievementData.CATCH_500_GRENWALLS);
            }
        }
        player.message(reward ? captured != null ? "You've caught a " + Misc.formatPlayerNameForDisplay(captured.toString()) + "." : "You dismantle the trap." : "Your trap collapsed.", reward && captured == null);
    }

    private int getTrapsCount(Player player, boolean item) {
        LinkedHashSet set = new LinkedHashSet<Integer>();
        for (Traps t : Traps.values()) {
            if (t.isItem() != item)
                continue;
            if (item) {
                set.add(t.getObjectId());
                set.add(t.getFailObjectId());
            } else
                set.add(t.getStartObjectId());
        }
        for (HunterData npc : HunterData.values()) {
            if (npc.getTrap().isItem() != item)
                continue;
            set.add(npc.getObjectId());
        }
        int trapsCount = 0;
        for(Object i : set)
            trapsCount += PrivateObjectManager.getObjects(player, (int) i);
        return trapsCount;
    }
}
