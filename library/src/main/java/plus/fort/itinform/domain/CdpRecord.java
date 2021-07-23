package plus.fort.itinform.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize(using = CdpRecordSerializer.class)
public class CdpRecord {

    public Interface localInterface;
    public Interface remoteInterface;
    public String remotePlatform = "";


    public CdpRecord() {
        this.localInterface = new Interface();
        this.remoteInterface = new Interface();
    }

    public boolean isValid() {
        return localInterface.name.length() > 0;
    }

    public String toString() {
        return localInterface.device.getName() +
                ":" + localInterface.getName() +
                " - " + remoteInterface.device.getName() +
                ":" + remoteInterface.getName();
    }
}
