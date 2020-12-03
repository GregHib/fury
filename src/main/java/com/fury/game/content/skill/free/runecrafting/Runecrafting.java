package com.fury.game.content.skill.free.runecrafting;

import com.fury.cache.def.Loader;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.util.Misc;

/**
 * Handles the Runecrafting skill
 *
 * @author Gabriel Hannason
 */
public class Runecrafting {

    public static void imbueStaff(final Player player, StaffImbuing type, int amount) {
        if (type == null)
            return;

        if (!player.getSkills().hasRequirement(Skill.RUNECRAFTING, type.getLevelReq(), "craft this"))
            return;

        Item staff = new Item(type.getStaffId());
        if (!player.getInventory().contains(staff)) {
            player.message("You need a " + Loader.getItem(type.getStaffId()).getName() + " to craft this.");
            return;
        }
        if (!player.getTimers().getClickDelay().elapsed(4500))
            return;

        player.animate(13662);

        player.getInventory().delete(staff);
        player.getInventory().add(new Item(type.getImbuedId()));
        player.getSkills().addExperience(Skill.RUNECRAFTING, type.getExp());
        player.getSkills().addExperience(Skill.MAGIC, type.getExp());
    }

    public static void craftRunes(final Player player, RuneData rune) {
        craftRunes(player, rune, 0, false);
    }

    public static void craftRunes(final Player player, RuneData rune, int amount, boolean dungeoneering) {
        if (!canRuneCraft(player, rune))
            return;
        int essence = -1;
        if (player.getInventory().contains(new Item(1436)) && !rune.pureRequired())
            essence = 1436;
        if (player.getInventory().contains(new Item(7936)) && essence < 0)
            essence = 7936;
        if (player.getInventory().contains(new Item(17776)))
            essence = 17776;
        if (essence == -1)
            return;

        double experience = 0;
        int esseUsed = 0;
        int amountMade = 0;
        if (dungeoneering) {
            player.animate(13659);
            player.graphic(2571);
            int amountToMake = amount;
            player.getInventory().delete(new Item(essence, amountToMake));
            esseUsed = amountToMake;
            player.getInventory().add(new Item(rune.getDungRuneID(), amountToMake));
            amountMade = amountToMake;
            experience = rune.getXP() * 0.02;
        } else {
            player.graphic(186);
            player.animate(791);
            int amountToMake = RunecraftingData.getMakeAmount(rune, player);
            for (int i = 28; i > 0; i--) {
                if (!player.getInventory().contains(new Item(essence)))
                    break;
                player.getInventory().delete(new Item(essence));
                esseUsed += 1;
                player.getInventory().add(new Item(rune.getRuneID(), amountToMake));
                amountMade += amountToMake;
                experience = rune.getXP();
            }
        }
        player.getSkills().addExperience(Skill.RUNECRAFTING, experience * esseUsed);
        StrangeRocks.handleStrangeRocks(player, Skill.RUNECRAFTING);

        if (rune == RuneData.EARTH_RUNE) {
            Achievements.finishAchievement(player, AchievementData.CRAFT_AN_EARTH_RUNE);
        } else if (rune == RuneData.BLOOD_RUNE) {
            Achievements.doProgress(player, AchievementData.CRAFT_6000_BLOOD_RUNES, amountMade);
        } else if (rune == RuneData.NATURE_RUNE) {
            Achievements.doProgress(player, AchievementData.CRAFT_50_NATURE_RUNES, amountMade);
        }
        ChristmasEvent.giveSnowflake(player);
        player.message("You bind the altar's power into " + rune.getName() + "s..", true);
        player.getTimers().getClickDelay().reset();
    }

