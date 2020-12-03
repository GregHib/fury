package com.fury.game.system.files.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.List;

public abstract class StorageFile {

    public static Gson builder = new GsonBuilder().setPrettyPrinting().create();

    public abstract String getDirectory(Player player);

    public abstract JsonObject save(Player player);

    public abstract void load(Player player, JsonObject reader);

    public abstract void setDefaults(Player player);

    protected static final JsonArray itemsToArray(List<Item> list, boolean saveSpaces) {
        Item[] items = list.toArray(new Item[list.size()]);
        return itemsToArray(items, saveSpaces);
    }

    protected static final JsonArray itemsToArray(Item[] items, boolean saveSpaces) {
        JsonArray array = new JsonArray(items.length);
        for(Item item : items)
            if((item != null && item.getId() != -1) || saveSpaces)
                array.add(itemToObject(item));
        return array;
    }

    protected static final JsonObject itemToObject(Item item) {
        JsonObject object = new JsonObject();
        if(item != null && item.getId() != -1) {
            object.addProperty("id", item.getId());
            if(item.getAmount() != -1)
                object.addProperty("amount", item.getAmount());
            if (item.getRevision() != Revision.RS2)
                object.addProperty("revision", item.getRevision().name());
        }
        return object;
    }
}
