package plus.fort.itinform.model;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import plus.fort.itinform.domain.*;
import plus.fort.itinform.service.CdpTableBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class NetworkModel {

    public static final int MAX_NESTING_LEVEL = 2;
    private static final Logger logger = LogManager.getLogger(NetworkModel.class);
    @Autowired
    CiscoDevice ciscoDevice;
    List<CdpRecord> cdpRecords = new ArrayList<>();
    NetworkDiscoveryRecords networkDiscoveryRecords = new NetworkDiscoveryRecords();
    private int nestingLevel = MAX_NESTING_LEVEL;

    public Set<Device> getHosts() {
        Set<Device> devices = new HashSet<>();
        cdpRecords.stream().forEach(o -> devices.add(o.remoteInterface.device));
        cdpRecords.stream().forEach(o -> devices.add(o.localInterface.device));
        return devices;
    }

    public List<CdpRecord> discovery(String host) {
        initDiscoveryRecords(host);
        for (int discoveryAttempts = 0; discoveryAttempts <= nestingLevel; discoveryAttempts++) {
            logger.log(Level.INFO, "current discovery nesting level:" + discoveryAttempts);

            Set<String> neighborsTmp = new HashSet<>();
            for (NetworkDiscoveryRecord networkDiscoveryRecord : networkDiscoveryRecords.getRecords()) {
                if (networkDiscoveryRecord.getScanned()) {
                    continue;
                }

                String deviceAddress = networkDiscoveryRecord.getDevice().getAddress();
                logger.log(Level.INFO, "looking for host CDP neighbors:" + deviceAddress);
                neighborsTmp.addAll(discoveryNeighbors(deviceAddress));
                networkDiscoveryRecord.setScanned(true);
            }
            networkDiscoveryRecords.addRecords(neighborsTmp);
        }
        logger.log(Level.INFO, "\n" + CdpTableBuilder.showCdpTable(cdpRecords));
        return cdpRecords;
    }

    public void setMaxNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
    }

    private void initDiscoveryRecords(String host) {
        NetworkDiscoveryRecord networkDiscoveryRecord = new NetworkDiscoveryRecord();
        networkDiscoveryRecord.getDevice().setAddress(host);
        networkDiscoveryRecords.getRecords().add(networkDiscoveryRecord);
    }

    public Set<String> discoveryNeighbors(String host) {
        Set<String> neighbors = new HashSet<>();
        cdpRecords.addAll(ciscoDevice.findCdpNeighbors(host));
        for (CdpRecord cdpRecord : cdpRecords) {
            if (cdpRecord.remotePlatform.matches(".*IP Phone.*")) {
                continue;
            }
            if (cdpRecord.remotePlatform.matches(".*mware.*")) {
                continue;
            }
            neighbors.add(cdpRecord.remoteInterface.device.getAddress());
        }
        return neighbors;
    }

    public Set<DeviceType> getDeviceTypes() {
        Set<DeviceType> deviceTypes = new HashSet<>();
        cdpRecords.stream().forEach(o -> deviceTypes.add(o.localInterface.device.getDeviceType()));
        cdpRecords.stream().forEach(o -> deviceTypes.add(o.remoteInterface.device.getDeviceType()));
        return deviceTypes;
    }

}
