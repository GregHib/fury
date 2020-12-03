package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.controller.impl.Wilderness;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * Handles Summoning Special Moves and Attacking
 *
 * @author Greg
 */
public class SummoningFamiliarPacketListener implements PacketListener {

    private static void attackPlayer(final Player player, Packet packet) {
        int index = packet.readShortA();
        if (index < 0 || index > GameWorld.getPlayers().capacity())
            return;
        Player target = GameWorld.getPlayers().get(index);
        if (target == null)
            return;

        if (player.getFamiliar() == null)
            return;
        player.getMovement().reset();

        if (!player.isCanPvp() || !target.isCanPvp()) {
            player.message("You can only attack players in a player-vs-player area.");
            return;
        }
        if (!player.getFamiliar().canAttack(target)) {
            player.message("You can only use your familiar in a multi-zone area.");
            return;
        } else {
            if(player.getControllerManager().getController() instanceof Wilderness)
                CombatConstants.skullPlayer(player);
            player.getFamiliar().setTarget(target);
        }
    }

    private static void attackNpc(final Player player, Packet packet) {
        final int index = packet.readShortA();

        if (index < 0 || index > GameWorld.getMobs().getMobs().capacity()) {
            return;
        }
        Mob mob = GameWorld.getMobs().getMobs().get(index);

        if (mob == null || !mob.getDefinition().hasAttackOption())
            return;


        if (player.getFamiliar() == null)
            return;

        player.getMovement().reset();
        if (mob == player.getFamiliar()) {
            player.message("You can't attack your own familiar.");
            return;
        }
        if (!player.getFamiliar().canAttack(mob)) {
            player.message("You can only use your familiar in a multi-zone area.");
            return;
        } else {
            player.getFamiliar().setTarget(mob);
        }
    }

    @SuppressWarnings("unused")
    public void specialOnItem(Player player, Packet packet) {
        int slot = packet.readShort();
        int itemId = packet.readShort();
        int pouchId = packet.readShort();

        if (!player.getInventory().validate(itemId, slot))
            return;

        Item item = player.getInventory().get(slot);

        if (player.getFamiliar() == null) {
            player.message("You do not have a familiar.");
            return;
        }

        player.getFamiliar().setSpecial(true);
        if (player.getFamiliar().getSpecialAttack() == Familiar.SpecialAttack.ITEM)
            if (player.getFamiliar().hasSpecialOn())
                player.getFamiliar().submitSpecial(item);
    }

    @Override
    public void handleMessage(Player player, Packet packet) {
        if (player.getHealth().getHitpoints() <= 0)
            return;
        switch (packet.getOpcode()) {
            case PacketConstants.SPECIAL_ON_ITEMS_OPCODE:
                specialOnItem(player, packet);
                break;
            case PacketConstants.FAMILIAR_ATTACK_NPC_OPCODE:
                attackNpc(player, packet);
                break;
            case PacketConstants.FAMILIAR_ATTACK_PLAYER_OPCODE:
                attackPlayer(player, packet);
                break;
        }
    }
}
