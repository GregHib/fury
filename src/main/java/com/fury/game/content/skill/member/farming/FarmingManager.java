package com.fury.game.content.skill.member.farming;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.woodcutting.Hatchet;
import com.fury.game.content.skill.free.woodcutting.Trees;
import com.fury.game.content.skill.free.woodcutting.Woodcutting;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.util.Misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class FarmingManager {

    private List<FarmingSpot> spots;
    private Player player;

    public FarmingManager(Player player) {
        this.player = player;
        spots = new CopyOnWriteArrayList<>();
    }

    public void init() {
        for (FarmingSpot spot : spots)
            spot.refresh();
    }

    public void process() {
        Set<FarmingSpot> removeSet = new HashSet<>();
        for (Iterator<FarmingSpot> iterator = spots.iterator(); iterator.hasNext(); ) {
            FarmingSpot spot = iterator.next();
            if (spot.needsRemoval())
                removeSet.add(spot);
            else
                spot.process();
        }
        spots.removeAll(removeSet);
    }

    public List<FarmingSpot> getSpots() {
        return spots;
    }

    public void setSpots(List<FarmingSpot> spots) {
        this.spots = spots;
    }

    public FarmingSpot getSpot(SpotInfo info) {
        for (FarmingSpot spot : spots)
            if (spot.getSpotInfo().equals(info))
                return spot;
        return null;
    }

    public boolean isFarming(int objectId, Item item, int optionId) {
        SpotInfo info = SpotInfo.getInfo(objectId);
        if (info != null) {
            handleFarming(info, item, optionId);
            return true;
        }
        return false;
    }

    public void handleFarming(SpotInfo info, Item item, int optionId) {
        FarmingSpot spot = getSpot(info);
        if (spot == null) {
            spot = new FarmingSpot(this, info);
            spots.add(spot);
        }
        if (!spot.isCleared()) {
            if (item != null) {
                if (info.getType() == FarmingConstants.COMPOST)
                    fillCompostBin(spot, item);
            } else {
                switch (optionId) {
                    case 1: // rake
                        if (info.getType() == FarmingConstants.COMPOST) {
                            if (spot.getHarvestAmount() == 15) {
                                spot.setCleared(true);
                                spot.setActive(ProductInfo.COMPOST_BIN);
                                spot.setHarvestAmount(15);
                                spot.refresh();
                                player.message("You close the compost bin.");
                                player.message("The vegetation begins to decompose.");
                            }
                        } else
                            startRakeAction(spot); // creates usable spot
                        break;
                    case 2: // inspect
                        sendNeedsWeeding(spot.isCleared());
                        break;
                    case 3: // guide
                        openGuide();
                        break;
                }
            }
        } else {
            if (item != null) {
                String itemName = item.getName().toLowerCase();
                if (itemName.startsWith("watering can ("))
                    startWateringAction(spot, item);
                else if (itemName.contains("compost"))
                    startCompostAction(spot, item, itemName.equals("supercompost"));
                else if (item.getId() == 6036)
                    startCureAction(spot, item);
                else if(item.isEqual(FarmingConstants.SPADE) && spot.getProductInfo() != null)
                    clearFarmingPatch(spot);
                else
                    startFarmingCycle(spot, item);
            } else if (spot.getProductInfo() != null) {
                switch (optionId) {
                    case 1:
                        if (info.getType() == FarmingConstants.TREES) {
                            if (spot.reachedMaxStage() && !spot.hasChecked())
                                checkHealth(spot);
                            else if (spot.reachedMaxStage() && !spot.isEmpty())
                                collectTreeProducts(spot, Trees.valueOf(spot.getProductInfo().name().toUpperCase()));
                            else if (spot.reachedMaxStage() && spot.isEmpty())
                                startHarvestingAction(spot);
                            else if (spot.isDead())
                                clearFarmingPatch(spot);
                            else if (spot.isDiseased())
                                startCureAction(spot, null);
                        } else if (info.getType() == FarmingConstants.FRUIT_TREES) {
                            if (spot.reachedMaxStage() && !spot.hasChecked())
                                checkHealth(spot);
                            else if (spot.reachedMaxStage() && !spot.hasEmptyHarvestAmount())
                                startPickingAction(spot);
                            else if (spot.reachedMaxStage() && !spot.isEmpty())
                                collectTreeProducts(spot, Trees.FRUIT_TREES);
                            else if (spot.reachedMaxStage() && spot.isEmpty() || spot.isDead())
                                clearFarmingPatch(spot);
                            else if (spot.isDiseased())
                                startCureAction(spot, null);
                        } else if (info.getType() == FarmingConstants.BUSHES) {
                            if (spot.reachedMaxStage() && !spot.hasChecked())
                                checkHealth(spot);
                            else if (spot.reachedMaxStage() && !spot.hasEmptyHarvestAmount())
                                startPickingAction(spot);
                            else if (spot.isDead())
                                clearFarmingPatch(spot);
                            else if (spot.isDiseased())
                                startCureAction(spot, null);
                        } else if (info.getType() == FarmingConstants.COMPOST) {
                            if (spot.reachedMaxStage() && !spot.hasChecked()) {
                                spot.setChecked(true);
                                spot.refresh();
                                player.message("You open the compost bin.", true);
                            } else if (!spot.reachedMaxStage())
                                player.getDialogueManager().startDialogue(new SimpleMessageD(), "The vegetation hasn't finished rotting yet.");
                            else
                                clearCompostAction(spot);
                        } else {
                            if (spot.isDead())
                                clearFarmingPatch(spot);
                            else if (spot.reachedMaxStage())
                                startHarvestingAction(spot);
                        }
                        break;
                    case 2:// inspect... usless tbh
                        break;
                    case 3:// clear & guide
                        if (spot.isDiseased() || spot.reachedMaxStage()) {
                            if (info.getType() == FarmingConstants.TREES) {
                                if (spot.isEmpty()) // stump
                                    startHarvestingAction(spot);
                                else {
                                    player.message("You need to chop the tree down before removing it.");
                                    return;
                                }
                            } else
                                clearFarmingPatch(spot);
                        } else if (spot.getProductInfo().getType() == FarmingConstants.FRUIT_TREES) {
                            if (spot.reachedMaxStage())
                                return;
                            clearFarmingPatch(spot);
                        } else
                            openGuide();
                        break;
                }
            }
        }
    }

    private void startRakeAction(final FarmingSpot spot) {
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start(Player player) {
                if (!player.getInventory().contains(FarmingConstants.RAKE)) {
                    player.message("You'll need a rake to get rid of the weeds.");
                    return false;
                }
                if (!player.getInventory().hasRoom()) {
                    player.message("You don't have enough free inventory space.");
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                if (!player.getInventory().hasRoom()) {
                    player.message("You don't have enough free inventory space.");
                    return false;
                }
                return spot.getStage() != 3;
            }

            @Override
            public int processWithDelay(Player player) {
                player.perform(FarmingConstants.RAKING_ANIMATION);
                if (Misc.random(3) == 0) {
                    spot.increaseStage();
                    if (spot.getStage() == 3)
                        spot.setCleared(true);
                    player.getInventory().add(new Item(6055, 1));
                    player.getSkills().addExperience(Skill.FARMING, 8);
                }
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
    }

    public void startHarvestingAction(final FarmingSpot spot) {
        final String patchName = getPatchName(spot.getProductInfo().getType());
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start(Player player) {
                if (!player.getInventory().contains(FarmingConstants.SPADE)) {
                    player.message("You need a spade to harvest your crops.");
                    return false;
                }
                if(!player.getInventory().hasRoom()) {
                    player.getInventory().full();
                    return false;
                }
                if (spot.hasEmptyHarvestAmount() && !spot.hasGivenAmount()) {
                    spot.setHarvestAmount(getRandomHarvestAmount(spot.getProductInfo().getType()));
                    spot.setHasGivenAmount(true);
                } else if (spot.getHarvestAmount() <= 0) {
                    player.message("You have successfully harvested this patch for new crops.", true);
                    player.animate(-1);
                    spot.setIdle();
                    return false;
                }
                player.message("You begin to harvest the " + patchName + " patch.", true);
                setActionDelay(player, 1);
                return true;
            }

            @Override
            public boolean process(Player player) {
                if(!player.getInventory().hasRoom()) {
                    player.getInventory().full();
                    return false;
                }
                if (spot.getHarvestAmount() > 0)
                    return true;
                else {
                    player.message("You have successfully harvested this patch for new crops.", true);
                    player.animate(-1);
                    spot.setIdle();
                    return false;
                }
            }

            @Override
            public int processWithDelay(Player player) {
                spot.setHarvestAmount(spot.getHarvestAmount() - 1);
                player.perform(getHarvestAnimation(spot.getProductInfo().getType()));
                player.getSkills().addExperience(Skill.FARMING, spot.getProductInfo().getExperience());
                player.getInventory().add(new Item(spot.getProductInfo().getProductId(), 1));
                ChristmasEvent.giveSnowflake(player);
                StrangeRocks.handleStrangeRocks(player, Skill.FARMING);
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
    }

    private void startPickingAction(final FarmingSpot spot) {
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start(Player player) {
                if(!player.getInventory().hasRoom()) {
                    player.getInventory().full();
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                if(!player.getInventory().hasRoom()) {
                    player.getInventory().full();
                    return false;
                }
                if (spot.getHarvestAmount() > 0)
                    return true;
                else {
                    player.message("You pick all of the " + (spot.getProductInfo().getType() == FarmingConstants.FRUIT_TREES ? "fruits" : "berries") + " from the " + getPatchName(spot.getProductInfo().getType()) + " patch.");
                    player.animate(-1);
                    return false;
                }
            }

            @Override
            public int processWithDelay(Player player) {
                player.message("You pick a " + Loader.getItem(spot.getProductInfo().getProductId(), Revision.RS2).getName().toLowerCase() + ".", true);
                player.perform(getHarvestAnimation(spot.getProductInfo().getType()));
                player.getSkills().addExperience(Skill.FARMING, spot.getProductInfo().getExperience());
                player.getInventory().add(new Item(spot.getProductInfo().getProductId(), 1));
                spot.setHarvestAmount(spot.getHarvestAmount() - 1);
                StrangeRocks.handleStrangeRocks(player, Skill.FARMING);
                spot.refresh();
                if (spot.getCycleTime() < Misc.currentTimeMillis())
                    spot.setCycleTime(FarmingConstants.REGENERATION_CONSTANT);
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 1);
            }
        });
    }

    public void clearCompostAction(final FarmingSpot spot) {
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start(Player player) {
                if (spot == null)
                    return false;
                else if (!player.getInventory().contains(FarmingConstants.EMPTY_BUCKET)) {
                    player.message("You'll need an empty bucket.");
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                if (!player.getInventory().contains(FarmingConstants.EMPTY_BUCKET)) {
                    player.message("You'll need an empty bucket.");
                    return false;
                } else if (spot.getHarvestAmount() > 0)
                    return true;
                else {
                    spot.setCleared(false);
                    spot.refresh();
                    spot.setProductInfo(null);
                    spot.remove();
                    player.animate(-1);
                    return false;
                }
            }

            @Override
            public int processWithDelay(Player player) {
                player.perform(FarmingConstants.FILL_COMPOST_ANIMATION);
                player.getSkills().addExperience(Skill.FARMING, 5);
                player.getInventory().add(new Item(spot.getCompost() ? 6032 : 6034, 1));
                player.getInventory().delete(FarmingConstants.EMPTY_BUCKET);
                spot.setHarvestAmount(spot.getHarvestAmount() - 1);
                spot.refresh();
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 1);
            }
        });
    }

    public void clearFarmingPatch(final FarmingSpot spot) {
        final String patchName = getPatchName(spot.getProductInfo().getType());
        player.getActionManager().setAction(new Action() {

            private boolean stage;

            @Override
            public boolean start(Player player) {
                if (!player.getInventory().contains(FarmingConstants.SPADE)) {
                    player.message("You need a spade to clear this farming patch.");
                    return false;
                }
                player.message("You start digging the " + patchName + " patch...");
                return true;
            }

            @Override
            public boolean process(Player player) {
                if (stage)
                    return true;
                else {
                    player.message("You have successfully cleared this patch for new crops.");
                    player.animate(-1);
                    spot.setIdle();
                    return false;
                }
            }

            @Override
            public int processWithDelay(Player player) {
                player.perform(FarmingConstants.SPADE_ANIMATION);
                if (Misc.random(3) == 0)
                    stage = true;
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
    }

    public boolean startFarmingCycle(FarmingSpot spot, Item item) { // check if
        // weeded
        ProductInfo productInfo = ProductInfo.getProduct(item.getId());
        if (spot == null || productInfo == null || spot.getSpotInfo().getType() != productInfo.getType() || !spot.isCleared() || spot.getProductInfo() != null || spot.getSpotInfo().getType() == FarmingConstants.COMPOST)
            return false;
        String patchName = getPatchName(productInfo.getType());
        String itemName = item.getDefinition().getName().toLowerCase();
        int requiredAmount = (productInfo.getType() == FarmingConstants.ALLOTMENT || productInfo.getType() == FarmingConstants.HOPS) ? 3 : 1;
        boolean isTree = productInfo.getType() == FarmingConstants.TREES || productInfo.getType() == FarmingConstants.FRUIT_TREES;
        int level = productInfo.getLevel();
        if (!player.getInventory().contains(isTree ? FarmingConstants.SPADE : FarmingConstants.SEED_DIBBER)) {
            player.message(isTree ? "You need a spade to plant the sapling into the dirt." : "You need a seed dipper to plant the seed in the dirt.");
            return true;
        }

        if (!(player.getInventory().contains(item) && player.getInventory().getAmount(item) >= requiredAmount)) {
            player.message("You don't have enough " + item.getDefinition().getName().toLowerCase() + " to plant " + (patchName.startsWith("(?i)[^aeiou]") ? "an" : "a") + " " + patchName + " patch.");
            return true;
        }

        if (player.getSkills().getLevel(Skill.FARMING) < level) {
            player.message("You need a farming level of " + level + " to plant this " + (isTree ? "sapling" : "seed") + ".");
            return true;
        }
        player.message("You plant the " + itemName + " in the " + patchName + " patch.");
        player.perform(isTree ? FarmingConstants.SPADE_ANIMATION : FarmingConstants.SEED_DIPPING_ANIMATION);
        player.getSkills().addExperience(Skill.FARMING, isTree ? productInfo.getExperience() : productInfo.getPlantingExperience());
        player.getInventory().delete(new Item(item.getId(), requiredAmount));
        spot.setActive(productInfo);
        return true;
    }

    public boolean startWateringAction(final FarmingSpot spot, Item item) {
        if (spot == null || spot.getProductInfo() == null)
            return false;
        if (item.getName().toLowerCase().startsWith("watering can") && !item.getName().toLowerCase().contains("(")) {
            player.message("Your watering can is empty and cannot water the plants.");
            return true;
        } else if (spot.isWatered()) {
            player.message("This patch is already watered.");
            return true;
        } else if (spot.reachedMaxStage() || spot.getProductInfo().getType() == FarmingConstants.HERBS || spot.getProductInfo().getType() == FarmingConstants.COMPOST || spot.getProductInfo().getType() == FarmingConstants.TREES || spot.getProductInfo().getType() == FarmingConstants.FRUIT_TREES || spot.getProductInfo() == ProductInfo.WHITE_LILY || spot.getProductInfo().getType() == FarmingConstants.BUSHES) {
            player.message("This patch doesn't need watering.");
            return true;
        } else if (spot.isDiseased()) {
            player.message("This crop is diseased and needs cure, not water!");
            return true;
        } else if (spot.isDead()) {
            player.message("This crop is dead and needs to be removed, not watered!");
            return true;
        }
        player.message("You begin to tip the can over...", true);
        player.perform(FarmingConstants.WATERING_ANIMATION);
        spot.setWatered(true);
        if (item != null) {
            player.getInventory().delete(item);
            player.getInventory().add(new Item(item.getId() - 1));
        }
        GameWorld.schedule(2, () -> {
            player.message("... and the patch becomes moist with water.", true);
            spot.refresh();
        });
        return true;
    }

    public boolean startCureAction(final FarmingSpot spot, final Item item) {
        if (spot == null || spot.getProductInfo() == null || spot.getProductInfo().getType() == FarmingConstants.COMPOST)
            return false;
        final boolean isTree = spot.getProductInfo().getType() == FarmingConstants.TREES || spot.getProductInfo().getType() == FarmingConstants.FRUIT_TREES;
        final boolean isBush = spot.getProductInfo().getType() == FarmingConstants.BUSHES;
        if (!spot.isDiseased()) {
            player.message("This patch doesn't need to be cured.");
            return true;
        } else if (isTree || isBush) {
            if (!(player.getInventory().contains(FarmingConstants.SECATEURS) || player.getInventory().contains(FarmingConstants.MAGIC_SECATEURS))) {
                player.message("You need a pair of secateurs to prune the tree.");
            }
        }
        player.message(isTree ? "You prune the " + spot.getProductInfo().name().toLowerCase() + " tree's diseased branches." : isBush ? "You prune the " + spot.getProductInfo().name().toLowerCase() + " bush's diseased leaves." : "You treat the " + getPatchName(spot.getSpotInfo().getType()) + " patch with the plant cure.");
        player.perform((isTree || isBush) ? FarmingConstants.PRUNING_ANIMATION : FarmingConstants.CURE_PLANT_ANIMATION);
        spot.setDiseased(false);
        GameWorld.schedule(2, () -> {
            if (!isTree && !isBush && item != null) {
                player.getInventory().delete(item);
                player.getInventory().add(new Item(229, 1));
            } else
                player.animate(-1);
            player.message("It is restored to health.");
            spot.refresh();
        });
        return true;
    }

    public boolean startCompostAction(final FarmingSpot spot, final Item item, boolean superCompost) {
        if (spot == null || spot.getSpotInfo().getType() == FarmingConstants.COMPOST)
            return false;
        if (spot.hasCompost()) {
            player.message("This patch is already saturated with a compost.");
            return true;
        } else if (!spot.isCleared()) {
            player.message("The patch needs to be cleared in order to saturate it with compost.");
            return true;
        }
        player.message("You dump a bucket of " + (superCompost ? "supercompost" : "compost") + "...");
        player.perform(FarmingConstants.COMPOST_ANIMATION);
        if (superCompost)
            spot.setSuperCompost(true);
        else
            spot.setCompost(true);
        GameWorld.schedule(2, () -> {
            player.getInventory().delete(item);
            player.getInventory().add(FarmingConstants.EMPTY_BUCKET);
            player.getSkills().addExperience(Skill.FARMING, 8);
            player.message("... and the patch becomes saturated with nutrients.");
            spot.refresh();
        });
        return true;
    }

    private void collectTreeProducts(final FarmingSpot spot, final Trees definitions) {
        player.getActionManager().setAction(new Action() {

            private Hatchet hatchet;

            @Override
            public boolean start(Player player) {
                if (!checkAll(player))
                    return false;
                player.message("You swing your hatchet at the tree...", true);
                setActionDelay(player, getWoodcuttingDelay(player));
                return true;
            }

            private int getWoodcuttingDelay(Player player) {
                int summoningBonus = player.getFamiliar() != null ? (player.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10 : 0 : 0;
                int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(Skill.WOODCUTTING) + summoningBonus) - Misc.random(hatchet.getAxeTime());
                if (wcTimer < 1 + definitions.getLogRandomTime())
                    wcTimer = 1 + Misc.random(definitions.getLogRandomTime());
                wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
                return wcTimer;
            }

            private boolean checkAll(Player player) {
                for (Hatchet def : Hatchet.values()) {
                    if (player.getInventory().contains(new Item(def.getItemId())) || player.getEquipment().get(Slot.WEAPON).getId() == def.getItemId()) {
                        hatchet = def;
                        if (!player.getSkills().hasLevel(Skill.WOODCUTTING, hatchet.getLevelRequired())) {
                            hatchet = null;
                            break;
                        }
                    }
                }
                if (hatchet == null) {
                    player.message("You don't have the required level to use that axe or you don't have a hatchet.");
                    return false;
                }
                if (!hasWoodcuttingLevel(player))
                    return false;
                if (!player.getInventory().hasRoom()) {
                    player.message("Not enough space in your inventory.");
                    return false;
                }
                return true;
            }

            private boolean hasWoodcuttingLevel(Player player) {
                return player.getSkills().hasRequirement(Skill.WOODCUTTING, definitions.getLevel(), "chop down this tree");
            }

            @Override
            public boolean process(Player player) {
                player.perform(new Animation(hatchet.getEmoteId()));
                return checkTree(player);
            }

            private boolean usedDepleteAurora;

            @Override
            public int processWithDelay(Player player) {
                Woodcutting.addLog(definitions, false, false, false, player);
                if (!usedDepleteAurora && (1 + Math.random()) < player.getAuraManager().getChanceNotDepleteMN_WC()) {
                    usedDepleteAurora = true;
                } else if (Misc.random(definitions.getRandomLifeProbability()) == 0) {
                    int time = definitions.getRespawnDelay();
                    spot.setEmpty(true);
                    spot.refresh();
                    spot.setCycleTime(true, time * 1000); // time in seconds
                    player.animate(-1);
                    return -1;
                }
                if (!player.getInventory().hasRoom()) {
                    player.animate(-1);
                    player.message("Not enough space in your inventory.");
                    return -1;
                }
                return getWoodcuttingDelay(player);
            }

            private boolean checkTree(Player player) {
                return spot != null && !spot.isEmpty();
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
    }

    private void fillCompostBin(final FarmingSpot spot, final Item item) {
        final boolean[] attributes = isOrganicItem(item.getId());
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start(Player player) {
                if (item == null || !player.getInventory().contains(item) || spot.isCleared())//Check contains isn't meant to be new Item(item, 1)
                    return false;
                else if (!attributes[0]) {
                    player.message("You cannot use this item to make compost.");
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                return spot.getHarvestAmount() != 15 && player.getInventory().contains(item);//and again ^
            }

            @Override
            public int processWithDelay(Player player) {
                player.perform(FarmingConstants.FILL_COMPOST_ANIMATION);
                player.getInventory().delete(item);//1?
                spot.setHarvestAmount(spot.getHarvestAmount() + 1);
                spot.refresh();
                return 2;
            }

            @Override
            public void stop(Player player) {
                setActionDelay(player, 3);
            }
        });
    }

    private void checkHealth(final FarmingSpot spot) {
        player.message("You examine the " + ((spot.getProductInfo().getType() == FarmingConstants.TREES || spot.getProductInfo().getType() == FarmingConstants.FRUIT_TREES) ? "tree" : "bush") + " for signs of disease and find that it is in perfect health.");
        player.getSkills().addExperience(Skill.FARMING, spot.getProductInfo().getPlantingExperience());
        player.perform(FarmingConstants.CHECK_TREE_ANIMATION);
        spot.setChecked(true);
        spot.refresh();
    }

    private String getPatchName(int type) {
        return FarmingConstants.PATCH_NAMES[type];
    }

    private int getRandomHarvestAmount(int type) {
        int maximumAmount, baseAmount, totalAmount;
        baseAmount = FarmingConstants.HARVEST_AMOUNTS[type][0];
        maximumAmount = FarmingConstants.HARVEST_AMOUNTS[type][1];
        totalAmount = Misc.inclusiveRandom(baseAmount, maximumAmount);
        if (player.getEquipment().get(Slot.WEAPON).getId() == 7409)
            totalAmount *= 1.1;
        return totalAmount;
    }

    private Animation getHarvestAnimation(int type) {
        if (type == FarmingConstants.ALLOTMENT || type == FarmingConstants.HOPS || type == FarmingConstants.TREES || type == FarmingConstants.MUSHROOMS || type == FarmingConstants.BELLADONNA)
            return FarmingConstants.SPADE_ANIMATION;
        else if (type == FarmingConstants.HERBS || type == FarmingConstants.FLOWERS) {
            if (player.getEquipment().get(Slot.WEAPON).getId() == 7409)
                return FarmingConstants.MAGIC_PICKING_ANIMATION;
            return type == FarmingConstants.HERBS ? FarmingConstants.HERB_PICKING_ANIMATION : FarmingConstants.FLOWER_PICKING_ANIMATION;
        } else if (type == FarmingConstants.FRUIT_TREES) {
            return FarmingConstants.FRUIT_PICKING_ANIMATION;
        } else if (type == FarmingConstants.BUSHES) {
            return FarmingConstants.BUSH_PICKING_ANIMATION;
        }
        return FarmingConstants.SPADE_ANIMATION;
    }

    private void sendNeedsWeeding(boolean cleared) {
        player.message(cleared ? "The patch is ready for planting." : "The patch needs weeding.");
    }

    private void openGuide() {
//        player.getTemporaryAttributes().put("skillMenu", 21);
//        player.getPacketSender().sendInterface(SkillInfo.widget);
    }

    private boolean[] isOrganicItem(int itemId) {
        boolean[] bools = new boolean[2];
        for (int organicId : FarmingConstants.COMPOST_ORGANIC) {
            if (itemId == organicId) {
                bools[0] = true;
                bools[1] = false;
            }
        }
        for (int organicId : FarmingConstants.SUPER_COMPOST_ORGANIC) {
            if (itemId == organicId) {
                bools[0] = true;
                bools[1] = false;
            }
        }
        return bools;
    }

    public void resetSpots() {
        spots.clear();
    }

    public void resetTreeTrunks() {
        for (FarmingSpot spot : spots) {
            if (spot.getSpotInfo().getType() == FarmingConstants.TREES || spot.getSpotInfo().getType() == FarmingConstants.FRUIT_TREES) {
                if (spot.isEmpty()) {
                    spot.setEmpty(false);
                    spot.refresh();
                }
            }
        }
    }

    public Player getPlayer() {
        return player;
    }
}
