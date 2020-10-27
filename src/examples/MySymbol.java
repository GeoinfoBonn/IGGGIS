package examples;
import java.awt.Color;
import java.awt.Graphics2D;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import io.structures.Feature;
import viewer.base.Transformation;
import viewer.symbols.Symbol;

public class MySymbol extends Symbol {
	
	Color fillColor;
	
	public MySymbol(Feature feature, Color fillColor) {
		super(feature);
		this.fillColor = fillColor;
	}
	
	@Override
	public void draw(Graphics2D g, Transformation t) {
		Geometry geom = this.myFeature.getGeometry().getGeometryN(0);
		if (geom instanceof Polygon) {
			java.awt.Polygon p = new java.awt.Polygon();
			for (Coordinate c : ((Polygon) geom).getExteriorRing().getCoordinates()) {
				p.addPoint(t.getColumn(c.x), t.getRow(c.y));
			}
			Color old = g.getColor();
			g.setColor(fillColor);
			g.fillPolygon(p);
			g.setColor(old);
			g.drawPolygon(p);
		}
	}
}