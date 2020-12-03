package com.fury.game.system.files.save.impl;

import com.google.gson.JsonObject;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.character.player.content.Music;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;

import java.util.ArrayList;
import java.util.List;

public class SongFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("songs") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.add("songs", builder.toJsonTree(player.getSongs()));
        object.add("playlist", builder.toJsonTree(player.getPlaylist()));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        List<Boolean> song_list = new ArrayList<Boolean>();
        boolean[] songs = builder.fromJson(reader.get("songs").getAsJsonArray(), boolean[].class);
        if(songs.length == 0) {
            Music.createList(player);
        } else {
            for(boolean s : songs)
                song_list.add(s);
            player.setSongs(song_list);
        }
        ArrayList<Integer> song_queue = new ArrayList<>();
        int[] playlist = builder.fromJson(reader.get("playlist").getAsJsonArray(), int[].class);
        if(playlist.length != 0) {
            for(int p : playlist)
                song_queue.add(p);
            player.setPlaylist(song_queue);
        }
    }

    @Override
    public void setDefaults(Player player) {
        Music.createList(player);
    }
}
