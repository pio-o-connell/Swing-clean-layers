package backend.repository;

import concordia.domain.Company;
import concordia.domain.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ItemRepository {
    private final EntityManager entityManager;

    public ItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Item> getAllItems() {
        TypedQuery<Item> query = entityManager.createQuery("SELECT i FROM Item i", Item.class);
        return query.getResultList();
    }

    public void insertNewItem(int companyId, int amount, String itemName, String notes) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Company company = entityManager.find(Company.class, companyId);
            Item item = new Item();
            item.setCompany(company);
            item.setCompanyId(companyId);
            item.setQuantity(amount);
            item.setItemName(itemName);
            item.setNotes(notes);
            entityManager.persist(item);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void deleteItem(int itemId) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Item item = entityManager.find(Item.class, itemId);
            if (item != null) {
                entityManager.remove(item);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void updateItem(int itemId, int companyId, int quantity, String itemName, String notes) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Item existing = entityManager.find(Item.class, itemId);
            if (existing == null) {
                throw new IllegalArgumentException("Item not found: " + itemId);
            }
            Company company = entityManager.find(Company.class, companyId);
            existing.setCompany(company);
            existing.setCompanyId(companyId);
            existing.setQuantity(quantity);
            existing.setItemName(itemName);
            existing.setNotes(notes);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}
