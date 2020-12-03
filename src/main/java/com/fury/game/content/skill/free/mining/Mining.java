package com.fury.game.content.skill.free.mining;

import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.mining.data.Ores;
import com.fury.game.content.skill.free.mining.data.Pickaxe;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;
import com.fury.util.Utils;

/**
 * Created by Greg on 10/12/2016.
 */
public class Mining extends MiningBase {

    public static final int[] UNCUT_GEMS = { 1623, 1619, 1621, 1617, 1631, 6571 };

    private GameObject rock;
    private Ores definitions;
    private Pickaxe pickaxe;

    public Mining(GameObject rock, Ores definitions) {
        this.rock = rock;
        this.definitions = definitions;
    }

    @Override
    public boolean start(Player player) {
        pickaxe = getPickaxe(player, false);
        if (!checkAll(player))
            return false;
        player.message("You swing your pickaxe at the rock.", true);
        setActionDelay(player, getMiningDelay(player));
        return true;
    }

    private int getMiningDelay(Player player) {
        int summoningBonus = 0;
        if (player.getFamiliar() != null) {
            if (player.getFamiliar().getId() == 7342 || player.getFamiliar().getId() == 7342)
                summoningBonus += 10;
            else if (player.getFamiliar().getId() == 6832 || player.getFamiliar().getId() == 6831)
                summoningBonus += 1;
        }
        int mineTimer = definitions.getOreBaseTime() - (player.getSkills().getLevel(Skill.MINING) + summoningBonus) - Misc.getRandom(pickaxe.getPickAxeTime());
        if (mineTimer < 1 + definitions.getOreRandomTime())
            mineTimer = 1 + Misc.getRandom(definitions.getOreRandomTime());
        mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
        return mineTimer;
    }

    private boolean checkAll(Player player) {
        if (pickaxe == null) {
            player.message("You do not have a pickaxe or do not have the required level to use the pickaxe.");
            return false;
        }
        if (!hasMiningLevel(player))
            return false;
        if (player.getInventory().getSpaces() <= 0) {
            player.message("Not enough space in your inventory.");
            return false;
        }
        return true;
    }

    private boolean hasMiningLevel(Player player) {
        return player.getSkills().hasRequirement(Skill.MINING, definitions.getLevel(), "mine this rock");
    }

    @Override
    public boolean process(Player player) {
        player.perform(new Animation(pickaxe.getAnimationId()));
        return checkRock();
    }

    private boolean usedDeplateAurora;

    @Override
    public int processWithDelay(Player player) {
        addOre(player);
        if (definitions.getEmptyId() != -1) {
            if (!usedDeplateAurora && (1 + Math.random()) < player.getAuraManager().getChanceNotDepleteMN_WC()) {
                usedDeplateAurora = true;
            } else if (Misc.getRandom(definitions.getRandomLifeProbability()) == 0) {
                TempObjectManager.spawnObjectTemporary(new GameObject(getEmptyId(rock.getId()), rock), definitions.getRespawnDelay() * 600, false, true);
                player.animate(-1);
                return -1;
            }
        }
        if (player.getInventory().getSpaces() <= 0 && definitions.getOreId() != -1) {
            player.animate(-1);
            player.message("Not enough space in your inventory.");
            return -1;
        }
        return getMiningDelay(player);
    }

    private int getEmptyId(int full) {
        int[] dessertSand = new int[] { 18991, 18994, 18997, 19000 };
        int[] muddyClay = new int[] { 11945, 11948, 11951, 11954, 11957, 11960, 11963, 15503 };
        int[] lightClay = new int[] { 11942, 11939, 11936, 11933, 11930 };
        int[] darkClay = new int[] { 5784, 5782, 5779, 5776, 5773, 5770, 5768 };
        int[] blackIsTheColourOfDeath = new int[] { 14850, 14853, 14856, 14859, 14862 };
        int[] greyRock = new int[] { 29221, 29224, 29227, 29230, 29233, 29236 };

        for(int rock : dessertSand)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 19003 + i;

        for(int rock : muddyClay)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 11555 + i;

        for(int rock : lightClay)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 11552 + i;

        for(int rock : darkClay)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 5763 + i;

        for(int rock : blackIsTheColourOfDeath)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 14832 + i;

        for(int rock : greyRock)
            for(int i = 0; i < 3; i++)
                if(full == rock + i)
                    return 29218 + i;
        return 11552;
    }

