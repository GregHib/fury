package com.fury.game.content.global.minigames.impl;

import com.fury.cache.Revision;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.shop.Shop;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.npcs.RFDCompletion;
import com.fury.game.content.dialogue.input.impl.EnterAmountToBuyFromShop;
import com.fury.game.content.dialogue.input.impl.EnterAmountToSellToShop;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.PlayerPanel;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.object.ObjectHandler;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.instance.MapInstance;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.util.Colours;
import com.fury.util.FontUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriel Hannason
 *         Wrote this quickly!!
 *         Handles the RFD quest
 */
public class RecipeForDisaster extends Controller {

    private MapInstance instance;
    private static final Position OUTSIDE = new Position(3203, 3423);


    @Override
    public void start() {
        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
            removeController();
            return;
        }
        enter();
    }

    public void enter() {
        instance = new MapInstance(236, 668);
        instance.load(() -> {
            player.moveTo(instance.getTile(11, 21).add(0, 0,2));
            player.getPrayer().closeAllPrayers();
            spawnWave(player, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
        });
    }

    //0 - logout
    //1 - teleport / death
    //2 - leave
    public void leave(int type) {
        player.stopAll();
        if(type != 0) {
            if(type == 1)
                player.getMovement().lock(3);
            else
                ObjectHandler.useStairs(player, -1, OUTSIDE, 0, 1);
            removeController();
        } else
            player.moveTo(OUTSIDE);
        instance.destroy(null);
    }

    @Override
    public boolean logout() {
        leave(0);
        return true;
    }

    @Override
    public boolean login() {
        player.moveTo(OUTSIDE);
        return true;
    }

    @Override
    public void magicTeleported(int type) {
        leave(1);
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 12356:
                leave(2);
                return false;
        }
        return true;
    }

    @Override
    public boolean sendDeath() {
        player.moveTo(OUTSIDE);
        player.stopAll();
        leave(1);
        player.reset();
        return false;//keep items
    }

    @Override
    public void processNPCDeath(Mob mob) {
        player.getMinigameAttributes().getRecipeForDisasterAttributes().setWavesCompleted(player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() + 1);
        switch (mob.getId()) {
            case 3493:
            case 3494:
            case 3495:
            case 3496:
            case 3497:
                int index = mob.getId() - 3490;
                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(index, true);
                break;
            case 3491:
                Graphic.sendGlobal(player, new Graphic(591), mob.copyPosition());
                player.getMinigameAttributes().getRecipeForDisasterAttributes().setPartFinished(8, true);
                leave(2);
                player.reset();
                player.getDialogueManager().startDialogue(new RFDCompletion());
                PlayerPanel.refreshPanel(player);
                break;
        }

        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
            return;
        GameWorld.schedule(3, () -> {
            if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
                return;
            spawnWave(player, player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted());
        });
    }

    public void spawnWave(final Player p, final int wave) {
        if (wave > 5)
            return;
        GameWorld.schedule(2, () -> {
            int npc = wave >= 5 ? 3491 : 3493 + wave;
            Mob n = GameWorld.getMobs().spawn(npc, npc == 3494 ? Revision.PRE_RS3 : Revision.RS2, instance.getTile(11, 11).setPlane(2), true);
            n.setSpawnedFor(p);
            n.setTarget(p);
        });
    }

    public static String getQuestTabPrefix(Player player) {
        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(0) && player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() < 6) {
            return FontUtils.YELLOW;
        }
        if (player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6) {
            return FontUtils.GREEN;
        }
        return FontUtils.RED;
    }

    public static void openQuestLog(Player p) {
        for (int i = 8145; i < 8196; i++)
            p.getPacketSender().sendString(i, "");
        for (int i = 12174; i < 12174 + 50; i++)
            p.getPacketSender().sendString(i, "");

        p.getPacketSender().sendInterface(8134);
        p.getPacketSender().sendString(8136, "Close window");
        p.getPacketSender().sendString(637, "" + questTitle);
        p.getPacketSender().sendString(8145, "");
        int questIntroIndex = 0;
        for (int i = 8147; i < 8147 + questIntro.length; i++) {
            p.getPacketSender().sendString(i, questIntro[questIntroIndex], Colours.DRE);
            questIntroIndex++;
        }
        int questGuideIndex = 0;
        for (int i = 8147 + questIntro.length; i < 8147 + questIntro.length + questGuide.length; i++) {
            if (!p.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(questGuideIndex))
                p.getPacketSender().sendString(i, questGuide[questGuideIndex]);
            else
                p.getPacketSender().sendString(i, "<s>" + questGuide[questGuideIndex] + "</s>");
            if (questGuideIndex == 2) {
                if (p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() > 0 && !p.getMinigameAttributes().getRecipeForDisasterAttributes().hasFinishedPart(questGuideIndex))
                    p.getPacketSender().sendString(i, questGuide[questGuideIndex], Colours.YELLOW);
                if (p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
                    p.getPacketSender().sendString(i, "<s>" + questGuide[questGuideIndex] + "</s>");
            }
            questGuideIndex++;
        }
        if (p.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted() == 6)
            p.getPacketSender().sendString(8147 + questIntro.length + questGuide.length, "Quest complete!", Colours.DRE);
    }

    public static void openRFDShop(final Player player) {
        List<Item> stock = new ArrayList<>();
        for (int i = 0; i <= player.getMinigameAttributes().getRecipeForDisasterAttributes().getWavesCompleted(); i++) {
            switch (i) {
                case 1:
                    stock.add(new Item(7453, 10));
                    break;
                case 2:
                    stock.add(new Item(7454, 10));
                    stock.add(new Item(7455, 10));
                    break;
                case 3:
                    stock.add(new Item(7456, 10));
                    stock.add(new Item(7457, 10));
                    break;
                case 4:
                    stock.add(new Item(7458, 10));
                    break;
                case 5:
                    stock.add(new Item(7459, 10));
                    stock.add(new Item(7460, 10));
                    break;
                case 6:
                    stock.add(new Item(7461, 10));
                    stock.add(new Item(7462, 10));
                    break;
            }
        }
        Shop shop = new Shop(player, ShopManager.RECIPE_FOR_DISASTER_STORE, "Culinaromancer's chest", new Item(995), stock.toArray(new Item[stock.size()]), false);
        shop.setPlayer(player);
        shop.sendPrices(shop);
        player.getPacketSender().sendItemContainer(player.getInventory(), Shop.INVENTORY_INTERFACE_ID);
        player.getPacketSender().sendItemContainer(shop, Shop.ITEM_CHILD_ID);
        player.getPacketSender().sendString(Shop.NAME_INTERFACE_CHILD_ID, "Culinaromancer's chest");
        if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
            player.getPacketSender().sendInterfaceSet(Shop.INTERFACE_ID, Shop.INVENTORY_INTERFACE_ID - 1);
        player.setShop(shop);
        player.setInterfaceId(Shop.INTERFACE_ID);
        player.setShopping(true);
    }


    private static final String questTitle = "Recipe for Disaster";
    private static final String[] questIntro = {
            "The Culinaromancer has returned and only you",
            "             can stop him!                  ",
            "",
    };
    private static final String[] questGuide = {
            "Talk to the Gypsy in Varrock and agree to help her.",
            "Enter the portal.",
            "Defeat the following servants:",
            "* Agrith-Na-Na",
            "* Flambeed",
            "* Karamel",
            "* Dessourt",
            "* Gelatinnoth mother",
            "And finally.. Defeat the Culinaromancer!"
    };
}
