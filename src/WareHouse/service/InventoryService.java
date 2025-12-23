
package WareHouse.service;
import WareHouse.annotations.Service;

import WareHouse.repository.CompanyRepository;
import WareHouse.repository.ItemRepository;
import WareHouse.repository.HistoryRepository;
import WareHouse.domain.Company;
import WareHouse.domain.Item;
import WareHouse.domain.history;
import java.sql.SQLException;
import java.util.List;

@Service
public class InventoryService {
            // Retrieve all items
            public List<Item> getAllItems() throws SQLException {
                return itemRepo.getAllItems();
            }
        // Retrieve all history records
        public List<history> getAllHistory() throws SQLException {
            return historyRepo.getAllHistory();
        }
    private final CompanyRepository companyRepo;
    private final ItemRepository itemRepo;
    private final HistoryRepository historyRepo;
    public InventoryService(CompanyRepository companyRepo, ItemRepository itemRepo, HistoryRepository historyRepo) {
        this.companyRepo = companyRepo;
        this.itemRepo = itemRepo;
        this.historyRepo = historyRepo;
    }
    // Example thin delegation methods
    public List<Company> getAllCompanies() throws SQLException {
        return companyRepo.loadCompaniesWithItemsAndUsers();
    }
    public void addItem(int companyId, int amount, String itemName, String notes) throws SQLException {
        itemRepo.insertNewItem(companyId, amount, itemName, notes);
    }
    public void deleteItem(int itemId) throws SQLException {
        itemRepo.deleteItem(itemId);
    }
    public void addHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) throws SQLException {
        historyRepo.insertHistory(itemId, amount, location, provider, deliveryDate, notes);
    }
    public void updateHistory(history hist) throws SQLException {
        historyRepo.updateHistory(hist);
    }
    public void deleteHistory(int historyId) throws SQLException {
        historyRepo.deleteHistory(historyId);
    }
}
