package com.fury.game.entity.character.player.content;

import com.fury.game.container.impl.Inventory;
import com.fury.game.container.impl.bank.Bank;
import com.fury.core.model.node.entity.actor.figure.player.Player;

public class PlayerRightsData {

    Player player;

    public Bank previewBank;

    public Inventory previewInventory;

    public PlayerRightsData(Player player) {
        this.player = player;
    }


}
