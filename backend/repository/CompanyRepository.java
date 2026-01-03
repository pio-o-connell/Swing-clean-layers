package backend.repository;

import concordia.domain.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CompanyRepository {
    private final EntityManager entityManager;

    public CompanyRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Company> getAllCompanies() {
        TypedQuery<Company> query = entityManager.createQuery(
            "SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.items LEFT JOIN FETCH c.users",
            Company.class
        );
        return query.getResultList();
    }

    public void insertCompany(int companyId, String companyName) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Company company = new Company(companyId, "", companyName, new java.util.HashSet<>(), new java.util.HashSet<>());
            entityManager.persist(company);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void updateCompany(Company company) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(company);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void deleteCompany(int companyId) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Company existing = entityManager.find(Company.class, companyId);
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
