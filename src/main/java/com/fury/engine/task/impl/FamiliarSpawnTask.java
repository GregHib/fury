package com.fury.engine.task.impl;


import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.npc.familiar.Familiar;

/**
 * Familiar spawn on login
 * @author Gabriel Hannason
 */
public class FamiliarSpawnTask implements Runnable {

	private int ticks;
	private int trackTimer;
	private int specialEnergy;
	private boolean trackDrain;
	private Summoning.Pouches pouch;

	private Player player;
	public Item[] burdenItems;
	
	@Override
	public void run() {
		if(player == null || !player.isRegistered())
    		return;

		final Familiar npc = Summoning.createFamiliar(player, pouch);
		if (npc == null) {
			player.getFamiliar().dismissFamiliar(true);
			return;
		}

		npc.setSpecialEnergy(specialEnergy);
		npc.setTicks(ticks);
		npc.setTrackTimer(trackTimer);
		npc.setTrackDrain(trackDrain);
		npc.setPouch(pouch);
		if(burdenItems != null)
			npc.getBeastOfBurden().setItems(burdenItems);
		Summoning.handleSpawn(player, npc, true);

		player.getSummoning().setFamiliarSpawnTask(null);

		/*if(player.getInterfaceId() > 0) {
			player.getPacketSender().sendInterfaceRemoval();
		}
		if(BossPet.forSpawnId(familiarId) != null) {
			player.getSummoning().summonPet(BossPet.forSpawnId(familiarId), true);
		} else if(OldPets.forSpawnId(familiarId) != null) {
			player.getSummoning().summonPet(OldPets.forSpawnId(familiarId), true);
		} else {
			player.getSummoning().summon(FamiliarData.forNPCId(familiarId), false, true);
			int pouch = FamiliarData.forNPCId(familiarId).pouchId;
			if(pouch < 0) {
				pouch = 0;
			}
			player.getPacketSender().sendSummoningInfo(true, pouch);
			if(player.getSummoning().getFamiliar() != null && deathTimer > 0)
				player.getSummoning().getFamiliar().setDeathTimer(deathTimer);
			if(player.getSummoning().getFamiliar() != null && specialTimer > 0)
				player.getSummoning().getFamiliar().setSpecialTimer(specialTimer);
			if(player.getSummoning().getFamiliar() != null && specialEnergy > 0)
				player.getSummoning().getFamiliar().setSpecialEnergy(specialEnergy);
			if(this.burdenItems != null && player.getSummoning().getNewBeastOfBurden() != null) {
				player.getSummoning().getNewBeastOfBurden().clear();
				for(Item item : burdenItems) {
					if(item != null) {
						player.getSummoning().getNewBeastOfBurden().add(item);
					}
				}
			}
			if(player.isInWilderness())
				player.getSummoning().setCombat(true);
		}*/
	}


	public FamiliarSpawnTask(Player player) {
		this.player = player;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	public void setTrackTimer(int trackTimer) {
		this.trackTimer = trackTimer;
	}

	public void setSpecialEnergy(int specialEnergy) {
		this.specialEnergy = specialEnergy;
	}

	public void setTrackDrain(boolean trackDrain) {
		this.trackDrain = trackDrain;
	}

	public void setPouch(Summoning.Pouches pouch) {
		this.pouch = pouch;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setBurdenItems(Item[] burdenItems) {
		this.burdenItems = burdenItems;
	}
}
