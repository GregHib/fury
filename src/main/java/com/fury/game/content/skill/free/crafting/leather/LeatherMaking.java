package com.fury.game.content.skill.free.crafting.leather;

import com.fury.cache.Revision;
import com.fury.game.content.dialogue.impl.skills.crafting.MakeLeatherD;
import com.fury.game.content.dialogue.input.impl.EnterAmountOfLeatherToCraft;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

public class LeatherMaking {

	public static void craftLeatherDialogue(final Player player, final int itemUsed, final int usedWith) {
		player.stopAll();
		for (final LeatherData l : LeatherData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			if (leather == l.getLeather().getId()) {
				if (l.getLeather().getId() == 1741) {
					player.getPacketSender().sendInterfaceModel(8654, 1, Revision.RS2, 150);
					player.getPacketSender().sendInterface(2311);
					player.setInputHandling(new EnterAmountOfLeatherToCraft());
					player.setSelectedSkillingItem(new Item(leather));
					break;
				} else if(l.getLeather().getId() == 1743) {
					player.getDialogueManager().startDialogue(new MakeLeatherD(), l.getLeather().getId());
					player.setSelectedSkillingItem(new Item(leather));
					break;
				} else if (l.getLeather().getId() == 6289) {
					String[] names = { "Body", "Chaps", "Bandana", "Boots", "Vamb" };
					player.getDialogueManager().startDialogue(new MakeLeatherD(), l.getLeather().getId(), names);
					player.setSelectedSkillingItem(new Item(leather));
					return;
				} else if(l.getLeather().getId() == 24372) {
					String[] names = { "Body", "Chaps", "Coif", "Vamb" };
					player.getDialogueManager().startDialogue(new MakeLeatherD(), l.getLeather().getId(), names);
					player.setSelectedSkillingItem(new Item(leather));
					return;
				}
			}
		}
		for (final LeatherDialogueData d : LeatherDialogueData.values()) {
			final int leather = (itemUsed == 1733 ? usedWith : itemUsed);
			boolean polypore = d == LeatherDialogueData.FUNGAL || d == LeatherDialogueData.GRIFOLIC || d == LeatherDialogueData.GANODERMIC;
			String[] names = polypore  ? new String[] {"Visor", "Leggings", "Poncho"} : new String[] { "Vamb", "Chaps", "Body" };
			if (leather == d.getLeather()) {
				player.getDialogueManager().startDialogue(new MakeLeatherD(), d.getLeather(), d, names);
				player.setSelectedSkillingItem(new Item(leather));
				return;
			}
		}
	}

	public static boolean handleButton(Player player, int button) {
		if(player.getSelectedSkillingItem() != null && player.getSelectedSkillingItem().getId() < 0)
			return false;
		for (final LeatherData l : LeatherData.values()) {
			if (button == l.getButtonId(button) && player.getSelectedSkillingItem().isEqual(l.getLeather())) {
				player.getActionManager().setAction(new CraftLeather(l, l.getAmount(button)));
				return true;
			}
		}
		return false;
	}

}
