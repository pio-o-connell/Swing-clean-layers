
package WareHouse.repository;
import WareHouse.annotations.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import WareHouse.domain.history;
import WareHouse.domain.Company;
import WareHouse.domain.Item;

@Repository
public class HistoryRepository {
        // Retrieve all history records from the database
        public ArrayList<history> getAllHistory() throws SQLException {
            ArrayList<history> historyList = new ArrayList<>();
            String query = "SELECT history_id, item_id, amount, location, provider, delivery_date, notes FROM history";
            PreparedStatement stmt = con.prepareStatement(query);
            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history hist = new history(
                    rs.getInt("history_id"),
                    rs.getInt("item_id"),
                    rs.getInt("amount"),
                    rs.getString("location"),
                    rs.getString("provider"),
                    rs.getString("delivery_date"),
                    rs.getString("notes")
                );
                historyList.add(hist);
            }
            rs.close();
            stmt.close();
            return historyList;
        }
    private final Connection con;
    public HistoryRepository(Connection con) {
        this.con = con;
    }

    public void updateHistory(history hist) throws SQLException {
        String query = "UPDATE history SET location = ?, provider = ?, delivery_date = ?, amount = ?, notes = ? WHERE history_id = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, hist.getLocation());
        stmt.setString(2, hist.getSupplier());
        stmt.setString(3, hist.getDeliveryDate());
        stmt.setInt(4, hist.getAmount());
        stmt.setString(5, hist.getNotes());
        stmt.setInt(6, hist.getHistoryId());
        stmt.executeUpdate();
    }

    public void deleteHistory(int historyId) throws SQLException {
        String query = "DELETE FROM history WHERE history_id = ?";
        PreparedStatement preparedStmt = con.prepareStatement(query);
        preparedStmt.setInt(1, historyId);
        preparedStmt.execute();
    }

    public void insertHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) throws SQLException {
        PreparedStatement statement = con.prepareStatement("INSERT INTO history(item_id, amount, location, provider, delivery_date, notes) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setInt(1, itemId);
        statement.setInt(2, amount);
        statement.setString(3, location);
        statement.setString(4, provider);
        statement.setString(5, deliveryDate);
        statement.setString(6, notes);
        statement.executeUpdate();
    }
}
