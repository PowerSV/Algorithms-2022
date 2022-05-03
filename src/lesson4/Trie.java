package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        SortedMap<Character, Node> children = new TreeMap<>();
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {

        private int wordCounter = 0;
        private final ArrayList<Integer> keySetIndexes = new ArrayList<>();
        private String currentWord;

        // Трудоемкость O(1)
        // Ресурсоемкость O(1)
        @Override
        public boolean hasNext() {
            return wordCounter < size;
        }

        // Трудоемкость O(word.length) где n - длина слова
        // Ресурсоемкость O(m) где m - длина самого длинного слова
        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            wordCounter++;
            currentWord = levelIterator(root, "", ' ', 0);
            return currentWord;
        }

        private void moveToNextChar(int levelIndex) {
            keySetIndexes.set(levelIndex - 1, keySetIndexes.get(levelIndex - 1) + 1);
            keySetIndexes.set(levelIndex, 0);
        }

        private String levelIterator(Node node, String word, char currentChar, int levelIndex) {
            if (levelIndex >= keySetIndexes.size()) {
                keySetIndexes.add(0);
            }
            if (node != root && currentChar == (char) 0) {
                moveToNextChar(levelIndex);
                return word;
            }

            Set<Character> childrenKeySet = node.children.keySet();
            if (childrenKeySet.isEmpty()) {
                moveToNextChar(levelIndex);
                return levelIterator(root, "", ' ', 0);
            }

            int index = 0;
            for (char character : childrenKeySet) {
                if (keySetIndexes.get(levelIndex) >= childrenKeySet.size()) {
                    break;
                }
                if (index < keySetIndexes.get(levelIndex)) {
                    index++;
                    continue;
                }
                if (node == root) {
                    return levelIterator(node.children.get(character), word, character, levelIndex + 1);
                }
                return levelIterator(node.children.get(character),
                        word + currentChar, character, levelIndex + 1);
            }
            moveToNextChar(levelIndex);
            return levelIterator(root, "", ' ', 0);
        }

        // Трудоемкость: O(n) где n - длина слова
        // Ресурсоемкость O(1)
        @Override
        public void remove() {
            if (currentWord == null) {
                throw new IllegalStateException();
            }
            Trie.this.remove(currentWord);
            currentWord = null;
            wordCounter--;
            for (int i = 1; i < keySetIndexes.size(); i++) {
                int prevLvlIndex = keySetIndexes.get(i - 1);
                if (keySetIndexes.get(i) == 0 && prevLvlIndex != 0) {
                    keySetIndexes.set(i - 1, prevLvlIndex - 1);
                }
            }
        }

    }
}