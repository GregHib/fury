package com.fury.game.system.files.world;

import com.fury.game.system.files.world.single.impl.*;
import com.fury.game.system.files.world.increment.timer.TimedFileHandler;

public class WorldFileHandler {
    public static void init() {
        loadBans();
        loadMutes();
        HWID.PromotionClaims.get().init();

        Starters.get().init();
        Compensation.get().init();

        TimedFileHandler.init();
    }

    public static void save() {
        saveBans();
        saveMutes();
        HWID.PromotionClaims.get().save();

        Starters.get().save();
        Compensation.get().save();

        TimedFileHandler.save();
    }

    public static void loadBans() {
        HWID.Bans.get().init();
        Bans.get().init();
    }

    public static void saveBans() {
        HWID.Bans.get().save();
        Bans.get().save();
    }

    public static void loadMutes() {
        HWID.Mutes.get().init();
        Mutes.get().init();
    }

    public static void saveMutes() {
        HWID.Mutes.get().save();
        Mutes.get().save();
    }
}
