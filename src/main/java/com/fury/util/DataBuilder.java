package com.fury.util;

import com.fury.game.GameSettings;

import java.io.File;
import java.io.IOException;

public class DataBuilder {

    final static String[] directories ={
            "backup",
            "cache",
            "def/items",
            "def/json",
            "def/npcs",
            "def/txt",
            "saves/characters",
            "saves/clans",
            "saves/construction",
            "saves/dungeoneering",
            "saves/farming",
            "saves/GE",
            "saves/logs",
            "saves/oldlogs",
            "saves/notes",
            "saves/songs",
            "saves/banks",
            "saves/settings",
            "saves/slayer",
            "saves/refer",
            "saves/summoning",
            "saves/combat",
            "saves/prayer",
            "saves/world",
            "saves/world/punishment"
    };

    final static String[] files = {
            "blockedhosts.txt",
            "top-exp.txt",
            "top-killstreaks.txt",
            "top-pkers.txt",
            "votes.txt",
            "statue.txt",
            "dailydonor.txt",
            "dailysnowflake.txt",
            "world/punishment/hardwarebans.txt",
            "world/punishment/hardwaremutes.txt",
            "world/punishment/macbans.txt",
            "world/punishment/bans.txt",
            "world/punishment/mutes.txt",
            "world/logins.txt",
            "world/compensation.txt",
            "GE/offers.dat"
    };

    public static void init() {
        File resources = new File(GameSettings.SAVES);
        if(!resources.exists())
            resources.mkdir();
        for(String path : directories) {
            File directory = new File(GameSettings.SAVES + path);
            if(!directory.exists())
                directory.mkdirs();
        }

        try {
            for(String path : files) {
                File directory = new File(GameSettings.SAVES + "saves" + GameSettings.SLASH + path);
                if(!directory.exists())
                    directory.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
