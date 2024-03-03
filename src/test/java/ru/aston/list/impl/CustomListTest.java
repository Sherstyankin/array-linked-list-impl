package ru.aston.list.impl;

import org.junit.jupiter.api.Test;
import ru.aston.list.CustomList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class CustomListTest {
    protected CustomList<Integer> list;

    @Test
    void add() {
        list.add(3);
        assertEquals(4, list.size());
    }

    @Test
    void addByIndex() {
        list.addByIndex(1, 3);
        assertEquals(3, list.get(1));
    }

    @Test
    void addByIndexWhenIndexOutOfBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.addByIndex(3, 3));
    }

    @Test
    void get() {
        assertEquals(4, list.get(1));
    }

    @Test
    void getWhenIndexOutOfBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.addByIndex(3, 3));
    }

    @Test
    void getWhenElementIsNull() {
        list.addByIndex(1, null);
        assertThrows(NullPointerException.class, () -> list.get(1));
    }

    @Test
    void remove() {
        list.remove(1);
        assertEquals(2, list.size());
    }

    @Test
    void removeWhenIndexOutOfBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
    }

    @Test
    void clearAll() {
        list.clearAll();
        assertEquals(0, list.size());
    }

    @Test
    void size() {
        assertEquals(3, list.size());
    }
}