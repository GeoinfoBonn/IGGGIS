package examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;

import io.structures.Feature;
import viewer.base.Transformation;
import viewer.symbols.PolygonSymbol;

public class SelectablePolygonSymbol extends PolygonSymbol {

	private Color selectedColor = Color.RED;
	private HashSet<Feature> selection;

	public SelectablePolygonSymbol(Feature feature, HashSet<Feature> selectedFeatures) {
		super(feature);
		this.selection = selectedFeatures;
	}

	public void setSelectedColor(Color c) {
		this.selectedColor = c;
	}

	@Override
	public void draw(Graphics2D g, Transformation t) {
		Color old = strokeColor;
		if (selection.contains(this.myFeature)) {
			strokeColor = selectedColor;
		}
		super.draw(g, t);
		strokeColor = old;
	}
}
