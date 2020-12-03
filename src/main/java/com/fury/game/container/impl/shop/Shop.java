package com.fury.game.container.impl.shop;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.engine.task.impl.ShopRestockTask;
import com.fury.game.GameSettings;
import com.fury.game.container.types.AlwaysStackContainer;
import com.fury.game.container.types.StackContainer;
import com.fury.game.content.dialogue.input.impl.EnterAmountToBuyFromShop;
import com.fury.game.content.dialogue.input.impl.EnterAmountToSellToShop;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.minigames.impl.RecipeForDisaster;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.game.entity.character.player.info.GameMode;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.world.GameWorld;
import com.fury.util.Misc;

import java.util.HashMap;

public class Shop extends AlwaysStackContainer {

    int id;
    private String name;
    private Item currency;
    private Item[] originalStock;
    private int dungeoneeringSplit;
    private int[] shopPrices;
    private boolean restockingItems;

    public Shop(Player player, int id, String name, Item currency, Item[] stockItems, boolean dungeoneeringShop) {
        super(player, dungeoneeringShop ? stockItems.length : 42);

        if (!dungeoneeringShop && stockItems.length > 42)
            throw new ArrayIndexOutOfBoundsException("Stock cannot have more than 40 items; check shop[" + id + "]: stockLength: " + stockItems.length);
        this.id = id;
        this.name = name.length() > 0 ? name : "General Store";
        this.currency = currency;
        setItems(stockItems);
        this.originalStock = stockItems.clone();
    }

    public static final int INTERFACE_ID = 3824;
    public static final int ITEM_CHILD_ID = 3900;
    public static final int NAME_INTERFACE_CHILD_ID = 3901;
    public static final int INVENTORY_INTERFACE_ID = 3823;


    private boolean canShop(Player player) {
        if (player.getGameMode().isIronMan() && id != 4 && id != 6 && id != 8 && id != 9 && id != 10 && id != 11 && id != 22 && id != 23 && id != 24 && id != 37 && id != 39 && id != 40 && id != 85 && id != 107 && id != 111 && id != 117 && id != 118 && id != 119 && id != 120 && id != 121 && id != 122 && id != 124 && id != 125 && id != 126 && id != 127 && id != 128 && !isDungeoneeringShop()) {
            player.message("You're unable to access this shop because you're an " + player.getGameMode().toString().toLowerCase().replace("_", " ") + " player.");
            return false;
//            }
        }
        return true;
    }

    public Shop open(Player player) {
        if (!canShop(player))
            return this;
        if (id == 24)
            player.message("Donate to fury using ::donate, you currently have " + player.getPoints().getInt(Points.DONOR) + " donor points.");

        setPlayer(player);
        sendPrices(this);
        player.getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
        player.getPacketSender().sendString(currency.getId() != 995 ? currency(currency.getId()) : 0, "currencyCoin");
        player.setShop(this);
        player.setInterfaceId(isDungeoneeringShop() ? DungeonConstants.SHOP_INTERFACE_ID : INTERFACE_ID);
        player.setShopping(true);
        refresh();
        if (Misc.getMinutesPlayed(player) <= 190)
            player.message("Note: When selling an item to a store, it loses 15% of its original value.", true);
        return this;
    }

    public void sendPrices(Shop shop) {
        shopPrices = new int[originalStock.length];
        for (int i = 0; i < originalStock.length; i++) {
            Item item = get(i);
            if (item == null)
                continue;
            if (shop.getCurrency().getId() == 995 || shop.getCurrency().getId() == DungeonConstants.RUSTY_COINS) {
                shopPrices[i] = item.getDefinition().getValue();
            } else {
                Object[] data = ShopManager.getCustomShopData(id, item.getId());
                if (data != null)
                    shopPrices[i] = (int) data[0];
            }
        }
        player.getPacketSender().sendShopPrice(shopPrices, dungeoneeringSplit);
    }

    public boolean isDungeoneeringShop() {
        return id >= 500;
    }

