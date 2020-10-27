package viewer.base;

import java.util.List;

import geometry.Envelope;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;

/**
 * A layer that can be added to a map.
 * 
 * @author haunert
 */
public abstract class Layer {

	/**
	 * the total extent of this layer
	 */
	protected Envelope extent;

	/**
	 * the factory used to transform features to drawable map objects
	 */
	protected SymbolFactory mySymbolFactory;

	public Layer() {
		this(SymbolFactory.DEFAULT_FACTORY);
	}

	public Layer(SymbolFactory symbolFactory) {
		mySymbolFactory = symbolFactory;
	}

	/**
	 * Returns the extent of this layer as an envelope
	 * 
	 * @return extent to return
	 */
	public Envelope getExtent() {
		return extent;
	}

	/**
	 * Sets the extent of this layer as an envelope. Use with care!
	 * @param extent extent to set
	 */
	public void setExtent(Envelope extent) {
		this.extent = extent;
	}

	/**
	 * Queries all symbols whose bounding boxes intersect the search envelope
	 * 
	 * @param searchEnv the search envelope
	 * @return found objects
	 */
	public abstract List<Symbol> query(Envelope searchEnv);

	/**
	 * creates a new layer in which all symbols are stored in a list, based on this
	 * layer
	 * 
	 * @return created layer
	 */
	public ListLayer toCachedLayer() {
		ListLayer myCachedLayer = new ListLayer();
		for (Symbol mo : this.query(extent)) {
			myCachedLayer.add(mo.getFeature());
		}
		return myCachedLayer;
	}
}
