package plus.fort.itinform.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "connection")
@Data
@EqualsAndHashCode(of = {"name"})
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Connection(){}
    public Connection(String name) {
        this.name = name;
    }
}
