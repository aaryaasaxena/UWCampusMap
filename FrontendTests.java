import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FrontendTests {

  @Test
  public void roleTest1() {
    Graph_Placeholder graph = new Graph_Placeholder();
    Backend_Placeholder backend = new Backend_Placeholder(graph);
    Frontend frontend = new Frontend(backend);


    String html = frontend.generateShortestPathPromptHTML();

    System.out.println(html);

    assertNotNull(html);
    assertTrue(html.contains("id=\"start\""));
    assertTrue(html.contains("Find Shortest Path"));
    assertTrue(html.contains("id=\"end\""));
  }

  @Test
  public void roleTest2() {
    Graph_Placeholder graph = new Graph_Placeholder();
    Backend_Placeholder backend = new Backend_Placeholder(graph);
    Frontend frontend = new Frontend(backend);


    String start = "Union South";
    String end = "Computer Sciences and Statistics";

    String html = frontend.generateShortestPathResponseHTML(start, end);

    assertNotNull(html);
    assertTrue(
        html.contains("Shortest Path From Union South to Computer Sciences and " + "Statistics"));
    assertTrue(html.contains("Total Travel Time: 3.0 Minutes"));
    assertTrue(html.contains("<ol>"));
    assertTrue(html.contains("<li>"));
    assertTrue(html.contains("</p>"));

  }

  @Test
  public void roleTest3() {
    Graph_Placeholder graph = new Graph_Placeholder();
    Backend_Placeholder backend = new Backend_Placeholder(graph);
    Frontend frontend = new Frontend(backend);

    String html = frontend.generateLongestLocationListFromPromptHTML();

    assertNotNull(html);
    assertTrue(html.contains("id=\"from\""));
    assertTrue(html.contains("Longest Location List From"));
  }

  @Test
  public void roleTest4() {
    Graph_Placeholder graph = new Graph_Placeholder();
    Backend_Placeholder backend = new Backend_Placeholder(graph);
    Frontend frontend = new Frontend(backend);


    String start = "Camp Randall";
    String end = "Witte Residence Hall";

    String html = frontend.generateShortestPathResponseHTML(start, end);


    assertNotNull(html);
    assertTrue(html.contains("No Path Found From Camp Randall to Witte Residence Hall"));
  }

  /**
   * test 1: Verify HTML generation for the shortest path with a different scenario
   *
   * @throws IOException
   */
  @Test
  public void integrationTest1() throws IOException {

    DijkstraGraph graph = new DijkstraGraph();
    Backend backend = new Backend(graph);
    Frontend frontend = new Frontend(backend);

    backend.loadGraphData("campus.dot");

    // start and end locations for the shortest path test
    String start = "Memorial Union";
    String end = "Science Hall";

    // create the HTML response for the shortest path
    String html = frontend.generateShortestPathResponseHTML(start, end);

    // verify the HTML is not null and contains the expected elements
    assertNotNull(html);

    // assert that the generated HTML contains the correct information
    assertTrue(html.contains("Shortest Path From Memorial Union to Science Hall"));
    assertTrue(html.contains("Total Travel Time: 105.8 Minutes"));
    assertTrue(html.contains("<ol>"));
    assertTrue(html.contains("<li>"));
  }

  /**
   * test 2: Verify response when no locations can be found in the longest location list, catches
   * exception
   *
   * @throws IOException
   */
  @Test
  public void integrationTest2() throws IOException {
    DijkstraGraph graph = new DijkstraGraph();
    Backend backend = new Backend(graph);
    Frontend frontend = new Frontend(backend);

    backend.loadGraphData("campus.dot");

    // start location that doesn't exist in the graph
    String start = "random location";

    // create the HTML response for the longest location list
    String html = frontend.generateLongestLocationListFromResponseHTML(start);

    // verify the HTML is not null and contains the appropriate error message
    assertNotNull(html);

    // assert that the generated HTML contains the error message indicating no location found
    assertTrue(html.contains("No such element found for the start location random location"));
  }

  /**
   * integration test 3: verify longest location list response when a path exists
   *
   * @throws IOException
   */
  @Test
  public void integrationTest3() throws IOException {
    DijkstraGraph graph = new DijkstraGraph();
    Backend backend = new Backend(graph);
    Frontend frontend = new Frontend(backend);

    backend.loadGraphData("campus.dot");

    //start location for the longest location list
    String start = "Science Hall";

    // create the HTML response for the longest location list
    String html = frontend.generateLongestLocationListFromResponseHTML(start);


    // verify the HTML is not null and contains the expected locations
    assertNotNull(html);
    assertTrue(html.contains("Longest Location List From Science Hall"));
    assertTrue(html.contains("Science Hall"));
    assertTrue(html.contains("Radio Hall"));
    assertTrue(html.contains("Education Building"));
    assertTrue(html.contains("South Hall"));
    assertTrue(html.contains("Law Building"));
    assertTrue(html.contains("Luther Memorial Church"));
    assertTrue(html.contains("Noland Hall"));
    assertTrue(html.contains("<ol>"));
    assertTrue(html.contains("<li>"));
    assertTrue(html.contains("Total Locations"));
  }

  /**
   * test 4: Verify response when no locations can't be found in longest location list
   *
   * @throws IOException
   */
  @Test
  public void integrationTest4() throws IOException {
    DijkstraGraph graph = new DijkstraGraph();
    Backend backend = new Backend(graph);
    Frontend frontend = new Frontend(backend);
    backend.loadGraphData("campus.dot");

    // start location that doesn't exist in the graph and a valid end location
    String start = "random location";
    String end = "DeLuca Biochemistry Laboratories";

    // create HTML response for the shortest path between the invalid start and valid end
    String html = frontend.generateShortestPathResponseHTML(start, end);

    // HTML is not null and contains the appropriate error message
    assertNotNull(html);

    // assert that the generated HTML contains the error message for no path found
    assertTrue(html.contains("No Path Found From random location to DeLuca Biochemistry Laboratories"));
    assertTrue(html.contains("</p>"));
  }
}

