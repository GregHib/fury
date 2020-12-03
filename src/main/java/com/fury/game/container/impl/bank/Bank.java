package com.fury.game.container.impl.bank;

import com.fury.cache.def.item.ItemDefinition;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.container.impl.equip.Equipment;
import com.fury.game.container.types.StackContainer;
import com.fury.game.content.global.Achievements;
import com.fury.game.entity.character.player.content.BankPin;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.info.DonorStatus;
import com.fury.core.model.item.Item;

public class Bank {

    private final int maxNumberOfTabs = 9;
    private int bankCapacity = 352;
    public static final int INTERFACE_ID = 5382;
    public static final int BANK_TAB_INTERFACE_ID = 5383;
    public static final int INVENTORY_INTERFACE_ID = 5064;

    private BankTab[] tabs = new BankTab[maxNumberOfTabs];
    private PlaceHolders placeHolders = new PlaceHolders();
    private BankSearch search = new BankSearch(this);

    protected boolean withdrawNotes = false;
    protected boolean alwaysPlaceholder = false;
    protected boolean searching = false;
    private int withdrawAmount = 100;
    private int currentTab = 0;
    private Player player;
    private boolean swapMode = true;

    public Bank(Player player, Bank bank) {
        this.player = player;
        search.setPlayer(player);
        bankCapacity = bank.getCapacity();
        refreshCapacity();
        setTabCount(bank.getTabCount());
        for (int i = 0; i < getTabCount(); i++) {
            tabs[i] = new BankTab(player, bankCapacity, this);
            tab(i).setItems(bank.tab(i).getItems().clone());
        }
    }

    public Bank(Player player) {
        this.player = player;
        search.setPlayer(player);
        setTabCount(1);
    }

    public void refreshCapacity() {
        int old = bankCapacity;
        bankCapacity = 352 + DonorStatus.get(player).getExtraBankSpace();
        if(old != bankCapacity)
            for (int tab = 0; tab < getTabCount(); tab++)
                tab(tab).extend(bankCapacity);
    }

    public final void open() {
        player.getPacketSender().sendClientRightClickRemoval();
        if (player.getBankPinAttributes().hasBankPin() && !player.getBankPinAttributes().hasEnteredBankPin()) {
            BankPin.init(player, () -> player.getBank().open());
            return;
        }

        refresh();
        player.setBanking(true);
        player.setInputHandling(null);
        player.getPacketSender().sendConfig(1164, alwaysPlaceholder ? 1 : 0);
        player.getPacketSender().sendConfig(115, withdrawNotes ? 1 : 0).sendConfig(304, isSwapMode() ? 0 : 1).sendConfig(117, searching ? 1 : 0).sendConfig(1165, withdrawAmount).sendInterfaceSet(5292, 5063);
    }

    public final void refresh() {
        if (player == null || !player.getSession().isPresent())
            return;
        sort();
        //Send details
        player.getPacketSender().sendString(22033, "" + getItemTotal());
        player.getPacketSender().sendString(22034, "" + bankCapacity);
        player.getPacketSender().sendItemContainer(player.getInventory(), 5064);
        if (!player.isBanking() || player.getInterfaceId() != 5292)
            player.getPacketSender().sendClientRightClickRemoval();

        //Send tabs
        refreshTabs();

        //Send items
        if (searching) {
            search.refresh();
            return;
        } else if (currentTab == 0) {
            if (getTabCount() > 1) {
                Item[] items = concatTabs();
                player.getPacketSender().sendItemContainer(items, 5382);
                int last = 0;
                for (int i = items.length - 1; i > 0; i--)
                    if (items[i] != null) {
                        last = i;
                        break;
                    }
                player.getPacketSender().sendInterfaceComponentScrollMax(5385, (int) Math.round(last * 4.5));
                return;
            }
        }

        player.getPacketSender().sendItemContainer(tab(), 5382);
        player.getPacketSender().sendInterfaceComponentScrollMax(5385, (int) Math.round(tab().getItemTotal() * 4.5));
    }

