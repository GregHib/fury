package com.fury.network.packet.impl;

import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.combat.magic.CombatSpells;
import com.fury.game.entity.character.combat.magic.MagicSpells;
import com.fury.game.node.entity.actor.figure.player.PlayerCombat;
import com.fury.game.world.GameWorld;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;

public class MagicOnPlayerPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		int playerIndex = packet.readShortA();
		if(playerIndex < 0 || playerIndex > GameWorld.getPlayers().capacity())
			return;
		int spellId = packet.readInt();
		if (spellId < 0) {
			return;
		}

		Player player2 = GameWorld.getPlayers().get(playerIndex);

		if (player2 == null || player2.equals(player)) {
			player.getMovement().reset();
			return;
		}

		if(MagicSpells.handleMagicSpells(player, spellId, player2))
			return;

		CombatSpell combatSpell = CombatSpells.getSpell(spellId);
		if(combatSpell == null) {
			player.getMovement().reset();
			return;
		}
		
		if(player2.getHealth().getHitpoints() <= 0) {
			player.getMovement()
					.reset();
			return;
		}
		
		// Start combat!
		player.getDirection().face(player2);
		player.getCombat().resetCombat();
		player.setCastSpell(combatSpell);
		if(player.getCombat() instanceof PlayerCombat)
			((PlayerCombat) player.getCombat()).target(player2);
	}

}
