package ru.eugene.graph.impl;

import ru.eugene.graph.EGraph;

final class EUnDirectedGraph<T> extends EGraph<T> {

    @Override
    protected void addVertexConcrete(final T vertexSource, final T vertexDestination) {
        eGraphMap.get(vertexSource).add(vertexDestination);
        eGraphMap.get(vertexDestination).add(vertexSource);
    }
}
