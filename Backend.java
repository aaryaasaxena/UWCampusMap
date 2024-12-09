import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.NoSuchElementException;

public class Backend implements BackendInterface {

    // private graph field
    private GraphADT<String, Double> graph;
    public Backend(GraphADT<String,Double> graph) {
        this.graph = graph;
    }
    /**
     * Loads graph data from a dot file.  If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @java.lang.Override
    public void loadGraphData(String filename) throws IOException {
        // path of all nodes in the graph to clear the graph first
        List<String> allNodes = new ArrayList<>(graph.getAllNodes());
        // remove all nodes in the graph to clear before trying to read.
        for (String node : allNodes) {
            graph.removeNode(node);
        }
       try (BufferedReader br = new BufferedReader(new FileReader(filename)))  {
           // current line being read from the file
           String currentLine;
           // keep reading until there are no more lines
           while ((currentLine = br.readLine()) != null) {
               // skip the line if it is not one of the nodes / weight
               if (currentLine.isEmpty() || currentLine.startsWith("digraph") || currentLine.startsWith("}") || currentLine.startsWith("{")) {
                   continue;
               }
               // call helper method to split the line into relevant parts
               String[] parts = parseLine(currentLine);
               // this regex gets rid of the quotes around the predecessor
               String pred = parts[0].trim().replace("\"", "");
               //Same thing for the successor
               String succ = parts[1].trim().replace("\"", "");
               // Parse for the double value in the weight entry
               Double weight = Double.parseDouble(parts[2].trim());
               // insert nodes, and edges.
               graph.insertNode(pred);
               graph.insertNode(succ);
               graph.insertEdge(pred, succ, weight);

           }
       }
    }

    /**
     * Private helper method to split lines from loaded file into relevant parts
     * @param line
     * @return the split line
     */
    private String[] parseLine(String line) {
        line = line.trim();
        //regex to split into , and weight.
        return line.split("->|\\[seconds=|];");


    }

    /**
     * Returns a list of all locations (node data) available in the graph.
     * @return list of all location names
     */
    @java.lang.Override
    public List<String> getListOfAllLocations() {
        // simply return list of all nodes in the graph
        return graph.getAllNodes();
    }

    /**
     * Returns the sequence of locations along the shortest path between startLocation and endLocation.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list of nodes along the shortest path, or an empty list if no path exists
     */
    @java.lang.Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
        // List for the shortest locations
        List<String> shortestLocations = null;
        try {
            shortestLocations = graph.shortestPathData(startLocation, endLocation);
        } catch (NoSuchElementException e) {
            // return empty list if shortestPathData throws NoSuchElementException
            return Collections.emptyList();
        }

        // if the path is null, we just return an empty list.
        if (shortestLocations == null) {
            return Collections.emptyList();
        }
        // otherwise, we just return the list given by the shortestPathData method
        return shortestLocations;
    }

    /**
     * Return the walking times in seconds between each two nodes on the
     * shortest path from startLocation to endLocation, or an empty list of no
     * such path exists.
     * @param startLocation the start location of the path
     * @param endLocation the end location of the path
     * @return a list with the walking times in seconds between two nodes along
     *         the shortest path from startLocation to endLocation, or an empty
     *         list if no such path exists
     */
	@Override
public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
    // Get all locations on the shortest path
    List<String> shortestPath = findLocationsOnShortestPath(startLocation, endLocation);
    
    // If the path is empty, no edges exist
    if (shortestPath.isEmpty()) {
        return Collections.emptyList();
    }

    // If the path contains only one location (start == end), no travel time is required
    if (shortestPath.size() == 1) {
        return Collections.singletonList(0.0); // or simply return an empty list if no edge traversal is needed
    }

    // Otherwise, calculate the edge weights for the shortest path
    List<Double> shortestPathTimes = new ArrayList<>();
    for (int i = 1; i < shortestPath.size(); i++) {
        shortestPathTimes.add(graph.getEdge(shortestPath.get(i - 1), shortestPath.get(i)));
    }

    return shortestPathTimes;
}

    /**
     * Returns the longest list of locations along any shortest path that starts
     * from startLocation and ends at any of the reachable destinations in the
     * graph.
     * @param startLocation the location to search through paths leaving from
     * @return the longest list of locations found on any shortest path that
     *         starts at the specified startLocation.
     * @throws NoSuchElementException if startLocation does not exist, or if
     *         there are no other locations that can be reached from there
     */
    @java.lang.Override
    public List<String> getLongestLocationListFrom(String startLocation) throws NoSuchElementException {
        // throws exception if the starting location does not exist
        if (!graph.containsNode(startLocation)) {
            throw new NoSuchElementException("Given starting location does not exist");
        }
        // list of all locations to iterate over
        List<String> locations = getListOfAllLocations();
        // int used to keep track of the current longest path
        int maxLength = 0;
        // list used to store the current longest path
        List<String> longestList = null;
        // iterate over all the locations to see what the end location is with the longest path from the starting location
        for (String location : locations) {
            if ((findLocationsOnShortestPath(startLocation, location).size()) > maxLength) {
                longestList = findLocationsOnShortestPath(startLocation, location);
                maxLength = longestList.size();
            }
        }
        // if there are no paths (no other node is reachable), we throw an exception
        if (maxLength == 0) {
            throw new NoSuchElementException("There are no reachable destinations from this starting node.");
        }
        return longestList;
    }

}
