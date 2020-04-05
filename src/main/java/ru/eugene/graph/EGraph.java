package ru.eugene.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class EGraph<T> {
    final protected Map<T, List<T>> eGraphMap = new HashMap<>();

    public void addVertex(final T vertex) {
        eGraphMap.putIfAbsent(vertex, new LinkedList<>());
    }

    public Map<T, List<T>> getVertices() {
        return eGraphMap;
    }

    protected abstract void addVertexConcrete(final T vertexSource, final T vertexDestination);

//    TODO: в зависимости от постановки можем добавлять автоматически вершины в граф, которых еще нет, в текущей реализации возвращаем ошибку
    public void addEdge(final T vertexSource, final T vertexDestination) {
        if (!hasVertex(vertexSource) || !hasVertex(vertexDestination)) {
            throw new IllegalArgumentException("Связь возможно установить только между существующими объектами в графе");
        }

        addVertexConcrete(vertexSource, vertexDestination);
    }

    public boolean hasEdge(final T vertexSource, final T vertexDestination) {
        if (!hasVertex(vertexSource) || !hasVertex(vertexDestination)) {
            throw new IllegalArgumentException("Нет вершины в графе");
        }

        return eGraphMap.get(vertexSource).stream().anyMatch(v -> v.equals(vertexDestination));
    }

    public boolean hasVertex(final T vertex) {
        return eGraphMap.containsKey(vertex);
    }

    public int getVertexCount() {
        return eGraphMap.size();
    }

    public List<T> getPath(final T vertexSource, final T vertexDestination) {

        if (vertexSource.equals(vertexDestination)) {
            return new LinkedList<>(Collections.singleton(vertexSource));
        }

        if (eGraphMap.get(vertexSource).contains(vertexDestination)) {
            return new LinkedList<>(Arrays.asList(vertexSource, vertexDestination));
        }

        final ArrayList<T> visited = new ArrayList<>();
        return getPathForNotLinkedVertex(vertexSource, vertexDestination, visited);
    }

    private List<T> getPathForNotLinkedVertex(final T vertexSource, final T vertexDestination, final ArrayList<T> visited) {

        final LinkedList<T> path = new LinkedList<>();
        path.add(vertexSource);

        for (T connectedVertex : eGraphMap.get(vertexSource)) {
            if (!visited.contains(connectedVertex)) {
                visited.add(connectedVertex);
                if (eGraphMap.get(connectedVertex).contains(vertexDestination)) {
                    path.add(connectedVertex);
                    return path;
                } else {
                    path.addAll(getPathForNotLinkedVertex(connectedVertex, vertexDestination, visited));
                }
            }
        }

        path.remove(vertexSource);
        return path;
    }
}