    private void addOre(Player player) {
        double xpBoost = 0;
        int idSome = 0;
        if (definitions == Ores.GRANITE) {
            idSome = Misc.getRandom(2) * 2;
            if (idSome == 2)
                xpBoost += 10;
            else if (idSome == 4)
                xpBoost += 25;
        } else if (definitions == Ores.SANDSTONE) {
            idSome = Misc.getRandom(3) * 2;
            xpBoost += idSome / 2 * 10;
        } else if (definitions == Ores.RED_SANDSTONE) {
            /*if (player.getRedStoneDelay() >= Utils.currentTimeMillis()) {
                player.message("It seems that there is no remaining ore, check again in twelve hours.");
                stop(player);
                return;
            }
            player.increaseRedStoneCount();
            if (player.getRedStoneCount() >= (player.isDonor(DonorStatus.EXTREME_DONATOR) ? 225 : player.isDonor() ? 150 : 75)) {
                player.resetRedStoneCount();
                player.setStoneDelay(3600000 * 24); // 12 hours
                //player.getVarsManager().sendVarBitOld(10133, 26);
            }*/// else if (player.getRedStoneCount() == 125)
                //player.getVarsManager().sendVarBitOld(10133, 25);
        }
        if(definitions == Ores.COPPER)
            Achievements.finishAchievement(player, Achievements.AchievementData.MINE_COPPER_ORE);
        else if(definitions == Ores.TIN)
            Achievements.finishAchievement(player, Achievements.AchievementData.MINE_TIN_ORE);
        else if(definitions == Ores.COAL)
            Achievements.doProgress(player, Achievements.AchievementData.MINE_100_COAL);
        else if(definitions == Ores.RUNITE)
            Achievements.doProgress(player, Achievements.AchievementData.MINE_1000_RUNITE_ORE);

        StrangeRocks.handleStrangeRocks(player, Skill.MINING);
        ChristmasEvent.giveSnowflake(player);

        double totalXp = definitions.getXp() + xpBoost;

        if (hasMiningSuit(player))
            totalXp *= 1.025;

        if (Equipment.wearingVarrockArmour(player, 1))
            totalXp *= 1.01;


        addExperience(player, totalXp);
        if (definitions.getOreId() != -1) {
            boolean dbl = Equipment.wearingVarrockArmour(player, 1) && varrockArmourChance(player, definitions);
            player.getInventory().add(new Item(definitions.getOreId() + idSome, dbl ? 2 : 1));

            String oreName = Loader.getItem(definitions.getOreId() + idSome).getName().toLowerCase();
            player.message("You mine some " + oreName + ".", true);
            if(dbl)
                player.message("Your armour allows you to mine two ores at once!");
            if (Utils.random(150) == 0)
                player.getInventory().add(new Item(UNCUT_GEMS[Utils.random(UNCUT_GEMS.length - 2)]));
            else if (Utils.random(5000) == 0)
                player.getInventory().add(new Item(UNCUT_GEMS[Utils.random(UNCUT_GEMS.length)]));
        }
    }

    public static void addExperience(Player player, double xp) {
        double xpBoost = 1.00;
        if (player.getEquipment().get(Slot.BODY).getId() == 20791)
            xpBoost += 0.015;
        if (player.getEquipment().get(Slot.LEGS).getId() == 20790)
            xpBoost += 0.015;
        if (player.getEquipment().get(Slot.HEAD).getId() == 20789 || player.getEquipment().get(Slot.HEAD).getId() == 20792)
            xpBoost += 0.015;
        if (player.getEquipment().get(Slot.FEET).getId() == 20788)
            xpBoost += 0.015;
        if (player.getEquipment().get(Slot.HANDS).getId() == 20787)
            xpBoost += 0.015;
        if (player.getEquipment().get(Slot.SHIELD).getId() == 15439)
            xpBoost += 0.005;
        player.getSkills().addExperience(Skill.MINING, xp * xpBoost);
    }

    private boolean varrockArmourChance(Player player, Ores definitions) {
        if(Equipment.wearingVarrockArmour(player, 1) && definitions.ordinal() <= 7)
            return Misc.random(10) == 0;
        else if(Equipment.wearingVarrockArmour(player, 2) && definitions.ordinal() <= 10)
            return Misc.random(10) == 0;
        else if(Equipment.wearingVarrockArmour(player, 3) && definitions.ordinal() <= 11)
            return Misc.random(10) == 0;
        else if(Equipment.wearingVarrockArmour(player, 4) && definitions.ordinal() <= 12)
            return Misc.random(10) == 0;
        return false;
    }

    private boolean hasMiningSuit(Player player) {
        return player.getEquipment().get(Slot.HEAD).getId() == 20789 && player.getEquipment().get(Slot.BODY).getId() == 20791 && player.getEquipment().get(Slot.LEGS).getId() == 20790 && player.getEquipment().get(Slot.FEET).getId() == 20788;
    }

    private boolean checkRock() {
        return ObjectManager.containsObjectWithId(rock, rock.getId());
    }
}