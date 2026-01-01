
package concordia.repository;
import concordia.annotations.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import concordia.domain.History;
import concordia.domain.Company;
import concordia.domain.Item;

@Repository
public class HistoryRepository {
    private final EntityManager em;
    public HistoryRepository(EntityManager em) {
        this.em = em;
    }

    // Retrieve all history records from the database using JPA
    public List<History> getAllHistory() {
        TypedQuery<History> query = em.createQuery("SELECT h FROM History h", History.class);
        return query.getResultList();
    }

    public void updateHistory(History hist) {
        em.getTransaction().begin();
        em.merge(hist);
        em.getTransaction().commit();
    }

    public void deleteHistory(int historyId) {
        em.getTransaction().begin();
        History hist = em.find(History.class, historyId);
        if (hist != null) {
            em.remove(hist);
        }
        em.getTransaction().commit();
    }

    public void insertHistory(int itemId, int amount, String location, String provider, String deliveryDate, String notes) {
        em.getTransaction().begin();
            History hist = new History();
        hist.setItemId(itemId);
        hist.setAmount(amount);
        hist.setLocation(location);
        hist.setSupplier(provider);
        hist.setDeliveryDate(deliveryDate);
        hist.setNotes(notes);
        em.persist(hist);
        em.getTransaction().commit();
    }
}
