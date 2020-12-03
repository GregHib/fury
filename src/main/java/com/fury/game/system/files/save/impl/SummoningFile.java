package com.fury.game.system.files.save.impl;

import com.fury.engine.task.impl.FamiliarSpawnTask;
import com.fury.game.content.skill.member.summoning.impl.PetDetails;
import com.fury.game.content.skill.member.summoning.impl.Summoning;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.fury.network.login.LoginUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SummoningFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("summoning") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("infuse-creatable", player.getSummoning().displayOnlyCreatable());
        if(player.getFamiliar() != null) {
            object.addProperty("pouch", player.getFamiliar().getPouch().getPouchId());
            object.addProperty("special-energy", player.getFamiliar().getSpecialEnergy());
            object.addProperty("tick", player.getFamiliar().getTicks());
            object.addProperty("timer", player.getFamiliar().getTrackTimer());
            object.addProperty("drain", player.getFamiliar().getTrackDrain());

            if (player.getFamiliar().getBeastOfBurden() != null)
                object.add("burden", itemsToArray(player.getFamiliar().getBeastOfBurden().getItems(), false));
        }

        object.addProperty("pet-item", player.getPetManager().getItemId());
        object.addProperty("pet-npc", player.getPetManager().getNpcId());
        object.addProperty("troll-name", player.getPetManager().getTrollBabyName());

        JsonArray array = new JsonArray();
        for(int key : player.getPetManager().getPetDetails().keySet()) {
            JsonObject obj = new JsonObject();
            PetDetails details = player.getPetManager().getPetDetails().get(key);
            obj.addProperty("id", key);
            obj.addProperty("growth", details.getGrowth());
            obj.addProperty("stage", details.getStage());
            obj.addProperty("hunger", details.getHunger());
            array.add(obj);
        }
        object.add("details", array);

        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("infuse-creatable"))
            player.getSummoning().setDisplayOnlyCreatable(reader.get("infuse-creatable").getAsBoolean());

        if (reader.has("pouch")) {
            Summoning.Pouches pouch = Summoning.Pouches.forId(reader.get("pouch").getAsInt());
            if(pouch != null) {
                player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player));
                player.getSummoning().getSpawnTask().setPouch(pouch);
            }
        }

        if (reader.has("special-energy") && player.getSummoning().getSpawnTask() != null)
            player.getSummoning().getSpawnTask().setSpecialEnergy(reader.get("special-energy").getAsInt());

        if (reader.has("tick") && player.getSummoning().getSpawnTask() != null)
            player.getSummoning().getSpawnTask().setTicks(reader.get("tick").getAsInt());

        if (reader.has("timer") && player.getSummoning().getSpawnTask() != null)
            player.getSummoning().getSpawnTask().setTrackTimer(reader.get("timer").getAsInt());

        if (reader.has("drain") && player.getSummoning().getSpawnTask() != null)
            player.getSummoning().getSpawnTask().setTrackDrain(reader.get("drain").getAsBoolean());

        if (reader.has("burden") && player.getSummoning().getSpawnTask() != null)
            player.getSummoning().getSpawnTask().setBurdenItems(LoginUtils.getItemContainer(reader.get("burden").getAsJsonArray(), true));

        if (reader.has("pet-item"))
            player.getPetManager().setItemId(reader.get("pet-item").getAsInt());

        if (reader.has("pet-npc"))
            player.getPetManager().setNpcId(reader.get("pet-npc").getAsInt());

        if (reader.has("troll-name"))
            player.getPetManager().setTrollBabyName(reader.get("troll-name").getAsString());

        if (reader.has("details")) {
            JsonArray array = reader.get("details").getAsJsonArray();
            for (JsonElement jsonElement : array) {
                JsonObject obj = (JsonObject) jsonElement;
                if(obj.has("id")) {
                    PetDetails details = new PetDetails(obj.get("growth").getAsDouble());
                    details.setStage(obj.get("stage").getAsInt());
                    details.setHunger(obj.get("hunger").getAsDouble());
                    player.getPetManager().getPetDetails().put(obj.get("id").getAsInt(), details);
                }
            }
        }

    }

    @Override
    public void setDefaults(Player player) {

    }
}
