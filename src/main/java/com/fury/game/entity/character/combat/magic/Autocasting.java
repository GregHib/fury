package com.fury.game.entity.character.combat.magic;

import com.fury.core.model.node.entity.actor.figure.player.Player;

public class Autocasting {

	public static boolean handleAutoCast(final Player player, int actionButtonId) {
		switch(actionButtonId) {
		case 6666:
			resetAutoCast(player, true);
			return true;
		case 6667:
			resetAutoCast(player, false);
			return true;
		}

		for(CombatSpells spell : CombatSpells.values()) {
			if(spell.getSpell().getId() == actionButtonId || (spell.getDungButton() != -1 && spell.getDungButton() == actionButtonId)) {
				if(!spell.getSpell().hasRequiredLevel(player)) {
					resetAutoCast(player, true);
					return true;
				}
				player.setAutoCast(true);
				player.setAutoCastSpell(spell.getSpell());
				player.setCastSpell(spell.getSpell());
				player.getPacketSender().sendAutocastId(player.getAutoCastSpell().getId());
				player.getPacketSender().sendConfig(108, 1);
			}
		}
		return false;
	}
	
	public static void onLogin(Player player) {
		if(player.getAutoCastSpell() == null || !player.isAutoCast())
			resetAutoCast(player, true);
		else {
			player.getPacketSender().sendAutocastId(player.getAutoCastSpell().getId());
		}
	}
	
	public static void resetAutoCast(Player player, boolean sendClient) {
		if(sendClient)
			player.getPacketSender().sendAutocastId(-1);
		player.setAutoCast(false);
		player.setAutoCastSpell(null);
		player.setCastSpell(null);
		player.getPacketSender().sendConfig(108, 3);
	}
}
