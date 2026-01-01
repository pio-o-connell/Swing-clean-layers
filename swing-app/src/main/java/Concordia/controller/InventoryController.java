
package concordia.controller;
import concordia.annotations.Controller;
import concordia.service.InventoryService;
import concordia.domain.Company;
import concordia.domain.History;
import java.util.List;

@Controller
public class InventoryController {
    private final InventoryService service;
    public InventoryController(InventoryService service) {
        this.service = service;
    }

    // Retrieve notes for an item by row index
    public String getItemNotesForRow(int row) {
        List<concordia.domain.Item> allItems = service.getAllItems();
        if (row >= 0 && row < allItems.size()) {
            return allItems.get(row).getNotes();
        }
        return null;
    }

    // Retrieve notes for a history record by row index
    public String getHistoryNotesForRow(int row) {
        List<History> allHistory = service.getAllHistory();
        if (row >= 0 && row < allHistory.size()) {
            return allHistory.get(row).getNotes();
        }
        return null;
    }

    public List<Company> getAllCompanies() {
        return service.getAllCompanies();
    }
    public void addItem(int companyId, int amount, String itemName, String notes) {
        service.addItem(companyId, amount, itemName, notes);
    }
    public void deleteItem(int itemId) {
        service.deleteItem(itemId);
    }
        public java.util.List<concordia.domain.History> getAllHistory() {
            return service.getAllHistory();
        }
    public void addHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        service.addHistory(itemId, amount, location, provider, deliveryDate, notes);
    }
    public void updateHistory(History hist) {
        service.updateHistory(hist);
    }
    public void deleteHistory(int historyId) {
        service.deleteHistory(historyId);
    }
}
