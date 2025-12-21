package WareHouse.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.User;
import WareHouse.domain.history;

public class CompanyRepository {
    private final Connection con;
    public CompanyRepository(Connection con) {
        this.con = con;
    }

    public ArrayList<Company> loadCompaniesWithItemsAndUsers() throws SQLException {
        ArrayList<Company> companies = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement userStmt = con.prepareStatement("SELECT * FROM users");
        ResultSet userResult = userStmt.executeQuery();
        while (userResult.next()) {
            users.add(new User(userResult.getInt(1), userResult.getInt(4), userResult.getString(2), userResult.getString(3)));
        }
        PreparedStatement companyStmt = con.prepareStatement("SELECT * FROM company");
        ResultSet companyResult = companyStmt.executeQuery();
        while (companyResult.next()) {
            int companyId = companyResult.getInt(1);
            String companyName = companyResult.getString("company_name");
            ArrayList<Item> companyItems = new ArrayList<>();
            PreparedStatement itemStmt = con.prepareStatement("SELECT * FROM item WHERE Company_ID = ?");
            itemStmt.setInt(1, companyId);
            ResultSet itemsResult = itemStmt.executeQuery();
            while (itemsResult.next()) {
                int itemId = itemsResult.getInt(1);
                int quantity = itemsResult.getInt(3);
                String itemName = itemsResult.getString("item_name");
                String itemNotes = null;
                try { itemNotes = itemsResult.getString("notes"); } catch (Exception e) { itemNotes = null; }
                ArrayList<history> historyList = new ArrayList<>();
                PreparedStatement histStmt = con.prepareStatement("SELECT * FROM HISTORY WHERE item_id = ?");
                histStmt.setInt(1, itemId);
                ResultSet histResult = histStmt.executeQuery();
                while (histResult.next()) {
                    int historyId = histResult.getInt(1);
                    int amount = histResult.getInt(3);
                    String location = histResult.getString(4);
                    String provider = histResult.getString(5);
                    String deliveryDate = histResult.getString(6);
                    String historyNotes = null;
                    try { historyNotes = histResult.getString("notes"); } catch (Exception e) { historyNotes = null; }
                    historyList.add(new history(historyId, itemId, amount, location, provider, deliveryDate, historyNotes));
                }
                companyItems.add(new Item(itemId, companyId, quantity, itemName, itemNotes, historyList));
            }
            companies.add(new Company(companyId, companyName, companyItems, users));
        }
        return companies;
    }
}
