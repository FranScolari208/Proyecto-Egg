package grupo7.egg.nutrividas.entidades;

import grupo7.egg.nutrividas.enums.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name="productos")
@SQLDelete(sql = "UPDATE Productos SET alta = false WHERE id = ?")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "El campo 'nombre' es obligatorio xxx")
    @Pattern(regexp = "^[\\p{L} .'-]+$",message="Solo se permiten letras" )
    private String nombre;
    @OneToOne
    private Marca marca;
    @DecimalMin(value = "0.5", message = "El precio debe se mayor o igual a 0.5")
    private Double precio;
    private Boolean aptoCeliacos;
    private Boolean aptoHipertensos;
    private Boolean aptoDiabeticos;
    private Boolean aptoIntoleranteLactosa;
    private Categoria categoria;

    @OneToOne
    private Foto foto;

    @CreatedDate
    @Column( updatable = false)
    private LocalDateTime creacion;

    @LastModifiedDate
    private LocalDateTime modificacion;

    private Boolean alta;
}
