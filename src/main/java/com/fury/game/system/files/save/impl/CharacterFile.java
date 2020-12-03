package com.fury.game.system.files.save.impl;

import com.fury.game.content.misc.objects.DwarfMultiCannon;
import com.fury.game.content.eco.ge.GrandExchangeSlot;
import com.fury.game.content.eco.ge.GrandExchangeSlotState;
import com.fury.game.content.global.treasuretrails.ClueConstants;
import com.fury.game.content.global.treasuretrails.ClueScroll;
import com.fury.game.content.global.treasuretrails.ClueTypes;
import com.fury.game.content.global.treasuretrails.TreasureTrails;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.SkillData;
import com.fury.game.node.entity.actor.figure.player.misc.redo.DropLog;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.node.entity.actor.figure.player.misc.redo.KillsTracker;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.content.combat.magic.MagicSpellBook;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.game.entity.character.player.info.PlayerRelations;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.mob.drops.DropLogEntry;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.figure.player.Variables;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.save.StorageFile;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Gender;
import com.fury.network.login.LoginUtils;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CharacterFile extends StorageFile {
    @Override
    public String getDirectory(Player player) {
        return Resources.getSaveDirectory("characters") + player.getUsername() + ".json";
    }

    @Override
    public JsonObject save(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("creation-time-ms", player.getCreationTime());
        object.addProperty("total-play-time-ms", player.getTotalPlayTime());
        object.addProperty("username", player.getUsername().trim());
        object.addProperty("password", player.getPasswordHash());
        object.addProperty("staff-rights", player.getRights().name());
        object.addProperty("game-mode", player.getGameMode().name());
        if(player.newPlayer())
            object.addProperty("new-player", true);
        object.addProperty("loyalty-title", player.getLoyaltyTitle().name());
        object.add("position", builder.toJsonTree(player.copyPosition()));
        object.addProperty("online-status", player.getRelations().getStatus().name());
        object.addProperty("given-starter", new Boolean(player.didReceiveStarter()));
        object.addProperty("money-pouch", player.getMoneyPouch().getTotal());
        object.addProperty("minutes-bonus-exp", new Integer(player.getMinutesBonusExp()));
        object.addProperty("minutes-bonus-mining", new Integer(player.getMinutesBonusMining()));
        object.addProperty("total-gained-exp", new Long(player.getSkills().getTotalGainedExp()));
        object.addProperty("total-loyalty-points", new Double(player.getAchievementAttributes().getTotalLoyaltyPointsEarned()));
        object.addProperty("gender", player.getAppearance().getGender().name());
        object.addProperty("spell-book", player.getSpellbook().name());
        object.addProperty("effigy", new Integer(player.getEffigy()));
        object.addProperty("christmas-2017", new Integer(player.getChristmasEventStage()));
        object.add("christmas-2017-chars", builder.toJsonTree(player.getChristmasCharacters()));
        object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim());

        if(player.getControllerManager().getLastController() != null) {
            object.addProperty("last-controller", player.getControllerManager().getLastController());
            object.add("last-arguments", builder.toJsonTree(player.getControllerManager().getLastControllerArguments()));
        }

        if(!player.getDecayedCannons().isEmpty())
            object.add("decayed-cannons", builder.toJsonTree(player.getDecayedCannons().toArray(new DwarfMultiCannon.CannonType[player.getDecayedCannons().size()])));

        object.add("statue", builder.toJsonTree(player.getStatue().getSkills()));
        object.add("points", builder.toJsonTree(player.getPoints().getAll()));
        object.add("vars", builder.toJsonTree(player.getVars().getAll()));
        object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers()));
        object.add("killed-gods", builder.toJsonTree(player.getAchievementAttributes().getGodsKilled()));
        object.add("barrows-brother", builder.toJsonTree(player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()));
        object.addProperty("random-coffin", new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()));
        object.addProperty("barrows-killcount", new Integer(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount()));
        object.add("recipe-for-disaster", builder.toJsonTree(player.getMinigameAttributes().getRecipeForDisasterAttributes().getQuestParts()));
        object.addProperty("recipe-for-disaster-wave", new Integer(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted()));
        object.addProperty("clue-progress", new Integer(player.getClueProgress()));
        object.addProperty("jar-charge", new Integer(player.getJarGeneratorCharge()));
        object.addProperty("has-bank-pin", new Boolean(player.getBankPinAttributes().hasBankPin()));
        object.addProperty("last-pin-attempt", new Long(player.getBankPinAttributes().getLastAttempt()));
        object.addProperty("invalid-pin-attempts", new Integer(player.getBankPinAttributes().getInvalidAttempts()));
        object.addProperty("converted-skills", player.getConverted());
        object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin()));
        object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));
        object.add("agility-obj", builder.toJsonTree(player.getCrossedObstacles()));
        object.add("skills", builder.toJsonTree(player.getSkills().getData()));
        object.add("inventory", itemsToArray(player.getInventory().getItems(), true));
        object.add("equipment", itemsToArray(player.getEquipment().getItems(), true));
        object.add("herb-pouch", itemsToArray(player.getHerbPouch().getItems(), true));
        object.add("quests", player.getQuestManager().getQuestsJson());

        //JsonArray bank = new JsonArray();
        //for(int i = 0; i < player.getBank().getTabCount(); i++)
        //    bank.add(itemsToArray(player.getBank().tab(i).getItems(), false));
        //object.add("bank", bank);

        //for(int i = 0; i < 9; i++)
        //    object.add("bank-" + i, itemsToArray(player.getBank(i).getValidItems(), true));

        object.add("ge-slots", builder.toJsonTree(player.getGrandExchangeSlots()));

        /** STORE SUMMON **/

        object.add("charm-imp", builder.toJsonTree(player.getSummoning().getCharmImpConfigs()));

        object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
        object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));
        object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles()));
        object.add("bought-loyalty-titles", builder.toJsonTree(player.getBoughtLoyaltyTitles()));
        object.add("kills", builder.toJsonTree(player.getKillsTracker().getKills().toArray()));
        object.add("drops", builder.toJsonTree(player.getDropLogs().getDrops().toArray()));
        object.add("achievements-completion", builder.toJsonTree(player.getAchievementAttributes().getCompletion()));
        object.add("achievements-progress", builder.toJsonTree(player.getAchievementAttributes().getProgress()));

        object.add("cape-colour-presets", builder.toJsonTree(player.getColourPresets()));
        object.add("cape-recolours", builder.toJsonTree(player.getCapeRecolours()));
        object.add("cape-rgb-colours", builder.toJsonTree(player.getRgbColours()));
        object.add("private-objects", builder.toJsonTree(player.getOwnedObjectManagerKeys()));

        JsonObject clues = new JsonObject();
        ClueScroll[] clueData = player.getClueScrolls();
        String[] names = new String[] {"easy", "medium", "hard", "elite"};
        for(int i = 0; i < 4; i++) {
            JsonObject clue = new JsonObject();
            if(clueData[i] != null) {
                clue.addProperty("type", clueData[i].getType().name());
                clue.addProperty("index", clueData[i].getIndexName());
                clue.addProperty("remaining", clueData[i].getRemainingClues());
                if(clueData[i].getType() == ClueTypes.EMOTE)
                    if(ClueConstants.Emotes.values()[clueData[i].getIndex()].isDoubleAgent())
                        clue.addProperty("agent-dead", clueData[i].isDoubleAgentDead());

                clues.add(names[i], clue);
            } else {
                clues.add(names[i], null);
            }
        }
        object.add("clue-scrolls", clues);
        return object;
    }

    @Override
    public void load(Player player, JsonObject reader) {
        if (reader.has("points")) {
            JsonObject object = reader.get("points").getAsJsonObject();
            for(Points point : Points.values())
                if(object.has(point.name()))
                    player.getPoints().set(point, object.get(point.name()).getAsDouble());
        }

        if (reader.has("vars")) {
            JsonObject object = reader.get("vars").getAsJsonObject();
            for(Variables var : Variables.values())
                if(object.has(var.name()))
                    player.getVars().set(var, builder.fromJson(object.get(var.name()), Object.class));
        }

        if (reader.has("creation-time-ms"))
            player.setCreationTime(reader.get("creation-time-ms").getAsLong());

        if (reader.has("total-play-time-ms"))
            player.setTotalPlayTime(reader.get("total-play-time-ms").getAsLong());

        if (reader.has("username"))
            player.setUsername(reader.get("username").getAsString());

        if (reader.has("password")) {
            String passwordHash = reader.get("password").getAsString();
            player.setPasswordHash(passwordHash);
        }

        if (reader.has("staff-rights")) {
            String rights = reader.get("staff-rights").getAsString();
            player.setRights(PlayerRights.valueOf(rights));
        }

        if (reader.has("game-mode")) {
            GameMode gm = GameMode.REGULAR;
            try {
                gm = GameMode.valueOf(reader.get("game-mode").getAsString());
            } catch (IllegalArgumentException e) {
                //e.printStackTrace();
            }
            player.setGameMode(gm);
        }

        if (reader.has("jail-time"))//Converter
            player.getVars().set(Variables.JAIL_TIME, reader.get("jail-time").getAsLong());

        if (reader.has("new-player"))
            player.setNewPlayer(true);

        if (reader.has("loyalty-title")) {
            LoyaltyProgramme.LoyaltyTitles title = LoyaltyProgramme.LoyaltyTitles.valueOf(reader.get("loyalty-title").getAsString());
            player.setLoyaltyTitle(title);
        }

        if (reader.has("position"))
            player.setPosition(builder.fromJson(reader.get("position"), Position.class));

        if (reader.has("online-status"))
            player.getRelations().setStatus(PlayerRelations.PrivateChatStatus.valueOf(reader.get("online-status").getAsString()), false);

        if (reader.has("money-pouch"))
            player.getMoneyPouch().setTotal(reader.get("money-pouch").getAsLong());

        if (reader.has("given-starter"))
            player.setReceivedStarter(reader.get("given-starter").getAsBoolean());

        if (reader.has("donated"))
            player.getPoints().set(Points.DONATED, reader.get("donated").getAsInt());

        if (reader.has("donor-points"))
            player.getPoints().set(Points.DONOR, reader.get("donor-points").getAsInt());

        if (reader.has("minutes-bonus-exp"))
            player.setMinutesBonusExp(reader.get("minutes-bonus-exp").getAsInt(), false);

        if (reader.has("minutes-bonus-mining"))
            player.setMinutesBonusMining(reader.get("minutes-bonus-mining").getAsInt(), false);

        if (reader.has("total-gained-exp"))
            player.getSkills().setTotalGainedExp(reader.get("total-gained-exp").getAsInt());

        if (reader.has("member-points"))
            player.getPoints().set(Points.MEMBER, reader.get("member-points").getAsInt());

        if (reader.has("achievement-points"))
            player.getPoints().set(Points.ACHIEVEMENT, reader.get("achievement-points").getAsInt());

        if (reader.has("achievement-rewards"))
            player.getPoints().set(Points.ACHIEVEMENT_REWARDS, reader.get("achievement-rewards").getAsInt());

        if (reader.has("commendations"))
            player.getPoints().set(Points.COMMENDATIONS, reader.get("commendations").getAsInt());

        if (reader.has("loyalty-points"))
            player.getPoints().set(Points.LOYALTY, reader.get("loyalty-points").getAsInt());

        if (reader.has("total-loyalty-points"))
            player.getAchievementAttributes().incrementTotalLoyaltyPointsEarned(reader.get("total-loyalty-points").getAsDouble());

        if (reader.has("voting-points"))
            player.getPoints().set(Points.VOTING, reader.get("voting-points").getAsInt());

        if (reader.has("slayer-points"))
            player.getPoints().set(Points.SLAYER, reader.get("slayer-points").getAsInt());

        if (reader.has("favour-points"))
            player.getPoints().set(Points.FAVOUR, reader.get("favour-points").getAsInt());

        if (reader.has("pk-points"))
            player.getPoints().set(Points.PK, reader.get("pk-points").getAsInt());

        if (reader.has("gender"))
            player.getAppearance().setGender(Gender.valueOf(reader.get("gender").getAsString()));

        if (reader.has("spell-book"))
            player.setSpellBook(MagicSpellBook.valueOf(reader.get("spell-book").getAsString()), true);

        if (reader.has("statue"))
            player.getStatue().setSkills(builder.fromJson(reader.get("statue"), Skill[].class));

        if (reader.has("last-controller"))
            player.getControllerManager().setLastController(reader.get("last-controller").getAsString());

        if (reader.has("last-arguments"))
            player.getControllerManager().setLastControllerArguments(builder.fromJson(reader.get("last-arguments"), Object[].class));

        if (reader.has("claim-esse"))//Converter
            player.getVars().set(Variables.CROMPERTY_ESSENCE_CLAIMED, reader.get("claim-esse").getAsLong());

        if (reader.has("claim-recharge"))//Converter
            player.getVars().set(Variables.ARDOUGNE_CLOAK_RECHARGE, reader.get("claim-recharge").getAsLong());

        if (reader.has("effigy"))
            player.setEffigy(reader.get("effigy").getAsInt());

        if (reader.has("christmas-2017"))
            player.setChristmasEventStage(reader.get("christmas-2017").getAsInt());

        if (reader.has("christmas-2017-chars"))
            player.setChristmasCharacters(builder.fromJson(reader.get("christmas-2017-chars").getAsJsonArray(), boolean[].class));

        if (reader.has("clanchat")) {
            String clan = reader.get("clanchat").getAsString();
            if (!clan.equals("null"))
                player.setClanChatName(clan);
        }
        if (reader.has("kills"))
            KillsTracker.submit(player, builder.fromJson(reader.get("kills").getAsJsonArray(), KillsTracker.KillsEntry[].class));

        if (reader.has("drops"))
            DropLog.submit(player, builder.fromJson(reader.get("drops").getAsJsonArray(), DropLogEntry[].class));

        if(reader.has("decayed-cannons")) {
            DwarfMultiCannon.CannonType[] types = builder.fromJson(reader.get("decayed-cannons").getAsJsonArray(), DwarfMultiCannon.CannonType[].class);
            for(DwarfMultiCannon.CannonType type : types)
                player.getDecayedCannons().add(type);
        }

        if (reader.has("killed-players")) {
            List<String> list = new ArrayList<>();
            String[] killed_players = builder.fromJson(reader.get("killed-players").getAsJsonArray(), String[].class);
            for (String s : killed_players)
                list.add(s);
            player.getPlayerKillingAttributes().setKilledPlayers(list);
        }

        if (reader.has("killed-gods"))
            player.getAchievementAttributes().setGodsKilled(builder.fromJson(reader.get("killed-gods").getAsJsonArray(), boolean[].class));

        if (reader.has("barrows-brother"))
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setBarrowsData(builder.fromJson(reader.get("barrows-brother").getAsJsonArray(), int[][].class));

        if (reader.has("random-coffin"))
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin((reader.get("random-coffin").getAsInt()));

        if (reader.has("barrows-killcount"))
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount((reader.get("barrows-killcount").getAsInt()));

        if (reader.has("recipe-for-disaster")) {
            player.getMinigameAttributes().getRecipeForDisasterAttributes().setQuestParts(builder.fromJson(reader.get("recipe-for-disaster").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("recipe-for-disaster-wave"))
            player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted((reader.get("recipe-for-disaster-wave").getAsInt()));

        if (reader.has("clue-progress"))
            player.setClueProgress((reader.get("clue-progress").getAsInt()));

        if (reader.has("jar-charge"))
            player.setJarGeneratorCharge((reader.get("jar-charge").getAsInt()));

        if (reader.has("bank-pin"))
            player.getBankPinAttributes().setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));

        if (reader.has("has-bank-pin"))
            player.getBankPinAttributes().setHasBankPin(reader.get("has-bank-pin").getAsBoolean());
        if (reader.has("last-pin-attempt"))
            player.getBankPinAttributes().setLastAttempt(reader.get("last-pin-attempt").getAsLong());
        if (reader.has("invalid-pin-attempts"))
            player.getBankPinAttributes().setInvalidAttempts(reader.get("invalid-pin-attempts").getAsInt());

        if (reader.has("bank-pin"))
            player.getBankPinAttributes().setBankPin(builder.fromJson(reader.get("bank-pin").getAsJsonArray(), int[].class));

        if (reader.has("appearance")) {
            player.getAppearance().set(builder.fromJson(reader.get("appearance").getAsJsonArray(), int[].class));
        }

        if (reader.has("agility-obj")) {
            player.setCrossedObstacles(builder.fromJson(reader.get("agility-obj").getAsJsonArray(), boolean[].class));
        }

        if (reader.has("converted-skills"))
            player.setConverted(reader.get("converted-skills").getAsBoolean());

        if (reader.has("skills")) {
            player.getSkills().setData(builder.fromJson(reader.get("skills"), SkillData.class));

            if(!player.getConverted()) {
                int level = player.getSkills().getData().getLevel(Skill.HUNTER);
                int max = player.getSkills().getData().getMaxLevel(Skill.HUNTER);
                double exp = player.getSkills().getData().getExperience(Skill.HUNTER);

                player.getSkills().getData().setLevel(Skill.HUNTER, player.getSkills().getData().getLevel(Skill.CONSTRUCTION));
                player.getSkills().getData().setMaxLevel(Skill.HUNTER, player.getSkills().getData().getMaxLevel(Skill.CONSTRUCTION));
                player.getSkills().getData().setExperience(Skill.HUNTER, player.getSkills().getData().getExperience(Skill.CONSTRUCTION));

                player.getSkills().getData().setLevel(Skill.CONSTRUCTION, level);
                player.getSkills().getData().setMaxLevel(Skill.CONSTRUCTION, max);
                player.getSkills().getData().setExperience(Skill.CONSTRUCTION, exp);

                player.setConverted(true);
            }
        }

        if (reader.has("inventory"))
            player.getInventory().setItems(LoginUtils.getItemContainer(reader.get("inventory").getAsJsonArray(), true));

        if (reader.has("equipment"))
            player.getEquipment().setItems(LoginUtils.getItemContainer(reader.get("equipment").getAsJsonArray(), true));

        if (reader.has("herb-pouch"))
            player.getHerbPouch().setItems(LoginUtils.getItemContainer(reader.get("herb-pouch").getAsJsonArray(), true));

        if(reader.has("quests"))
            player.getQuestManager().setQuests(reader.getAsJsonArray("quests"));

        if (reader.has("ge-slots")) {
            GrandExchangeSlot[] slots = builder.fromJson(reader.get("ge-slots").getAsJsonArray(), GrandExchangeSlot[].class);
            for (GrandExchangeSlot slot : slots)
                if (slot.getOffer() != null && slot.getOffer().getId() == -1) {
                    slot.setOffer(null);
                    slot.setState(GrandExchangeSlotState.EMPTY);
                }
            player.setGrandExchangeSlots(slots);
        }

        if (reader.has("store")) {
            Item[] validStoredItems = LoginUtils.getItemContainer(reader.get("store").getAsJsonArray());
            if (player.getSummoning().getSpawnTask() != null) {
                player.getSummoning().getSpawnTask().setBurdenItems(validStoredItems);
            }
        }

        if (reader.has("charm-imp")) {
            int[] charmImpConfig = builder.fromJson(reader.get("charm-imp").getAsJsonArray(), int[].class);
            player.getSummoning().setCharmImpConfig(charmImpConfig);
        }

        if (reader.has("friends")) {
            long[] friends = builder.fromJson(
                    reader.get("friends").getAsJsonArray(), long[].class);

            for (long l : friends) {
                player.getRelations().getFriendList().add(l);
            }
        }
        if (reader.has("ignores")) {
            long[] ignores = builder.fromJson(
                    reader.get("ignores").getAsJsonArray(), long[].class);

            for (long l : ignores) {
                player.getRelations().getIgnoreList().add(l);
            }
        }

        if (reader.has("loyalty-titles"))
            player.setUnlockedLoyaltyTitles(builder.fromJson(reader.get("loyalty-titles").getAsJsonArray(), boolean[].class));

        if (reader.has("bought-loyalty-titles")) {
            player.setBoughtLoyaltyTitles(builder.fromJson(reader.get("bought-loyalty-titles").getAsJsonArray(), boolean[].class));
            if (!player.getBoughtLoyaltyTitles()[player.getLoyaltyTitle().getId()])
                player.setBoughtLoyaltyTitle(player.getLoyaltyTitle().getId());
        }
        if (reader.has("achievements-completion"))
            player.getAchievementAttributes().setCompletion(builder.fromJson(reader.get("achievements-completion").getAsJsonArray(), boolean[].class));

        if (reader.has("achievements-progress"))
            player.getAchievementAttributes().setProgress(builder.fromJson(reader.get("achievements-progress").getAsJsonArray(), int[].class));

        if (reader.has("cape-colour-presets"))
            player.setColourPresets(builder.fromJson(reader.get("cape-colour-presets").getAsJsonArray(), int[][].class));

        if (reader.has("cape-recolours"))
            player.setCapeRecolours(builder.fromJson(reader.get("cape-recolours").getAsJsonArray(), int[].class));

        if (reader.has("cape-rgb-colours"))
            player.setRgbColours(builder.fromJson(reader.get("cape-rgb-colours").getAsJsonArray(), int[].class));

        if (reader.has("private-objects"))
            player.setOwnedObjectManagerKeys(builder.fromJson(reader.get("private-objects").getAsJsonArray(), String[].class));

        if (reader.has("clue-scrolls")) {
            JsonObject clues = reader.get("clue-scrolls").getAsJsonObject();
            ClueScroll[] clueData = new ClueScroll[4];
            String[] names = new String[]{"easy", "medium", "hard", "elite"};
            for (int i = 0; i < 4; i++) {
                try {
                    if (clues.has(names[i])) {
                        JsonObject clue = clues.get(names[i]).getAsJsonObject();
                        clueData[i] = new ClueScroll();
                        if (clue.has("type"))
                            clueData[i].setType(ClueTypes.get(clue.get("type").getAsString()));
                        if (clue.has("index"))
                            clueData[i].setIndex(TreasureTrails.getIndexUsingType(clueData[i].getType(), clue.get("index").getAsString()));
                        if (clue.has("remaining"))
                            clueData[i].setRemainingClues(clue.get("remaining").getAsInt());
                        if (clue.has("agent-dead") && clueData[i].getType() == ClueTypes.EMOTE)
                            if (ClueConstants.Emotes.values()[clueData[i].getIndex()].isDoubleAgent())
                                clueData[i].setDoubleAgentDead(clue.get("agent-dead").getAsBoolean());
                    }
                } catch (Throwable e) {
                    clueData[i] = null;
                }
            }
            player.setClueScrolls(clueData);
        }
    }

    @Override
    public void setDefaults(Player player) {
    }
}
