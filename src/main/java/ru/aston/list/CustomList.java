package ru.aston.list;

import java.util.Comparator;

public interface CustomList<T> {
    void add(T element);

    void addByIndex(int index, T element);

    T get(int index);

    void remove(int index);

    void clearAll();

    void sort(Comparator<? super T> c);

    int size();
}
