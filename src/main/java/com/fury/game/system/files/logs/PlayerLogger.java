package com.fury.game.system.files.logs;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerLogger {
    private static final int LOG_CAPACITY = 100;
    private Player player;
    private Map<String, Integer> computers = new HashMap<>();
    private Map<String, Integer> ipAddresses = new HashMap<>();
    private Map<String, Integer> macAddresses = new HashMap<>();

    private List<LoggedItem> npcDrops = new ArrayList<>();
    private List<LoggedItem> itemsEmptied = new ArrayList<>();
    private List<LoggedItem> deathItemDrops = new ArrayList<>();
    private List<LoggedShopItem> alchedItems = new ArrayList<>();
    private List<LoggedPlayerItem> groundItems = new ArrayList<>();
    private List<LoggedPlayerItem> itemsTraded = new ArrayList<>();
    private List<LoggedPlayerItem> itemsStaked = new ArrayList<>();
    private List<LoggedShopItem> itemsSold = new ArrayList<>();

    private List<LoggedMessage> privateMessages = new ArrayList<>();
    private List<LoggedMessage> chatMessages = new ArrayList<>();
    private List<LoggedMessage> clanMessages = new ArrayList<>();

    public PlayerLogger(Player player) {
        this.player = player;
    }

    public Map<String, Integer> getComputers() {
        return computers;
    }

    public Map<String, Integer> getIpAddresses() {
        return ipAddresses;
    }

    public Map<String, Integer> getMacAddresses() {
        return macAddresses;
    }

    public List<LoggedItem> getNpcDrops() {
        return npcDrops;
    }

    public List<LoggedShopItem> getAlchedItems() {
        return alchedItems;
    }

    public List<LoggedItem> getItemsEmptied() {
        return itemsEmptied;
    }

    public List<LoggedItem> getDeathItemDrops() {
        return deathItemDrops;
    }

    public List<LoggedPlayerItem> getGroundItems() {
        return groundItems;
    }

    public List<LoggedPlayerItem> getTradedItems() {
        return itemsTraded;
    }

    public List<LoggedPlayerItem> getStakedItems() {
        return itemsStaked;
    }

    public List<LoggedShopItem> getSoldItems() {
        return itemsSold;
    }

    public List<LoggedMessage> getPrivateMessages() {
        return privateMessages;
    }

    public List<LoggedMessage> getChatMessages() {
        return chatMessages;
    }

    public List<LoggedMessage> getClanMessages() {
        return clanMessages;
    }

    public void setAlchedItems(List<LoggedShopItem> alchedItems) {
        this.alchedItems.addAll(alchedItems);
    }

    public void setNpcDrops(List<LoggedItem> npcDrops) {
        this.npcDrops.addAll(npcDrops);
    }

    public void setItemsEmptied(List<LoggedItem> itemsEmptied) {
        this.itemsEmptied.addAll(itemsEmptied);
    }

    public void setDeathItemDrops(List<LoggedItem> deathItemDrops) {
        this.deathItemDrops.addAll(deathItemDrops);
    }

    public void setGroundItems(List<LoggedPlayerItem> groundItems) {
        this.groundItems.addAll(groundItems);
    }

    public void setItemsTraded(List<LoggedPlayerItem> itemsTraded) {
        this.itemsTraded.addAll(itemsTraded);
    }

    public void setItemsStaked(List<LoggedPlayerItem> itemsStaked) {
        this.itemsStaked.addAll(itemsStaked);
    }

    public void setItemsSold(List<LoggedShopItem> itemsSold) {
        this.itemsSold.addAll(itemsSold);
    }

    public void setPrivateMessages(List<LoggedMessage> privateMessages) {
        this.privateMessages.addAll(privateMessages);
    }

    public void setChatMessages(List<LoggedMessage> chatMessages) {
        this.chatMessages.addAll(chatMessages);
    }

    public void setClanMessages(List<LoggedMessage> clanMessages) {
        this.clanMessages.addAll(clanMessages);
    }

    //Old?
    private String macAddress;
    private String ipAddress;
    private String hardwareId;

    public String getIpAddress() {
        if(!player.getSession().isPresent() || ipAddress == null)
            return getAverageIpAddress();
        return ipAddress;
    }

    public String getHardwareId() {
        if(!player.getSession().isPresent() || hardwareId == null)
            return getAverageHardwareId();
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        if(!player.getSession().isPresent() || macAddress == null)
            return getAverageMacAddress();
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void addAlch(Item item, int value) {
        alchedItems.add(0, new LoggedShopItem(item, -1, value));
        if(alchedItems.size() > LOG_CAPACITY)
            alchedItems.remove(alchedItems.size() - 1);
    }

    public void addNpcDrop(Item item) {
        npcDrops.add(0, new LoggedItem(item));
        if(npcDrops.size() > LOG_CAPACITY)
            npcDrops.remove(npcDrops.size() - 1);
    }

    public void addEmpty(Item item) {
        itemsEmptied.add(0, new LoggedItem(item));
        if(itemsEmptied.size() > LOG_CAPACITY)
            itemsEmptied.remove(itemsEmptied.size() - 1);
    }

    public void addDeathDrop(Item item) {
        deathItemDrops.add(0, new LoggedItem(item));
        if(deathItemDrops.size() > LOG_CAPACITY)
            deathItemDrops.remove(deathItemDrops.size() - 1);
    }

    public void addDrop(Item item) {
        groundItems.add(0, new LoggedPlayerItem(item, player, false));
        if(groundItems.size() > LOG_CAPACITY)
            groundItems.remove(groundItems.size() - 1);
    }

    public void addPickup(Item item, Player owner) {
        groundItems.add(0, new LoggedPlayerItem(item, owner, true));
        if(groundItems.size() > LOG_CAPACITY)
            groundItems.remove(groundItems.size() - 1);
    }

    public void addTradeItem(Item item, Player with, boolean received) {
        itemsTraded.add(0, new LoggedPlayerItem(item, with, received));
        if(itemsTraded.size() > LOG_CAPACITY)
            itemsTraded.remove(itemsTraded.size() - 1);
    }

    public void addStaked(Item item, Player with, boolean received) {
        itemsStaked.add(0, new LoggedPlayerItem(item, with, received));
        if(itemsStaked.size() > LOG_CAPACITY)
            itemsStaked.remove(itemsStaked.size() - 1);
    }

    public void addSold(Item item, int shop, int price) {
        itemsSold.add(0, new LoggedShopItem(item, shop, price));
        if(itemsSold.size() > LOG_CAPACITY)
            itemsSold.remove(itemsSold.size() - 1);
    }

    public void addPrivateMessage(String message, Player friend) {
        privateMessages.add(0, new LoggedMessage(message, friend.getUsername()));
        if(privateMessages.size() > LOG_CAPACITY)
            privateMessages.remove(privateMessages.size() - 1);
    }

    public void addMessage(String message) {
        chatMessages.add(0, new LoggedMessage(message, ""));
        if(chatMessages.size() > LOG_CAPACITY)
            chatMessages.remove(chatMessages.size() - 1);
    }

    public void addClanMessage(String message, String clan) {
        clanMessages.add(0, new LoggedMessage(message, clan));
        if(clanMessages.size() > LOG_CAPACITY)
            clanMessages.remove(clanMessages.size() - 1);
    }

    public void addLogin() {
        if(computers.containsKey(hardwareId)) {
            int count = computers.get(hardwareId);
            computers.put(hardwareId, count + 1);
        } else
            computers.put(hardwareId, 1);

        checkComputers();
    }

    public Map<String, Integer> getComputersSorted() {
        return sortByValue(computers);
    }

    public Map<String, Integer> getIpsSorted() {
        return sortByValue(ipAddresses);
    }

    public Map<String, Integer> getMacsSorted() {
        return sortByValue(macAddresses);
    }

    private void checkComputers() {
        //Shouldn't have an impact on login as most people won't use more than 5 pc's
        computers = sortByValue(computers);
        String avg = getAverageHardwareId();
        if(avg != null) {
            if (!hardwareId.equals(avg)) {
                int avgAmount = computers.get(avg);
                int amount = computers.get(hardwareId);
//                if (avgAmount > 5 && amount < 5)
//                    System.out.println("New computer");
            }
        }
    }

    private String getAverageHardwareId() {
        Map<String, Integer> sorted = getComputersSorted();
        String[] keys = sorted.keySet().toArray(new String[sorted.keySet().size()]);
        return keys.length > 0 ? keys[0] : null;
    }

    private String getAverageIpAddress() {
        Map<String, Integer> sorted = getIpsSorted();
        String[] keys = sorted.keySet().toArray(new String[sorted.keySet().size()]);
        return keys.length > 0 ? keys[0] : null;
    }

    private String getAverageMacAddress() {
        Map<String, Integer> sorted = getMacsSorted();
        String[] keys = sorted.keySet().toArray(new String[sorted.keySet().size()]);
        return keys.length > 0 ? keys[0] : null;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void setComputers(Map<String, Integer> computers) {
        this.computers = computers;
    }

    public void setIPAddresses(Map<String, Integer> addresses) {
        this.ipAddresses = addresses;
    }

    public void setMacAddresses(Map<String, Integer> addresses) {
        this.macAddresses = addresses;
    }
}
