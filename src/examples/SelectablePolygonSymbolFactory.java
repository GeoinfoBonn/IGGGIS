package examples;
import java.util.HashSet;

import io.structures.Feature;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;

public class SelectablePolygonSymbolFactory implements SymbolFactory {

	private HashSet<Feature> selection;
	
	public SelectablePolygonSymbolFactory(HashSet<Feature> selection) {
		this.selection = selection;
	}
	
	@Override
	public Symbol createSymbol(Feature feature) {
		return new SelectablePolygonSymbol(feature, selection);
	}

}
