package plus.fort.itinform.domain;

public class CdpRecord {
    public Interface localInterface = new Interface();
    public Interface remoteInterface = new Interface();
    public String remotePlatform = "";

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


