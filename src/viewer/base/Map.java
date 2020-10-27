package viewer.base;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.TreeMap;

import javax.swing.JComponent;

import geometry.Envelope;
import viewer.symbols.Symbol;

/**
 * This class represents a simple GUI component that allows Symbols to be
 * displayed.
 * 
 * @author haunert
 */
public class Map extends JComponent implements MouseListener, MouseMotionListener {

	/**
	 * The class JComponent is serializable, so we need a serialVersionUID.
	 */
	private static final long serialVersionUID = 530617013091206685L;

	/**
	 * If true, the map content is automatically scaled to fit this display frame
	 * when its size is changed.
	 */
	private boolean autoFitToDisplay;

	/**
	 * If frameRation {@literal >} 0, a margin is left between the map and the frame
	 * of the display.
	 */
	private double frameRatio;

	/**
	 * The Layers of Symbols that are displayed in this Map.
	 */
	private TreeMap<Integer, Layer> layers;

	/**
	 * The transformation that is used to transform map coordinates to image
	 * coordinates.
	 */
	private Transformation myTransformation;

	/**
	 * The column of the mouse cursor when the mouse was pressed the last time.
	 */
	private int mouseColumn;

	/**
	 * The row of the mouse cursor when the mouse was pressed the last time.
	 */
	private int mouseRow;

	/**
	 * The minimum x coordinate of the map.
	 */
	private double xMin;

	/**
	 * The minimum y coordinate of the map.
	 */
	private double yMin;

	/**
	 * The maximum x coordinate of the map.
	 */
	private double xMax;

	/**
	 * The maximum y coordinate of the map.
	 */
	private double yMax;

	/**
	 * The MapFrame containing this MapDisplay.
	 */
	private MapPanel myMapFrame;

	/**
	 * This constructor generates a new empty MapDisplay.
	 * @param m map panel
	 */

