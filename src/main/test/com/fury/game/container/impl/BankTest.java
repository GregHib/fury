package com.fury.game.container.impl;

import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.game.container.Container;
import com.fury.game.container.impl.bank.Bank;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import org.junit.Before;
import org.junit.BeforeClass;

public class BankTest {

    Player player = new Player();
    Bank bank = new Bank(player);
    Container container = new Container(null,2);
    Item item = new Item(882);

    @BeforeClass
    public static void setUp(){
        try {
            GameLoader.getCache().init();
            Loader.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void set() {
        player.setBanking(true);
        player.setInterfaceId(5292);
    }

    /**
     * DepositAll
     */
    /*@Test
    public void testDepositAll() {
        assertTrue(container.add(new Item(2, 10)));
        assertTrue(container.add(new Item(3)));
        assertEquals(2, container.getValidIndices());

        assertTrue(bank.depositAll(container));
        assertEquals(0, container.getValidIndices());
        assertEquals(2, bank.getValidIndices());

        assertTrue(bank.clear());
    }

    @Test
    public void testDepositAllFull() {
        assertTrue(container.add(new Item(2, 10)));
        assertTrue(container.add(new Item(400)));
        assertEquals(2, container.getValidIndices());
        //Fill bank
        for(int i = 0; i < 352; i++)
            assertTrue(bank.add(new Item(i)));
        assertEquals(352, bank.getValidIndices());
        assertTrue(bank.depositAll(container));
        assertEquals(1, container.getValidIndices());//item id 2 already is in bank
    }
    *//**
     * Add
     *//*
    @Test
    public void testAdd() {
        player.setBanking(false);
        assertFalse(bank.add(item));
        player.setBanking(true);
        //Stackable
        assertTrue(bank.add(item));
        assertTrue(bank.add(item));
        assertEquals(item.getAmount() * 2, bank.get(item).getAmount());

        //Non stackable
        Item item = new Item(1);
        assertTrue(bank.add(item));
        assertTrue(bank.add(item));
        assertEquals(item.getAmount() * 2, bank.get(item).getAmount());

        //Bank full
        for(int i = 0; i <= 350; i++)
            assertTrue(bank.add(new Item(i)));
        assertEquals(352, bank.getValidIndices());
        assertFalse(bank.add(item));
    }

    @Test
    public void testAddToTab() {
        assertTrue(bank.add(new Item(2), 0));
        assertFalse(bank.add(new Item(3), 5));
        assertFalse(bank.add(null, 0));
        //Bank full
        for(int i = 0; i <= 351; i++)
            assertTrue(bank.add(new Item(i)));
        assertEquals(352, bank.getValidIndices());
        assertFalse(bank.add(new Item(4), 0));
    }

    *//**
     * Remove
     *//*
    @Test
    public void testRemove() {
        assertTrue(bank.add(item));
        assertTrue(bank.remove(item));
        assertTrue(bank.add(item));
        player.setBanking(false);
        assertFalse(bank.remove(item));

        //Bank full
        player.setBanking(true);
        for(int i = 0; i <= 350; i++)
            assertTrue(bank.add(new Item(i)));
        assertEquals(352, bank.getValidIndices());
        assertTrue(bank.remove(item));
    }

    *//**
     * Tab
     *//*
    @Test
    public void testCreateTab() {
        assertEquals(1, bank.getTabCount());
        assertTrue(bank.add(new Item(0)));
        assertTrue(bank.addTab(new Item(0)));

        assertEquals(1, bank.tab(1).getValidIndices());
        assertEquals(0, bank.tab(1).getItems()[0].getId());

        //Add tab using item that isn't in bank
        assertFalse(bank.addTab(new Item(11)));
        assertFalse(bank.addTab(null));

        for(int i = 0; i < 6; i++) {
            assertTrue(bank.add(new Item(i)));
            assertTrue(bank.addTab(new Item(i)));
        }

        assertTrue(bank.add(new Item(8)));
        assertFalse(bank.addTab(new Item(8)));
    }

    @Test
    public void testRemoveTab() {
        assertFalse(bank.removeTab(0));
        assertFalse(bank.removeTab(1));
        assertFalse(bank.removeTab(100));
        assertFalse(bank.removeTab(-1));

        for(int i = 0; i <= 6; i++) {
            assertTrue(bank.add(new Item(i)));
            assertTrue(bank.addTab(new Item(i)));
        }

        assertTrue(bank.removeTab(1));
        assertEquals(1, bank.tab(1).getItems()[0].getId());
        assertEquals(7, bank.getTabCount());
    }*/
}
