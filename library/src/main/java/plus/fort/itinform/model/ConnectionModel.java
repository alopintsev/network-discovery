package plus.fort.itinform.model;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Repository;
import plus.fort.itinform.domain.Connection;
import plus.fort.itinform.domain.Interface;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class ConnectionModel extends BaseModel {

    public ConnectionModel() {
        super();
    }


    public int deleteConnection(Long id) {
        return getEntityManager().createQuery("delete from Connection c where c.id=:ConnectionId")
                .setParameter("connectionId", id)
                .executeUpdate();
    }

    public Connection getConnection(Long id) {
        return getEntityManager()
                .createQuery("from Connection c where c.id=:connectionId", Connection.class)
                .setParameter("connectionId", id)
                .getSingleResult();
    }

    public List<Connection> getConnection() {
        return getEntityManager()
                .createQuery("from Connection", Connection.class)
                .setMaxResults(2)
                .setFirstResult(0)
                .getResultList();
    }

    public Connection getConnection(String name) {
        Connection result = null;
        try {
            result = getEntityManager()
                    .createQuery("from Connection d where d.name=:name", Connection.class)
                    .setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
        }
        return result != null ? result : new Connection();
    }

    public boolean saveConnection(Interface interface1, Interface interface2) {
        boolean result = true;
        try {
            getEntityManager().getTransaction().begin();
            mergeData(interface1);
            mergeData(interface2);
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            logger.log(Level.ERROR, "got exception while saving connection:" + e.getMessage());
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
        return result;
    }

    public Connection updateFromModel(Connection connection) {
        Connection result = getConnection(connection.getName());
        if (result.getId() == null) {
            result = connection;
        }
        return result;
    }

}
