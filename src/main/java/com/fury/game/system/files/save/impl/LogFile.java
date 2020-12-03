package com.fury.game.system.files.save.impl;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.logs.LoggedItem;
import com.fury.game.system.files.logs.LoggedMessage;
import com.fury.game.system.files.logs.LoggedPlayerItem;
import com.fury.game.system.files.logs.LoggedShopItem;
import com.fury.game.system.files.save.StorageFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("logs") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.add("computers", builder.toJsonTree(player.getLogger().getComputers()));
        object.add("ip-addresses", builder.toJsonTree(player.getLogger().getIpAddresses()));
        object.add("mac-addresses", builder.toJsonTree(player.getLogger().getMacAddresses()));
        object.add("npc-drops", builder.toJsonTree(array(player.getLogger().getNpcDrops())));
        object.add("item-emptied", builder.toJsonTree(array(player.getLogger().getItemsEmptied())));
        object.add("death-drops", builder.toJsonTree(array(player.getLogger().getDeathItemDrops())));
        object.add("ground-items", builder.toJsonTree(arrayPlayer(player.getLogger().getGroundItems())));
        object.add("items-traded", builder.toJsonTree(arrayPlayer(player.getLogger().getTradedItems())));
        object.add("items-staked", builder.toJsonTree(arrayPlayer(player.getLogger().getStakedItems())));
        object.add("items-sold", builder.toJsonTree(arrayShop(player.getLogger().getSoldItems())));
        object.add("alch-items", builder.toJsonTree(arrayShop(player.getLogger().getAlchedItems())));
        object.add("private-messages", builder.toJsonTree(arrayMessage(player.getLogger().getPrivateMessages())));
        object.add("clan-messages", builder.toJsonTree(arrayMessage(player.getLogger().getClanMessages())));
        object.add("messages", builder.toJsonTree(arrayMessage(player.getLogger().getChatMessages())));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("computers")) {
            Type type = new TypeToken<Map<String, Integer>>() {
            }.getType();
            player.getLogger().setComputers(builder.fromJson(reader.get("computers").getAsJsonObject(), type));
        }

        if (reader.has("ip-addresses")) {
            Type type = new TypeToken<Map<String, Integer>>() {
            }.getType();
            player.getLogger().setIPAddresses(builder.fromJson(reader.get("ip-addresses").getAsJsonObject(), type));
        }

        if (reader.has("mac-addresses")) {
            Type type = new TypeToken<Map<String, Integer>>() {
            }.getType();
            player.getLogger().setMacAddresses(builder.fromJson(reader.get("mac-addresses").getAsJsonObject(), type));
        }

        if (reader.has("npc-drops")) {
            player.getLogger().setNpcDrops(getLogItems(reader.get("npc-drops").getAsJsonArray()));
        }

        if (reader.has("item-emptied")) {
            player.getLogger().setItemsEmptied(getLogItems(reader.get("item-emptied").getAsJsonArray()));
        }

        if (reader.has("death-drops")) {
            player.getLogger().setDeathItemDrops(getLogItems(reader.get("death-drops").getAsJsonArray()));
        }

        if (reader.has("ground-items")) {
            player.getLogger().setGroundItems(getLogPlayerItems(reader.get("ground-items").getAsJsonArray()));
        }

        if (reader.has("items-traded")) {
            player.getLogger().setItemsTraded(getLogPlayerItems(reader.get("items-traded").getAsJsonArray()));
        }

        if (reader.has("items-staked")) {
            player.getLogger().setItemsStaked(getLogPlayerItems(reader.get("items-staked").getAsJsonArray()));
        }

        if (reader.has("items-sold")) {
            player.getLogger().setItemsSold(getLogShopItems(reader.get("items-sold").getAsJsonArray()));
        }

        if (reader.has("alch-items")) {
            player.getLogger().setAlchedItems(getLogShopItems(reader.get("alch-items").getAsJsonArray()));
        }

        if (reader.has("private-messages")) {
            player.getLogger().setPrivateMessages(getMessages(reader.get("private-messages").getAsJsonArray()));
        }

        if (reader.has("clan-messages")) {
            player.getLogger().setClanMessages(getMessages(reader.get("clan-messages").getAsJsonArray()));
        }

        if (reader.has("messages")) {
            player.getLogger().setChatMessages(getMessages(reader.get("messages").getAsJsonArray()));
        }
    }

    @Override
    public void setDefaults(Player player) {

    }


    /**
     * Everything below here is crap
     */

    private JsonArray array(List<LoggedItem> items) {
        JsonArray array = new JsonArray(items.size());
        for (Object item : items)
            if ((item != null && (item instanceof LoggedItem) && ((LoggedItem) item).getId() != -1))
                array.add(getObject(item));
        return array;
    }

    private JsonArray arrayPlayer(List<LoggedPlayerItem> items) {
        JsonArray array = new JsonArray(items.size());
        for (Object item : items)
            if ((item != null && (item instanceof LoggedItem) && ((LoggedItem) item).getId() != -1))
                array.add(getObject(item));
        return array;
    }

    private JsonArray arrayShop(List<LoggedShopItem> items) {
        JsonArray array = new JsonArray(items.size());
        for (Object item : items)
            if ((item != null && (item instanceof LoggedItem) && ((LoggedItem) item).getId() != -1))
                array.add(getObject(item));
        return array;
    }

    private JsonArray arrayMessage(List<LoggedMessage> messages) {
        JsonArray array = new JsonArray(messages.size());
        for (LoggedMessage message : messages)
            if (message != null && message.getMessage() != null && !message.getMessage().isEmpty())
                array.add(getMessage(message));
        return array;
    }

    private JsonObject getMessage(LoggedMessage message) {
        JsonObject object = new JsonObject();
        object.addProperty("time", message.getTimestamp());
        object.addProperty("message", message.getMessage());
        object.addProperty("to", message.getTo());
        return object;
    }

    private JsonObject getObject(Object item) {
        JsonObject object = new JsonObject();
        LoggedItem logged = (LoggedItem) item;
        if (logged != null && logged.getId() != -1 && logged.getAmount() > 0) {
            object.addProperty("id", logged.getId());
            object.addProperty("time", logged.getTimestamp());
            if (logged instanceof LoggedPlayerItem) {
                object.addProperty("player", ((LoggedPlayerItem) logged).getUsername());
                object.addProperty("received", ((LoggedPlayerItem) logged).wasReceived());
            }
            if (logged instanceof LoggedShopItem) {
                object.addProperty("shop", ((LoggedShopItem) logged).getShop());
                object.addProperty("price", ((LoggedShopItem) logged).getPrice());
            }
            if (logged.getAmount() > 1)
                object.addProperty("amount", logged.getAmount());
            if (logged.getRevision() != Revision.RS2)
                object.addProperty("revision", logged.getRevision().name());
        }
        return object;
    }

    public static List<LoggedItem> getLogItems(JsonArray array) {
        List<LoggedItem> items = new ArrayList<>();
        for (JsonElement element : array)
            items.add(getItem((JsonObject) element));
        return items;
    }

    public static LoggedItem getItem(JsonObject object) {
        if (object.has("revision")) {
            return new LoggedItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1, Revision.valueOf(object.get("revision").getAsString()));
        } else if (object.has("id"))
            return new LoggedItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1);
        else
            return null;
    }

    public static List<LoggedPlayerItem> getLogPlayerItems(JsonArray array) {
        List<LoggedPlayerItem> items = new ArrayList<>();
        for (JsonElement element : array)
            items.add(getPlayerItem((JsonObject) element));
        return items;
    }

    public static LoggedPlayerItem getPlayerItem(JsonObject object) {
        if (object.has("player") && object.has("received")) {
            if (object.has("revision")) {
                return new LoggedPlayerItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1, Revision.valueOf(object.get("revision").getAsString()), object.get("player").getAsString(), object.get("received").getAsBoolean());
            } else if (object.has("id"))
                return new LoggedPlayerItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1, object.get("player").getAsString(), object.get("received").getAsBoolean());
        }
        return null;
    }


    public static List<LoggedShopItem> getLogShopItems(JsonArray array) {
        List<LoggedShopItem> items = new ArrayList<>();
        for (JsonElement element : array)
            items.add(getShopItem((JsonObject) element));
        return items;
    }

    public static LoggedShopItem getShopItem(JsonObject object) {
        if (object.has("shop")) {
            if (object.has("revision")) {
                return new LoggedShopItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1, Revision.valueOf(object.get("revision").getAsString()), object.get("shop").getAsInt(), object.get("price").getAsLong());
            } else if (object.has("id"))
                return new LoggedShopItem(object.get("id").getAsInt(), object.get("time").getAsLong(), object.has("amount") ? object.get("amount").getAsInt() : 1, object.get("shop").getAsInt(), object.get("price").getAsLong());
        }
        return null;
    }

    public static List<LoggedMessage> getMessages(JsonArray array) {
        List<LoggedMessage> items = new ArrayList<>();
        for (JsonElement element : array)
            items.add(getMessage((JsonObject) element));
        return items;
    }

    public static LoggedMessage getMessage(JsonObject object) {
        return new LoggedMessage(object.get("time").getAsLong(), object.get("message").getAsString(), object.get("to").getAsString());
    }
}
