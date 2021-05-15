package plus.fort.itinform.model;

import org.springframework.stereotype.Repository;
import plus.fort.itinform.domain.DeviceType;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class DeviceTypeModel extends BaseModel {

    public DeviceTypeModel() {
        super();
    }

    public DeviceType getDeviceType(String name) {
        DeviceType result = null;
        if (name == null || name.length() == 0) {
            name = "-";
        }
        try {
            result = getEntityManager()
                    .createQuery("from DeviceType d where d.name=:name", DeviceType.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        return result != null ? result : new DeviceType();
    }

    public DeviceType getDeviceType(Long id) {
        return getEntityManager()
                .createQuery("from device_type d where d.id=:deviceTypeId", DeviceType.class)
                .setParameter("deviceTypeId", id)
                .getSingleResult();
    }

    public List<DeviceType> getDeviceType() {
        return getEntityManager()
                .createQuery("from device_type", DeviceType.class)
                .setMaxResults(2)
                .setFirstResult(0)
                .getResultList();
    }

}
