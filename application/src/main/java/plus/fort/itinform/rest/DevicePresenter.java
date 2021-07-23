package plus.fort.itinform.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import plus.fort.itinform.domain.CdpRecord;
import plus.fort.itinform.domain.Device;
import plus.fort.itinform.exception.NotFoundException;
import plus.fort.itinform.model.DeviceModel;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DevicePresenter extends BasePresenter {

    private final DeviceModel deviceModel;

    public DevicePresenter(DeviceModel deviceModel) {
        super();
        this.deviceModel = deviceModel;
    }

    @GetMapping(path = "/rest/v1/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Device getDevice(@PathVariable Long id) throws NotFoundException {
        try {
            return deviceModel.getDevice(id);
        } catch (Exception e) {
            throw new NotFoundException();
        }
    }

    @GetMapping(path = "/rest/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Device> getDevice() {
        return deviceModel.getDevice();
    }


    @GetMapping(path = "/rest/v1/device/{id}/interfaces", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CdpRecord> getInterfaces(@PathVariable long id) {
        List<CdpRecord> result = deviceModel.getInterfaces(id);
        return result;
    }

}
