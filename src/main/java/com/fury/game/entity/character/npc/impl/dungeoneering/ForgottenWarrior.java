package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.content.skill.free.dungeoneering.DungeonUtils;
import com.fury.game.content.skill.free.dungeoneering.RoomReference;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ForgottenWarrior extends Guardian {

	public ForgottenWarrior(int id, Position tile, DungeonManager manager, RoomReference reference, double multiplier) {
		super(id, tile, manager, reference, multiplier);
	}

	@Override
	public List<Drop> addDrops(Player killer) {
		ArrayList<Item> items = new ArrayList<>();
		//just 1 for now
		for (int type = 0; type < DungeonConstants.FORGOTTEN_WARRIORS.length; type++) {
			for (int id : DungeonConstants.FORGOTTEN_WARRIORS[type])
				if (id == getId()) {
					int tier = getManager().getParty().getAverageCombatLevel() / 10;
					if (tier > 10)
						tier = 10;
					else if (tier < 1)
						tier = 1;
					//melee mage range
					if (type == 0)
						items.add(new Item(DungeonUtils.getRandomMeleeGear(Misc.random(tier) + 1)));
					else if (type == 1)
						items.add(new Item(DungeonUtils.getRandomMagicGear(Misc.random(tier) + 1)));
					else
						items.add(new Item(DungeonUtils.getRandomRangeGear(Misc.random(tier) + 1)));
					break;
				}
		}
		List<Drop> drops = new ArrayList<>();
		for (Item item : items)
			drops.add(new Drop(item.getId(), item.getRevision(), 100.0, item.getAmount(), item.getAmount(), false));
		return drops;
	}

}
