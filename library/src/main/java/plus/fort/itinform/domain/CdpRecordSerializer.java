package plus.fort.itinform.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CdpRecordSerializer extends StdSerializer<CdpRecord>
{

    private static final long serialVersionUID = 1L;

    public CdpRecordSerializer() {
        this(null);
    }

    protected CdpRecordSerializer(Class<CdpRecord> t) {
        super(t);
    }

    @Override
    public void serialize(CdpRecord cdpRecord, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        provider.defaultSerializeField("localInterfaceName", cdpRecord.localInterface.getName(), gen);
        provider.defaultSerializeField("remoteDeviceName", cdpRecord.remoteInterface.getDevice().getName(), gen);
        provider.defaultSerializeField("remoteInterfaceName", cdpRecord.remoteInterface.getName(), gen);
        provider.defaultSerializeField("connectionId", cdpRecord.localInterface.getConnection().getId(), gen);
        provider.defaultSerializeField("connectionName", cdpRecord.localInterface.getConnection().getName(), gen);
        gen.writeEndObject();
    }

}
