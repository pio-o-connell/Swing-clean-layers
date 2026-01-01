
package concordia.service;
import concordia.annotations.Service;

import concordia.repository.CompanyRepository;
import concordia.repository.ItemRepository;
import concordia.repository.HistoryRepository;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.History;
import java.util.List;

@Service
public class InventoryService {
    private final CompanyRepository companyRepo;
    private final ItemRepository itemRepo;
    private final HistoryRepository historyRepo;
    public InventoryService(CompanyRepository companyRepo, ItemRepository itemRepo, HistoryRepository historyRepo) {
        this.companyRepo = companyRepo;
        this.itemRepo = itemRepo;
        this.historyRepo = historyRepo;
    }

    // Retrieve all items
    public List<Item> getAllItems() {
        return itemRepo.getAllItems();
    }

    // Retrieve all history records
    public List<History> getAllHistory() {
        return historyRepo.getAllHistory();
    }

    // Example thin delegation methods
    public List<Company> getAllCompanies() {
        return companyRepo.loadCompaniesWithItemsAndUsers();
    }

    public void addItem(int companyId, int amount, String itemName, String notes) {
        itemRepo.insertNewItem(companyId, amount, itemName, notes);
    }

    public void deleteItem(int itemId) {
        itemRepo.deleteItem(itemId);
    }

    public void addHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        historyRepo.insertHistory(itemId, amount, location, provider, deliveryDate, notes);
    }

    public void updateHistory(History hist) {
        historyRepo.updateHistory(hist);
    }

    public void deleteHistory(int historyId) {
        historyRepo.deleteHistory(historyId);
    }
}
