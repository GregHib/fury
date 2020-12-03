package com.fury.game.system.files.save.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.google.gson.JsonObject;

public class SettingsFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("settings") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.add("settings", builder.toJsonTree(player.getSettings().getAll()));
        object.add("configs", builder.toJsonTree(player.getConfig().configs()));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("run-energy"))
            player.getSettings().set(Settings.RUN_ENERGY, reader.get("run-energy").getAsInt());
        if (reader.has("accept-aid"))
            player.getSettings().set(Settings.ACCEPT_AID, reader.get("accept-aid").getAsBoolean());
        if (reader.has("running"))
            player.getSettings().set(Settings.RUNNING, reader.get("running").getAsBoolean());
        if (reader.has("special-amount"))
            player.getSettings().set(Settings.SPECIAL_ENERGY, reader.get("special-amount").getAsInt());
        if (reader.has("examine-tables"))
            player.getSettings().set(Settings.EXAMINE_DROP_TABLES, reader.get("examine-tables").getAsBoolean());
        if (reader.has("no-wild-warning"))
            player.getSettings().set(Settings.WILDERNESS_WARNINGS, reader.get("no-wild-warning").getAsBoolean());

        if (reader.has("settings")) {
            JsonObject object = reader.get("settings").getAsJsonObject();
            for(Settings setting : Settings.values())
                if(object.has(setting.name()))
                    player.getSettings().set(setting, builder.fromJson(object.get(setting.name()), Object.class));
        }

        if (reader.has("configs"))
            player.getConfig().setConfigs(builder.fromJson(reader.get("configs"), int[].class));
    }

    @Override
    public void setDefaults(Player player) {

    }
}
