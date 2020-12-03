package com.fury.game.entity.item.content;

import com.fury.game.content.global.Achievements;
import com.fury.game.content.dialogue.impl.misc.ItemRepairD;
import com.fury.game.content.dialogue.impl.misc.ItemRepairVoidKnightD;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 05/08/2016.
 */
public enum ItemRepair {
    TORVA_FULL_HELM(20138, 20135, 4000000),
    TORVA_PLATEBODY(20142, 20139, 6000000),
    TORVA_PLATELEGS(20146, 20143, 5000000),
    PERNIX_COWL(20150, 20147, 4000000),
    PERNIX_BODY(20154, 20151, 6000000),
    PERNIX_LEGS(20158, 20155, 5000000),
    VIRTUS_MASK(20162, 20159, 4000000),
    VIRTUS_ROBE_TOP(20166, 20163, 6000000),
    VIRTUS_ROBE_LEGS(20170, 20167, 5000000),
    ZUREILS_HOOD(13866, 13864, 200000),
    ZUREILS_STAFF(13869, 13867, 200000),
    ZURIELS_ROBE_TOP(13860, 13858, 500000),
    ZURIELS_ROBE_BOTTOM(13863, 13861, 400000),
    MORRIGANS_LEATHER_BODY(13872, 13870, 500000),
    MORRIGANS_LEATHER_CHAPS(13875, 13873, 400000),
    MORRIGANS_COIF(13878, 13876, 200000),
    STATIUSS_WARHAMMER(13904, 13902, 400000),
    STATIUSS_PLATEBODY(13886, 13884, 1000000),
    STATIUSS_PLATELEGS(13892, 13890, 800000),
    STATIUSS_FULL_HELM(13898, 13896, 400000),
    VESTAS_CHAINBODY(13889, 13887, 1200000),
    VESTAS_PLATESKIRT(13895, 13893, 1000000),
    VESTAS_LONGSWORD(13901, 13899, 500000),
    VESTAS_SPEAR(13907, 13905, 500000);
    //TODO add barrows


    private int[] brokenItems;
    private int repairedItem, cost;

    ItemRepair(int itemId, int repairedId, int cost) {
        brokenItems = new int[]{itemId};
        repairedItem = repairedId;
        this.cost = cost;
    }

    ItemRepair(int[] itemIds, int repairedId, int cost) {
        brokenItems = itemIds;
        repairedItem = repairedId;
        this.cost = cost;
    }

    public String getName() {
        String name = this.name().toLowerCase().replaceAll("_", " ");
        name = name.replaceAll("vestas", "vesta's");
        name = name.replaceAll("zuriels", "zuriel's");
        name = name.replaceAll("statiuss", "statius's");
        return name;
    }

    public int[] getBrokenItems() {
        return brokenItems;
    }

    public void setBrokenItems(int[] brokenItems) {
        this.brokenItems = brokenItems;
    }

    public int getRepairedItem() {
        return repairedItem;
    }

    public void setRepairedItem(int repairedItem) {
        this.repairedItem = repairedItem;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public static ItemRepair forId(int itemId) {
        for (ItemRepair item : ItemRepair.values()) {
            for (int broken : item.getBrokenItems()) {
                if (broken == itemId) {
                    return item;
                }
            }
        }
        return null;
    }

    public static void handlePurchaseDialogue(Player player, int npcId, int itemId) {
        ItemRepair item = ItemRepair.forId(itemId);
        if (item == null)
            return;
        player.setInteractingItem(new Item(itemId));
        player.getDialogueManager().startDialogue(new ItemRepairD(), npcId, item);
    }

    public static void handleDialogue(Player player, int npcId) {
        player.getDialogueManager().startDialogue(new ItemRepairVoidKnightD(), npcId);
    }

    public static void handlePurchase(Player player, Item item) {
        ItemRepair repair = ItemRepair.forId(item.getId());
        if(repair == null)
            return;

        int cost = repair.getCost();
        if(DonorStatus.isDonor(player,DonorStatus.ONYX_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.50);
        if(DonorStatus.isDonor(player,DonorStatus.DRAGONSTONE_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.75);
        if(DonorStatus.isDonor(player,DonorStatus.DIAMOND_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.80);
        if(DonorStatus.isDonor(player,DonorStatus.RUBY_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.85);
        if(DonorStatus.isDonor(player,DonorStatus.EMERALD_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.90);
        if(DonorStatus.isDonor(player,DonorStatus.SAPPHIRE_DONOR))
            cost = (int) Math.round(repair.getCost() * 0.95);

        if(player.getInventory().removeCoins(cost)) {
            player.getInventory().delete(item);
            player.getInventory().add(new Item(repair.getRepairedItem()));
            player.getPacketSender().sendInterfaceRemoval();
            player.message("Your " + repair.getName() + " has been fully repaired and looks as good as new.");
            Achievements.finishAchievement(player, Achievements.AchievementData.REPAIR_A_BROKEN_ITEM);
        }
    }
}
