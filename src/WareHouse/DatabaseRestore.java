package WareHouse;

//------------------------------------------------------------------//

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.User;
import WareHouse.domain.history;

// Restores the database from backup - loads into memory
// The 'Restore' button functionality in main window
// ------------------------------------------------------------------//



//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;
import WareHouse.annotations.Configuration;

@Configuration
public class DatabaseRestore {

    Connection con;
    ArrayList<history> history11 = new ArrayList<history>();
    ArrayList<Item> Item11 = new ArrayList<Item>();
    ArrayList<User> User11 = new ArrayList<User>();

    DatabaseRestore(Connection con) throws Exception {

        this.con = con;

    }

    public void setup(Connection con1, ArrayList<Company> companies) throws SQLException {
        this.con = con1;
        //select distinct items for the company
        try {

            String query = "SELECT DISTINCT ITEM_ID from ITEM";
            PreparedStatement statement;
            statement = (PreparedStatement) con.prepareStatement("SELECT * from ITEM");
//            statement = (PreparedStatement) con.prepareStatement(query);
            ResultSet itemsResult = statement.executeQuery();

            while (itemsResult.next()) {
                PreparedStatement statement1 = (PreparedStatement) con.prepareStatement("SELECT * FROM HISTORY WHERE ITEM_ID="
                        + itemsResult.getInt(1) + "");
              
                
                ResultSet historyResult = statement1.executeQuery();
                history11 = new ArrayList<history>();
                while (historyResult.next()) {

                    history11.add(new history(historyResult.getInt(1), historyResult.getInt(2), historyResult.getInt(3), historyResult.getString(4), historyResult.getString(5), historyResult.getString(6), historyResult.getString(7)));
                    System.out.println("\n history_id \t " + historyResult.getInt(1));
                    System.out.println("item_id " + historyResult.getInt(2));
                    System.out.println("amount" + historyResult.getInt(3));
                    System.out.println("location" + historyResult.getString(4));
                        // System.out.println("Supplier" + historyResult.getString(5));
                    System.out.println("Delivery Date" + historyResult.getString(6));
                }
                System.out.println("Item-ID" + itemsResult.getInt(1));

                // Updated Item constructor: (int itemId, int companyId, int quantity, String itemName, ArrayList<history> historyItem)
                Item11.add(new Item(
                    itemsResult.getInt(1), // itemId
                    itemsResult.getInt(2), // companyId
                    itemsResult.getInt(3), // quantity
                    itemsResult.getString(4), // itemName
                    itemsResult.getString(5), // notes
                    history11
                ));

                System.out.println("\n Item id" + itemsResult.getInt(1));
                System.out.println("\t Company Id" + itemsResult.getInt(2));
                System.out.println("\t Quantity" + itemsResult.getInt(3));
                System.out.println("\t ItemName" + itemsResult.getString(4));

            }
            // Create the users - only one
            PreparedStatement statement3 = (PreparedStatement) con.prepareStatement("select * from users ");
            ResultSet result3 = statement3.executeQuery();
            while (result3.next()) {
                System.out.println("userId\n" + result3.getInt(1) + " Username:" + result3.getString(2));
                System.out.println("user Password\n" + result3.getString(3) + "company id" + result3.getString(4));
                User11.add(new User(result3.getInt(1), result3.getInt(4), result3.getString(2), result3.getString(3)));
            }

            // Create the Company - only one
            PreparedStatement statement4 ;
            statement4 = (PreparedStatement) con.prepareStatement("select * from company");
            ResultSet result4 = statement4.executeQuery();
            while (result4.next()) {
                System.out.println("\n" + result4.getInt(1) + " name:" + result4.getString(2));
                companies.add(new Company(result4.getInt(1), result4.getString(2), Item11, User11));
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }

    }

}
