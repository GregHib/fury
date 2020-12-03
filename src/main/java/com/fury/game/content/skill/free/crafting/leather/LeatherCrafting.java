package com.fury.game.content.skill.free.crafting.leather;

import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

/**
 * Created by Greg on 20/12/2016.
 */
public class LeatherCrafting extends Action {

    LeatherData leather;
    int amount, ticks;
    private boolean dungeoneering;
    private boolean polypore;
    public LeatherCrafting(LeatherData leather, int amount, boolean dungeoneering) {
        this.leather = leather;
        this.amount = amount;
        this.dungeoneering = dungeoneering;
        this.polypore = getPolypore() != -1;
    }

    private static final Item DUNG_THREAD = new Item(17447);
    public static final Item DUNG_NEEDLE = new Item(17446), NEEDLE = new Item(1733), THREAD = new Item(1734);

    @Override
    public boolean start(Player player) {
        if (!checkAll(player))
            return false;
        int leatherAmount = player.getInventory().getAmount(leather.getLeather());
        int requestedAmount = amount;
        if (requestedAmount > leatherAmount)
            requestedAmount = leatherAmount;

        setTicks(player, requestedAmount);
        return true;
    }

    public void setTicks(Player player, int ticks) {
        this.ticks = ticks;
        if (!dungeoneering)
            player.getInventory().delete(THREAD);
    }

    public boolean checkAll(Player player) {
        final int levelReq = leather.getLevelReq();
        if (!player.getSkills().hasRequirement(Skill.CRAFTING, levelReq, "craft this hide"))
            return false;

        if (player.getInventory().getAmount(leather.getLeather()) < leather.getLeatherAmount()) {
            player.message("You need at least " + leather.getLeatherAmount() + " " + leather.getLeather().getName() + "s to craft this.");
            return false;
        }
        if (!player.getInventory().contains(dungeoneering ? DUNG_THREAD : THREAD)) {
            player.message("You need a thread in order to bind the " + leather.getLeather().getName() + "s together.");
            return false;
        }
        if (!player.getInventory().contains(dungeoneering ? DUNG_NEEDLE : NEEDLE)) {
            player.message("You need a needle in order to bind the " + leather.getLeather().getName() + "s together.");
            return false;
        }

        if(polypore) {
            Item extra = new Item(getPolypore());
            if (!player.getInventory().contains(extra)) {
                player.message("You need a " + extra.getName().toLowerCase() + " to craft this.");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean process(Player player) {
        return ticks > 0 && checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        ticks--;
        if (dungeoneering)
            player.getInventory().delete(DUNG_THREAD);
        else if (ticks % 4 == 0)// will use one every time AT LEAST
            player.getInventory().delete(THREAD);
        Item item = new Item(leather.getProduct(), 1);
        player.getInventory().delete(new Item(leather.getLeather(), leather.getLeatherAmount()));
        player.getInventory().addSafe(item);
        player.getSkills().addExperience(Skill.CRAFTING, leather.getXP());
        StrangeRocks.handleStrangeRocks(player, Skill.CRAFTING);
        if(leather == LeatherData.BLUE_DHIDE_VAMBRACES)
            Achievements.finishAchievement(player, Achievements.AchievementData.CRAFT_BLUE_VAMBRACES);

        if(polypore) {
            Item extra = new Item(getPolypore());
            player.getInventory().delete(extra);
        }
        ChristmasEvent.giveSnowflake(player);
        player.perform(new Animation(dungeoneering ? 13247 : 1249));
        return 3;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

    private int getPolypore() {
        switch (leather) {
            case GANODERMIC_PONCHO:
            case FUNGAL_PONCHO:
            case GRIFOLIC_PONCHO:
                return 22456;
            case GANODERMIC_LEGGINGS:
            case FUNGAL_LEGGINGS:
            case GRIFOLIC_LEGGINGS:
                return 22454;
            case GANODERMIC_VISOR:
            case FUNGAL_VISOR:
            case GRIFOLIC_VISOR:
                return 22452;
            default:
                return -1;
        }
    }
}
