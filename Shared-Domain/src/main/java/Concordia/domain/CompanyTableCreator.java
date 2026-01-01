package concordia.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.ArrayList;

public class CompanyTableCreator {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        // Persist a dummy company to force table creation
        Company company = new Company(0, "ForceCreate", "ForceCreate", new java.util.HashSet<>(), new java.util.HashSet<>());
        em.persist(company);
        em.getTransaction().commit();
        em.close();
        emf.close();
        System.out.println("Company table should now exist in the database.");
    }
}
