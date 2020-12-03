package com.fury.game.content.skill.free.woodcutting;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.entity.character.player.content.Sounds.Sound;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;
import com.fury.util.Utils;

public class Woodcutting extends Action {

    private static final int[] BIRD_NESTS = {5070, 5071, 5072, 5073, 5074, 7413, 11966};

    private GameObject tree;
    private Trees definitions;
    private Hatchet hatchet;

    private boolean root;
    private boolean usingBeaver;

    public Woodcutting(GameObject tree, Trees definitions, boolean usingBeaver) {
        this.tree = tree;
        this.definitions = definitions;
        this.usingBeaver = usingBeaver;
        this.root = definitions == Trees.MUTATED_ROOT || definitions == Trees.STRAIT_ROOT || definitions == Trees.CURLY_ROOT || definitions == Trees.CURLY_ROOT_CUT || definitions == Trees.STRAIT_ROOT_CUT;
    }

    public Woodcutting(GameObject tree, Trees definitions) {
        this(tree, definitions, false);
    }

    @Override
    public boolean start(Player player) {
        if (!checkAll(player))
            return false;
        player.message(usingBeaver ? "Your beaver uses its strong ivory teeth to chop down the tree." : "You swing your hatchet at the " + (root ? "root" : Trees.IVY == definitions ? "ivy" : "tree") + ".");
        setActionDelay(player, getWoodcuttingDelay(player));
        return true;
    }

    private int getWoodcuttingDelay(Player player) {
        int summoningBonus = player.getFamiliar() != null && player.getFamiliar().getId() == 6808 ? 2 : 0;
        int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(Skill.WOODCUTTING) + summoningBonus) - Utils.random(hatchet.getAxeTime());
        if (wcTimer < 1 + definitions.getLogRandomTime())
            wcTimer = 1 + Utils.random(definitions.getLogRandomTime());
        wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();

        if (player.getEffects().hasActiveEffect(Effects.EVIL_TREE_BUFF) && wcTimer > 1)
            wcTimer -= 1;

