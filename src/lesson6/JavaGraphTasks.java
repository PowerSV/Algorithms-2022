package lesson6;

import kotlin.NotImplementedError;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    // Трудоемкость O(E + V)
    // Ресурсоемкость O(E + v)
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        List<Graph.Edge> result = new ArrayList<>();

        Set<Graph.Vertex> vertices = graph.getVertices();
        if (vertices.isEmpty()) {
            return result;
        }

        for (Graph.Vertex vertex : vertices) {
            Set<Graph.Vertex> neighbors = graph.getNeighbors(vertex);
            if (neighbors.size() == 0 || neighbors.size() % 2 != 0) {
                return result;
            }
        }

        Deque<Graph.Vertex> stack = new ArrayDeque<>();
        stack.push(vertices.iterator().next());
        Set<Graph.Edge> visitedEdges = new HashSet<>();
        while (!stack.isEmpty()) {
            Graph.Vertex current = stack.peek();
            boolean foundEdge = false;
            for (Graph.Vertex vertex : graph.getNeighbors(current)) {
                Graph.Edge currentEdge = graph.getConnection(current, vertex);
                if (visitedEdges.contains(currentEdge)){
                    continue;
                }
                visitedEdges.add(currentEdge);
                stack.push(vertex);
                foundEdge = true;
                break;
            }
            if (!foundEdge) {
                stack.pop();
                if (stack.peek() != null) {
                    result.add(graph.getConnection(current, stack.peek()));
                }
            }
        }

        if (result.size() != graph.getEdges().size()) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    // Трудоемкость O(E + V)
    // Ресурсоемкость O(E + V)
    public static Path longestSimplePath(Graph graph) {
        Path result = new Path();
        Set<Graph.Vertex> vertexSet = graph.getVertices();
        if (vertexSet.isEmpty()) {
            return result;
        }

        Deque<Path> stack = new ArrayDeque<>();
        int maxLen = 0;
        for (Graph.Vertex v : vertexSet) {
            stack.add(new Path(v));
        }

        while (!stack.isEmpty()) {
            Path path = stack.pop();
            if (path.getLength() > maxLen) {
                result = path;
                maxLen = path.getLength();
            }
            List<Graph.Vertex> vertexList = path.getVertices();
            Graph.Vertex vertex = vertexList.get(path.getLength());
            Set<Graph.Vertex> neighbors = graph.getNeighbors(vertex);
            for (Graph.Vertex v : neighbors) {
                if (!path.contains(v)) stack.add(new Path(path, graph, v));
            }
        }
        return result;
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
