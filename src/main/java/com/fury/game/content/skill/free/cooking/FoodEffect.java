package com.fury.game.content.skill.free.cooking;

import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.update.flag.block.Gender;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

public enum FoodEffect {
    MEAT {
        @Override
        public void activateEffect(Player player) {
            Achievements.finishAchievement(player, Achievements.AchievementData.EAT_SOME_MEAT);
        }
    },
    SALMON {
        @Override
        public void activateEffect(Player player) {
            Achievements.finishAchievement(player, Achievements.AchievementData.EAT_A_SALMON);
        }
    },
    CHOCOLATE_BAR {
        @Override
        public void activateEffect(Player player) {
            Achievements.finishAchievement(player, Achievements.AchievementData.EAT_A_BAR_OF_CHOCOLATE);
        }
    },
    SUMMER_PIE {
        @Override
        public void activateEffect(Player player) {
            int runEnergy = (int) (player.getSettings().getInt(Settings.RUN_ENERGY) * 1.1);
            player.getSettings().set(Settings.RUN_ENERGY, runEnergy > 100 ? 100 : runEnergy);
            player.getSkills().boost(Skill.AGILITY, 5);
        }

    },
    GARDEN_PIE {
        @Override
        public void activateEffect(Player player) {
            player.getSkills().boost(Skill.FARMING, 3);
        }

    },
    FISH_PIE {
        @Override
        public void activateEffect(Player player) {
            player.getSkills().boost(Skill.FISHING, 3);
        }
    },
    ADMIRAL_PIE {
        @Override
        public void activateEffect(Player player) {
            player.getSkills().boost(Skill.FISHING, 5);
        }
    },
    WILD_PIE {
        @Override
        public void activateEffect(Player player) {
            player.getSkills().boost(Skill.SLAYER, 4);
            player.getSkills().boost(Skill.RANGED, 4);
        }
    },
    SPICY_STEW {
        @Override
        public void activateEffect(Player player) {
            if (Misc.random(100) > 5) {
                player.getSkills().boost(Skill.COOKING, 6);
            } else {
                player.getSkills().drain(Skill.COOKING, 6);
            }
        }

    },
    CABBAGE {
        @Override
        public void activateEffect(Player player) {
            player.message("You don't really like it much.", true);
        }
    },
    ONION {
        @Override
        public void activateEffect(Player player) {
            player.message("It hurts to see a grown " + (player.getAppearance().getGender() == Gender.MALE ? "male" : "female") + " cry.");
        }
    },
    POISON_KARAMBWAN {
        @Override
        public void activateEffect(Player player) {
            player.getCombat().applyHit(new Hit(player, 50, HitMask.GREEN, CombatIcon.NONE));
        }
    },
    PURPLE_SWEET {
        @Override
        public void activateEffect(Player player) {
            int newRunEnergy = (int) (player.getSettings().getInt(Settings.RUN_ENERGY) * 1.2);
            player.getSettings().set(Settings.RUN_ENERGY, newRunEnergy > 100 ? 100 : newRunEnergy);
        }
    },
    BISCUITS {
        @Override
        public void activateEffect(Player player) {
            player.getSkills().restore(Skill.PRAYER, 10);
        }
    },
    RAW_CAVE_POTATO {
        @Override
        public void activateEffect(Player player) {
            player.message("You must really be hungry.");
        }
    },
    ROCKTAIL {
        @Override
        public int getHitpointsModification(Player player) {
            return 100;
        }
    },
    ROCKTAIL_SOUP {
        @Override
        public int getHitpointsModification(Player player) {
            return (int) (player.getMaxConstitution() * 0.15);
        }
    },
    TIGER_SHARK {
        @Override
        public int getHitpointsModification(Player player) {
            return 100;
        }
    },
    SHADOW_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.SHADOW_FRUIT, 100));
        }
    },
    IGNEOUS_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.IGNEOUS_FRUIT, 100));
        }
    },
    CANNIBAL_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.CANNIBAL_FRUIT, 100));
        }
    },
    AQUATIC_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.AQUATIC_FRUIT, 100));
        }
    },
    AMPHIBIOUS_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.AMPHIBIOUS_FRUIT, 100));
        }
    },
    CARRION_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.CARRION_FRUIT, 100));
        }
    },
    DISEASED_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.DISEASED_FRUIT, 100));
        }
    },
    CAMOUFLAGED_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.CAMOUFLAGED_FRUIT, 100));
        }
    },
    DRACONIC_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getEffects().startEffect(new Effect(Effects.DRACONIC_FRUIT, 100));
        }
    },
    SARADOMIN_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getSkills().restore(Skill.PRAYER, 100);
        }
    },
    GUTHIX_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getSkills().restore(Skill.PRAYER, 100);
        }
    },
    ZAMORAK_FRUIT {
        @Override
        public void activateEffect(Player player) {
            if (player.getControllerManager().getController() instanceof JadinkoLair)
                player.getSkills().restore(Skill.PRAYER, 100);
        }
    },;

    FoodEffect() {
    }

    public int getHitpointsModification(Player player) {
        return 0;
    }

    public void activateEffect(Player player) {
    }
}
