package plus.fort.itinform.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "device_type")
@Data
@EqualsAndHashCode(of = {"name"})
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name="-";

    public DeviceType() {
    }
    public DeviceType(String name) {
        this.name = name;
    }
}
