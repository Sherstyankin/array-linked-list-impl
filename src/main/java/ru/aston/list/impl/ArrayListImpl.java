package ru.aston.list.impl;

import ru.aston.list.CustomList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Данный класс реализует динамический массив для хранения объектов.
 * Имплементирует интерфейс CustomList.
 * При достижении предельной вместимости массива, он увеличивается в 2 (два) раза.
 * Сложность операций:
 * - вставка (если массив не заполнен): O(1);
 * - вставка (если массив заполнен): O(n);
 * - удаление: O(n);
 * - поиск по индексу: O(1).
 * @author Sergey Sherstyankin
 */
public class ArrayListImpl<T> implements CustomList<T> {
    private Object[] array;
    private static final int INITIAL_CAPACITY = 8;

    private int size = 0;

    public ArrayListImpl() {
        this.array = new Object[INITIAL_CAPACITY];
    }

    public ArrayListImpl(int size) {
        this.array = new Object[size];
    }

    /**
     * Добавляет элемент в ближайшую свободную (null) ячейку массива.
     * Если свободных ячеек не осталось, то создается новый увеличенный (в 2 раза)
     * массив, в который перекладываются элементы из старого массива. Далее добавляется элемент
     * в ближайшую свободную (null) ячейку нового массива.
     *
     * @param element - элемент, который нужно добавить.
     */
    @Override
    public void add(T element) {
        if (array.length <= size) {
            Object[] oldArray = array;
            array = new Object[oldArray.length * 2];
            System.arraycopy(oldArray, 0, array, 0, oldArray.length);
        }
        array[size] = element;
        size++;
    }

    /**
     * Добавляет элемент по индексу массива. Перед добавлением элемента,
     * часть массива начиная с указанного индекса
     * по конец массива сдвигается вправо, если вместимость массива позволяет.
     * Иначе переносим обе части массива (копии) в увеличенный массив следующим образом:
     * 1. Копируем из старого массива (диапазон от начала до индекса (не включительно)) и вставляем в новый;
     * 2. Копируем из старого массива (диапазон от индекса (включительно) до конца массива) и вставляем в новый;
     * 3. Вставляем элемент на указанный индекс.
     *
     * @param index   - индекс, куда добавить элемент.
     * @param element - элемент, который нужно добавить.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ массива.
     */
    @Override
    public void addByIndex(int index, T element) {
        checkIndex(index, size);
        if (array.length <= size) {
            Object[] oldArray = array;
            array = new Object[oldArray.length * 2];
            //Переносим левую часть массива
            System.arraycopy(oldArray, 0, array, 0, index);
            //Переносим правую часть массива
            System.arraycopy(oldArray, index, array, index + 1, oldArray.length - index);
        } else {
            //Сдвигаем правую часть массива на шаг вправо
            System.arraycopy(array, index, array, index + 1, size - index);
        }
        //Вставляем элемент
        array[index] = element;
        size++;
    }

    /**
     * Получение элемента массива по указанному индексу.
     *
     * @param index - индекс элемента в массиве.
     * @return - элемент массива.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ массива.
     * @throws NullPointerException,      если элемент по индексу является null.
     */
    @Override
    public T get(int index) {
        checkIndex(index, size);
        checkNPE(index);
        return (T) array[index];
    }

    /**
     * Получение элемента массива по указанному индексу.
     * Метод удаляет элемент массива,
     * если он существует, иначе выбрасывается NullPointerException.
     * Если указанный индекс выходит на рамки массива, то выбрасывается
     * исключение IndexOutOfBoundsException.
     * Удаление элемента происходит посредством сдвига всех элементов влево (после удаляемого).
     * То есть индекс каждого последующего элемента уменьшается на 1 (один)
     * и размер массива уменьшается на 1 (один).
     *
     * @param index - индекс элемента, который нужно удалить.
     * @throws IndexOutOfBoundsException, если указанный индекс вне границ массива.
     */
    @Override
    public void remove(int index) {
        checkIndex(index, size);
        for (int i = index; i < size; i++) {
            array[i] = array[i + 1];
        }
        size--;
    }

    /**
     * Удаляет все элементы из массива.
     * Метод меняет значения всех элементов на null.
     */
    @Override
    public void clearAll() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    /**
     * Возвращает размер массива (количество элементов, которые НЕ null).
     *
     * @return - размер массива
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Метод осуществляет естественный порядок сортировки, если компаратор не задан (null),
     * иначе сортировка осуществляется, согласно правилам переданного компаратора.
     *
     * @param c - компаратор.
     */
    @Override
    public void sort(Comparator<? super T> c) {
        if (c == null) {
            Arrays.sort((T[]) array, 0, size);
        } else {
            Arrays.sort((T[]) array, 0, size, c);
        }
    }

    /**
     * Возвращает массив в виде строки (String) без пустых (null) элементов.
     *
     * @return - массив в виде строки (String)
     */
    @Override
    public String toString() {
        Object[] arrayWithoutNulls = new Object[size];
        System.arraycopy(array, 0, arrayWithoutNulls, 0, size);
        return Arrays.toString(arrayWithoutNulls);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrayListImpl<?> arrayList)) return false;
        return size == arrayList.size && Arrays.equals(array, arrayList.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    private void checkIndex(int index, int size) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(index);
    }

    private void checkNPE(int index) {
        if (array[index] == null) {
            throw new NullPointerException();
        }
    }
}
