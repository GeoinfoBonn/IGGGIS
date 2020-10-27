package io.shp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import io.structures.Feature;

/**
 * 
 * Reader for (geometry) features
 *
 */
public class FeatureReader {

	/**
	 * Reads an ESRI Shapefile and returns a list of raw JTS-geometries without the
	 * features' attributes.
	 * 
	 * @param shapefile location of the shapefile (.shp) to read
	 * @return LinkedList of the geometries
	 * @throws IOException IO exception to throw
	 */
	public static List<Geometry> readRawGeometryFromShapefile(File shapefile) throws IOException {
		long time = System.currentTimeMillis();

		Map<String, Object> map = new HashMap<>();
		map.put("url", shapefile.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

		LinkedList<Geometry> result = new LinkedList<>();

		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();

				Object geometry = feature.getDefaultGeometry();
				if (geometry instanceof Geometry) {
					result.add((Geometry) geometry);
				}
			}
		}

		time = System.currentTimeMillis() - time;
		System.out.println("Finished reading shapefile, found " + result.size() + " geometries.");

		return result;
	}

	/**
	 * Reads an ESRI Shapefile and returns a list of GeometryFeatures containing the
	 * geometry as well as all the attributes.
	 * 
	 * @param shapefile location of the shapefile (.shp) to read
	 * @return LinkedList of the features
	 * @throws IOException IO exception to throw
	 */
	public static List<Feature> readFeaturesFromShapefile(File shapefile) throws IOException {
		long time = System.currentTimeMillis();

		Map<String, Object> map = new HashMap<>();
		map.put("url", shapefile.toURI().toURL());

		System.out.println(shapefile.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();

		LinkedList<Feature> result = new LinkedList<>();
		Feature currentFeature = null;
		String name;
		Object value;

		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();

				Object geometry = feature.getDefaultGeometry();
				try {
					currentFeature = new Feature((Geometry) geometry);
				} catch (ClassCastException e) {
					System.err.println("feature geometry has wrong type!");
					continue;
				}

				for (Property property : feature.getProperties()) {
					name = property.getName().getLocalPart();
					value = property.getValue();
					if (name != "the_geom") // geometry is already added separately, should not appear in attribute list
						currentFeature.setAttribute(name, value);
				}

				result.add(currentFeature);

			}
		}

		time = System.currentTimeMillis() - time;
		System.out.println("Finished reading shapefile, found " + result.size() + " geometries.");

		return result;
	}
}
