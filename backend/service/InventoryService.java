package backend.service;

import backend.repository.CompanyRepository;
import backend.repository.HistoryRepository;
import backend.repository.ItemRepository;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.history;
import java.util.List;

public class InventoryService {
    private final CompanyRepository companyRepository;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;

    public InventoryService(CompanyRepository companyRepository, ItemRepository itemRepository, HistoryRepository historyRepository) {
        this.companyRepository = companyRepository;
        this.itemRepository = itemRepository;
        this.historyRepository = historyRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.getAllItems();
    }

    public List<history> getAllHistory() {
        return historyRepository.getAllHistory();
    }

    public List<Company> getAllCompanies() {
        return companyRepository.getAllCompanies();
    }

    public void addItem(int companyId, int amount, String itemName, String notes) {
        itemRepository.insertNewItem(companyId, amount, itemName, notes);
    }

    public void deleteItem(int itemId) {
        itemRepository.deleteItem(itemId);
    }

    public void updateItem(int itemId, int companyId, int amount, String itemName, String notes) {
        itemRepository.updateItem(itemId, companyId, amount, itemName, notes);
    }

    public void addHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        historyRepository.insertHistory(itemId, amount, location, provider, deliveryDate, notes);
    }

    public void updateHistory(int historyId, int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        historyRepository.updateHistory(historyId, itemId, amount, location, provider, deliveryDate, notes);
    }

    public void deleteHistory(int historyId) {
        historyRepository.deleteHistory(historyId);
    }
}
