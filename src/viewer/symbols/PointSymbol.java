package viewer.symbols;

import java.awt.Color;
import java.awt.Graphics2D;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import io.structures.Feature;
import viewer.base.Transformation;

/**
 * Marker for a point map feature. The feature is marked on the map using a
 * circle.
 */
public class PointSymbol extends Symbol {

	protected Color color = Color.BLACK;
	protected int pointWidth = 5;

	/**
	 * 
	 * @param feature feature which the point is associated to
	 */
	public PointSymbol(Feature feature) {
		super(feature);
	}

	/**
	 * 
	 * @param feature feature which the point is associated to
	 * @param color color of the point
	 */
	public PointSymbol(Feature feature, Color color) {
		this(feature);
		this.color = color;
	}

	/**
	 * 
	 * @param feature feature
	 * @param color color
	 * @param pointWidth point width
	 */
	public PointSymbol(Feature feature, Color color, int pointWidth) {
		this(feature, color);
		this.pointWidth = pointWidth;
	}

	@Override
	public void draw(Graphics2D g, Transformation t) {
		Color oldColor = g.getColor();
		g.setColor(color);

		for (int k = 0; k < this.myFeature.getGeometry().getNumGeometries(); k++) {
			Geometry geom = this.myFeature.getGeometry().getGeometryN(k);
			drawSingle(g, t, geom);
		}

		g.setColor(oldColor);
	}

	/***
	 * draws a point into a specified Graphics object
	 * 
	 * @param g    Graphics object
	 * @param t    transformation
	 * @param geom geometry
	 */
	private void drawSingle(Graphics2D g, Transformation t, Geometry geom) {

		if (geom instanceof Point) {
			Point myPoint = (Point) geom;
			g.fillOval(t.getColumn(myPoint.getX()) - pointWidth / 2, t.getRow(myPoint.getY()) - pointWidth / 2,
					pointWidth, pointWidth);
		}
	}
}
