package viewer.base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 * panel that contains a map and a panel with the coordinates of the mouse
 * cursor
 * 
 * @author haunert
 */
public class MapPanel extends JPanel {

	private static final long serialVersionUID = 6063108022820064493L;

	/**
	 * the map displayed in this panel
	 */
	private Map myMap;

	/**
	 * the label displaying the x-coordinate
	 */
	private JLabel xLabel;

	/**
	 * the label displaying the y-coordinate
	 */
	private JLabel yLabel;

	/**
	 * constructor for crating an empty MapPanel
	 */
	public MapPanel() {

		setLayout(new BorderLayout());

		// labels showing the world coordinates of the mouse pointer
		xLabel = new JLabel();
		yLabel = new JLabel();

		myMap = new Map(this);

		// when fitting the content to the map extend, use some empty space at the
		// boundary
		myMap.setFrameRatio(0.1);

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2, 1));
		myPanel.add(xLabel);
		myPanel.add(yLabel);
		myPanel.setBorder(BorderFactory.createTitledBorder(new EtchedBorder(), "Map Coordinates", 0, 0));

		// some GUI attributes
		myMap.fitMapToDisplay();

		myPanel.setMinimumSize(new Dimension(200, 80));
		myPanel.setPreferredSize(new Dimension(200, 80));

		this.add(BorderLayout.NORTH, myPanel);
		this.add(BorderLayout.CENTER, myMap);
	}

	/**
	 * setter method used to update the coordinates displayed as text
	 * 
	 * @param x: the x-coordinate that is displayed
	 * @param y: the y-coordinate that is displayed
	 */
	public void setXYLabelText(double x, double y) {
		xLabel.setText(" x = " + x);
		xLabel.repaint();
		yLabel.setText(" y = " + y);
		yLabel.repaint();
	}

	/**
	 * the map displayed in this panel
	 * 
	 * @return the map
	 */
	public Map getMap() {
		return myMap;
	}
}
