package plus.fort.itinform.service;


import org.junit.jupiter.api.Test;
import plus.fort.itinform.domain.CdpRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SshConnectionServiceTest {

    @Test
    public void checkConnectionTest()  {
        String response = null;
        List<CdpRecord> cdpRecords = new ArrayList<>();
        try (SshConnectionService sshConnectionService = new SshConnectionService(null)) {
            sshConnectionService.connect("172.18.209.3");
            sshConnectionService.sendMessage("show ver\n");
            response = sshConnectionService.readUntilPattern();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(response);
    }


}
