package com.fury.game.entity.character.npc.impl.dungeoneering;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.game.content.skill.free.dungeoneering.DungeonManager;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DungeonSlayerMob extends DungeonMob {

	public DungeonSlayerMob(int id, Position tile, DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier, id > Loader.getTotalNpcs(Revision.RS2) ? Revision.PRE_RS3 : Revision.RS2); // remove revision argument once converted to Kotlin
	}

	@Override
	public List<Drop> addDrops(Player killer) {
		ArrayList<Item> items = new ArrayList<>();
		if (getId() == 10694) {
			if (Misc.random(2) == 0)
				items.add(new Item(17261));
			else if (Misc.random(10) == 0)
				items.add(new Item(17263));
		} else if (getId() == 10695) {
			if (Misc.random(2) == 0)
				items.add(new Item(17265));
			else if (Misc.random(10) == 0)
				items.add(new Item(17267));
		} else if (getId() == 10696) {
			if (Misc.random(2) == 0)
				items.add(new Item(17269));
			else if (Misc.random(10) == 0)
				items.add(new Item(17271));
		} else if (getId() == 10697) {
			if (Misc.random(2) == 0)
				items.add(new Item(17273));
		} else if (getId() == 10698) {
			if (Misc.random(10) == 0)
				items.add(new Item(17279));
		} else if (getId() == 10699) {
			if (Misc.random(2) == 0)
				items.add(new Item(17281));
			else if (Misc.random(10) == 0)
				items.add(new Item(17283));
		} else if (getId() == 10700) {
			if (Misc.random(2) == 0)
				items.add(new Item(17285));
			else if (Misc.random(10) == 0)
				items.add(new Item(17287));
		} else if (getId() == 10701) {
			if (Misc.random(10) == 0)
				items.add(new Item(17289));
		} else if (getId() == 10702) {
			if (Misc.random(10) == 0)
				items.add(new Item(17293));
		} else if (getId() == 10704) {
			if (Misc.random(10) == 0)
				items.add(new Item(17291));
		} else if (getId() == 10705) {
			if (Misc.random(10) == 0)
				items.add(new Item(17295));
		}

		List<Drop> drops = new ArrayList<>();
		for (Item item : items)
			drops.add(new Drop(item.getId(), item.getRevision(), 100.0, item.getAmount(), item.getAmount(), false));
		return drops;
	}

}
