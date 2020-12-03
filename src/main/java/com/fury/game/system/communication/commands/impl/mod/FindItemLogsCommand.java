package com.fury.game.system.communication.commands.impl.mod;

import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.global.ListWidget;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.system.communication.commands.Command;
import com.fury.game.system.files.logs.LoggedItem;
import com.fury.game.system.files.logs.LoggedPlayerItem;
import com.fury.game.system.files.logs.LoggedShopItem;
import com.fury.game.world.World;
import com.fury.tools.accounts.Utils.SearchUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FindItemLogsCommand implements Command {

    private static Pattern pattern = Pattern.compile("^find\\sitem\\s(.*)\\slogs\\s(.*)");

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String prefix() {
        return "find item ";
    }

    @Override
    public String format() {
        return "find item [query] logs [username]";
    }

    @Override
    public void process(Player player, String... values) {
        String search = values[1];
        String name = values[2];
        Player target = World.getPlayerByName(name);

        if (target == null) {
            player.message("'" + name + "' is not currently online.");
            target = SearchUtils.getPlayerFromName(name);
        }

        if(target == null) {
            player.message("Error loading that players file.");
            return;
        }

        List<String> list = new ArrayList<>();
        for(LoggedShopItem item : target.getLogger().getAlchedItems()) {
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " for " + item.getPrice();
            if(s.contains(search))
                list.add("Alch: " + s);
        }
        for(LoggedItem item : target.getLogger().getDeathItemDrops()) {
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount();
            if(s.contains(search))
                list.add("Death: " + s);
        }
        for(LoggedItem item : target.getLogger().getItemsEmptied()) {
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount();
            if(s.contains(search))
                list.add("Empty: " + s);
        }
        for(LoggedShopItem item : target.getLogger().getSoldItems()) {
            int id = item.getShop();
            Shop shop = ShopManager.getShops().get(id);
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount() + " " + (shop != null ? "to " + shop.getName() : "") + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getShop() + ")" : "") + " for " + item.getPrice() + " ea";
            if(s.contains(search))
                list.add("Sold: " + s);
        }
        for(LoggedItem item : target.getLogger().getNpcDrops()) {
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount();
            if(s.contains(search))
                list.add("Npc: " + s);
        }
        for(LoggedItem item : target.getLogger().getGroundItems()) {
            String s = item.getTime() + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount();
            if(s.contains(search))
                list.add("Floor: " + s);
        }
        for(LoggedPlayerItem item : target.getLogger().getTradedItems()) {
            String s = item.getTime() + " " + (item.wasReceived() ? "Received" : "Gave") + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount() + " to " + item.getUsername();
            if(s.contains(search))
                list.add("Trade: " + s);
        }
        for(LoggedPlayerItem item : target.getLogger().getStakedItems()) {
            String s = item.getTime() + " " + (item.wasReceived() ? "Won" : "Lost") + " " + item.getName() + (player.getRights().isOrHigher(PlayerRights.DEVELOPER) ? " (" + item.getId() + ")" : "") + " " + item.getAmount() + " to " + item.getUsername();
            if(s.contains(search))
                list.add("Staked: " + s);
        }
        ListWidget.display(player, "Item Log Search '" + search + "': " + target.getUsername(), "", list.toArray(new String[list.size()]));
    }

    @Override
    public boolean rights(Player player) {
        return player.getRights().isOrHigher(PlayerRights.MODERATOR);
    }
}
