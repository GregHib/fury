package com.fury.game.system.files.save.impl;

import com.google.gson.JsonObject;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;

import java.util.ArrayList;
import java.util.List;

public class NoteFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("notes") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.add("notes", builder.toJsonTree(player.getNotes().getNotes()));
        object.add("colours", builder.toJsonTree(player.getNotes().getNoteColours()));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        List<String> notes_list = new ArrayList<String>();
        String[] notes = builder.fromJson(reader.get("notes").getAsJsonArray(), String[].class);
        for(String n : notes)
            notes_list.add(n);
        player.getNotes().setNotes(notes_list);

        List<Integer> colours_list = new ArrayList<Integer>();
        int[] colours = builder.fromJson(reader.get("colours").getAsJsonArray(), int[].class);
        for(int c : colours)
            colours_list.add(c);
        player.getNotes().setNoteColours(colours_list);
    }

    @Override
    public void setDefaults(Player player) {

    }
}
