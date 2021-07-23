package plus.fort.itinform.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table( name = "Interface" ,
        uniqueConstraints=
        @UniqueConstraint(columnNames={"device_id", "name"}))
@Data
@EqualsAndHashCode(of = {"name", "device"})
public class Interface {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "device_id", foreignKey=@ForeignKey(name="FK_INTERFACE__DEVICE"))
    public Device device;// = new Device();

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name = "connection_id", foreignKey=@ForeignKey(name="FK_INTERFACE__CONNECTION"))
    public Connection connection;

    @Column(nullable = false)
    public String name;

    public Interface(){}

    public Interface(String name, Device device) {
        this.name = name;
        this.device = device;
    }
    public Interface(Interface interf) {
        this.name = interf.getName();
        this.device = interf.getDevice();
    }

}

