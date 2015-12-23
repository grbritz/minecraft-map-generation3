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


    /**
     * Returns the distance between two coordinates in feet
     * @param a
     * @param b
     * @return distance in feet
     */
    public static float distance(Point2D a, Point2D b) {

        double lat1 = a.getY();
        double lng1 = a.getX();
        double lat2 = b.getY();
        double lng2 = b.getX();

        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double d = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(d), Math.sqrt(1-d));
        float dist = (float) (earthRadius * c);

        // Convert to feet
        return dist * 3.28084f;
    }



}
