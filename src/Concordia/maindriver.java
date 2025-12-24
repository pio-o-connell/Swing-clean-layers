package Concordia;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;

public class maindriver {
	// All data and initialization should be handled by the controller layer
	public static void main(String[] args) {
		Concordia.infrastructure.ApplicationBootstrap.main(args);
    }
}