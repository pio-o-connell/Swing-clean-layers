package WareHouse.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import WareHouse.domain.Item;
import WareHouse.domain.Company;
import WareHouse.domain.history;

public class ItemRepository {
    private final Connection con;
    public ItemRepository(Connection con) {
        this.con = con;
    }

    public void insertNewItem(int companyId, int amount, String itemName, String notes) throws SQLException {
        PreparedStatement statement = con.prepareStatement("INSERT INTO item(company_id,quantity,itemName,notes) VALUES (?,?,?,?)");
        statement.setInt(1, companyId);
        statement.setInt(2, amount);
        statement.setString(3, itemName);
        statement.setString(4, notes);
        statement.executeUpdate();
    }

    public void deleteItem(int itemId) throws SQLException {
        String query = "DELETE FROM item WHERE Item_id = ?";
        PreparedStatement preparedStmt1 = con.prepareStatement(query);
        preparedStmt1.setInt(1, itemId);
        preparedStmt1.execute();
        String query1 = "DELETE FROM history WHERE item_id = ?";
        PreparedStatement preparedStmt11 = con.prepareStatement(query1);
        preparedStmt11.setInt(1, itemId);
        preparedStmt11.execute();
    }
}
