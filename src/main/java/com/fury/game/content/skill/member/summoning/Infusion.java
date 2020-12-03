package com.fury.game.content.skill.member.summoning;

import com.fury.cache.Revision;
import com.fury.engine.task.impl.FamiliarSpawnTask;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.action.Action;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.graphic.Graphic;

import java.util.ArrayList;
import java.util.List;

/**
 * The summoning skill is based upon creating pouches that contain
 * certain 'familiars', which you can then summon and use their abilities
 * as a form of 'assistance'.
 *
 * @author Gabriel Hannason
 */

public class Infusion {

    Player player;
    private boolean displayOnlyCreatable = false;
    private InfusionInterface infusionInterface = null;

    public boolean displayOnlyCreatable() {
        return displayOnlyCreatable;
    }

    public void setDisplayOnlyCreatable(boolean displayOnlyCreatable) {
        this.displayOnlyCreatable = displayOnlyCreatable;
    }

    public InfusionInterface getInfusionInterface() {
        return infusionInterface;
    }

    public void setInfusionInterface(InfusionInterface infusionInterface) {
        this.infusionInterface = infusionInterface;
    }

    public static void openInfuseInterface(Player player, boolean is_scroll, boolean dungeoneering) {
        SummoningType type = dungeoneering ? SummoningType.DUNGEONEERING : SummoningType.NORMAL;
        openInfuseInterface(player, is_scroll, type);
    }

    public static void openInfuseInterface(Player player, boolean is_scroll) {
        openInfuseInterface(player, is_scroll, player.getSummoning().getInfusionInterface().getSummoningType());
    }

    private static void generateInterfaceStock(Player player, boolean is_scroll, SummoningType type) {
        int amount = type.getEnd() - type.getStart();

        Item[] scrollsOrPouches;
        int[] reqLevels;
        int[][] reqItems;
        int[][] reqAmounts;
        Revision[][] reqRevisions;
        int[] prices;
        if (is_scroll) {
            scrollsOrPouches = Scroll.getAllScrolls(type);
            reqLevels = OldPouch.getRequiredLevels(type);
            reqItems = Scroll.getRequiredItems(type);
            reqRevisions = Scroll.getRequiredRevisions(type);
            reqAmounts = new int[amount][];
            prices = new int[amount];

            for (int i = 0; i < amount; i++) {
                prices[i] = 1;
                reqAmounts[i] = new int[]{1};
            }
        } else {
            scrollsOrPouches = OldPouch.getPouches(type);
            reqLevels = OldPouch.getRequiredLevels(type);
            reqItems = OldPouch.getRequiredItems(type);
            reqRevisions = OldPouch.getRequiredRevisions(type);
            reqAmounts = OldPouch.getRequiredAmounts(type);
            prices = OldPouch.getShardRequired(type);
        }

        if (player.getSummoning().displayOnlyCreatable()) {
            List<Item> pouchesList = new ArrayList<>();
            List<Integer> reqLevelsList = new ArrayList<>();
            List<int[]> reqItemsList = new ArrayList<>();
            List<int[]> reqAmountsList = new ArrayList<>();
            List<Revision[]> reqRevisionsList = new ArrayList<>();
            List<Integer> pricesList = new ArrayList<>();
            for (int i = 0; i < reqItems.length; i++) {
                if (player.getSkills().getLevel(Skill.SUMMONING) >= reqLevels[i]) {
                    int has = 0;
                    for (int j = 0; j < reqItems[i].length; j++)
                        if (player.getInventory().getAmount(new Item(reqItems[i][j], reqRevisions[i][j])) >= reqAmounts[i][j])
                            has++;
                    if (has == reqItems[i].length) {
                        pricesList.add(prices[i]);
                        pouchesList.add(scrollsOrPouches[i]);
                        reqLevelsList.add(reqLevels[i]);
                        reqItemsList.add(reqItems[i]);
                        reqAmountsList.add(reqAmounts[i]);
                        reqRevisionsList.add(reqRevisions[i]);
                    }
                }
            }

            prices = pricesList.stream().mapToInt(i -> i).toArray();
            reqLevels = reqLevelsList.stream().mapToInt(i -> i).toArray();
            reqItems = reqItemsList.toArray(new int[reqItemsList.size()][]);
            reqAmounts = reqAmountsList.toArray(new int[reqAmountsList.size()][]);
            reqRevisions = reqRevisionsList.toArray(new Revision[reqRevisionsList.size()][]);
            scrollsOrPouches = pouchesList.toArray(new Item[pouchesList.size()]);
        }
        player.getSummoning().setInfusionInterface(new InfusionInterface(scrollsOrPouches, reqLevels, reqItems, reqRevisions, reqAmounts, prices, type));
    }

