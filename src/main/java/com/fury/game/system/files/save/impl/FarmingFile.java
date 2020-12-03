package com.fury.game.system.files.save.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.fury.game.content.skill.member.farming.FarmingSpot;
import com.fury.game.content.skill.member.farming.ProductInfo;
import com.fury.game.content.skill.member.farming.SpotInfo;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FarmingFile extends StorageFile {

    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("farming") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        List<FarmingSpot> spots = player.getFarmingManager().getSpots();
        object.add("spots", spotsToArray(spots));
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        player.getFarmingManager().setSpots(readSpots(player, reader));
    }

    @Override
    public void setDefaults(Player player) {

    }

    private List<FarmingSpot> readSpots(Player player, JsonObject reader) {
        List<FarmingSpot> spots = new ArrayList<>();
        if(reader.has("spots")) {
            JsonArray array = reader.get("spots").getAsJsonArray();
            for (JsonElement element : array) {
                if(!element.isJsonObject())
                    continue;

                JsonObject object = (JsonObject) element;

                if(object.has("spotInfo")) {
                    SpotInfo info = SpotInfo.valueOf(object.get("spotInfo").getAsString());
                    if(info != null) {
                        FarmingSpot spot = new FarmingSpot(player.getFarmingManager(), info);
                        if(object.has("productInfo"))
                            spot.setProductInfo(ProductInfo.valueOf(object.get("productInfo").getAsString()));
                        if(object.has("stage"))
                            spot.setStage(object.get("stage").getAsInt());
                        if(object.has("cycleTime"))
                            spot.setActualCycleTime(object.get("cycleTime").getAsLong());
                        if(object.has("harvestAmount"))
                            spot.setHarvestAmount(object.get("harvestAmount").getAsInt());
                        if(object.has("attributes"))
                            spot.setAttributes(builder.fromJson(object.getAsJsonArray("attributes"), boolean[].class));
                        spots.add(spot);
                    }
                }
            }
        }
        return spots;
    }

    private static final JsonArray spotsToArray(List<FarmingSpot> list) {
        FarmingSpot[] spots = list.toArray(new FarmingSpot[list.size()]);
        return spotsToArray(spots);
    }

    private static final JsonArray spotsToArray(FarmingSpot[] spots) {
        JsonArray array = new JsonArray(spots.length);
        for(FarmingSpot spot : spots)
            array.add(spotToObject(spot));
        return array;
    }

    private static final JsonObject spotToObject(FarmingSpot spot) {
        JsonObject object = new JsonObject();
        if(spot != null) {
            object.addProperty("spotInfo", spot.getSpotInfo().name());
            if(spot.getProductInfo() != null)
                object.addProperty("productInfo", spot.getProductInfo().name());
            if(spot.getStage() != 0)
                object.addProperty("stage", spot.getStage());
            object.addProperty("cycleTime", spot.getCycleTime());
            if(spot.getHarvestAmount() != 0)
                object.addProperty("harvestAmount", spot.getHarvestAmount());
            if(!Arrays.equals(spot.getAttributes(), new boolean[10]))
                object.add("attributes", builder.toJsonTree(spot.getAttributes()));
        }
        return object;
    }
}