package com.fury.game.container.types;

import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.core.model.item.Item;
import org.junit.BeforeClass;

public class StackContainerTest {

    StackContainer container = new StackContainer(null, 5);
    Item item = new Item(1);

    @BeforeClass
    public static void setUp(){
        try {
            GameLoader.getCache().init();
            Loader.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add
     */
    /*@Test
    public void testAdd() {
        assertTrue(container.add(item));
        assertTrue(container.add(new Item(2)));
        assertFalse(container.add(new Item(1, 3)));//Item id 3 isn't stackable
        assertEquals(1, container.get(item).getAmount());//Item id 1 isn't stackable

        Item item = new Item(882, 10);
        assertTrue(container.add(item));//Bronze arrows
        assertTrue(container.add(new Item(882, 20)));
        assertEquals(30, container.get(item).getAmount());//arrows are stackable


        assertFalse(container.add(new Item(-100, -100)));
        assertFalse(container.add(new Item(882, -100)));
        assertFalse(container.add(null));
    }

    *//**
     * Set
     *//*
    @Test
    public void testSet() {
        assertTrue(container.add(item));
        item.setAmount(100);
        assertTrue(container.set(item));
        assertEquals(100, container.getAmount(item));

        item.setAmount(0);
        assertTrue(container.clear(item));
        assertTrue(container.set(item));
        assertFalse(container.contains(item));

        item.setAmount(-1);
        assertTrue(container.set(item));
        assertFalse(container.contains(item));

        item.setAmount(1);
        assertTrue(container.set(item));
        assertTrue(container.contains(item));

        assertFalse(container.set(null));
    }

    *//**
     * Remove
     *//*
    @Test
    public void testRemove() {
        assertTrue(container.add(item));
        assertTrue(container.contains(item));
        assertTrue(container.remove(item));
        assertFalse(container.contains(item));

        //Remove more than there is
        item.setAmount(1);
        assertTrue(container.add(item));
        item.setAmount(2);
        assertFalse(container.remove(item));
        assertEquals(1, container.getAmount(item));

        //Remove 1 from stack
        assertTrue(container.clear());
        item.setAmount(1);
        assertTrue(container.add(item));
        assertTrue(container.add(item));
        item.setAmount(1);
        assertTrue(container.remove(item));
        assertFalse(item == null || !container.contains(item));
        assertFalse(!container.containsEnough(item));
        assertEquals(1, container.getAmount(item));

        assertTrue(container.clear());

        item.setAmount(1);
        assertTrue(container.add(item));
    }*/
}
