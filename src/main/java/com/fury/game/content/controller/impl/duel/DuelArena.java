package com.fury.game.content.controller.impl.duel;

import com.fury.core.task.Task;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.controller.impl.Rule;
import com.fury.game.content.dialogue.impl.minigames.ForfeitD;
import com.fury.game.content.dialogue.impl.misc.SimpleMessageD;
import com.fury.game.content.skill.free.cooking.Food;
import com.fury.game.content.skill.member.herblore.Drink;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.actions.PlayerCombatAction;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.PlayerDeath;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class DuelArena extends Controller {

    //TODO make sure can't use veng?
    private final Item[] FUN_WEAPONS = {};

    @Override
    public void start() {
        player.stopAll();
        player.addStopDelay(2); // fixes mass click steps
        player.getTemporaryAttributes().put("startedDuel", true);
        player.getTemporaryAttributes().put("canFight", false);
        player.reset();
        player.setCanPvp(true);
        player.getPacketSender().sendPositionalHint(player.getDuelConfigurations().getOther(player).copyPosition(), 10);
        player.getPacketSender().sendEntityHint(player.getDuelConfigurations().getOther(player));
        GameWorld.schedule(new Task(true, 2) {
            int count = 3;
            @Override
            public void run() {
                if (count > 0)
                    player.forceChat("" + count);

                if (count == 0) {
                    player.getTemporaryAttributes().put("canFight", true);
                    player.forceChat("FIGHT!");
                    this.stop();
                }
                count--;
            }
        });
    }

    @Override
    public boolean canEat(Food food) {
        if (player.getDuelConfigurations().getRule(Rule.NO_FOOD)) {
            player.message("You cannot eat during this duel.", true);
            return false;
        }
        return true;
    }

    @Override
    public boolean canDrink(Drink drink) {
        if (player.getDuelConfigurations().getRule(Rule.NO_DRINKS)) {
            player.message("You cannot drink during this duel.", true);
            return false;
        }
        return true;
    }

    @Override
    public boolean canMove(int dir) {
        if (player.getDuelConfigurations().getRule(Rule.NO_MOVEMENT)) {
            player.message("You cannot move during this duel!", true);
            return false;
        }
        return true;
    }

    @Override
    public boolean processMagicTeleport(Position toTile) {
        player.getDialogueManager().startDialogue(new SimpleMessageD(), "A magical force prevents you from teleporting from the arena.");
        return false;
    }

    @Override
    public boolean processItemTeleport(Position toTile) {
        player.getDialogueManager().startDialogue(new SimpleMessageD(), "A magical force prevents you from teleporting from the arena.");
        return false;
    }

    @Override
    public boolean processButtonClick(int buttonId) {
        switch (buttonId) {
            case 271:
                if (player.getDuelConfigurations().getRule(Rule.NO_PRAYER)) {
                    player.message("You can't use prayers in this duel.");
                    return false;
                }
                return true;
            case 193:
            case 430:
            case 192:
                if (player.getDuelConfigurations().getRule(Rule.NO_MAGIC))
                    return false;
                return true;
            case 884:
                switch (buttonId) {
                    case 4:
                        if (player.getDuelConfigurations().getRule(Rule.NO_SPECIAL_ATTACK)) {
                            player.message("You can't use special attacks in this duel.");
                            return false;
                        }
                        return true;
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        player.getDialogueManager().startDialogue(new ForfeitD());
        return true;
    }

    @Override
    public boolean sendDeath() {
        player.addStopDelay(7);
        GameWorld.schedule(new PlayerDeath(
                player,
                    player.copyPosition(), GameSettings.DEFAULT_POSITION, false, () ->{
                    player.animate(-1);
                    player.setCanPvp(false);
                    Player other =player.getDuelConfigurations().getOther(player);player.getDuelConfigurations().reward(other);
                     player.getDuelConfigurations().addSpoils(other);
                    player.getDuelConfigurations().endDuel(player, false, false);
                    player.getDuelConfigurations().endDuel(other, false, false);
                    removeController();
                    player.getPacketSender().sendInterface(6733);player.getControllerManager().startController(new DuelController());
                    other.getControllerManager().startController(new DuelController());
                    }));
        return false;
    }

    @Override
    public boolean login() {
        return true;
    }

    @Override
    public boolean logout() {
        player.getDuelConfigurations().endDuel(player, false, true);
        player.getDuelConfigurations().endDuel(player.getDuelConfigurations().getOther(player), false, false); // other
        // player
        return false;
    }

    @Override
    public boolean keepCombating(Figure victim) {
        boolean isRanging = PlayerCombatAction.isRanging(player) != 0;
        System.out.println(player.getTemporaryAttributes().get("canFight"));
        if (player.getTemporaryAttributes().get("canFight") == Boolean.FALSE) {
            player.message("The duel hasn't started yet.",
                    true);
            return false;
        }
        if (player.getDuelConfigurations().getOther(player) != victim)
            return false;
        if ((player.getCastSpell() != null || player.getAutoCastSpell() != null) && player.getDuelConfigurations().getRule(Rule.NO_MAGIC)) {
            player.message("You cannot use Magic in this duel!", true);
            return false;
        } else if (isRanging && player.getDuelConfigurations().getRule(Rule.NO_RANGED)) {
            player.message("You cannot use Range in this duel!", true);
            return false;
        } else if (!isRanging && player.getDuelConfigurations().getRule(Rule.NO_MELEE) && player.getAutoCastSpell() == null) {
            player.message("You cannot use Melee in this duel!", true);
            return false;
        } else {
            for (Item item : FUN_WEAPONS) {
                if (player.getDuelConfigurations().getRule(Rule.LOCK_WEAPONS) && !player.getInventory().containsAmount(item)) {
                    player.message("You can only use fun weapons in this duel!");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canEquip(Item item, Slot slot) {
        if (player.getDuelConfigurations().getRule(Rule.values()[11 + slot.ordinal()])) {
            player.message("You can't equip " + item.getName().toLowerCase() + " during this duel.");
            return false;
        }
        if (slot == Slot.WEAPON && Equipment.isTwoHandedWeapon(item) && player.getDuelConfigurations().getRule(Rule.NO_SHIELD)) {
            player.message("You can't equip " + item.getName().toLowerCase() + " during this duel.");
            return false;
        }
        return true;
    }
}
