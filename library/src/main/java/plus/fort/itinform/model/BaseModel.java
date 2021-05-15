package plus.fort.itinform.model;

//import org.jeasy.random.EasyRandom;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class BaseModel {
    static final Logger logger = LogManager.getLogger(BaseModel.class);
    private static EntityManagerFactory sessionFactory;
    private static EntityManager entityManager;
    BaseModel() {
    }
    protected EntityManager getEntityManager() {
        if(sessionFactory == null) {
            sessionFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
        }
        if(entityManager == null) {
            entityManager = sessionFactory.createEntityManager();
        }
        return entityManager;
    }

    protected <T> boolean mergeData(T data) {
        boolean result = true;
        boolean autoCommit=false;
        try {
            if(!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
                autoCommit = true;
            }
            getEntityManager().persist(data);
            if(autoCommit) {
                getEntityManager().getTransaction().commit();
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "exception while mergeData " + data + ", " + e.getMessage());
            if (getEntityManager().getTransaction().isActive())
                getEntityManager().getTransaction().rollback();
            result = false;
        }
        return result;
    }

    protected <T> boolean persistData(T data) {
        boolean result = true;
        boolean autoCommit=false;
        try {
            if(!getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().begin();
                autoCommit = true;
            }
            getEntityManager().persist(data);
            if(autoCommit) {
                getEntityManager().getTransaction().commit();
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "exception while persistData " + data + ", " + e.getMessage());
            if (getEntityManager().getTransaction().isActive())
                getEntityManager().getTransaction().rollback();
            result = false;
        }
        return result;
    }
}
