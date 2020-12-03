package com.fury.game.content.dialogue.impl.skills.herblore;

import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.member.herblore.Drink;
import com.fury.game.content.skill.member.herblore.Drinkables;
import com.fury.game.content.skill.member.herblore.Herblore;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class FlaskDecantingD extends Action {

    private Drink potion, flask;
    private int potionSlot, flaskSlot, ticks;

    public FlaskDecantingD(Drink potion, int potionSlot, Drink flask, int flaskSlot) {
        this.potion = potion;
        this.flask = flask;
        this.potionSlot = potionSlot;
        this.flaskSlot = flaskSlot;
        if(flask == null) {
            for (Drink pot : Drink.values()) {
                if (pot.ordinal() == potion.ordinal() || !pot.name().replace("_FLASK", "").equals(potion.name().replace("_POTION", ""))) {
                    continue;
                }
                this.flask = pot;
                break;
            }
        }
    }

    @Override
    public boolean start(Player player) {
        ticks = 1;
        return checkAll(player);
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player) && ticks > 0;
    }

    @Override
    public int processWithDelay(Player player) {
        ticks--;
        Item jar = player.getInventory().get(flaskSlot);
        Item potion = player.getInventory().get(potionSlot);
        if (jar == null || potion == null)
            return -1;
        int requiredDoses = flask.getMaxDoses() - Drinkables.getDoses(flask, jar);
        int potionDoses = Drinkables.getDoses(this.potion, potion);
        int reducedDoses = ((requiredDoses - potionDoses) * -1);
        player.getInventory().set(new Item(requiredDoses >= potionDoses ? Herblore.EMPTY_VIAL.getId() : this.potion.getIdForDoses(reducedDoses)), potionSlot);
        if (requiredDoses > potionDoses) {
            requiredDoses = potionDoses;
        }
        int totalDoses = requiredDoses + Drinkables.getDoses(flask, jar);
        player.getInventory().set(new Item(flask.getIdForDoses(totalDoses > 6 ? 6 : totalDoses)), flaskSlot);
        player.getInventory().refresh();
        return 1;
    }

    @Override
    public void stop(Player player) {

    }

    private boolean checkAll(Player player) {
        int maxDoses = potion.getMaxDoses();

        if(!flask.name().replace("_FLASK", "").equals(potion.name().replace("_POTION", "")))
            return false;

        if (maxDoses > 4) {
            System.out.println("Error in pot: " + potion.getIdForDoses(maxDoses));
            return false;
        } else if (flaskSlot == -1) {
            return false;
        } else if (potionSlot == -1) {
            return false;
        } else if (player.getInventory().get(flaskSlot).getId() == flask.getIdForDoses(flask.getMaxDoses())) {
            if (flaskSlot == -1)
                return false;
        }
        return true;
    }
}