    public final void refreshTabs() {
        player.getPacketSender().sendString(27001, Integer.toString(getTabCount() - 1)).sendString(27002, Integer.toString(currentTab));
        Item[] tabItems = new Item[maxNumberOfTabs];
        for (int i = 1; i < maxNumberOfTabs; i++)
            tabItems[i - 1] = new Item(tabExists(i) ? getInterfaceModel(tab(i)) : -1, 1);
        player.getPacketSender().sendItemContainer(tabItems, 22035);
        player.getPacketSender().sendString(27000, "1");//Update
    }

    private final Item[] concatTabs() {
        Item[] temp = new Item[bankCapacity + 19 * maxNumberOfTabs];//Why 19?
        int index = 0;
        for (int tab = 0; tab < getTabCount(); tab++) {
            for (Item item : tab(tab).getItems()) {
                if (item != null)
                    temp[index++] = item;
            }
            index += (index % 10) > 0 ? (10 - (index % 10)) : 0;//Number of empty spaces till end of line
            index += 10;//a whole empty line
        }
        Item[] items = new Item[index];
        System.arraycopy(temp, 0, items, 0, index);
        return items;
    }

    public final int[] isolateTab(int toSlot) {
        int total = player.getBank().tab(0).getItemTotal();
        int offset = toSlot - total;
        int previousAmount = total;
        for (int tab = 1; tab < player.getBank().getTabCount(); tab++) {
            int overhead = (previousAmount % 10) > 0 ? (10 - (previousAmount % 10)) : 0;
            offset -= 10 + overhead;

            int index = player.getBank().tab(tab).getItemTotal();
            if (offset >= 0 && offset <= index)
                return new int[]{tab, offset};
            offset -= index;
            previousAmount = index;
        }
        return null;
    }

    public boolean swap(int tabIndex1, int index1, int tabIndex2, int index2) {
        return tab(tabIndex1).swap(index1, tab(tabIndex2), index2);
    }

    public boolean insert(int tabIndex1, int index1, int tabIndex2, int index2) {
        if (tabIndex1 != tabIndex2) {
            BankTab tab = tab(tabIndex2);
            int newIndex = tab.getFreeIndex();
            tab(tabIndex1).move(index1, tab, newIndex);
            return tab(tabIndex1).insert(newIndex, tab(tabIndex2), index2);
        } else
            return tab(tabIndex1).insert(index1, tab(tabIndex2), index2);
    }

    public final boolean addTab(int fromTab, int slot, int toTab) {
        if (getTabCount() >= maxNumberOfTabs)
            return false;

        Item item = player.getBank().tab(fromTab).get(slot);

        if (item == null)
            return false;

        if (tabExists(toTab))
            return false;

        if(toTab - getTabCount() >= 1)
            return false;

        tabs[toTab] = new BankTab(player, bankCapacity, this);
        tab(fromTab).move(item, tab(toTab));
        if (currentTab != 0)
            setCurrentTab(toTab);
        checkCollapse(fromTab);
        return true;
    }

    public final void checkCollapse(int tab) {
        if (player.getBank().tab(tab).getItemTotal() == 0)
            player.getBank().collapse(tab);
    }

    public final boolean collapse(int index) {
        int tabCount = getTabCount();
        if (tabCount == 1 || index <= 0)
            return false;

        //Collapse tab
        if (tab(index).getItemTotal() != 0) {
            for (Item item : tab(index).getItems()) {
                if (item == null)
                    continue;
                BankTab newTab = tab(index - 1);
                tab(index).move(item, newTab);
            }
        }

        //Remove tab
        tabs[index] = null;

        //Shift tabs in front back one
        if (index != tabCount) {
            for (int i = index; i < tabCount; i++)
                if (i != tabCount - 1)
                    tabs[i] = tabs[i + 1];
            tabs[tabCount - 1] = null;
        }

        if (currentTab >= getTabCount() - 1)
            setCurrentTab(getTabCount() - 1);
        return true;
    }

    /**
     * Tab setters
     */

