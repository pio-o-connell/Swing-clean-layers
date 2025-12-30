package com.concordia.infrastructure;

import com.concordia.domain.User;
import com.concordia.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class JpaTransactionExample {
    public void saveUserExample(User user) {
        EntityManagerFactory emf = JpaBootstrap.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        UserRepository repo = new UserRepository(em);
        try {
            tx.begin();
            repo.save(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}
