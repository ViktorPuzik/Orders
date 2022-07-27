package org.example;

import org.junit.After;
import org.junit.Before;


import javax.persistence.EntityTransaction;
import java.util.concurrent.Callable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.concurrent.Callable;

import static org.example.App.emOrder;

public abstract class Bases {

    private static final String NAME = "Goods";

    private static EntityManagerFactory emFactory;
    protected static EntityManager em;

    @Before
    public void init() {
        emFactory = Persistence.createEntityManagerFactory(NAME);
        em = emFactory.createEntityManager();
    }

    @After
    public void finish() {
        if (em != null) em.close();
        if (emFactory != null) emFactory.close();
    }

    protected static <T> T performTransaction(Callable<T> action) {
        emFactory = Persistence.createEntityManagerFactory(NAME);
        em = emFactory.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            T result = action.call();
            transaction.commit();

            return result;
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();

            throw new RuntimeException(ex);
        }
    }

}
