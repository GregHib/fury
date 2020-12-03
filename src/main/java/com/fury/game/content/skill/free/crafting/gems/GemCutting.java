package com.fury.game.content.skill.free.crafting.gems;

import com.fury.game.container.impl.Inventory;
import com.fury.game.content.misc.items.StrangeRocks;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.dialogue.impl.skills.crafting.GemCuttingD;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.update.flag.block.Animation;

/**
 * Created by Greg on 20/12/2016.
 */
public class GemCutting extends Action {

    public static boolean isCutting(Player player, Item item1, Item item2) {
        Item gem = Inventory.contains(1755, item1, item2);
        if (gem == null)
            return false;
        return isCutting(player, gem);
    }

    private static boolean isCutting(Player player, Item use) {
        for (Gem gem : Gem.values()) {
            if (gem.getUncut().isEqual(use)) {
                cut(player, gem);
                return true;
            }
        }
        return false;
    }

    public static Gem getGem(int gemId) {
        for (Gem gem : Gem.values()) {
            if (gem.getUncut().getId() == gemId) {
                return gem;
            }
        }
        return null;
    }

    public static Gem getGemByProduce( int gemId) {
        for (Gem gem : Gem.values()) {
            if (gem.getCut().getId() == gemId) {
                return gem;
            }
        }
        return null;
    }

    public static void cut(Player player, Gem gem) {
        if (player.getInventory().getAmount(gem.getUncut()) <= 1)
            player.getActionManager().setAction(new GemCutting(gem, 1));
        else
            player.getDialogueManager().startDialogue(new GemCuttingD(), gem);
    }

    private Gem gem;
    private int quantity;
    public GemCutting(Gem gem, int quantity) {
        this.gem = gem;
        this.quantity = quantity;
    }

    public boolean checkAll(Player player) {
        if (!player.getInventory().contains(new Item(1755))) {
            player.message("You do not have the required items to cut this.");
            return false;
        }
        if (player.getSkills().getLevel(Skill.CRAFTING) < gem.getLevelRequired()) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You need a crafting level of " + gem.getLevelRequired() + " to cut that gem.");
            return false;
        }
        if (player.getInventory().getAmount(gem.getUncut()) <= 0) {
            player.getDialogueManager().startDialogue(new SimpleMessageD(), "You don't have any " + gem.getUncut().getDefinition().getName().toLowerCase() + " to cut.");
            return false;
        }
        return true;
    }

    @Override
    public boolean start(Player player) {
        if (checkAll(player)) {
            setActionDelay(player, 1);
            player.perform(new Animation(gem.getEmote()));
            return true;
        }
        return false;
    }

    @Override
    public boolean process(Player player) {
        return checkAll(player);
    }

    @Override
    public int processWithDelay(Player player) {
        player.getInventory().delete(gem.getUncut());
        player.getInventory().add(gem.getCut());
        player.getSkills().addExperience(Skill.CRAFTING, gem.getExperience());
        StrangeRocks.handleStrangeRocks(player, Skill.CRAFTING);
        player.getPacketSender().sendMessage("You cut the " + gem.getUncut().getDefinition().getName().toLowerCase() + ".", true);
        quantity--;
        if (quantity <= 0)
            return -1;
        player.perform(new Animation(gem.getEmote()));
        return 2;
    }

    @Override
    public void stop(final Player player) {
        setActionDelay(player, 3);
    }
}
