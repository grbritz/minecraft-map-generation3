import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;


/**
 * Created by grbritz on 12/22/15.
 */
public class TerrainFetcher {
    // Sample URL: https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034|36.455556,-116.866667&key=YOUR_API_KEY

    private final ArrayList<String> ApiKeys;
    private final Integer samplesPerKey = 2500;
    private final Integer resultsPerSample = 512;
    private final double mapWidth = 10.15 * 5280; //10 miles wide * 5280 ft/mile
    private final double mapHeight = 14.22 * 5280; // 14 miles high * 5280 ft/mile
    private final double blockSize = 2.5; // 2.5 ft
    private final double totalBlocks = (mapWidth * mapHeight) / blockSize;



    public TerrainFetcher() {
        ApiKeys = new ArrayList<String>();
        ApiKeys.add("AIzaSyD-kSmZg5MlVGNolit43y2DMqROfpL41Uc");

    }


    // Fetches the terrain values within the boundaries
    // provided
    //
    // Returns elevations within the boundaries
    public List<List<Integer>> fetchTerrain(Point.Double topLeft, Point.Double botRight) {
        double conversion = maxResultsAvailable() / totalBlocks;
        int rowWidth = (int) (conversion * mapWidth);
        int numRows = (int) (conversion * mapHeight);
        double deltaLat = (topLeft.getY() - botRight.getY()) / numRows;

        List<List<Integer>> results = new ArrayList<List<Integer>>(numRows);

        //for(int h = 0; h < numRows; h++) {
        for(int h =0; h < 1; h++){
            // interpolate new y coordinate (latitude value), the google api
            // will interpolate the points between our left and right pt
            // in the longitude direction
            Point.Double leftPt = new Point.Double(topLeft.getX(), topLeft.getY() - h * deltaLat);
            Point.Double rightPt = new Point.Double(botRight.getX(), leftPt.getY());
            ArrayList<Point.Double> locations = new ArrayList<>();
            locations.add(leftPt);
            locations.add(rightPt);

            String altitudesString = runQuery(locations, rowWidth);
            //https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034|36.455556,-116.866667&key=YOUR_API_KEY

        }

        return results;
    }

    //TODO: Add diff apiKey support
    private String runQuery(List<Point.Double> locationsLine, int numSamples) {
        String address = "https://maps.googleapis.com/maps/api/elevation/json";
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();

        StringBuilder locationsBuilder = new StringBuilder();

        for(Point.Double location : locationsLine) {
            locationsBuilder.append(String.format("%f,%f|", location.getY(), location.getX()));
        }

        String locationsParam  = locationsBuilder.substring(0, locationsBuilder.length() - 1); // remove trailing pipe char

        String params;
        try {

            //TODO: Add diff apiKey support
            params = String.format("path=%s&samples=%d&key=",
                    URLEncoder.encode(locationsParam, charset),
                    numSamples,
                    ApiKeys.get(0));


            URLConnection connection = new URL(address + "?" + params).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(response));
            StringBuilder queryResult = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                queryResult.append(line);
            }

            System.out.println(queryResult);
            return queryResult.toString();
        }
        catch(Exception e) {
            System.out.println(e);
        }


        return "";
    }

//    private Integer mapWidth(Point2D topLeft, Point2D botRight) {
//        return abs(botRight.getX() - topLeft.getX());
//    }
//
//    private Integer mapHeight(Point2D topleft, point2D botRight) {
//        return abs(topLeft.getY() - botRight.getY());
//    }

    private Integer maxResultsAvailable() {
        return ApiKeys.size() * samplesPerKey * resultsPerSample;
    }

}
