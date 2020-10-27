package viewer.base;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import geometry.Envelope;
import io.structures.Feature;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;

/**
 * This class represents a layer that can be displayed in a Map.
 * 
 * @author Jan-Henrik Haunert
 */
public class ListLayer extends Layer {

	/**
	 * The symbols of this Layer.
	 */
	private LinkedList<Symbol> mySymbols;

	/**
	 * Constructs a new empty Layer
	 * 
	 * 
	 */
	public ListLayer() {
		extent = null;
		mySymbols = new LinkedList<Symbol>();
	}

	/***
	 * Constructs a new empty Layer
	 * @param factory symbol factory
	 */
	public ListLayer(SymbolFactory factory) {
		super(factory);
		extent = null;
		mySymbols = new LinkedList<Symbol>();
	}

	/***
	 * Constructs a new Layer with specified features
	 * @param features specified features 
	 */
	public ListLayer(List<Feature> features) {
		for (Feature feature : features) {
			mySymbols.add(mySymbolFactory.createSymbol(feature));
		}
	}

	/**
	 * Adds a GeometryFeature to this Layer.
	 * 
	 * @param f the feature to be added
	 */
	public void add(Feature f) {
		Symbol m = mySymbolFactory.createSymbol(f);
		if (m == null)
			return;

		mySymbols.add(m);
		if (extent == null) {
			extent = m.getBoundingBox();
		} else {
			extent.expandToInclude(m.getBoundingBox());
		}
	}

	/**
	 * Removes the specified Symbol from this Layer.
	 * 
	 * @param s the symbol to be added
	 * @return if the symbol got removed successfully
	 */
	public boolean remove(Symbol s) {
		// TODO change to remove(GeometryFeature)?
		return mySymbols.remove(s);
	}

	/**
	 * Removes all symbols from the layer and resets the extend.
	 */
	public void clearLayer() {
		extent = null;
		mySymbols = new LinkedList<Symbol>();
	}

	public List<Symbol> getSymbols() {
		return Collections.unmodifiableList(mySymbols);
	}

	/**
	 * Returns the symbols of this layer that intersect a specified envelope.
	 * 
	 * @param searchEnv the query envelope
	 * @return the symbols of this layer that intersect the envelope
	 */
	@Override
	public List<Symbol> query(Envelope searchEnv) {
		List<Symbol> result = new LinkedList<Symbol>();
		for (Symbol m : mySymbols) {
			if (searchEnv.intersects(m.getBoundingBox())) {
				result.add(m);
			}
		}
		return result;
	}

	/***
	 * Converts the current ListLayer to a TreeLayer
	 * 
	 * @return converted TreeLayer
	 */
	public TreeLayer toTreeLayer() {
		TreeLayer tl = new TreeLayer();
		for (Symbol mo : mySymbols) {
			tl.add(mo.getFeature());
		}
		return tl;
	}

	/**
	 * Is the ListLayeer empty?
	 * 
	 * @return boolean value for the empty test
	 */
	public boolean isEmpty() {
		return mySymbols.isEmpty();
	}

	/***
	 * sets symbol factory
	 * @param factory symbol factory to be set
	 */
	public void setSymbolFactory(SymbolFactory factory) {
		this.mySymbolFactory = factory;
	}
}
