package lectures.gisII_vl1;

import java.awt.Color;
import java.util.TreeMap;

import io.structures.Feature;
import viewer.symbols.PointSymbol;
import viewer.symbols.Symbol;
import viewer.symbols.SymbolFactory;

public class CategorizedSymbolFactory implements SymbolFactory {
	private Color defaultColor;
	private String categoryColName;
	private TreeMap<String, Color> categories;
		
	public CategorizedSymbolFactory(Color c, String s) {
		this.defaultColor = c;
		this.categoryColName = s;
		this.categories = new TreeMap<String, Color>();
	}
	
	public void addCategory(String name, Color c) {
		this.categories.put(name, c);
	}
	
	@Override
	public Symbol createSymbol(Feature f) {
		Color c = categories.get(f.getAttribute(categoryColName));
		if (c == null) c = defaultColor;
		return new PointSymbol(f, c);
	}
}
