package edu.fpt.simple_blog.repository;

import edu.fpt.simple_blog.entity.Article;
import edu.fpt.simple_blog.util.JpaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class ArticleRepository {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static final ArticleRepository INSTANCE = new ArticleRepository();

    private ArticleRepository() {}

    public static ArticleRepository getInstance() {
        return INSTANCE;
    }

    public List<Article> adminSearch(String title, String content, Article.Status status, int offset, int size) {
        if(title == null) title = "";
        if(content == null) content = "";

        EntityManager em = null;
        List result = null;
        try {
            em = JpaUtil.getEntityManager();
            Query query;
            if(status == null) {
                query = em.createQuery("SELECT NEW Article(a.id, a.postingDate, a.title, a.description, a.author, a.status) FROM Article a WHERE a.title LIKE ?1 AND a.content LIKE ?2 ORDER BY a.postingDate DESC", Article.class);
            } else {
                query = em.createQuery("SELECT NEW Article(a.id, a.postingDate, a.title, a.description, a.author, a.status) FROM Article a WHERE a.title LIKE ?1 AND a.content LIKE ?2 AND a.status = ?3 ORDER BY a.postingDate DESC", Article.class)
                        .setParameter(3, status);
            }

            result = query
                    .setParameter(1, "%" + title + "%")
                    .setParameter(2, "%" + content + "%")
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();

        } catch (Exception e) {
            logger.error("Admin searching failed", e);
        } finally {
            em.close();
        }

        return result;
    }

    public Article searchById(long id, Article.Status status) {
        EntityManager em = null;
        Article result = null;
        try {
            em = JpaUtil.getEntityManager();
            Query query;
            if(status != null) {
                query = em.createQuery("SELECT a FROM Article a WHERE a.id = ?1 AND a.status = ?2")
                        .setParameter(2, Article.Status.ACTIVE);
            } else {
                query = em.createQuery("SELECT a FROM Article a WHERE a.id = ?1");
            }
            result = (Article) query.setParameter(1, id).getSingleResult();
        } catch (NoResultException e) {
            //  do nothing
        } catch (Exception e) {
            logger.error("Searching article by id failed", e);
        } finally {
            em.close();
        }
        return result;
    }

    public List<Article> searchByContent(String value, int offset, int size) {
        if(value == null) value = "";
        EntityManager em = null;
        List<Article> result = null;
        try {
            em = JpaUtil.getEntityManager();

            result = em.createQuery("SELECT NEW Article(a.id, a.postingDate, a.title, a.description, a.author) FROM Article a WHERE a.content LIKE ?1 AND a.status = ?2 ORDER BY a.postingDate DESC", Article.class)
                    .setParameter(1, "%" + value + "%")
                    .setParameter(2, Article.Status.ACTIVE)
                    .setFirstResult(offset)
                    .setMaxResults(size)
                    .getResultList();

        } catch (NoResultException e) {
            //  do nothing
        } catch (Exception e) {
            logger.error("Searching article by content failed", e);
        } finally {
            em.close();
        }

        return result;
    }

    public long countSearchByContent(String value) {
        if(value == null) value = "";
        long result = 0;
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            result = (long) em.createQuery("SELECT COUNT(a.id) FROM Article a WHERE a.content LIKE ?1 AND a.status = ?2")
                    .setParameter(1, "%" + value + "%")
                    .setParameter(2, Article.Status.ACTIVE)
                    .getSingleResult();

        } catch (NoResultException e) {
            //  do nothing
        } catch (Exception e) {
            logger.error("Getting count by content search failed", e);
        } finally {
            em.close();
        }

        return result;
    }

    public boolean create(Article article) {
        boolean isSuccess = true;

        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(article);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Creating article failed", e);
            isSuccess = false;
        } finally {
            em.close();
        }

        return isSuccess;
    }

    public void updateStatus(long articleId, Article.Status status) {
        EntityManager em = null;
        try {
            em = JpaUtil.getEntityManager();
            em.getTransaction().begin();
            Article article = em.find(Article.class, articleId);
            article.setStatus(status);
            em.getTransaction().commit();
        } catch (RollbackException e) {
            em.getTransaction().rollback();
        } catch (Exception e) {
            logger.error("Updating article's status failed", e);
        } finally {
            em.close();
        }
    }

    public long getCountAdmin(String title, String content, Article.Status status) {
        if(title == null) title = "";
        if(content == null) content = "";

        EntityManager em = null;
        long result = 0;
        try {
            em = JpaUtil.getEntityManager();
            Query query;
            if(status == null) {
                query = em.createQuery("SELECT COUNT(a.id) FROM Article a WHERE a.title LIKE ?1 AND a.content LIKE ?2");
            } else {
                query = em.createQuery("SELECT COUNT(a.id) FROM Article a WHERE a.title LIKE ?1 AND a.content LIKE ?2 AND a.status = ?3")
                        .setParameter(3, status);
            }

            result = (long) query
                    .setParameter(1, "%" + title + "%")
                    .setParameter(2, "%" + content + "%")
                    .getSingleResult();

        } catch (Exception e) {
            logger.error("Admin searching failed", e);
        } finally {
            em.close();
        }

        return result;
    }
}
