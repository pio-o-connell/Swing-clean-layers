package WareHouse;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;

public class maindriver {
	// All data and initialization should be handled by the controller layer

		// Legacy main removed. Use ApplicationBootstrap as the entry point.
	public static void main(String[] args) {
        WareHouse.infrastructure.ApplicationBootstrap.main(args);
    }
}