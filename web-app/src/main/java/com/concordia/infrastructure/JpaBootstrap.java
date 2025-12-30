package com.concordia.infrastructure;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaBootstrap {
    public static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("concordiaPU");
    }
}
