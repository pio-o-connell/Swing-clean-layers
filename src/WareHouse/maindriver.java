package WareHouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.User;
import WareHouse.domain.history;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/*---------------------------------------------------------------------------------------
/*
 * The 'maindriver' class is the entry point of the program.
 * After password clearance,user is then shown the main window viz. "Main Inventory System".
 * 
 ---------------------------------------------------------------------------------------*/

public class maindriver{
	public static ArrayList<Company> Company = new ArrayList<Company>();
	// Alias for legacy code compatibility
	public static ArrayList<Company> Company11 = Company;


	//Necessary for initial table window display
	final ArrayList<Company> tableCompanyPointer = Company;  //pointer to first element of Company data structure
	final ArrayList<Item>tableItemPointer = Company.get(Mainframe.companyIndex).getItems(); 

	
	public static void main(String[] args) throws Exception{
		// Show progress dialog while initializing database
		javax.swing.JDialog progressDialog = new javax.swing.JDialog((JFrame)null, "Please wait", true);
		javax.swing.JLabel label = new javax.swing.JLabel("Initializing database...", javax.swing.SwingConstants.CENTER);
		progressDialog.getContentPane().add(label);
		progressDialog.setSize(300, 100);
		progressDialog.setLocationRelativeTo(null);
		progressDialog.setDefaultCloseOperation(javax.swing.JDialog.DO_NOTHING_ON_CLOSE);

		// Run DB init in a background thread
		new Thread(() -> {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/warehouse?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC","root","ROOT");

				Databases db = new Databases(con);
				// Try to load database into memory, if not present, generate test data
				boolean dbLoaded = db.init(con, Company);
				if (!dbLoaded) {
					db.setup(con, Company);
				}

				String dateString1 = "2002-03-03";
				String dateString2 = "2012-03-10";

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

				Date dateFrom = format.parse(dateString1);
				Date dateTo = format.parse(dateString2);

				GenerateReport report= new GenerateReport(Company);
				report.GenerateItemsDatesReport(dateFrom, dateTo);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				progressDialog.setVisible(false);
				progressDialog.dispose();
			}
		}).start();

		// Show the dialog (blocks until disposed)
		progressDialog.setVisible(true);

		// Now launch the main window
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame frame;
				try {
					frame = new Mainframe("Warehouse Inventory System");
					frame.setSize(800,100);
					frame.setResizable(true);
					frame.setLocationRelativeTo(null);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}