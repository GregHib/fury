package com.fury.game.container.action.impl;

import com.fury.game.container.action.ContainerActions;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.eco.ge.GrandExchangeOffer;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class GECollectBuyActions extends ContainerActions {
    @Override
    public void first(Player player, int widget, int id, int slot) {
        GrandExchange.collectItem(player, id, slot, GrandExchangeOffer.OfferType.BUYING);
    }
}
