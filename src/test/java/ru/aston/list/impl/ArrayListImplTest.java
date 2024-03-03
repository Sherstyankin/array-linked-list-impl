package ru.aston.list.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.list.CustomList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayListImplTest extends CustomListTest {
    @BeforeEach
    void setUp() {
        list = new ArrayListImpl<>();
        list.add(1);
        list.add(4);
        list.add(2);
    }

    @Test
    void sort() {
        CustomList<Integer> expected = new ArrayListImpl<>();
        expected.add(1);
        expected.add(2);
        expected.add(4);
        list.sort(null);
        assertEquals(expected, list);
    }
}
