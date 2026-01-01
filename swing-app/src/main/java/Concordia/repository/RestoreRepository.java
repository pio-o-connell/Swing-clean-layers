package concordia.repository;
import java.util.List;
import concordia.annotations.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;

@Repository
public class RestoreRepository {
    private Connection con;
    public RestoreRepository(Connection con) {
        this.con = con;
    }

    public void setup(ArrayList<Company> Company11) throws SQLException {
        try {
            String query = "SELECT DISTINCT ITEM_ID from ITEM";
            PreparedStatement statement;
            statement = (PreparedStatement) con.prepareStatement("SELECT * from ITEM");
            ResultSet itemsResult = statement.executeQuery();

            java.util.Set<Item> Item11 = new java.util.HashSet<>();
            java.util.Set<User> User11 = new java.util.HashSet<>();
            List<History> history11;

            while (itemsResult.next()) {
                PreparedStatement statement1 = (PreparedStatement) con.prepareStatement("SELECT * FROM HISTORY WHERE ITEM_ID="
                        + itemsResult.getInt(1) + "");
                ResultSet historyResult = statement1.executeQuery();
                history11 = new java.util.ArrayList<>();
                while (historyResult.next()) {
                    // Fix: pass null for Item in History constructor, as only historyId, item, amount, location, provider, deliveryDate, notes are expected
                    history11.add(new History(
                        historyResult.getInt(1), // historyId
                        null, // Item (not available here)
                        historyResult.getInt(2), // amount
                        historyResult.getString(4), // location
                        historyResult.getString(5), // provider
                        historyResult.getString(6), // deliveryDate
                        historyResult.getString(7)  // notes
                    ));
                    System.out.println("\n history_id \t " + historyResult.getInt(1));
                    System.out.println("item_id " + historyResult.getInt(2));
                    System.out.println("amount" + historyResult.getInt(3));
                    System.out.println("location" + historyResult.getString(4));
                    System.out.println("Delivery Date" + historyResult.getString(6));
                }
                System.out.println("Item-ID" + itemsResult.getInt(1));
                // Find the Company object by companyId
                Company company = null;
                int companyId = itemsResult.getInt(2);
                for (Company c : Company11) {
                    if (c.getCompanyId() == companyId) {
                        company = c;
                        break;
                    }
                }
                // Ensure company is not null, otherwise skip this item
                if (company != null) {
                    Item11.add(new Item(
                        itemsResult.getInt(1), // itemId
                        company, // Company object
                        itemsResult.getInt(3), // quantity
                        itemsResult.getString(4), // itemName
                        itemsResult.getString(5), // location
                        itemsResult.getString(6), // notes
                        itemsResult.getTimestamp(7), // date
                        history11
                    ));
                } else {
                    System.err.println("No matching company found for itemId " + itemsResult.getInt(1) + ", companyId " + itemsResult.getInt(2));
                }
                System.out.println("\n Item id" + itemsResult.getInt(1));
                System.out.println("\t Company Id" + itemsResult.getInt(2));
                System.out.println("\t Quantity" + itemsResult.getInt(3));
                System.out.println("\t ItemName" + itemsResult.getString(4));
            }
            PreparedStatement statement3 = (PreparedStatement) con.prepareStatement("select * from users ");
            ResultSet result3 = statement3.executeQuery();
            while (result3.next()) {
                System.out.println("userId\n" + result3.getInt(1) + " Username:" + result3.getString(2));
                System.out.println("user Password\n" + result3.getString(3) + "company id" + result3.getString(4));
                User11.add(new User(result3.getInt(1), result3.getInt(4), result3.getString(2), result3.getString(3)));
            }
            PreparedStatement statement4 ;
            statement4 = (PreparedStatement) con.prepareStatement("select * from company");
            ResultSet result4 = statement4.executeQuery();
            while (result4.next()) {
                System.out.println("\n" + result4.getInt(1) + " name:" + result4.getString(2));
                // Company(int companyId, String companyTitle, String companyName, Set<Item> items, Set<User> users)
                Company11.add(new Company(result4.getInt(1), result4.getString(2), result4.getString(3), Item11, User11));
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
}
// ...existing code...
