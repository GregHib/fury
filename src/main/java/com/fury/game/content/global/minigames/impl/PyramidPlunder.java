package com.fury.game.content.global.minigames.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Greg on 05/12/2016.
 */
public class PyramidPlunder {

    public enum Urn {
        URN1(16501),
        URN2(16502),
        URN3(16503),
        URN4(16504);
        int objectId;

        Urn(int objectId) {
            this.objectId = objectId;
        }

        public int getObjectId() {
            return objectId;
        }

        public int getSearched() {
            return objectId + 4;
        }

        public int getSnake() {
            return objectId + 8;
        }

        public int getCharmed() {
            return objectId + 12;
        }
    }

    public enum UrnRewards {
        IVORY_COMB(new Item(9026, 1), 100),
        STONE_SEAL(new Item(9042, 1), 50),
        GOLD_SEAL(new Item(9040, 1), 15),
        POTTERY_SCARAB(new Item(9032, 1), 75),
        GOLDEN_SCARAB(new Item(9028, 1), 15),
        POTTERY_STATUETTE(new Item(9036, 1), 75),
        STONE_STATUETTE(new Item(9038, 1), 50),
        GOLDEN_STATUETTE(new Item(9034, 1), 15),
        JEWELLED_GOLDEN_STATUETTE(new Item(20661, 1), 3);

        Item item;
        int weight;

        UrnRewards(Item item, int weight) {
            this.item = item;
            this.weight = weight;
        }

        public Item getItem() {
            return item;
        }

        public int getWeight() {
            return weight;
        }

        public static UrnRewards forId(int itemId) {
            for(UrnRewards urn : values()) {
                if(urn.getItem().getId() == itemId || Item.Companion.getNoted(urn.getItem()).getId() == itemId) {
                    return urn;
                }
            }
            return null;
        }
    }

