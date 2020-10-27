package viewer.symbols;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import geometry.Envelope;
import io.structures.Feature;
import viewer.base.Transformation;

/**
 * abstract class that must be implemented by all classes representing symbols
 * that can be displayed in a map
 * 
 * @author haunert
 */
public abstract class Symbol {

	/**
	 * The feature this symbol represents
	 */
	protected Feature myFeature;

	/**
	 * Predefined stroke for a dashed line.
	 */
	public static final Stroke DASHED = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1,
			new float[] { 5, 8 }, 0);

	/**
	 * Predefined stroke for a dotted line.
	 */
	public static final Stroke DOTTED = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1,
			new float[] { 2, 2 }, 0);

	/**
	 * Default constructor to enforce setting of feature.
	 * 
	 * @param feature the feature this symbol represents
	 */
	public Symbol(Feature feature) {
		this.myFeature = feature;
	}

	/**
	 * This method is called to draw this Symbol into a specified Graphics object.
	 * 
	 * @param g the Graphics object
	 * @param t the Transformation that maps world coordinates to image coordinates
	 */
	public abstract void draw(Graphics2D g, Transformation t);

	/**
	 * @return the bounding box
	 */
	public Envelope getBoundingBox() {
		org.locationtech.jts.geom.Envelope jtsEnvelope = myFeature.getGeometry().getEnvelopeInternal();
		return new Envelope(jtsEnvelope.getMinX(), jtsEnvelope.getMaxX(), jtsEnvelope.getMinY(), jtsEnvelope.getMaxY());
	}

	/**
	 * Returns the feature this Symbol represents.
	 * 
	 * @return a feature, consisting of geometry and attributes
	 */
	public Feature getFeature() {
		return myFeature;
	}
}
