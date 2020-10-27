package lectures.gisII_vl1;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;
import viewer.symbols.BasicSymbolFactory;

public class Beispiel2 {

	public static void main(String[] args) {

		try {
			
			List<Feature> features = FeatureReader.readFeaturesFromShapefile(new File("data/pois-bonn.shp"));

			ListLayer myLayer = new ListLayer(new BasicSymbolFactory(Color.RED, null));
			for (Feature f : features) {
				myLayer.add(f);
			}
			
			
			MapPanel panel = new MapPanel();
			panel.getMap().addLayer(myLayer, 1);

			
			JFrame frame = new JFrame("POIs in Bonn");
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


