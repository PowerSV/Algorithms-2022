package lesson5;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private final boolean[] deletedMark;

    private int size = 0;

    private enum Flag {
        DELETED
    }

    private final Object deleted = new Object();

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
        deletedMark = new boolean[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null && current != Flag.DELETED) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     * @noinspection unchecked
     */
    @Override
    public boolean remove(Object o) {
        int indexOfRemovable = indexOf((T) o);
        if (indexOfRemovable == -1) {
            return false;
        }
        storage[indexOfRemovable] = Flag.DELETED;
        size--;
        return true;
    }

    private int indexOf(T o) {
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return index;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                break;
            }
            current = storage[index];
        }
        return -1;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new OpenAddressingSetIterator();
    }

    private class OpenAddressingSetIterator implements Iterator<T> {

        private final Deque<T> stack = Arrays.stream(storage)
                .filter(Objects::nonNull)
                .filter(element -> !element.equals(Flag.DELETED))
                .map(element -> (T) element)
                .collect(Collectors.toCollection(ArrayDeque::new));

        private T current = null;

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = stack.pop();
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            OpenAddressingSet.this.remove(current);
            current = null;
        }
    }
}
