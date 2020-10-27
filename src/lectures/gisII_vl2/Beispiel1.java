package lectures.gisII_vl2;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;

import org.locationtech.jts.algorithm.MinimumBoundingCircle;

import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;
import viewer.symbols.BasicSymbolFactory;

public class Beispiel1 {

	public static void main(String[] args) {

		try {
			
			List<Feature> features = FeatureReader.readFeaturesFromShapefile(new File("data/gebaeude.shp"));

			ListLayer l1 = new ListLayer(new BasicSymbolFactory(Color.BLACK, Color.GRAY));
			ListLayer l2 = new ListLayer(new BasicSymbolFactory(Color.RED, null));
			ListLayer l3 = new ListLayer(new BasicSymbolFactory(Color.BLUE, null));
			
			for (Feature f : features) {
				l1.add(f);
				l2.add(new Feature(f.getGeometry().convexHull()));
				MinimumBoundingCircle c = new MinimumBoundingCircle(f.getGeometry());
				l3.add(new Feature(c.getCircle()));
			}
			
			MapPanel panel = new MapPanel();
			panel.getMap().addLayer(l1, 1);
			panel.getMap().addLayer(l2, 2);
			panel.getMap().addLayer(l3, 3);

			
			JFrame frame = new JFrame("Buildings");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(panel);
			Dimension size = new Dimension(640, 480);
			frame.setSize(size);
			frame.setPreferredSize(size);
			frame.setVisible(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


