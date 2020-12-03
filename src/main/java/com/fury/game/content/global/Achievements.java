package com.fury.game.content.global;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.world.GameWorld;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

public class Achievements {

    private static int easyTasksId = 34006;
    private static int medTasksId = 34059;
    private static int hardTasksId = 34111;
    private static int eliteTasksId = 34145;

    public enum AchievementData {
        TALK_WITH_GUIDE(Difficulty.EASY, "Talk With The Guide", easyTasksId++, null, "To do this either complete this on login or talk to the Fury Guide at Edgeville"),
        KILL_A_YAK(Difficulty.EASY, "Kill A Yak", easyTasksId++, null, ""),
        BURY_A_BONE(Difficulty.EASY, "Bury A Bone", easyTasksId++, null, ""),
        KILL_A_CHICKEN(Difficulty.EASY, "Kill A Chicken", easyTasksId++, null, ""),
        OFFER_BONES_ON_ALTAR(Difficulty.EASY, "Offer Bones On An Altar", easyTasksId++, null, ""),
        CUT_A_TREE(Difficulty.EASY, "Cut A Tree", easyTasksId++, null, ""),
        LIGHT_A_FIRE(Difficulty.EASY, "Light A Fire", easyTasksId++, null, ""),
        COOK_YAK_MEAT(Difficulty.EASY, "Cook Yak Meat", easyTasksId++, null, ""),
        CATCH_A_TROUT(Difficulty.EASY, "Catch A Trout", easyTasksId++, null, ""),
        EAT_A_SALMON(Difficulty.EASY, "Eat A Salmon", easyTasksId++, null, ""),
        CATCH_A_CRIMSON_SWIFT(Difficulty.EASY, "Catch A Crimson Swift", easyTasksId++, null, ""),
        PICK_FLAX(Difficulty.EASY, "Pick A Flax", easyTasksId++, null, ""),
        SPIN_BOW_STRING(Difficulty.EASY, "Spin A Bow String", easyTasksId++, null, ""),
        CUT_AN_OAK_TREE(Difficulty.EASY, "Cut An Oak Tree", easyTasksId++, null, ""),
        FLETCH_ARROW_SHAFTS(Difficulty.EASY, "Fletch Arrow Shafts", easyTasksId++, null, ""),
        FILL_VIAL(Difficulty.EASY, "Fill A Vial", easyTasksId++, null, "Fill an empty vial with water"),
        MINE_COPPER_ORE(Difficulty.EASY, "Mine Copper Ore", easyTasksId++, null, ""),
        MINE_TIN_ORE(Difficulty.EASY, "Mine Tin Ore", easyTasksId++, null, ""),
        SMELT_A_BRONZE_BAR(Difficulty.EASY, "Smelt A Bronze Bar", easyTasksId++, null, ""),
        SMITH_A_BRONZE_DAGGER(Difficulty.EASY, "Smith A Bronze Dagger", easyTasksId++, null, ""),
        EQUIP_LEATHER_BOOTS(Difficulty.EASY, "Equip Leather Boots", easyTasksId++, null, "Equip some leather boots."),
        EAT_A_BAR_OF_CHOCOLATE(Difficulty.EASY, "Eat Chocolate", easyTasksId++, null, "Eat a bar of chocolate."),
        BUY_AN_IRON_PLATEBODY(Difficulty.EASY, "Buy an iron platebody", easyTasksId++, null, "Buy an iron platebody from any store."),
        //LOCATE_USING_A_TALISMAN(Difficulty.EASY, "Locate", easyTasksId++, null, "Locate using a talisman"),
        CRAFT_AN_EARTH_RUNE(Difficulty.EASY, "Craft An Earth Rune", easyTasksId++, null, ""),
        LISTEN_TO_MUSICIAN(Difficulty.EASY, "Listen To A Musician", easyTasksId++, null, ""),
        CROSS_A_LOG_BALANCE(Difficulty.EASY, "Cross A Log Balance", easyTasksId++, null, ""),
        SUMMON_A_DREADFOWL(Difficulty.EASY, "Summon A Dreadfowl", easyTasksId++, null, ""),
        ATTACK_A_BANDIT(Difficulty.EASY, "Attack A Bandit", easyTasksId++, null, ""),
        EAT_SOME_MEAT(Difficulty.EASY, "Eat Some Meat", easyTasksId++, null, ""),
        PRAY_AT_AN_ALTAR(Difficulty.EASY, "Pray At An Altar", easyTasksId++, null, ""),
        PICKPOCKET_A_MAN(Difficulty.EASY, "Pickpocket A Man", easyTasksId++, null, ""),
        DISMISS_A_FAMILIAR(Difficulty.EASY, "Dismiss A Familiar", easyTasksId++, null, ""),
        ADD_COINS_TO_POUCH(Difficulty.EASY, "Add Coins To Pouch", easyTasksId++, null, "Add some coins to your pouch."),
        TALK_TO_DUNG_TUTOR(Difficulty.EASY, "Talk To Dung Tutor", easyTasksId++, null, "Speak with the dungeoneering tutor in daemonheim."),
        DEPOSIT_ALL(Difficulty.EASY, "Deposit All", easyTasksId++, null, "Deposit all carried items into a bank."),
        COMPLETE_A_C1_DUNG(Difficulty.EASY, "Complete A C1 Dung", easyTasksId++, null, "Complete a complexity 1 dungeon."),
        TALK_TO_REWARDS_TRADER(Difficulty.EASY, "Talk To Rewards Trader", easyTasksId++, null, "Talk to the rewards trader in daemonheim."),
        BUY_A_ANTI_DRAGON_SHIELD(Difficulty.EASY, "Buy An Anti-Dragon Shield", easyTasksId++, null, ""),
        USE_TAVERLEY_SHORTCUT(Difficulty.EASY, "Use The Taverley Shortcut", easyTasksId++, null, ""),
        KILL_A_BABY_BLUE_DRAGON(Difficulty.EASY, "Kill A Baby Blue Dragon", easyTasksId++, null, ""),
        GRIND_BLUE_SCALE_DUST(Difficulty.EASY, "Grind Blue Dragon Scales", easyTasksId++, null, "Grind blue dragon scales into dragon scale dust."),
        STEAL_A_CAKE(Difficulty.EASY, "Steal A Cake", easyTasksId++, null, ""),
        COMPLETE_A_SLAYER_TASK(Difficulty.EASY, "Get A Slayer Task", easyTasksId++, null, ""),
        USE_A_TELEPORT_TAB(Difficulty.EASY, "Use A Teleport Tablet", easyTasksId++, null, ""),
        TELEPORT_SPIRIT_TREE(Difficulty.EASY, "Teleport Using Spirit Tree", easyTasksId++, null, ""),
        PRICE_CHECK_A_CAKE(Difficulty.EASY, "Price Check A Cake", easyTasksId++, null, ""),
        //MAKE_A_PLANK(Difficulty.EASY, "Make A Plank", easyTasksId++, null, "Make a plank using"),
        SET_A_BANK_PIN(Difficulty.EASY, "Set A Bank Pin", easyTasksId++, null, ""),
        TALK_TO_KING_HEALTHORG(Difficulty.EASY, "Talk To King Healthorg", easyTasksId++, null, "Speak to King Healthorg about voting"),
        SWITCH_ANCIENT_SPELLBOOK(Difficulty.EASY, "Switch To Ancient Spellbook", easyTasksId++, null, ""),
        ADD_A_FRIEND(Difficulty.EASY, "Add A Friend", easyTasksId++, null, ""),
        AIR_GUITAR(Difficulty.EASY, "Air guitar!", easyTasksId++, null, ""),

