package plus.fort.itinform.model;

import org.springframework.stereotype.Repository;
import plus.fort.itinform.domain.Device;
import plus.fort.itinform.domain.DeviceType;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;

@Repository
public class DeviceModel extends BaseModel {
    DeviceTypeModel deviceTypeModel;

    public DeviceModel() {
        super();
        deviceTypeModel = new DeviceTypeModel();
    }

    public Device getDevice(Long id) {
        return getEntityManager()
                .createQuery("from Device d where d.id=:deviceId", Device.class)
                .setParameter("deviceId", id)
                .getSingleResult();
    }

    public Device getDevice(String name) {
        Device result = null;
        try {
            result = getEntityManager()
                    .createQuery("from Device d where d.name=:name", Device.class)
                    .setParameter("name", name)
                    .getSingleResult();

        } catch (NoResultException e) {
        }
        return result != null ? result : new Device();
    }

    public List<Device> getDevice() {
        return getEntityManager()
                .createQuery("from Device", Device.class)
                .setMaxResults(2)
                .setFirstResult(0)
                .getResultList();
    }

    public Device updateFromModel(Device device) {
        Device result = getDevice(device.getName());
        DeviceType deviceType = deviceTypeModel.getDeviceType(device.getDeviceType().getName());

        if (result.getId() == null) {
            result = device;
        }
        if (deviceType.getId() != null) {
            result.setDeviceType(deviceType);
        }
        return result;
    }

    public void saveDevices(Set<Device> devices) {
        List<Device> devicesInDb = getEntityManager()
                .createQuery("from Device", Device.class)
                .getResultList();

        DeviceTypeModel deviceTypeModel = new DeviceTypeModel();
        for (Device device : devices) {
            if (devicesInDb.contains(device))
                continue;
            DeviceType deviceType = deviceTypeModel.getDeviceType(device.getDeviceType().getName());
            if (deviceType.getId() != null) {
                device.setDeviceType(deviceType);
            }
            persistData(device);
        }
    }
}
