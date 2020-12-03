package com.fury.game.content.controller.impl;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.hunter.ButterflyData;
import com.fury.game.content.skill.member.hunter.ImpData;
import com.fury.game.entity.character.player.actions.ForceMovement;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

public class PuroPuro extends Controller {
    public static final int[][] implings = {
            /**
             * Baby imps
            */
            {6055, 2612, 4318},
            {6055, 2602, 4314},
            {6055, 2610, 4338},
            {6055, 2582, 4344},
            {6055, 2578, 4344},
            {6055, 2568, 4311},
            {6055, 2583, 4295},
            {6055, 2582, 4330},
            {6055, 2600, 4303},
            {6055, 2611, 4301},
            {6055, 2618, 4329},

            /**
             * Young imps
            */
            {6056, 2591, 4332},
            {6056, 2600, 4338},
            {6056, 2595, 4345},
            {6056, 2610, 4327},
            {6056, 2617, 4314},
            {6056, 2619, 4294},
            {6056, 2599, 4294},
            {6056, 2575, 4303},
            {6056, 2570, 4299},

            /**
             * Gourment imps
            */
            {6057, 2573, 4339},
            {6057, 2567, 4328},
            {6057, 2593, 4297},
            {6057, 2618, 4305},
            {6057, 2605, 4316},
            {6057, 2596, 4333},

            /**
             * Earth imps
            */
            {6058, 2592, 4338},
            {6058, 2611, 4345},
            {6058, 2617, 4339},
            {6058, 2614, 4301},
            {6058, 2606, 4295},
            {6058, 2581, 4299},

            /**
             * Essence imps
            */
            {6059, 2602, 4328},
            {6059, 2608, 4333},
            {6059, 2609, 4296},
            {6059, 2581, 4304},
            {6059, 2570, 4318},

            /**
             * Eclectic imps
            */
            {6060, 2611, 4310},
            {6060, 2617, 4319},
            {6060, 2600, 4347},
            {6060, 2570, 4326},
            {6060, 2579, 4310},

            /**
             * Spirit imps
            */

            /**
             * Nature imps
            */
            {6061, 2581, 4310},
            {6061, 2581, 4310},
            {6061, 2603, 4333},
            {6061, 2576, 4335},
            {6061, 2588, 4345},

            /**
             * Magpie imps
            */
            {6062, 2612, 4324},
            {6062, 2602, 4323},
            {6062, 2587, 4348},
            {6062, 2564, 4320},
            {6062, 2566, 4295},

            /**
             * Ninja imps
            */
            {6063, 2570, 4347},
            {6063, 2572, 4327},
            {6063, 2578, 4318},
            {6063, 2610, 4312},
            {6063, 2594, 4341},

            /**
             * Dragon imps
            */
            {6064, 2613, 4341},
            {6064, 2585, 4337},
            {6064, 2576, 4319},
            {6064, 2576, 4294},
            {6064, 2592, 4305},

    };

    @Override
    public void start() {
//		player.getPacketSender().sendBlackOut(2);
    }

    @Override
    public void forceClose() {
//		player.getPacketSender().sendBlackOut(0);
    }

