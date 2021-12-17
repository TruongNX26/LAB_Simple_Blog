package edu.fpt.simple_blog.repository;

import edu.fpt.simple_blog.entity.Comment;
import edu.fpt.simple_blog.util.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.lang.invoke.MethodHandles;

public class CommentRepository {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static final CommentRepository INSTANCE = new CommentRepository();

    private CommentRepository() {}
    public static CommentRepository getInstance() {
        return INSTANCE;
    }

    public boolean create(Comment comment) {
        boolean isSuccess = false;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
            isSuccess = true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Creating comment failed", e);
        } finally {
            em.close();
        }
        return isSuccess;
    }
}
