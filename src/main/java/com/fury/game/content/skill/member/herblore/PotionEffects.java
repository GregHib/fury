package com.fury.game.content.skill.member.herblore;

import com.fury.core.task.Task;
import com.fury.game.content.controller.impl.FreeForAllController;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public enum PotionEffects {
    WEAK_MELEE_POTION(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 2 + (realLevel * 0.07));
        }
    },
    WEAK_MAGIC_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 2 + (realLevel * 0.07));
        }
    },
    WEAK_RANGE_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 2 + (realLevel * 0.07));
        }
    },
    WEAK_DEFENCE_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 2 + (realLevel * 0.07));
        }
    },
    WEAK_STAT_RESTORE_POTION(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.AGILITY, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING, Skill.FLETCHING, Skill.HERBLORE, Skill.MINING, Skill.RUNECRAFTING, Skill.SLAYER, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.12) + 5;
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }
    },
    WEAK_STAT_CURE_POTION() {
        @Override
        public void extra(Player player) {
            player.getEffects().startEffect(new Effect(Effects.FIRE_IMMUNITY, 500));
            player.setAntipoisonDelay(500);
        }
    },
    WEAK_REJUVINATION_POTION(Skill.SUMMONING, Skill.PRAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int restore = (int) (Math.floor(player.getSkills().getMaxLevel(skill) * 0.08) + 4);
            if (actualLevel + restore > realLevel)
                return realLevel;
            return actualLevel + restore;
        }

        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 40, 0.08);
        }
    },
    WEAK_GATHERER_POTION(Skill.WOODCUTTING, Skill.MINING, Skill.FISHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.02));
        }
    },
    WEAK_ARTISTIANS_POTION(Skill.SMITHING, Skill.CRAFTING, Skill.FLETCHING, Skill.CONSTRUCTION, Skill.FIREMAKING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.02));
        }
    },
    WEAK_NATURALISTS_POTION(Skill.COOKING, Skill.FARMING, Skill.HERBLORE, Skill.RUNECRAFTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.02));
        }
    },
    WEAK_SURVIVALISTS_POTION(Skill.AGILITY, Skill.HUNTER, Skill.THIEVING, Skill.SLAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    REGULAR_MELEE_POTION(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.11));
        }
    },
    REGULAR_MAGIC_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.11));
        }
    },
    REGULAR_RANGE_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.11));
        }
    },
    REGULAR_DEFENCE_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.11));
        }
    },
    REGULAR_STAT_RESTORE_POTION(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.AGILITY, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING, Skill.FLETCHING, Skill.HERBLORE, Skill.MINING, Skill.RUNECRAFTING, Skill.SLAYER, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.17) + 7;
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }
    },
    REGULAR_STAT_CURE_POTION() {
        @Override
        public void extra(Player player) {
            player.getEffects().startEffect(new Effect(Effects.SUPER_FIRE_IMMUNITY, 1000));
            player.setAntipoisonDelay(1000);
        }
    },
    REGULAR_REJUVINATION_POTION(Skill.SUMMONING, Skill.PRAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int restore = (int) (Math.floor(player.getSkills().getMaxLevel(skill) * 0.15) + 7);
            if (actualLevel + restore > realLevel)
                return realLevel;
            return actualLevel + restore;
        }

        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 70, 0.15);
        }
    },
    REGULAR_GATHERER_POTION(Skill.WOODCUTTING, Skill.MINING, Skill.FISHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 4 + (realLevel * 0.04));
        }
    },
    REGULAR_ARTISTIANS_POTION(Skill.SMITHING, Skill.CRAFTING, Skill.FLETCHING, Skill.CONSTRUCTION, Skill.FIREMAKING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 4 + (realLevel * 0.04));
        }
    },
    REGULAR_NATURALISTS_POTION(Skill.COOKING, Skill.FARMING, Skill.HERBLORE, Skill.RUNECRAFTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 4 + (realLevel * 0.04));
        }
    },
    REGULAR_SURVIVALISTS_POTION(Skill.AGILITY, Skill.HUNTER, Skill.THIEVING, Skill.SLAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 4 + (realLevel * 0.04));
        }
    },
    STRONG_MELEE_POTION(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.15));
        }
    },
    STRONG_MAGIC_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.15));
        }
    },
    STRONG_RANGE_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.15));
        }
    },
    STRONG_DEFENCE_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.15));
        }
    },
    STRONG_STAT_RESTORE_POTION(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.AGILITY, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING, Skill.FLETCHING, Skill.HERBLORE, Skill.MINING, Skill.RUNECRAFTING, Skill.SLAYER, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.24) + 10;
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }
    },
    STRONG_STAT_CURE_POTION() {
        @Override
        public void extra(Player player) {
            player.getEffects().startEffect(new Effect(Effects.SUPER_FIRE_IMMUNITY, 2000));
            player.setAntipoisonDelay(2000);
        }
    },
    STRONG_REJUVINATION_POTION(Skill.SUMMONING, Skill.PRAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int restore = (int) (Math.floor(player.getSkills().getMaxLevel(skill) * 0.22) + 10);
            if (actualLevel + restore > realLevel)
                return realLevel;
            return actualLevel + restore;
        }

        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 100, 0.22);
        }
    },
    STRONG_GATHERER_POTION(Skill.WOODCUTTING, Skill.MINING, Skill.FISHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 6 + (realLevel * 0.06));
        }
    },
    STRONG_ARTISTIANS_POTION(Skill.SMITHING, Skill.CRAFTING, Skill.FLETCHING, Skill.CONSTRUCTION, Skill.FIREMAKING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 6 + (realLevel * 0.06));
        }
    },
    STRONG_NATURALISTS_POTION(Skill.COOKING, Skill.FARMING, Skill.HERBLORE, Skill.RUNECRAFTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 6 + (realLevel * 0.06));
        }
    },
    STRONG_SURVIVALISTS_POTION(Skill.AGILITY, Skill.HUNTER, Skill.THIEVING, Skill.SLAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 6 + (realLevel * 0.06));
        }
    },
    ATTACK_POTION(Skill.ATTACK) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    AGILITY_POTION(Skill.AGILITY) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    COMBAT_POTION(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    FISHING_POTION(Skill.FISHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    CRAFTING_POTION(Skill.CRAFTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    HUNTER_POTION(Skill.HUNTER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    FLETCHING_POTION(Skill.FLETCHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    ZAMORAK_BREW(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE) {
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.ATTACK) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return (int) (level + 2 + (realLevel * 0.20));
            } else if (skill == Skill.DEFENCE) {
                return (int) (actualLevel * 0.90) - 2;
            } else {//STRENGTH
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return (int) (level + 2 + (realLevel * 0.12));
            }
        }

        @Override
        public boolean canDrink(Player player) {
            int damage = (int) (player.getHealth().getHitpoints() * 0.10) + 20;
            if (player.getHealth().getHitpoints() - damage < 0) {
                player.message("You need more hitpoints in order to survive the effects of the zamorak brew.");
                return false;
            }
            player.getCombat().applyHit(new Hit(player, damage, HitMask.RED, CombatIcon.NONE));
            return true;
        }
    },
    SANFEW_SERUM(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.AGILITY, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING, Skill.FLETCHING, Skill.HERBLORE, Skill.MINING, Skill.RUNECRAFTING, Skill.SLAYER, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING, Skill.SUMMONING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.33);
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }

        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, player.getInventory().hasHolyWrench() ? Misc.random(10, 30) : 0, 0.33 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
            player.setAntipoisonDelay(600); //6 mins
        }

    },
    SUMMONING_POT(Skill.SUMMONING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int restore = (int) (Math.floor(player.getSkills().getMaxLevel(skill) * 0.25) + 7);
            if (actualLevel + restore > realLevel)
                return realLevel;
            return actualLevel + restore;
        }

        @Override
        public void extra(Player player) {
            Familiar familiar = player.getFamiliar();
            if (familiar != null)
                familiar.restoreSpecialEnergy(15);
        }
    },
    ANTIPOISON() {
        @Override
        public void extra(Player player) {
            player.setAntipoisonDelay(300);//3 mins
        }
    },
    ANTIPOISON_PLUS() {
        @Override
        public void extra(Player player) {
            player.setAntipoisonDelay(900);//9 mins
        }
    },
    ANTIPOISON_DOUBLE_PLUS() {
        @Override
        public void extra(Player player) {
            player.setAntipoisonDelay(1200);//12 mins
        }
    },
    SUPER_ANTIPOISON() {
        @Override
        public void extra(Player player) {
            player.setAntipoisonDelay(600); //6 mins
        }
    },
    ENERGY_POTION() {
        @Override
        public void extra(Player player) {
            player.getSettings().set(Settings.SPECIAL_ENERGY, player.getSettings().getInt(Settings.SPECIAL_ENERGY) + 10);
        }
    },
    SUPER_ENERGY() {
        @Override
        public void extra(Player player) {
            player.getSettings().set(Settings.SPECIAL_ENERGY, player.getSettings().getInt(Settings.SPECIAL_ENERGY) + 20);
        }
    },
    ANTI_FIRE() {
        @Override
        public void extra(final Player player) {
            player.getEffects().startEffect(new Effect(Effects.FIRE_IMMUNITY, 600));
        }
    },
    SUPER_ANTI_FIRE() {
        @Override
        public void extra(final Player player) {
            player.getEffects().startEffect(new Effect(Effects.SUPER_FIRE_IMMUNITY, 600));
        }
    },
    STRENGTH_POTION(Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    DEFENCE_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    RANGE_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    MAGIC_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 3 + (realLevel * 0.1));
        }
    },
    GUTHIX_REST() {
        @Override
        public void extra(Player player) {
            player.getEffects().removeEffect(Effects.POISON);
            player.getSettings().set(Settings.RUN_ENERGY, (int) (player.getSettings().getInt(Settings.RUN_ENERGY) * 0.05) + player.getSettings().getInt(Settings.RUN_ENERGY));
            player.getHealth().heal(50, 50);
        }
    },
    MAGIC_ESSENCE(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 3;
        }
    },
    PRAYER_POTION() {
        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 70, 0.25 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
        }
    },
    PRAYER_MIX() {
        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 31, 0.25 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
        }
    },
    SUPER_STRENGTH_MIX() {
        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.STRENGTH, 31, 0.25 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
        }
    },
    SUPER_STR_POTION(Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.15));
        }
    },
    SUPER_DEF_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.15));
        }
    },
    SUPER_ATT_POTION(Skill.ATTACK) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.15));
        }
    },
    SUPER_MAGIC_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.15));
        }
    },
    SUPER_RANGE_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.15));
        }
    },
    EXTREME_STR_POTION(Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.22));
        }
    },
    EXTREME_DEF_POTION(Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.22));
        }
    },
    EXTREME_ATT_POTION(Skill.ATTACK) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 5 + (realLevel * 0.22));
        }
    },
    EXTREME_RAN_POTION(Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return (int) (level + 4 + (Math.floor(realLevel / 5.2)));
        }
    },
    EXTREME_MAG_POTION(Skill.MAGIC) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int level = actualLevel > realLevel ? realLevel : actualLevel;
            return level + 7;
        }
    },
    RECOVER_SPECIAL() {
        @Override
        public boolean canDrink(Player player) {
            Long time = (Long) player.getTemporaryAttributes().get("Recover_Special_Pot");
            if (time != null && Misc.currentTimeMillis() - time < 30000) {
                player.message("You may only use this pot once every 30 seconds.");
                return false;
            }
            return true;
        }

        @Override
        public void extra(Player player) {
            player.getTemporaryAttributes().put("Recover_Special_Pot", Misc.currentTimeMillis());
            CombatSpecial.restore(player, 25, false);
        }
    },
    SARADOMIN_BREW("You drink some of the foul liquid.", Skill.ATTACK, Skill.DEFENCE, Skill.STRENGTH, Skill.MAGIC, Skill.RANGED) {
        @Override
        public boolean canDrink(Player player) {
            return true;
        }

        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.DEFENCE) {
                int boost = (int) (realLevel * 0.25);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel * 0.90);
            }
        }

        @Override
        public void extra(Player player) {
            int heal = (int) (player.getMaxConstitution() * 0.15);
            player.getHealth().heal(heal, heal);
        }
    },
    OVERLOAD() {
        @Override
        public boolean canDrink(Player player) {
            if (FreeForAllController.isOverloadChanged(player)) {
                player.message("You cannot drink this potion here.");
                return false;
            }
            return true;
        }

        @Override
        public void extra(final Player player) {
            Effect currentEffect = player.getEffects().getEffectForType(Effects.OVERLOAD);
            if (currentEffect == null || currentEffect.getCycle() < 480) {
                GameWorld.schedule(new Task(true, 2) {
                    private int count = 4;
                    @Override
                    public void run() {
                        if (count == 0)
                            stop();
                        player.getCombat().applyHit(new Hit(player, 100, HitMask.RED, CombatIcon.NONE));
                        player.animate(3170);
                        player.graphic(560);
                        count--;
                    }
                });
            }
            player.getEffects().startEffect(new Effect(Effects.OVERLOAD, 501));
        }
    },
    SUPER_PRAYER() {
        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, 70, 0.35 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
        }
    },
    PRAYER_RENEWAL() {
        @Override
        public void extra(Player player) {
            if (player.getSkills().getMaxLevel(Skill.PRAYER) <= 200)
                player.getSkills().restore(Skill.PRAYER, 200 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
            else
                player.getEffects().startEffect(new Effect(Effects.PRAYER_RENEWAL, 516));
        }
    },
    SUPER_RESTORE(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.AGILITY, Skill.COOKING, Skill.CRAFTING, Skill.FARMING, Skill.FIREMAKING, Skill.FISHING, Skill.FLETCHING, Skill.HERBLORE, Skill.MINING, Skill.RUNECRAFTING, Skill.SLAYER, Skill.SMITHING, Skill.THIEVING, Skill.WOODCUTTING, Skill.SUMMONING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.33);
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }

        @Override
        public void extra(Player player) {
            player.getSkills().restore(Skill.PRAYER, player.getInventory().hasHolyWrench() ? Misc.random(10, 30) : 0, 0.33 * player.getAuraManager().getPrayerPotsRestoreMultiplier());
        }

    },
    RESTORE_POTION(Skill.ATTACK, Skill.STRENGTH, Skill.MAGIC, Skill.RANGED) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            int boost = (int) (realLevel * 0.30) + 10;
            if (actualLevel > realLevel)
                return actualLevel;
            if (actualLevel + boost > realLevel)
                return realLevel;
            return actualLevel + boost;
        }
    },
    BEER("You drink a glass of beer.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (realLevel * 0.04);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel * 0.93);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(20);
        }
    },
    TANKARD("You quaff the beer. You feel slightly reinvigorated... but very dizzy too.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (4);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 9 && actualLevel != 0 ? actualLevel - 9 : actualLevel >> 9);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    KEG_OF_BEER("You chug the keg. You feel reinvigorated... ...but extremely drunk, too.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (10);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 40 && actualLevel != 0 ? actualLevel - 40 : actualLevel >> 40);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(20);

        }
    },
    MOONLIGHT_MEAD("You drink a glass of Moonlight Mead.") {
        @Override
        public void extra(Player player) {
            player.getHealth().heal(40);
            player.message("It tastes like something died in your mouth.");
        }
    },
    GROG("You drink a glass of grog.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (3);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 6 && actualLevel != 0 ? actualLevel - 6 : actualLevel >> 6);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(30);
        }
    },
    CIDER("You drink a glass of cider.", Skill.ATTACK, Skill.STRENGTH, Skill.FARMING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.FARMING) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 2 && actualLevel != 0 ? actualLevel - 2 : actualLevel >> 2);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(20);
        }
    },
    GREENMANS_ALE("You drink a glass of Greenman's Ale.", Skill.ATTACK, Skill.STRENGTH, Skill.HERBLORE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.HERBLORE) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 2 && actualLevel != 0 ? actualLevel - 2 : actualLevel >> 2);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    DWARVEN_STOUT("You drink a glass of Dwarven Stout.", Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MINING, Skill.SMITHING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.MINING && skill == Skill.SMITHING) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 2 && actualLevel != 0 ? actualLevel - 2 : actualLevel >> 2);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    DRAGON_BITTER("You drink a glass of Dragon Bitter.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (2);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 1 && actualLevel != 0 ? actualLevel - 1 : actualLevel >> 1);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    RANGERS_AID("You drink a glass of Ranger's Aid.", Skill.RANGED, Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.RANGED) {
                int boost = (int) (2);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 5 && actualLevel != 0 ? actualLevel - 5 : actualLevel >> 5);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    SLAYER_RESPITE("You drink a glass of Slayer's Respite.", Skill.ATTACK, Skill.STRENGTH, Skill.SLAYER) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.SLAYER) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 2 && actualLevel != 0 ? actualLevel - 2 : actualLevel >> 2);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    WIZARD_MIND_BOMB("You drink a glass of Wizard's mind bomb.", Skill.ATTACK, Skill.STRENGTH, Skill.MAGIC, Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.MAGIC) {
                int boost = (int) (player.getSkills().getMaxLevel(Skill.MAGIC) >= 50 ? 3 : 2);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else if (skill == Skill.ATTACK) {
                return (int) (actualLevel > 4 && actualLevel != 0 ? actualLevel - 4 : actualLevel >> 4);
            } else {
                return (int) (actualLevel > 3 && actualLevel != 0 ? actualLevel - 3 : actualLevel >> 3);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    CHEF_DELIGHT("You drink a glass of Chef's Delight", Skill.ATTACK, Skill.STRENGTH, Skill.COOKING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.COOKING) {
                int boost = (int) (realLevel * 0.05);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel * 0.97);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    BANDIT_BREW("You drink a glass of Bandit's brew.", Skill.ATTACK, Skill.STRENGTH, Skill.THIEVING, Skill.DEFENCE) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.ATTACK && skill == Skill.THIEVING) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else if (skill == Skill.STRENGTH) {
                return (int) (actualLevel > 1 && actualLevel != 0 ? actualLevel - 1 : actualLevel >> 1);
            } else {
                return (int) (actualLevel > 6 && actualLevel != 0 ? actualLevel - 6 : actualLevel >> 6);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(20);
        }
    },
    AXEMANS("You drink a glass of Axeman's Folly", Skill.ATTACK, Skill.STRENGTH, Skill.WOODCUTTING) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.WOODCUTTING) {
                int boost = (int) (1);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 3 && actualLevel != 0 ? actualLevel - 3 : actualLevel >> 3);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    ASGARNIAN_ALE("You drink a glass of asgarnian ale.", Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int boost = (int) (2);
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + boost;
            } else {
                return (int) (actualLevel > 4 && actualLevel != 0 ? actualLevel - 4 : actualLevel >> 4);
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(10);
        }
    },
    WINE(Skill.ATTACK) {
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            return (int) (actualLevel * 0.90);
        }

        @Override
        public void extra(Player player) {
            player.message("You drink the wine. You feel slightly reinvigorated...");
            player.message("...and slightly dizzy too.");
            player.getHealth().heal(70);
        }
    },
    TEA(Skill.ATTACK) {
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            return actualLevel + 2;
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(30);
        }
    },
    NETTLE_TEA() {
        @Override
        public void extra(Player player) {
            player.getSettings().set(Settings.RUN_ENERGY, (int) (player.getSettings().getInt(Settings.RUN_ENERGY) * 0.05) + player.getSettings().getInt(Settings.RUN_ENERGY));
            player.getHealth().heal(30);
        }
    },
    FRUIT_BLAST() {
        @Override
        public void extra(Player player) {
            player.getHealth().heal(90);
        }
    },
    PINEAPPLE_PUNCH() {
        @Override
        public void extra(Player player) {
            player.getHealth().heal(90);
        }
    },
    WIZARD_BLIZZARD(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + 6;
            } else {
                return actualLevel - 4;
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(50);
        }
    },
    SHORT_GREEN_GUY(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + 4;
            } else {
                return actualLevel - 3;
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(50);
        }
    },
    DRUNK_DRAGON(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + 7;
            } else {
                return actualLevel - 4;
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(50);
        }
    },
    CHOCOLATE_SATURDAY(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + 7;
            } else {
                return actualLevel - 4;
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(50);
        }
    },
    BLURBERRY_SPECIAL(Skill.ATTACK, Skill.STRENGTH) {
        @Override
        public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
            if (skill == Skill.STRENGTH) {
                int level = actualLevel > realLevel ? realLevel : actualLevel;
                return level + 6;
            } else {
                return actualLevel - 4;
            }
        }

        @Override
        public void extra(Player player) {
            player.getHealth().heal(60);
        }
    };

    private Skill[] affectedSkills;
    private String drinkMessage;


    public String getDrinkMessage() {
        return drinkMessage;
    }

    public Skill[] getAffectedSkills() {
        return affectedSkills;
    }

    PotionEffects(Skill... affectedSkills) {
        this(null, affectedSkills);
    }

    PotionEffects(String drinkMessage, Skill... affectedSkills) {
        this.drinkMessage = drinkMessage;
        this.affectedSkills = affectedSkills;
    }

    public int getAffectedSkill(Player player, Skill skill, int actualLevel, int realLevel) {
        return actualLevel;
    }

    public boolean canDrink(Player player) {
        return true;
    }

    public void extra(Player player) {
    }
}
