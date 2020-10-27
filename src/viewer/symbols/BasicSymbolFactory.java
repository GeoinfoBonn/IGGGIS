package viewer.symbols;

import java.awt.BasicStroke;
import java.awt.Color;

import io.structures.Feature;

/***
 * Basic symbol factory with a stroke and fill color
 *
 *
 */
public class BasicSymbolFactory implements SymbolFactory {

	private Color strokeColor;
	private Color fillColor;
	private float strokeWidth;

	public BasicSymbolFactory(Color strokeColor, Color fillColor) {
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = 1;
	}
	
	public BasicSymbolFactory(Color strokeColor, Color fillColor, float strokeWidth) {
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = strokeWidth;
	}

	@Override
	public Symbol createSymbol(Feature feature) {
//		if (feature.getGeometryType().startsWith("Multi"))
//			System.err.println("WARNING! Feature has geometry of Multi-type (" + feature.getGeometryType()
//					+ ") only first feature is drawn!");

		switch (feature.getGeometryType()) {
		case "Point":
		case "MultiPoint":
			return new PointSymbol(feature, strokeColor);

		case "LineString":
		case "MultiLineString":
			return new LineSymbol(feature, strokeColor, new BasicStroke(strokeWidth));

		case "Polygon":
		case "MultiPolygon":
			return new PolygonSymbol(feature, strokeColor, new BasicStroke(strokeWidth), fillColor);

		default:
			System.err.println("Unknown geometry type! (" + feature.getGeometryType() + ")");
			return null;
		}
	}
}