    public static void craftZMI(final Player player) {
        RuneData rune = RuneData.ZMI;
        if (!canRuneCraft(player, rune))
            return;
        int essence = -1;
        if (player.getInventory().contains(new Item(1436)) && !rune.pureRequired())
            essence = 1436;
        if (player.getInventory().contains(new Item(7936)) && essence < 0)
            essence = 7936;
        if (essence == -1)
            return;
        player.graphic(186);
        player.animate(791);
        double experience = 0;
        for (int i = 28; i > 0; i--) {
            if (!player.getInventory().contains(new Item(essence)))
                break;
            rune = RuneData.values()[Misc.randomMinusOne(RuneData.values().length - 1)];//Minus one because of RuneData.ZMI
            int amountToMake = RunecraftingData.getMakeAmount(rune, player);
            player.getInventory().delete(new Item(essence));
            player.getInventory().add(new Item(rune.getRuneID(), amountToMake));
            experience += rune.getXP() * 2;
            if (rune == RuneData.BLOOD_RUNE) {
                if (Equipment.wearingMorytaniaLegs(player, 4))
                    if (Misc.random(100) < 10)
                        player.getInventory().add(new Item(rune.getRuneID(), 1));
                Achievements.doProgress(player, AchievementData.CRAFT_6000_BLOOD_RUNES, amountToMake);
            } else if (rune == RuneData.NATURE_RUNE) {
                Achievements.doProgress(player, AchievementData.CRAFT_50_NATURE_RUNES, amountToMake);
            } else if (rune == RuneData.EARTH_RUNE) {
                Achievements.finishAchievement(player, AchievementData.CRAFT_AN_EARTH_RUNE);
            }
        }
        StrangeRocks.handleStrangeRocks(player, Skill.RUNECRAFTING);
        player.getSkills().addExperience(Skill.RUNECRAFTING, experience);
        player.message("You bind the altar's power into " + rune.getName() + "s..", true);
        Achievements.finishAchievement(player, AchievementData.CRAFT_RUNES_USING_ZMI);
        player.getTimers().getClickDelay().reset();
    }

    public static void handleTalisman(Player player, Item item) {
        //Doesn't handle if ruins is in different location
        //Or if already in the alter
        TalismanData talisman = TalismanData.forId(item.getId());
        if (talisman == null)
            return;
        if (talisman.getRuinsLocation() == null) {
            player.message("The talisman is having trouble pin-pointing the location.");
            return;
        }

        int posX = talisman.getRuinsLocation().getX();
        int posY = talisman.getRuinsLocation().getY();

        String directionY = null, directionX = null;
        if (posY > player.getY()) {
            directionY = "North";
        } else if (posY < player.getY()) {
            directionY = "South";
        }
        if (posX > player.getX()) {
            directionX = "East";
        } else if (posX < player.getX()) {
            directionX = "West";
        }
        player.message("The talisman is pulling towards the " + directionY + (directionX != null && directionY != null ? "-" : "") + directionX + ".");
    }

    public static boolean canRuneCraft(Player player, RuneData rune) {
        if (rune == null)
            return false;

        if (!player.getSkills().hasRequirement(Skill.RUNECRAFTING, rune.getLevelRequirement(), "craft this"))
            return false;

        if (rune.pureRequired() && !player.getInventory().contains(new Item(7936)) && !player.getInventory().contains(new Item(1436)) && !player.getInventory().contains(new Item(17776))) {
            player.message("You do not have any Pure essence in your inventory.");
            return false;
        } else if (rune.pureRequired() && !player.getInventory().contains(new Item(7936)) && !player.getInventory().contains(new Item(17776)) && player.getInventory().contains(new Item(1436))) {
            player.message("Only Pure essence has the power to bind this altar's energy.");
            return false;
        }

        if (!player.getInventory().contains(new Item(7936)) && !player.getInventory().contains(new Item(1436)) && !player.getInventory().contains(new Item(17776))) {
            player.message("You do not have any essence in your inventory.");
            return false;
        }
        return player.getTimers().getClickDelay().elapsed(4500);
    }

    public static boolean runecraftingAltar(Player player, int ID) {
        return ID >= 2478 && ID < 2489 || ID == 17010 || ID == 30624 || ID == 26847;
    }

    public static boolean talismanOnAlter(Player player, GameObject gameObject, Item item) {
        for (TalismanData tali : TalismanData.values()) {
            if (item.getId() == tali.getTalismanId()) {
                if (gameObject.getId() == tali.getObjectId()) {
                    player.moveTo(tali.getLocation());
                    return true;
                }
            }
        }
        return false;
    }

    public static void zmiBank(Player player, int itemId) {
        if (player.getInventory().getAmount(new Item(itemId)) < 20) {
            player.getPacketSender().sendInterfaceRemoval();
            player.message("You do not have enough of this type of rune to pay the bank charge.");
            return;
        }
        player.getInventory().delete(new Item(itemId, 20));
        player.getBank().open();
    }
}
