package com.fury.game.entity.character.player;

import com.fury.Config;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.impl.BonusExperienceTask;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.dialogue.impl.misc.WelcomeD;
import com.fury.game.content.eco.ge.GrandExchange;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.thievingguild.ThievingGuild;
import com.fury.game.content.misc.items.random.impl.imps.MysteryBoxTimedGen;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.content.skill.member.construction.PlayerHouseLoading;
import com.fury.game.content.skill.member.construction.PlayerHouseSaving;
import com.fury.game.entity.character.combat.equipment.weapon.WeaponInterface;
import com.fury.game.entity.character.combat.magic.Autocasting;
import com.fury.game.entity.character.combat.pvp.BountyHunter;
import com.fury.game.entity.character.player.actions.ItemMorph;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.content.Music;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.game.network.packet.out.MapRegion;
import com.fury.game.network.packet.out.PlayerDetails;
import com.fury.game.network.packet.out.SystemUpdate;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.communication.MessageType;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.system.files.world.increment.timer.impl.DailyVotes;
import com.fury.game.system.mysql.impl.Highscores;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.network.PlayerSession;
import com.fury.network.SessionState;
import com.fury.network.security.ConnectionHandler;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class PlayerHandler {

    public static void handleLogin(Player player) {
        //Register the player
        System.out.println("[World " + new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "] Registering player - [" + player.getUsername() + ", " + player.getLogger().getIpAddress() + "]");
        player.register();
        ConnectionHandler.add(player.getLogger().getHardwareId());
        World.updatePlayersOnline();
        if(player.getSession().isPresent())
            player.getSession().get().setState(SessionState.LOGGED_IN);

        if (World.getUpdateTime() >= 0)
            player.send(new SystemUpdate(World.getUpdateTime()));

        //Packets
        player.send(new MapRegion());
        player.send(new PlayerDetails());

        player.getTimers().getLogin().reset();

        //Tabs
        player.getPacketSender().sendTabs();

        //Setting up the player's item containers..
        player.getInventory().refresh();
        player.getEquipment().refresh();

        //Weapons and equipment..
        Equipment.resetWeapon(player);
        if (player.getWeapon() == WeaponInterface.UNARMED)//Quick fix
            player.getCombatDefinitions().setAttackStyle(0);
        /*WeaponAnimations.update(player);
        WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
        CombatSpecial.updateBar(player);*/
        BonusManager.update(player);

        //Skills
        player.getSummoning().login();

        if (player.getFamiliar() != null)
            player.getFamiliar().respawnFamiliar(player);
        else
            player.getPetManager().init();

        player.getFarmingManager().init();

        for (Skill skill : Skill.values())
            player.getSkills().refresh(skill);

        //Resting
        player.getPacketSender().sendOrb(4, false);

        //Relations
        player.getRelations().setPrivateMessageId(1).onLogin(player).updateLists(true, true);

        //Client configurations
        player.getConfig().init();
        player.getPacketSender().sendButtonToggle(26046, player.getSettings().getBool(Settings.EXAMINE_DROP_TABLES));
        player.getPacketSender().sendButtonToggle(26050, player.getSettings().getBool(Settings.WILDERNESS_WARNINGS));
        player.getPacketSender().sendConfig(172, player.isAutoRetaliate() ? 1 : 0)
                .sendTotalXp(player.getSkills().getTotalGainedExp())
                .sendConfig(player.getFightType().getParentId(), player.getFightType().getChildId())
                .sendRunStatus().sendRunEnergy(player.getSettings().getInt(Settings.RUN_ENERGY)).sendRights()
                .sendString(8135, "" + player.getMoneyPouch().getTotal())
                .sendInterfaceRemoval().sendString(39161, "Server time: " + FontUtils.YELLOW + Misc.getCurrentServerTime() + FontUtils.COL_END, Colours.ORANGE_2);
        player.getPacketSender().sendInteractionOption("Follow", 3, false).sendInteractionOption("Trade With", 4, false);

        if(player.getEquipment().get(Slot.WEAPON).getId() == 11951)
            player.getPacketSender().sendInteractionOption("Pelt", 2, false);

        player.getPacketSender().sendButtonToggle(29329, false);
        player.getPacketSender().sendTooltip(29329, "Join Clan");

        Autocasting.onLogin(player);

        player.getPrayer().init();
        player.getPrayer().closeAllPrayers();
        Achievements.init(player);

        player.getDungManager().setPlayer(player);

        player.getControllerManager().setPlayer(player);

        House house = null;
        try {
            house = PlayerHouseLoading.loadPlayerHouse(player);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (house == null) {
            player.setHouse(new House());
            player.getHouse().setPlayer(player);
            player.getHouse().init();
        } else {
            player.setHouse(house);
            player.getHouse().setPlayer(player);
            player.getHouse().init();
        }

        PrivateObjectManager.updateLogin(player);

        //Tasks
        if (player.getMinutesBonusExp() >= 0)
            GameWorld.schedule(new BonusExperienceTask(player));

        //Update appearance

        //Others
        player.message("Welcome to " + GameSettings.NAME + (Config.TEST ? " Test world": "") + "!");
        if(Config.TEST) {
            player.message("Latest changes: Items, Mob hits, Imp jar looting");
            player.message("Testing needed:");
            player.message("Mob combat, hits, death.");
            player.message("Skill/spec restoring, multi areas, donors");
            player.message("Just looking for crashes, anything out of the ordinary.");
        }
        if (GameSettings.BONUS_EXP)
            player.getPacketSender().sendMessage("Bonus experience weekend is live!!", 0x0072bc);
        if (player.experienceLocked())
            player.getPacketSender().sendMessage(MessageType.SERVER_ALERT, FontUtils.RED + "Warning: your experience is currently locked." + FontUtils.COL_END);

        if (GameSettings.BONUS_EXP)
            player.getPacketSender().updateBonusExp();

        PlayerPanel.refreshPanel(player);

        //New player
        if (player.newPlayer() && player.getCreationTime() < 1) {
            player.getMovement().lock();
            player.setCreationTime(Misc.currentTimeMillis());
            player.setClanChatName("Help");
            Music.createList(player);
            player.getDialogueManager().startDialogue(new WelcomeD());
            player.save();
        } else {
            player.getMovement().unlock();
        }

        player.getControllerManager().login();

        player.getReferAFriend().init();

        ClanChatManager.handleLogin(player);

        if(player.hasItem(18768))
            MysteryBoxTimedGen.newTimerFor(player, new Item(18768));

        DailyVotes.get().handleLogin(player);


        if (player.getRights() == PlayerRights.SUPPORT || player.getRights() == PlayerRights.MODERATOR || player.getRights() == PlayerRights.ADMINISTRATOR)
            GameWorld.sendBroadcast(FontUtils.imageTags(535) + FontUtils.colourTags(0x008fb2) + Misc.formatText(player.getRights().toString().toLowerCase()) + " " + player.getUsername() + " has just logged in, feel free to message them for support.");

        GrandExchange.onLogin(player);

        if (player.getPoints().getInt(Points.ACHIEVEMENT) == 0)
            Achievements.setPoints(player);

        if(ThievingGuild.isInVault(player) && !ThievingGuild.isEventActive())
            player.moveTo(4764, 5793);


        player.getBank().refreshCapacity();

        //Notes
        player.getNotes().login();
        //Songs
        Music.login(player);
        //Defensive Casting
        player.getPacketSender().sendButtonToggle(11001, player.isDefensiveCasting());

        player.getDirection().setDirection(Direction.SOUTH);
        player.getUpdateFlags().add(Flag.APPEARANCE);

        player.getLogger().addLogin();
        PlayerLogs.log(player.getUsername(), "Login from host " + player.getLogger().getIpAddress() + ", serial number: " + player.getLogger().getMacAddress() + ", hwid: " + player.getLogger().getHardwareId());
    }

    public static boolean handleLogout(Player player) {
        try {
            if(!player.getSession().isPresent())
                return true;

            PlayerSession session = player.getSession().get();

            if (session.getChannel().isOpen())
                session.getChannel().close();

            if (!player.isRegistered())
                return true;

            boolean exception = World.isUpdating();
            if (player.canLogout() || exception) {
                System.out.println("[World " + new SimpleDateFormat("HH:mm:ss yyyy/MM/dd").format(Calendar.getInstance().getTime()) + "] Deregistering player - [" + player.getUsername() + ", " + player.getLogger().getIpAddress() + "]");
                ConnectionHandler.remove(player.getLogger().getHardwareId());
                session.setState(SessionState.LOGGING_OUT);
                player.setTotalPlayTime(player.getTotalPlayTime() + player.getTimers().getLogin().elapsed());
                player.getPacketSender().sendInterfaceRemoval();
                if (exception && player.getResetPosition() != null) {
                    player.moveTo(player.getResetPosition());
                    player.setResetPosition(null);
                }

                PlayerHouseSaving.saveHouse(player);
                player.getControllerManager().logout();
                if(player.getHouse() != null)
                    player.getHouse().finish();

                if (player.getFamiliar() != null && !player.getFamiliar().getFinished())
                    player.getFamiliar().dismissFamiliar(true);
                else if (player.getPet() != null)
                    player.getPet().deregister();

                if(player.getActionManager().getAction() instanceof ItemMorph)
                    player.getActionManager().forceStop();

                Highscores.save(player);
                BountyHunter.handleLogout(player);
                ClanChatManager.leave(player, false);
                player.getRelations().updateLists(false);
                player.getDungManager().finish();
                MysteryBoxTimedGen.removeTimerFor(player);
//                GameWorld.getPlayers().remove(player);
//                World.removePlayer(player);
                session.setState(SessionState.LOGGED_OUT);
                World.updatePlayersOnline();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
