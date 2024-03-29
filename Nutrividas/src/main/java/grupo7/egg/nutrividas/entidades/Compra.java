package grupo7.egg.nutrividas.entidades;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="compras")
@SQLDelete(sql = "UPDATE compra SET alta = false WHERE id = ?")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "compra")
    private List<DetalleCompra> detalleCompras;

    private Double precioFinal;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Tarjeta tarjeta;


    private Boolean alta;

    private Boolean asignada;

}
