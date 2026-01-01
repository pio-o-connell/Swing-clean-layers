

package concordia.repository;
import concordia.annotations.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import concordia.domain.Company;
import concordia.domain.Item;
import concordia.domain.User;
import concordia.domain.History;

@Repository
public class CompanyRepository {
    private final EntityManager em;
    public CompanyRepository(EntityManager em) {
        this.em = em;
    }

    public void insertCompany(int companyId, String companyName) {
        em.getTransaction().begin();
        Company company = new Company(companyId, "", companyName, new java.util.HashSet<>(), new java.util.HashSet<>());
        em.persist(company);
        em.getTransaction().commit();
    }

    public void updateCompany(Company company) {
        em.getTransaction().begin();
        em.merge(company);
        em.getTransaction().commit();
    }

    public void deleteCompany(int companyId) {
        em.getTransaction().begin();
        Company company = em.find(Company.class, companyId);
        if (company != null) {
            em.remove(company);
        }
        em.getTransaction().commit();
    }

    public List<Company> loadCompaniesWithItemsAndUsers() {
        // This will load all companies, items, and users using JPA relationships
        TypedQuery<Company> query = em.createQuery("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.items LEFT JOIN FETCH c.users", Company.class);
        return query.getResultList();
    }
}
