package com.fury.game.system.files.save.impl;

import com.fury.game.content.skill.member.slayer.SlayerMaster;
import com.fury.game.content.skill.member.slayer.SlayerTask;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class SlayerFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("slayer") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        if(player.getSlayerManager().getCurrentMaster() != null)
            object.addProperty("slayer-master", player.getSlayerManager().getCurrentMaster().name());
        if(player.getSlayerManager().getCurrentTask() != null)
            object.addProperty("slayer-task", player.getSlayerManager().getCurrentTask().name());
        if(player.getSlayerManager().getDifficultyWildernessSlayer() != -1)
            object.addProperty("task-wilderness", player.getSlayerManager().getDifficultyWildernessSlayer());
        if(player.getSlayerManager().getCompletedWildernessTasks() > 0)
            object.addProperty("task-wilderness-completed", player.getSlayerManager().getCompletedWildernessTasks());

        object.addProperty("task-max", player.getSlayerManager().getMaximumTaskCount());
        object.addProperty("task-count", player.getSlayerManager().getCurrentTaskCount());
        object.addProperty("tasks-completed", player.getSlayerManager().getCompletedTasks());
        object.addProperty("double-slay-xp", player.getSlayerManager().doubleSlayerXP);
        object.add("slayer-learnt", builder.toJsonTree(player.getSlayerManager().getLearnt()));
        object.add("slayer-removed", builder.toJsonTree(player.getSlayerManager().getCanceledNames()));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("slayer-master"))
            player.getSlayerManager().setSlayerMaster(SlayerMaster.valueOf(reader.get("slayer-master").getAsString()));

        if (reader.has("slayer-task")) {
            String name = reader.get("slayer-task").getAsString();
            if(name != null) {
                try {
                    SlayerTask task = SlayerTask.valueOf(name);
                    player.getSlayerManager().setCurrentTask(task);
                } catch (Exception e) {
                    System.err.println("Missing slayer task: " + name);
                }
            }
        }
        if (reader.has("task-wilderness"))
            player.getSlayerManager().setDifficultyWildernessSlayer(reader.get("task-wilderness").getAsInt());

        if (reader.has("task-wilderness-completed"))
            player.getSlayerManager().setCompletedWildernessTasks(reader.get("task-wilderness-completed").getAsInt());

        if (reader.has("task-max"))
            player.getSlayerManager().seMaximumTaskCount(reader.get("task-max").getAsInt());

        if (reader.has("task-count"))
            player.getSlayerManager().setCurrentTaskCount(reader.get("task-count").getAsInt());

        if (reader.has("tasks-completed"))
            player.getSlayerManager().setCompletedTasks(reader.get("tasks-completed").getAsInt());

        if (reader.has("double-slay-xp"))
            player.getSlayerManager().doubleSlayerXP = reader.get("double-slay-xp").getAsBoolean();

        if (reader.has("slayer-learnt"))
            player.getSlayerManager().setLearnt(builder.fromJson(reader.get("slayer-learnt").getAsJsonArray(), boolean[].class));

        if (reader.has("slayer-removed")) {
            String[] arr = builder.fromJson(reader.get("slayer-removed").getAsJsonArray(), String[].class);
            SlayerTask[] array = new SlayerTask[arr.length];
            try {
                for (int i = 0; i < array.length; i++) {
                    if(arr[i] != null)
                        array[i] = SlayerTask.valueOf(arr[i]);
                }
            } catch (Exception e) {
                System.err.println("Missing removed slayer tasks: " + Arrays.toString(arr));
            }
            player.getSlayerManager().setCanceled(array);
        }
    }

    @Override
    public void setDefaults(Player player) {

    }
}
