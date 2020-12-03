package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.map.Position;

public class AdeptWorldbearer extends Familiar {

	public AdeptWorldbearer(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Second Wind";
	}

	@Override
	public String getSpecialDescription() {
		return "Each use of the scroll restores up to 37% of the player's run energy.";
	}

	@Override
	public int getStoreSize() {
		return 28;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 2;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int runEnergy = (int) (player.getSettings().getInt(Settings.RUN_ENERGY) * 1.37);
		if (player.getSettings().getInt(Settings.RUN_ENERGY) == 100) {
			player.message("Your run energy is completely full.");
			return false;
		}
		player.getSettings().set(Settings.RUN_ENERGY, runEnergy > 100 ? 100 : runEnergy);
		getOwner().graphic(1300);
		getOwner().animate(7660);
		return true;
	}
}
