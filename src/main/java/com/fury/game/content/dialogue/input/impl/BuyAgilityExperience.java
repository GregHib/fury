package com.fury.game.content.dialogue.input.impl;

import com.fury.game.content.skill.Skill;
import com.fury.game.content.dialogue.input.EnterAmount;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class BuyAgilityExperience extends EnterAmount {

    @Override
    public void handleAmount(Player player, int amount) {
        player.getPacketSender().sendInterfaceRemoval();
        Item ticket = new Item(2996);
        int ticketAmount = player.getInventory().getAmount(ticket);
        if (ticketAmount == 0) {
            player.message("You do not have any tickets.");
            return;
        }
        if (ticketAmount > amount) {
            ticketAmount = amount;
        }

        if (player.getInventory().getAmount(ticket) < ticketAmount) {
            return;
        }

        int exp = ticketAmount * 154;
        player.getInventory().delete(new Item(ticket, ticketAmount));
        player.getSkills().addExperience(Skill.AGILITY, exp);
        player.message("You've bought " + (exp * player.getGameMode().getSkillRate()) + " Agility experience for " + ticketAmount + " Agility ticket" + (ticketAmount == 1 ? "" : "s") + ".");
    }

}
