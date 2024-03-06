/* 2210A Assignment 5
 * Tsid Tomori
 * class to represent a maze
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Maze {
    private Graph graph;
    private GraphNode entranceNode;
    private GraphNode exitNode;
    private int coins;
    private int width;
    private int length;

    public Maze(String inputFile) throws MazeException, GraphException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));

        //first four lines
        int scale = Integer.parseInt(reader.readLine().trim());
        width = Integer.parseInt(reader.readLine().trim());
        length = Integer.parseInt(reader.readLine().trim());
        coins = Integer.parseInt(reader.readLine().trim());

        graph = new Graph(width * length);

        int nodeCounter = 0; // keeping track of nodes

        for (int i = 0; i < length * 2 - 1; i++) {
            processLine(reader.readLine(), nodeCounter);
        }
    }

    private void processLine(String line, int nodeCounter) throws GraphException {
        int verticalCounter = 0;

        for (int j = 0; j < width * 2 - 1; j++) {
            char symbol = line.charAt(j);
            createNode(symbol, nodeCounter, verticalCounter);
        }
    }

    private void createNode(char symbol, int nodeCounter, int verticalCounter) throws GraphException {
        // room options: o = room, s = entrance, x = exit
        if (symbol == 'o' || symbol == 's' || symbol == 'x') {
            GraphNode room = graph.getNode(nodeCounter);
            nodeCounter++;

            if (symbol == 's') {
                entranceNode = room;
            } else if (symbol == 'x') {
                exitNode = room;
            }
        }
        // corridor option
        else if (symbol == 'c') {
            createCorridor(nodeCounter, verticalCounter);
        }
        // door option
        else if (Character.isDigit(symbol)) {
            createDoor(nodeCounter, verticalCounter, symbol);
        }
    }

    private void createCorridor(int nodeCounter, int verticalCounter) throws GraphException {
        if (nodeCounter % 2 == 0) {
            // horizontal edge
            GraphNode u = graph.getNode(nodeCounter - 1);
            GraphNode v = graph.getNode(nodeCounter);

            graph.insertEdge(u, v, 0, "corridor");
        } else {
            // vertical edge
            GraphNode u = graph.getNode(nodeCounter - width + verticalCounter);
            GraphNode v = graph.getNode(nodeCounter + verticalCounter);

            graph.insertEdge(u, v, 0, "corridor");
            verticalCounter++;
        }
    }

    private void createDoor(int nodeCounter, int verticalCounter, char symbol) throws GraphException {
        if (nodeCounter % 2 == 0) {
            // horizontal edge
            GraphNode u = graph.getNode(nodeCounter - 1);
            GraphNode v = graph.getNode(nodeCounter);

            graph.insertEdge(u, v, Character.getNumericValue(symbol), "door");
        } else {
            // vertical edge
            GraphNode u = graph.getNode(nodeCounter - width + verticalCounter);
            GraphNode v = graph.getNode(nodeCounter + verticalCounter);

            graph.insertEdge(u, v, Character.getNumericValue(symbol), "door");
            verticalCounter++;
        }
    }

    public Graph getGraph() throws MazeException {
        if (graph == null) {
            throw new MazeException("Graph = null");
        }
        return graph;
    }

    public Iterator solve() {
        Stack<GraphNode> path = new Stack<>();
        Set<GraphNode> visited = new HashSet<>();

        try {
            dfs(entranceNode, path, visited);

            // check for solution
            if (!path.isEmpty() && path.peek() == exitNode) {
                return path.iterator();
            } else {
                return null; // no solution
            }
        } catch (GraphException e) {
            e.printStackTrace();
            return null; 
        }
    }

    private void dfs(GraphNode current, Stack<GraphNode> path, Set<GraphNode> visited) throws GraphException {
        path.push(current);
        visited.add(current);

        if (current == exitNode) {
            return;
        }

        Iterator<GraphEdge> edges = graph.incidentEdges(current);
        while (edges.hasNext()) {
            GraphEdge edge = edges.next();
            GraphNode neighbor = edge.secondEndpoint();

            // check if the neighbor is not visited
            if (!visited.contains(neighbor)) {
                // check if the edge is a corridor or if enough coins are available
                if (edge.getLabel().equals("corridor") || coins > edge.getType()) {
                    // recursively explore the neighbor
                    dfs(neighbor, path, visited);
                    // check if the exit is reached
                    if (path.peek() == exitNode) {
                        return;
                    }
                }
            }
        }
        //go back if no solution
        path.pop();
    }
}