    public final void setCurrentTab(int index) {
        if (tabOutOfBounds(index))
            return;
        if (player != null && currentTab != index)
            player.getPacketSender().sendInterfaceComponentScrollPosition(5385, 0);
        currentTab = index;
        refresh();
    }

    /**
     * Tab checks
     */

    public final boolean tabExists(int index) {
        if (tabOutOfBounds(index))
            return false;
        return tabs[index] != null;
    }

    public final boolean tabOutOfBounds(int index) {
        return index < 0 || index >= getTabCount();//TODO > or >=?
    }

    public final boolean contains(Item item) {
        if (item == null)
            return false;

        for (int i = 0; i < getTabCount(); i++)
            if (tab(i).contains(item))
                return true;
        return false;
    }

    /**
     * Tab getters
     */

    public Item get(Item item) {
        if (item == null)
            return null;
        for (int i = 0; i < getTabCount(); i++)
            if (tab(i).contains(item))
                return tab(i).get(item);
        return null;
    }

    public final int getItemTotal() {
        int total = 0;
        for (int i = 0; i < getTabCount(); i++)
            total += tab(i).getItemTotal();
        return total;
    }

    public final int getTabCount() {
        int count = 0;
        for (int i = 0; i < maxNumberOfTabs; i++)
            if (tabs[i] != null)
                count++;
        return count;
    }

    public final BankTab tab() {
        if (currentTab > getTabCount() || currentTab < 0)
            return null;
        return tab(currentTab);
    }

    public final BankTab tab(int index) {
        if (tabOutOfBounds(index))
            return null;
        return tabs[index];
    }

    public final BankTab getTab(Item item) {
        for (int i = 0; i < getTabCount(); i++)
            if (tab(i).contains(item))
                return tab(i);
        return tab();
    }

    public final int getTabIndex(Item item) {
        for (int i = 0; i < getTabCount(); i++)
            if (tab(i).contains(item))
                return i;
        return -1;
    }

    /**
     * Bank checks
     */

    public final boolean banking() {
        if (player != null && player.isBanking() && player.getInterfaceId() == 5292)
            return true;
        return false;
    }

    public final boolean isFull() {
        if (getItemTotal() == bankCapacity)
            return true;
        return false;
    }

    /**
     * Junk to sort out
     */
    public static final int getInterfaceModel(BankTab tab) {
        if (tab.getItems()[0] == null || tab.getItemTotal() == 0)
            return -1;
        Item item = tab.getItems()[0];
        ItemDefinition def = item.getDefinition();
        if (def.stackAmounts != null)
            def = def.morph(item.getAmount());
        return def.id;
    }

    public final void sort() {
        for (int i = 0; i < getTabCount(); i++)
            tab(i).sort();
    }

    public boolean handleButton(int id) {
        if (!player.isBanking())
            return false;
        if (id >= 27014 && id <= 27022) {
            if (currentTab != id - 27014)
                setCurrentTab(id - 27014);
            if (searching)
                search.stop();
            return true;
        } else if (id == 18986) {
            player.setBanking(false);
            player.getPacketSender().sendInterfaceRemoval();
            player.getPacketSender().sendInterface(21172);
            BonusManager.update(player);
        } else if (id == 22008) {
            withdrawNotes = !withdrawNotes;
            player.getPacketSender().sendConfig(115, withdrawNotes ? 1 : 0);
        } else if (id == 22009) {
            alwaysPlaceholder = !alwaysPlaceholder;
            if (!alwaysPlaceholder) {
                removePlaceholders();
                for (int i = 1; i < getTabCount(); i++)
                    checkCollapse(i);
                refresh();
            }
            player.getPacketSender().sendConfig(1164, alwaysPlaceholder ? 1 : 0);
        } else if (id == 27005) {//equip
            if (deposit(player.getEquipment())) {
                Equipment.resetWeapon(player);
                Achievements.finishAchievement(player, Achievements.AchievementData.DEPOSIT_ALL);
            }
        } else if (id == 22012) {//inv
            if (deposit(player.getInventory()))
                Achievements.finishAchievement(player, Achievements.AchievementData.DEPOSIT_ALL);
        } else if (id == 27009) {
            if(!player.isCommandViewing())
                player.getMoneyPouch().bank();
        } else if (id == 27023) {
            if (player.getFamiliar() == null) {
                player.message("You don't have a familiar.");
                return true;
            }

            if (player.getFamiliar().getBeastOfBurden() == null) {
                player.message("Your familiar is not capable of carrying items.");
                return true;
            }
            if (deposit(player.getFamiliar().getBeastOfBurden()))
                Achievements.finishAchievement(player, Achievements.AchievementData.DEPOSIT_ALL);
        }
        if (search.handleButton(id))
            return true;
        return false;
    }

