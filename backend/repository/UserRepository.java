package backend.repository;

import concordia.domain.Company;
import concordia.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UserRepository {
    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public void insertUser(int userId, int companyId, String username, String password) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Company company = entityManager.find(Company.class, companyId);
            User user = new User();
            user.setUserId(userId);
            user.setCompany(company);
            user.setCompanyId(companyId);
            user.setUsername(username);
            user.setPassword(password);
            entityManager.persist(user);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void updateUser(User user) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.merge(user);
            tx.commit();
        } catch (RuntimeException ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw ex;
        }
    }

    public void deleteUser(int userId) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            User existing = entityManager.find(User.class, userId);
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