    private static final int[] CURRENCIES = {// 42 = guardians items  78 = stardust exchange
            995, 24, 33, 39, 26, 43, 45, 78, 25, 47, 42, 44
    };

    private static int currency(int currencyId) {
        for (int i = 0; i < CURRENCIES.length; i++)
            if (currencyId == CURRENCIES[i])
                return i;
        return 0;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void refresh() {
        if (id == ShopManager.RECIPE_FOR_DISASTER_STORE) {
            RecipeForDisaster.openRFDShop(player);
            return;
        }
        for (Player player : GameWorld.getPlayers()) {
            if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
                continue;

            player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
            if (isDungeoneeringShop()) {
                Item[] resources = new Item[getDungeoneeringSplit()];
                System.arraycopy(getItems(), 0, resources, 0, getDungeoneeringSplit());
                Item[] tools = new Item[24];
                System.arraycopy(getItems(), getDungeoneeringSplit(), tools, 0, 24);

                player.getPacketSender().sendItemContainer(DungeonConstants.RESOURCE_ITEM_CHILD_ID, getDungeoneeringSplit(), resources);
                player.getPacketSender().sendItemContainer(DungeonConstants.TOOLS_ITEM_CHILD_ID, 24, tools);

                //Auto resizing
                int toolCount = 0;
                for (Item tool : tools) {
                    if (tool == null || tool.getId() <= 0)
                        continue;
                    toolCount++;
                }
                int toolRows = (int) Math.ceil((double) toolCount / 9);
                int resourceRows = (int) Math.ceil((double) getDungeoneeringSplit() / 9);
                player.getPacketSender().sendInterfaceComponentPosition(601, 2, 0, 25 + (58 * resourceRows));
                player.getPacketSender().sendInterfaceComponentPosition(601, 3, 0, 50 + (58 * resourceRows));
                player.getPacketSender().sendInterfaceComponentScrollMax(601, 50 + (58 * (resourceRows + toolRows)));
            } else
                player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);

            if (id == ShopManager.GENERAL_STORE) {
                shopPrices = new int[capacity()];
                for (int i = 0; i < capacity(); i++) {
                    Item item = get(i);
                    if (item != null)
                        shopPrices[i] = item.getDefinitions().getValue();
                }
                player.getPacketSender().sendShopPrice(shopPrices, dungeoneeringSplit);
            }

            player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
            if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop || player.getInputHandling() instanceof EnterAmountToBuyFromShop))
                player.getPacketSender().sendInterfaceSet(isDungeoneeringShop() ? DungeonConstants.SHOP_INTERFACE_ID : INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
        }
    }

    public void publicRefresh() {
        Shop publicShop = ShopManager.getShops().get(id);
        if (publicShop == null)
            return;
        publicShop.setItems(getItems());
        for (Player player : GameWorld.getPlayers()) {
            if (player == null)
                continue;
            if (player.getShop() != null && player.getShop().getId() == id && player.isShopping())
                player.getShop().setItems(publicShop.getItems());
        }
    }

    public void checkSellValue(Player player, int slot) {
        //TODO validate
        Item item = player.getInventory().get(slot);

        if (item == null)
            return;

        if (!shopBuysItem(id, item)) {
            player.message("You cannot sell this item to this store.");
            return;
        }

        String name = item.getDefinition().name;
        StringBuilder builder = new StringBuilder();

        builder.append(name + ": shop will buy for ");

        if (currency.getId() != -1) {
            int value = getCurrencyValue(item);
            String currency = getCurrencyName(item);
            if (value != 1)
                value = (int) (value * 0.85);

            builder.append(value + " " + currency + "" + shopPriceEx(value) + ".");
        } else {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return;
            int value = (int) obj[0];
            if (value != 1)
                value = (int) (value * 0.85);
            builder.append(value + " " + obj[1] + ".");
        }

        if (player != null)
            player.message(builder.toString());
    }

    private boolean isCurrencyShop() {
        return id == ShopManager.TOKKUL_EXCHANGE_STORE || id == ShopManager.STARDUST_EXCHANGE_STORE || id == ShopManager.ENERGY_FRAGMENT_STORE || id == ShopManager.AGILITY_TICKET_STORE || id == ShopManager.GRAVEYARD_STORE || id == ShopManager.DONATION_POINTS_STORE || id == ShopManager.MEMBERS_STORE_II || id == ShopManager.SANTAS_SNOWFLAKE_SHOP;
    }

    private int getCurrencyValue(Item item) {
        int value = item.getDefinitions().getValue();
        if (isCurrencyShop()) {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return -1;
            value = (int) obj[0];
        }

        return value;
    }

    private String getCurrencyName(Item item) {
        String currencyName = currency.getDefinition().getName().toLowerCase();
        String currency = currencyName.endsWith("s") ? currencyName : currencyName + "s";

        if (isCurrencyShop()) {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return null;
            currency = (String) obj[1];
        }
        return currency;
    }

    public static String shopPriceEx(int shopPrice) {
        String ShopAdd = "";
        if (shopPrice >= 1000 && shopPrice < 1000000) {
            ShopAdd = " (" + (shopPrice / 1000) + "K)";
        } else if (shopPrice >= 1000000) {
            ShopAdd = " (" + (shopPrice / 1000000) + " million)";
        }
        return ShopAdd;
    }

    public static boolean shopBuysItem(int shopId, Item item) {
        if (!item.tradeable())
            return false;
        if (item.getId() == 995 || item.getId() == DungeonConstants.RUSTY_COINS)
            return false;
        if (shopId == ShopManager.GENERAL_STORE)
            return true;
        if (shopId >= 500 && item.tradeable())
            return true;
        if (shopId == ShopManager.DUNGEONEERING_STORE || shopId == ShopManager.PKING_REWARDS_STORE || shopId == ShopManager.STARDUST_EXCHANGE_STORE || shopId == ShopManager.VOTING_REWARDS_STORE || shopId == ShopManager.IRONMAN_VOTING_REWARDS_STORE || shopId == ShopManager.RECIPE_FOR_DISASTER_STORE || shopId == ShopManager.ENERGY_FRAGMENT_STORE || shopId == ShopManager.AGILITY_TICKET_STORE || shopId == ShopManager.GRAVEYARD_STORE || shopId == ShopManager.TOKKUL_EXCHANGE_STORE || shopId == ShopManager.SLAYER_STORE || shopId == ShopManager.DONATION_POINTS_STORE || shopId == ShopManager.MEMBERS_STORE_II || shopId == ShopManager.SANTAS_SNOWFLAKE_SHOP || shopId == ShopManager.SLAYER_REWARDS_SHOP || shopId == ShopManager.WILDERNESS_SLAYER_REWARDS_SHOP || shopId == ShopManager.STRANGE_SKILL_ROCK_SHOP)
            return false;
        Shop shop = ShopManager.getShops().get(shopId);
        if (shop != null && shop.getOriginalStock() != null) {
            for (Item it : shop.getOriginalStock()) {
                if (it != null && it.getId() == item.getId())
                    return true;
            }
        }
        return false;
    }

    public void checkBuyValue(Player player, int slot) {
        this.setPlayer(player);

        Item item = get(slot);

        if (item == null)
            return;

        if (!player.isShopping()) {
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        String name = item.getDefinition().name;
        StringBuilder builder = new StringBuilder();
        builder.append(name + " currently costs ");

        if (currency.getId() != -1) {
            int value = getCurrencyValue(item);
            String currency = getCurrencyName(item);

            builder.append(value + " " + currency + "" + shopPriceEx(value) + ".");
        } else {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return;
            int value = (int) obj[0];
            builder.append(value + " " + obj[1] + ".");
        }

        if (player != null)
            player.message(builder.toString());
    }

    public void sellItem(Player player, int slot, int amountToSell) {
        sellItem(player, slot, amountToSell, true);
    }

    public void sellItem(Player player, int slot, int amountToSell, boolean refresh) {
        this.setPlayer(player);
        if (!player.isShopping() || player.isBanking()) {
            if (refresh)
                player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        if (id == ShopManager.GENERAL_STORE && GameSettings.HOSTED) {
            if (player.getRights() == PlayerRights.OWNER || player.getRights() == PlayerRights.DEVELOPER) {
                player.message("You cannot sell items.");
                return;
            }
        }

        if (!player.isShopping() || player.isBanking()) {//TODO duplicate
            if (refresh)
                player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        Item item = player.getInventory().get(slot);

        if (item == null)
            return;

        if (!shopBuysItem(id, item)) {
            player.message("You cannot sell this item to this store.");
            return;
        }

        if (!isDungeoneeringShop() && full(item))
            return;

        int currentAmount = player.getInventory().getAmount(item);
        if (currentAmount < amountToSell)
            amountToSell = currentAmount;

        if (amountToSell == 0)
            return;

        String name = item.getDefinition().getName();

        if (amountToSell > 5000) {
            String s = name.endsWith("s") ? name : name + "s";
            player.message("You can only sell 5000 " + s + " at a time.");
            return;
        }

        if (!player.getControllerManager().canSellItemShop(player, item, amountToSell, id))
            return;

        boolean customShop = this.currency.getId() == -1;
        boolean inventorySpace = customShop;

        if (!customShop) {
            if (!item.getDefinition().stackable) {
                if (!player.getInventory().contains(this.currency))
                    inventorySpace = true;
            }
            if (player.getInventory().getSpaces() <= 0 && player.getInventory().getAmount(this.currency) > 0)
                inventorySpace = true;
            if (player.getInventory().getSpaces() > 0 || player.getInventory().getAmount(this.currency) > 0)
                inventorySpace = true;
        }
        int itemValue = 0;
        if (currency.getId() > 0) {
            itemValue = Loader.getItem(item.getId()).getValue();
        } else {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return;
            itemValue = (int) obj[0];
        }
        if (itemValue <= 0)
            return;
        itemValue = (int) (itemValue * 0.85);
        if (itemValue <= 0) {
            itemValue = 1;
        }

        boolean addToShop = !isDungeoneeringShop();
        for (int i = amountToSell; i > 0; i--) {
            if ((addToShop && full(item)) || !player.isShopping())
                break;
            if (!item.getDefinition().isStackable()) {
                if (inventorySpace) {
                    if (addToShop)
                        player.getInventory().move(new Item(item, 1), this);
                    else
                        player.getInventory().delete(new Item(item, 1));
                    if (!customShop)
                        player.getInventory().add(new Item(currency, itemValue));
                } else {
                    player.message("Please free some inventory space before doing that.");
                    break;
                }
            } else {
                if (inventorySpace) {
                    if (addToShop)
                        player.getInventory().move(new Item(item, amountToSell), this);
                    else
                        player.getInventory().delete(new Item(item, amountToSell));
                    if (!customShop)
                        player.getInventory().add(new Item(currency, itemValue * amountToSell));
                    Achievements.finishAchievement(player, Achievements.AchievementData.SELL_AN_ITEM);
                    break;
                } else {
                    player.message("Please free some inventory space before doing that.");
                    break;
                }
            }
            amountToSell--;
        }
        player.getLogger().addSold(item, getId(), itemValue);

        if (customShop && refresh)
            player.getPointsHandler().refreshPanel();
        player.getInventory().refresh();
        fireRestockTask();
        if (refresh)
            refresh();
        publicRefresh();
    }

    public Shop switchItem(StackContainer to, Item item, int slot, boolean sort, boolean refresh) {
        Item shopItem = getItems()[slot];

        if (player == null)
            return this;
        if (!player.isShopping() || player.isBanking()) {
            player.getPacketSender().sendInterfaceRemoval();
            return this;
        }
        //Won't get here cause ironman can't open?
        if (this.id == ShopManager.GENERAL_STORE || id == ShopManager.MERCHANT_STORE) {
            if (player.getGameMode().isIronMan()) {
                player.message("Ironman-players are not allowed to buy items from the " + (id == ShopManager.MERCHANT_STORE ? "Merchants" : "General") + " store.");
                return this;
            }
        }

        if (!contains(item))
            return this;

        if (shopItem.getAmount() < 1 && id != ShopManager.GENERAL_STORE) {
            player.message("The shop has run out of stock for this item.");
            return this;
        }
        if (id == 89 || id == 2 && shopItem.getId() == 1115)
            Achievements.finishAchievement(player, Achievements.AchievementData.BUY_AN_IRON_PLATEBODY);
        else if (id == 4 && shopItem.getId() == 1540)
            Achievements.finishAchievement(player, Achievements.AchievementData.BUY_A_ANTI_DRAGON_SHIELD);
        else if (id == 81 && shopItem.getId() == 1397)
            Achievements.finishAchievement(player, Achievements.AchievementData.BUY_AN_AIR_BATTLESTAFF);

        if (id == ShopManager.LEGENDS_SHOP) {
            if (!canBuyLegends(player, shopItem.getId()))
                return this;
        }

        if (id == 111) {
            if (shopItem.getId() >= 20494 && shopItem.getId() <= 20497 && !DonorStatus.isDonor(player, DonorStatus.RUBY_DONOR)) {
                player.message("You need to be at least a ruby donor in order to buy this.");
                return this;
            }
            if (shopItem.getId() >= 20498 && shopItem.getId() <= 20501 && !player.getSkills().isMaxed()) {
                player.message("You must have maximum level in all skills in order to buy this.");
                return this;
            }
        }
        if (id == ShopManager.ACHIEVEMENTS_STORE) {
            if (shopItem.getName().contains("1"))
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.EASY)) {
                    player.message("You need to have completed all easy achievements in order to buy this.");
                    return this;
                }
            if (shopItem.getName().contains("2"))
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.MEDIUM)) {
                    player.message("You need to have completed all medium achievements in order to buy this.");
                    return this;
                }
            if (shopItem.getName().contains("3"))
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.HARD)) {
                    player.message("You need to have completed all hard achievements in order to buy this.");
                    return this;
                }
            if (shopItem.getName().contains("4"))
                if (!Achievements.hasFinishedAll(player, Achievements.Difficulty.ELITE)) {
                    player.message("You need to have completed all elite achievements in order to buy this.");
                    return this;
                }
        }
        if (id == ShopManager.SLAYER_REWARDS_SHOP) {
            if (shopItem.getId() == 15220) {
                if (!player.getInventory().containsAmount(new Item(6737, shopItem.getAmount()))) {
                    player.message("You need a berserker ring to exchange for this.");
                    return this;
                }
            } else if (shopItem.getId() == 15019) {
                if (!player.getInventory().containsAmount(new Item(6733, shopItem.getAmount()))) {
                    player.message("You need an archers' ring to exchange for this.");
                    return this;
                }
            } else if (shopItem.getId() == 15018) {
                if (!player.getInventory().containsAmount(new Item(6731, shopItem.getAmount()))) {
                    player.message("You need a seers' ring to exchange for this.");
                    return this;
                }
            } else if (shopItem.getId() == 15020) {
                if (!player.getInventory().containsAmount(new Item(6735, shopItem.getAmount()))) {
                    player.message("You need a warriors ring to exchange for this.");
                    return this;
                }
            }
        }

        if (item.getAmount() > shopItem.getAmount())
            item.setAmount(shopItem.getAmount());
        int amountBuying = item.getAmount();
        if (amountBuying == 0)
            return this;
        if (amountBuying > 5000) {
            player.message("You can only buy 5000 " + item.getName() + "s at a time.");
            return this;
        }
        boolean customShop = currency.getId() == -1;
        boolean usePouch = false;
        int playerCurrencyAmount = 0;
        int value = item.getDefinitions().getValue();
        String currencyName = "";
        if (currency.getId() != -1) {
            playerCurrencyAmount = player.getInventory().getAmount(currency);
            currencyName = currency.getDefinition().getName().toLowerCase();
            if (currency.getId() == 995) {
                if (player.getMoneyPouch().getTotal() >= value) {
                    playerCurrencyAmount = player.getMoneyPouch().getAmountAsInt();
                    if (!(player.getInventory().getSpaces() == 0 && player.getInventory().getAmount(currency) == value)) {
                        usePouch = true;
                    }
                }
            } else if (currency.getId() == DungeonConstants.RUSTY_COINS) {
                usePouch = false;
            } else {
                if (isCurrencyShop())
                    value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
            }
        } else {
            Object[] obj = ShopManager.getCustomShopData(id, item.getId());
            if (obj == null)
                return this;
            value = (int) obj[0];
            currencyName = (String) obj[1];
            if (id == ShopManager.PKING_REWARDS_STORE) {
                playerCurrencyAmount = player.getPoints().getInt(Points.PK);
            } else if (id == ShopManager.VOTING_REWARDS_STORE || id == ShopManager.IRONMAN_VOTING_REWARDS_STORE) {
                playerCurrencyAmount = player.getPoints().getInt(Points.VOTING);
            } else if (id == ShopManager.DUNGEONEERING_STORE) {
                playerCurrencyAmount = player.getDungManager().getTokens();
            } else if (id == ShopManager.SLAYER_STORE) {
                playerCurrencyAmount = player.getPoints().getInt(Points.SLAYER);
            } else if (id == ShopManager.DONATION_POINTS_STORE || id == ShopManager.MEMBERS_STORE_II) {
                playerCurrencyAmount = player.getPoints().getInt(Points.DONOR);
            } else if (id == ShopManager.SANTAS_SNOWFLAKE_SHOP) {
                playerCurrencyAmount = player.getInventory().getAmount(new Item(33596, Revision.RS3));
            } else if (id == ShopManager.SLAYER_REWARDS_SHOP || id == ShopManager.WILDERNESS_SLAYER_REWARDS_SHOP) {
                playerCurrencyAmount = player.getPoints().getInt(Points.SLAYER);
            } else if (id == ShopManager.STRANGE_SKILL_ROCK_SHOP) {
                playerCurrencyAmount = player.getPoints().getInt(Points.STRANGE_SKILL);
            }
        }
        if (value <= 0) {
            return this;
        }
        if (!hasInventorySpace(player, item, currency.getId(), value)) {
            player.message("You do not have any free inventory slots.");
            return this;
        }
        if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
            player.message("You do not have enough " + ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s"))) + " to purchase this item.");
            return this;
        }
        if (id == ShopManager.SKILLCAPE_STORE_HOODS || id == ShopManager.SKILLCAPE_STORE || id == ShopManager.SKILLCAPE_STORE_T) {
            HashMap<Integer, Integer> requirements = item.getDefinition().getSkillRequirements();
            if (requirements != null) {
                for (int index : requirements.keySet()) {
                    Skill skill = Skill.values()[index];
                    int requirement = requirements.get(index);

                    if (!player.getSkills().hasMaxRequirement(skill, requirement, "buy this item"))
                        return this;
                }
            }
        }
        if (id == ShopManager.MASTER_CAPE_STORE) {
            HashMap<Integer, Integer> requirements = item.getDefinition().getSkillRequirements();
            if (requirements != null) {
                for (int index : requirements.keySet()) {
                    Skill skill = Skill.values()[index];

                    if (!player.getSkills().hasExpRequirement(skill, 200000000, "buy this item"))
                        return this;
                }
            }
        }

        for (int i = amountBuying; i > 0; i--) {
            if (!contains(item)) {
                break;
            }
            if (shopItem.getAmount() < 1 && id != ShopManager.GENERAL_STORE) {
                player.message("The shop has run out of stock for this item.");
                break;
            }
            if (!item.getDefinition().isStackable()) {
                if (playerCurrencyAmount >= value && hasInventorySpace(player, item, currency.getId(), value)) {

                    if (!customShop) {
                        if (usePouch) {
                            player.getMoneyPouch().setTotal((player.getMoneyPouch().getTotal() - value));
                        } else {
                            player.getInventory().delete(new Item(currency, value));
                        }
                    } else {
                        if (id == ShopManager.PKING_REWARDS_STORE) {
                            player.getPoints().remove(Points.PK, value);
                        } else if (id == ShopManager.VOTING_REWARDS_STORE || id == ShopManager.IRONMAN_VOTING_REWARDS_STORE) {
                            player.getPoints().remove(Points.VOTING, value);
                        } else if (id == ShopManager.DUNGEONEERING_STORE) {
                            player.getDungManager().setTokens(player.getDungManager().getTokens() - value);
                        } else if (id == ShopManager.SLAYER_STORE) {
                            player.getPoints().remove(Points.SLAYER, value);
                        } else if (id == ShopManager.DONATION_POINTS_STORE || id == ShopManager.MEMBERS_STORE_II) {
                            player.getPoints().remove(Points.DONOR, value);
                        } else if (id == ShopManager.SANTAS_SNOWFLAKE_SHOP) {
                            player.getInventory().delete(new Item(33596, value, Revision.RS3));
                        } else if (id == ShopManager.SLAYER_REWARDS_SHOP || id == ShopManager.WILDERNESS_SLAYER_REWARDS_SHOP) {
                            player.getPoints().remove(Points.SLAYER, value);
                            if (shopItem.getId() == 15220) {
                                player.getInventory().delete(new Item(6737));
                            } else if (shopItem.getId() == 15019) {
                                player.getInventory().delete(new Item(6733));
                            } else if (shopItem.getId() == 15018) {
                                player.getInventory().delete(new Item(6731));
                            } else if (shopItem.getId() == 15020) {
                                player.getInventory().delete(new Item(6735));
                            }
                        } else if (id == ShopManager.STRANGE_SKILL_ROCK_SHOP) {
                            player.getPoints().remove(Points.STRANGE_SKILL, value);
                        }
                    }

                    move(new Item(item, 1), to);

                    playerCurrencyAmount -= value;
                } else {
                    break;
                }
            } else {
                if (playerCurrencyAmount >= value && hasInventorySpace(player, item, currency.getId(), value)) {

                    int canBeBought = playerCurrencyAmount / (value);

                    if (canBeBought >= amountBuying)
                        canBeBought = amountBuying;

                    if (canBeBought == 0)
                        break;

                    if (!customShop) {
                        if (usePouch) {
                            player.getMoneyPouch().setTotal((player.getMoneyPouch().getTotal() - (value * canBeBought)));
                        } else {
                            player.getInventory().delete(new Item(currency, value * canBeBought));
                        }
                    } else {
                        if (id == ShopManager.PKING_REWARDS_STORE) {
                            player.getPoints().remove(Points.PK, value * canBeBought);
                        } else if (id == ShopManager.VOTING_REWARDS_STORE || id == ShopManager.IRONMAN_VOTING_REWARDS_STORE) {
                            player.getPoints().remove(Points.VOTING, value * canBeBought);
                        } else if (id == ShopManager.DUNGEONEERING_STORE) {
                            player.getDungManager().setTokens(player.getDungManager().getTokens() - (value * canBeBought));
                        } else if (id == ShopManager.SLAYER_STORE) {
                            player.getPoints().remove(Points.SLAYER, value * canBeBought);
                        } else if (id == ShopManager.DONATION_POINTS_STORE || id == ShopManager.MEMBERS_STORE_II) {
                            player.getPoints().remove(Points.DONOR, value * canBeBought);
                        } else if (id == ShopManager.SANTAS_SNOWFLAKE_SHOP) {
                            player.getInventory().delete(new Item(33596, (value * canBeBought), Revision.RS3));
                        } else if (id == ShopManager.SLAYER_REWARDS_SHOP || id == ShopManager.WILDERNESS_SLAYER_REWARDS_SHOP) {
                            player.getPoints().remove(Points.SLAYER, value * canBeBought);
                            if (shopItem.getId() == 15220) {
                                player.getInventory().delete(new Item(6737, canBeBought));
                            } else if (shopItem.getId() == 15019) {
                                player.getInventory().delete(new Item(6733, canBeBought));
                            } else if (shopItem.getId() == 15018) {
                                player.getInventory().delete(new Item(6731, canBeBought));
                            } else if (shopItem.getId() == 15019) {
                                player.getInventory().delete(new Item(6735, canBeBought));
                            }
                        } else if (id == ShopManager.STRANGE_SKILL_ROCK_SHOP) {
                            player.getPoints().remove(Points.STRANGE_SKILL, value * canBeBought);
                        }
                    }
                    move(new Item(item, canBeBought), to);
                    playerCurrencyAmount -= value;
                    break;
                } else {
                    break;
                }
            }
            amountBuying--;
        }

        if (customShop)
            player.getPointsHandler().refreshPanel();
        else if (usePouch)
            player.getPacketSender().sendString(8135, "" + player.getMoneyPouch().getTotal()); //Update the money pouch

        player.getInventory().refresh();
        fireRestockTask();
        refresh();
        publicRefresh();
        return this;
    }

    public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
        if (player.getInventory().getSpaces() >= 1) {
            return true;
        }
        if (item.getDefinition().stackable) {
            if (player.getInventory().contains(item)) {
                return true;
            }
        }
        if (currency != -1) {
            if (player.getInventory().getSpaces() == 0 && player.getInventory().getAmount(new Item(currency)) == pricePerItem) {
                return true;
            }
        }
        return false;
    }

    public void fireRestockTask() {
        if (isRestockingItems() || fullyRestocked())
            return;
        setRestockingItems(true);
        GameWorld.schedule(new ShopRestockTask(this));
    }

    public boolean fullyRestocked() {
        if (id == ShopManager.GENERAL_STORE) {
            return getItemTotal() == 0;
        } else if (id == ShopManager.RECIPE_FOR_DISASTER_STORE) {
            return true;
        }
        if (getOriginalStock() != null) {
            for (int index = 0; index < getOriginalStock().length; index++) {
                Item original = originalStock[index];
                Item current = items[index];
                if (original != null && current != null) {
                    if (original.getAmount() != current.getAmount())
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean set(Item item, int index) {
        if (item == null || indexOutOfBounds(index))
            return false;

        if (id != ShopManager.GENERAL_STORE) {
            items[index] = item.getAmount() >= 0 ? item.copy() : null;
        } else {
            items[index] = item.getAmount() > 0 ? item.copy() : null;
        }
        return true;
    }

    private boolean canBuyLegends(Player player, int selectedItem) {
        if (!player.getGameMode().equals(GameMode.LEGEND)) {
            player.getPacketSender().sendMessage("This is a legends only shop!", 255);
            return false;
        }
        switch (selectedItem) {
            case 6339:
                if (player.getTotalPlayTime() < 18_000_000) {
                    player.getPacketSender().sendMessage("You need a total playtime of five hours, to buy this item.", 255);
                    return false;
                }
                return true;
            case 6279:
                if (player.getTotalPlayTime() < 10_800_000) {
                    player.getPacketSender().sendMessage("You need a total playtime of three hours, to buy this item.", 255);
                    return false;
                }
                return true;
            case 1052:
                if (player.getTotalPlayTime() < 3_600_000) {
                    player.getPacketSender().sendMessage("You need a total playtime of an hour, to buy this item.", 255);
                    return false;
                }
                return true;
            case 20857:
                return true;
        }
        return false;
    }

    public boolean isRestockingItems() {
        return restockingItems;
    }

    public void setRestockingItems(boolean restockingItems) {
        this.restockingItems = restockingItems;
    }

    private boolean full(Item item) {
        return getSpaces() <= 0 && !(contains(item) && item.getDefinition().stackable);
    }

    @Override
    public String getFullMessage() {
        return "The shop is currently full. Please come back later.";
    }

    public int getDungeoneeringSplit() {
        return dungeoneeringSplit;
    }

    public void setDungeoneeringSplit(int dungeoneeringSplit) {
        this.dungeoneeringSplit = dungeoneeringSplit;
    }

    public Item getCurrency() {
        return currency;
    }

    public Item[] getOriginalStock() {
        return originalStock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
