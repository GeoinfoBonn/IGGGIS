package examples;

import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.postgresql.ds.PGSimpleDataSource;

import geometry.Envelope;
import viewer.base.DatabaseLayer;
import viewer.base.MapPanel;
import viewer.symbols.SymbolFactory;

public class Listing03 {

	public static void main(String[] args) {

		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setServerNames(new String[] { "131.220.71.164" });
		dataSource.setPortNumbers(new int[] { 5432 });
		dataSource.setDatabaseName("strassen_nrw");
		dataSource.setUser("jhaunert");

		// set password by system dialog
		JPasswordField jpf = new JPasswordField();
		JOptionPane.showConfirmDialog(null, jpf, "Password:", JOptionPane.OK_CANCEL_OPTION);
		dataSource.setPassword(new String(jpf.getPassword()));

		DatabaseLayer dbl = null;
		try {
			dbl = new DatabaseLayer(dataSource.getConnection().createStatement(), SymbolFactory.DEFAULT_FACTORY,
					"lines", "geom");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Exiting program.");
			System.exit(1);
		}

		JFrame frame = new JFrame("Roads NRW");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension size = new Dimension(640, 480);
		frame.setSize(size);
		frame.setPreferredSize(size);

		MapPanel panel = new MapPanel();
		panel.getMap().setAutoFitToDisplay(false);
		frame.add(panel);
		frame.setVisible(true);

		double xM = 365600;
		double yM = 5620900;
		panel.getMap().fitBoxToDisplay(new Envelope(xM - 500, xM + 500, yM - 500, yM + 500));

		panel.getMap().addLayer(dbl, 1, false);

	}

}
