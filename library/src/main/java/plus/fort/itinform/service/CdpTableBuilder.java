package plus.fort.itinform.service;

import plus.fort.itinform.domain.CdpRecord;

import java.util.List;

public class CdpTableBuilder {
    public static String showCdpTable( List<CdpRecord> cdpRecords) {
        String result = "";
        for(CdpRecord cdpRecord : cdpRecords) {
            result += cdpRecord.localInterface.device.getName() + " | " + cdpRecord.localInterface.name + " | " + cdpRecord.remoteInterface.device.getName() + " | " + cdpRecord.remoteInterface.name + "\n";
        }
        return result;
    }
}
