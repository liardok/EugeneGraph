package ru.eugene.graph.impl;

import ru.eugene.graph.EGraph;
import ru.eugene.graph.domain.EGraphType;

public final class EGraphFactory {

    public static <T> EGraph<T> getEGraph(final EGraphType type) {
        switch (type) {
            case DIRECTED:
                return new EDirectedGraph<>();
            case UNDIRECTED:
                return new EUnDirectedGraph<>();
            default:
                throw new IllegalArgumentException("Неверный тип аргумента:" + type);
        }
    }
}
