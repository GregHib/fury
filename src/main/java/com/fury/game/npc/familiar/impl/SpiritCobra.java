package com.fury.game.npc.familiar.impl;


import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.npc.familiar.Familiar;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class SpiritCobra extends Familiar {

	public SpiritCobra(Player owner, Summoning.Pouches pouch, Position tile) {
		super(owner, pouch, tile);
	}

	@Override
	public String getSpecialName() {
		return "Ophidian Incubation";
	}

	@Override
	public String getSpecialDescription() {
		return "Transforms a single egg from the player's inventory into a corresponding variety of Cockatrice egg.";
	}

	@Override
	public int getStoreSize() {
		return 0;
	}

	@Override
	public int getSpecialAttackEnergy() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		final Player player = (Player) object;
		final Familiar npc = this;
		GameWorld.schedule(2, () -> ProjectileManager.send(new Projectile(npc, player, 1389, 34, 16, 30, 35, 16, 0)));
		graphic(1388);
		animate(8159);
		itemLoop: for (Item item : player.getInventory().getItems()) {
			if (item == null)
				continue;
			int replacement = getChocoTriceEgg(item.getId());
			if (replacement != item.getId()) {
				int itemSlot = player.getInventory().indexOf(item);
				player.getInventory().set(new Item(replacement), itemSlot);
				break itemLoop;
			}
		}
		return true;
	}

	private int getChocoTriceEgg(int itemId) {
		switch (itemId) {
		case 1944:
			return 12109;
		case 5076:
			return 12113;
		case 5077:
			return 12115;
		case 5078:
			return 12111;
		case 11964:
			return 12119;
		case 12483:
			return 12117;
		case 11965:
			return 12121;
		}
		return itemId;
	}
}
