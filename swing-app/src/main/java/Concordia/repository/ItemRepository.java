
package concordia.repository;
import concordia.annotations.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import concordia.domain.Item;
import concordia.domain.Company;
import concordia.domain.History;

@Repository
public class ItemRepository {
    private final EntityManager em;
    public ItemRepository(EntityManager em) {
        this.em = em;
    }

    // Retrieve all items from the database using JPA
    public List<Item> getAllItems() {
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i", Item.class);
        return query.getResultList();
    }

    public void insertNewItem(int companyId, int amount, String itemName, String notes) {
        em.getTransaction().begin();
        Item item = new Item();
        // item.setCompanyId(companyId); // Refactored: set the Company object instead
        // You must set the Company object here, e.g.:
        // Company company = ... // fetch or create the Company object by companyId
        // item.setCompany(company);
        // For now, this is a placeholder and must be implemented properly.
        item.setQuantity(amount);
        item.setItemName(itemName);
        item.setNotes(notes);
        em.persist(item);
        em.getTransaction().commit();
    }

    public void deleteItem(int itemId) {
        em.getTransaction().begin();
        Item item = em.find(Item.class, itemId);
        if (item != null) {
            em.remove(item);
        }
        // Optionally, delete related History records if not handled by cascade
        em.getTransaction().commit();
    }
}
