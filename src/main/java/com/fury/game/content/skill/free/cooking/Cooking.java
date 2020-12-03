package com.fury.game.content.skill.free.cooking;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.impl.JadinkoLair;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.cooking.CookD;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.Achievements.AchievementData;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

public class Cooking extends Action {
    private int amount;
    private Cookables cook;
    private Item item;
    private GameObject object;
    private Animation FIRE_COOKING = new Animation(897), RANGE_COOKING = new Animation(897);

    public Cooking(GameObject object, Item item, int amount, Cookables cook) {
        this.amount = amount;
        this.item = item;
        this.object = object;
        this.cook = cook;
    }

    @Override
    public boolean start(Player player) {
        if (cook == null)
            cook = Cookables.forId((short) item.getId());
        if (cook == null) {
            return false;
        }
        if (cook.isFireOnly() && !object.getDefinition().getName().contains("Fire")) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You may only cook this on a fire.");
        } else if (cook.isSpitRoast() && object.getId() != 11363) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You may only cook this on an iron spit.");
            return false;
        }
        if (!process(player))
            return false;
        player.getPacketSender().sendMessage("You attempt to cook the " + cook.getProduct().getName().toLowerCase() + ".", true);
        player.getDirection().face(object);
        return true;
    }

    private boolean isBurned(Cookables cook, Player player) {
        int level = player.getSkills().getLevel(Skill.COOKING);
        if (player.getEquipment().get(Slot.HANDS).getId() == 775) {
            if (level >= (cook.getBurningLvl() - (cook.getProduct().getId() == 391 ? 0 : 6)))
                return false;
        }
        int levelsToStopBurn = cook.getBurningLvl() - level;
        if (levelsToStopBurn > 20)
            levelsToStopBurn = 20;
        return Misc.random(40) <= levelsToStopBurn;
    }

    public static Cookables isCookingSkill(Item item) {
        return Cookables.forId((short) item.getId());
    }


    public static Cookables getCook(Player player) {
        for (Cookables c : Cookables.values()) {
            if (player.getInventory().contains(c.getRawItem()))
                return c;
        }
        return Cookables.RAW_CHICKEN;
    }

    public static Cookables getCookForRaw(int id) {
        for (Cookables c : Cookables.values()) {
            if (c.getRawItem().getId() == id)
                return c;
        }
        return null;
    }


    @Override
    public boolean process(Player player) {
        GameObject obj = Region.getGameObject(object);
        if (obj == null)
            return false;
        if (obj.getId() != object.getId())
            return false;
        if (!player.getInventory().contains(item))
            return false;
        if (!player.getInventory().contains(cook.getRawItem()))
            return false;
        if (!player.getSkills().hasRequirement(cook.getRawItem().getId() == Cookables.HARDENED_STRAIT_ROOT.getRawItem().getId() ? Skill.FIREMAKING : Skill.COOKING, cook.getLvl(), "cook this"))
            return false;
        return true;
    }

    @Override
    public int processWithDelay(Player player) {
        amount--;
        player.perform(cook == Cookables.HARDENED_STRAIT_ROOT ? new Animation(3235) : object.getDefinition().getName().contains("fire") ? FIRE_COOKING : RANGE_COOKING);
        if (player.getSkills().getLevel(Skill.COOKING) < cook.getBurningLvl() && isBurned(cook, player)) {
            player.getInventory().delete(new Item(item, 1));
            player.getInventory().add(cook.getBurntId());
            player.getPacketSender().sendMessage("Oops! You accidently burnt the " + cook.getProduct().getName().toLowerCase() + ".", true);
        } else {
            player.getInventory().delete(new Item(item, 1));
            player.getInventory().add(cook.getProduct());
            player.getSkills().addExperience(cook.getRawItem().getId() == Cookables.HARDENED_STRAIT_ROOT.getRawItem().getId() ? Skill.FIREMAKING : Skill.COOKING, cook.getXp(), player.getEquipment().get(Slot.HANDS).getId() == 775 ? 1.01 : 1.0);
            if (cook.getBurntId().getId() == 1781)
                player.message("You burn the " + item.getName() + " to soda ash.", true);
            else
                player.message("You successfully cook the " + cook.getProduct().getName().toLowerCase() + ".", true);
            if (cook == Cookables.RAW_YAK_MEAT)
                Achievements.finishAchievement(player, AchievementData.COOK_YAK_MEAT);
            else if (cook == Cookables.CAKE)
                Achievements.finishAchievement(player, AchievementData.BAKE_A_CAKE);


            StrangeRocks.handleStrangeRocks(player, Skill.COOKING);
        }
        if (cook.getBurntId() == Cookables.CAKE.getBurntId())
            player.getInventory().add(new Item(1887));
        else if (cook.getRawItem().getId() == Cookables.HARDENED_STRAIT_ROOT.getRawItem().getId())
            JadinkoLair.addPoints(player, 2);
        if (amount > 0) {
            player.message("You attempt to cook the " + cook.getProduct().getName().toLowerCase() + ".", true);
            return 3;
        }
        return -1;
    }

    @Override
    public void stop(final Player player) {
        setActionDelay(player, 3);
    }

    public static void selectionInterface(Player player, CookingData cookingData) {
        if (cookingData == null)
            return;
        player.setSelectedSkillingItem(new Item(cookingData.getRawItem()));
        player.getDialogueManager().startDialogue(new CookD(), cookingData.getCookedItem());
    }

    public static void selectionInterface(Player player, Pies cookingData) {
        if (cookingData == null)
            return;
        player.setSelectedSkillingItem(new Item(cookingData.getRawItem()));
        player.getDialogueManager().startDialogue(new CookD(), cookingData.getCookedItem());
    }

    public static void cook(final Player player, final int rawItem, final int amount) {
        cookFish(player, rawItem, amount);
        cookPie(player, rawItem, amount, false);
    }

    public static void cookFish(final Player player, final int rawFish, final int amount) {
        final CookingData fish = CookingData.forFish(rawFish);
        if (fish == null)
            return;
        player.stopAll();
        player.getPacketSender().sendInterfaceRemoval();
        if (!CookingData.canCookFish(player, rawFish))
            return;
        player.animate(896);
        GameWorld.schedule(new Task(2) {
            int amountCooked = 0;

            @Override
            public void run() {
                if (!CookingData.canCookFish(player, rawFish)) {
                    stop();
                    return;
                }
                player.animate(896);
                player.getInventory().delete(new Item(rawFish));
                if (!CookingData.success(player, 3, fish.getLevelReq(), fish.getStopBurn())) {
                    player.getInventory().add(new Item(fish.getBurntItem()));
                    player.message("You accidentally burn the " + fish.getName() + ".", true);
                } else {
                    player.getInventory().add(new Item(fish.getCookedItem()));
                    player.getSkills().addExperience(Skill.COOKING, fish.getXp());
                    if (fish == CookingData.CAKE) {
                        player.getInventory().add(new Item(1887));
                    } /*else if(fish == CookingData.SALMON) {
                        Achievements.finishAchievement(player, AchievementData.COOK_A_SALMON);
					} else if(fish == CookingData.ROCKTAIL) {
						Achievements.doProgress(player, AchievementData.COOK_25_ROCKTAILS);
						Achievements.doProgress(player, AchievementData.COOK_1000_ROCKTAILS);
					}*/
                    ChristmasEvent.giveSnowflake(player);
                }
                amountCooked++;
                if (amountCooked >= amount)
                    stop();
            }

            @Override
            public void onCancel(boolean logout) {
                player.setSelectedSkillingItem(null);
                if (!logout)
                    player.animate(-1);
            }
        });
    }

    public static void cookPie(final Player player, final int rawPie, final int amount, boolean bakePie) {
        final Pies pie = Pies.forPie(rawPie);
        if (pie == null)
            return;
        player.stopAll();
        player.getPacketSender().sendInterfaceRemoval();
        if (!CookingData.canCookPie(player, rawPie))
            return;
        if (!bakePie) player.animate(896);
        GameWorld.schedule(new Task(2) {
            int amountCooked = 0;

            @Override
            public void run() {
                if (!CookingData.canCookPie(player, rawPie)) {
                    stop();
                    return;
                }
                if (!bakePie) player.animate(896);
                player.getInventory().delete(new Item(rawPie));
                if (!bakePie && !CookingData.success(player, 3, pie.getLevelReq(), pie.getStopBurn())) {
                    player.getInventory().add(new Item(pie.getBurntItem()));
                    player.message("You accidentally burn the " + pie.getName() + ".", true);
                } else {
                    player.getInventory().add(new Item(pie.getCookedItem()));
                    player.getSkills().addExperience(Skill.COOKING, pie.getXp());
                }
                amountCooked++;
                if (amountCooked >= amount)
                    stop();
            }

            @Override
            public void onCancel(boolean logout) {
                player.setSelectedSkillingItem(null);
                if (!bakePie && !logout) player.animate(-1);
            }
        });
    }
}
