// === CS400 File Header Information ===
// Name: Aarya Saxena
// Email: asaxena26@wisc.edu
// Group and Team: P2.3607
// Lecturer: Florian
// Notes to Grader: N/A

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
     * and the nodes linked together through predecessor references to represent
     * all the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
        if (!containsNode(start) || !containsNode(end)){
            throw new NoSuchElementException("start or end node not in graph!");
        }

        PlaceholderMap<NodeType, SearchNode> shortestPaths = new PlaceholderMap<>();
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();

        SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
        shortestPaths.put(start, startNode);
        pq.add(startNode);
        while (!pq.isEmpty()){
            SearchNode currentNode = pq.poll();
            if (currentNode.node.data.equals(end)) {
                return currentNode;
            }
            for (BaseGraph<NodeType, EdgeType>.Edge edge : currentNode.node.edgesLeaving){
                NodeType successors = edge.successor.data;
                double cost = currentNode.cost + edge.data.doubleValue();

                if (!shortestPaths.containsKey(successors) ||
                    cost < shortestPaths.get(successors).cost){
                    if (shortestPaths.containsKey(successors)) {
                        shortestPaths.remove(successors);
                    }
                    SearchNode nextNode = new SearchNode(edge.successor, cost, currentNode);
                    shortestPaths.put(successors, nextNode);
                    pq.add(nextNode);
                }
            }
        }
        throw new NoSuchElementException("No path exists between " + start + " and " + end);
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
        if (!containsNode(start) || !containsNode(end)){
            throw new NoSuchElementException("start or end node not in graph!");
        }
        List<SearchNode> nodeList = pathTaken(start, end);
        return pathConverter(nodeList);
	}

    /**
     * Constructs a list of SearchNodes representing the path from the start node to the end node
     * as determined by Dijkstra's shortest path algorithm.
     *
     * This method uses the result from the `computeShortestPath` method to build the path by
     * following each SearchNode's predecessor references. The resulting list begins with the
     * start node and ends with the end node, in the order of traversal.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return a list of SearchNodes representing the shortest path from the start to the end node
     * @throws NoSuchElementException if there is no path from start to end,
     *                                or if either start or end node is not present in the graph
     */
    private List<SearchNode> pathTaken(NodeType start, NodeType end){
        SearchNode result = computeShortestPath(start, end); // store SearchNode result of algo
        SearchNode variableResult = computeShortestPath(start, end); //variable result for while
        // loop
        SearchNode startNode = new SearchNode(nodes.get(start), 0, null);
        List<SearchNode> list = new LinkedList<>(); // create a list to store the path
        list.add(startNode); //add the starting node
        while (variableResult.predecessor != null && variableResult.predecessor.node.data != start){
            // make sure the predecessor isn't null, and also make sure we don't add the start node
            list.add(1,variableResult.predecessor); //add after start node
            variableResult = variableResult.predecessor; // update reference to result
        }
        list.add(list.size(), result); // add the end node to end
        return list;
    }

    /**
     * Converts a list of SearchNode objects into a list of their corresponding NodeType data.
     *
     * This method iterates over a list of SearchNodes and extracts the data stored in each node,
     * returning a list of NodeType values that represent the path.
     *
     * @param nodeList the list of SearchNode objects to be converted
     * @return a list of NodeType values representing the data in each SearchNode along the path
     */
    private List<NodeType> pathConverter(List<SearchNode> nodeList){
        List<NodeType> pathData = new LinkedList<>(); //convert SearchNode list into a
        // NodeType LinkedList
        for (SearchNode node : nodeList){ //traverse through
            pathData.add(node.node.data); // add the node.data to the NodeType list
        }
        return pathData;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path from the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        SearchNode result = computeShortestPath(start, end); // call dijkstras algo implemented
        // in computeShortestPath()

        return result.cost; //result contains the total path cost
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
}
