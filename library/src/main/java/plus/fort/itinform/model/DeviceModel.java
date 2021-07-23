package plus.fort.itinform.model;

import org.springframework.stereotype.Repository;
import plus.fort.itinform.domain.*;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
                .setMaxResults(200)
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

    public List<CdpRecord> getInterfaces(long deviceId) {
        List<CdpRecord> result = new ArrayList<>();
        List<Interface> localInterfaces = new ArrayList<>();
        List<Interface> remoteInterfaces = new ArrayList<>();

        try {
            Device device = new Device();
            device.setId(deviceId);

//            record.localInterface =
            localInterfaces = getEntityManager()
                    .createQuery("from Interface d where d.device=:deviceId", Interface.class)
                    .setParameter("deviceId", device)
                    .getResultList();
            List<Connection> connections = new ArrayList<>();

            connections = localInterfaces.stream().map(Interface::getConnection).collect(Collectors.toList());

            remoteInterfaces = getEntityManager()
                    .createQuery("from Interface d where d.connection in (:connection) and d.device <> :device", Interface.class)
                    .setParameter("connection", connections)
                    .setParameter("device", device)
                    .getResultList();

            for(Interface interfaceRecord : localInterfaces) {
                CdpRecord record = new CdpRecord();
                record.localInterface = interfaceRecord;
                record.remoteInterface = remoteInterfaces.stream().filter(p->p.getConnection().getId() == interfaceRecord.connection.getId()).collect(Collectors.toList()).get(0);
                result.add(record);
            }
//            localInterfaces.stream().forEach(p->(){
//                record.localInterface = p;
//
//            });

        } catch (NoResultException e) {
        }
        return result;
    }
}
