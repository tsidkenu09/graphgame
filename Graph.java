/* 2210A Assignment 5
 * Tsid Tomori
 * class to represent an entire graph
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Graph implements GraphADT {
    private int numNodes;
    private List<GraphNode> nodes;
    private GraphEdge[][] adjacencyMatrix;

    public Graph(int n) {
        this.numNodes = n;
        this.nodes = new ArrayList<>(n);
        this.adjacencyMatrix = new GraphEdge[n][n];

        for (int i = 0; i < n; i++) {
            nodes.add(new GraphNode(i));
        }
    }

    @Override
    public void insertEdge(GraphNode u, GraphNode v, int edgeType, String label) throws GraphException {
        if (!nodes.contains(u) || !nodes.contains(v)) {
            throw new GraphException("One or both of these nodes don't exist in the graph");
        }

        int uIndex = u.getName();
        int vIndex = v.getName();

        if (adjacencyMatrix[uIndex][vIndex] != null || adjacencyMatrix[vIndex][uIndex] != null) {
            throw new GraphException("This edge already exists");
        }

        adjacencyMatrix[uIndex][vIndex] = new GraphEdge(u, v, edgeType, label);
        adjacencyMatrix[vIndex][uIndex] = new GraphEdge(v, u, edgeType, label);
        
        //print statemtn to let me know what is being connected
    }

    @Override
    public GraphNode getNode(int name) throws GraphException {
        if (name < 0 || name >= numNodes) {
            throw new GraphException("The node with this name doesn't exist");
        }

        return nodes.get(name);
    }

    public void printAdjacencyMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                System.out.print((adjacencyMatrix[i][j] != null ? "1" : "0") + " ");
            }
            System.out.println();
        }
    }

    public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
        if (!nodes.contains(u)) {
            throw new GraphException("This node is not part of the graph");
        }

        // make a list of edges
        List<GraphEdge> edges = new ArrayList<>();

        int uIndex = u.getName();
        for (int i = 0; i < numNodes; i++) {
            if (adjacencyMatrix[uIndex][i] != null) {
                edges.add(adjacencyMatrix[uIndex][i]);
            }
        }

        // what if it self-loops?
        if (adjacencyMatrix[uIndex][uIndex] != null) {
            edges.add(adjacencyMatrix[uIndex][uIndex]);
        }

        return edges.iterator();
    }

    @Override
    public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
        if (!nodes.contains(u) || !nodes.contains(v)) {
            throw new GraphException("One or both of these nodes don't exist in the graph");
        }

        int uIndex = u.getName();
        int vIndex = v.getName();

        if (adjacencyMatrix[uIndex][vIndex] != null || adjacencyMatrix[vIndex][uIndex] != null) {
            // There is an edge between u and v
            return adjacencyMatrix[uIndex][vIndex] != null ? adjacencyMatrix[uIndex][vIndex] : adjacencyMatrix[vIndex][uIndex];
        }

        // If the condition is not met, the edge does not exist
        throw new GraphException("This edge does not exist");
    }

    @Override
    public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
        if (!nodes.contains(u) || !nodes.contains(v)) {
            throw new GraphException("One or both of these nodes don't exist in the graph");
        }

        int uIndex = u.getName();
        int vIndex = v.getName();

        return adjacencyMatrix[uIndex][vIndex] != null || adjacencyMatrix[vIndex][uIndex] != null;
    }
}
