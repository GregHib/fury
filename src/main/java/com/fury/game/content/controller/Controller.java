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
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.map.Position;

public abstract class Controller {

    protected Player player;

    public final void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public final Object[] getArguments() {
        return player.getControllerManager().getLastControllerArguments();
    }

    public final void setArguments(Object[] objects) {
        player.getControllerManager().setLastControllerArguments(objects);
    }

    public final void removeController() {
        player.getControllerManager().removeControllerWithoutCheck();
    }

    public abstract void start();

    public boolean canEat(Food food) {
        return true;
    }

    public boolean canDrink(Drink drink) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean canTakeItem(FloorItem item) {
        return true;
    }

    /**
     * after the normal checks, extra checks, only called when you attacking
     */
    public boolean keepCombating(Figure target) {
        return true;
    }

    public boolean canEquip(Item item, Slot slot) {
        return true;
    }

    public boolean canRemoveEquip(int slotId, int itemId) {
        return true;
    }

    /**
     * after the normal checks, extra checks, only called when you start trying
     * to attack
     */
    public boolean canAttack(Figure target) {
        return true;
    }

    public void trackXP(int skillId, int addedXp) {

    }

    public boolean canDeleteInventoryItem(int itemId, int amount) {
        return true;
    }

    public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
        return true;
    }

    public boolean canAddInventoryItem(int itemId, int amount) {
        return true;
    }

    public boolean canPlayerOption1(Player target) {
        return true;
    }

    public boolean canPlayerOption2(Player target) {
        return true;
    }

    public boolean canPlayerOption3(Player target) {
        return true;
    }

    public boolean canPlayerOption4(Player target) {
        return true;
    }

    /**
     * hits as ice barrage and that on multi areas
     */
    public boolean canHit(Figure entity) {
        return true;
    }

    /**
     * processes every game ticket, usualy not used
     */
    public void process() {

    }

    public void moved() {

    }

    /**
     * called once teleport is performed
     */
    public void magicTeleported(int type) {

    }

    public void sendInterfaces() {

    }

    /**
     * return can use script
     */
    public boolean useDialogueScript(Object key) {
        return true;
    }

    /**
     * return can teleport
     */
    public boolean processMagicTeleport(Position toTile) {
        return true;
    }

    /**
     * return can teleport
     */
    public boolean processItemTeleport(Position toTile) {
        return true;
    }

    /**
     * return can teleport
     */
    public boolean processObjectTeleport(Position toTile) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processObjectClick1(GameObject object) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processItemClick1(Item item) {
        return true;
    }

    /**
     * return process normaly
     *
     */
    public boolean processButtonClick(int componentId) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processNPCClick1(Mob mob) {
        return true;
    }

    public boolean processNPCDistanceClick1(Mob mob) {
        return true;
    }

    public boolean processNPCDistanceClick2(Mob mob) {
        return true;
    }
    public boolean processNPCDistanceClick3(Mob mob) {
        return true;
    }
    public boolean processNPCDistanceClick4(Mob mob) {
        return true;
    }
    /**
     * return process normaly
     */
    public boolean processNPCClick2(Mob mob) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processNPCClick3(Mob mob) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processNPCClick4(Mob mob) {
        return true;
    }

    public boolean processUntouchableObjectClick1(GameObject object) {
        return true;
    }
    /**
     * return process normaly
     */
    public boolean processObjectClick2(GameObject object) {
        return true;
    }

    /**
     * return process normaly
     */
    public boolean processObjectClick3(GameObject object) {
        return true;
    }

    public boolean processObjectClick4(GameObject object) {
        return true;
    }

    public boolean processObjectClick5(GameObject object) {
        return true;
    }

    public boolean handleItemOnObject(GameObject object, Item item) {
        return true;
    }

    public void processOutgoingHit(final Hit hit) {

    }

    public void processActualHit(final Hit hit, Entity target) {

    }

    /**
     * return let default death
     */
    public boolean sendDeath() {
        return true;
    }

    /**
     * return can move that step
     */
    public boolean canMove(int dir) {
        return true;
    }

    /**
     * return can set that step
     */
    public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
        return true;
    }

    /**
     * return remove controller
     */
    public boolean login() {
        return true;
    }

    /**
     * return remove controller
     */
    public boolean logout() {
        return true;
    }

    public void forceClose() {
    }

    public boolean processItemOnNPC(Mob mob, Item item) {
        return true;
    }

    public boolean canDropItem(Item item) {
        return true;
    }

    public boolean canSummonFamiliar() {
        return true;
    }

    public boolean processItemOnPlayer(Player target, Item item, int slot) {
        return true;
    }

    public void processNPCDeath(Mob id) {

    }

    public void trackLevelUp(int skillId, int level) {

    }

    public boolean handleMagicSpells(MagicSpells spell) {
        return false;
    }

    public boolean processObjectDistanceClick(GameObject object, int opcode) {
        return true;
    }

    public boolean canBuyShopItem(Player player, Item item) {
        return true;
    }

    public boolean canSellItemShop(Player player, Item item, int amount, int shopId) {
        return true;
    }

    public boolean canAddMoneyToPouch(Player player, int amount) {
        return true;
    }

    public boolean canCast(Player player, Spell combatSpell) {
        return true;
    }
}
