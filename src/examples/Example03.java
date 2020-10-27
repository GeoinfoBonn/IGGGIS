package examples;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import org.postgresql.ds.PGSimpleDataSource;

import viewer.base.DatabaseLayer;
import viewer.base.MapPanel;
import viewer.symbols.BasicSymbolFactory;

public class Example03 {

	static DatabaseLayer myLayer;

	public static void main(String[] args) {

		// Angaben zur Datenquelle
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setServerNames(new String[] { "131.220.71.164" });
		dataSource.setPortNumbers(new int[] { 5432 });
		dataSource.setDatabaseName("vstroh");
		dataSource.setUser("vstroh");

		// Fenster zur Eingabe des Passworts
		JPasswordField jpf = new JPasswordField();
		JOptionPane.showConfirmDialog(null, jpf, "Password:", JOptionPane.OK_CANCEL_OPTION);
		dataSource.setPassword(new String(jpf.getPassword()));

		// Try to create database layer and add to MapFrame
		DatabaseLayer dl = null;
		try {
			dl = new DatabaseLayer(dataSource.getConnection().createStatement(),
					new BasicSymbolFactory(Color.RED, Color.BLUE), "gebaeude", "geom");

			JFrame frame = new JFrame("TestMain");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			MapPanel panel = new MapPanel();
			panel.getMap().addLayer(dl, 1);

			frame.add(panel);

			Dimension size = new Dimension(640, 480);
			frame.setSize(size);
			frame.setPreferredSize(size);
			frame.setVisible(true);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
