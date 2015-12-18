package fr.gprince.kotlin.sandbox.graph.dsl


import org.junit.Test
import fr.gprince.kotlin.sandbox.graph.dsl.DGraph.Companion.graph
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 *
 */
class DGraphTest {

    @Test
    fun it_Should_create_a_DGraph_as_expected() {

        // given the DGraph DSL and Some names
        val graphName = "test-graph"
        val vertexName1 = "vertex_1"
        val vertexName2 = "vertex_2"
        val edgeName = "$vertexName1-To-$vertexName2"

        // on using the DSL to create a DGraph with one Edge between tow Vertices
        val graph: DGraph = graph(graphName) {
            edge(edgeName) from vertex(vertexName1) to vertex(vertexName2)
        }

        // it Should create a DGraph as expected
        assertNotNull(graph)
        assertEquals(graphName, graph.name)

        val vertices = graph.vertices
        val edges = graph.edges

        assertEquals(2, vertices.size)
        assertEquals(1, edges.size)
        assertNotNull(vertices[vertexName1])
        assertNotNull(vertices[vertexName2])
        assertNotNull(edges[edgeName])
        assertNotNull(edges[edgeName]?.source)
        assertNotNull(edges[edgeName]?.target)
        assertEquals(vertices[vertexName1], edges[edgeName]?.source)
        assertEquals(vertices[vertexName2], edges[edgeName]?.target)
        assertEquals(vertexName1, vertices[vertexName1]?.name)
        assertEquals(vertexName2, vertices[vertexName2]?.name)
        assertEquals(edgeName, edges[edgeName]?.name)
        assertTrue {
            vertices[vertexName1]?.outgoingEdges?.size == 1 &&
                    vertices[vertexName1]?.incomingEdges?.size == 0 &&
                    vertices[vertexName2]?.outgoingEdges?.size == 0 &&
                    vertices[vertexName2]?.incomingEdges?.size == 1 &&
                    edgeName in vertices[vertexName1]?.outgoingEdges ?: hashMapOf() &&
                    edgeName in vertices[vertexName2]?.incomingEdges ?: hashMapOf()
        }
    }

    @Test
    fun it_Should_create_a_DGraph_with_the_given_spec() {

        // given the DGraph DSL and Some names
        val graphName = "Complet_DGraph"
        val initialName = "Initial"
        val s1Name = "S1"
        val s2Name = "S2"
        val finalName = "Final"


        // on using the DSL to create a DGraph with one Edge between tow Vertices
        val graph: DGraph = graph(graphName) {
            vertex(initialName) to vertex(s1Name) to vertex(s2Name) to vertex(finalName)
        }

        // it Should create a DGraph as expected
        assertNotNull(graph)
        assertEquals(graphName, graph.name)

        val vertices = graph.vertices
        val edges = graph.edges

        assertEquals(4, vertices.size)
        assertEquals(3, edges.size)

    }

}