
package Concordia.controller;
import Concordia.annotations.Controller;

import Concordia.service.InventoryService;
import Concordia.domain.Company;
import Concordia.domain.history;
import java.sql.SQLException;
import java.util.List;

@Controller
public class InventoryController {
            // Retrieve notes for an item by row index
            public String getItemNotesForRow(int row) {
                try {
                    List<Concordia.domain.Item> allItems = service.getAllItems();
                    if (row >= 0 && row < allItems.size()) {
                        return allItems.get(row).getNotes();
                    }
                } catch (Exception e) {
                    return "Error retrieving notes: " + e.getMessage();
                }
                return null;
            }
        // Retrieve notes for a history record by row index
        public String getHistoryNotesForRow(int row) {
            try {
                List<history> allHistory = service.getAllHistory();
                if (row >= 0 && row < allHistory.size()) {
                    return allHistory.get(row).getNotes();
                }
            } catch (Exception e) {
                return "Error retrieving notes: " + e.getMessage();
            }
            return null;
        }
    private final InventoryService service;
    public InventoryController(InventoryService service) {
        this.service = service;
    }
    public List<Company> getAllCompanies() throws SQLException {
        return service.getAllCompanies();
    }
    public void addItem(int companyId, int amount, String itemName, String notes) throws SQLException {
        service.addItem(companyId, amount, itemName, notes);
    }
    public void deleteItem(int itemId) throws SQLException {
        service.deleteItem(itemId);
    }
    public void addHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) throws SQLException {
        service.addHistory(itemId, amount, location, provider, deliveryDate, notes);
    }
    public void updateHistory(history hist) throws SQLException {
        service.updateHistory(hist);
    }
    public void deleteHistory(int historyId) throws SQLException {
        service.deleteHistory(historyId);
    }
}
