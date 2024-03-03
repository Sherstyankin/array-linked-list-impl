package ru.aston.list.impl;


import ru.aston.list.CustomList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Данный класс реализует двусвязанный список для хранения объектов.
 * Имплементирует интерфейс CustomList.
 * Список состоит из узлов (node).
 * Каждый узел содержит объект и ссылки на предыдущий и следующий элемент.
 * Итерация по списку может проходить либо с начала (head) или с конца (tail).
 * Сложность операций:
 * - вставка/удаление (конец/начало): O(1);
 * - вставка/удаление (середина): O(n/2);
 * - поиск по индексу: O(n/2).
 * @author Sergey Sherstyankin
 */
public class LinkedListImpl<T> implements CustomList<T> {
    Node<T> head;
    Node<T> tail;
    private int size = 0;

    public LinkedListImpl() {
        // Empty list
    }

    /**
     * Добавляет элемент в список (в конец списка)
     *
     * @param element - элемент, который нужно добавить
     */
    @Override
    public void add(T element) {
        linkLast(element);
    }

    /**
     * Вставка элемента по индексу (в начало/середину/конец).
     *
     * @param index   - индекс элемента, на место которого, нужно вставить новый элемент.
     * @param element - элемент для вставки.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ списка.
     */
    @Override
    public void addByIndex(int index, T element) {
        checkIndex(index, size);
        if (index == size) {
            linkLast(element);
        } else if (index == 0) {
            linkFirst(element);
        } else {
            linkBefore(index, element);
        }
    }

    /**
     * Возвращает элемент по индексу.
     *
     * @param index - индекс элемента, который нужно получить.
     * @return - элемент списка.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ списка.
     */
    @Override
    public T get(int index) {
        checkIndex(index, size);
        Node<T> n = findNode(index);
        checkNPE(n);
        return n.element;
    }

    /**
     * Удаление элемента по индексу
     *
     * @param index - индекс элемента, который нужно удалить.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ списка.
     */
    @Override
    public void remove(int index) {
        checkIndex(index, size);
        Node<T> n = findNode(index); // узел, который нужно удалить
        Node<T> prev = n.prev; // узел перед удаляемым
        Node<T> next = n.next; // узел после удаляемого

        // связать prev c next
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            n.prev = null;
        }

        // связать next c prev
        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            n.next = null;
        }

        n.element = null; // удаление элемента
        size--;
    }

    /**
     * Удаляет все элементы из списка.
     */
    @Override
    public void clearAll() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Метод осуществляет естественный порядок сортировки, если компаратор не задан (null),
     * иначе сортировка осуществляется, согласно правилам переданного компаратора.
     *
     * @param c - компаратор.
     */
    @Override
    public void sort(Comparator<? super T> c) {
        Object[] array = this.toArray();
        if (c == null) {
            Arrays.sort((T[]) array);
        } else {
            Arrays.sort((T[]) array, c);
        }
        fromArray(array);
    }

    /**
     * Возвращает размер списка.
     *
     * @return - размер списка.
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        Node<T> n = head;
        do {
            result.append(n.element);
            n = n.next;
            result.append(", ");
        } while (n.next != null);
        result.append(n.element); // добавить последний элемент
        return "[" + result + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListImpl<?> that)) return false;
        return size == that.size && Objects.equals(head, that.head) && Objects.equals(tail, that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, size);
    }

    private void fromArray(Object[] array) {
        clearAll();
        for (Object o : array) {
            add((T) o);
        }
    }

    private Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> n = head;
        int index = 0;
        do {
            array[index] = n.element;
            n = n.next;
            index++;
        } while (n.next != null);
        array[index] = n.element; // добавить последний элемент
        return array;
    }

    /**
     * Добавляет элемент в конец списка.
     *
     * @param element - элемент для добавления.
     */
    private void linkLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    /**
     * Добавляет элемент в середину списка.
     *
     * @param element - элемент для добавления.
     */
    private void linkBefore(int index, T element) {
        Node<T> curNode = findNode(index);
        Node<T> curNodePrev = curNode.prev;
        Node<T> newNode = new Node<>(curNodePrev, element, curNode);
        curNode.prev = newNode;
        curNodePrev.next = newNode;
        size++;
    }

    /**
     * Добавляет элемент в начало списка.
     *
     * @param element - элемент для добавления.
     */
    private void linkFirst(T element) {
        Node<T> oldHead = head;
        Node<T> newNode = new Node<>(null, element, oldHead);
        head = newNode;
        if (oldHead == null) {
            tail = newNode;
        } else {
            oldHead.prev = newNode;
        }
        size++;
    }

    /**
     * Поиск узла (node) по индексу.
     * Если индекс в первой половине списка, то итерируемся начиная с головы (head).
     * Иначе - с хвоста (tail).
     *
     * @param index - индекс элемента.
     * @return - узел (node).
     */
    private Node<T> findNode(int index) {
        Node<T> n;
        if (index < size / 2) {
            n = head;
            for (int i = 0; i < index; i++)
                n = n.next;
        } else {
            n = tail;
            for (int i = size - 1; i > index; i--)
                n = n.prev;
        }
        return n;
    }

    private void checkIndex(int index, int size) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index);
    }

    private void checkNPE(Node<T> n) {
        if (n.element == null) {
            throw new NullPointerException();
        }
    }

    private static class Node<T> {
        T element;
        Node<T> next;
        Node<T> prev;

        Node(Node<T> prev, T element, Node<T> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
