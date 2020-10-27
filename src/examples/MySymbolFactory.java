package examples;
import java.awt.Color;

import io.structures.Feature;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;
public class MySymbolFactory implements SymbolFactory {

	@Override
	public Symbol createSymbol(Feature feature) {
		Color c = Color.LIGHT_GRAY;
		String s = (String) feature.getAttribute("street_nam");
		if (s.startsWith("Bachstr")) {
			c = Color.RED;
		}
		return new MySymbol(feature, c);
	}
}
