package com.fury.game.content.global.quests;

import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.entity.object.GameObject;
import com.fury.util.FontUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class QuestManager {

    private Map<Quests, Quest> activeQuests = new HashMap<>();
    private Player player;

    public QuestManager(Player player) {
        this.player = player;
        init();
    }

    public boolean hasStarted(Quests quest) {
        return activeQuests.containsKey(quest) && !activeQuests.get(quest).hasFinished();
    }

    public Quest getQuest(Quests quest) {
        return activeQuests.get(quest);
    }

    public void start(Quest quest) {
        if (quest.hasRequirements(player)) {
            activeQuests.put(quest.getQuest(), quest);
            quest.start(player);
        }
    }

    private void init() {
        for (Quests quest : Quests.values()) {

        }
    }

    public int getQuestPoints() {
        int total = 0;
        for (Quest quest : activeQuests.values())
            if (quest.hasFinished())
                total += quest.getRewards().questPoints();
        return total;
    }

    public void refreshTab() {

    }

    public boolean isObjectOption1(GameObject object) {
        for (Quest quest : activeQuests.values())
            if (!quest.hasFinished() && quest.isObjectOption1(player, object))
                return true;
        return false;
    }

    public boolean isNpcOption1(Mob mob) {
        for (Quest quest : activeQuests.values())
            if (!quest.hasFinished() && quest.isNpcOption1(player, mob))
                return true;
        return false;
    }

    public boolean isButtonPressed(int button) {
        for (Quest quest : activeQuests.values())
            if (!quest.hasFinished() && quest.isButtonPressed(player, button))
                return true;
        return false;
    }

    public Map<Quests, Quest> getQuests() {
        return activeQuests;
    }

    public JsonArray getQuestsJson() {
        JsonArray array = new JsonArray(activeQuests.size());
        for (Quests key : activeQuests.keySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", key.name());
            object.addProperty("stage", activeQuests.get(key).getStage());
            array.add(object);
        }
        return array;
    }

    public void setQuests(JsonArray quests) {
        for (JsonElement ele : quests) {
            try {
                JsonObject object = ele.getAsJsonObject();
                Quests name = null;
                Quest quest = null;
                if (object.has("name")) {
                    name = Quests.valueOf(object.get("name").getAsString());
                    quest = name.getAClass().getConstructor().newInstance();
                }

                if (object.has("stage") && quest != null)
                    quest.setStage(object.get("stage").getAsInt());

                if (name != null && quest != null)
                    activeQuests.put(name, quest);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTabString(Quests quest) {
        String colour = FontUtils.RED;
        if(activeQuests.containsKey(quest)) {
            if(activeQuests.get(quest).hasFinished())
                colour = FontUtils.GREEN;
            else
                colour = FontUtils.YELLOW;
        }
        return colour + quest.getName() + FontUtils.COL_END;
    }

    public boolean hasFinished(Quests quest) {
        return activeQuests.containsKey(quest) && activeQuests.get(quest).hasFinished();
    }

    public void end(Quests quest) {
        if(activeQuests.containsKey(quest))
            activeQuests.remove(quest);
    }
}
