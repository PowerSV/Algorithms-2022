package lesson3;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    private Node<T> findParent(T value) {
        if (root == null) {
            return null;
        }
        return findParent(root, value);
    }

    private Node<T> findParent(Node<T> parent, T value) {
        int comparison = value.compareTo(parent.value);
        if (comparison == 0) {
            return parent;
        }
        else if (comparison < 0) {
            if (parent.left == null) {
                return null;
            }
            else if (parent.left.value == value) {
                return parent;
            }
            else {
                return findParent(parent.left, value);
            }
        }
        else {
            if (parent.right == null) {
                return null;
            }
            else if (parent.right.value == value) {
                return parent;
            }
            return findParent(parent.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    // трудоемкость O(log n)
    // ресурсоемкость O(n)
    @Override
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> toRemove = find(t);
        Node<T> parent = findParent(t);
        assert toRemove != null;

        if (toRemove.left == null && toRemove.right == null) {      // если у узла нет "детей"
            replaceToRemove(null, toRemove, parent);            // то просто удаляем
        } else if (toRemove.left == null) {                          // если у узла только справа дети
            replaceToRemove(toRemove.right, toRemove, parent);       // то ставим на место родителя детей
        } else if (toRemove.right == null) {                         // если у узла только слева дети
            replaceToRemove(toRemove.left, toRemove, parent);        // то ставим на место родителя детей
        } else {
            Node<T> heir = findHeir(toRemove);
            replaceToRemove(heir, toRemove, parent);
        }
        size--;
        return  true;
    }

    private void replaceToRemove(Node<T> heir, Node<T> toRemove, Node<T> parent) {
        if (toRemove == root) {                                 // если узел был корнем,
            root = heir;                                        // то изменяем корень
        } else if (parent.left == toRemove) {
            parent.left = heir;
        } else {
            parent.right = heir;
        }
    }
    // Трудоемкость O(log n)
    private Node<T> findHeir(Node<T> parent) {
        Node<T> tempParent = parent;           // родитель преемника
        Node<T> heir = parent;                 // преемник
        Node<T> current = parent.right;
        while (current != null) {
            tempParent = heir;
            heir = current;
            current = current.left;
        }
        if (parent.right != heir) {            // если узел не справа от предшественника
            tempParent.left = heir.right;      // то правые узлы приемника становятся левыми узлами родителя
            heir.left = parent.left;           // узлы предшественника становятся узлами преемника
            heir.right = parent.right;
        } else {
            heir.left = parent.left;           // иначе левые узлы предшественника становятся узлами преемника
        }
        return heir;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {

        Deque<Node<T>> stack = new ArrayDeque<>();
        Node<T> current = null;

        private BinarySearchTreeIterator() {
            fillStack(root);
        }
        // Трудоемкость O(log n)
        // ресурсоемкость O(log n)
        private void fillStack(Node<T> node) {
            if (node != null) {
                stack.push(node);
                fillStack(node.left);
            }
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        // Трудоемкость O(1)
        // ресурсоемкость O(1)
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */
        // Трудоемкость O(log n)
        // ресурсоемкость O(log n)
        @Override
        public T next() {
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }
            current = stack.pop();
            if (current.right != null) {
                fillStack(current.right);
            }
            return current.value;
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */
        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            BinarySearchTree.this.remove(current.value);
            current = null;
        }
    }
    // трудоемкость O(log n)
    // ресурсоемкость O(n)

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}