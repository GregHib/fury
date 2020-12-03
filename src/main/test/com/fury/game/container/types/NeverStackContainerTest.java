package com.fury.game.container.types;

import com.fury.cache.def.Loader;
import com.fury.game.GameLoader;
import com.fury.core.model.item.Item;
import org.junit.BeforeClass;

public class NeverStackContainerTest {

    NeverStackContainer container = new NeverStackContainer(null, 5);
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
//    @Test
//    public void testAdd() {
//        //Stackable
//        assertFalse(container.add(new Item(822)));
//
//        Item item = new Item(1);//Non stackable
//        assertTrue(container.add(item));
//        assertTrue(container.add(item));
//        assertEquals(item.getAmount(), container.get(item).getAmount());
//        assertEquals(2, container.getItemTotal());
//    }
//
//    /**
//     * Set
//     */
//    @Test
//    public void testSet() {
//        assertTrue(container.add(item));
//        assertTrue(container.set(item));
//        assertEquals(item.getAmount(), container.get(item).getAmount());
//        assertEquals(1, container.getValidIndices());//1 because set just changes amount
//
//        assertTrue(container.clear());
//        assertTrue(container.set(item));
//        assertEquals(item.getAmount(), container.get(item).getAmount());
//        assertEquals(1, container.getValidIndices());
//        item.setAmount(100);
//        assertTrue(container.set(item));
//        assertEquals(item.getAmount(), container.get(item).getAmount());
//    }
//
//    /**
//     * Remove
//     */
//    @Test
//    public void testRemove() {
//        assertTrue(container.add(item));
//        assertTrue(container.remove(item));
//        assertEquals(0, container.getValidIndices());
//
//        assertTrue(container.add(item));
//        item.setAmount(2);
//        assertFalse(container.remove(item));//Can only remove 1 from a never stack container
//        assertEquals(1, container.getValidIndices());
//    }
}
