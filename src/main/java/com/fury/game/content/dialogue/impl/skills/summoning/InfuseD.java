package com.fury.game.content.dialogue.impl.skills.summoning;

import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.dialogue.input.impl.EnterAmountToMake;
import com.fury.game.content.skill.member.summoning.Infusion;
import com.fury.game.content.skill.member.summoning.Scroll;

/**
 * Created by Greg on 23/11/2016.
 */
public class InfuseD extends Dialogue {
    @Override
    public void start() {
        int id = (int) parameters[0];
        int slot = (int) parameters[1];
        parameters = new Object[3];
        parameters[1] = slot;
        parameters[2] = id;
        player.getPacketSender().sendEnterAmountPrompt("How many " + (Scroll.isScroll(id) ? "scrolls" : "pouches") + " would you like to " + (Scroll.isScroll(id) ? "transform" : "infuse") + "?");
        player.setInputHandling(new EnterAmountToMake());
    }

    @Override
    public void run(int optionId) {
        int quantity = (int) parameters[0];
        int slot = optionId;
        int id = (int) parameters[2];
        Infusion.handleInfusionInterface(player, slot, id, quantity);
    }
}