        HAVE_A_REST(Difficulty.MEDIUM, "Have A Rest", medTasksId++, null, "Rest"),
        CUT_A_YEW_TREE(Difficulty.MEDIUM, "Cut A Yew Tree", medTasksId++, null, ""),
        BURN_5_YEW_LOGS(Difficulty.MEDIUM, "Burn 5 Yew Logs", medTasksId++, new int[]{0, 5}, ""),
        KILL_A_BLUE_DRAGON_USING_RANGED(Difficulty.MEDIUM, "Kill A Blue Dragon Using Range", medTasksId++, null, ""),
        BURY_A_DRAGON_BONE(Difficulty.MEDIUM, "Bury A Dragon Bone", medTasksId++, null, ""),
        TAN_DRAGONHIDE(Difficulty.MEDIUM, "Tan Blue Dragonhide", medTasksId++, null, ""),
        CRAFT_BLUE_VAMBRACES(Difficulty.MEDIUM, "Craft Blue D'hide Vambraces", medTasksId++, null, ""),
        SWITCH_LUNAR_SPELLBOOK(Difficulty.MEDIUM, "Switch To Lunar Spellbook", medTasksId++, null, ""),
        PERFORM_SPECIAL_ATTACK(Difficulty.MEDIUM, "Perform A Special Attack", medTasksId++, null, ""),
        KILL_A_PLAYER_WILDERNESS(Difficulty.MEDIUM, "Kill A Player In The Wilderness", medTasksId++, null, ""),
        SETUP_DWARF_CANNON(Difficulty.MEDIUM, "Setup A Dwarf Cannon", medTasksId++, null, ""),
        KILL_A_WHITE_WOLF(Difficulty.MEDIUM, "Kill A White Wolf", medTasksId++, null, ""),
        MAKE_10_SPIRIT_WOLF_POUCHES(Difficulty.MEDIUM, "Make 10 Spirit Wolf Pouches", medTasksId++, new int[]{1, 10}, ""),
        TABLET_TELEPORT_TO_NATURE_ALTAR(Difficulty.MEDIUM, "Tablet Tele To Nature Altar", medTasksId++, null, ""),
        CRAFT_50_NATURE_RUNES(Difficulty.MEDIUM, "Craft 50 Nature Runes", medTasksId++, new int[]{2, 50}, ""),
        TRAVEL_TO_THE_LAW_ALTAR_BY_ABYSS(Difficulty.MEDIUM, "Travel To Law Altar By Abyss", medTasksId++, null, ""),
        CRAFT_RUNES_USING_ZMI(Difficulty.MEDIUM, "Craft runes using ZMI Altar", medTasksId++, null, ""),
        SWITCH_TO_CURSES(Difficulty.MEDIUM, "Switch To Curses", medTasksId++, null, "Switch to Curses Prayerbook."),
        WITHDRAW_10K(Difficulty.MEDIUM, "Withdraw 10k", medTasksId++, null, "Withdraw 10k from your money pouch."),
        BUY_AN_AIR_BATTLESTAFF(Difficulty.MEDIUM, "Buy An Air Battlestaff", medTasksId++, null, "Buy an air battlestaff from zaff"),
        SELL_AN_ITEM(Difficulty.MEDIUM, "Sell An Item", medTasksId++, null, "Sell an item to the general store."),
        SELL_AN_ITEM_GE(Difficulty.MEDIUM, "Sell An Item GE", medTasksId++, null, "Sell an item on the Grand Exchange."),
        ADD_A_SONG_TO_PLAYLIST(Difficulty.MEDIUM, "Add A Song To Playlist", medTasksId++, null, "Add a song to your playlist."),
        CATCH_20_LOBSTERS(Difficulty.MEDIUM, "Catch 20 Lobsters", medTasksId++, new int[]{3, 20}, ""),
        BAKE_A_CAKE(Difficulty.MEDIUM, "Bake A Cake", medTasksId++, null, ""),
        MINE_SHOOTING_STAR(Difficulty.MEDIUM, "Mine A Shooting Star", medTasksId++, null, ""),
        MINE_100_COAL(Difficulty.MEDIUM, "Mine 100 Coal", medTasksId++, new int[]{4, 100}, ""),
        FLETCH_100_ARROWS(Difficulty.MEDIUM, "Fletch 100 Arrows", medTasksId++, new int[]{5, 100}, ""),
        CUT_50_MAPLES(Difficulty.MEDIUM, "Cut 50 Maple Trees", medTasksId++, new int[]{6, 50}, ""),
        SEARCH_5_BIRDS_NESTS(Difficulty.MEDIUM, "Search 5 Bird's Nests", medTasksId++, new int[]{7, 5}, ""),
        CRUSH_A_BIRDS_NEST(Difficulty.MEDIUM, "Crush A Bird's Nest", medTasksId++, null, ""),
        KILL_A_UNHOLY_CURSEBEARER(Difficulty.MEDIUM, "Kill An Unholy Cursebearer", medTasksId++, null, ""),
        BIND_AN_ITEM(Difficulty.MEDIUM, "Bind An Item", medTasksId++, null, "Bind an item in dungeoneering."),
        COMPLETE_A_DUNGEON_WITHOUT_DEATH(Difficulty.MEDIUM, "Complete Dung No Death", medTasksId++, null, "Complete a dungeoneering dungeon without dieing a single time."),
        CREATE_A_GATESTONE(Difficulty.MEDIUM, "Create a gatestone", medTasksId++, null, "Create a gatestone in dungeoneering."),
        CATCH_A_SALAMANDER(Difficulty.MEDIUM, "Catch A Salamander", medTasksId++, null, ""),
        KILL_A_MONSTER_USING_SALAMANDER(Difficulty.MEDIUM, "Kill A Monster With Salamander", medTasksId++, null, ""),
        LOOT_BARROWS_CHEST(Difficulty.MEDIUM, "Loot The Barrows Chest", medTasksId++, null, ""),
        REPAIR_A_BROKEN_ITEM(Difficulty.MEDIUM, "Repair A Broken Item", medTasksId++, null, "Repair a broken item with the squire in pest control."),
        FINISH_A_GAME_OF_PEST_CONTROL(Difficulty.MEDIUM, "Finish A Game Of Pest Control", medTasksId++, null, ""),
        KILL_A_PORTAL(Difficulty.MEDIUM, "Kill A Portal", medTasksId++, null, ""),
        FINISH_PEST_CONTROL_WITHOUT_DEATH(Difficulty.MEDIUM, "Finish Pest Control Without Deaths", medTasksId++, null, "Complete a game of pest control without dieing a single time."),
        JOIN_A_CLAN_CHAT(Difficulty.MEDIUM, "Join A Clan Chat", medTasksId++, null, ""),
        NAME_YOUR_CLAN(Difficulty.MEDIUM, "Name Your Clan", medTasksId++, null, ""),
        ACTIVATE_LOOTSHARE(Difficulty.MEDIUM, "Activate LootShare", medTasksId++, null, ""),
        RECEIVE_LOOTSHARE_DROP(Difficulty.MEDIUM, "Receive A LootShare Drop", medTasksId++, null, ""),
        PROMOTE_A_FRIEND(Difficulty.MEDIUM, "Promote A Friend", medTasksId++, null, "Promote a friend in your clan chat."),
        TALK_TO_SMITHING_TUTOR(Difficulty.MEDIUM, "Talk To Smithing Tutor", medTasksId++, null, "Talk to the smithing tutor in varrock."),
        KILL_A_DAGANNOTH_KING(Difficulty.MEDIUM, "Kill A Dagannoth King", medTasksId++, null, ""),
        COMPLETE_A_MEDIUM_CLUE(Difficulty.MEDIUM, "Complete A Med Clue Scroll", medTasksId++, null, ""),