        return wcTimer;
    }

    private boolean checkAll(Player player) {
        hatchet = getHatchet(player, definitions.ordinal() > 18);
        if (hatchet == null) {
            player.message("You don't have a hatchet that you can use.");
            return false;
        }
        if (!player.getSkills().hasRequirement(Skill.WOODCUTTING, definitions.getLevel(), "chop down this " + (root ? "root" : "tree")))
            return false;
        if (player.getInventory().getSpaces() <= 0) {
            player.getInventory().full();
            return false;
        }
        return true;
    }

    public static Hatchet getHatchet(Player player, boolean dungeoneering) {
        //Check equipment
        for (int i = dungeoneering ? 10 : Hatchet.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) { //from best to worst
            Hatchet def = Hatchet.values()[i];
            if (player.getEquipment().get(Slot.WEAPON).getId() == def.getItemId() && player.getSkills().hasLevel(Skill.WOODCUTTING, def.getLevelRequired()))
                return def;
        }

        //Check inventory
        for (int i = dungeoneering ? 10 : Hatchet.values().length - 1; i >= (dungeoneering ? 0 : 11); i--) { //from best to worst
            Hatchet def = Hatchet.values()[i];
            if (player.getInventory().contains(new Item(def.getItemId())) && player.getSkills().hasLevel(Skill.WOODCUTTING, def.getLevelRequired()))
                return def;
        }
        return null;
    }

    @Override
    public boolean process(Player player) {
        if (usingBeaver) {
            player.getFamiliar().animate(7722);
            player.getFamiliar().graphic(1458);
        } else {
            player.perform(new Animation(definitions == Trees.IVY ? hatchet.getIvyEmoteId() : root ? hatchet.getRootEmoteId() : hatchet.getEmoteId()));
        }
        return ObjectManager.containsObjectWithId(tree, tree.getId());
    }

    private boolean usedDepleteAurora;

    @Override
    public int processWithDelay(Player player) {
        boolean dungeoneering = definitions.ordinal() > 18;

        if (definitions == Trees.STRAIT_ROOT && tree.getId() == 12290) {
            if (!player.getEffects().hasActiveEffect(Effects.CAMOUFLAGED_FRUIT)) {
                player.getCombat().applyHit(new Hit(player, Misc.inclusiveRandom(10, 40), HitMask.RED, CombatIcon.NONE));
                player.graphic(449);
                player.message("As you attack one of the healthy roots, the Queen bids roots to last out at you.");
            }

            JadinkoLair.removePoints(player, 1);
        } else if (definitions == Trees.MUTATED_ROOT)
            JadinkoLair.addPoints(player, player.getEffects().hasActiveEffect(Effects.AMPHIBIOUS_FRUIT) ? 4 : 2);

        addLog(definitions, dungeoneering, root, usingBeaver, player);

        if (!usedDepleteAurora && (1 + Math.random()) < player.getAuraManager().getChanceNotDepleteMN_WC()) {
            usedDepleteAurora = true;
        } else if (definitions.getStumpId() != -1 && Utils.random(definitions.getRandomLifeProbability()) == 0) {
            long time = definitions.getRespawnDelay() * 600;
            if (time < 0) {
                if (dungeoneering) {
                    ObjectManager.spawnObject(new GameObject(tree.getId() + 1, tree, tree.getType(), tree.getDirection(), Revision.RS2));
                    player.message("You have depleted this resource.");
                } else
                    ObjectManager.removeObject(tree);
            } else {
                TempObjectManager.spawnObjectTemporary(new GameObject(dungeoneering ? tree.getId() + 1 : definitions.getStumpId(), tree, tree.getType(), tree.getDirection(), Revision.RS2), time, false, true);
                if (definitions == Trees.IVY)
                    player.message("You successfully chop away some ivy.", true);
            }
            return -1;
        }
        if (player.getInventory().getSpaces() <= 0) {
            player.animate(-1);
            player.message("Not enough space in your inventory.");
            return -1;
        }
        return getWoodcuttingDelay(player);
    }

    public static void addLog(Trees definitions, boolean dungeoneering, boolean root, boolean beaver, Player player) {
        Hatchet hatchet = getHatchet(player, definitions.ordinal() > 18);
        boolean logs = definitions.getLogsId() != null;
        boolean burn = false;
        boolean bank = false;

        //Items
        if (logs) {
            boolean hasAdaze = !dungeoneering && hatchet != null && hatchet == Hatchet.INFERNO;
            burn = hasAdaze && Utils.random(2) == 0;
            bank = player.getEffects().hasActiveEffect(Effects.EVIL_TREE_BUFF);
            boolean duplicate = definitions == Trees.NORMAL && Equipment.wearingSeersHeadband(player, 1);

            if (beaver)
                burn = bank = duplicate = false;

            //Give logs
            if (!burn) {
                for (int item : definitions.getLogsId()) {
                    if (bank)
                        player.getBank().deposit(new Item(item, duplicate ? 2 : 1));
                    else
                        player.getInventory().add(new Item(item, duplicate ? 2 : 1));
                }
            }
        }

        if (!beaver) {
            //Experience
            double experience = definitions.getXp();

            if (definitions == Trees.MAPLE && Equipment.wearingSeersHeadband(player, 1))
                experience += 10;

            if (burn)
                player.getSkills().addExperience(Skill.FIREMAKING, experience);
            addExperience(player, experience);

            StrangeRocks.handleStrangeRocks(player, Skill.WOODCUTTING);

            //Achievements
            if (definitions == Trees.OAK) {
                Achievements.finishAchievement(player, AchievementData.CUT_AN_OAK_TREE);
            } else if (definitions == Trees.MAPLE) {
                Achievements.doProgress(player, AchievementData.CUT_50_MAPLES);
            } else if (definitions == Trees.NORMAL) {
                Achievements.finishAchievement(player, AchievementData.CUT_A_TREE);
            } else if (definitions == Trees.YEW) {
                if (burn)
                    Achievements.doProgress(player, AchievementData.BURN_5_YEW_LOGS);
                else
                    Achievements.finishAchievement(player, AchievementData.CUT_A_YEW_TREE);
            } else if (definitions == Trees.MAGIC) {
                Achievements.doProgress(player, burn ? AchievementData.BURN_2500_MAGIC_LOGS : AchievementData.CUT_2000_MAGIC_LOGS);
            }

            //Extra items
            ChristmasEvent.giveSnowflake(player);

            if (!dungeoneering && !root && Utils.random(bank ? 40 : definitions == Trees.IVY ? 45 : 50) == 0) {
                FloorItemManager.addGroundItem(new Item(BIRD_NESTS[Utils.random(BIRD_NESTS.length)]), player.copyPosition(), player, true, 180, true, 180);
                player.message("A bird's nest falls out of the tree!");
            }
        }

        //Message
        if (logs) {
            String logName = Loader.getItem(definitions.getLogsId()[0]).getName().toLowerCase();
            if (burn) {
                player.message("You chop some " + logName + ". The heat of the inferno adze incinerates them.");
            } else if (bank) {
                Sounds.sendSound(player, Sound.WOODCUT);
                player.message("The logs are magically escorted to your bank.", 0xffffff, true);
            } else {
                Sounds.sendSound(player, Sound.WOODCUT);
                player.message("You get some " + logName + ".", true);
            }
        }
    }

    public static void addExperience(Player player, double xp) {
        double xpBoost = 1.00;
        if (player.getEquipment().get(Slot.BODY).getId() == 10939)
            xpBoost += 0.02;
        if (player.getEquipment().get(Slot.LEGS).getId() == 10940)
            xpBoost += 0.02;
        if (player.getEquipment().get(Slot.HEAD).getId() == 10941)
            xpBoost += 0.02;
        if (player.getEquipment().get(Slot.FEET).getId() == 10933)
            xpBoost += 0.02;
        player.getSkills().addExperience(Skill.WOODCUTTING, xp * xpBoost);
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

}
