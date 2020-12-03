package com.fury.game.content.controller;

import com.fury.core.model.item.FloorItem;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.Entity;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.skill.free.cooking.Food;
import com.fury.game.content.skill.member.herblore.Drink;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;

import java.io.Serializable;


public final class ControllerManager implements Serializable {

    private Player player;
    private Controller controller;
    private boolean inited;
    private Object[] lastControllerArguments;

    private String lastController;

    public static final String START_CONTROLLER = "NewPlayerController";

    public ControllerManager() {
//        lastController = START_CONTROLLER;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Controller getController() {
        return controller;
    }

    public void startController(Object key, Object... parameters) {
        if (controller != null)
            forceStop();
        controller = (Controller) (key instanceof Controller ? key : ControllerHandler.getController(key));
        if (controller == null)
            return;
        controller.setPlayer(player);
        lastControllerArguments = parameters;
        lastController = key.getClass().getSimpleName();
        controller.start();
        inited = true;
        //Logger.globalLog(player.getUsername(), player.getTheSession().getIP(), new String(" started controller: " + key.toString() + "."));
    }

    public void login() {
        if (lastController == null)
            return;
        controller = ControllerHandler.getController(lastController);
        if (controller == null) {
            forceStop();
            return;
        }
        controller.setPlayer(player);
        if (controller.login())
            forceStop();
        else
            inited = true;
    }

    public void logout() {
        if (controller == null)
            return;
        if (controller.logout())
            forceStop();
    }

    public boolean canMove(int dir) {
        if (controller == null || !inited)
            return true;
        return controller.canMove(dir);
    }

