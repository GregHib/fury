package com.fury.game.content.global.thievingguild;

import com.fury.cache.Revision;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectDirection;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectType;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ThievingGuild {
    private static ThievingMaster thievingMaster;
    private static Position thievingMasterSpawnChoordinates = new Position(4765, 5811, 0);
    ;
    static final int THIEVING_MASTER = 11275;
    private static boolean vaultOpen;
    private static boolean eventActive;
    private static Map<Long, Integer> receivedItems = new HashMap<>();
    private static final Map<Long, Integer> ITEMS_RECEIVED = new HashMap<>();
    private static final long TIME_TILL_VAULT_CLOSE = TimeUnit.MINUTES.toMillis(5);

    public static void init() {
        GameExecutorManager.slowExecutor.schedule(ThievingGuild::spawn, 5, TimeUnit.MINUTES);

    }

    private static void spawn() {
        cleanUp();
        eventActive = true;
        thievingMaster = new ThievingMaster(thievingMasterSpawnChoordinates);
        GameWorld.sendBroadcast(FontUtils.imageTags(535) + " The guild master has woken up.. The thieving guild event has started!", 0x6600ff);
        GameWorld.getPlayers().forEach(p -> p.getPacketSender().sendString(39164, "Thieving Guild Event: " + FontUtils.GREEN + "Active" + FontUtils.COL_END, Colours.ORANGE_2));
    }

    private static void cleanUp() {
        health = totalHealth;
        eventActive = false;
        vaultOpen = false;
        receivedItems.clear();
        ITEMS_RECEIVED.clear();
        if (thievingMaster != null)
            GameWorld.getMobs().remove(thievingMaster);
        GameWorld.getPlayers().forEach(p -> {
            if (isInVault(p.copyPosition())) p.moveTo(4764, 5793);
        });
        thievingMaster = null;
    }

    public static void disturbTheMaster(int amount) {
        if(!eventActive || thievingMaster == null)
            return;
        
        if (GameSettings.DEBUG) {
            reduceOpening(health);
        } else {
            reduceOpening(amount);
        }

        if (health % 200 == 0) {
            thievingMaster.forceChat("YOU BLIMEY HEATHENS STOP IT IN THEIR BEFORE I COME OUT!");
            GameWorld.getPlayers().forEach(p -> {
                if (isInThievingGuild(p.copyPosition()))
                    p.getPacketSender().sendMessage("We have " + health + " hanky points left to go!", 255);
            });
        }
        if (health <= 0) {
            thievingMaster.forceChat("THAT'S IT IM COMING OUT!");
            GameWorld.getPlayers().forEach(p -> {
                if (isInThievingGuild(p.copyPosition())) p.getPacketSender().sendMessage("THE VAULT IS OPEN!", 255);
            });
            openVault();
            resetHealth();
        }
    }

    public static boolean canDisturb() {
        return !vaultOpen && eventActive;
    }

    public static boolean canRecieveBonusAwards() {
        return vaultOpen && eventActive;
    }

    public static int getHealth() {
        return health;
    }

    public static Map<Long, Integer> getReceivedItems() {
        return receivedItems;
    }

    private static int health = 0;
    private static int totalHealth = 5000;

    private static void reduceOpening(int amount) {
        health -= amount;
    }

    private static void resetHealth() {
        health = totalHealth;
    }

    public static void openVault() {
        vaultOpen = true;
        GameObject closedVaultDoorRight;
        GameObject closedVaultDoorLeft;
        closedVaultDoorRight = new GameObject(52381, new Position(4765, 5806, 0), ObjectType.STRAIGHT_WALL, ObjectDirection.NORTH, Revision.RS2);
        closedVaultDoorLeft = new GameObject(52382, new Position(4764, 5806, 0), ObjectType.STRAIGHT_WALL, ObjectDirection.SOUTH, Revision.RS2);
        GameWorld.getPlayers().forEach(p -> {
            ObjectHandler.handleDoor(closedVaultDoorLeft, TIME_TILL_VAULT_CLOSE);
            ObjectHandler.handleDoor(closedVaultDoorRight, TIME_TILL_VAULT_CLOSE);
        });
        GameExecutorManager.slowExecutor.schedule(() -> {
            GameWorld.getPlayers().forEach(p -> {
                if (isInVault(p.copyPosition())) p.moveTo(4764, 5793);
            });
            GameWorld.getPlayers().forEach(p -> {
                p.getPacketSender().sendString(39164, "Thieving Guild Event: " + FontUtils.YELLOW + "Inactive" + FontUtils.COL_END, Colours.ORANGE_2);
                p.getPacketSender().sendMessage("The thieving guild event is now over!", 255);
            });
            eventActive = false;
            vaultOpen = false;
            GameExecutorManager.slowExecutor.schedule(ThievingGuild::spawn, 4, TimeUnit.HOURS);
        }, 5, TimeUnit.MINUTES);
    }

    public static boolean isInThievingGuild(Position tile) {
        return (tile.getX() >= 4736 && tile.getX() <= 4799 && tile.getY() >= 5759 && tile.getY() <= 5823);
    }

    public static boolean isInVault(Position tile) {
        return (tile.getX() >= 4758 && tile.getX() <= 4770 && tile.getY() >= 5807 && tile.getY() <= 5814);
    }

    public static void pickpocketObject(Player player, GameObject object) {
        int delay = 3;
        if (!player.getTimers().getPickpocketMark().elapsed((3 * 1000))) {
            player.message("You must wait at least " + delay + " seconds between every pickpocket.");
            return;
        }
        player.perform(new Animation(881));
        if (!isSuccessful(player)) {
            if (object.getDefinition().getName().contains("chest")) {
                player.message("You fail to crack the " + object.getDefinition().getName().toLowerCase() + ".", true);
            } else {
                player.message("You fail to pick the " + object.getDefinition().getName().toLowerCase() + "'s pocket.", true);
                player.getCombat().applyHit(new Hit(Misc.random(100, 200), HitMask.RED, CombatIcon.NONE));
            }
            player.animate(424);
            player.perform(new Graphic(80, 5, GraphicHeight.HIGH));
            player.message("You've been stunned.", true);
            player.perform(new Graphic(80, GraphicHeight.HIGH));
            player.getMovement().lock(4);
        } else {
            if (ThievingGuild.isInThievingGuild(player.copyPosition()) && ThievingGuild.canDisturb()) {
                if (!ThievingGuild.isInVault(player.copyPosition()))
                    ThievingGuild.disturbTheMaster(100);
            }
            if (object.getDefinition().getName().contains("chest")) {
                int toMoveToEnterance = Misc.random(40, 100);
                if (object.copyPosition().getY() > 5788)
                    player.moveTo(object.copyPosition().getX(), object.copyPosition().getY() - 2, 0);
                else
                    player.moveTo(object.copyPosition().getX(), object.copyPosition().getY() + 2, 0);
                setThievingAttempts(player, 1);
                if (getThievingAttempts(player) >= toMoveToEnterance) {
                    player.moveTo(4762, 5765, 0);
                    removeThievingAttempts(player);
                }
                TempObjectManager.spawnObjectTemporary(new GameObject(52297, object.getCentredPosition(), object.getObjectType(), object.getFace()), 1500 + Misc.getRandom(5000));
            }
            double totalXp = 150;
            if (hasThievingSuit(player))
                totalXp *= 1.025;
            player.getSkills().addExperience(Skill.THIEVING, totalXp);
            player.getInventory().add(new Item(995, 15000));
        }
        player.getTimers().getPickpocketMark().reset();
    }

    private static boolean isSuccessful(Player player) {
        int thievingLevel = player.getSkills().getLevel(Skill.THIEVING) + (player.getSkills().getLevel(Skill.THIEVING) == 1 ? 5 : 0);
        int increasedChance = getIncreasedChance(player);
        int level = Misc.getRandom(thievingLevel + increasedChance);
        double ratio = level / (Utils.random(20 + 5) + 1);
        if (Math.round(ratio * thievingLevel) < 20 / player.getAuraManager().getThievingAccurayMultiplier())
            return false;
        return true;
    }

    private static int getIncreasedChance(Player player) {
        int chance = 0;
        if (player.getEquipment().get(Slot.HANDS).getId() == 10075)
            chance += 12;
        if (Equipment.wearingArdyCloak(player, 2))
            chance += 15;
        return chance;
    }

    public static boolean hasThievingSuit(Player player) {
        return player.getEquipment().get(Slot.HEAD).getId() == 21482 && player.getEquipment().get(Slot.BODY).getId() == 21480 && player.getEquipment().get(Slot.LEGS).getId() == 21481 && player.getEquipment().get(Slot.FEET).getId() == 21483;
    }

    public static boolean canReceiveItems(Player player) {
        if (receivedItems.containsKey(player.getLongUsername())) {
            return receivedItems.get(player.getLongUsername()) < 2;
        } else {
            return true;
        }
    }

    public static void incrementReceiveItems(Player player) {
        receivedItems.merge(player.getLongUsername(), 1, Integer::sum);
    }

    private static final String KEY = "ThievingGuild";

    public static void removeThievingAttempts(Player player) {
        player.getTemporaryAttributes().remove(KEY);
    }

    public static void setThievingAttempts(Player player, int stage) {
        player.getTemporaryAttributes().put(KEY, stage);
    }

    public static int getThievingAttempts(Player player) {
        return player.getTemporaryAttributes().get(KEY) == null ? 0 : (Integer) player.getTemporaryAttributes().get(KEY);
    }

    public static boolean alreadyClaimed(int itemId, Player player) {
        Integer receivedItem = ITEMS_RECEIVED.get(player);
        return receivedItem != null && receivedItem == itemId;
    }

    public static boolean isEventActive() {
        return eventActive;
    }

    public static void claim(Item reward, Player player) {
        incrementReceiveItems(player);
        ITEMS_RECEIVED.put(player.getLongUsername(), reward.getId());
        player.getInventory().add(reward);
    }

    public static boolean isVaultOpen() {
        return vaultOpen;
    }
}
