package concordia;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;

public class Databases {
    @PersistenceContext
    private EntityManager entityManager;

    public Databases(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Example: Insert a new company using JPA
    public void insertCompany(int companyId, String companyName) {
        Company company = new Company(companyId, companyName, new ArrayList<>(), new ArrayList<>());
        entityManager.getTransaction().begin();
        entityManager.persist(company);
        entityManager.getTransaction().commit();
    }

    // Example: Update a history record using JPA
    public void updateHistory(int historyId, String location, String supplier, String delivery, int quantity, String notes) {
        entityManager.getTransaction().begin();
        history hist = entityManager.find(history.class, historyId);
        if (hist != null) {
            hist.setLocation(location);
            hist.setSupplier(supplier);
            hist.setDeliveryDate(delivery);
            hist.setAmount(quantity);
            hist.setNotes(notes);
            entityManager.merge(hist);
        }
        entityManager.getTransaction().commit();
    }

    // Example: Get all companies
    public List<Company> getAllCompanies() {
        return entityManager.createQuery("SELECT c FROM Company c", Company.class).getResultList();
    }

    // Example: Delete a company
    public void deleteCompany(int companyId) {
        entityManager.getTransaction().begin();
        Company company = entityManager.find(Company.class, companyId);
        if (company != null) {
            entityManager.remove(company);
        }
        entityManager.getTransaction().commit();
    }

    // Add more methods as needed, following this pattern for JPA usage
}