    public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
        if (controller == null || !inited)
            return true;
        return controller.checkWalkStep(lastX, lastY, nextX, nextY);
    }

    public boolean canTakeItem(FloorItem item) {
        if (controller == null || !inited)
            return true;
        return controller.canTakeItem(item);
    }

    public boolean keepCombating(Figure target) {
        if (controller == null || !inited)
            return true;
        return controller.keepCombating(target);
    }

    public boolean canEquip(Item item, Slot slot) {
        if (controller == null || !inited)
            return true;
        return controller.canEquip(item, slot);
    }

    public boolean canRemoveEquip(int slotId, int itemId) {
        if (controller == null || !inited)
            return true;
        return controller.canRemoveEquip(slotId, itemId);
    }

    public boolean canAddInventoryItem(int itemId, int amount) {
        if (controller == null || !inited)
            return true;
        return controller.canAddInventoryItem(itemId, amount);
    }

    public void trackXP(int skillId, int addedXp) {
        if (controller == null || !inited)
            return;
        controller.trackXP(skillId, addedXp);
    }

    public void trackLevelUp(int skillId, int level) {
        if (controller == null || !inited)
            return;
        controller.trackLevelUp(skillId, level);
    }

    public boolean canDeleteInventoryItem(int itemId, int amount) {
        if (controller == null || !inited)
            return true;
        return controller.canDeleteInventoryItem(itemId, amount);
    }

    public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
        if (controller == null || !inited)
            return true;
        return controller.canUseItemOnItem(itemUsed, usedWith);
    }

    public boolean canAttack(Figure entity) {
        if (controller == null || !inited)
            return true;
        return controller.canAttack(entity);
    }

    public boolean canPlayerOption1(Player target) {
        if (controller == null || !inited)
            return true;
        return controller.canPlayerOption1(target);
    }

    public boolean canPlayerOption2(Player target) {
        if (controller == null || !inited)
            return true;
        return controller.canPlayerOption2(target);
    }

    public boolean canPlayerOption3(Player target) {
        if (controller == null || !inited)
            return true;
        return controller.canPlayerOption3(target);
    }

    public boolean canPlayerOption4(Player target) {
        if (controller == null || !inited)
            return true;
        return controller.canPlayerOption4(target);
    }

    public boolean canHit(Figure entity) {
        if (controller == null || !inited)
            return true;
        return controller.canHit(entity);
    }

    public void moved() {
        if (controller == null || !inited)
            return;
        controller.moved();
    }

    public void magicTeleported(int type) {
        if (controller == null || !inited)
            return;
        //player.getAppearence().setRenderEmote(-1);
        controller.magicTeleported(type);
    }

    public void sendInterfaces() {
        if (controller == null || !inited)
            return;
        controller.sendInterfaces();
    }

    public void process() {
        if (controller == null || !inited)
            return;
        controller.process();
    }

    public boolean sendDeath() {
        if (controller == null || !inited)
            return true;
        return controller.sendDeath();
    }

    public boolean canEat(Food food) {
        if (controller == null || !inited)
            return true;
        return controller.canEat(food);
    }

    public boolean canDrink(Drink drink) {
        if (controller == null || !inited)
            return true;
        return controller.canDrink(drink);
    }

    public boolean useDialogueScript(Object key) {
        if (controller == null || !inited)
            return true;
        return controller.useDialogueScript(key);
    }

    public boolean processTeleport(TeleportType teleType, Position toTile) {
        if (controller == null || !inited)
            return true;
        switch(teleType) {
            case NORMAL:
            case ANCIENT:
            case LUNAR:
                return processMagicTeleport(toTile);
            case RING_TELE:
            case ARDOUGNE_CLOAK:
            case FAIRY_RING_TELE:
            case SCROLL_TELEPORT:
            case TELE_TAB:
                return processItemTeleport(toTile);
            case CABBAGE_TELEPORT:
            case LEVER:
            case SPIRIT_TREE:
                return processObjectTeleport(toTile);
        }
        return processMagicTeleport(toTile);
    }

    public boolean processMagicTeleport(Position toTile) {
        if (controller == null || !inited)
            return true;
        return controller.processMagicTeleport(toTile);
    }

    public boolean processItemTeleport(Position toTile) {
        if (controller == null || !inited)
            return true;
        return controller.processItemTeleport(toTile);
    }

    public boolean processObjectTeleport(Position toTile) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectTeleport(toTile);
    }

    public boolean processObjectClick1(GameObject object) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectClick1(object);
    }

    public boolean processItemClick1(Item item) {
        if (controller == null || !inited)
            return true;
        return controller.processItemClick1(item);
    }

    public boolean processButtonClick(int componentId) {
        if (controller == null || !inited)
            return true;
        return controller.processButtonClick(componentId);
    }

    public boolean processNPCClick1(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCClick1(mob);
    }

    public boolean processNPCDistanceClick1(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCDistanceClick1(mob);
    }

    public boolean processNPCDistanceClick2(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCDistanceClick2(mob);
    }

    public boolean processNPCDistanceClick3(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCDistanceClick3(mob);
    }

    public boolean processNPCDistanceClick4(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCDistanceClick4(mob);
    }

    public boolean canSummonFamiliar() {
        if (controller == null || !inited)
            return true;
        return controller.canSummonFamiliar();
    }

    public boolean processNPCClick2(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCClick2(mob);
    }

    public boolean processNPCClick3(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCClick3(mob);
    }

    public boolean processNPCClick4(Mob mob) {
        if (controller == null || !inited)
            return true;
        return controller.processNPCClick4(mob);
    }

    public boolean processObjectClick2(GameObject object) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectClick2(object);
    }

    public boolean processObjectClick3(GameObject object) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectClick3(object);
    }

    public boolean processItemOnNPC(Mob mob, Item item) {
        if (controller == null || !inited)
            return true;
        return controller.processItemOnNPC(mob, item);
    }

    public boolean canDropItem(Item item) {
        if (controller == null || !inited)
            return true;
        return controller.canDropItem(item);
    }

    public void forceStop() {
        if (controller != null) {
            controller.forceClose();
            controller = null;
        }
        lastControllerArguments = null;
        lastController = null;
        inited = false;
        //Logger.globalLog(player.getUsername(), player.getTheSession().getIP(), new String(" current controller has been stopped."));
    }

    public void removeControllerWithoutCheck() {
        controller = null;
        lastControllerArguments = null;
        lastController = null;
        inited = false;
        //Logger.globalLog(player.getUsername(), player.getTheSession().getIP(), new String(" current controller has been stopped."));
    }

    public void setLastController(String controller, Object... args) {
        lastController = controller;
        lastControllerArguments = args;
    }

    public Object[] getLastControllerArguments() {
        return lastControllerArguments;
    }

    public String getLastController() {
        return lastController;
    }

    public void setLastControllerArguments(Object[] arguments) {
        this.lastControllerArguments = arguments;
    }

    public boolean processObjectClick4(GameObject object) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectClick4(object);
    }

    public boolean processObjectClick5(GameObject object) {
        if (controller == null) {
            return true;
        }
        if (!inited) {
            return true;
        }
        return controller.processObjectClick5(object);
    }

    public boolean handleItemOnObject(GameObject object, Item item) {
        if (controller == null || !inited)
            return true;
        return controller.handleItemOnObject(object, item);
    }

    public boolean processItemOnPlayer(Player target, Item item, int slot) {
        if (controller == null || !inited)
            return true;
        return controller.processItemOnPlayer(target, item, slot);
    }

    public void processNpcDeath(Mob id) {
        if (controller == null || !inited)
            return;
        controller.processNPCDeath(id);
    }

    public void processActualHit(Hit hit, Entity target) {
        if (controller == null || !inited)
            return;
        controller.processActualHit(hit, target);
    }

    public void processOutgoingHit(Hit hit) {
        if (controller == null || !inited)
            return;
        controller.processOutgoingHit(hit);
    }

    public boolean handleMagicSpells(MagicSpells spell) {
        if(controller == null || !inited)
            return false;
        return controller.handleMagicSpells(spell);
    }

    public boolean processUntouchableObjectClick1(GameObject object) {
        if (controller == null || !inited)
            return true;
        return controller.processUntouchableObjectClick1(object);
    }

    public boolean canTrade() {
        return true;
    }

    public boolean processObjectDistanceClick(GameObject object, int opcode) {
        if (controller == null || !inited)
            return true;
        return controller.processObjectDistanceClick(object, opcode);
    }

    public boolean canBuyShopItem(Player player, Item item) {
        if (controller == null || !inited)
            return true;
        return controller.canBuyShopItem(player, item);
    }

    public boolean canSellItemShop(Player player, Item item, int amount, int shopId) {
        if (controller == null || !inited)
            return true;
        return controller.canSellItemShop(player, item, amount, shopId);
    }

    public boolean canAddMoneyToPouch(Player player, int amount) {
        if (controller == null || !inited)
            return true;
        return controller.canAddMoneyToPouch(player, amount);
    }

    public boolean canCast(Spell castSpell) {
        if (controller == null || !inited)
            return true;
        return controller.canCast(player, castSpell);
    }
}
