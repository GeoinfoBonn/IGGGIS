package geometry;

/**
 * Extent as minimum bounding box
 * 
 * @author haunert
 *
 */
public class Envelope {

	/**
	 * xMin
	 */
	private double x1;
	/**
	 * yMin
	 */
	private double y1;
	/**
	 * xMax
	 */
	private double x2;
	/**
	 * yMax
	 */
	private double y2;

	public Envelope(double x1, double x2, double y1, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	/**
	 * Constructor
	 */
	public Envelope() {
		this.x1 = Double.POSITIVE_INFINITY;
		this.x2 = Double.NEGATIVE_INFINITY;
		this.y1 = Double.POSITIVE_INFINITY;
		this.y2 = Double.NEGATIVE_INFINITY;
	}

	/**
	 * 
	 * @return xMin
	 */
	public double getxMin() {
		return x1;
	}

	/**
	 * 
	 * @return yMin
	 */
	public double getyMin() {
		return y1;
	}

	/**
	 * 
	 * @return xMax
	 */
	public double getxMax() {
		return x2;
	}

	/**
	 * 
	 * @return yMax
	 */
	public double getyMax() {
		return y2;
	}

	/**
	 * Expand the current envelope by a point (x,y) 
	 * @param x x coordinate of the point
	 * @param y y coordinate of the point
	 */
	public void expandToInclude(double x, double y) {
		x1 = Math.min(x, x1);
		x2 = Math.max(x, x2);
		y1 = Math.min(y, y1);
		y2 = Math.max(y, y2);

	}
	
	/**
	 *  Expand the current envelope by a bounding box
	 * @param boundingBox Bounding box
	 */
	public void expandToInclude(Envelope boundingBox) {
		x1 = Math.min(x1, boundingBox.x1);
		y1 = Math.min(y1, boundingBox.y1);
		x2 = Math.max(x2, boundingBox.x2);
		y2 = Math.max(y2, boundingBox.y2);

	}

	/**
	 * Checks the intersection of two envelopes
	 * @param boundingBox envelope to test
	 * @return boolean value for the result of the intersection check
	 */
	public boolean intersects(Envelope boundingBox) {
		if (x1 > boundingBox.x2)
			return false;
		if (y1 > boundingBox.y2)
			return false;
		if (x2 < boundingBox.x1)
			return false;
		if (y2 < boundingBox.y1)
			return false;
		return true;
	}

	/**
	 * Checks whether the envelope is bounded or not
	 * @return boolean value for the bounding test
	 */
	public boolean isBounded() {
		if (this.x1 == Double.POSITIVE_INFINITY)
			return false;
		if (this.x2 == Double.NEGATIVE_INFINITY)
			return false;
		if (this.y1 == Double.POSITIVE_INFINITY)
			return false;
		if (this.y2 == Double.NEGATIVE_INFINITY)
			return false;
		return true;
	}
	/**
	 * Checks whether this envelope contains another envelope
	 * @param env envelope to be tested
	 * @return boolean value for the containment test
	 */
	public boolean contains(Envelope env) {
		if (x1 > env.x1)
			return false;
		if (y1 > env.y1)
			return false;
		if (x2 < env.x2)
			return false;
		if (y2 < env.y2)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Envelope[x1=" + x1 + ",x2=" + x2 + ",y1=" + y1 + ",y2=" + y2 + "]";
	}

}