        KILL_100_GLACORS(Difficulty.HARD, "Kill 100 Glacors", hardTasksId++, new int[]{8, 100}, ""),
        KILL_100_CHAOS_DWARF_CANNONNEERS(Difficulty.HARD, "Kill 100 Chaos Dwarves", hardTasksId++, new int[]{9, 100}, ""),
        RECEIVE_HAND_CANNON_DROP(Difficulty.HARD, "Get A Hand Cannon Drop", hardTasksId++, null, ""),
        LOW_ALCH_1M_ITEM(Difficulty.HARD, "Low Alch A Item Worth 1m", hardTasksId++, null, "Perform low alchemy on a single item worth 1 million gp or more."),
        KILL_100_FROST_DRAGONS(Difficulty.HARD, "Kill 100 Frost Dragons", hardTasksId++, new int[]{45, 100}, ""),
        CATCH_500_GRENWALLS(Difficulty.HARD, "Catch 500 Grenwalls", hardTasksId++, new int[]{10, 500}, ""),
        LOOT_1000_IMP_JARS(Difficulty.HARD, "Loot 1000 Impling Jars", hardTasksId++, new int[]{11, 1000}, ""),
        PICKPOCKET_5000_PEOPLE(Difficulty.HARD, "Pickpocket 5000 people", hardTasksId++, new int[]{12, 5000}, ""),
        KILL_WITH_WRATH(Difficulty.HARD, "Kill With Wrath", hardTasksId++, null, "Deal a finishing blow to a monster using wrath."),
        KILL_100_REVENANTS(Difficulty.HARD, "Kill 100 Revenants", hardTasksId++, new int[]{46, 100}, ""),
        SMELT_1000_RUNE_BARS(Difficulty.HARD, "Smelt 1000 Rune Bars", hardTasksId++, new int[]{13, 1000}, ""),
        USE_FAIRY_RING_TELEPORT(Difficulty.HARD, "Use Fairy Ring Teleport", hardTasksId++, null, ""),
        DEFEAT_JAD(Difficulty.HARD, "Defeat Jad", hardTasksId++, null, ""),
        OPEN_THE_DOOR(Difficulty.HARD, "Open the door", hardTasksId++, null, "Open a door..."),
        GET_OFF_THE_FLOOR(Difficulty.HARD, "Get Off The Floor", hardTasksId++, null, "Pick an item up off the floor..."),
        EVERYBODY_WALK_THE_DINOSAUR(Difficulty.HARD, "Everybody Walk The Dinosaur", hardTasksId++, null, "Kill a Mastyx in group dungeoneering."),
        COMPLETE_5_C6_WARPED_DUNGEONS(Difficulty.HARD, "Complete 5 C6 Warped Dungeons", hardTasksId++, new int[]{14, 5}, "Complete 5 complexity 6 Warped dungeoneering dungeons."),
        COMPLETE_35_SLAYER_TASKS(Difficulty.HARD, "Complete 35 Slayer Tasks", hardTasksId++, new int[]{15, 35}, ""),
        PRETTY_FIRE(Difficulty.HARD, "Pretty Fire", hardTasksId++, null, "Light a fire using a firelighter."),
        GROW_A_PET(Difficulty.HARD, "Grow A Pet", hardTasksId++, null, ""),
        ATTACK_USING_A_DFS(Difficulty.HARD, "Attack Using A DFS", hardTasksId++, null, "Attack using a dragonfire shield's charge."),
        KILL_THE_KBD_USING_A_BRONZE_DAGGER(Difficulty.HARD, "Kill The KBD", hardTasksId++, null, "Kill the KBD using nothing but a bronze dagger."),
        DEFEAT_NEX(Difficulty.HARD, "Defeat Nex", hardTasksId++, null, ""),
        MAKE_100_OVERLOADS(Difficulty.HARD, "Make 100 Overloads", hardTasksId++, new int[]{16, 100}, ""),
        CRAFT_6000_BLOOD_RUNES(Difficulty.HARD, "Craft 6000 Blood Runes", hardTasksId++, new int[]{17, 6000}, ""),
        STEAL_100000_ITEMS(Difficulty.HARD, "Steal 100,000 items", hardTasksId++, new int[]{18, 100000}, ""),
        MAKE_500_STEEL_TITAN_POUCHES(Difficulty.HARD, "Make 500 Steel Titan Pouches", hardTasksId++, new int[]{19, 500}, ""),
        CUT_2000_MAGIC_LOGS(Difficulty.HARD, "Cut 2000 Magic Logs", hardTasksId++, new int[]{20, 2000}, ""),
        BURN_2500_MAGIC_LOGS(Difficulty.HARD, "Burn 2500 Magic Logs", hardTasksId++, new int[]{21, 2500}, ""),
        COMPLETE_1000_AGILITY_COURSE_LAPS(Difficulty.HARD, "Complete 1000 Agility Laps", hardTasksId++, new int[]{22, 1000}, "Complete 1000 agility course laps."),
        REACH_MAX_EXP_IN_A_SKILL(Difficulty.HARD, "Reach 200m Exp In A Skill", hardTasksId++, null, ""),
        SEAL_THE_DEAL(Difficulty.HARD, "Seal the deal", hardTasksId++, null, ""),

