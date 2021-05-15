package plus.fort.itinform.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NetworkDiscoveryRecords {

    List<NetworkDiscoveryRecord> records;

    public NetworkDiscoveryRecords() {
        records = new ArrayList<>();
    }

    public List<NetworkDiscoveryRecord> getRecords() {
        return records;
    }

    public int addRecords(Set<String> records) {
        int response=0;
        for(String record :records) {
            if(!this.records.stream().anyMatch( o -> o.getDevice().getAddress().equals(record))) {
                this.records.add(new NetworkDiscoveryRecord(record));
                response ++;
            }
        }
        return response;
    }
}
