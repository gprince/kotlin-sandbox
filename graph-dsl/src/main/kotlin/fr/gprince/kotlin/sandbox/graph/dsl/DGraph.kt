package fr.gprince.kotlin.sandbox.graph.dsl

import fr.gprince.kotlin.sandbox.graph.dsl.DEdge.Companion.createEdge
import fr.gprince.kotlin.sandbox.graph.dsl.Vertex.Companion.createVertex

/**
 * # Empty String
 */
val String.Companion.EMPTY: String
    get() = ""

/**
 * # Vertex is the fundamental unit of which graphs are formed
 * @constructor Private constructor to enforce the use of [createVertex] method
 * @property name A name for the [Vertex]
 * @property graph The [DGraph] that holds this [Vertex]
 */
class Vertex private constructor(val name: String, val graph: DGraph) {

    val incomingEdges = hashMapOf<String, DEdge>()
    val outgoingEdges = hashMapOf<String, DEdge>()

    companion object {

        /**
         * # A NULL [Vertex] has empty [String] name and is associated to [DGraph.NULL]
         */
        val NULL = Vertex(String.EMPTY, DGraph.NULL)

        /**
         * # Factory that create a new [Vertex]
         * @param name A name for the Vertex to create
         * @param graph The graph that hold this [Vertex]
         */
        fun createVertex(name: String, graph: DGraph): Vertex {
            return Vertex(name, graph)
        }
    }

    infix fun to(other: Vertex): Vertex {

        val edge = createEdge(DEdge.EDGE_NAME_PATTERN.format(this.name, other.name), this.graph) from this to other
        graph.edges.put(edge.name, edge)
        return other
    }

}

/**
 * # A edge connects two vertices named : source and target
 * @constructor Private constructor to enforce the use of [createEdge] method
 * @property name A name for the [DEdge]
 * @property graph The [DGraph] that holds this [DEdge]
 */
class DEdge protected constructor(val name: String, val graph: DGraph) {

    private var _source: Vertex? = null
    val source: Vertex
        get() = this._source ?: Vertex.NULL

    private var _target: Vertex? = null
    val target: Vertex
        get() = this._target ?: Vertex.NULL

    companion object {

        val EDGE_NAME_PATTERN = "%s-To-%s"

        /**
         * # Factory that create a new [DEdge]
         * @param name A name for the [DEdge] to create
         * @param graph The graph that hold this [DEdge]
         */
        fun createEdge(name: String, graph: DGraph): DEdge {
            return DEdge(name, graph)
        }
    }


    /**
     *
     */
    infix fun from(source: Vertex): DEdge {
        this._source = source
        source.outgoingEdges.put(this.name, this)
        return this
    }

    infix fun to(target: Vertex): DEdge {
        this._target = target
        target.incomingEdges.put(this.name, this)
        return this
    }
}


/**
 * # A Directed Graph is a triple consisting of a set of vertices connected by edges, where the edges have a direction associated with them.
 * @constructor Create a new empty [DGraph]
 * @property name A name for the [DGraph]
 */
class DGraph(val name: String) {

    val vertices = hashMapOf<String, Vertex>()
    val edges = hashMapOf<String, DEdge>()

    companion object {

        /**
         * A null [DGraph]
         */
        val NULL = DGraph(String.EMPTY)

        /**
         * # Create a new [DGraph] with the given name and builder function.
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
     * # Build a new [Vertex] with the given name in this [DGraph].
     * @param name A name for the [Vertex] to build
     * @param build A builder extension function to initialize the new [Vertex], default {}
     */
    fun vertex(name: String, build: Vertex.() -> Unit = {}): Vertex {
        val vertex = createVertex(name, this)
        vertex.build()
        vertices.put(name, vertex)
        return vertex
    }

    /**
     * # Build a new [DEdge] with the given name in this [DGraph].
     * @param name A name for the [DEdge] to build
     * @param build A builder extension function to initialize the new [DEdge], default {}
     */
    fun edge(name: String, build: DEdge.() -> Unit = {}): DEdge {
        val edge = createEdge(name, this)
        edge.build()
        edges.put(name, edge)
        return edge
    }

}