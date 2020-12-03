package com.fury.game.system.files;

import com.fury.game.GameSettings;

import java.io.File;
import java.util.*;

public class Resources {

    static Map<String, String> resource_directories = new HashMap<>();
    static Map<String, String> resource_files = new HashMap<>();
    static Map<String, String> save_directories = new HashMap<>();
    static Map<String, String> save_files = new HashMap<>();
    static List<String> exclusions = new ArrayList<>();

    public static void main(String[] args) {
        init();
    }

    public static void init() {
        setupResources();

        File file = new File(GameSettings.RESOURCES);
        if (file.exists())
            listResources(file);

        exclusions.clear();
        setupSaves();

        file = new File(GameSettings.SAVES);
        if (file.exists())
            listSaves(file);

        exclusions.clear();
        exclusions = null;
    }

    private static void setupResources() {
        exclusions.add("tools");
        exclusions.add("revisions");
        exclusions.add("backup");
        exclusions.add("cache");
        exclusions.add("saves");
    }

    private static void setupSaves() {
        exclusions.add("def");
        exclusions.add("tools");
        exclusions.add("revisions");
        exclusions.add("backup");
        exclusions.add("cache");
        exclusions.add("characters");
        exclusions.add("clans");
        exclusions.add("construction");
        exclusions.add("dungeoneering");
        exclusions.add("farming");
        exclusions.add("logs");
        exclusions.add("oldlogs");
        exclusions.add("notes");
        exclusions.add("songs");
        exclusions.add("settings");
        exclusions.add("slayer");
        exclusions.add("refer");
        exclusions.add("summoning");
        exclusions.add("combat");
        exclusions.add("prayer");
        exclusions.add("world");
    }

    public static void listResources(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String name = file.getName().toLowerCase();
                if (name.contains("."))
                    name = name.substring(0, file.getName().indexOf("."));
                resource_files.put(name, file.getPath().substring(GameSettings.RESOURCES.length()));
            } else if (file.isDirectory()) {
                resource_directories.put(file.getName(), file.getPath().substring(GameSettings.RESOURCES.length()));
                if (!exclusions.contains(file.getName()))
                    listResources(file);
            }
        }
    }

    public static void listSaves(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String name = file.getName().toLowerCase();
                if (name.contains("."))
                    name = name.substring(0, file.getName().indexOf("."));
                save_files.put(name, file.getPath().substring(GameSettings.SAVES.length()));
            } else if (file.isDirectory()) {
                save_directories.put(file.getName(), file.getPath().substring(GameSettings.SAVES.length()));
                if (!exclusions.contains(file.getName()))
                    listSaves(file);
            }
        }
    }

    public static String getSaveFile(String name) {
        if (save_files.containsKey(name.toLowerCase()))
            return GameSettings.SAVES + save_files.get(name.toLowerCase());
        else
            return "";
    }

    public static String getSaveDirectory(String name) {
        if (save_directories.containsKey(name.toLowerCase()))
            return GameSettings.SAVES + save_directories.get(name.toLowerCase()) + GameSettings.SLASH;
        else
            return "";
    }

    public static String getFile(String name) {
        if (resource_files.containsKey(name.toLowerCase()))
            return GameSettings.RESOURCES + resource_files.get(name.toLowerCase());
        else
            return "";
    }

    public static String getDirectory(String name) {
        if (resource_directories.containsKey(name.toLowerCase()))
            return GameSettings.RESOURCES + resource_directories.get(name.toLowerCase()) + GameSettings.SLASH;
        else
            return "";
    }
}
