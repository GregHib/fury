package com.fury.game.content.skill.member.construction;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class Sawmill {

    public static enum Plank {
        WOOD(HouseConstants.PLANK, new Item(1511), 20000),
        OAK(HouseConstants.OAK_PLANK, new Item(1521), 35000),
        TEAK(HouseConstants.TEAK_PLANK, new Item(6333), 70000),
        MAHOGANY(HouseConstants.MAHOGANY_PLANK, new Item(6332), 140000);
        private int cost;
        private Item plank, log;

        Plank(Item id, Item log, int cost) {
            this.plank = id;
            this.log = log;
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }

        public Item getPlank() {
            return plank;
        }

        public Item getLog() {
            return log;
        }
    }

    public static Plank getPlankForLog(int id) {
        for (Plank plank : Plank.values())
            if (plank.log.getId() == id)
                return plank;
        return null;
    }

    public static void exchange(Player player, Plank plank, int amount) {
        int logs = player.getInventory().getAmount(plank.getLog());
        if (logs <= 0)
            return;

        if (amount < logs)
            logs = amount;
        int toPay = logs * plank.getCost();

        player.getPacketSender().sendInterfaceRemoval();

        if(player.getInventory().removeCoins(toPay, "cover all the planks in your inventory")) {
            player.getInventory().delete(new Item(plank.getLog(), logs));
            player.getInventory().add(new Item(plank.getPlank(), logs));
            player.message("You receive your planks.");
        }
    }

    public static boolean handleButtonClick(Player player, int id){
        switch(id) {
            case 28819:
            case 28820:
            case 28821:
            case 28822:
                int amount;
                Plank plank = Plank.values()[id - 28819];
                if(plank == null)
                    return true;

                amount = player.getInventory().getAmount(plank.getLog());

                if(amount <= 0) {
                    player.message("You do not have any " + plank.getLog().getName() + " to make into planks.");
                    return true;
                }

                exchange(player, plank, amount);
                return true;
        }

        return false;
    }
}

