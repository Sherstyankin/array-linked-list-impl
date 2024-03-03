package ru.aston.list.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedListImplTest extends CustomListTest {
    @BeforeEach
    void setUp() {
        list = new LinkedListImpl<>();
        list.add(1);
        list.add(4);
        list.add(2);
    }

    @Test
    void sort() {
        list.sort(null);
        assertEquals(2, list.get(1));
    }
}