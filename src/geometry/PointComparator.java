package geometry;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * Lexicographical order for points
 * 
 * @author haunert
 */
public class PointComparator implements Comparator<Point2D> {

	/**
	 * implementation of compare method of interface Comparator
	 */
	@Override
	public int compare(Point2D p1, Point2D p2) {
		if (p1.getX() > p2.getX() || (p1.getX() == p2.getX() && p1.getY() > p2.getY()))
			return 1;
		if (p1.getX() == p2.getX() && p1.getY() == p2.getY())
			return 0;
		return -1;
	}

}
