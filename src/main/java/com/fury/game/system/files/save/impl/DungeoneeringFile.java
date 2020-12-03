package com.fury.game.system.files.save.impl;

import com.google.gson.JsonObject;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.fury.network.login.LoginUtils;

public class DungeoneeringFile extends StorageFile {

    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("dungeoneering") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("tokens", player.getDungManager().getTokens());
        object.addProperty("max-floor", player.getDungManager().getMaxFloor());
        object.addProperty("max-complexity", player.getDungManager().getMaxComplexity());
        object.addProperty("rejoin-key", player.getDungManager().getRejoinKey());
        object.add("binded-items", itemsToArray(player.getDungManager().getBindedItems().getItems(), false));
        object.add("binded-ammo", itemToObject(player.getDungManager().getBindedAmmo()));
        object.addProperty("previous-progress", player.getDungManager().getPreviousProgress());

        object.add("current-progress", builder.toJsonTree(player.getDungManager().getCurrentProgressData()));
        object.add("visited-resources", builder.toJsonTree(player.getDungManager().getVisitedResources()));
        object.add("unlocked-scrolls", builder.toJsonTree(player.getDungManager().getUnlockedScrolls()));
        object.add("gem-bag", itemsToArray(player.getGemBag().getItems(), true));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("tokens"))
            player.getDungManager().setTokens(reader.get("tokens").getAsInt());
        if (reader.has("max-floor"))
            player.getDungManager().setMaxFloor(reader.get("max-floor").getAsInt());
        if (reader.has("max-complexity"))
            player.getDungManager().setMaxComplexity(reader.get("max-complexity").getAsInt());
        if (reader.has("rejoin-key"))
            player.getDungManager().setRejoinKey(reader.get("rejoin-key").getAsString());
        if (reader.has("binded-items"))
            player.getDungManager().getBindedItems().setItems(LoginUtils.getItemContainer(reader.get("binded-items").getAsJsonArray(), true));
        if (reader.has("binded-ammo"))
            player.getDungManager().setBindedAmmo(LoginUtils.getItem(reader.get("binded-ammo").getAsJsonObject()));
        if (reader.has("previous-progress"))
            player.getDungManager().setPreviousProgress(reader.get("previous-progress").getAsInt());
        if (reader.has("current-progress"))
            player.getDungManager().setCurrentProgress(builder.fromJson(reader.get("current-progress").getAsJsonArray(), boolean[].class));
        if (reader.has("visited-resources"))
            player.getDungManager().setVisitedResources(builder.fromJson(reader.get("visited-resources").getAsJsonArray(), boolean[].class));
        if (reader.has("unlocked-scrolls"))
            player.getDungManager().setUnlockedScrolls(builder.fromJson(reader.get("unlocked-scrolls").getAsJsonArray(), boolean[].class));
        if (reader.has("gem-bag"))
            player.getGemBag().setItems(LoginUtils.getItemContainer(reader.get("gem-bag").getAsJsonArray(), true));
    }

    @Override
    public void setDefaults(Player player) {

    }
}
