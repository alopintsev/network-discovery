package plus.fort.itinform.model;

import org.junit.jupiter.api.Test;
import plus.fort.itinform.domain.CdpRecord;
import plus.fort.itinform.service.SshConnectionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CiscoDeviceTest {

    @Test
    public void findCdpNeighborsTest() throws Exception {
        List<CdpRecord> cdpRecords = null;
        String cdpNeighbors = "";
        SshConnectionService sshConnectionService = mock(SshConnectionService.class);

        cdpNeighbors = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("show-cdp-nei-detail-test.txt").toURI())));
        when(sshConnectionService.readUntilPattern()).thenReturn(cdpNeighbors);

        try (CiscoDevice ciscoDevice = new CiscoDevice(sshConnectionService)) {
            cdpRecords = ciscoDevice.findCdpNeighbors("some.host");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(cdpRecords.size(), 3);
    }

}
