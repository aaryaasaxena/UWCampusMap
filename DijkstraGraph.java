// === CS400 File Header Information ===
// Name: Aarya Saxena
// Email: asaxena26@wisc.edu
// Group and Team: <your group name: two letters, and team color>
// Group TA: <name of your group's ta>
// Lecturer: Florian
// Notes to Grader: <optional extra notes>

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;


/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }


    @Test
    public void testShortestPath(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);


        DijkstraGraph<String, Double>.SearchNode result = graph.computeShortestPath("A", "E");

        assertEquals("E", result.node.data, "End node should be E.");

        List<String> expectedPath = List.of("A", "B", "D", "E");
        List<String> actualPath = new LinkedList<>();
        while (result != null) {
            actualPath.add(0, result.node.data);  // Build path from start to end
            result = result.predecessor;
        }
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testShortestPathData(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        assertEquals(Arrays.asList("A", "B", "D", "E"), graph.shortestPathData("A", "E"));
        assertEquals(Arrays.asList("A", "B", "D", "F"), graph.shortestPathData("A", "F"));
        
    }

    @Test
    public void testShortestPathCost(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        assertEquals(8.0, graph.shortestPathCost("A", "E"));
        assertEquals(5.0, graph.shortestPathCost("A", "F"));

    }

    @Test
    public void testNoPath(){
        DijkstraGraph<String, Double> graph = new DijkstraGraph<>();

        graph.insertNode("A");
        graph.insertNode("B");
        graph.insertNode("C");
        graph.insertNode("D");
        graph.insertNode("E");
        graph.insertNode("F");
        graph.insertNode("G");
        graph.insertNode("H");

        graph.insertEdge("A", "C", 2.0);
        graph.insertEdge("A", "B", 4.0);
        graph.insertEdge("A", "E", 15.0);
        graph.insertEdge("B", "E", 10.0);
        graph.insertEdge("B", "D", 1.0);
        graph.insertEdge("C", "D", 5.0);
        graph.insertEdge("D", "E", 3.0);
        graph.insertEdge("D", "F", 0.0);
        graph.insertEdge("F", "D", 2.0);
        graph.insertEdge("F", "H", 4.0);
        graph.insertEdge("G", "H", 4.0);

        assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("A", "G"), "no " +
            "path found!");
        assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("A", "G"), "no " +
            "path found!");
        assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("C", "G"), "no " +
            "path found!");
        assertThrows(NoSuchElementException.class, () -> graph.shortestPathCost("C", "G"), "no " +
            "path found!");

    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new PlaceholderMap<>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method it represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        // implement in step 5.3
        return null;
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shortest path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
        // implement in step 5.4
        return null;
	}

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        // implement in step 5.4
        return Double.NaN;
    }

    // TODO: implement 3+ tests in step 4.1
}
