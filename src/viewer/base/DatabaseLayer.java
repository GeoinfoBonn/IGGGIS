package viewer.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.postgis.PGbox2d;

import geometry.Envelope;
import io.structures.Feature;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;

/**
 * This class represents a layer enabling a database connection
 * 
 * @author Jan-Henrik Haunert
 */
public class DatabaseLayer extends Layer {

	private Statement st;
	private String tablename;
	private String geocolname;
	private int srid;

	/**
	 * 
	 * @param st statement for the execution of an SQL query
	 * @param factory symbol factory object
	 * @param tablename table name
	 * @param geocolumnname column of the geometry
	 */
	public DatabaseLayer(Statement st, SymbolFactory factory, String tablename, String geocolumnname) {
		super(factory);
		this.st = st;
		this.tablename = tablename;
		this.geocolname = geocolumnname;
		this.srid = querySRID();
		computeBBox();
	}

	/***
	 * retrieves the SRID
	 * 
	 * @return srid
	 */
	private int querySRID() {
		int id = 0;
		try {
			String query = "SELECT ST_SRID(" + geocolname + ") AS srid FROM " + tablename + " LIMIT 1;";
			
			ResultSet rs = st.executeQuery(query);
			rs.next();
			id = rs.getInt("srid");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/***
	 * computes the extent of the queried symbols
	 */
	private void computeBBox() {
		try {
			String query = "SELECT ST_EXTENT(" + geocolname + ") AS box FROM " + tablename + ";";
			ResultSet rs = st.executeQuery(query);
			rs.next();
			PGbox2d box = (PGbox2d) rs.getObject("box");
			this.extent = new Envelope(box.getLLB().x, box.getURT().x, box.getLLB().y, box.getURT().y);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Symbol> query(Envelope searchEnv) {
		List<Symbol> mySymbols = new LinkedList<Symbol>();

		double x1 = searchEnv.getxMin();
		double x2 = searchEnv.getxMax();
		double y1 = searchEnv.getyMin();
		double y2 = searchEnv.getyMax();

		System.out.println(x1 + " " + x2 + " " + y1 + " " + y2);
		
		String query = "SELECT *, ST_asText(" + geocolname + ") as " + geocolname + "_WKT FROM " + tablename
				+ " AS ls, ST_SetSRID(ST_MakeBox2D(ST_Point(" + x1 + "," + y1 + "), ST_Point(" + x2 + "," + y2 + ")), "
				+ srid + ") AS querygeo WHERE ST_INTERSECTS(" + geocolname + ", querygeo);";

		try {
			System.out.println(query);
			long starttime = System.currentTimeMillis();
			ResultSet rs = st.executeQuery(query);
			long endtime = System.currentTimeMillis();
			System.out.println("Query executed in " + (endtime - starttime) + " ms");
			while (rs.next()) {

				// fetch all attributes of current row into HashMap
				HashMap<String, Object> attributes = new HashMap<String, Object>();
				for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
					String colname = rs.getMetaData().getColumnName(i);
					if (!colname.equals(geocolname)) {
						Object o = rs.getObject(i);
						attributes.put(colname, o);
					}
				}

				String s_geo = rs.getString(geocolname + "_WKT");
				WKTReader wktReader = new WKTReader(new GeometryFactory(new PrecisionModel(), srid));
				Geometry jts_geo = wktReader.read(s_geo);

				Feature myFeature = new Feature(jts_geo, attributes);
				mySymbols.add(mySymbolFactory.createSymbol(myFeature));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		return mySymbols;
	}

}
