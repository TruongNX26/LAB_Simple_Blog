package edu.fpt.simple_blog.repository;

import edu.fpt.simple_blog.entity.User;
import edu.fpt.simple_blog.util.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.lang.invoke.MethodHandles;

public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static final UserRepository INSTANCE = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    public int findConfirmCode(String email) {
        int result = -1;

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            result = (int) em.createQuery("SELECT u.confirmCode FROM User u WHERE u.email = ?1")
                    .setParameter(1, email)
                    .getSingleResult();

        } catch (NoResultException e) {
            // do nothing
        } catch (Exception e) {
            logger.error("Updating user's status failed", e);
        } finally {
            em.close();
        }
        return result;
    }

    public boolean updateStatus(String email, User.Status status) {
        boolean isSuccess = false;

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            User user = em.find(User.class, email);
            user.setStatus(status);
            em.getTransaction().commit();

            isSuccess = true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Updating user's status failed", e);
        } finally {
            em.close();
        }
        return isSuccess;
    }

    public boolean checkExistedEmail(String email) {
        boolean isExist = true;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.createQuery("SELECT u.email FROM User u WHERE u.email = ?1")
                    .setParameter(1, email)
                    .getSingleResult();
        } catch (NoResultException e) {
            isExist = false;
        } catch (Exception e) {
            logger.error("Checking existed email failed", e);
        } finally {
            em.close();
        }

        return isExist;

    }

    public boolean create(User user) {
        boolean isSuccess = false;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            isSuccess = true;

        } catch (Exception e) {
            logger.error("Creating user failed", e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return isSuccess;
    }

    public User findByEmailPassword(String email, String password) {
        User user = null;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            Object[] result = (Object[]) em.createQuery(
                    "SELECT u.name, u.role, u.status FROM User u " +
                            "WHERE u.email = ?1 AND u.password = ?2 AND u.status <> ?3")
                    .setParameter(1, email)
                    .setParameter(2, password)
                    .setParameter(3, User.Status.INACTIVE)
                    .getSingleResult();

            String name = (String) result[0];
            User.Role role = (User.Role) result[1];
            User.Status status = (User.Status) result[2];

            user = User.builder()
                    .email(email)
                    .name(name)
                    .role(role)
                    .status(status)
                    .build();

        } catch (NoResultException e) {
            //  do nothing
        } catch (Exception e) {
            logger.error("Finding user by email and password failed", e);
        } finally {
            em.close();
        }

        return user;
    }
}
