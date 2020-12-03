package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.objects.OfferingStoneD;
import com.fury.game.content.global.config.ConfigConstants;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.firemaking.bonfire.Firepit;
import com.fury.game.content.skill.free.woodcutting.Hatchet;
import com.fury.game.content.skill.free.woodcutting.Trees;
import com.fury.game.content.skill.free.woodcutting.Woodcutting;
import com.fury.game.entity.character.combat.effects.EffectType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.entity.object.TempObject;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Logger;
import com.fury.util.Misc;

import java.util.TimerTask;

public class JadinkoLair extends Controller { // 10, 13, 50

    public static int playersCount;
    private static final int[] JADE_ROOTS = {12290, 12291, 12272, 12274};
    private static final Position[] JADE_WORLDTILE =
            {
                    new Position(3053, 9239),
                    new Position(3055, 9246),
                    new Position(3056, 9250),
                    new Position(3061, 9240),
                    new Position(3048, 9233),
                    new Position(3044, 9237),
                    new Position(3036, 9241),
                    new Position(3026, 9237),
                    new Position(3023, 9232),
                    new Position(3035, 9233),
                    new Position(3035, 9233),
                    new Position(3035, 9233),
                    new Position(3033, 9231),
                    new Position(3033, 9231),
                    new Position(3023, 9229),
                    new Position(3020, 9243),
                    new Position(3015, 9252),
                    new Position(3024, 9252),
                    new Position(3023, 9257),
                    new Position(3040, 9262),
                    new Position(3041, 9268),
                    new Position(3046, 9268),
                    new Position(3046, 9268),
                    new Position(3046, 9268),
                    new Position(3053, 9249),
                    new Position(3058, 9251),
                    new Position(3058, 9251),
                    new Position(3064, 9238),
                    new Position(3059, 9241),
                    new Position(3056, 9237),
                    new Position(3060, 9243),
                    new Position(3026, 9234),
                    new Position(3038, 9237),
                    new Position(3041, 9263),
                    new Position(3040, 9269),
                    new Position(3047, 9265),
                    new Position(3046, 9273),
                    new Position(3045, 9270),
                    new Position(3044, 9263),
                    new Position(3043, 9259),
                    new Position(3037, 9265),
                    new Position(3040, 9260),
                    new Position(3043, 9264),
                    new Position(3021, 9257),
                    new Position(3017, 9259),
                    new Position(3015, 9263),
                    new Position(3011, 9261),
                    new Position(3017, 9251),
                    new Position(3020, 9252),
                    new Position(3020, 9256),
                    new Position(3021, 9260),
                    new Position(3024, 9257),
                    new Position(3021, 9252),
                    new Position(3022, 9238),
                    new Position(3021, 9234),
                    new Position(3020, 9229),
                    new Position(3028, 9232),
                    new Position(3026, 9236),
                    new Position(3022, 9236),
                    new Position(3026, 9236),
                    new Position(3027, 9233),
                    new Position(3029, 9231),
                    new Position(3028, 9237),
                    new Position(3030, 9236),
                    new Position(3033, 9239),
                    new Position(3034, 9235),
                    new Position(3035, 9237),
                    new Position(3036, 9232),
                    new Position(3036, 9237),
                    new Position(3036, 9241),
                    new Position(3041, 9236),
                    new Position(3040, 9241),
                    new Position(3043, 9229),
                    new Position(3045, 9234),
                    new Position(3041, 9238),
                    new Position(3047, 9234),
                    new Position(3042, 9243),
                    new Position(3054, 9238),
                    new Position(3055, 9244),
                    new Position(3054, 9249),
                    new Position(3058, 9249),
                    new Position(3059, 9245),
                    new Position(3063, 9241),
                    new Position(3062, 9237),
                    new Position(3059, 9240),
                    new Position(3064, 9242),
                    new Position(3062, 9246),
                    new Position(3063, 9236),
                    new Position(3041, 9265),
                    new Position(3042, 9261),
                    new Position(3045, 9262),
                    new Position(3048, 9266),
                    new Position(3042, 9271)};

    public static void init() {
        GameExecutorManager.fastExecutor.schedule(new JadinkoTimer(), 600, 600);
        for (Position tile : JADE_WORLDTILE) {
            if (Misc.random(2) == 0)
                continue;
            createJadeRoot(JADE_ROOTS[Misc.random(JADE_ROOTS.length)], tile, true);
        }
    }

    static class JadinkoTimer extends TimerTask {

        int ticks;

        @Override
        public void run() {
            try {
                ticks++;
                if (ticks % 10 == 0) {
                    if (playersCount != 0) {
                        for (int i = 0; i < Misc.random(Misc.random(JADE_ROOTS.length)); i++)
                            createJadeRoot(JADE_ROOTS[Misc.random(JADE_ROOTS.length)], JADE_WORLDTILE[Misc.random(JADE_WORLDTILE.length)], false);
                    }
                }
            } catch (Throwable e) {
                Logger.handle(e);
            }
        }
    }

    private static void createJadeRoot(int id, Position tile, boolean permanent) {
        if (ObjectManager.getStandardObject(tile) != null)
            return;
        GameObject o = new GameObject(id, tile, 10, 0);
        if (permanent)
            ObjectManager.spawnObject(o);
        else
            TempObjectManager.spawnObjectTemporary(o, Misc.random(10000, 30000));
    }

