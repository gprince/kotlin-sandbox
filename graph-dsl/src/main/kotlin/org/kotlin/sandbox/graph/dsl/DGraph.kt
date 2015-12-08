package org.kotlin.sandbox.graph.dsl

val String.Companion.EMPTY: String
    get() = ""

class Vertex private constructor(val name: String, val graph: DGraph) {

    val incomingEdges = hashMapOf<String, Edge>()
    val outgoingEdges = hashMapOf<String, Edge>()

    internal companion object {

        val NULL = Vertex(String.EMPTY, DGraph.NULL)

        /**
         * @param name A name for the Vertex to build
         * @param graph The graph that hold this Vertex
         */
        fun vertex(name: String, graph: DGraph): Vertex {
            return Vertex(name, graph)
        }
    }

}

class Edge private constructor(val name: String, val graph: DGraph) {

    private var _source: Vertex? = null
    val source: Vertex
        get() = this._source ?: Vertex.NULL


    private var _target: Vertex? = null
    val target: Vertex
        get() = this._target ?: Vertex.NULL

    internal companion object {
        /**
         * @param name A name for the Edge to build
         * @param graph The graph that hold this Edge
         */
        fun edge(name: String, graph: DGraph): Edge {
            return Edge(name, graph)
        }
    }


    infix fun from(source: Vertex): Edge {
        this._source = source
        source.outgoingEdges.put(this.name, this)
        return this
    }

    infix fun to(target: Vertex): Edge {
        this._target = target
        target.incomingEdges.put(this.name, this)
        return this
    }
}


/**
 * <h1>A Directed Graph is a triple consisting of a vertex set an edge set, and a
 * directed relation that associates with each edge two vertices (not necessarily
 * distinct) called source and target.</h1>
 * @constructor create a new empty [DGraph]
 * @property name A name for the [DGraph]
 */
class DGraph(val name: String) {

    val vertices = hashMapOf<String, Vertex>()
    val edges = hashMapOf<String, Edge>()

    companion object {

        /**
         * A null [DGraph]
         */
        val NULL = DGraph(String.EMPTY)

        /**
         * <h1>Create a new [DGraph] with the given name and builder function.</h1>
         * @param name A name for the [DGraph] to build
         * @param build A builder extension function to initialize the new [DGraph], default {}
         */
        fun graph(name: String, build: DGraph.() -> Unit = {}): DGraph {
            val graph = DGraph(name)
            graph.build()
            return graph
        }
    }

    /**
     * <h1>Build a new [Vertex] with the given name in this [DGraph].</h1>
     * @param name A name for the [Vertex] to build
     * @param build A builder extension function to initialize the new [Vertex], default {}
     */
    fun vertex(name: String, build: Vertex.() -> Unit = {}): Vertex {
        val vertex = Vertex.vertex(name, this)
        vertex.build()
        vertices.put(name, vertex)
        return vertex
    }

    /**
     * <h1>Build a new [Edge] with the given name in this [DGraph].</h1>
     * @param name A name for the [Edge] to build
     * @param build A builder extension function to initialize the new [Edge], default {}
     */
    fun edge(name: String, build: Edge.() -> Unit = {}): Edge {
        val edge = Edge.edge(name, this)
        edge.build()
        edges.put(name, edge)
        return edge
    }

}

fun main(args: Array<String>) {

    val graphName = "test-graph"
    val vertexName1 = "vertex_1"
    val vertexName2 = "vertex_2"
    val edgeName = "$vertexName1-To-$vertexName2"


    val graph: DGraph = DGraph.graph(graphName) {
        edge(edgeName) from vertex(vertexName1) to vertex(vertexName2)
    }

    kotlin.test.assertNotNull(graph)
    kotlin.test.assertEquals(graphName, graph.name)
    kotlin.test.assertEquals(2, graph.vertices.size)
    kotlin.test.assertEquals(1, graph.edges.size)
    kotlin.test.assertEquals(graph.vertices[vertexName1], graph.edges[edgeName]?.source)
    kotlin.test.assertEquals(graph.vertices[vertexName2], graph.edges[edgeName]?.target)

}
