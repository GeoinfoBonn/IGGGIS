package io.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.locationtech.jts.geom.Geometry;

/***
 * 
 * Class representing a feature with geometry and attributes
 *
 */
public class Feature {

	private Geometry geometry;
	private HashMap<String, Object> attributes;

	/**
	 * Initializes this feature without attributes.
	 * 
	 * @param geom the geometry of this feature
	 */
	public Feature(Geometry geom) {
		this(geom, null);
	}

	/**
	 * Initializes this feature with geometry and attributes.
	 * 
	 * @param geom       the geometry of this feature
	 * @param attributes the map of attributes for this feature
	 */
	public Feature(Geometry geom, HashMap<String, Object> attributes) {
		this.geometry = geom;
		if (attributes == null)
			this.attributes = new HashMap<String, Object>();
		else
			this.attributes = attributes;
	}

	/**
	 * Returns the (JTS-) geometry of this feature
	 * 
	 * @return a JTS geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Sets the attribute for this geometry. Returns true if former attribute value
	 * got replaced, false if attribute is a new attribute.
	 * 
	 * @param key   name of the attribute
	 * @param value value of the attribute
	 * @return true if feature previously had a value for this attribute
	 */
	public boolean setAttribute(String key, Object value) {
		boolean replace = hasAttribute(key);
		attributes.put(key, value);
		return replace;
	}

	/**
	 * Returns if the feature currently has a value for the given attribute.
	 * 
	 * @param name name of the attribute
	 * @return boolean value for the result
	 */
	public boolean hasAttribute(String name) {
		return attributes.containsKey(name);
	}

	/**
	 * Returns the value of the attribute, or null if feature does not have this
	 * attribute.
	 * 
	 * @param name name of the attribute
	 * @return attribute value or null if absent
	 */
	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Returns a String representation of this features geometry type.
	 * 
	 * @return possible values: GeometryCollection, LinearRing, LineString,
	 *         MultiLineString, MultiPoint, MultiPolygon, Point, Polygon
	 */
	public String getGeometryType() {
		return geometry.getGeometryType();
	}

	/**
	 * Returns a list of all the attribute names this feature has.
	 * 
	 * @return ArrayList of the names
	 */
	public List<String> getAttributeNames() {
		return new ArrayList<>(attributes.keySet());
	}
}
