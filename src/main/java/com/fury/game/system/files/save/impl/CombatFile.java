package com.fury.game.system.files.save.impl;

import com.fury.game.entity.character.combat.effects.Effect;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.weapon.FightType;
import com.fury.game.entity.character.combat.magic.CombatSpells;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class CombatFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("combat") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        List<Effect> effects = player.getEffects().getEffects();
        JsonArray array = new JsonArray();
        for(Effect effect : effects) {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", effect.getType().name());
            obj.addProperty("cycle", effect.getCycle());
            obj.add("args", builder.toJsonTree(effect.getArguments()));
            array.add(obj);
        }
        object.add("effects", array);

        object.addProperty("autocast", new Boolean(player.isAutoCast()));
        object.addProperty("autocast-spell", player.getAutoCastSpell() != null ? player.getAutoCastSpell().getId() : -1);

        object.addProperty("auto-retaliate", new Boolean(player.isAutoRetaliate()));
        object.addProperty("gravestone", player.getGravestone());
        object.addProperty("xp-locked", new Boolean(player.experienceLocked()));
        object.addProperty("def-cast", new Boolean(player.isDefensiveCasting()));
        object.addProperty("veng-cast", new Boolean(player.hasVengeance()));
        object.addProperty("last-veng", player.getTimers().getLastVengeance().elapsed());
        object.addProperty("player-kills", new Integer(player.getPlayerKillingAttributes().getPlayerKills()));
        object.addProperty("player-killstreak", new Integer(player.getPlayerKillingAttributes().getPlayerKillStreak()));
        object.addProperty("player-deaths", new Integer(player.getPlayerKillingAttributes().getPlayerDeaths()));
        object.addProperty("target-percentage", new Integer(player.getPlayerKillingAttributes().getTargetPercentage()));
        object.addProperty("bh-rank", new Integer(player.getAppearance().getBountyHunterSkull()));
        object.addProperty("fight-type", player.getFightType().name());
        object.addProperty("dfs-charges", player.getDfsCharges());
        object.addProperty("recoil-deg", new Integer(player.getRecoilCharges()));

        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {

        if(reader.has("effects")) {
            for(JsonElement element : reader.get("effects").getAsJsonArray()) {
                JsonObject object = element.getAsJsonObject();
                if(object.has("type")) {
                    String name = object.get("type").getAsString();
                    int cycle = object.get("cycle").getAsInt();
                    Object[] args = builder.fromJson(object.get("args"), Object[].class);
                    Effects type = Effects.valueOf(name);
                    Effect effect = new Effect(type, cycle);
                    effect.setArguments(args);
                    player.getEffects().startEffect(effect);
                }
            }
        }
        if (reader.has("autocast"))
            player.setAutoCast(reader.get("autocast").getAsBoolean());

        if (reader.has("autocast-spell")) {
            int spell = reader.get("autocast-spell").getAsInt();
            if (spell != -1)
                player.setAutoCastSpell(CombatSpells.getSpell(spell));
        }

        if (reader.has("last-bork"))//Converter
            player.getVars().set(Variables.LAST_BORK_DEFEAT, reader.get("last-bork").getAsLong());

        if (reader.has("player-kills"))
            player.getPlayerKillingAttributes().setPlayerKills(reader.get("player-kills").getAsInt());

        if (reader.has("player-killstreak"))
            player.getPlayerKillingAttributes().setPlayerKillStreak(reader.get("player-killstreak").getAsInt());

        if (reader.has("player-deaths"))
            player.getPlayerKillingAttributes().setPlayerDeaths(reader.get("player-deaths").getAsInt());

        if (reader.has("target-percentage"))
            player.getPlayerKillingAttributes().setTargetPercentage(reader.get("target-percentage").getAsInt());

        if (reader.has("dfs-charges"))
            player.incrementDfsCharges(reader.get("dfs-charges").getAsInt());

        if (reader.has("bh-rank"))
            player.getAppearance().setBountyHunterSkull(reader.get("bh-rank").getAsInt());
        if (reader.has("auto-retaliate"))
            player.setAutoRetaliate(reader.get("auto-retaliate").getAsBoolean());
        if (reader.has("gravestone"))
            player.setGravestone(reader.get("gravestone").getAsInt());
        if (reader.has("xp-locked"))
            player.setExperienceLocked(reader.get("xp-locked").getAsBoolean());
        if (reader.has("def-cast"))
            player.setDefensiveCasting(reader.get("def-cast").getAsBoolean());
        if (reader.has("veng-cast"))
            player.setHasVengeance(reader.get("veng-cast").getAsBoolean());
        if (reader.has("last-veng"))
            player.getTimers().getLastVengeance().reset(reader.get("last-veng").getAsLong());
        if (reader.has("fight-type"))
            player.setFightType(FightType.valueOf(reader.get("fight-type").getAsString()));
        if (reader.has("recoil-deg"))
            player.setRecoilCharges(reader.get("recoil-deg").getAsInt());

    }

    @Override
    public void setDefaults(Player player) {

    }
}