    public static void openInfuseInterface(Player player, boolean is_scroll, SummoningType type) {
        generateInterfaceStock(player, is_scroll, type);

        player.getPacketSender().sendConfig(1998, player.getSummoning().displayOnlyCreatable() ? 1 : 0);
        player.getPacketSender().sendInterface(is_scroll ? SCROLL_INTERFACE_ID : POUCH_INTERFACE_ID);
        player.getPacketSender().sendShopPrice(player.getSummoning().getInfusionInterface().getPrices(), 0);
        player.getPacketSender().sendRequiredItems(player.getSummoning().getInfusionInterface().getReqLevels(), player.getSummoning().getInfusionInterface().getReqItems(), player.getSummoning().getInfusionInterface().getReqRevisions(), player.getSummoning().getInfusionInterface().getReqAmounts());
        player.getPacketSender().sendWidgetItems(is_scroll ? SCROLL_ITEMS_INTERFACE_ID : POUCH_ITEMS_INTERFACE_ID, player.getSummoning().getInfusionInterface().getScrollsOrPouches());
        player.getPacketSender().sendInterfaceComponentScrollMax(is_scroll ? SCROLL_INTERFACE_ID + 10 : POUCH_INTERFACE_ID + 6, (int) Math.ceil((double) player.getSummoning().getInfusionInterface().getAmount() / 8) * 55 + 8);
    }

    private static class Infusing extends Action {

        private int slot, id, amount;
        private int[] reqItems, reqAmounts;
        private boolean isScroll;
        private Revision[] reqRevisions;
        private int count = 0;

        public Infusing(int slot, int id, int amount) {
            this.slot = slot;
            this.id = id;
            this.amount = amount;
        }

        @Override
        public boolean start(Player player) {
            if(player.getSummoning().getInfusionInterface() == null)
                return false;

            player.getPacketSender().sendInterfaceRemoval();

            if(slot >= player.getSummoning().getInfusionInterface().getScrollsOrPouches().length || player.getSummoning().getInfusionInterface().getScrollsOrPouches()[slot].getId() != id) {
                System.err.println("Infusion Interface Error");//or potential interface manipulation.
                return false;
            }

            reqItems = player.getSummoning().getInfusionInterface().getReqItems()[slot];
            reqAmounts = player.getSummoning().getInfusionInterface().getReqAmounts()[slot];
            reqRevisions = player.getSummoning().getInfusionInterface().getReqRevisions()[slot];
            isScroll = Scroll.isScroll(id);

            if(!checkAll(player))
                return false;

            return true;
        }

        @Override
        public boolean process(Player player) {
            return count < amount;
        }

        @Override
        public int processWithDelay(Player player) {
            if(!checkAll(player))
                return -1;

            for (int i = 0; i < reqItems.length; i++)
                player.getInventory().delete(new Item(reqItems[i], reqAmounts[i], reqRevisions[i]));

            Item scroll = player.getSummoning().getInfusionInterface().getScrollsOrPouches()[slot];

            ChristmasEvent.giveSnowflake(player);

            player.getInventory().add(scroll);
            player.perform(POUCH_INFUSION_ANIMATION);
            player.perform(POUCH_INFUSION_GRAPHICS);
            if(isScroll) {
                Scroll pouch = Scroll.forScroll(scroll.getId());
                if(pouch != null)
                    player.getSkills().addExperience(Skill.SUMMONING, pouch.getExp());
            } else {
                OldPouch pouch = OldPouch.forId(scroll.getId());
                if(pouch != null) {
                    player.getSkills().addExperience(Skill.SUMMONING, pouch.getExp());

                    if(pouch == OldPouch.SPIRIT_WOLF)
                        Achievements.doProgress(player, Achievements.AchievementData.MAKE_10_SPIRIT_WOLF_POUCHES);
                    else if(pouch == OldPouch.STEEL_TITAN)
                        Achievements.doProgress(player, Achievements.AchievementData.MAKE_500_STEEL_TITAN_POUCHES);
                }
            }
            count++;
            return 2;
        }

        @Override
        public void stop(Player player) {

        }

