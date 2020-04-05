# EugeneGraph

Eugene graph library.

It can be used to store directed or undirected graph (it depends on enum type).
It has some methods (T - user defined type):
  1. addVertex(T vertex) - to add verticies to graph. Verticies are user defined objects. They should override equals and hashcode;
  2. addEdge(T vertexSource, T vertexDestination) - to add edge between verticies;
  3. getPath(T vertexSource, T vertexDestination) - to find path between two verticies;
  4. getVertexCound - to get count all verticies of graph;
  5. hasVertex(T vertex) - to check if vertex exists in graph;
  6. hasEdge(T vertexSource, T vertexDestination) - to check if edge exists between two verticies.

using:
- for code: java 11, maven 3
- for tests: spock 2 (groovy 3)

It is not thread-safe now, but it can be released it next version.

Example:

        EGraph<SimpleEugeneClass> eGraph = EGraphFactory.getEGraph(UNDIRECTED);
        
        SimpleEugeneClass simpleEugeneClassOne = new SimpleEugeneClass("test1String1", "test1String2");
        SimpleEugeneClass simpleEugeneClassTwo = new SimpleEugeneClass("test2String1", "test2String2");
        SimpleEugeneClass simpleEugeneClassThree = new SimpleEugeneClass("test3String1", "test3String2")
        SimpleEugeneClass simpleEugeneClassFour = new SimpleEugeneClass("test4String1", "test4String2")
        SimpleEugeneClass simpleEugeneClassFive = new SimpleEugeneClass("test5String1", "test5String2")
        
        eGraph.addVertex(simpleEugeneClassOne)
        eGraph.addVertex(simpleEugeneClassTwo)
        eGraph.addVertex(simpleEugeneClassThree)
        eGraph.addVertex(simpleEugeneClassFour)
        eGraph.addVertex(simpleEugeneClassFive)

        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassTwo)
        eGraph.addEdge(simpleEugeneClassOne, simpleEugeneClassFour)
        eGraph.addEdge(simpleEugeneClassFour, simpleEugeneClassFive)
        eGraph.addEdge(simpleEugeneClassFive, simpleEugeneClassThree)

        List<SimpleEugeneClass> vertexList = eGraph.getPath(simpleEugeneClassOne, simpleEugeneClassThree)