        FIRE_50000_CANNON_BALLS(Difficulty.ELITE, "Fire 50,000 Cannon Balls", eliteTasksId++, new int[]{23, 50000}, ""),
        ALCH_100M(Difficulty.ELITE, "Receive 100m From Alchemy", eliteTasksId++, new int[]{24, 100000000}, ""),
        BURY_5000_BONES(Difficulty.ELITE, "Bury 5000 Bones", eliteTasksId++, new int[]{25, 5000}, ""),
        OFFER_5000_BONES(Difficulty.ELITE, "Offer 5000 Bones", eliteTasksId++, new int[]{26, 5000}, ""),
        SCATTER_500_ASHES(Difficulty.ELITE, "Scatter 500 Ashes", eliteTasksId++, new int[]{27, 500}, ""),
        BURY_1000_FROST_DRAGON_BONES(Difficulty.ELITE, "Bury 1000 Frost Dragon Bones", eliteTasksId++, new int[]{28, 1000}, ""),
        COMPLETE_100_DUNGEONS(Difficulty.ELITE, "Complete 100 Dungeons", eliteTasksId++, new int[]{29, 100}, ""),
        TELEPORT_10000_TIMES(Difficulty.ELITE, "Teleport 10000 Times", eliteTasksId++, new int[]{30, 10000}, ""),
        UNLOCK_150_SONGS(Difficulty.ELITE, "Unlock 150 Songs", eliteTasksId++, null, ""),
        OPEN_10_PVP_BOXES(Difficulty.ELITE, "Open 10 PvP Boxes", eliteTasksId++, new int[]{31, 10}, ""),
        MINE_1000_RUNITE_ORE(Difficulty.ELITE, "Mine 1000 Runite Ore", eliteTasksId++, new int[]{32, 1000}, ""),
        KILL_10000_MONSTERS(Difficulty.ELITE, "Kill 10,000 Monsters", eliteTasksId++, new int[]{33, 10000}, ""),
        COMPLETE_15_ELITE_CLUE_SCROLLS(Difficulty.ELITE, "Complete 15 Elite Clue Scrolls", eliteTasksId++, new int[]{34, 15}, ""),
        VOTE_100_TIMES(Difficulty.ELITE, "Vote 100 Times", eliteTasksId++, new int[]{35, 100}, ""),
        CREATE_A_FURY(Difficulty.ELITE, "Create A Fury", eliteTasksId++, null, ""),
        DEAL_15M_DAMAGE(Difficulty.ELITE, "Deal 15m Damage", eliteTasksId++, new int[]{36, 15000000}, ""),
        KILL_QBD_100_TIMES(Difficulty.ELITE, "Kill QBD 100 Times", eliteTasksId++, new int[]{38, 100}, ""),
        REACH_MAX_LEVEL_IN_ALL_SKILLS(Difficulty.ELITE, "Reach Max Level In All Skills", eliteTasksId++, null, "Reach level 99 in all skills aside from dungeoneering which is level 120."),
        DEFEAT_500_BOSSES(Difficulty.ELITE, "Defeat 500 Boss Monsters", eliteTasksId++, new int[]{37, 500}, "");

