package plus.fort.itinform.model;

import java.util.regex.Pattern;

public class CiscoPatterns {

    public final static Pattern deviceId = Pattern.compile("^Device ID:\\s*([A-Za-z][A-Za-z0-9-]{0,60}).+$", Pattern.MULTILINE);
    public final static Pattern localInterface = Pattern.compile("^Interface: (.*),", Pattern.MULTILINE);
    public final static Pattern remotePlatform = Pattern.compile("^Platform: (.*),", Pattern.MULTILINE);
    public final static Pattern remoteInterfacePattern = Pattern.compile("Port ID .outgoing port.: (.*)$", Pattern.MULTILINE);
    public final static Pattern remoteAddress = Pattern.compile("IP.*[aA]ddress: (.*)$", Pattern.MULTILINE);
    public final static Pattern localName = Pattern.compile("^([A-Za-z][A-Za-z0-9-./]{0,60}[A-Za-z0-9])?(>|#)\\s?(?!\\n{1,})$", Pattern.MULTILINE);
    public final static Pattern prompt = Pattern.compile("^[A-Za-z][A-Za-z0-9-./]{0,60}[A-Za-z0-9]?(>|#)\\s?(?!\\n{1,})$", Pattern.MULTILINE);
    public final static Pattern deviceCapability= Pattern.compile("Capabilities: (.*)$", Pattern.MULTILINE);
}
