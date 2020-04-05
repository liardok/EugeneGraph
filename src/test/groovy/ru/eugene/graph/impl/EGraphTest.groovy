package ru.eugene.graph.impl

import ru.eugene.graph.EGraph
import spock.lang.Specification

import static ru.eugene.graph.domain.EGraphType.DIRECTED
import static ru.eugene.graph.domain.EGraphType.UNDIRECTED

class EGraphTest extends Specification {

    def "Создание обыного графа"() {
        when: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(UNDIRECTED)

        then: "Объект eGraph существует"
        eGraph != null
        eGraph instanceof EUnDirectedGraph
    }

    def "Создание направленного графа"() {
        when: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(DIRECTED)

        then: "Объект eGraph существует"
        eGraph != null
        eGraph instanceof EDirectedGraph
    }

    def "Добавляем вершину пользовательского типа в граф - должны быть только добавленные вершины"() {
        given: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа"
        def simpleEugeneClass = new SimpleEugeneClass("test1", "test2")

        when: "Добавляем вершины в граф"
        eGraph.addVertex(simpleEugeneClass)

        then: "Проверяем что в графе есть только одна вершина == тествому объекту"
        !eGraph.getVertices().isEmpty()
        eGraph.getVertices().size() == 1
        eGraph.getVertices().containsKey(simpleEugeneClass)
    }

    def "Добавляем вершину пользовательского типа в граф - количество вершин должно быть = 1"() {
        given: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа"
        def simpleEugeneClass = new SimpleEugeneClass("test1", "test2")

        when: "Добавляем вершины в граф"
        eGraph.addVertex(simpleEugeneClass)

        then: "Проверяем что в графе есть только одна вершина == тествому объекту"
        eGraph.getVertexCount() == 1
    }

    def "Добавляем вершину пользовательского типа в граф - метод hasVertex для этого объекта должен вернуть true"() {
        given: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа"
        def simpleEugeneClass = new SimpleEugeneClass("test1", "test2")

        when: "Добавляем вершины в граф"
        eGraph.addVertex(simpleEugeneClass)

        then: "Проверяем что в графе есть только одна вершина == тествому объекту"
        !eGraph.getVertices().isEmpty()
        eGraph.hasVertex(simpleEugeneClass)
    }

//    TODO: в зависимости от постановки можем добавлять автоматически вершины в граф, которых еще нет, в текущей реализации возвращаем ошибку
    def "Добавляем грань для объектов, которых нет в графе - должны вернуть ошибку"() {
        given: "Создание нового графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")