    @Override
    public void magicTeleported(int type) {
        player.getControllerManager().forceStop();
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 25014:
                player.getControllerManager().forceStop();
                TeleportHandler.teleportPlayer(player, new Position(2427, 4446), TeleportType.PURO_PURO);
                return true;
        }
        return true;//No other objects are in use
    }

    @Override
    public boolean processObjectClick5(GameObject object) {
        switch (object.getId()) {
            case 25029:
            case 25016:
                PuroPuro.pushThrough(player, object);
                return true;
        }
        return true;
    }

    public static void spawn() {
        for (int i = 0; i < implings.length; i++)
            GameWorld.getMobs().spawn(implings[i][0], Revision.RS2, new Position(implings[i][1], implings[i][2]));

        /**
         * Kingly imps
         * Randomly spawned
         */
        int random = Misc.getRandom(6);
        Position pos = new Position(2596, 4351);
        switch (random) {
            case 1:
                pos = new Position(2620, 4348);
                break;
            case 2:
                pos = new Position(2607, 4321);
                break;
            case 3:
                pos = new Position(2588, 4289);
                break;
            case 4:
                pos = new Position(2576, 4305);
                break;
        }
        GameWorld.getMobs().spawn(7903, Revision.RS2, pos);
    }

    /**
     * Catches an Impling
     *
     * @param player The player catching an Imp
     * @param imp    The Npc (Impling) to catch
     */
    public static void catchImpling(Player player, final Mob imp) {
        ImpData implingData = ImpData.forId(imp.getId());
        if (player.getInterfaceId() > 0 || player == null || imp == null || implingData == null || !imp.isRegistered() || !player.getTimers().getClickDelay().elapsed(2000))
            return;

        if (!player.getSkills().hasRequirement(Skill.HUNTER, implingData.levelReq, "catch this impling"))
            return;

        if (player.getEquipment().get(Slot.WEAPON).getId() != 10010 && player.getEquipment().get(Slot.WEAPON).getId() != 11259) {
            player.message("You do not have a net equipped to catch this impling with.");
            return;
        }
        if (!player.getInventory().contains(new Item(11260))) {
            player.message("You do not have any empty jars to hold this impling with.");
            return;
        }
        player.animate(6605);
        boolean success = player.getSkills().getLevel(Skill.HUNTER) <= 8 || Misc.getRandom(player.getSkills().getLevel(Skill.HUNTER) / 2) > 1;
        if (success) {
            if (imp.isRegistered()) {
                imp.sendDeath(player);
                player.getInventory().delete(new Item(11260));
                player.getInventory().addSafe(new Item(implingData.impJar));
                player.message("You successfully catch the impling.", true);
                player.getSkills().addExperience(Skill.HUNTER, implingData.XPReward);
            }
        } else
            player.message("You failed to catch the impling.", true);
        player.getTimers().getClickDelay().reset();
    }

    /**
     * Catches an Impling
     *
     * @param player    The player catching an Imp
     * @param butterfly The Npc (Butterfly) to catch
     */
    public static void catchButterfly(Player player, final Mob butterfly) {
        ButterflyData butterflyData = ButterflyData.forId(butterfly.getId());
        if (player.getInterfaceId() > 0 || player == null || butterfly == null || butterflyData == null || !butterfly.isRegistered() || !player.getTimers().getClickDelay().elapsed(2000))
            return;
        if (!player.getSkills().hasRequirement(Skill.HUNTER, butterflyData.levelReq, "to catch this butterfly"))
            return;

        if (player.getEquipment().get(Slot.WEAPON).getId() != 10010 && player.getEquipment().get(Slot.WEAPON).getId() != 11259) {
            player.message("You do not have a net equipped to catch this butterfly with.");
            return;
        }
        if (!player.getInventory().contains(new Item(10012))) {
            player.message("You do not have any empty jars to hold this butterfly with.");
            return;
        }
        player.animate(6605);
        boolean success = player.getSkills().getLevel(Skill.HUNTER) <= 8 || (Misc.getRandom(player.getSkills().getLevel(Skill.HUNTER)) + (player.getEquipment().get(Slot.WEAPON).getId() == 11259 ? 10 : 0) / 2) > 1;
        if (success) {
            if (butterfly.isRegistered()) {
                butterfly.sendDeath(player);
                player.getInventory().delete(new Item(10012));
                player.getInventory().addSafe(new Item(butterflyData.butterflyJar));
                player.message("You successfully catch the butterfly.", true);
                player.getSkills().addExperience(Skill.HUNTER, butterflyData.XPReward);
            }
        } else
            player.message("You failed to catch the butterfly.", true);
        player.getTimers().getClickDelay().reset();
    }

    public static void pushThrough(final Player player, GameObject object) {
        int objectX = object.getX();
        int objectY = object.getY();
        int direction;
        if (player.getX() == objectX && player.getY() < objectY) {
            objectY = objectY + 1;
            direction = ForceMovement.NORTH;
        } else if (player.getX() == objectX && player.getY() > objectY) {
            objectY = objectY - 1;
            direction = ForceMovement.SOUTH;
        } else if (player.getY() == objectY && player.getX() < objectX) {
            objectX = objectX + 1;
            direction = ForceMovement.EAST;
        } else if (player.getY() == objectY && player.getX() > objectX) {
            objectX = objectX - 1;
            direction = ForceMovement.WEST;
        } else {
            objectY = objectY - 1;
            objectX = objectX + 1;
            direction = ForceMovement.SOUTH | ForceMovement.EAST;
        }
        boolean faster = Misc.getRandom(2) == 0;
        player.message(faster ? "You use your strength to push through the wheat in the most efficient fashion." : "You use your strength to push through the wheat.");
        player.getDirection().face(object);
        player.animate(6594);
        player.getMovement().lock();
        final Position tile = new Position(objectX, objectY, 0);
        player.getDirection().face(object);
        player.setForceMovement(new ForceMovement(tile, faster ? 4 : 6, direction));
        GameWorld.schedule(faster ? 4 : 6, () -> {
            player.getMovement().unlock();
            player.moveTo(tile);
        });
    }

}
