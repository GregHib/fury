package com.fury.game.system.files.save.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.google.gson.JsonObject;

public class ReferFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("refer") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();

        if(player.getReferAFriend().hasReferral())
            object.addProperty("referral", player.getReferAFriend().getReferral());
        if(player.getReferAFriend().hasReceivedReward())
            object.addProperty("reward", player.getReferAFriend().hasReceivedReward());
        object.add("referred-friends", builder.toJsonTree(player.getReferAFriend().getFriends()));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("referral"))
            player.getReferAFriend().setReferral(reader.get("referral").getAsString());

        if (reader.has("reward"))
            player.getReferAFriend().setReceivedReward(reader.get("reward").getAsBoolean());

        if (reader.has("referred-friends"))
            player.getReferAFriend().setFriends(builder.fromJson(reader.get("referred-friends").getAsJsonArray(), String[].class));
    }

    @Override
    public void setDefaults(Player player) {

    }
}
