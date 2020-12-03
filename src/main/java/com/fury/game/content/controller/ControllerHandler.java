package com.fury.game.content.controller;

import com.fury.game.GameSettings;
import com.fury.game.content.controller.impl.*;
import com.fury.game.content.controller.impl.duel.DuelController;
import com.fury.game.content.global.minigames.impl.Barrows;
import com.fury.game.content.global.minigames.impl.WarriorsGuild;
import com.fury.game.content.skill.free.dungeoneering.DungeonController;

import java.util.HashMap;

public class ControllerHandler {

    private static final HashMap<Object, Class<? extends Controller>> handledControllers = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void init() {
        long start = System.currentTimeMillis();
        try {
            handledControllers.put("Wilderness", (Class<Controller>) Class.forName(Wilderness.class.getCanonicalName()));
            handledControllers.put("Daemonheim", (Class<Controller>) Class.forName(Daemonheim.class.getCanonicalName()));
            handledControllers.put("DuelController", (Class<Controller>) Class.forName(DuelController.class.getCanonicalName()));
            handledControllers.put("PuroPuro", (Class<Controller>) Class.forName(PuroPuro.class.getCanonicalName()));
            handledControllers.put("WarriorsGuild", (Class<Controller>) Class.forName(WarriorsGuild.class.getCanonicalName()));
            handledControllers.put("GodWars", (Class<Controller>) Class.forName(GodWars.class.getCanonicalName()));
            handledControllers.put("ZarosGodwars", (Class<Controller>) Class.forName(ZarosGodwars.class.getCanonicalName()));
            handledControllers.put("Barrows", (Class<Controller>) Class.forName(Barrows.class.getCanonicalName()));
            handledControllers.put("FightCavesController", (Class<Controller>) Class.forName(FightCavesController.class.getCanonicalName()));
            handledControllers.put("JailController", (Class<Controller>) Class.forName(JailController.class.getCanonicalName()));
            handledControllers.put("FreeForAllController", (Class<Controller>) Class.forName(FreeForAllController.class.getCanonicalName()));
            handledControllers.put("PestControlLobby", (Class<Controller>) Class.forName(PestControlLobby.class.getCanonicalName()));
            handledControllers.put("JadinkoLair", (Class<Controller>) Class.forName(JadinkoLair.class.getCanonicalName()));
            handledControllers.put("FirstAdventureController", (Class<Controller>) Class.forName(FirstAdventureController.class.getCanonicalName()));
            handledControllers.put("Abyss", (Class<Controller>) Class.forName(Abyss.class.getCanonicalName()));

            handledControllers.put("FightKilnController", (Class<Controller>) Class.forName(FightKiln.class.getCanonicalName()));
            handledControllers.put("QueenBlackDragonController", (Class<Controller>) Class.forName(QueenBlackDragonController.class.getCanonicalName()));
            handledControllers.put("HouseController", (Class<Controller>) Class.forName(HouseController.class.getCanonicalName()));
            handledControllers.put("DungeonController", (Class<Controller>) Class.forName(DungeonController.class.getCanonicalName()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if(GameSettings.DEBUG)
            System.out.println(handledControllers.size() + " controllers loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void reload() {
        handledControllers.clear();
        init();
    }

    public static Controller getController(Object key) {
        if (key instanceof Controller)
            return (Controller) key;
        Class<? extends Controller> classC = handledControllers.get(key);
        if (classC == null)
            return null;
        try {
            return classC.getConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
