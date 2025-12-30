package Concordia.domain;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.*;
import org.hibernate.cfg.AvailableSettings;
import java.util.HashMap;
import java.util.Map;

public class CompanyTest {
    @Test
    public void testCompanyNameAndId() {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        Company company = new Company(1, "TestCo", items, users);
        assertEquals(Integer.valueOf(1), company.getCompanyId());
        assertEquals("TestCo", company.getCompanyName());
        assertEquals("TestCo", company.getName());
        assertTrue(company.getItems().isEmpty());
        assertTrue(company.getUsers().isEmpty());
    }

    @Test
    public void testAddItem() {
        Company company = new Company(2, "AddItemCo", new ArrayList<>(), new ArrayList<>());
        assertEquals(0, company.getItems().size());
        company.addItem();
        assertEquals(1, company.getItems().size());
        assertNotNull(company.getItems().get(0));
    }

    @Test
    public void testSetAndGetUsers() {
        Company company = new Company(3, "UserCo", new ArrayList<>(), new ArrayList<>());
        ArrayList<User> users = new ArrayList<>();
        User user = new User(1, 3, "testuser", "password");
        users.add(user);
        company.setUsers(users);
        assertEquals(1, company.getUsers().size());
        assertEquals("testuser", company.getUsers().get(0).getUsername());
    }

    @Test
    public void testPersistCompanyWithJPA() {
        Map<String, Object> props = new HashMap<>();
        props.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        props.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/concordia");
        props.put(AvailableSettings.USER, "postgres");
        props.put(AvailableSettings.PASS, "password");
        props.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        props.put(AvailableSettings.SHOW_SQL, "true");
        props.put(AvailableSettings.FORMAT_SQL, "true");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu", props);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Company company = new Company(0, "JPACo", new ArrayList<>(), new ArrayList<>());
        em.persist(company);
        em.getTransaction().commit();
        em.close();
        emf.close();
        // assertNotNull(company.getCompanyId());
    }

    @Test
    public void testPersistAndRetrieveDummyCompany() {
        Map<String, Object> props = new HashMap<>();
        props.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        props.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/concordia");
        props.put(AvailableSettings.USER, "postgres");
        props.put(AvailableSettings.PASS, "password");
        props.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        props.put(AvailableSettings.SHOW_SQL, "true");
        props.put(AvailableSettings.FORMAT_SQL, "true");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu", props);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Company dummy = new Company(123, "DummyCo", new ArrayList<>(), new ArrayList<>());
        em.persist(dummy);
        em.getTransaction().commit();
        // Retrieve
        Company found = em.find(Company.class, 123);
        assertNotNull(found);
        assertEquals("DummyCo", found.getCompanyName());
        em.close();
        emf.close();
    }
}


