package plus.fort.itinform.model;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import plus.fort.itinform.domain.CdpRecord;
import plus.fort.itinform.domain.Device;
import plus.fort.itinform.service.SshConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CiscoDevice implements AutoCloseable {

    private static final Logger logger = LogManager.getLogger(CiscoDevice.class);
    @Autowired
    SshConnectionService sshConnectionService;
    private String host;

    public CiscoDevice(SshConnectionService sshConnectionService) {
        this.sshConnectionService = sshConnectionService;
    }

    public List<CdpRecord> findCdpNeighbors(String host) {
        String response = "";
        this.host = host;

        List<CdpRecord> cdpRecords = new ArrayList<>();

        try {
            sshConnectionService.connect(host);
            sshConnectionService.sendMessage("show cdp nei detail\n");
            response = sshConnectionService.readUntilPattern();
            cdpRecords.addAll(parseCdpNeighbors(response));
        } catch (Exception e) {
            logger.log(Level.ERROR, "got exception when collect data from host:" + host + ", " + e.getMessage());
        }

        return cdpRecords;
    }

    private List<CdpRecord> parseCdpNeighbors(String cdpInfo) {
        List<CdpRecord> cdpRecords = new ArrayList<>();
        String localName = findPattern(cdpInfo, CiscoPatterns.localName);

        String[] cdpNeighbors = cdpInfo.split("-------------------------");

        for (String cdpNeighbor : cdpNeighbors) {
            CdpRecord cdpRecord = new CdpRecord();

            cdpRecord.localInterface.setDevice(new Device());
            cdpRecord.localInterface.device.setName(localName);
            cdpRecord.localInterface.device.setAddress(host);
            cdpRecord.localInterface.setName(findPattern(cdpNeighbor, CiscoPatterns.localInterface));

            cdpRecord.remoteInterface.setDevice(new Device());
            cdpRecord.remoteInterface.device.setAddress(findPattern(cdpNeighbor, CiscoPatterns.remoteAddress));
            cdpRecord.remoteInterface.device.setName(findPattern(cdpNeighbor, CiscoPatterns.deviceId));
            cdpRecord.remoteInterface.setName(findPattern(cdpNeighbor, CiscoPatterns.remoteInterfacePattern));
            cdpRecord.remotePlatform = findPattern(cdpNeighbor, CiscoPatterns.remotePlatform);
            cdpRecord.remoteInterface.device.getDeviceType().setName(findPattern(cdpNeighbor, CiscoPatterns.deviceCapability));

            if (cdpRecord.isValid()) {
                cdpRecords.add(cdpRecord);
            }
        }
        return cdpRecords;
    }

    public String findPattern(String text, Pattern pattern) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1) : "";
    }

    @Override
    public void close() throws Exception {
        //TODO
    }

    public void setCredentionals(String loginName, String password) {
    }
}
