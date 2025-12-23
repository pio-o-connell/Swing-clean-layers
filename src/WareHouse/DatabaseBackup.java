package WareHouse;
//--------------------------------------------------------------------------//
//
// Backups up database(from memory) to BackupWareHouse on the server, this can subsequently be
// retrieved and loaded. Part of main window functionality. Backup button..
//
//---------------------------------------------------------------------------------

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.User;
import WareHouse.domain.history;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;
import WareHouse.annotations.Configuration;

@Configuration
public class DatabaseBackup {

    Connection con;
    ArrayList<history> history11 = new ArrayList<history>();
    ArrayList<Item> Item11 = new ArrayList<Item>();
    ArrayList<User> User11 = new ArrayList<User>();

    DatabaseBackup(Connection con) throws Exception {

        this.con = con;

    }

    public void backup(Connection con1, ArrayList<Company> companies) throws SQLException {
        this.con = con1;
        try {

            PreparedStatement statement = (PreparedStatement) con.prepareStatement("DROP DATABASE if exists BackupWareHouse");
            statement.executeUpdate();

            statement = (PreparedStatement) con.prepareStatement("CREATE DATABASE BackupWareHouse");
            statement.executeUpdate();

            statement = (PreparedStatement) con.prepareStatement("USE BackupWareHouse");
            statement.executeUpdate();

            String query = "CREATE TABLE Company("
                    + "Company_ID INT NOT NULL,"
                    + "Company_title CHAR(25) NULL,"
                    + "PRIMARY KEY(Company_ID)"
                    + ")"
                    + "ENGINE = InnoDB  ";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

                statement = (PreparedStatement) con.prepareStatement(
                    "ALTER TABLE Item ADD UNIQUE KEY uq_item_id (Item_ID)"
                );
                statement.executeUpdate();

            query = " CREATE TABLE Users("
                    + "User_ID INT NOT NULL,"
                    + "User_Name CHAR(25) NULL,"
                    + "User_Password CHAR(25) NOT NULL,"
                    + "Company_ID INT NOT NULL,"
                    + "PRIMARY KEY(User_ID),"
                    + "CONSTRAINT fk_companies1 FOREIGN KEY(Company_ID)"
                    + "REFERENCES Company(Company_ID)"
                    + " )"
                    + " ENGINE = InnoDB";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            query = "CREATE TABLE Item("
                    + "Item_ID INT NOT NULL AUTO_INCREMENT,"
                    + "Company_ID INT NOT NULL,"
                    + "quantity INT NULL,"
                    + "itemName CHAR(25) NULL,"
                    + "PRIMARY KEY(Item_ID)," 
                    + "CONSTRAINT fk_companies FOREIGN KEY(Company_ID)"
                    + "REFERENCES Company(Company_ID)"
                    + ")"
                    + "ENGINE = InnoDB";
            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            query = "CREATE TABLE History("
                    + "history_id INT NOT NULL AUTO_INCREMENT,"
                    + "item_ID INT NOT NULL,"
                    + "amount INT NULL,"
                    + "location CHAR(25) NULL,"
                    + "Supplier CHAR(25) NULL,"
                    + "Delivery_Date DATE NULL,"
                    + "PRIMARY KEY(history_id,item_ID),"
                    + "CONSTRAINT fk_items FOREIGN KEY(item_ID) "
                    + "REFERENCES Item(Item_ID)"
                    + ")"
                    + "ENGINE = InnoDB ";

            statement = (PreparedStatement) con.prepareStatement(query);
            statement.executeUpdate();

            // for Company		
            System.out.println(companies.get(0).getCompanyId());
            System.out.println("Sizeof Company Array" + companies.size());

            //for item
            System.out.println(companies.get(0).getItems().get(0).getItemName());
            System.out.println("Sizeof items array" + companies.get(0).getItems().size());

            //for history
            System.out.println(companies.get(0).getItems().get(0).getHistory().get(0).getLocation());
            System.out.println("Sizeof history array" + companies.get(0).getItems().get(0).getHistory().size());

            System.out.println("Sizeof history array" + companies.get(0).getItems().get(1).getHistory().size());
            System.out.println("HistoryId" + companies.get(0).getItems().get(1).getHistory().get(0).getHistoryId());

            //for users
            System.out.println("Users" + companies.get(0).getUsers().get(0).getPassword());

            // backup the data to the backup
            for (int i = 0; i < companies.size(); i++) { // will be only one company here
                statement = (PreparedStatement) con.prepareStatement("INSERT INTO COMPANY(Company_ID,Company_title)  VALUES  (?,?)");
                statement.setInt(1, companies.get(i).getCompanyId());
                statement.setString(2, companies.get(i).getCompanyName());
                statement.executeUpdate();
                for (int j = 0; j < companies.get(i).getItems().size(); j++) {

                    statement = (PreparedStatement) con.prepareStatement("INSERT INTO ITEM(ITEM_ID,COMPANY_ID,QUANTITY,ITEMNAME)  VALUES  (?,?,?,?)");
                    statement.setInt(1, companies.get(i).getItems().get(j).getItemId());
                    statement.setInt(2, companies.get(i).getCompanyId());
                    statement.setInt(3, companies.get(i).getItems().get(j).getQuantity());
                    statement.setString(4, companies.get(i).getItems().get(j).getItemName());
                    statement.executeUpdate();

                    for (int k = 0; k < companies.get(i).getItems().get(j).getHistory().size(); k++) {
                        statement = (PreparedStatement) con.prepareStatement("INSERT  INTO  history(HISTORY_ID,ITEM_id,AMOUNT,LOCATION,Supplier,DELIVERY_DATE)  VALUES  (?,?,?,?,?,?)");
                        statement.setInt(1, companies.get(i).getItems().get(j).getHistory().get(k).getHistoryId());
                        statement.setInt(2, companies.get(i).getItems().get(j).getItemId());
                        statement.setInt(3, companies.get(i).getItems().get(j).getHistory().get(k).getAmount());
                        statement.setString(4, companies.get(i).getItems().get(j).getHistory().get(k).getLocation());
                        statement.setString(5, companies.get(i).getItems().get(j).getHistory().get(k).getSupplier());
                        statement.setString(6, companies.get(i).getItems().get(j).getHistory().get(k).getDeliveryDate());

                        statement.executeUpdate();

                    }

                    //backup the users
                    /*	for(int l=0;l<companies.get(i).getUsers().size();l++){
				    statement = (PreparedStatement) con.prepareStatement("INSERT INTO USERs(USER_ID,USER_NAME,USER_PASSWORD,COMPANY_ID)  VALUES  (?,?,?,?)"); 
			    	statement.setInt(1, companies.get(i).getUsers().get(l).getUserId());
			    	statement.setString(2, companies.get(i).getUsers().get(l).getUsername());
			    	statement.setString(3, companies.get(i).getUsers().get(l).getPassword());
			    	statement.setInt(4, companies.get(i).getUsers().get(l).getCompanyId());
			    	statement.executeUpdate();
			    	
			    	}*/
                }

            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

    }

}
