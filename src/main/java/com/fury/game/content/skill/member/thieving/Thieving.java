package com.fury.game.content.skill.member.thieving;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Thieving {
    public static boolean isStall(GameObject object) {
        for (final Stalls stall : Stalls.values())
            if (stall.getObjectId() == object.getId())
                return true;
        return false;
    }

    public static void handleStalls(final Player player, final GameObject object) {
        if (player.getCombat().getAttackedBy() != null && player.getCombat().getAttackedByDelay() > Utils.currentTimeMillis()) {
            player.message("You can't do this while you're in combat.");
            return;
        }
        for (final Stalls stall : Stalls.values()) {
            if (stall.getObjectId() == object.getId()) {
                final GameObject emptyStall = new GameObject(object.getRevision() == Revision.PRE_RS3 ? stall == Stalls.WINE_STALL ? 2046 : stall == Stalls.SEED_STALL ? 2047 : stall.getReplaceObject() : stall.getReplaceObject(), object);

                if (!player.getSkills().hasRequirement(Skill.THIEVING, stall.getLevel(), "steal from this stall."))
                    return;

                if (player.getInventory().getSpaces() <= 0) {
                    player.getInventory().full();
                    return;
                }

                player.animate(832);
                player.getMovement().lock(2);
                int amount = stall.getAmount() == 1 ? 1 : Misc.getRandom(stall.getAmount()) + 1;
                Achievements.doProgress(player, Achievements.AchievementData.STEAL_100000_ITEMS, amount);
                int item = stall.getItem(Misc.getRandom(stall.getItems().length - 1));
                if (item == 1891)
                    Achievements.finishAchievement(player, Achievements.AchievementData.STEAL_A_CAKE);
                if (item == 1436 || item == 7936)
                    amount = 1;
                player.getInventory().add(new Item(item, amount));
                player.getSkills().addExperience(Skill.THIEVING, stall.getExperience());
                StrangeRocks.handleStrangeRocks(player, Skill.THIEVING);
                ChristmasEvent.giveSnowflake(player);
                checkGuards(player);
                GameWorld.schedule(2, () -> {
                    if (!ObjectManager.containsObjectWithId(object, object.getId()))
                        return;
                    if (stall.getReplaceObject() != -1)
                        TempObjectManager.spawnObjectTemporary(emptyStall, (int) (1500 * stall.getTime()));
                });
            }
        }
    }

    public static void checkGuards(Player player) {
        Mob guard = null;
        int lastDistance = -1;
        for (Mob mob : GameWorld.getRegions().getLocalNpcs(player)) {
            if (mob == null)
                continue;
            if (!mob.getName().toLowerCase().contains("guard") || mob.getCombat().isInCombat() || mob.getFinished() || !mob.isWithinDistance(player, 4) || !World.canMove(mob, player, mob.getSizeX(), mob.getSizeY()))
                continue;
            int distance = Misc.getDistance(mob.getX(), mob.getY(), player.getX(), player.getY());
            if (lastDistance == -1 || lastDistance > distance) {
                guard = mob;
                lastDistance = distance;
            }
        }
        if (guard != null && Misc.random(Equipment.wearingArdyCloak(player, 1) ? 10 : 5) == 0) {
            guard.forceChat("Hey, what do you think you are doing!");
            guard.setTarget(player);
        }
    }


    public static boolean pickDoor(Player player, GameObject object) {
        int delay = 3;
        if (!player.getTimers().getPickpocketMark().elapsed((3 * 1000))) {
            player.message("You must wait at least " + delay + " seconds between every pickpocket.");
            return false;
        }
        player.getTemporaryAttributes().putIfAbsent("numbFingers", 0);
        player.animate(2246);
        int thievingLevel = player.getSkills().getLevel(Skill.THIEVING);
        int increasedChance = getIncreasedChance(player);
        int decreasedChance = (Integer) player.getTemporaryAttributes().get("numbFingers");
        int level = Misc.getRandom(thievingLevel + (increasedChance - decreasedChance)) + 1;
        double ratio = level / (Misc.getRandom(45 + 5) + 1);
        if (Math.round(ratio * thievingLevel) < (player.getCombat().getAttackedByDelay() > 0 ? 50 : 40) / player.getAuraManager().getThievingAccurayMultiplier()) {
            player.message("You fail to unlock the door and your hands begin to numb down.", true);
            player.getTemporaryAttributes().put("numbFingers", decreasedChance + 1);
            player.animate(424);
            player.perform(new Graphic(80, 5, GraphicHeight.HIGH));
            player.message("You've been stunned.", true);
            player.perform(new Graphic(80, GraphicHeight.HIGH));
            player.getMovement().lock(4);
            return false;
        }
        player.message("You successfully unlock the door.", true);
        if (ThievingGuild.isInThievingGuild(player)) {
            double totalXp = 25;
            if (ThievingGuild.hasThievingSuit(player))
                totalXp *= 1.025;
            player.getSkills().addExperience(Skill.THIEVING, totalXp);
        }
        player.getTimers().getPickpocketMark().reset();
        ObjectHandler.handleDoor(object, 1500 + Misc.getRandom(5000));
        return true;
    }

    private static int getIncreasedChance(Player player) {
        int chance = 0;
        if (player.getEquipment().get(Slot.HANDS).getId() == 10075)
            chance += 12;
        if (player.getEquipment().get(Slot.CAPE).getId() == 15349)
            chance += 15;
        return chance;
    }
}
