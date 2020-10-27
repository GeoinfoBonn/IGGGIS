package examples;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import geometry.Envelope;
import io.structures.Feature;
import viewer.base.Layer;
import viewer.base.Map;
import viewer.symbols.Symbol;

public class Selector implements MouseListener {

	private Map map;
	private Layer controlledLayer;
	private HashSet<Feature> selection;

	public Selector(Map m, Layer l, HashSet<Feature> s) {
		this.map = m;
		this.controlledLayer = l;
		this.selection = s;
		m.removeMouseListener(m);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.isShiftDown()) {
			selection.clear();
			int row = e.getY();
			int col = e.getX();
			double x = map.getTransformation().getX(col);
			double y = map.getTransformation().getY(row);
			Envelope env = new Envelope(x, x, y, y);
			Point p = (new GeometryFactory()).createPoint(new Coordinate(x, y));
			List<Symbol> list = controlledLayer.query(env);
			for (Symbol s : list) {
				if (s.getFeature().getGeometry().contains(p))
					selection.add(s.getFeature());
			}
			map.repaint();
		} else {
			map.mouseClicked(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		map.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		map.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		map.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		map.mouseExited(e);
	}

}
