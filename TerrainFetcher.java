import com.sun.xml.internal.xsom.impl.scd.Iterators;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by grbritz on 12/22/15.
 */
public class TerrainFetcher {
    // Sample URL: https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034|36.455556,-116.866667&key=YOUR_API_KEY

    private final ArrayList<String> ApiKeys;
    private final Integer samplesPerKey = 2500;




    public TerrainFetcher() {
        ApiKeys = new ArrayList<String>();
        ApiKeys.add("AIzaSyD-kSmZg5MlVGNolit43y2DMqROfpL41Uc");

    }


    // Fetches the terrain values within the boundaries
    // provided
    //
    // Returns elevations within the boundaries
    public Iterators.Array<Integer> fetchTerrain(Point2D topLeft, Point2D topRight) {

        return null;


    }







}
