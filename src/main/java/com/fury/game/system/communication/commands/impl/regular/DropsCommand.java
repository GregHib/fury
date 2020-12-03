package com.fury.game.system.communication.commands.impl.regular;

import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.game.content.actions.item.ImplingJarsPlugin;
import com.fury.game.content.global.ListWidget;
import com.fury.game.content.misc.items.random.RandomItem;
import com.fury.game.content.misc.items.random.RandomItemGenerator;
import com.fury.game.content.misc.items.random.impl.MysteryBoxGen;
import com.fury.game.content.misc.items.random.impl.PvpArmourBoxGen;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.communication.commands.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DropsCommand implements Command {

    static Pattern pattern = Pattern.compile("^drops\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "drops ";
    }

    @Override
    public String format() {
        return "drops [npc name]";
    }

    @Override
    public void process(Player player, String... values) {

        if (!player.getTimers().getClickDelay().elapsed(10000) && !player.getRights().isOrHigher(PlayerRights.OWNER)) {
            player.message("You can only do this once every 10 seconds.");
            return;
        }
        player.getTimers().getClickDelay().reset();

        String name = values[1];

        if(ImplingJarsPlugin.handleDropsCommand(player, name))
            return;

        switch (name) {
            case "pvp box":
                showDrops(player, PvpArmourBoxGen.INSTANCE, "PvP Armour Box", -1);
                return;
            case "mystery box":
                showDrops(player, MysteryBoxGen.INSTANCE, "Mystery Box", -1);
                return;
        }

        NpcDefinition def = NpcDefinition.get(name);
        if (def == null) {
            player.message("Could not find npc \"" + name + "\".");
            return;
        }

        showDrops(player, def);
    }

    public static void showDrops(Player player, NpcDefinition definition) {
        player.getPacketSender().sendInterfaceRemoval();
        String gameModeRate = "[+" +  String.format("%.0f", ((player.getDropRate() - 1.0) * 100.0)) + "%]";

        Drop[] drops = MobDrops.getDrops(definition.getId(), definition.revision);

        if (drops == null || drops.length <= 0)
            return;


        List<String> lines = new ArrayList<>();
        lines.add("");

        int dropTotal = 0;
        for (Drop drop : drops)
            dropTotal += drop.getRate();

        for (Drop drop : drops) {
            if (drop.getMinAmount() != 0) {
                String dropRange = drop.getExtraAmount() == 0 ? drop.getMinAmount() == 1 ? "" : "[" + drop.getMinAmount() + "]" : ("[" + drop.getMinAmount() + " - " + (drop.getMinAmount() + drop.getExtraAmount()) + "]");
                double dropRate = drop.getRate() == 100 ? 1 : (dropTotal / drop.getRate()) * (2.0 - player.getDropRate());
                lines.add(new Item(drop.getItemId()).getDefinition().getName() + " " + dropRange + " 1/" + String.format("%.0f", dropRate));
            }
        }

        ListWidget.display(player, "Drop Rates for " + definition.name + " " + gameModeRate + (player.getRights() == PlayerRights.DEVELOPER ? " " + definition.id : ""), "Name [Amount] Rate", lines.toArray(new String[lines.size()]));
    }

    public static void showDrops(Player player, RandomItemGenerator generator, String name, int id) {
        player.getPacketSender().sendInterfaceRemoval();

        List<String> lines = new ArrayList<>();

        generator.getTables().forEach(table -> {
            lines.add("");
            lines.add(table.getName() + " 1/" + (Math.round((1.0 / table.getChance().doubleValue()) * 100.0) / 100.0));
            if(table.isIntArray()) {
                for(int itemId : table.getItems()) {
                    lines.add(Loader.getItem(itemId).getName());
                }
            } else {
                for(RandomItem item : table.getRandomItems()) {
                    lines.add(Loader.getItem(item.getId()).getName() + " " + ((item.getMinAmount() == item.getMaxAmount()) ? item.getMinAmount() == 1 ? "" : "[" + item.getMinAmount() + "]" : "[" + item.getMinAmount() + " - " + item.getMaxAmount() + "]"));
                }
            }
        });

        ListWidget.display(player, "Drop Rates for " + name + " " + (player.getRights() == PlayerRights.DEVELOPER && id != -1 ? " " + id : ""), "Name [Amount] Rate", lines.toArray(new String[lines.size()]));
    }
    @Override
    public boolean rights(Player player) {
        return true;
    }
}