        when: "Добавляем связь для двух объектов вне графа"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Получаем ошибку"
        thrown(IllegalArgumentException.class)
    }

    def "Добавляем две веришны и создаем грань между ними в направленном графе - должна быть связь от вершины 1 к вершине 2"() {
        given: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)

        when: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Получение связи для вершины один - проверка что есть грань"
        eGraph.getVertices().get(simpleEugeneClassOne).size() == 1
        eGraph.getVertices().get(simpleEugeneClassOne).find {it -> (it == simpleEugeneClassTwo) }
        and: "Связи от второй вершины к первой нет"
        eGraph.getVertices().get(simpleEugeneClassTwo).size() == 0

    }

    def "Добавляем две веришны и создаем грань между ними в направленном графе - hasEdge должен вернуть true"() {
        given: "Создание нового графа"
        EGraph eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)

        when: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Получение связи для вершины один - проверка что есть грань"
        eGraph.hasEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        and: "проверка что связи от второго объекта к первому нет"
        !eGraph.hasEdge(simpleEugeneClassTwo, simpleEugeneClassOne)
    }

    def "Добавляем две веришны и создаем грань между ними в ненаправленном графе - связь должна быть в оба направления"() {
        given: "Создание нового ненаправленного графа"
        EGraph eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)

        when: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Проверка, что есть связь в оба направления"
        eGraph.hasEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
    }

    def "Получить путь между одной и той же вершиной - должны вернуть массив из одной вершины"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassOne)

        then: "Должен быть массив из объекта один и объекта два"
        vertexList.size() == 1
        vertexList.containsAll(simpleEugeneClassOne)

    }

    def "Получить путь между двумя связанными напрямую вершинами в ненаправленном графе"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Должен быть массив из объекта один и объекта два"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassOne, simpleEugeneClassTwo)

    }

    def "Получить путь между двумя связанными напрямую вершинами в ненаправленном графе от объекта два к объекту один"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassTwo, simpleEugeneClassOne)

        then: "Должен быть массив из объекта один и объекта два"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassOne, simpleEugeneClassTwo)

    }

    def "Получить путь между двумя связанными напрямую вершинами в направленном графе"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassTwo)

        then: "Должен быть массив из объекта один и объекта два"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassOne, simpleEugeneClassTwo)

    }

    def "Получить путь между двумя связаннами напрямую вершинами в направленном графе от объекта два к объекту один"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassTwo, simpleEugeneClassOne)

        then: "Массив должен быть пустым, так как путь есть только от объекта один к объекту два"
        vertexList.size() == 0

    }

    def "Получить путь между двумя не связаннами напрямую вершинами в направленном графе от объекта один к объекту три"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "Объект пользовательского типа три"
        def simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        and: "Объект пользовательского типа четыре"
        def simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        and: "Объект пользовательского типа пять"
        def simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassThree)

        then: "Массив должен cодержать объекты 'один'->'четыре'->'пять'->'три'"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassFour, simpleEugeneClassFive)

    }

    def "Получить путь между двумя не связаннами напрямую вершинами в направленном графе от объекта три к объекту один"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "Объект пользовательского типа три"
        def simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        and: "Объект пользовательского типа четыре"
        def simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        and: "Объект пользовательского типа пять"
        def simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassThree, simpleEugeneClassOne)

        then: "Массив должен быть пустым, так как обратных связей нет"
        vertexList.size() == 0

    }

    def "Получить путь между двумя не связаннами напрямую вершинами в ненаправленном графе от объекта один к объекту три"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "Объект пользовательского типа три"
        def simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        and: "Объект пользовательского типа четыре"
        def simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        and: "Объект пользовательского типа пять"
        def simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassThree)

        then: "Массив должен cодержать объекты 'четыре'->'пять'"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassFour, simpleEugeneClassFive)

    }

    def "Получить путь между двумя не связаннами напрямую вершинами в ненаправленном графе от объекта три к объекту один"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "Объект пользовательского типа три"
        def simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        and: "Объект пользовательского типа четыре"
        def simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        and: "Объект пользовательского типа пять"
        def simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassThree)

        then: "Массив должен cодержать объекты 'пять'->'четыре'"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassFive, simpleEugeneClassFour)

    }


    def "Получить путь между двумя не связаннами напрямую вершинами в направленном графе от объекта три к объекту один, граф имеет циклическую зависимость"() {
        given: "Создание нового ненаправленного графа"
        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(DIRECTED)
        and: "Объект пользовательского типа один"
        def simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2")
        and: "Объект пользовательского типа два"
        def simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2")
        and: "Объект пользовательского типа три"
        def simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        and: "Объект пользовательского типа четыре"
        def simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        and: "Объект пользовательского типа пять"
        def simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        and: "добавляем вершны в граф"
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)
        and: "Добавляем связь между вершинами"
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassOne)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        when: "Возвращаем путь между объектом один и объектом два"
        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassThree)

        then: "Массив должен cодержать объекты 'пять'->'четыре'"
        vertexList.size() == 2
        vertexList.containsAll(simpleEugeneClassFive, simpleEugeneClassFour)

    }

    class SimpleEugeneClass {
        private String simpleStringOne
        private String simpleStringTwo

        private SimpleEugeneClass() {}

        SimpleEugeneClass(String simpleStringOne, String simpleStringTwo) {
            this.simpleStringOne = simpleStringOne
            this.simpleStringTwo = simpleStringTwo
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (!(o instanceof SimpleEugeneClass)) return false

            SimpleEugeneClass that = (SimpleEugeneClass) o

            if (simpleStringOne != that.simpleStringOne) return false
            if (simpleStringTwo != that.simpleStringTwo) return false

            return true
        }

        int hashCode() {
            int result
            result = (simpleStringOne != null ? simpleStringOne.hashCode() : 0)
            result = 31 * result + (simpleStringTwo != null ? simpleStringTwo.hashCode() : 0)
            return result
        }
    }

}