        public boolean checkAll(Player player) {
            if (!player.getSkills().hasRequirement(Skill.SUMMONING, player.getSummoning().getInfusionInterface().getReqLevels()[slot], "be able to make this"))
                return false;

            for (int i = 0; i < reqItems.length; i++) {
                if (player.getInventory().getAmount(new Item(reqItems[i], reqRevisions[i])) < reqAmounts[i]) {
                    player.message("You do not have all the items required to make this.");
                    return false;
                }
            }

            return true;
        }
    }

    public static void handleInfusionInterface(Player player, int slot, int id, int amount) {
        player.getActionManager().setAction(new Infusing(slot, id, amount));
    }

    public static final int SCROLL_INTERFACE_ID = 41000, SCROLL_ITEMS_INTERFACE_ID = SCROLL_INTERFACE_ID + 12, POUCH_INTERFACE_ID = 41014, POUCH_ITEMS_INTERFACE_ID = POUCH_INTERFACE_ID + 7;
    private static final Animation POUCH_INFUSION_ANIMATION = new Animation(4411);
    private static final Graphic POUCH_INFUSION_GRAPHICS = new Graphic(1207);

    public Infusion(Player p) {
        this.player = p;
    }

    public void setCombat(boolean asCombat) {
        Mob familiar = getFamiliar().getSummonMob();
        FamiliarData data = FamiliarData.forNPCId(familiar.getId());
        if (data == null)
            return;
        familiar.setTransformation((asCombat) ? data.combatNpcId : data.npcId);
    }

    public static String getTimer(int seconds) {
        int minuteCounter = 0;
        int secondCounter = 0;
        for (int j = seconds; j > 0; j--) {
            if (secondCounter >= 59) {
                minuteCounter++;
                secondCounter = 0;
            } else
                secondCounter++;
        }
        if (minuteCounter == 0 && secondCounter == 0)
            return "";
        String secondString = "" + secondCounter;
        if (secondCounter < 10)
            secondString = "0" + secondCounter + "";
        return " " + minuteCounter + "." + secondString;
    }

    public String getSpecial() {
        return getFamiliar().getSpecialEnergy() + "/60";
    }

    public void login() {
        clearInterface();
        if (spawnTask != null) {
            GameWorld.schedule(2, spawnTask);
        } else {
            player.getPacketSender().sendSummoningInfo(false, 0);//Summoning icon
        }
        spawnTask = null;
    }

    public void clearInterface() {
        player.getPacketSender().sendString(54028, "");
        player.getPacketSender().sendString(54043, "00.00");
        player.getPacketSender().sendString(54045, "99/99");
        player.getPacketSender().sendString(54046, "60/60");
        player.getPacketSender().sendButtonToggle(54050, false);
        player.getPacketSender().sendString(54051, "0");

        player.getPacketSender().sendString(54223, "");
        player.getPacketSender().sendString(54232, "0%");
        player.getPacketSender().sendString(54234, "0%");

        player.getPacketSender().sendNpcHeadOnInterface(60, Revision.RS2, 54221); // invisible head to remove it
        player.getPacketSender().sendNpcHeadOnInterface(60, Revision.RS2, 54021); // invisible head to remove it
        player.getPacketSender().sendString(18045, !player.getSkills().hasLevel(Skill.SUMMONING, 10) ? "   " + player.getSkills().getLevel(Skill.SUMMONING) + "/" + player.getSkills().getMaxLevel(Skill.SUMMONING) : " " + player.getSkills().getLevel(Skill.SUMMONING) + "/" + player.getSkills().getMaxLevel(Skill.SUMMONING));
    }

    private FamiliarSpawnTask spawnTask;
    private OldFamiliar familiar;

    public FamiliarSpawnTask getSpawnTask() {
        return spawnTask;
    }

    public FamiliarSpawnTask setFamiliarSpawnTask(FamiliarSpawnTask spawnTask) {
        this.spawnTask = spawnTask;
        return this.spawnTask;
    }

    public OldFamiliar getFamiliar() {
        return this.familiar;
    }

    public void setFamiliar(OldFamiliar familiar) {
        this.familiar = familiar;
    }

    private int[] charmImpConfigs = new int[]{0, 0, 0, 0};

    public void setCharmImpConfig(int index, int config) {
        this.charmImpConfigs[index] = config;
    }

    public void setCharmImpConfig(int[] charmImpConfig) {
        this.charmImpConfigs = charmImpConfig;
    }

    public int getCharmImpConfig(int index) {
        return charmImpConfigs[index];
    }

    public int[] getCharmImpConfigs() {
        return charmImpConfigs;
    }
}