    @Override
    public boolean handleItemOnObject(GameObject object, Item item) {
        switch (object.getId()) {
            case 12286:
                Firepit.addRoot(player, object, item);
                return item.getId() != 21350;
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(final GameObject object) {
        switch (object.getId()) {
            case 12284:
                if (!player.hasItemOnThem(590)) {
                    player.message("You need a tinderbox in order to light this.");
                    return false;
                }

                if (!player.hasItemOnThem(21350)) {
                    player.message("You need a curly root in order to light this.");
                    return false;
                }

                if (!player.getSkills().hasRequirement(Skill.FIREMAKING, 83, "light this"))
                    return false;

                player.getMovement().lock(5);
                player.animate(827);
                GameWorld.schedule(new Task(true, 1) {
                    private byte ticks = 0;

                    @Override
                    public void run() {
                        ticks++;
                        if (ticks == 2) {
                            player.getInventory().delete(new Item(21350));
                            TempObjectManager.spawnObjectTemporary(new GameObject(object.getId() + 1, object, object.getType(), object.getDirection()), 1000);//Should be 2000 (change to fix with broken ticks)
                        } else if (ticks == 3) {
                            player.animate(16700);
                        } else if (ticks == 6) {
                            TempObjectManager.spawnTemporaryObject(new TempObject(object.getId() + 2, object, object.getType(), object.getDirection(), 30000), false, true);
                            player.getSkills().addExperience(Skill.FIREMAKING, 378.7);
                            JadinkoLair.addPoints(player, 3);
                            stop();
                        }
                    }
                });
                return false;
            case 12327:
                player.getControllerManager().forceStop();
                ObjectHandler.useStairs(player, -1, new Position(2948, 2955), 1, 2);
                return false;
            case 12321:
                if (player.getConfig().get(ConfigConstants.JADINKO_LAIR_ROOT) == 0) {
                    if (!player.getSkills().hasRequirement(Skill.WOODCUTTING, 90, "chop these roots"))
                        return false;

                    Hatchet hatch = Woodcutting.getHatchet(player, false);
                    if (hatch == null) {
                        player.message("You don't have a hatchet to chop this with.");
                        return false;
                    }
                    player.perform(new Animation(hatch.getIvyEmoteId()));
                    GameWorld.schedule(3, () -> {
                        player.getConfig().send(ConfigConstants.JADINKO_LAIR_ROOT, 1, true);
                        player.message("You chop away the roots to discover a passage.");
                    });
                } else {
                    ObjectHandler.useStairs(player, 11042, new Position(2946, 2886), 2, 3);
                    GameWorld.schedule(1, () -> {
                        player.getDirection().face(new Position(2946, 2887));
                        player.animate(11043);
                        player.getControllerManager().forceStop();
                    });
                }
                return false;
            case 12290:
            case 12272:
                player.getActionManager().setAction(new Woodcutting(object, Trees.STRAIT_ROOT));
                return false;
            case 12277:
                player.getActionManager().setAction(new Woodcutting(object, Trees.STRAIT_ROOT_CUT));
                return false;
            case 12291:
                player.getActionManager().setAction(new Woodcutting(object, Trees.MUTATED_ROOT));
                return false;
            case 12274:
                player.getActionManager().setAction(new Woodcutting(object, Trees.CURLY_ROOT));
                return false;
            case 12279:
                player.getActionManager().setAction(new Woodcutting(object, Trees.CURLY_ROOT_CUT));
                return false;
            case 12286:
                Firepit.addRoots(player, object);
                return false;
            case 12313:
                player.getDialogueManager().startDialogue(new OfferingStoneD(), false);
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick2(GameObject object) {
        switch (object.getId()) {
            case 12313:
                player.getDialogueManager().startDialogue(new OfferingStoneD(), true);
                return false;
        }
        return true;
    }

    public void refreshFavourPoints() {
        int points = player.getPoints().getInt(Points.FAVOUR);
        player.getPacketSender().sendString(715, 1, points + "/2000", points < 2000 ? 0xff981f : 0x790000);
    }

    @Override
    public void start() {
        playersCount++;
        sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
        player.send(new WalkableInterface(715));
        refreshFavourPoints();
    }

    @Override
    public void magicTeleported(int teleType) {
        player.getControllerManager().forceStop();
    }

    @Override
    public boolean logout() {
        playersCount--;
        return false;
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public void forceClose() {
        player.message("As you move away from the vine, your fruits lose their magic and");
        player.message("start behaving like common fruits.");
        player.getEffects().removeEffectsWithAction(EffectType.FRUIT);
        playersCount--;
        player.send(new WalkableInterface(-1));
    }


    public static void addPoints(Player player, int amount) {
        int points = player.getPoints().getInt(Points.FAVOUR);
        points += amount;

        if (points > 2000) {
            points = 2000;
            player.message("The offering stone is full!");
            player.message("The jadinkos won't deposit any more rewards until you have taken some.");
        }
        player.getPoints().set(Points.FAVOUR, points);
        if (player.getControllerManager().getController() instanceof JadinkoLair)
            ((JadinkoLair) player.getControllerManager().getController()).refreshFavourPoints();
    }


    public static void removePoints(Player player, int amount) {
        int points = player.getPoints().getInt(Points.FAVOUR);
        points -= amount;
        player.getPoints().set(Points.FAVOUR, points < 0 ? 0 : points);
        if (player.getControllerManager().getController() instanceof JadinkoLair)
            ((JadinkoLair) player.getControllerManager().getController()).refreshFavourPoints();
    }
}
