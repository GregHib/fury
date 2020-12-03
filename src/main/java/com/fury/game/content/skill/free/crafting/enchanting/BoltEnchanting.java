package com.fury.game.content.skill.free.crafting.enchanting;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

/**
 * Created by Greg on 22/02/2017.
 */
public class BoltEnchanting {
    public static void enchant(Player player, int boltId, int amount) {
        EnchantedBolts bolts = EnchantedBolts.forId(boltId);
        if(bolts == null)
            return;

        Item bolt = new Item(boltId);

        if(!player.getInventory().contains(bolt)) {
            player.message("You do not have enough " + bolt.getDefinition().getName() + " to enchant.");
            return;
        }

        Item coins = new Item(995);
        int amountToMake = Math.min(amount, player.getInventory().getAmount(bolt));

        int cost = amountToMake * (bolts.ordinal() * 100);

        boolean usePouch = player.getMoneyPouch().getTotal() >= cost;

        if((player.getInventory().getAmount(coins) < cost && !usePouch) || !usePouch) {
            player.message("You do not have enough money to buy that amount.");
            return;
        }

        if(usePouch) {
            player.getMoneyPouch().setTotal(player.getMoneyPouch().getTotal() - cost);
            player.getPacketSender().sendString(8135, ""+player.getMoneyPouch().getTotal());
        } else {
            player.getInventory().delete(new Item(coins, cost));
        }
        player.getInventory().delete(new Item(bolts.getBoltId(), amountToMake));
        player.getInventory().addSafe(new Item(bolts.getEnchantedBoltId(), amountToMake));
    }
}
