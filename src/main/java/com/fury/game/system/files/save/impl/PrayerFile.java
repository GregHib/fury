package com.fury.game.system.files.save.impl;

import com.google.gson.JsonObject;
import com.fury.game.content.skill.free.prayer.Prayerbook;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;

public class PrayerFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("prayer") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("prayer-book", player.getPrayerbook().name());
        object.add("quick-prayers", builder.toJsonTree(player.getPrayer().getQuickPrayers()));

        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("prayer-book"))
            player.setPrayerbook(Prayerbook.valueOf(reader.get("prayer-book").getAsString()));
        if (reader.has("quick-prayers"))
            player.getPrayer().setQuickPrayers(builder.fromJson(reader.get("quick-prayers"), boolean[][].class));
    }

    @Override
    public void setDefaults(Player player) {
        player.setPrayerbook(Prayerbook.NORMAL);
    }
}
