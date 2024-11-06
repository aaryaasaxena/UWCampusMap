import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class BackendTests {

    /**
     * Tests loadGraphData() and getListOfAllLocations() to see if they work correctly
     */
    @Test
   public void roleTest1() {
     Graph_Placeholder graph = new Graph_Placeholder();
     Backend backend = new Backend(graph);
     // this should not throw an error because it's a valid file name
     boolean error = false;
     try {
        backend.loadGraphData("campus.dot");
     } catch (IOException e) {
        error = true;
     }
     List<String> locations = backend.getListOfAllLocations();
     // should contain the hardcoded Union South should be there, and it should only read the first line which contains Memorial Union.
     Assertions.assertTrue(locations.contains("Union South") && locations.contains("Memorial Union") && !error);
     Assertions.assertEquals(4, locations.size());

     // here we will do an invalid file name to see if it correctly throws an exception
     try {
         backend.loadGraphData("dkfksd");
     } catch (IOException e) {
         error = true;
     }
     Assertions.assertTrue(error);

    }

    /**
     * Tests findTimesOnShortestPath() and findLocationsOnShortestPath()
     */
    @Test
    public void roleTest2() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        List<String> shortest = backend.findLocationsOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        List<Double> edge = backend.findTimesOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
        // should contian the original 3
        Assertions.assertEquals(3, shortest.size());
        // should be 5 because of how placeholder works: adding index 1 and index 2 = 3.
        Assertions.assertEquals(3, edge.get(0) + edge.get(1));
        // here we will test one that should just be from the same location to itself
        List<String> shortest2 = backend.findLocationsOnShortestPath("Union South", "Union South");
        List<Double> edge2 = backend.findTimesOnShortestPath("Union South", "Union South");
        // should be 1 because only one location on the path
        Assertions.assertEquals(1, shortest2.size());
        // should be empty because there are no edges
        Assertions.assertEquals(0, edge2.size());
        // here we will test one that should give an empty list
        List<String> shortest3 = backend.findLocationsOnShortestPath("", "");
        List<Double> edge3 = backend.findTimesOnShortestPath("", "");
        // should be empty because no valid nodes are listed
        Assertions.assertEquals(0, shortest3.size());
        // should be 0 for the same reason
        Assertions.assertEquals(0, edge3.size());
    }

    /**
     * Tests getLongestLocationListFrom()
     */
    @Test
    public void roleTest3() {
        Graph_Placeholder graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        List<String> locations = backend.getLongestLocationListFrom("Union South");
        // should be the entire path
        Assertions.assertEquals(3, locations.size());

        List<String> locations2 = backend.getLongestLocationListFrom("Computer Sciences and Statistics");
        // should be only the last 2
        Assertions.assertTrue(locations2.size() == 2 && locations2.get(0).equals("Computer Sciences and Statistics"));

        List<String> locations3 = backend.getLongestLocationListFrom("Atmospheric, Oceanic and Space Sciences");
        // Should be only the last one
        Assertions.assertTrue(locations3.size() == 1);

        boolean error = false;
        try {
            List<String> locations4 = backend.getLongestLocationListFrom("");
        } catch (NoSuchElementException e) {
            error = true;
        }
        // there should be an error since this was not a valid node
        Assertions.assertTrue(error);

    }





}
