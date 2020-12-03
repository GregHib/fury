package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.combat.magic.Spell;
import com.fury.core.model.node.entity.actor.figure.combat.magic.context.SpellOnObjectContext;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.combat.magic.spell.modern.skill.charge.ChargeOrbSpell;
import com.fury.game.content.misc.objects.OrbCharging;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.path.RouteEvent;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketConstants;
import com.fury.network.packet.PacketListener;

/**
 * Handles magic on objects.
 *
 * @author Greg
 */
public class MagicOnObjectPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int opcode = packet.getOpcode();
        switch (opcode) {
            case PacketConstants.MAGIC_ON_OBJECT_OPCODE:
                handleObjects(player, packet);
                break;
        }
    }

    private void handleObjects(Player player, Packet packet) {
        int x = packet.readLEShort();
        int spellId = packet.readInt();
        int y = packet.readUnsignedShortA();
        int objectId = packet.readLEShort();

        final Position position = new Position(x, y, player.getZ() % 4);

        final GameObject object = PrivateObjectManager.isPrivateObject(player, objectId) ? PrivateObjectManager.getObject(player, objectId) : ObjectManager.getObjectWithId(objectId, position);

        if (object == null || !object.validate(objectId)) {
            if (player.getRights() == PlayerRights.DEVELOPER)
                player.getPacketSender().sendConsoleMessage("Unregistered Object[" + packet.getOpcode() + ", " + objectId + ", " + position.toString() + "]");

            if (player.getControllerManager().getController() instanceof DungeonController)
                player.message("Error verifying object, please relog to fix.");
            return;
        }
        if (object.getId() != objectId)
            object.setId(objectId);

        if (player.getRights() == PlayerRights.DEVELOPER)
            player.getPacketSender().sendConsoleMessage("Object[" + packet.getOpcode() + ", " + objectId + ", " + position.toString() + "]");

        if (!player.getTimers().getClickDelay().elapsed(1300))
            return;

        MagicSpells magicSpell = MagicSpells.forSpellId(spellId);
        if (magicSpell == null)
            return;

        player.stopAll(false);
        player.setRouteEvent(new RouteEvent(object, () -> {
            // unreachable objects exception
            player.getDirection().faceObject(object);

            handleSpell(player, magicSpell, object);
        }));
    }

    private static void handleSpell(Player player, MagicSpells magicSpell, GameObject object) {
        Spell spell = magicSpell.getSpell();
        if (spell == null)
            return;

        if(spell instanceof ChargeOrbSpell) {
            if (!spell.canCast(player))
                return;

            ChargeOrbSpell orbSpell = (ChargeOrbSpell) spell;
            if(orbSpell.getOrb().getObjectId() == object.getId())
                player.getActionManager().setAction(new OrbCharging(orbSpell, object, player.getInventory().getAmount(ChargeOrbSpell.getUNPOWERED_ORB())));
        } else {
            if(spell.cast(player, new SpellOnObjectContext(object)))
                player.getTimers().getClickDelay().reset();
        }

    }
}
