package grupo7.egg.nutrividas.entidades;

import grupo7.egg.nutrividas.enums.Provincia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import grupo7.egg.nutrividas.entidades.Biografia;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="comedores")
@SQLDelete(sql = "UPDATE Comedores SET alta = false WHERE id = ?")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Comedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "unique")
    private String nombre;
    private String direccion;
    private String localidad;
    private Provincia provincia;
    private Integer cantidadDePersonas;
    private Long telefono;
    @OneToOne
    private Biografia biografia;

    //Mapped by comedor ---> agregar hacemos la relación bidireccional añadiendo a Persona el atributo comedor
    @OneToMany(mappedBy = "comedor")
    private List<Persona> personas;
    //Mapped by comedor ---> agregar hacemos la relación bidireccional añadiendo a Canasta el atributo comedor
    @OneToMany(mappedBy = "comedor")
    private List<Canasta> canastas;

    @ManyToOne
    private Nutricionista nutricionista;

    private Boolean alta;

    @CreatedDate
    @Column( updatable = false)
    private LocalDateTime creacion;

    @LastModifiedDate
    private LocalDateTime modificacion;
}
