package com.fury.game.system.files.save.impl;

import com.fury.game.container.impl.bank.Bank;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.fury.network.login.LoginUtils;

import java.util.ArrayList;
import java.util.List;

public class BankFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("banks") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        Bank bank = player.getActualBank();
        object.addProperty("withdraw", bank.getWithdrawAmount());
        object.addProperty("placeholder", bank.isAlwaysPlaceholder());
        object.addProperty("swap", bank.isSwapMode());
        object.addProperty("note", bank.isWithdrawNotes());
        object.add("placeholders", itemsToArray(bank.getPlaceHolders().getItems(), false));
        JsonArray items = new JsonArray();
        for(int i = 0; i < bank.getTabCount(); i++)
            items.add(itemsToArray(bank.tab(i).getItems(), false));
        object.add("bank", items);
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if(reader.has("withdraw"))
            player.getBank().setWithdrawAmount(reader.get("withdraw").getAsInt());
        if(reader.has("placeholder"))
            player.getBank().setAlwaysPlaceholder(reader.get("placeholder").getAsBoolean());
        if(reader.has("swap"))
            player.getBank().setSwapMode(reader.get("swap").getAsBoolean());
        if(reader.has("note"))
            player.getBank().setWithdrawNotes(reader.get("note").getAsBoolean());
        if(reader.has("placeholders"))
            player.getBank().getPlaceHolders().addAll(LoginUtils.getItemContainer(reader.get("placeholders").getAsJsonArray(), true));

        if (reader.has("bank")) {
            List<Item[]> tabs = new ArrayList<>();
            JsonArray array = reader.get("bank").getAsJsonArray();
            for (int i = 0; i < array.size(); i++)
                tabs.add(LoginUtils.getItemContainer(array.get(i).getAsJsonArray(), true));
            player.setBank(tabs.toArray(new Item[tabs.size()][]));
        }
    }

    @Override
    public void setDefaults(Player player) {

    }
}
