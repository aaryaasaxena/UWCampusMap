import java.util.List;
import java.util.NoSuchElementException;

public class Frontend implements FrontendInterface {
  private BackendInterface backend;

  /**
   * Implementing classes should support the constructor below.
   *
   * @param backend is used for shortest path computations
   */
  public Frontend(BackendInterface backend) {
    this.backend = backend;
  }



  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a text input field with the id="start", for the start location - a
   * text input field with the id="end", for the destination - a button labelled "Find Shortest
   * Path" to request this computation. Ensure that these text fields are clearly labelled, so that
   * the user can understand how to use them.
   *
   * @return an HTML string that contains input controls that the user can make use of to request a
   * shortest path computation
   */
@Override
public String generateShortestPathPromptHTML() {
    return """
        <div>
            <!-- Label and input field for the start location -->
            <label for="start">Start Location:</label>
            <input type="text" id="start" name="start" placeholder="Enter Start Location" />
            <br/>

            <!-- Label and input field for the destination -->
            <label for="end">Destination:</label>
            <input type="text" id="end" name="end" placeholder="Enter Destination" />
            <br/>

            <!-- Input button to trigger the shortest path computation -->
            <input type="button" value="Find Shortest Path" onclick="generateShortestPathResponseHTML()" />
        </div>
        """;
}


  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a paragraph (p) that describes the path's start and end locations - an
   * ordered list (ol) of locations along that shortest path - a paragraph (p) that includes the
   * total travel time along this path Or if there is no such path, the HTML returned should instead
   * indicate the kind of problem encountered.
   *
   * @param start is the starting location to find the shortest path from
   * @param end   is the destination that this shortest path should end at
   * @return an HTML string that describes the shortest path between these two locations
   */
  @Override
  public String generateShortestPathResponseHTML(String start, String end) {
    List<String> locationsOnShortestPath = backend.findLocationsOnShortestPath(start, end);

    StringBuilder htmlResponse = new StringBuilder("<div>");

    if (locationsOnShortestPath.isEmpty()) {
      htmlResponse.append("<p>No Path Found From ").append(start).append(" to ").append(end).append(".</p>");
    } else {
      htmlResponse.append("<p>Shortest Path From ").append(start).append(" to ").append(end).append(":</p>").append("<ol>");

      for (String location : locationsOnShortestPath) {
        htmlResponse.append("<li>").append(location).append("</li>"); // adds values into backend as
        // ordered list
      }
      htmlResponse.append("</ol>");

      double totalTravelTime = 0.0;
      for (Double time : backend.findTimesOnShortestPath(start, end)) {
        totalTravelTime += time;
      }
      htmlResponse.append("<p>Total Travel Time: ").append(totalTravelTime).append(" Minutes</p>");
    }
    htmlResponse.append("</div>");
    return htmlResponse.toString();
  }

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a text input field with the id="from", for the start location - a
   * button labelled "Longest Location List From" to submit this request Ensure that this text field
   * is clearly labelled, so that the user can understand how to use it.
   *
   * @return an HTML string that contains input controls that the user can make use of to request a
   * longest location list calculation
   */
@Override
public String generateLongestLocationListFromPromptHTML() {
    return """
        <div>
            <!-- Label and input field for the start location -->
            <label for="from">Start Location:</label>
            <input type="text" id="from" name="from" placeholder="Enter Start Location" />
            <br/>

            <!-- Input button to trigger the longest location list computation -->
            <input type="button" value="Longest Location List From" onclick="generateLongestLocationListFromResponseHTML()" />
        </div>
        """;
}

  /**
   * Returns an HTML fragment that can be embedded within the body of a larger html page.  This HTML
   * output should include: - a paragraph (p) that describes the path's start and end locations - an
   * ordered list (ol) of locations along that shortest path - a paragraph (p) that includes the
   * total number of locations on path Or if no such path can be found, the HTML returned should
   * instead indicate the kind of problem encountered.
   *
   * @param start is the starting location to find the longest list from
   * @return an HTML string that describes the longest list of locations along a shortest path
   * starting from the specified location
   */
  @Override
  public String generateLongestLocationListFromResponseHTML(String start) {
    try {
      // Retrieve the longest location list starting from the given location
      List<String> locations = backend.getLongestLocationListFrom(start);

      // If no locations are found, return an HTML message indicating so
      if (locations.isEmpty()) {
        return """
              <div>
                  <p>No locations found starting from """ + start + ".</p>" + """
              </div>
              """;
      }

      // Create a StringBuilder to dynamically construct the HTML response
      StringBuilder htmlBuilder = new StringBuilder();

      // Start the HTML div and add a description paragraph
      htmlBuilder.append("<div>");
      htmlBuilder.append("<p>Longest Location List From ").append(start).append(":</p>");

      // Start an ordered list to display the locations
      htmlBuilder.append("<ol>");

      // Loop through each location and add it as a list item
      for (String location : locations) {
        htmlBuilder.append("<li>").append(location).append("</li>");
      }

      // Close the ordered list and add a paragraph with the total number of locations
      htmlBuilder.append("</ol>");
      htmlBuilder.append("<p>Total Locations: ").append(locations.size()).append("</p>");

      // Close the outer div
      htmlBuilder.append("</div>");

      // Convert the StringBuilder content to a string and return it
      return htmlBuilder.toString();
    } catch (NoSuchElementException e) {
      // Handle the case where getLongestLocationListFrom throws NoSuchElementException
      return """
          <div>
              <p>Error: No such element found for the start location """ + " " + start + ".</p>" + """
          </div>
          """;
    }
  }
}

