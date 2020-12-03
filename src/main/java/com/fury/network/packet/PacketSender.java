package com.fury.network.packet;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.container.Container;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.Music;
import com.fury.game.entity.character.player.content.PlayerInteractingOption;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.system.communication.MessageType;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Area;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.DynamicRegion;
import com.fury.game.world.map.instance.Palette;
import com.fury.game.world.map.instance.Palette.PaletteTile;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class manages making the packets that will be sent (when called upon) onto
 * the associated player's client.
 *
 * @author relex lawl, Gabbe & Greg
 */

public class PacketSender {

    public PacketSender sendCompCapeInterfaceColours() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(132);
        for (int i = 0; i < 7; i++) {
            out.putInt(player.getRgbColours()[i]);
        }
        for (int p = 0; p < 3; p++) {
            for (int i = 0; i < 7; i++) {
                out.putInt(player.getColourPresets()[p][i]);
            }
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendFogToggle(int colour) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(92);
        //0 normal
        //1 black
        //2 white
        out.put(colour);
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends player song info to the client.
     */
    public PacketSender sendSongList(List<Boolean> list) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(91);
        out.putShort(Music.interfaceStartId);
        out.putShort(list.size());
        for (int i = 0; i < list.size(); i++)
            out.put(list.get(i) ? 1 : 0);
        out.putShort(0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendSongList(List<Boolean> list, ArrayList<Integer> playlist) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(91);
        out.putShort(Music.interfaceStartId);
        out.putShort(list.size());
        for (int i = 0; i < list.size(); i++)
            out.put(list.get(i) ? 1 : 0);
        out.putShort(playlist.size());
        for (int i = 0; i < playlist.size(); i++)
            out.putShort(playlist.get(i));
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendSound(int soundId, int volume, int delay) {
        if (!player.getSession().isPresent())
            return this;

        if (soundId != -1) {
            PacketBuilder out = new PacketBuilder(174);
            out.putShort(soundId).put(volume).putShort(delay);
            player.getSession().get().write(out);
        }
        return this;
    }

    public PacketSender sendSong(int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(74);
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendAutocastId(int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(38);
        out.putShort(id);
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends skill information onto the client, to calculate things such as
     * constitution, prayer and summoning orb and other configurations.
     *
     * @param skill The skill being sent.
     * @return The PacketSender instance.
     */
    public PacketSender sendSkill(Skill skill, int exp) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(134);
        out.put(skill.ordinal());
        out.putInt(exp, ByteOrder.MIDDLE);
        out.putShort(player.getSkills().getLevel(skill));
        out.putShort(player.getSkills().getMaxLevel(skill));
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends a configuration button's state.
     *
     * @param id    The id of the configuration button.
     * @param state The state to set it to.
     * @return The PacketSender instance.
     */
    public PacketSender sendConfig(int id, int state) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(36);
        out.putShort(id, ByteOrder.LITTLE);
        out.put(state);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendConfigByFile(int file, int state) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(37);
        out.putShort(file, ByteOrder.LITTLE);
        out.put(state);
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends the state in which the player has their chat options, such as public, private, friends only.
     *
     * @param publicChat  The state of their public chat.
     * @param privateChat The state of their private chat.
     * @param tradeChat   The state of their trade chat.
     * @return The PacketSender instance.
     */
    public PacketSender sendChatOptions(int publicChat, int privateChat, int tradeChat) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(206);
        out.put(publicChat).put(privateChat).put(tradeChat);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendRunEnergy(int energy) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(110);
        out.put(energy);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendSummoningPouch(int pouchId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(108);
        out.putShort(pouchId);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender updateBonusExp() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(111);
        out.put(GameSettings.BONUS_EXP ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendOrb(int orb, boolean active) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(116);
        out.putShort(orb);
        out.put(active ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendSummoningInfo(boolean active, int special) {
        sendOrb(3, active);
        sendSummoningPouch(special);
        return this;
    }

    public PacketSender sendDungeoneeringTabIcon(boolean show) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(103);
        out.put(show ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendHeight() {
        if (player.getSession().isPresent())
            player.getSession().get().write(new PacketBuilder(86).put(player.getZ()));
        return this;
    }

    public PacketSender sendClanChatStatus(boolean active) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(113);
        out.put(active ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendClanChatListOptionsVisible(int config) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(115);
        out.put(config); //0 = no right click options, 1 = Kick only, 2 = demote/promote & kick
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendButtonToggle(int interfaceId, boolean state) {//TODO use sendConfig
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(118);
        out.putShort(interfaceId);
        out.put(state ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendLoyaltyInfo(int interfaceId, int command, boolean toggle) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(119);
        out.putShort(interfaceId);
        out.putShort(command);
        out.put(toggle ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendLoyaltyItem(int interfaceId, int itemId, Revision revision) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(131);
        out.putShort(interfaceId);
        out.putInt(itemId);
        out.put(revision.ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendRunStatus() {
        sendOrb(2, player.getSettings().getBool(Settings.RUNNING));
        return this;
    }

    public PacketSender sendWeight(int weight) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(240);
        out.putShort(weight);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender commandFrame(int i) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(28);
        out.put(i);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterface(int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(97);
        out.putInt(id);
        player.getSession().get().write(out);
        player.setInterfaceId(id);
        return this;
    }

    public PacketSender sendInterfaceSpriteUpdate(int interfaceId, int spriteID) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(182);
        out.putShort(interfaceId);
        out.putShort(spriteID);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceDisplayState(int interfaceId, boolean hide) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(171);
        out.put(hide ? 1 : 0);
        out.putShort(interfaceId);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendPlayerHeadOnInterface(int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(185);
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendNpcHeadOnInterface(int id, Revision revision, int interfaceId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(75);
        out.putShort(id, ValueType.A, ByteOrder.LITTLE);
        out.putShort(interfaceId, ValueType.A, ByteOrder.LITTLE);
        out.put(revision.ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendEnterAmountPrompt(String title) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(27);
        out.putString(title);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendEnterInputPrompt(String title) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(187);
        out.putString(title);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceReset() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(68);
        player.getSession().get().write(out);
        return this;
    }

    public void sendCSVarInteger(int id, int value) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
            sendInterfaceComponentScrollPosition(id, value);
        else
            sendInterfaceComponentScrollMax(id, value);
    }

    public PacketSender sendInterfaceComponentScrollPosition(int interfaceId, int scrollPosition) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(78);
        out.putShort(interfaceId);
        out.putShort(scrollPosition);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceComponentScrollMax(int interfaceId, int scrollMax) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(77);
        out.putInt(interfaceId);
        out.putShort(scrollMax);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceComponentPosition(int parent, int child, int x, int y) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(76);
        out.putShort(parent);
        out.putShort(child);
        out.putShort(x);
        out.putShort(y);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceComponentOffset(int x, int y, int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(70);
        out.putShort(x);
        out.putShort(y);
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().get().write(out);
        return this;
    }

    /*public PacketSender sendBlinkingHint(String title, String information, int worldX, int worldY, int speed, int pause, int type, final int time) {
        if (!player.getSession().isPresent())
            return this;
        player.getSession().get().write(new PacketBuilder(179, PacketType.SHORT).putString(title).putString(information).putShort(worldX).putShort(worldY).put(speed).put(pause).put(type));
        if(type > 0) {
            TaskManager.submit(new Task(time, player, false) {
                @Override
                public void execute() {
                    player.getPacketSender().sendBlinkingHint("", "", 0, 0, 0, 0, -1, 0);
                    stop();
                }
            });
        }
        return this;
    }
     */
    public PacketSender sendInterfaceAnimation(int interfaceId, int animationId, Revision revision) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(200);
        out.putShort(interfaceId);
        out.putShort(animationId);
        out.put(revision.ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceModel(int interfaceId, int itemId, Revision revision, int zoom) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(246);
        out.putShort(interfaceId, ByteOrder.LITTLE);
        out.putShort(zoom).putInt(itemId);
        out.put(revision.ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendTabInterface(int tabId, int interfaceId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(71);
        out.putShort(interfaceId);
        out.put(tabId, ValueType.A);
        player.getSession().get().write(out);
        player.setTabInterface(tabId, interfaceId);
        return this;
    }

    public PacketSender sendTabs() {
        sendTabInterface(GameSettings.ATTACK_TAB, 2423);
        sendTabInterface(GameSettings.SKILLS_TAB, 3917);//31110);
        sendTabInterface(GameSettings.QUESTS_TAB, 639);
        player.getPacketSender().sendDungeoneeringTabIcon(false);
        sendTabInterface(GameSettings.ACHIEVEMENT_TAB, 37000);
        sendTabInterface(GameSettings.INVENTORY_TAB, 3213);
        sendTabInterface(GameSettings.EQUIPMENT_TAB, 15000);
        sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
        sendTabInterface(GameSettings.PRAYER_TAB, player.getPrayerbook().getInterfaceId());
        //Row 2
        sendTabInterface(GameSettings.FRIEND_TAB, 5065);
        sendTabInterface(GameSettings.IGNORE_TAB, 5715);
        sendTabInterface(GameSettings.CLAN_CHAT_TAB, 29328);
        sendTabInterface(GameSettings.LOGOUT, 2449);
        sendTabInterface(GameSettings.OPTIONS_TAB, 904);//19261
        sendTabInterface(GameSettings.EMOTES_TAB, 147);
        sendTabInterface(GameSettings.SUMMONING_TAB, -1);//54017
        sendTabInterface(GameSettings.MUSIC_TAB, Music.INTERFACE_ID);
        sendTabInterface(GameSettings.NOTES_TAB, 55200);
        return this;
    }

    public PacketSender sendTab(int id) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(106);
        out.put(id, ValueType.C);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendChatboxInterface(int id) {
        if (!player.getSession().isPresent())
            return this;

        if (player.getInterfaceId() <= 0)
            player.setInterfaceId(55);
        PacketBuilder out = new PacketBuilder(164);
        player.setDialogueId(id);
        out.putShort(id, ByteOrder.LITTLE);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendMapState(int state) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(99);
        out.put(state);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendCameraAngle(int x, int y, int level, int speed, int angle) {
        if (!player.getSession().isPresent())
            return this;

        //sendPosition(new Position(x, y, level));
        PacketBuilder out = new PacketBuilder(177);
        out.put(x / 64);
        out.put(y / 64);
        out.putShort(level);
        out.put(speed);
        out.put(angle);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendCameraShake(int verticalAmount, int verticalSpeed, int horizontalAmount, int horizontalSpeed) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(35);
        out.put(verticalAmount);
        out.put(verticalSpeed);
        out.put(horizontalAmount);
        out.put(horizontalSpeed);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendCameraSpin(int x, int y, int z, int speed, int angle) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(new Position(x, y, z));
        PacketBuilder out = new PacketBuilder(166);
        out.put(x / 64);
        out.put(y / 64);
        out.putShort(z);
        out.put(speed);
        out.put(angle);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendGrandExchangeUpdate(String s) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(244);
        out.putString(s);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendCameraNeutrality() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(107);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceRemoval() {
        if (player.isCommandViewing()) {
            player.setCommandViewing(false);
            player.getRightsData().previewBank = null;
            player.getRightsData().previewInventory = null;
            player.getInventory().refresh();
        }
        if (player.isBanking()) {
            sendClientRightClickRemoval();
            player.setBanking(false);
            if (player.isCommandBanking())
                player.setCommandBanking(false);
        }
        if (player.isShopping()) {
            sendClientRightClickRemoval().sendWidgetItems(Shop.INTERFACE_ID, new Item[]{new Item(-1)});
            player.setShopping(false);
        }
        if (player.getPriceChecker().isOpen())
            player.getPriceChecker().exit();

        if (player.getTrade().inTrade()) {
            sendClientRightClickRemoval();
            player.getTrade().declineTrade(true);
        }

        if (player.getSettings().isResting())
            player.getSettings().setRestingState(0);

        if (player.isSitting())
            player.setSitting(false);

        //player.settingPreloadName = false;
        /*
		if(player.getMinigameAttributes().getFishingTrawlerAttributes().isViewingInterface()) {
			sendClientRightClickRemoval().sendItemsOnInterface(Shop.INTERFACE_ID, new Item[]{new Item(-1)});
			player.getMinigameAttributes().getFishingTrawlerAttributes().setViewingInterface(false).getRewards().clear();
		}
		if(player.getAdvancedSkills().getSummoning().isStoring()) {
			sendClientRightClickRemoval();
			player.getAdvancedSkills().getSummoning().setStoring(false);
		}
		if(player.isPriceChecking()) {
			sendClientRightClickRemoval();
			PriceChecker.closePriceChecker(player);
		}
		if(player.getBankSearchingAttribtues().isSearchingBank())
			BankSearchAttributes.stopSearch(player, false);
		if(player.isBanking()) {
			sendClientRightClickRemoval();
			player.setBanking(false);
		}*/
        if(player.getQuestManager().hasStarted(Quests.FIRST_ADVENTURE) && player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE).getStage() != 1 && player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE).getStage() != 2) {
            player.getDialogueManager().finishDialogue();
            player.setDialogueActionId(-1);
        }
        player.setInterfaceId(-1);
        player.setDialogueId(-1);

        if (player.getCloseInterfacesEvent() != null) {
            player.getCloseInterfacesEvent().run();
            player.setCloseInterfacesEvent(null);
        }

        player.getAppearance().setCanChangeAppearance(false);

        if (player.getSession().isPresent())
            player.getSession().get().write(new PacketBuilder(219));
        return this;
    }

    public PacketSender sendInterfaceSet(int interfaceId, int sidebarInterfaceId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(248);
        out.putShort(interfaceId, ValueType.A);
        out.putShort(sidebarInterfaceId);
        player.getSession().get().write(out);
        player.setInterfaceId(interfaceId);
        return this;
    }

    public PacketSender sendItemContainer(Item[] items, int interfaceId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(interfaceId);
        out.putShort(items.length);
        for (Item item : items) {
            if (item == null) {
                out.put(0);
            } else {
                out.put(1);
                out.putInt(item.getAmount());
                out.putInt(item.getId() + 1);
                if (item.getRevision() == null)//TODO remove
                    out.put(0);
                else
                    out.put(item.getRevision().ordinal());
            }
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemContainer(Container container, int interfaceId) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(interfaceId);
        out.putShort(container.getItems().length);
        for (Item item : container.getItems()) {
            if (item == null) {
                out.put(0);
            } else {
                out.put(1);
                out.putInt(item.getAmount());
                out.putInt(item.getId() + 1);
                if (item.getRevision() == null)//TODO remove
                    out.put(0);
                else
                    out.put(item.getRevision().ordinal());
            }
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemContainer(int interfaceId, int capacity, Item[] items) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(interfaceId);
        out.putShort(capacity);
        for (Item item : items) {
            if (item == null) {
                out.put(0);
            } else {
                out.put(1);
                out.putInt(item.getAmount());
                out.putInt(item.getId() + 1);
                if (item.getRevision() == null)//TODO remove
                    out.put(0);
                else
                    out.put(item.getRevision().ordinal());
            }
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemOnInterface(int frame, int item, Revision revision, int slot, int amount) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(34);
        out.putShort(frame);
        out.put(slot);
        out.putInt(item + 1);
        out.putShort(amount);
        out.put(revision == null ? 0 : revision.ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendDuelEquipment() {
        if (!player.getSession().isPresent())
            return this;

        for (int i = 0; i < player.getEquipment().capacity(); i++) {
            PacketBuilder out = new PacketBuilder(34);
            out.putShort(13824);//13824
            out.put(i);
            out.putInt(player.getEquipment().get(i).getId() + 1);
            out.putShort(player.getEquipment().get(i).getAmount());
            out.put(player.getEquipment().get(i).getRevision().ordinal());
            player.getSession().get().write(out);
        }
        return this;
    }

    public PacketSender sendSmithingData(int id, int slot, int widget, int amount) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(34);
        out.putShort(widget);
        out.put(slot);
        out.putInt(id + 1);
        out.putShort(amount);
        out.put(0);//revision
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendInterfaceItems(int interfaceId, CopyOnWriteArrayList<Item> items) {
        sendWidgetItems(interfaceId, items.toArray(new Item[items.size()]));
        return this;
    }

    public PacketSender sendWidgetItems(int interfaceId, Item[] items) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(interfaceId);
        out.putShort(items.length);
        for (Item item : items) {
            if (item == null) {
                out.put(0);
            } else {
                out.put(1);
                out.putInt(item.getAmount());
                out.putInt(item.getId() + 1);
                if (item.getRevision() == null)//TODO remove
                    out.put(0);
                else
                    out.put(item.getRevision().ordinal());
            }
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemOnInterface(int interfaceId, int item, int amount) {//TODO update to Item and send Revision
        if (!player.getSession().isPresent())
            return this;
        if (item <= 0)
            item = -1;
        if (amount <= 0)
            amount = 1;
        if (interfaceId <= 0)
            return this;
        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(interfaceId);
        out.putShort(1);
        if (item == -1) {
            out.put(0);
        } else {
            out.put(1);
            out.putInt(amount);
            out.putInt(item + 1);
            out.put(0);
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemOnWidget(int widget, Item item) {
        if (!player.getSession().isPresent())
            return this;

        if (widget <= 0)
            return this;
        PacketBuilder out = new PacketBuilder(53);
        out.put(1);
        out.putShort(widget);
        out.putShort(1);
        if (item == null) {
            out.put(0);
        } else {
            out.put(1);
            out.putInt(item.getAmount());
            out.putInt(item.getId() + 1);
            out.put(item.getRevision() == null ? 0 : item.getRevision().ordinal());
        }
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendItemsOnInterface(int interfaceId, Item[] items) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(53);
        if (items == null) {
            out.putShort(0);
            out.put(0);
            out.putInt(0);
            player.getSession().get().write(out);
            return this;
        }
        out.put(0);
        out.putShort(interfaceId);
        out.putShort(items.length);
        for (Item item : items) {
            if (item != null) {
                if (item.getAmount() > 254) {
                    out.put(255);
                    out.putInt(item.getAmount(), ByteOrder.INVERSE_MIDDLE);
                } else {
                    out.put(item.getAmount());
                }
                out.putInt(item.getId() + 1);
            } else {
                out.put(0);
                out.putInt(0);
            }
        }
        player.getSession().get().write(out);
        return this;
    }


    public PacketSender sendInteractionOption(String option, int slot, boolean top) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(104);
        out.put(slot, ValueType.C);
        out.put(top ? 1 : 0, ValueType.A);
        out.putString(option);
        player.getSession().get().write(out);
        PlayerInteractingOption interactingOption = PlayerInteractingOption.forName(option);
        if (interactingOption != null)
            player.setPlayerInteractingOption(interactingOption);
        return this;
    }

    public PacketSender sendTooltip(int id, String tooltip) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(129);
        out.putString(tooltip);
        out.putShort(id);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendString(int id, String string, int colour) {
        return sendString(id, FontUtils.add(string, colour));
    }

    public PacketSender sendString(int id, String string) {
        if (!player.getSession().isPresent())
            return this;

        if (id == 18250 && string.length() < 2)
            return this;
        if (player.getRights() != PlayerRights.DEVELOPER && !player.getFrameUpdater().shouldUpdate(string, id))
            return this;
        PacketBuilder out = new PacketBuilder(126);
        out.putString(string);
        out.putShort(id);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendString(int id, int child, String string, int colour) {
        if (!player.getSession().isPresent())
            return this;

        if (!player.getFrameUpdater().shouldUpdate(string, id))
            return this;

        PacketBuilder out = new PacketBuilder(130);
        out.putString(string);
        out.putShort(id);
        out.putShort(child);
        out.putInt(colour);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendShopPrice(int[] priceArray, int split) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(201);
        out.putShort(priceArray.length);
        out.put(split);
        for (int value : priceArray)
            out.putInt(value);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendRequiredItems(int[] requiredLevels, int[][] requiredItems, Revision[][] requiredRevisions, int[][] requiredAmounts) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(202);
        out.putShort(requiredLevels.length);
        for (int value : requiredLevels)
            out.putInt(value);

        out.putShort(requiredItems.length);
        for (int i = 0; i < requiredItems.length; i++) {
            out.putShort(requiredItems[i].length);
            for (int j = 0; j < requiredItems[i].length; j++) {
                out.putInt(requiredItems[i][j]);
                out.putInt(requiredAmounts[i][j]);
                out.put(requiredRevisions[i][j].ordinal());
            }
        }

        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendClientRightClickRemoval() {
        sendString(0, "[CLOSEMENU]");
        return this;
    }

    /**
     * Sends the players rights ordinal to the client.
     *
     * @return The packetsender instance.
     */
    public PacketSender sendRights() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(127);
        out.put(player.getRightsId());
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends a hint to specified position.
     *
     * @param position     The position to create the hint.
     * @param tilePosition The position on the square (middle = 2; west = 3; east = 4; south = 5; north = 6)
     * @return The Packet Sender instance.
     */
    public PacketSender sendPositionalHint(Position position, int tilePosition) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(254);
        out.put(tilePosition);
        out.putShort(position.getX());
        out.putShort(position.getY());
        out.put(position.getZ());
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends a hint above an figure's head.
     *
     * @param entity The target figure to draw hint for.
     * @return The PacketSender instance.
     */
    public PacketSender sendEntityHint(Figure entity) {
        if (!player.getSession().isPresent())
            return this;

        int type = entity.isPlayer() ? 10 : 1;
        PacketBuilder out = new PacketBuilder(254);
        out.put(type);
        out.putShort(entity.getIndex());
        out.putInt(0, ByteOrder.TRIPLE_INT);
        player.getSession().get().write(out);
        return this;
    }

    /**
     * Sends a hint removal above an figure's head.
     *
     * @param playerHintRemoval Remove hint from a player or an Npc?
     * @return The PacketSender instance.
     */
    public PacketSender sendEntityHintRemoval(boolean playerHintRemoval) {
        if (!player.getSession().isPresent())
            return this;

        int type = playerHintRemoval ? 10 : 1;
        PacketBuilder out = new PacketBuilder(254);
        out.put(type).putShort(-1);
        out.putInt(0, ByteOrder.TRIPLE_INT);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendMultiIcon(int value) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(61);
        out.put(value);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendPrivateMessage(long name, int rights, byte[] message, int size) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(196);
        out.putLong(name);
        out.putInt(player.getRelations().getPrivateMessageId());
        out.put(rights);
        out.putBytes(message, size);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendFriendStatus(int status) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(221);
        out.put(status);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendFriend(long name, int world, boolean login) {
        if (!player.getSession().isPresent())
            return this;
        world = world != 0 ? world + 9 : world;
        PacketBuilder out = new PacketBuilder(50);
        out.putLong(name);
        out.put(world);
        out.put(login ? 1 : 0);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender toggleViewDistance(int state) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(233);
        out.put(state > 1 ? 0 : state);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendTotalXp(long xp) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(45);
        out.putLong(xp);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendIgnoreList() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(214);
        int amount = player.getRelations().getIgnoreList().size();
        out.putShort(amount);
        for (int i = 0; i < amount; i++)
            out.putString("" + player.getRelations().getIgnoreList().get(i));
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendAnimationReset() {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(1);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendGraphic(Graphic graphic, Position position) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(position);
        PacketBuilder out = new PacketBuilder(4);
        out.put(0);
        out.putShort(graphic.getId());
        out.put(graphic.getRevision().ordinal());
        out.put(position.getZ());
        out.putShort(graphic.getDelay());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendObject(GameObject object) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(object);
        PacketBuilder out = new PacketBuilder(151);
        out.put(object.getZ(), ValueType.A);
        out.putInt(object.getId());
        out.put(object.getRevision().ordinal());
        out.put((byte) ((object.getType() << 2) + (object.getDirection() & 3)), ValueType.S);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendObjectRemoval(GameObject object) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(object);
        PacketBuilder out = new PacketBuilder(101);
        out.put((object.getType() << 2) + (object.getDirection() & 3), ValueType.C);
//        out.put(object.getZ());
        out.put(0);
        out.put(object.getRevision().ordinal());
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendObjectAnimation(GameObject object, Animation anim) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(object);
        PacketBuilder out = new PacketBuilder(160);
        out.put(0, ValueType.S);
        out.put((object.getType() << 2) + (object.getDirection() & 3), ValueType.S);
        out.putShort(anim.getId(), ValueType.A);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendGroundItemAmount(Position position, Item item, int amount) {
        if (!player.getSession().isPresent())
            return this;

        sendPosition(position);
        PacketBuilder out = new PacketBuilder(84);
        out.put(position.getZ());
        out.putInt(item.getId()).putShort(item.getAmount()).putShort(amount);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendPosition(final Position position) {
        if (!player.getSession().isPresent())
            return this;

        final Position other = player.getLastKnownRegion();
        if (other == null)
            return this;
        PacketBuilder out = new PacketBuilder(85);
        out.put(position.getY() - 8 * (other.getChunkY() - 6), ValueType.C);
        out.put(position.getX() - 8 * (other.getChunkX() - 6), ValueType.C);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender(Player player) {
        this.player = player;
    }

    private Player player;

    public PacketSender sendCombatBoxData(Figure figure) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder out = new PacketBuilder(125);
        out.putShort(figure.getIndex());
        out.put(figure.isPlayer() ? 0 : 1);
        if (figure.isPlayer()) {
            player.getSession().get().write(out);
        } else {
            Mob mob = (Mob) figure;
            boolean sendList = mob.getMaxConstitution() >= 2500 && Area.isMulti(mob);
            out.put(sendList ? 1 : 0);
            if (sendList) {
                /*List<DamageDealer> list = npc.fetchNewDamageMap() ? npc.getCombatBuilder().getTopKillers(npc) : npc.getDamageDealerMap();
                if (npc.fetchNewDamageMap()) {
                    npc.setDamageDealerMap(list);
                    npc.setFetchNewDamageMap(false);
                }
                out.put(list.size());
                for (int i = 0; i < list.size(); i++) {
                    DamageDealer dd = list.get(i);
                    out.putString(dd.getPlayer().getUsername());
                    out.putShort(dd.getDamage());
                }*/
            }
            player.getSession().get().write(out);
        }
        return this;
    }

    public PacketSender sendHideCombatBox() {
        if (player.getSession().isPresent())
            player.getSession().get().write(new PacketBuilder(128));
        return this;
    }

    public PacketSender sendDynamicGameScene() {
        if (!player.getSession().isPresent())
            return this;

        //player.setRegionChange(true).setAllowRegionChangePacket(true);
        player.setLastKnownRegion(player.copyPosition());
        PacketBuilder builder = new PacketBuilder(241);

        int chunkX = player.getChunkX();
        int chunkY = player.getChunkY();

        builder.putShort(chunkX);
        builder.putShort(chunkY);

        int sceneLength = GameSettings.MAP_SIZES[player.getMapSize()] >> 4;
        // the regionids(maps files) that will be used to load this scene
        int[] regionIds = new int[4 * sceneLength * sceneLength];
        int newRegionIdsCount = 0;

        for (int z = 0; z < 4; z++) {
            for (int cx = chunkX - 6; cx < chunkX + 7; cx++) {
                y:
                for (int cy = chunkY - 6; cy < chunkY + 7; cy++) {

                    int regionId = ((cx / 8) << 8) + (cy / 8);
                    Region region = GameWorld.getRegions().getActiveRegions().get(regionId);

                    int newChunkX = 0;//cx + 6;
                    int newChunkY = 0;//cy + 6;
                    int newPlane = 0;//z;
                    int rotation = 0;// no rotation
                    if (region instanceof DynamicRegion) { // generated map
                        DynamicRegion dynamicRegion = (DynamicRegion) region;
                        int[] palette = dynamicRegion.getRegionCoords()[z][cx - ((cx / 8) * 8)][cy - ((cy / 8) * 8)];
                        newChunkX = palette[0];
                        newChunkY = palette[1];
                        newPlane = palette[2];
                        rotation = palette[3];
                    }

                    if (newChunkX == 0 || newChunkY == 0)
                        builder.put(0);
                    else {
                        builder.put(1);
                        int info = (rotation << 1) | (newPlane << 24) | (newChunkX << 14) | (newChunkY << 3);
                        builder.putInt(info);
                        int newRegionId = (((newChunkX / 8) << 8) + (newChunkY / 8));
                        for (int index = 0; index < newRegionIdsCount; index++)
                            if (regionIds[index] == newRegionId)
                                continue y;
                        regionIds[newRegionIdsCount++] = newRegionId;
                    }
                }
            }
        }
        player.getSession().get().write(builder);
        return this;
    }

    public PacketSender constructMapRegion(Palette palette, int regionX, int regionY) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder builder = new PacketBuilder(241);
        builder.putShort(regionX);
        builder.putShort(regionY);

        builder.initializeAccess(PacketBuilder.AccessType.BIT);
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    builder.put(tile != null ? 1 : 0);
                    if (tile != null) {
                        int locationHash = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() % 4 << 1;
                        builder.putBits(26, locationHash);
                    }
                }
            }
        }
        player.getSession().get().write(builder);
        return this;
    }

    public PacketSender constructMapRegion(Palette palette) {
        if (!player.getSession().isPresent())
            return this;

        PacketBuilder builder = new PacketBuilder(241);
        builder.putShort(player.getChunkY(), ValueType.A);
        builder.putShort(player.getChunkX());

        builder.initializeAccess(PacketBuilder.AccessType.BIT);
        for (int z = 0; z < 4; z++) {
            for (int x = 0; x < 13; x++) {
                for (int y = 0; y < 13; y++) {
                    PaletteTile tile = palette.getTile(x, y, z);
                    builder.put(tile != null ? 1 : 0);
                    if (tile != null) {
                        int locationHash = tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() % 4 << 1;
                        builder.putBits(26, locationHash);
                    }
                }
            }
        }
        player.getSession().get().write(builder);
        return this;
    }

    public PacketSender sendObjectsRemoval(int chunkX, int chunkY, int height) {
        if (player.getSession().isPresent())
            player.getSession().get().write(new PacketBuilder(153).put(chunkX).put(chunkY).put(height));
        return this;
    }

    public PacketSender sendConsoleMessage(String message) {
        if (!player.getSession().isPresent())
            return this;
        PacketBuilder out = new PacketBuilder(123);
        out.putString(message);
        player.getSession().get().write(out);
        return this;
    }

    public PacketSender sendMessage(String message, int colour) {
        sendMessage(FontUtils.add(message, colour), false);
        return this;
    }

    public PacketSender sendMessage(String message, int colour, boolean filter) {
        sendMessage(FontUtils.add(message, colour), filter);
        return this;
    }

    public PacketSender sendMessage(String message, boolean filter) {
        sendMessage(filter ? 109 : 0, message, null);
        return this;
    }

    public PacketSender sendMessage(MessageType type, String message) {
        sendMessage(type.getPrefix() + message + type.getSuffix(), false);
        return this;
    }

    public void sendGoblinRaidMessage(String text) {
        sendMessage(121, text, null);
    }

    public void sendDemonRaidMessage(String text) {
        sendMessage(122, text, null);
    }

    public void sendSinkholeMessage(String text) {
        sendMessage(123, text, null);
    }

    public void sendWarbandsMessage(String text) {
        sendMessage(124, text, null);
    }

    public void sendWorldEventMessage(String text) {
        sendMessage(126, text, null);
    }

    public void sendTradeRequestMessage(Player p) {
        sendMessage(100, "wishes to trade with you.", p);
    }

    public void sendClanWarsRequestMessage(Player p) {
        sendMessage(101, "wishes to challenge your clan to a clan war.", p);
    }

    public void sendClanInviteMessage(Player p) {
        sendMessage(117, p.getUsername() + " is inviting you to join their clan.", p);
    }

    public void sendDuelChallengeRequestMessage(Player p, boolean friendly) {
        sendMessage(101, "wishes to duel with you (" + (friendly ? "friendly" : "stake") + ").", p);
    }

    public void sendClanChatMessage(String message) {
        sendMessage(102, message, null);
    }
    public void sendClanChatMessage(String message, int colour) {
        sendMessage(102, FontUtils.add(message, colour), null);
    }

    public void sendUrl(String message) {
        sendMessage(103, message, null);
    }

    public void resetCameraShake() {
        sendCameraShake(1, 0, 0, 0);
    }

    public void sendDungeonneringRequestMessage(Player player) {
        sendMessage(111, "has invited you to a dungeon party.", player);
    }

    public PacketSender sendMessage(int type, String text, Player player) {
        if (text == null || this.player != null && !this.player.getSession().isPresent() || player != null && !player.getSession().isPresent() || text.isEmpty())
            return this;

        int maskData = 0;
        if (player != null)
            maskData |= 0x1;

        PacketBuilder out = new PacketBuilder(253);
        out.putSmart(type);
        out.put(maskData);
        if ((maskData & 0x1) != 0)
            out.putString(Misc.formatPlayerNameForDisplay(player.getUsername()));
        out.putString(text);
        this.player.getSession().get().write(out);
        return this;
    }
}
