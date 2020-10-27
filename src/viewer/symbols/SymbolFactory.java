package viewer.symbols;

import java.awt.Color;

import io.structures.Feature;

/***
 * 
 * @author haunert
 *
 */
public interface SymbolFactory {

	public static final SymbolFactory DEFAULT_FACTORY = new BasicSymbolFactory(Color.BLACK, null);

	public Symbol createSymbol(Feature feature);

}
