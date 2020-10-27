package lectures.gisII_vl3;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;
import viewer.symbols.BasicSymbolFactory;
import viewer.symbols.Symbol;

public class Beispiel1 {
	public static void main(String[] args) {
		try {
			List<Feature> features = FeatureReader.readFeaturesFromShapefile(new File("data/gebaeude.shp"));
			ListLayer l1 = new ListLayer(new BasicSymbolFactory(Color.BLACK, Color.GRAY));		
			for (Feature f : features) {
				l1.add(f);	
			}
			MapPanel panel = new MapPanel();
			panel.getMap().addLayer(l1, 1);
			

			
			LinkedList<Coordinate> cl = new LinkedList<Coordinate>();
			for (Symbol s : l1.getSymbols()) {
				MultiPolygon p = (MultiPolygon) s.getFeature().getGeometry();
				for (Coordinate c : p.getCoordinates()) { 
					cl.add(c); 
				}
			}
			// now all vertices have been collected in list cl
			
			DelaunayTriangulationBuilder dtb = new DelaunayTriangulationBuilder();
			
			GeometryFactory gf = new GeometryFactory();
			Coordinate[] coords = new Coordinate[cl.size()]; 
			coords = cl.toArray(coords);
			MultiPoint mp = gf.createMultiPointFromCoords(coords);
			dtb.setSites(mp);
			GeometryCollection tri = (GeometryCollection) dtb.getTriangles(gf);
								
			ListLayer l2 = new ListLayer(new BasicSymbolFactory(Color.BLACK, null));
			for (int i = 0; i < tri.getNumGeometries(); i++){
				Polygon triangle = (Polygon) tri.getGeometryN(i);
				l2.add(new Feature(triangle));
			}
			panel.getMap().addLayer(l2, 2);
			
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