    /**
     * Bank searching
     */

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public boolean isSearching() {
        return searching;
    }

    private void removePlaceholders() {
        for (int tab = 0; tab < getTabCount(); tab++) {
            for (int index = 0; index < tab(tab).capacity(); index++) {
                Item item = tab(tab).get(index);
                //We only want to remove placeholders made by "always placeholder"
                if (item != null && item.getAmount() == 0 && !placeHolders.contains(item))
                    tab(tab).delete(index);
            }
        }
    }

    public boolean swapMode() {
        return swapMode;
    }

    public void setSwapMode(boolean swapMode) {
        this.swapMode = swapMode;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public PlaceHolders getPlaceHolders() {
        return placeHolders;
    }

    public boolean deposit(Item item, StackContainer from) {
        //Depositing notes
        if (item.getDefinition().noteId != -1 && item.getDefinition().noted) {
            Item unnoted = new Item(item.getDefinition().noteId, item);
            if(contains(unnoted)) {
                BankTab tab = getTab(unnoted);
                boolean success = from.move(item, tab);
                if (currentTab != 0)
                    setCurrentTab(getTabIndex(unnoted));
                return success;
            }
        }

        if(item.getDefinition().noted || item.getId() == 4717)
            item = new Item(item.getId(), item.getAmount(), item.getRevision());

        if (contains(item)) {
            BankTab tab = getTab(item);
            boolean success = from.move(item, tab);
            if (currentTab != 0)
                setCurrentTab(getTabIndex(item));
            return success;
        } else {
            return from.move(item, tab());
        }
    }

    public boolean deposit(Item item) {
        //Depositing notes
        if (item.getDefinition().noteId != -1 && item.getDefinition().noted) {
            Item unnoted = new Item(item.getDefinition().noteId, item);
            if(contains(unnoted)) {
                BankTab tab = getTab(unnoted);
                boolean success = tab.add(item);
                if (currentTab != 0)
                    setCurrentTab(getTabIndex(unnoted));
                return success;
            }
        }

        if (contains(item)) {
            BankTab tab = getTab(item);
            boolean success = tab.add(item);
            if (currentTab != 0)
                setCurrentTab(getTabIndex(item));
            return success;
        } else {
            return tab().add(item);
        }
    }

    public final boolean deposit(StackContainer container) {
        if (!banking() || player.isCommandViewing())
            return false;
        for (Item item : container.getItems()) {
            if (item != null)
                deposit(item, container);
        }
        container.refresh();
        refresh();
        return true;
    }

    public int getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(int withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
        if (banking())
            player.getPacketSender().sendConfig(1165, withdrawAmount);
    }

    public BankSearch getSearch() {
        return search;
    }

    public boolean isWithdrawNotes() {
        return withdrawNotes;
    }

    public boolean isSwapMode() {
        return swapMode;
    }

    public void setWithdrawNotes(boolean withdrawNotes) {
        this.withdrawNotes = withdrawNotes;
    }

    public boolean isAlwaysPlaceholder() {
        return alwaysPlaceholder;
    }

    public void setAlwaysPlaceholder(boolean alwaysPlaceholder) {
        this.alwaysPlaceholder = alwaysPlaceholder;
    }

    public void setTabCount(int tabCount) {
        for (int i = 0; i < tabCount; i++)
            tabs[i] = new BankTab(player, bankCapacity, this);
    }

    public int getCapacity() {
        return bankCapacity;
    }
}
