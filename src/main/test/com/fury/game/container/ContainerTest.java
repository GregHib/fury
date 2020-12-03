package com.fury.game.container;

public class ContainerTest {

//    BasicContainer container = new BasicContainer(null, 1);

    /**
     * Add
     */
    /*@Test
    public void testAdd() {
        assertTrue(container.add(new Item(1)));
    }

    @Test
    public void testAddAmount() {
        assertTrue(container.add(new Item(1, 10)));
    }

    @Test
    public void testAddFull() {
        assertTrue(container.add(new Item(1)));
        assertFalse(container.add(new Item(2)));
    }

    @Test
    public void testAddAmountNull() {
        assertFalse(container.add(null));
    }

    *//**
     * Get
     *//*
    @Test
    public void testGet() {
        Item item = new Item(1, 2);
        container.add(new Item(1, 2));
        assertNotNull(container.get(item));
        assertNull(container.get(new Item(2)));
    }

    @Test
    public void testGetNull() {
        assertNull(container.get(-1));
    }

    @Test
    public void testGetNegative() {
        assertNull(container.get(new Item(-1)));
    }

    @Test
    public void testGetMax() {
        assertNull(container.get(new Item(Integer.MAX_VALUE + 1)));
    }


    @Test
    public void testGetChange() {
        Item item = new Item(1, 100);
        assertTrue(container.add(item));
        assertNotNull(container.get(item));
        assertNotNull(container.get(item).setAmount(50));
        assertEquals(item.getAmount(), container.get(item).getAmount());
    }
    *//**
     * Set
     *//*
    @Test
    public void testSet() {
        int amount = 10;
        Item item = new Item(1, 2);

        assertFalse(item == null);
        assertFalse(container.stackOutBounds(item));

        item.setAmount(amount);
        assertTrue(container.set(item));
        assertEquals(amount, container.get(item).getAmount());
    }

    @Test
    public void testSetSameIndex() {
        container.add(new Item(1));
        int amount = 20;
        Item item = new Item(1, 2);

        assertFalse(item == null);
        assertTrue(container.contains(item));
        assertFalse(container.stackOutBounds(item));

        item.setAmount(amount);
        assertTrue(container.set(item));
        assertEquals(amount, container.get(item).getAmount());
    }

    @Test
    public void testSetNewIndex() {
        container = new BasicContainer(null, 2);
        assertTrue(container.add(new Item(1)));
        int amount = 30;
        Item item = new Item(2, 2);

        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertFalse(container.stackOutBounds(item));

        item.setAmount(amount);
        assertTrue(container.set(item));
        assertEquals(amount, container.get(item).getAmount());
    }

    @Test
    public void testSetNull() {
        assertFalse(container.set(null));
        assertNull(container.get(new Item(1)));
    }

    @Test
    public void testSetNegative() {
        Item item = new Item(1);
        int amount = -1;

        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertFalse(container.stackOutBounds(item));

        item.setAmount(amount);
        assertTrue(container.set(item));
        assertNotNull(container.get(item));
    }

    *//**
     * Remove
     *//*

    @Test
    public void testRemove() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertTrue(container.remove(item));
        assertNull(container.get(item));
    }

    @Test
    public void testRemoveDifferentInstance() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertTrue(container.remove(new Item(1)));
        assertNull(container.get(item));
    }

    @Test
    public void testRemoveMore() {
        Item item = new Item(1, 100);
        assertTrue(container.add(item));
        assertTrue(container.remove(new Item(1, 101)));
        assertNull(container.get(item));
    }

    @Test
    public void testRemoveLess() {
        Item item = new Item(1, 100);
        assertTrue(container.add(item));
        assertTrue(container.remove(new Item(1, 99)));
        assertNull(container.get(item));
    }

    @Test
    public void testRemoveDifferent() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertFalse(container.remove(new Item(2)));
        assertNotNull(container.get(item));
    }

    @Test
    public void testRemoveNull() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertFalse(container.remove(null));
        assertNotNull(container.get(item));
    }

    *//**
     * Stack
     *//*

    @Test
    public void testStack() {
        Item item = new Item(1);
        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertTrue(container.stack(item));
    }

    @Test
    public void testStackReplace() {
        assertTrue(container.add(new Item(1)));
        Item item = new Item(1);
        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertTrue(container.stack(item));
        assertEquals(2, container.get(item).getAmount());
    }

    @Test
    public void testStackDifferentAmount() {
        container = new BasicContainer(null, 2);
        assertTrue(container.add(new Item(1)));
        int amount = 2;
        Item item = new Item(2, amount);
        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertTrue(container.stack(item));
        assertEquals(amount, container.get(item).getAmount());
    }

    @Test
    public void testStackNegativeId() {
        container = new BasicContainer(null, 2);
        assertTrue(container.add(new Item(-2)));
        int amount = 1;
        Item item = new Item(-1, amount);
        assertFalse(item == null);
        assertFalse(!container.contains(item) && !container.hasRoom());
        assertTrue(container.stack(item));
        assertEquals(amount, container.get(item).getAmount());
    }

    @Test
    public void testStackNull() {
        Item item = null;
        assertTrue(container.add(new Item(1)));
        assertFalse(container.stack(item));
    }

    *//**
     * Contains
     *//*

    @Test
    public void testStackContains() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertTrue(container.contains(item));
    }

    @Test
    public void testStackContainsInstance() {
        assertTrue(container.add(new Item(1)));
        assertTrue(container.contains(new Item(1)));
    }

    @Test
    public void testStackNotContains() {
        assertFalse(container.contains(new Item(1)));
    }

    @Test
    public void testStackContainsNull() {
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertFalse(container.contains(null));
    }

    @Test
    public void testStackContainsIds() {
        container = new BasicContainer(null, 2);
        Item item = new Item(1);
        assertTrue(container.add(new Item(2)));
        assertTrue(container.add(item));
        assertTrue(container.contains(item));
    }

    @Test
    public void testStackContainsNegative() {
        container = new BasicContainer(null, 2);
        Item item = new Item(1);
        assertTrue(container.add(new Item(-1)));
        assertTrue(container.add(item));
        assertTrue(container.contains(item));
    }

    *//**
     * Exists
     *//*
    @Test
    public void testExists() {
        assertFalse(container.exists(0));
        assertFalse(container.exists(1));
    }

    @Test
    public void testExistsOutOfBounds() {
        container = new BasicContainer(null, 2);
        container.add(new Item(1));
        assertTrue(container.exists(0));
        assertFalse(container.exists(1));
        assertFalse(container.exists(2));
        assertFalse(container.exists(-1));
        assertFalse(container.exists(Integer.MAX_VALUE + 1));
    }

    *//**
     * Get amount
     *//*
    @Test
    public void testAmount() {
        assertEquals(container.getAmount(new Item(1)), -1);
        assertEquals(container.getAmount(new Item(-1)), -1);
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertEquals(1, container.getAmount(item));
        item.setAmount(2);
        assertTrue(container.set(item));
        assertEquals(2, container.getAmount(item));
    }

    *//**
     * stackOutBounds
     *//*
    @Test
    public void testStackBounds() {
        Item item = new Item(1);
        assertFalse(container.stackOutBounds(item));
        assertTrue(container.add(item));
        assertFalse(container.stackOutBounds(item));
        item = new Item(1, Integer.MAX_VALUE);
        assertFalse(item == null || !container.contains(item));
        assertEquals(1, container.getAmount(item));
        assertTrue(container.stackOutBounds(item));
    }

    *//**
     * IndexOf
     *//*
    @Test
    public void testIndexOf() {
        container = new BasicContainer(null, 2);
        assertTrue(container.add(new Item(1)));
        Item item = new Item(1);
        assertTrue(container.add(item));
        assertEquals(0, container.indexOf(item));
        assertTrue(container.remove(item));
        assertTrue(container.remove(item));
        assertTrue(container.add(item));
        item = new Item(2);
        assertTrue(container.add(item));
        assertEquals(1, container.indexOf(item));
    }

    *//**
     * Revisions
     *//*
    @Test
    public void testRevisions() {
        container = new BasicContainer(null, 2);
        Item item = new Item(1, Revision.OSRS);
        assertTrue(container.add(item));
        assertTrue(container.add(new Item(1, Revision.PRE_RS3)));
        assertTrue(container.contains(item));
        assertTrue(container.contains(new Item(1, Revision.PRE_RS3)));//Amount ignored
        assertFalse(container.contains(new Item(1, Revision.RS2)));
        assertEquals(Revision.OSRS, container.get(item).getRevision());

        assertTrue(container.remove(new Item(1, Revision.PRE_RS3)));
        assertNull(container.get(new Item(1)));
        assertTrue(container.add(new Item(1, Revision.RS2)));
        assertNotNull(container.get(new Item(1)));

        assertFalse(container.set(new Item(1, 1, Revision.PRE_RS3)));
        assertTrue(container.set(new Item(1, 2, Revision.RS2)));
        assertEquals(2, container.get(new Item(1)).getAmount());
    }

    *//**
     * Clear
     *//*
    @Test
    public void testClear() {
        container = new BasicContainer(null, 2);
        Item item = new Item(1);
        assertTrue(container.add(new Item(10, 100, Revision.PRE_RS3)));
        assertTrue(container.add(item));

        assertTrue(container.clear());
        assertFalse(container.contains(item));
        assertEquals(2, container.getSpaces());
        assertEquals(0, container.getValidIndices());


        assertTrue(container.add(item));
        assertTrue(container.clear(item));
        assertFalse(container.contains(item));
        assertEquals(2, container.getSpaces());
        assertEquals(0, container.getValidIndices());
    }

    *//**
     * GetIndices
     *//*
    @Test
    public void testGetIndices() {
        int capacity = 5;
        container = new BasicContainer(null, capacity);
        Item item = new Item(1);
        assertTrue(container.add(new Item(10, 100, Revision.PRE_RS3)));
        assertTrue(container.add(item));

        assertEquals(capacity - 2, container.getSpaces());
        assertEquals(2, container.getValidIndices());
    }

    *//**
     * Move
     *//*
    @Test
    public void testMove() {
        int capacity = 5;
        BasicContainer cont = new BasicContainer(null, capacity);
        container = new BasicContainer(null, capacity);


        Item item = new Item(1);

        assertTrue(container.add(item));
        assertTrue(container.add(item));

        assertTrue(container.move(item, cont));
        assertEquals(1, container.getValidIndices());
        assertTrue(cont.move(item, container));
        assertEquals(2, container.getValidIndices());
        assertTrue(container.add(item));
        assertTrue(container.add(item));
        assertTrue(container.add(item));
        assertTrue(cont.add(item));
        assertFalse(cont.move(item, container));
    }*/
}