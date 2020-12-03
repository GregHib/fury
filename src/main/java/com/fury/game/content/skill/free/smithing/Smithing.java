package com.fury.game.content.skill.free.smithing;

import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.smithing.SmithingData.SmithData;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Sounds;
import com.fury.game.entity.character.player.content.Sounds.Sound;
import com.fury.core.model.item.Item;

public class Smithing extends Action {

    private Item bar;
    private SmithData smith;
    private int amount;
    private int amountMade = 0;

    public Smithing(Item bar, SmithData smith, int amount) {
        this.bar = bar;
        this.smith = smith;
        this.amount = amount;
    }


    public static void handleAnvil(Player player) {
        SmeltingData bar = searchForBars(player);
        if (bar == null) {
            player.message("You do not have any bars in your inventory to smith.");
        } else {
            handleAnvil(player, bar.getBar().getId());
        }
    }

    public static void handleAnvil(Player player, int itemId) {
        switch (itemId) {
            case 2349:
                player.setSelectedSkillingItem(new Item(2349));
                SmithingData.showBronzeInterface(player);
                break;
            case 2351:
                player.setSelectedSkillingItem(new Item(2351));
                SmithingData.makeIronInterface(player);
                break;
            case 2353:
                player.setSelectedSkillingItem(new Item(2353));
                SmithingData.makeSteelInterface(player);
                break;
            case 2359:
                player.setSelectedSkillingItem(new Item(2359));
                SmithingData.makeMithInterface(player);
                break;
            case 2361:
                player.setSelectedSkillingItem(new Item(2361));
                SmithingData.makeAddyInterface(player);
                break;
            case 2363:
                player.setSelectedSkillingItem(new Item(2363));
                SmithingData.makeRuneInterface(player);
                break;
        }
    }

    public static SmeltingData searchForBars(Player player) {
        for (SmeltingData bar : SmeltingData.values()) {
            if (player.getInventory().contains(bar.getBar())) {
                return bar;
            }
        }
        return null;
    }


    private boolean checkReq(Player player) {
        if (!player.getInventory().contains(new Item(2347)) && !player.getInventory().contains(new Item(2949))) {
            player.message("You need a Hammer to smith items.");
            player.getPacketSender().sendInterfaceRemoval();
            return false;
        }

        if (player.getInventory().getAmount(bar) < bar.getAmount() || amount <= 0) {
            player.message("You do not have enough bars to smith this item.");
            return false;
        }

        if (player.getSmithingInterfaceType() != getType())
            return false;

        if (!player.getSkills().hasRequirement(Skill.SMITHING, smith.getLevelReq(), "make this item"))
            return false;

        return true;
    }

    public static void smithItem(final Player player, final Item bar, final Item itemToSmith, final int amount) {
        SmithData smith = SmithData.forId(itemToSmith);
        if (smith == null)
            return;

        player.getActionManager().setAction(new Smithing(bar, smith, amount));
    }

    @Override
    public boolean start(Player player) {
        player.getPacketSender().sendInterfaceRemoval();

        if (bar.getId() < 0)
            return false;

        if (!player.getTimers().getClickDelay().elapsed(1100))
            return false;

        return checkReq(player);
    }

    @Override
    public boolean process(Player player) {
        if (player.getInventory().getAmount(bar) < bar.getAmount() || (!player.getInventory().contains(new Item(2347)) && !player.getInventory().contains(new Item(2949))) || amountMade >= amount)
            return false;
        return checkReq(player);
    }

    @Override
    public int processWithDelay(Player player) {
        if (player.getInteractingObject() != null)
            player.getInteractingObject().graphic(2123);
        player.animate(898);
        amountMade++;
        Sounds.sendSound(player, Sound.SMITH_ITEM);
        player.getInventory().delete(bar);
        player.getInventory().add(new Item(smith.getId(), SmithingData.getItemAmount(new Item(smith.getId()))));
        player.getInventory().refresh();
        player.getSkills().addExperience(Skill.SMITHING, smith.getExperience(), player.getInventory().contains(new Item(2949)) ? 1.01 : 1.0);
        if (smith == SmithingData.SmithData.BRONZE_DAGGER)
            Achievements.finishAchievement(player, Achievements.AchievementData.SMITH_A_BRONZE_DAGGER);
        StrangeRocks.handleStrangeRocks(player, Skill.SMITHING);
        ChristmasEvent.giveSnowflake(player);
        return 3;
    }

    @Override
    public void stop(Player player) {
        setActionDelay(player, 3);
    }

    public int getType() {
        switch (smith.name().split("_")[0].toLowerCase()) {
            case "bronze":
                return 1;
            case "iron":
                return 2;
            case "steel":
            case "cannon":
                return 3;
            case "mithril":
                return 4;
            case "adamant":
                return 5;
            case "rune":
                return 6;
        }
        return 0;
    }
}
