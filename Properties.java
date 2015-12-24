import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by grbritz on 12/23/15.
 */
public class Properties {
    private final Integer FEET_MILE = 5280;
    private final double blockSize = 3;


    private Point2D upperLeft;
    private Point2D bottomRight;

    private double scale;


    public Properties(Point2D upperLeft, Point2D bottomRight, double scale) {
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
        this.scale = scale;
    }

    public int totalBlocks() {
        return worldCols() * worldRows();
    }

    public int worldRows() {
        double feetWide = distance(upperLeft, new Point2D.Double(bottomRight.getX(), upperLeft.getY())) * FEET_MILE;
        return (int) ((feetWide / blockSize) * scale);
    }

    public int worldCols() {
        double feetTall = distance(upperLeft, new Point2D.Double(upperLeft.getX(), bottomRight.getY())) * FEET_MILE;
        return (int) ((feetTall / blockSize) * scale);
    }



//    public int numRows() {
//
//    }
//
//    public int numCols() {
//
//    }
//
    public double realWorldHeight() {
        return upperLeft.getY() - bottomRight.getY();
    }

    public double realWorldWidth() {
        return bottomRight.getX()-upperLeft.getX();
    }

    public Point2D getUpperLeft() {
        return upperLeft;
    }

    public Point2D getBottomRight() {
        return bottomRight;
    }

    public double getBlockSize() {
        return blockSize;
    }

    private double distance(Point2D a, Point2D b) {

        double lat1 = a.getY();
        double lon1 = a.getX();
        double lat2 = b.getY();
        double lon2 = b.getX();

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
