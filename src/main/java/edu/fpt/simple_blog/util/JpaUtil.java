package edu.fpt.simple_blog.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("pu");

    public static EntityManagerFactory getEmf() {
        return EMF;
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}