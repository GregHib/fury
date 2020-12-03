package com.fury.game;

import com.fury.game.world.map.Position;
import com.fury.util.Misc;

import java.io.File;
import java.util.Calendar;

public class GameSettings {

    public static final String NAME = "Fury";
    public static final String WEBSITE = "http://furyps.com";
    public static final String DISCORD = "https://discord.gg/sTE7QUm";
    public static boolean HOSTED = System.getProperty("user.dir").contains("TeamCity");
    private static final String DATA_DIRECTORY = "data";
    public static final String SLASH = File.separator;

    public static final String RESOURCES = HOSTED ? System.getProperty("user.dir") + SLASH + DATA_DIRECTORY + SLASH + "def" + SLASH : "." + SLASH + DATA_DIRECTORY + SLASH + "def";

    public static final String SAVES = HOSTED ? System.getProperty("user.home") + SLASH + "Documents" + SLASH + "onyx" + SLASH + DATA_DIRECTORY + SLASH : "." + SLASH + DATA_DIRECTORY + SLASH;

    public static final boolean JAGGRAB_ENABLED = false;

    public static boolean DEBUG = false;//HOSTED;

    public static final boolean PACK = !HOSTED;//~1s slower

    public static final int GAME_VERSION = 32;

    public static final int GAME_UID = 11;

    public static final int LOGIN_THRESHOLD = 20;

    public static final int LOGOUT_THRESHOLD = 20;

    public static final int VOTE_REWARDING_THRESHOLD = 15;


    /** GAME **/

    public static final long MIN_FREE_MEM_ALLOWED = 30000000;
    public static boolean MYSQL_ENABLED = HOSTED;
    public static boolean BONUS_EXP = Misc.isWeekend();
    public static boolean YELL_ACTIVE = true;

    public static int LOYALTY_RESET_DAY = Calendar.SATURDAY;
    public static int LOYALTY_RESET_HOUR = 11;
    public static int LOYALTY_RESET_MINUTE = 0;
    public static final int MAX_STARTERS_PER_IP = 5;
    public static final int CONNECTION_LIMIT = 3;

    public static final long COMBAT_DELAY = 6000;
    public static final long COMBAT_LOGOUT_TIME = 8000;

    public static final Position DEFAULT_POSITION = new Position(3093, 3503);


    public static final int
            ATTACK_TAB = 0,
            ACHIEVEMENT_TAB = 1,
            SKILLS_TAB = 2,
            QUESTS_TAB = 3,
            INVENTORY_TAB = 4,
            EQUIPMENT_TAB = 5,
            PRAYER_TAB = 6,
            MAGIC_TAB = 7,

            SUMMONING_TAB = 8,
            FRIEND_TAB = 9,
            IGNORE_TAB = 10,
            CLAN_CHAT_TAB = 11,
            LOGOUT = 16,
            OPTIONS_TAB = 12,
            EMOTES_TAB = 13,
            MUSIC_TAB = 14,
            NOTES_TAB = 15;

    public static final int[] MAP_SIZES = { 104, 120, 136, 168, 72 };
}
