package plus.fort.itinform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Device")
@Data
@EqualsAndHashCode(of = {"name"})
public class Device {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "device_name", unique = true, nullable = false)
    private String name;

    @Column(name = "device_address")
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Interface> interfaces;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private DeviceType deviceType;

    public Device() {
        deviceType = new DeviceType();
    }

    public String toString() {
        return name;
    }
}
