package backend.repository;

import concordia.domain.Item;
import concordia.domain.history;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class HistoryRepository {
    private final EntityManager entityManager;

    public HistoryRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<history> getAllHistory() {
        TypedQuery<history> query = entityManager.createQuery("SELECT h FROM history h", history.class);
        return query.getResultList();
    }

    public void insertHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            history record = new history();
            Item item = entityManager.find(Item.class, itemId);
            record.setItem(item);
            record.setAmount(amount);
            record.setLocation(location);
            record.setProvider(provider);
            record.setDeliveryDate(deliveryDate);
            record.setNotes(notes);
            entityManager.persist(record);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void updateHistory(int historyId, int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            history existing = entityManager.find(history.class, historyId);
            if (existing == null) {
                throw new IllegalArgumentException("History not found: " + historyId);
            }
            existing.setAmount(amount);
            existing.setLocation(location);
            existing.setProvider(provider);
            existing.setDeliveryDate(deliveryDate);
            existing.setNotes(notes);
            if (itemId != 0) {
                Item item = entityManager.find(Item.class, itemId);
                existing.setItem(item);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void deleteHistory(int historyId) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            history existing = entityManager.find(history.class, historyId);
            if (existing != null) {
                entityManager.remove(existing);
            }
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }
}