        AchievementData(Difficulty difficulty, String interfaceLine, int interfaceFrame, int[] progressData, String interfaceText) {
            this.difficulty = difficulty;
            this.interfaceLine = interfaceLine;
            this.interfaceFrame = interfaceFrame;
            this.progressData = progressData;
            this.interfaceText = interfaceText;
        }

        private Difficulty difficulty;
        private String interfaceLine;
        private String interfaceText;
        private int interfaceFrame;
        private int[] progressData;

        public Difficulty getDifficulty() {
            return difficulty;
        }
    }

    public enum Difficulty {
        BEGINNER(5000), EASY(10000), MEDIUM(50000), HARD(100000), ELITE(2000000);
        int reward;

        Difficulty(int reward) {
            this.reward = reward;
        }

        public int getReward() {
            return reward;
        }
    }

    public static void init(Player player) {
        Achievements.updateInterface(player);

        int total = 0;
        for (int index = 0; index < AchievementData.values().length; index++) {
            AchievementData achievement = AchievementData.values()[index];
            if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
                total += achievement.difficulty.reward;
        }

        if (total > player.getPoints().getInt(Points.ACHIEVEMENT_REWARDS)) {
            player.message("You have been rewarded " + (total / 1000) + "k for your completed achievements.", 0x0d8011);
            player.getInventory().addCoins(total, true);
            player.getPoints().add(Points.ACHIEVEMENT_REWARDS, total);
        }
    }

