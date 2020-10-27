package viewer.symbols;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import io.structures.Feature;
import viewer.base.Transformation;

/**
 * A line segment that can be added to a map
 * 
 * @author haunert
 */
public class LineSymbol extends Symbol {

	protected Stroke stroke = new BasicStroke();
	protected Color strokeColor = Color.BLACK;

	/**
	 * 
	 * @param feature feature which the line is associated to
	 */
	public LineSymbol(Feature feature) {
		super(feature);
	}

	/**
	 * 
	 * @param feature feature
	 * @param strokeColor stroke color 
	 */
	public LineSymbol(Feature feature, Color strokeColor) {
		this(feature);
		this.strokeColor = strokeColor;
	}

	/**
	 * 
	 * @param feature feature which the polygon is associated to
	 * @param strokeColor stroke color
	 * @param stroke stroke object
	 */
	public LineSymbol(Feature feature, Color strokeColor, Stroke stroke) {
		this(feature, strokeColor);
		this.stroke = stroke;
	}

	@Override
	public void draw(Graphics2D g, Transformation t) {
		Color oldColor = g.getColor();
		g.setColor(strokeColor);

		Stroke oldStroke = g.getStroke();
		g.setStroke(stroke);

		for (int k = 0; k < this.myFeature.getGeometry().getNumGeometries(); k++) {
			Geometry geom = myFeature.getGeometry().getGeometryN(k);
			drawSingle(g, t, geom);
		}

		g.setColor(oldColor);
		g.setStroke(oldStroke);
	}

	/**
	 * 
	 * @param g    graphics2D object
	 * @param t    transformation
	 * @param geom geometry
	 */
	private void drawSingle(Graphics2D g, Transformation t, Geometry geom) {

		if (geom instanceof LineString) {
			LineString myLineString = (LineString) geom;
			Coordinate[] coords = myLineString.getCoordinates();
			int n = coords.length;
			int[] x = new int[n];
			int[] y = new int[n];
			for (int i = 0; i < n; i++) {
				x[i] = t.getColumn(coords[i].x);
				y[i] = t.getRow(coords[i].y);
			}
			g.drawPolyline(x, y, n);
		}
	}
}
