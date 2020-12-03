package com.fury.game.content.controller.impl;

import com.fury.core.task.TickableTask;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.npcs.NexEntranceD;
import com.fury.game.content.global.config.ConfigConstants;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.smithing.SmithingData;
import com.fury.game.content.skill.member.agility.Agility;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GodWars extends Controller {
    public static final int EMPTY_SECTOR = -1, BANDOS = 0, ARMADYL = 1, SARADOMIN = 2, ZAMORAK = 3, ZAROS = 4;
    private static final int BANDOS_SECTOR = 4, ARMADYL_SECTOR = 5, SARADOMIN_SECTOR = 6, ZAMORAK_SECTOR = 7, ZAROS_PRE_CHAMBER = 8, ZAROS_SECTOR = 9;
    public static final Position[] CHAMBER_TELEPORTS =
            {
                    new Position(2864, 5354, 2),
                    new Position(2863, 5354, 2), // bandos
                    new Position(2839, 5296, 2),
                    new Position(2839, 5295, 2), // armadyl
                    new Position(2907, 5265),
                    new Position(2908, 5265), // saradomin
                    new Position(2925, 5331, 2),
                    new Position(2925, 5332, 2), // zamorak
            };

    private int[] killCount = new int[5];
    private long lastPrayerRecharge;
    private int sector;

    public static void setConfigs(Player player) {
        player.getConfig().send(ConfigConstants.GODWARS_SARADOMIN_ROCK2, 1, true);
        player.getConfig().send(ConfigConstants.GODWARS_SARADOMIN_ROCK1, 1, true);
        player.getConfig().send(ConfigConstants.GODWARS_ENTRANCE, 1, true);
    }

    @Override
    public void start() {
        sector = EMPTY_SECTOR;
        sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
        if (player.getWalkableInterfaceId() != 16210)
            player.send(new WalkableInterface(16210));
        refresh();
    }

    @Override
    public boolean logout() {
        setArguments(new Object[]{killCount, lastPrayerRecharge, sector});
        return false;
    }

    @Override
    public boolean login() {
        if (this.getArguments().length != 3) {
            player.moveTo(GameSettings.DEFAULT_POSITION);
            return true;
        }
        List<Double> numbers = (ArrayList) this.getArguments()[0];
        double[] doubles = numbers.stream().mapToDouble(Double::doubleValue).toArray();
        final int[] intArray = new int[doubles.length];
        for (int i = 0; i < intArray.length; ++i)
            intArray[i] = (int) doubles[i];
        killCount = intArray;
        lastPrayerRecharge = (long) (double) this.getArguments()[1];
        sector = (int) (double) this.getArguments()[2];
        sendInterfaces();
        return false;
    }

    @Override
    public boolean processObjectClick1(final GameObject object) {
        if (object.getId() == 57225) {
            player.getDialogueManager().startDialogue(new NexEntranceD());
            return false;
        } else if (object.getId() == 26287 || object.getId() == 26286 || object.getId() == 26288 || object.getId() == 26289) {
            if (lastPrayerRecharge >= Misc.currentTimeMillis()) {
                player.message("You must wait a total of 10 minutes before being able to recharge your prayer points.");
                return false;
            } else if (player.getCombat().getAttackedByDelay() >= Misc.currentTimeMillis()) {
                player.message("You cannot recharge your prayer while engaged in combat.");
                return false;
            }
            player.getSkills().restore(Skill.PRAYER, 10.0);
            player.animate(645);
            player.message("Your prayer points feel rejuvenated.");
            lastPrayerRecharge = 600000 + Misc.currentTimeMillis();
            return false;
        } else if (object.getId() == 57258) {
            if (sector == ZAROS) {
                player.message("The door will not open in this direction.");
                return false;
            }

            boolean hasCeremonial = hasFullCeremonial(player);
            int requiredKc = player.isDonor() ? 10 : 15;
            if (killCount[4] >= requiredKc || hasCeremonial) {
                if (hasCeremonial)
                    player.message("The door recognises your familiarity with the area and allows you to pass through.");
                if (killCount[4] >= requiredKc)
                    killCount[4] -= requiredKc;
                sector = ZAROS;
                player.getMovement().addWalkSteps(2900, 5203, -1, false);
                refresh();
            } else
                player.message("You don't have enough kills to enter the lair of Zaros.");
            return false;
        } else if (object.getId() == 57234) {
            if (!player.getSkills().hasRequirement(Skill.CONSTITUTION, 70, "cross this obstacle"))
                return false;
            final boolean travelingEast = sector == ZAROS_PRE_CHAMBER;
            player.animate(1133);
            final Position tile = new Position(2863 + (travelingEast ? 0 : -3), 5219, 0);
            player.setForceMovement(new ForceMovement(tile, 1, travelingEast ? ForceMovement.EAST : ForceMovement.WEST));
            GameWorld.schedule(1, () -> {
                sector = travelingEast ? ZAROS_SECTOR : ZAROS_PRE_CHAMBER;
                player.moveTo(tile);
                sendInterfaces();
            });
            return false;
        } else if (object.getId() == 75089) {
            if (sector == ZAROS_PRE_CHAMBER) {
                player.getDialogueManager().startDialogue(new SimpleMessageD(), "You pull out your key once more but the door doesn't respond.");
                return false;
            }
            if (player.getInventory().contains(new Item(20120, 1))) {
                player.message("You flash the key in front of the door");
                ObjectHandler.useStairs(player, 1133, new Position(2887, 5278), 1, 2, "...and a strange force flings you in.");
                sector = ZAROS_PRE_CHAMBER;
            } else
                player.getDialogueManager().startDialogue(new SimpleMessageD(), "You try to push the door open, but it wont budge.... It looks like there is some kind of key hole.");
            return false;
        } else if (object.getId() == 57256) {
            ObjectHandler.useStairs(player, -1, new Position(2855, 5222), 1, 2, "You climb down the stairs.");
            return false;
        } else if (object.getId() == 57260) {
            ObjectHandler.useStairs(player, -1, new Position(2887, 5276), 1, 2, "You climb up the stairs.");
            return false;
        } else if (object.getId() == 26293) {
            ObjectHandler.useStairs(player, 828, new Position(2913, 3741), 1, 2);
            player.getControllerManager().forceStop();
            return false;
        } else if (object.getId() == 26384) { // bandos
            if (!player.getInventory().contains(SmithingData.HAMMER)) {
                player.message("You look at the door but find no knob, maybe it opens some other way.");
                return false;
            }
            if (player.getSkills().getLevel(Skill.STRENGTH) < 70) {
                player.message("You attempt to hit the door, but realize that you are not yet experienced enough.");
                return false;
            }
            final boolean withinBandos = sector == BANDOS_SECTOR;
            if (!withinBandos)
                player.animate(7002);
            GameWorld.schedule(withinBandos ? 0 : 1, () -> {
                ObjectHandler.handleDoor(object, 1000);
                player.getMovement().addWalkSteps(withinBandos ? 2851 : 2850, 5333, -1, false);
                sector = withinBandos ? EMPTY_SECTOR : BANDOS_SECTOR;
            });
            return false;
        } else if (object.getId() == 26303) {
            if (!player.getTimers().getClickDelay().elapsed(1200))
                return false;
            if (!player.getSkills().hasRequirement(Skill.RANGED, 70, "swing across here"))
                return false;

            if (!player.hasItemOnThem(9419)) {
                player.message("You need a mith grapple to swing across here.");
                return false;
            } else {
                final boolean withinArmadyl = sector == ARMADYL_SECTOR;

                player.animate(789);
                GameWorld.schedule(2, () -> {
                    player.message("You use your mith grapple to navigate across to the other side.", true);
                    player.moveTo(new Position(2871, withinArmadyl ? 5279 : 5269, 2));
                    sector = withinArmadyl ? EMPTY_SECTOR : ARMADYL_SECTOR;
                });
                player.getTimers().getClickDelay().reset();
            }
            return false;
        } else if (object.getId() == 26439) {
            if (!player.getSkills().hasRequirement(Skill.CONSTITUTION, 70, "cross this river"))
                return false;
            if (!player.getTimers().getClickDelay().elapsed(1000))
                return false;
            final boolean withinZamorak = sector == ZAMORAK_SECTOR;
            player.message("You jump into the icy cold water..", true);
            player.getDirection().setDirection(withinZamorak ? Direction.SOUTH : Direction.NORTH);
            player.animate(3067);
            player.getMovement().lock();
            boolean running = player.getSettings().getBool(Settings.RUNNING);
            player.getSettings().set(Settings.RUNNING, false);
            player.getMovement().addWalkSteps(2885, withinZamorak ? 5333 : 5345, -1, false);
            GameWorld.schedule(new TickableTask(true) {
                @Override
                public void tick() {
                    if (getTick() == 1) {
                        player.graphic(68);
                        player.setSkillAnimation(new Animation(773));
                    } else if (player.getY() == (withinZamorak ? 5333 : 5343) || getTick() >= 12) {
                        player.getSettings().set(Settings.RUNNING, running);
                        player.setSkillAnimation(null);
                        player.getMovement().unlock();
                        player.message("You climb out of the water safely.", true);
                        sector = withinZamorak ? EMPTY_SECTOR : ZAMORAK_SECTOR;
                        player.getTimers().getClickDelay().reset();
                        stop();
                    }
                }
            });
            return false;
        } else if (object.getId() == 75462) {
            if (object.getX() == 2912 && (object.getY() == 5298 || object.getY() == 5299)) {
                sector = SARADOMIN_SECTOR;
                useAgilityStones(player, object, new Position(2915, object.getY(), 0), 70, 15239, 7);
            } else if (object.getX() == 2914 && (object.getY() == 5298 || object.getY() == 5299)) {
                sector = EMPTY_SECTOR;
                useAgilityStones(player, object, new Position(2911, object.getY(), 0), 70, 3378, 7);
            } else if ((object.getX() == 2919 || object.getX() == 2920) && object.getY() == 5278)
                useAgilityStones(player, object, new Position(object.getX(), 5275, 0), 70, 15239, 7);
            else if ((object.getX() == 2920 || object.getX() == 2919) && object.getY() == 5276)
                useAgilityStones(player, object, new Position(object.getX(), 5279, 0), 70, 3378, 7);
            return false;
        } else if (object.getId() >= 26425 && object.getId() <= 26428) {
            if (sector == -1) {
                player.message("The door cannot open in this direction, perhaps there is another way out.");
                return false;
            }
            int index = object.getId() - 26425;
            int requiredKc = 15;
            if (DonorStatus.isDonor(player, DonorStatus.SAPPHIRE_DONOR))
                requiredKc -= 3;
            if (DonorStatus.isDonor(player, DonorStatus.EMERALD_DONOR))
                requiredKc -= 2;
            if (DonorStatus.isDonor(player, DonorStatus.RUBY_DONOR))
                requiredKc -= 3;
            if (DonorStatus.isDonor(player, DonorStatus.DIAMOND_DONOR))
                requiredKc -= 2;
            if (DonorStatus.isDonor(player, DonorStatus.DRAGONSTONE_DONOR))
                requiredKc -= 3;
            if (DonorStatus.isDonor(player, DonorStatus.ONYX_DONOR))
                requiredKc -= 2;

            if (player.getRights().isOrHigher(PlayerRights.ADMINISTRATOR) || killCount[index] >= requiredKc) {
                Position tile = CHAMBER_TELEPORTS[index * 2];
                player.getMovement().addWalkSteps(tile.getX(), tile.getY(), -1, false);
                if (killCount[index] > 0)
                    killCount[index] -= requiredKc;
                sector = index;
                refresh();
            } else
                player.message("You don't have enough kills to enter the lair of the gods.");
            return false;
        }

        switch (object.getId()) {
            case 26298:
                player.moveTo(2920, 5276, 1);
                return false;
            case 26294:
                player.moveTo(2912, 5300, 2);
                sector = EMPTY_SECTOR;
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick2(GameObject object) {
        if (object.getId() == 26287 || object.getId() == 26286 || object.getId() == 26288 || object.getId() == 26289) {
            player.message("The god's pity you and allow you to leave the encampment.");
            ObjectHandler.useStairs(player, -1, CHAMBER_TELEPORTS[(sector * 2) + 1], 3, 4);
            sector += 4;
            player.getCombat().getHits().resetReceivedHits();
            return false;
        }
        switch (object.getId()) {
            case 26444:
                player.moveTo(2915, 5300, 1);
                sector = SARADOMIN_SECTOR;
                return false;
            case 26445:
                player.moveTo(2920, 5273);
                return false;
        }
        return true;
    }

    public void incrementKillCount(int index) {
        killCount[index]++;
        refresh();
    }

    public void resetKillCount(int index) {
        killCount[index] = 0;
        refresh();
        player.message("The power of all those you slew in the dungeon drains from your body.");
    }

    public void refresh() {
        player.getPacketSender().sendString(16216, String.valueOf(killCount[ARMADYL]));
        player.getPacketSender().sendString(16217, String.valueOf(killCount[BANDOS]));
        player.getPacketSender().sendString(16218, String.valueOf(killCount[SARADOMIN]));
        player.getPacketSender().sendString(16219, String.valueOf(killCount[ZAMORAK]));
//        player.getPacketSender().sendString(16220, String.valueOf(killCount[ZAROS]));
    }

    public static void useAgilityStones(final Player player, final GameObject object, final Position tile, int levelRequired, final int emote, final int delay) {
        if (!Agility.hasLevel(player, levelRequired))
            return;
        player.getDirection().faceObject(object);
        player.getMovement().addWalkSteps(object.getX(), object.getY());
        GameWorld.schedule(1, () -> {
            player.getCombat().getHits().resetReceivedHits();
            ObjectHandler.useStairs(player, emote, tile, delay, delay + 1);
        });
    }

    @Override
    public boolean sendDeath() {
        if (sector > EMPTY_SECTOR && sector <= ZAMORAK) {
            player.getMovement().lock(8);
            player.stopAll();
            player.appendDeath(CHAMBER_TELEPORTS[(sector * 2) + 1]);
            return false;
        }
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        player.getControllerManager().forceStop();
    }

    @Override
    public void forceClose() {
        player.send(new WalkableInterface(-1));
    }

    public static void passGiantBoulder(Player player, GameObject object, boolean liftBoulder) {
        if (player.getSkills().getLevel(liftBoulder ? Skill.STRENGTH : Skill.AGILITY) < 60) {
            player.message("You need a " + (liftBoulder ? "Strength" : "Agility") + " of 60 in order to " + (liftBoulder ? "lift" : "squeeze past") + " this boulder.");
            return;
        }
        boolean isReturning = player.getY() >= 3709;
        player.getDirection().face(new Position(2907, isReturning ? 3707 : 3713));
        if (liftBoulder)
            GameExecutorManager.slowExecutor.schedule(() -> World.sendObjectAnimation(object, new Animation(6980)), 1, TimeUnit.SECONDS);
        if (liftBoulder && isReturning)//No anim
            return;
        int baseAnimation = liftBoulder ? 3725 : isReturning ? 3465 : 3466;
        player.getSettings().set(Settings.RUN_ENERGY, 0);
        ObjectHandler.useStairs(player, isReturning ? baseAnimation-- : baseAnimation, new Position(player.getX(), player.getY() + (isReturning ? -4 : 4), 0), liftBoulder ? 10 : 5, liftBoulder ? 11 : 6, null, true);
    }

    private static boolean hasFullCeremonial(Player player) {
        Item helm = player.getEquipment().get(Slot.HEAD);
        Item chest = player.getEquipment().get(Slot.BODY);
        Item legs = player.getEquipment().get(Slot.LEGS);
        Item boots = player.getEquipment().get(Slot.FEET);
        Item gloves = player.getEquipment().get(Slot.HANDS);
        if (helm == null || chest == null || legs == null || boots == null || gloves == null)
            return false;
        return helm.getName().contains("Ancient ceremonial") && chest.getName().contains("Ancient ceremonial") && legs.getName().contains("Ancient ceremonial") && boots.getName().contains("Ancient ceremonial") && gloves.getName().contains("Ancient ceremonial");
    }
}
