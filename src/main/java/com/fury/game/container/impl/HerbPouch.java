package com.fury.game.container.impl;

import com.fury.game.container.types.AlwaysStackContainer;
import com.fury.game.content.skill.member.herblore.Herbs;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.util.NameUtils;

import java.util.ArrayList;
import java.util.List;

public class HerbPouch extends AlwaysStackContainer {

    private static Herbs[] herbs = new Herbs[] {
            Herbs.GUAM,
            Herbs.MARRENTILL,
            Herbs.TARROMIN,
            Herbs.HARRALANDER,
            Herbs.RANARR,
            Herbs.TOADFLAX,
            Herbs.IRIT,
            Herbs.AVANTOE,
            Herbs.KWUARM,
            Herbs.SNAPDRAGON,
            Herbs.CADANTINE,
            Herbs.LANTADYME,
            Herbs.DWARF_WEED,
            Herbs.TORSTOL
    };

    public static int getIndex(int id) {
        for(int index = 0; index < herbs.length; index++)
            if (id == herbs[index].getHerbId())
                return index;
        return -1;
    }

    public HerbPouch(Player player) {
        super(player, 14);
    }

    @Override
    public String getFullMessage() {
        return "Your herb pouch is full of that type of herb.";
    }

    public void empty() {
        int spaces = player.getInventory().getSpaces();

        if(spaces <= 0) {
            player.getInventory().full();
            return;
        }

        int herbSpaces = getSpaces();

        if(herbSpaces == capacity()) {
            player.message("Your herb pouch is empty.");
            return;
        }

        int total = spaces;
        for(Item herb : items) {
            if(herb == null)
                continue;
            for(int i = 0; i < herb.getAmount(); i++) {
                move(new Item(herb, 1), player.getInventory());
                total--;
                if(total <= 0) {
                    player.message("You withdraw as many herbs as you can.");
                    return;
                }
            }
        }

        player.message("You withdraw all the herbs.");
    }

    public void fill() {
        boolean moved = false;
        for(int slot = 0; slot < player.getInventory().capacity(); slot++) {
            Item item = player.getInventory().get(slot);
            if(item == null)
                continue;
            if(!item.getDefinition().isNoted() && !item.getDefinition().isStackable()) {
                int index = getIndex(item.getId());

                if(index == -1)
                    continue;

                if(!store(item, slot, index))
                    return;
                moved = true;
            }
        }
        if(moved)
            player.message("You fill your pouch with herbs.");
        else
            player.message("You do not have any herbs to store in your pouch.");
    }

    public boolean store(Item item, int slot, int index) {
        if(indexOutOfBounds(index))
            return false;

        if(items[index] == null) {
            items[index] = item;
        } else {
            if(!item.isEqual(items[index]))
                return false;

            int amount = items[index].getAmount();

            if (amount + 1 > 30) {
                full();
                return false;
            }

            items[index].setAmount(amount + 1);
        }
        player.getInventory().delete(slot);
        return true;
    }

    public void check() {
        List<String> lines = new ArrayList<>();
        for(int i = 0; i < herbs.length; i++) {
            if(items[i] == null)
                continue;
            lines.add(items[i].getAmount() + " " + NameUtils.capitalize(items[i].getName().replaceAll("Grimy", "").trim()));
        }

        if(lines.size() > 1) {
            player.message("Your herb pouch contains: " + lines.get(0) + " " + lines.get(1) + (lines.size() > 2 ? " " + lines.get(2) : ""));
            if(lines.size() > 3) {
                for (int i = 0; i < (lines.size() - 3) / 4 + 1; i++) {
                    StringBuilder line = new StringBuilder();
                    for(int j = 0; j < 4; j++) {
                        if(3 + (i * 4) + j >= lines.size())
                            break;
                        line.append(lines.get(3 + (i * 4) + j)).append(" ");
                    }

                    player.message(line.toString());
                }
            }
        } else {
            player.message("Your herb pouch is empty.");
        }
    }
}
