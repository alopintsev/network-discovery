package plus.fort.itinform.model;

import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Repository;
import plus.fort.itinform.domain.Connection;
import plus.fort.itinform.domain.Interface;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class InterfaceModel extends BaseModel {

    public InterfaceModel() {
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

    public Interface getInterface(String name, Long device_id) {
        Interface result = null;
        try {
            result = getEntityManager()
                    .createQuery("from Interface d where d.name=:name and d.device.id=:device_id", Interface.class)
                    .setParameter("name", name)
                    .setParameter("device_id", device_id)
                    .getSingleResult();

        } catch (NoResultException e) {
        }
        return result != null ? result : new Interface();
    }

    public boolean saveConnection(Interface interface1, Interface interface2){
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

    public Interface updateFromModel(Interface interf) {
        Interface result = getInterface(interf.getName(), interf.getDevice().getId());
        if (result.getId() == null) {
            result = interf;
        }
        return result;
    }

}
