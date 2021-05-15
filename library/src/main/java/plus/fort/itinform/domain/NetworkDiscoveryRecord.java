package plus.fort.itinform.domain;

import lombok.Data;

@Data
public class NetworkDiscoveryRecord {

    private Boolean knownHost=false;
    private Boolean discoveryFail=false;
    private Boolean scanned=false;
    private Device device = new Device();

    private String failMessage;

    public NetworkDiscoveryRecord(String address) {
        device.setAddress(address);
    }

    public NetworkDiscoveryRecord() {
    }

}