	public Map(MapPanel m) {
		super();
		myMapFrame = m;
		autoFitToDisplay = true;
		myTransformation = new Transformation();

		layers = new TreeMap<Integer, Layer>();

		xMin = Double.POSITIVE_INFINITY;
		yMin = Double.POSITIVE_INFINITY;
		xMax = Double.NEGATIVE_INFINITY;
		yMax = Double.NEGATIVE_INFINITY;

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evt) {
				if (autoFitToDisplay)
					fitMapToDisplay();
			}
		});

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Defines whether the map content is automatically scaled to fit this display
	 * frame when its size is changed.
	 * 
	 * @param b if true the map content is automatically scaled
	 */
	public void setAutoFitToDisplay(boolean b) {
		autoFitToDisplay = b;
	}

	/**
	 * Adds a new layer to this map.
	 * 
	 * @param l: the layer
	 * @param i: the level to which this layer is added. Lower levels are drawn
	 *           first.
	 */
	public void addLayer(Layer l, int i) {
		this.addLayer(l, i, true);
	}

	/**
	 * Adds a new layer to this map.
	 * 
	 * @param l:               the layer
	 * @param i:               the level to which this layer is added. Lower levels
	 *                         are drawn first.
	 * @param fitMapToDisplay: whether map shall be fit to display
	 */
	public void addLayer(Layer l, int i, boolean fitMapToDisplay) {
		if (l.getExtent() == null)
			return;
		Envelope env = l.getExtent();
		xMin = Math.min(xMin, env.getxMin());
		xMax = Math.max(xMax, env.getxMax());
		yMin = Math.min(yMin, env.getyMin());
		yMax = Math.max(yMax, env.getyMax());
		if (fitMapToDisplay) {
			fitMapToDisplay();
		}

		layers.put(i, l);
	}

	public void removeLayer(int i) {
		if (layers.containsKey(i))
			layers.remove(i);
	}

	/**
	 * Redefines the transformation that transforms map coordinates to image
	 * coordinates.
	 * 
	 * @param t the new transformation
	 */
	public void setTransformation(Transformation t) {
		myTransformation = t;
	}

	/**
	 * Returns the transformation that transforms map coordinates to image
	 * coordinates.
	 * 
	 * @return the transformation
	 */
	public Transformation getTransformation() {
		return myTransformation;
	}

	/**
	 * Defines the margin between the map and the frame of this map display.
	 * 
	 * @param ratio the margin relative to the size of the frame.
	 */
	public void setFrameRatio(double ratio) {
		frameRatio = ratio;
	}

	/**
	 * Draws all Symbols
	 */
	@Override
	public void paint(Graphics gSimple) {
		Graphics2D g = (Graphics2D) gSimple;
		
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHints(rh);
		
		double x1 = myTransformation.getX(0);
		double y1 = myTransformation.getY(this.getHeight());
		double x2 = myTransformation.getX(this.getWidth());
		double y2 = myTransformation.getY(0);
		Envelope e = new Envelope(x1, x2, y1, y2);
		Color oldColor = g.getColor();
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		for (Layer l : layers.values()) {
			for (Object o : l.query(e)) {
				Symbol mo = (Symbol) o;
				mo.draw(g, myTransformation);
			}
		}
		g.setColor(oldColor);
	}

	/**
	 * Fits the map to this MapDisplay.
	 */
	public void fitMapToDisplay() {
		fitBoxToDisplay(xMin, yMin, xMax, yMax);
	}

	/**
	 * Fits a part of the map to the current extent of the display, specified by the
	 * part's coordinate bounds.
	 * 
	 * @param xMinBox the minimum x coordinate
	 * @param yMinBox the minimum y coordinate
	 * @param xMaxBox the maximum x coordinate
	 * @param yMaxBox the maximum y coordinate
	 */
	public void fitBoxToDisplay(double xMinBox, double yMinBox, double xMaxBox, double yMaxBox) {
		double dx = frameRatio * (xMaxBox - xMinBox);
		double dy = frameRatio * (yMaxBox - yMinBox);
		xMinBox = xMinBox - dx;
		xMaxBox = xMaxBox + dx;
		yMinBox = yMinBox - dy;
		yMaxBox = yMaxBox + dy;

		int mapWidth = this.getSize().width;
		int mapHeight = this.getSize().height;

		if (xMaxBox == xMinBox && yMaxBox == yMinBox) {
			xMaxBox += 10.0;
			yMaxBox += 10.0;
			xMinBox -= 10.0;
			yMinBox -= 10.0;
		} else if (xMaxBox == xMinBox) {
			xMaxBox += 0.01 * (yMaxBox - yMinBox);
			xMinBox -= 0.01 * (yMaxBox - yMinBox);
		} else if (yMaxBox == yMinBox) {
			yMaxBox += 0.01 * (xMaxBox - xMinBox);
			yMinBox -= 0.01 * (xMaxBox - xMinBox);
		}

		double m1 = mapWidth / (xMaxBox - xMinBox);
		double m2 = mapHeight / (yMaxBox - yMinBox);
		double m;
		int frameSizeX = 0;
		int frameSizeY = 0;

		if (m1 < m2) {
			m = m1;
			frameSizeY = (int) (0.5 * (mapHeight - m * (yMaxBox - yMinBox)));
		} else {
			m = m2;
			frameSizeX = (int) (0.5 * (mapWidth - m * (xMaxBox - xMinBox)));
		}
		int ColumnOrigin = frameSizeX - (int) (m * xMinBox);
		int RowOrigin = frameSizeY + (int) (m * yMaxBox);

		myTransformation = new Transformation(m, ColumnOrigin, RowOrigin);
		repaint();
	}

	/**
	 * Fits a part of the map to the current extent of the display, specified by the
	 * part's coordinate bounds.
	 * 
	 * @param e coordinate bounds
	 */
	public void fitBoxToDisplay(Envelope e) {
		fitBoxToDisplay(e.getxMin(), e.getyMin(), e.getxMax(), e.getyMax());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = e.getY();
		int column = e.getX();
		double xCenter = myTransformation.getX(column);
		double yCenter = myTransformation.getY(row);
		if (e.getButton() == MouseEvent.BUTTON1) {
			myTransformation.setM(2.0 * myTransformation.getM());
		} else {
			myTransformation.setM(0.5 * myTransformation.getM());
		}
		int rowTemp = myTransformation.getRow(yCenter);
		int columnTemp = myTransformation.getColumn(xCenter);
		myTransformation.setColumnOrigin(myTransformation.getColumnOrigin() + column - columnTemp);
		myTransformation.setRowOrigin(myTransformation.getRowOrigin() + row - rowTemp);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseColumn = e.getX();
		mouseRow = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		myTransformation.setColumnOrigin(myTransformation.getColumnOrigin() + e.getX() - mouseColumn);
		myTransformation.setRowOrigin(myTransformation.getRowOrigin() + e.getY() - mouseRow);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		myTransformation.setColumnOrigin(myTransformation.getColumnOrigin() + e.getX() - mouseColumn);
		myTransformation.setRowOrigin(myTransformation.getRowOrigin() + e.getY() - mouseRow);
		mouseColumn = e.getX();
		mouseRow = e.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int r = e.getY();
		int c = e.getX();
		myMapFrame.setXYLabelText(myTransformation.getX(c), myTransformation.getY(r));
	}

	public void resetMouseCoordinates() {
		mouseRow = -100;
		mouseColumn = -100;
	}

}
