package viewer.base;

import java.util.LinkedList;
import java.util.List;

import org.locationtech.jts.index.strtree.STRtree;

import geometry.Envelope;
import io.structures.Feature;
import viewer.symbols.Symbol;

/**
 * a layer of a map that organizes the Symbols in a tree to enable fast querying
 * with rectangles
 * 
 * @author haunert_admin
 *
 */
public class TreeLayer extends Layer {

	/**
	 * the Symbols in a tree
	 */
	private STRtree myObjects;

	/**
	 * constructor for generating an empty layer
	 * 
	 * 
	 */
	public TreeLayer() {
		extent = null;
		myObjects = new STRtree();
	}

	/**
	 * method for retrieving all Symbols intersecting a query envelope
	 */
	@Override
	public List<Symbol> query(Envelope bb) {
		LinkedList<Symbol> result = new LinkedList<Symbol>();
		org.locationtech.jts.geom.Envelope jtsEnv = new org.locationtech.jts.geom.Envelope(bb.getxMin(), bb.getxMax(),
				bb.getyMin(), bb.getyMax());
		for (Object o : myObjects.query(jtsEnv)) {
			Symbol mo = (Symbol) o;
			result.add(mo);
		}
		return result;
	}

	/**
	 * method for adding a GeometryFeature to the layer
	 * 
	 * @param f: the feature to be added
	 */
	public void add(Feature f) {
		Symbol mo = mySymbolFactory.createSymbol(f);
		if (mo == null)
			return;

		Envelope bb = mo.getBoundingBox();
		org.locationtech.jts.geom.Envelope jtsEnv = new org.locationtech.jts.geom.Envelope(bb.getxMin(), bb.getxMax(),
				bb.getyMin(), bb.getyMax());
		myObjects.insert(jtsEnv, mo);
		if (extent == null) {
			extent = mo.getBoundingBox();
		} else {
			extent.expandToInclude(mo.getBoundingBox());
		}
	}

}