    public static void search(Player player, GameObject object, Urn urn, boolean isSnake, boolean isCharmed) {
        boolean success = Misc.random(isSnake ? 2 : 3) == 0 || isCharmed;
        final GameObject resetUrn = new GameObject(urn.getObjectId(), object);
        final GameObject urnReplacement = new GameObject(success ? urn.getSearched() : urn.getSnake(), object);
        player.animate(4340);
        GameWorld.schedule(1, () -> {
            if (success) {
                player.animate(4342);

                switch (player.getSkills().getMaxLevel(Skill.THIEVING) / 10) {
                    case 4:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 100 : 150);
                        break;
                    case 5:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 140 : 215);
                        break;
                    case 6:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 200 : 300);
                        break;
                    case 7:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 300 : 450);
                        break;
                    case 8:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 450 : 675);
                        break;
                    case 9:
                        player.getSkills().addExperience(Skill.THIEVING, isCharmed ? 550 : 825);
                        break;
                }

                List<Item> filler = new ArrayList<>();
                Item reward = null;
                for (UrnRewards _reward : UrnRewards.values())
                    for (int i = 0; i < _reward.getWeight(); i++)
                        filler.add(_reward.getItem());

                Collections.shuffle(filler);
                reward = filler.get(Misc.random(0, filler.size() - 1));
                if (reward != null)
                    player.getInventory().add(reward);

                player.stopAll();
            } else {
                player.animate(4341);
                player.getCombat().applyHit(new Hit(50, HitMask.RED, CombatIcon.NONE));
                player.forceChat("Ouch!");
            }
            player.getTimers().getClickDelay().reset();
            if(success)
                spawnUrn(urnReplacement, resetUrn,  7);
            else
                ObjectManager.spawnObject(urnReplacement);
        });
    }

    public static void charmSnake(Player player, GameObject object, Urn urn) {
        boolean success = Misc.random(2) == 0;
        final GameObject urnReplacement = new GameObject(success ? urn.getCharmed() : urn.getSnake(), object);
        player.animate(1877);
        GameWorld.schedule(1, () -> {
            if (success) {
                player.message("You successfully charm the snake.", true);
            } else {
                player.message("You fail to charm the snake and it bites you!", true);
                player.getCombat().applyHit(new Hit(25, HitMask.RED, CombatIcon.NONE));
                player.forceChat("Ouch!");
            }
            player.getTimers().getClickDelay().reset();
            ObjectManager.spawnObject(urnReplacement);
        });
    }

    public static void checkUrn(Player player, GameObject object, Urn urn) {
        boolean success = Misc.random(3) != 0;
        final GameObject urnReplacement = new GameObject(success ? urn.getSnake() : urn.getObjectId(), object);
        player.animate(4340);
        GameWorld.schedule(5, () -> {
            if (success) {
                switch (player.getSkills().getMaxLevel(Skill.THIEVING) / 10) {
                    case 4:
                        player.getSkills().addExperience(Skill.THIEVING, 50);
                        break;
                    case 5:
                        player.getSkills().addExperience(Skill.THIEVING, 70);
                        break;
                    case 6:
                        player.getSkills().addExperience(Skill.THIEVING, 100);
                        break;
                    case 7:
                        player.getSkills().addExperience(Skill.THIEVING, 150);
                        break;
                    case 8:
                        player.getSkills().addExperience(Skill.THIEVING, 225);
                        break;
                    case 9:
                        player.getSkills().addExperience(Skill.THIEVING, 275);
                        break;
                }
                player.message("You found a snake!", true);
            } else {
                player.message("You found a snake! It bit you!", true);
                player.getCombat().applyHit(new Hit(25, HitMask.RED, CombatIcon.NONE));
                player.forceChat("Ouch!");
            }
            player.getTimers().getClickDelay().reset();
            ObjectManager.spawnObject(urnReplacement);
        });
    }

    private static void spawnUrn(GameObject object, GameObject resetUrn, int seconds) {
        ObjectManager.spawnObject(object);
        GameExecutorManager.slowExecutor.schedule(() -> ObjectManager.spawnObject(resetUrn), seconds, TimeUnit.SECONDS);
    }

    public static boolean handleSnakes(final Player player, final GameObject object) {
        for (final Urn urn : Urn.values()) {
            if (!isUrn(object, urn))
                continue;

            boolean isCharmed = object.getId() == urn.getCharmed();
            boolean isSnake = object.getId() == urn.getSnake() || isCharmed;

            if (!isSnake)
                continue;

            if (checkReq(player))
                return true;

            player.getMovement().lock(2);
            player.getDirection().face(object);
            if (isCharmed)
                search(player, object, urn, isSnake, isCharmed);
            else
                charmSnake(player, object, urn);
        }
        return false;
    }

    public static boolean handleUrns(final Player player, final GameObject object) {
        for (final Urn urn : Urn.values()) {
            if (!isUrn(object, urn))
                continue;

            boolean isCharmed = object.getId() == urn.getCharmed();
            boolean isSnake = object.getId() == urn.getSnake() || isCharmed;

            if (urn.getSearched() == object.getId()) {
                player.message("This urn has already been looted.");
                return true;
            }

            if (checkReq(player))
                return true;

            player.getMovement().lock(2);
            player.getDirection().face(object);
            search(player, object, urn, isSnake, isCharmed);
        }
        return false;
    }

    public static boolean handleUrnCheck(final Player player, final GameObject object) {
        for (final Urn urn : Urn.values()) {
            if (!isUrn(object, urn))
                continue;

            if(object.getId() != urn.getObjectId())
                continue;

            if (checkReq(player))
                return true;

            player.getMovement().lock(2);
            player.getDirection().face(object);
            checkUrn(player, object, urn);
        }
        return false;
    }

    private static boolean isUrn(GameObject object, Urn urn) {
        return object.getId() == urn.getObjectId() || object.getId() == urn.getSearched() || object.getId() == urn.getSnake() || object.getId() == urn.getCharmed();
    }

    private static boolean checkReq(Player player) {
        if (!player.getSkills().hasRequirement(Skill.THIEVING, 60, "steal from this urn"))
            return true;

        if (player.getInventory().getSpaces() <= 0) {
            player.message("You do not have enough space in your inventory.");
            return true;
        }
        return !player.getTimers().getClickDelay().elapsed(2000);
    }
}
