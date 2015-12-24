import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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


// "AIzaSyD-kSmZg5MlVGNolit43y2DMqROfpL41Uc"

/**
 * Created by grbritz on 12/22/15.
 */
public class ElevationFetcher {
    // Sample URL: https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034|36.455556,-116.866667&key=YOUR_API_KEY

    private final List<String> ApiKeys;
    private final Integer samplesPerKey = 2500;
    private final Integer resultsPerSample = 512;

    private final double mapWidth = 10.15 * 5280; //10 miles wide * 5280 ft/mile
    private final double mapHeight = 14.22 * 5280; // 14 miles high * 5280 ft/mile
    private final double blockSize = 2.5; // 2.5 ft
    private final double totalBlocks = (mapWidth * mapHeight) / blockSize;


    private Properties propList;


    public ElevationFetcher(Properties propList, List<String> apiKeys) {
        ApiKeys = apiKeys;
        this.propList = propList;
    }


    // Fetches the terrain values within the boundaries
    // provided
    //
    // Returns elevations within the boundaries
    public List<List<Double>> fetchElevations() {
        int scaledCols = (int) (propList.worldCols() * blockDensityRatio());
        int scaledRows = (int) (propList.worldRows() * blockDensityRatio());

        Point2D upperLeft = propList.getUpperLeft();

        List<List<Double>> elevationsList = new ArrayList<>();


        for(int row = 0; row < scaledRows; row++) {
            double rowOffset = propList.realWorldHeight() / scaledRows;

            // Number of requests for this row
            int numRequests = (int) Math.ceil((double)scaledCols / resultsPerSample);

            // TODO: This is northern hemisphere only atm
            double currY = upperLeft.getY() - row * rowOffset;
            
            List<Double> elevationsRow = new ArrayList<>();

            double leftOffset = 0;
            for(int request = 0; request < numRequests; request++) {
                try {
                    Thread.sleep(200);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }


                int numSamples = Math.min(scaledCols - request * resultsPerSample, resultsPerSample);
                double coordOffset = (double)numSamples/scaledCols * propList.realWorldWidth();

                Point2D.Double leftPt =  new Point2D.Double(upperLeft.getX() + leftOffset, currY);
                Point2D.Double rightPt =  new Point2D.Double(leftPt.getX() + coordOffset, currY);

                ArrayList<Point.Double> locations = new ArrayList<Point.Double>();
                locations.add(leftPt);
                locations.add(rightPt);

                String altitudesString = runQuery(locations, numSamples);
                System.out.println(altitudesString);
                JsonObject jsonObject = new JsonParser().parse(altitudesString).getAsJsonObject();
                List<Double> elevations = getElevations(jsonObject);
                
                elevationsRow.addAll(elevations);
                leftOffset += coordOffset;

            }
            
            elevationsList.add(elevationsRow);
        }
        return elevationsList;
    }

    public double blockDensityRatio() {
        return (double) totalSamples() / propList.totalBlocks();
    }

    private int totalSamples() {
        int maxSamples = ApiKeys.size() * samplesPerKey * resultsPerSample;
        return Math.min(propList.totalBlocks(), maxSamples);
    }
//
    private List<Double> getElevations(JsonObject queryResults) {
        JsonArray results = queryResults.getAsJsonArray("results");

        ArrayList<Double> elevations = new ArrayList<>();

        for (JsonElement result: results) {
            JsonObject currObj = result.getAsJsonObject();

            double elevationSample = currObj.get("elevation").getAsDouble();

            // Offset by 1 meter because of errors in the data - EMPIRCAL
            elevations.add(elevationSample - 1);
        }

        return elevations;
    }
//
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

            //System.out.println(queryResult);
            return queryResult.toString();
        }
        catch(Exception e) {
            System.out.println(e);
        }


        return "";
    }
}
