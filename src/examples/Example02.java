package examples;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;

import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;

public class Example02 {

	static ListLayer myLayer;

	public static void main(String[] args) {

		try {
			List<Feature> geometries = FeatureReader
					.readFeaturesFromShapefile(new File("data/gebaeude.shp"));

			createMap(geometries);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createMap(List<Feature> geometries) {
		HashSet<Feature> selection = new HashSet<Feature>();

		// myLayer = new ListLayer(Color.BLACK, SymbolFactory.BASIC_FACTORY);
		myLayer = new ListLayer(new SelectablePolygonSymbolFactory(selection));

		for (Feature f : geometries) {
			myLayer.add(f);
		}
		JFrame frame = new JFrame("TestMain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MapPanel panel = new MapPanel();
		panel.getMap().addLayer(myLayer, 100);

		panel.getMap().addMouseListener(new Selector(panel.getMap(), myLayer, selection));

		frame.add(panel);

		Dimension size = new Dimension(640, 480);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setVisible(true);
	}
}
