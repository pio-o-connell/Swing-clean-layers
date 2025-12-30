package com.concordia.repository;

import Concordia.domain.User;
import jakarta.persistence.EntityManager;

public class UserRepository {
    private final EntityManager em;
    public UserRepository(EntityManager em) { this.em = em; }
    public void save(User user) { em.persist(user); }
    public User find(Long id) { return em.find(User.class, id); }
}
