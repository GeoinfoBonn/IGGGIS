package examples;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;

public class Example01 {

	static ListLayer myLayer;

	public static void main(String[] args) {

		try {
			List<Feature> geometries = FeatureReader
					.readFeaturesFromShapefile(new File("data/gebaeude.shp"));

			fillLayer(geometries);
			createAndShowUI();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void fillLayer(List<Feature> geometries) {
		// myLayer = new ListLayer(Color.BLACK, SymbolFactory.BASIC_FACTORY);
		myLayer = new ListLayer(new MySymbolFactory());

		for (Feature f : geometries) {
			myLayer.add(f);
		}
	}

	public static void createAndShowUI() {
		JFrame frame = new JFrame("TestMain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MapPanel panel = new MapPanel();
		panel.getMap().addLayer(myLayer, 100);

		frame.add(panel);

		Dimension size = new Dimension(640, 480);
		frame.setSize(size);
		frame.setPreferredSize(size);
		frame.setVisible(true);
	}
}
