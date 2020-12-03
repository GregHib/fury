package com.fury.game.content.skill.member.slayer;

import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class Slayer {

    public static int getLevelRequirement(String name) {
        for (SlayerTask task : SlayerTask.values()) {

            //Exceptions
            if(task.ordinal() >= SlayerTask.GENERAL_GRAARDOR.ordinal())//Bosses
                return 1;

            if(task == SlayerTask.LIVING_ROCK)
                return 1;

            if (name.toLowerCase().contains(task.toString().replace("_", " ").toLowerCase()))
                return task.getLevelRequired();
        }
        return 1;
    }

    public static boolean hasNosepeg(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 4168 || hasSlayerHelmet(target);
    }

    public static boolean hasEarmuffs(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 4166 || hat == 13277 || hasSlayerHelmet(target);
    }

    public static boolean hasMask(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 1506 || hat == 4164 || hat == 13277 || hasSlayerHelmet(target);
    }

    public static boolean hasWitchWoodIcon(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.AMULET).getId();
        return hat == 8923;
    }

    public static boolean hasHexcrest(Figure target) {
        if (!(target instanceof Player))
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 15488;
    }

    public static boolean hasSlayerHelmet(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 13263 || hat == 14636 || hat == 14637 || hasFullSlayerHelmet(target);
    }

    public static boolean hasFullSlayerHelmet(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 15492 || hat == 15496 || hat == 15497 || (hat >= 22528 && hat <= 22550);
    }

    public static boolean hasReflectiveEquipment(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int shieldId = targetPlayer.getEquipment().get(Slot.SHIELD).getId();
        return shieldId == 4156;
    }

    public static boolean hasSpinyHelmet(Figure target) {
        if (!target.isPlayer())
            return true;
        Player targetPlayer = (Player) target;
        int hat = targetPlayer.getEquipment().get(Slot.HEAD).getId();
        return hat == 4551 || hasSlayerHelmet(target);
    }

    public static boolean isUsingBell(final Player player) {
        player.getMovement().lock(3);
        player.animate(6083);
        /*List<GameObject> objects = World.getRegion(player.getRegionId()).getAllObjects();
        if (objects == null)
            return false;
        for (final GameObject object : objects) {
            if (!object.isWithinDistance(player, 3) || object.getId() != 22545)
                continue;
            player.message("The bell re-sounds loudly throughout the cavern.");
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    Npc npc = World.spawnNpc(5751, player, -1, true);
                    npc.getCombat().target(player);
                    GameObject o = new GameObject(object);
                    o.setId(22544);
                    World.spawnObjectTemporary(o, 30000);
                }
            }, 1);
            return true;
        }*/
        return false;
    }

    public static boolean isBlackMask(int requestedId) {
        return requestedId >= 8901 && requestedId <= 8920;
    }

    private static final int[] SLAYER_HELMET_PARTS = { 8921, 4166, 4164, 4551, 4168 };
    private static final int[] FULL_SLAYER_HELMET_PARTS = { 13263, 15490, 15488 };

    public static boolean createSlayerHelmet(Player player, int itemUsed, int itemUsedWith) {
        if (itemUsed == itemUsedWith)
            return false;
        boolean firstCycle = false, secondCycle = false, full = false;
        for (int parts : SLAYER_HELMET_PARTS) {
            if (itemUsed == parts)
                firstCycle = true;
            if (itemUsedWith == parts)
                secondCycle = true;
        }
        if (!firstCycle || !secondCycle) {
            firstCycle = false;
            secondCycle = false;
            for (int parts : FULL_SLAYER_HELMET_PARTS) {
                if (itemUsed == parts)
                    firstCycle = true;
                if (itemUsedWith == parts)
                    secondCycle = true;
            }
            full = true;
        }
        if (firstCycle && secondCycle) {
            if (!player.getSlayerManager().getLearnt()[SlayerRewards.Learn.CRAFT_HELMETS.ordinal()]) {
                player.message("You don't know what to do with these parts. You should talk to an expert, perhaps they know how to assemble these parts.");
                return false;
            } else if (player.getSkills().getLevel(Skill.CRAFTING) < 55) {
                player.message("You need a Crafting level of 55 in order to assemble a slayer helmet.");
                return false;
            }
            for (int parts : (full ? FULL_SLAYER_HELMET_PARTS : SLAYER_HELMET_PARTS))
                if (!player.getInventory().contains(new Item(parts, 1)))
                    return false;
            for (int parts : (full ? FULL_SLAYER_HELMET_PARTS : SLAYER_HELMET_PARTS))
                player.getInventory().delete(new Item(parts, 1));
            player.getInventory().add(new Item(full ? 15492 : FULL_SLAYER_HELMET_PARTS[0], 1));
            player.message(full ? "You attach two parts to your slayer helmet." : "You combine all parts of the helmet.");
            return true;
        }
        return false;
    }

    public static void dissasembleSlayerHelmet(Player player, boolean full) {
        if (!(player.getInventory().getSpaces() >= (full ? 2 : 4))) {
            player.message("You don't have enough space in your inventory to disassemble the helmet.");
            return;
        }
        player.getInventory().delete(new Item(full ? 15492 : 13263, 1));
        if (full) {
            for (int parts : FULL_SLAYER_HELMET_PARTS)
                player.getInventory().add(new Item(parts, 1));
        } else {
            for (int parts : SLAYER_HELMET_PARTS)
                player.getInventory().add(new Item(parts, 1));
        }
    }

    public static boolean isSlayerHelmet(Item item) {
        return item.getName().toLowerCase().contains("slayer helm");
    }
}