    public static boolean handleButton(Player player, int button) {
        int index = -1;
        for (int i = 0; i < AchievementData.values().length; i++) {
            if (AchievementData.values()[i].interfaceFrame == button) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return false;

        processButton(player, index);
        return true;
    }

    private static void processButton(Player player, int index) {
        if (index >= 0 && index < AchievementData.values().length) {
            AchievementData achievement = AchievementData.values()[index];
            if (!achievement.interfaceText.isEmpty())
                player.message(achievement.interfaceText);
            if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
                player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have completed the achievement: " + achievement.interfaceLine + ".", 0x339900);
                updateInterface(player);
            } else if (achievement.progressData == null) {
                player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have not started the achievement: " + achievement.interfaceLine + ".", 0x660000);
            } else {
                int progress = player.getAchievementAttributes().getProgress()[achievement.progressData[0]];
                int requiredProgress = achievement.progressData[1];
                if (progress == 0) {
                    player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " You have not started the achievement: " + achievement.interfaceLine + ".", 0x660000);
                } else if (progress != requiredProgress) {
                    player.getPacketSender().sendMessage(FontUtils.imageTags(535) + " Your progress for this achievement is currently at: " + Misc.insertCommasToNumber("" + progress) + "/" + Misc.insertCommasToNumber("" + requiredProgress) + ".", 0xffff00);
                }
            }
        }
    }

    private static String prefixFormat = "Achievements: ";

    public static void updateInterface(Player player) {
        if (player.getAchievementAttributes().getCompletion().length != AchievementData.values().length) {
            player.setAchievementAttributes(new AchievementAttributes());//Reset
        }
        for (AchievementData achievement : AchievementData.values()) {
            boolean completed = player.getAchievementAttributes().getCompletion()[achievement.ordinal()];
            boolean progress = achievement.progressData != null && player.getAchievementAttributes().getProgress()[achievement.progressData[0]] > 0;
            int colour = completed ? Colours.GREEN : progress ? Colours.YELLOW : Colours.RED;
            player.getPacketSender().sendString(achievement.interfaceFrame, achievement.interfaceLine, colour);
        }
        player.getPacketSender().sendString(34001, prefixFormat + player.getPoints().getInt(Points.ACHIEVEMENT) + "/" + AchievementData.values().length);
    }

    public static void setPoints(Player player) {
        int points = 0;
        for (AchievementData achievement : AchievementData.values()) {
            if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()]) {
                points++;
            }
        }
        player.getPoints().set(Points.ACHIEVEMENT, points);
    }

    public static void doProgress(Player player, AchievementData achievement) {
        doProgress(player, achievement, 1);
    }

    public static void doProgress(Player player, AchievementData achievement, int amount) {
        if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
            return;
        if (achievement.progressData != null) {
            int progressIndex = achievement.progressData[0];
            int amountNeeded = achievement.progressData[1];
            int previousDone = player.getAchievementAttributes().getProgress()[progressIndex];
            if ((previousDone + amount) < amountNeeded) {
                player.getAchievementAttributes().getProgress()[progressIndex] = previousDone + amount;
                if (previousDone == 0)
                    player.getPacketSender().sendString(achievement.interfaceFrame, achievement.interfaceLine, Colours.YELLOW);
            } else {
                finishAchievement(player, achievement);
            }
        }
    }

    public static boolean hasFinishedAll(Player player, Difficulty difficulty) {
        int numberOfTasks = 0;
        int completed = 0;
        for (AchievementData achievement : AchievementData.values()) {
            if (achievement.getDifficulty() == difficulty) {
                numberOfTasks++;
                if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
                    completed++;
            }
        }
        return completed == numberOfTasks;
    }

    @SuppressWarnings("incomplete-switch")
    public static void finishAchievement(Player player, AchievementData achievement) {
        if (player.getAchievementAttributes().getCompletion()[achievement.ordinal()])
            return;
        player.getAchievementAttributes().getCompletion()[achievement.ordinal()] = true;
        player.getPacketSender().sendString(achievement.interfaceFrame, achievement.interfaceLine, Colours.GREEN);
        player.getInventory().addCoins(achievement.difficulty.reward, true);
        player.getPoints().add(Points.ACHIEVEMENT_REWARDS, achievement.difficulty.reward);
        player.message(FontUtils.imageTags(535) + " " + FontUtils.colourTags(0x339900) + "You have completed the achievement " + Misc.formatText(achievement.toString().toLowerCase() + "." + FontUtils.COL_END));
        player.getPacketSender().sendString(37001, prefixFormat + player.getPoints().getInt(Points.ACHIEVEMENT) + "/" + AchievementData.values().length);


        player.getPoints().add(Points.ACHIEVEMENT, 1);
        player.getPacketSender().sendString(34001, prefixFormat + player.getPoints().getInt(Points.ACHIEVEMENT) + "/" + AchievementData.values().length);

        if (achievement.getDifficulty() == Difficulty.ELITE) {
            switch (achievement) {
                case KILL_10000_MONSTERS:
                case DEFEAT_500_BOSSES:
                case VOTE_100_TIMES:
                    int colour;
                    String taskName = achievement.name().substring(0, 1) + achievement.name().substring(1).toLowerCase().replace("_", " ");
                    switch (achievement) {
                        case KILL_10000_MONSTERS:
                            colour = 0x118e5d;
                            break;
                        case DEFEAT_500_BOSSES:
                            colour = 0xba6215;
                            break;
                        case VOTE_100_TIMES:
                            colour = 0x0d8011;
                            break;
                        default:
                            colour = 0xfbe264;
                            break;
                    }
                    GameWorld.sendBroadcast(FontUtils.colourTags(0x0000ff) + "News:" + FontUtils.COL_END + " " + player.getUsername() + " has just completed the " + FontUtils.colourTags(colour) + taskName + FontUtils.COL_END + " achievement!");
                    break;
                default:
                    break;
            }
        }
    }

    public static class AchievementAttributes {

        /**
         * ACHIEVEMENTS
         **/
        private boolean[] completed = new boolean[AchievementData.values().length];
        private int[] progress = new int[53];

        public boolean[] getCompletion() {
            return completed;
        }

        public void setCompletion(int index, boolean value) {
            this.completed[index] = value;
        }

        public void setCompletion(boolean[] completed) {
            this.completed = completed;
        }

        public int[] getProgress() {
            return progress;
        }

        public void setProgress(int index, int value) {
            this.progress[index] = value;
        }

        public void setProgress(int[] progress) {
            this.progress = progress;
        }

        /**
         * MISC
         **/
        private double totalLoyaltyPointsEarned;
        private boolean[] godsKilled = new boolean[5];

        public double getTotalLoyaltyPointsEarned() {
            return totalLoyaltyPointsEarned;
        }

        public void incrementTotalLoyaltyPointsEarned(double totalLoyaltyPointsEarned) {
            this.totalLoyaltyPointsEarned += totalLoyaltyPointsEarned;
        }

        public boolean[] getGodsKilled() {
            return godsKilled;
        }

        public void setGodKilled(int index, boolean godKilled) {
            this.godsKilled[index] = godKilled;
        }

        public void setGodsKilled(boolean[] b) {
            this.godsKilled = b;
        }
    }
}
