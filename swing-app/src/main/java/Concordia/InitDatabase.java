package concordia;

import concordia.domain.Company;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.util.ArrayList;

public class InitDatabase {
    public static void main(String[] args) {
        // Set up JPA EntityManagerFactory and EntityManager
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("test-pu");
            em = emf.createEntityManager();
            Databases db = new Databases(em);
            // Example: create a company using ORM
            db.insertCompany(1, "Example Company");
            // You can add more ORM-based initialization here as needed
            System.out.println("Database tables created and populated successfully using JPA/ORM.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }
}
