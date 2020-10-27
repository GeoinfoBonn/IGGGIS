package lectures.gisII_vl3;


import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.operation.union.CascadedPolygonUnion;
import org.locationtech.jts.triangulate.ConformingDelaunayTriangulationBuilder;

import io.shp.FeatureReader;
import io.structures.Feature;
import viewer.base.ListLayer;
import viewer.base.MapPanel;
import viewer.symbols.BasicSymbolFactory;

public class Beispiel3 {
	public static void main(String[] args) {
		try {
//read features
List<Feature> features = FeatureReader.readFeaturesFromShapefile(new File("data/gebaeude.shp"));
			
//insert coordinates in list cl and polygons in list pl
LinkedList<Coordinate> cl = new LinkedList<Coordinate>();
LinkedList<Geometry> pl = new LinkedList<Geometry>();								
for (Feature f : features) {
	Geometry p = f.getGeometry();
	pl.add(p);
	for (Coordinate c : p.getCoordinates()) { 
		cl.add(c); 
	}
}

//jts.geom.GeometryFactory for generating new geometries
GeometryFactory gf = new GeometryFactory();		

//create new MultiPoint containing all points
Coordinate[] coords = new Coordinate[cl.size()]; 
coords = cl.toArray(coords);
MultiPoint mp = gf.createMultiPointFromCoords(coords);

//define vertices of triangulation with MultiPoint 
ConformingDelaunayTriangulationBuilder cdt = new ConformingDelaunayTriangulationBuilder();	
cdt.setSites(mp);

//union of all polygons to one big MultiPolygon and define the triangulation's constraints with it
CascadedPolygonUnion cpu = new CascadedPolygonUnion(pl);
cdt.setConstraints(cpu.union());
GeometryCollection tri = (GeometryCollection) cdt.getTriangles(gf);
			
//add polygons to layer l1
ListLayer l1 = new ListLayer(new BasicSymbolFactory(Color.BLACK, Color.GRAY));		
for (Feature f : features) {
	l1.add(f);	
}

//add triangles that pass test to layer l2
ListLayer l2 = new ListLayer(new BasicSymbolFactory(Color.RED, Color.RED));
double eps = 100;
for (int i = 0; i < tri.getNumGeometries(); i++){
	Polygon triangle = (Polygon) tri.getGeometryN(i);		
	Coordinate[] c = triangle.getCoordinates();
	Coordinate p1 = c[0];
	Coordinate p2 = c[1];
	Coordinate p3 = c[2];
	if (p1.distance(p2) <= eps && p2.distance(p3) <= eps && p3.distance(p1) <= eps) {
		l2.add(new Feature(triangle));
	}
}

//build graphical user interface
MapPanel panel = new MapPanel();
panel.getMap().addLayer(l1, 1);
panel.getMap().addLayer(l2, 2);

JFrame frame = new JFrame("Buildings");
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.add(panel);
Dimension size = new Dimension(1000, 1000);
frame.setSize(size);
frame.setPreferredSize(size);
frame.setVisible(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}