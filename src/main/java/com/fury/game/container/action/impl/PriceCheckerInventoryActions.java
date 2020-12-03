package com.fury.game.container.action.impl;

import com.fury.game.container.impl.PriceChecker;
import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.dialogue.input.impl.EnterAmountToPriceCheck;
import com.fury.game.content.global.Achievements;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class PriceCheckerInventoryActions extends ContainerActions {

    public static void check(Player player, int id, int slot, int amount, boolean all) {
        if (player.getInterfaceId() != PriceChecker.INTERFACE_ID || !player.getPriceChecker().isOpen())
            return;

        if (!player.getInventory().validate(id, slot))
            return;

        Item item = player.getInventory().get(slot);
        int currentAmount = player.getInventory().getAmount(item);

        if(!item.tradeable()) {
            player.message("This item cannot be traded.");
            return;
        }

        if (all)
            item.setAmount(currentAmount);
        else
            item.setAmount(amount > currentAmount ? currentAmount : amount);
        player.getInventory().move(item, player.getPriceChecker());
        player.getPriceChecker().refresh();
        if (id == 1891 || id == 1897)
            Achievements.finishAchievement(player, Achievements.AchievementData.PRICE_CHECK_A_CAKE);
    }

    @Override
    public void first(Player player, int widget, int id, int slot) {
        check(player, id, slot, 1, false);
    }

    @Override
    public void second(Player player, int widget, int id, int slot) {
        check(player, id, slot, 5, false);
    }

    @Override
    public void third(Player player, int widget, int id, int slot) {
        check(player, id, slot, 10, false);
    }

    @Override
    public void fourth(Player player, int widget, int id, int slot) {
        check(player, id, slot, 0, true);
    }

    @Override
    public void fifth(Player player, int widget, int id, int slot) {
        if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
            player.setInputHandling(new EnterAmountToPriceCheck(id, slot));
            player.getPacketSender().sendEnterAmountPrompt("Enter amount:");
        }
    }
}
