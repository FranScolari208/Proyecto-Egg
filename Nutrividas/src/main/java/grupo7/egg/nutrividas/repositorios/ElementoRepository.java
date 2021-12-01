package grupo7.egg.nutrividas.repositorios;


import grupo7.egg.nutrividas.entidades.Elemento;
import grupo7.egg.nutrividas.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementoRepository extends JpaRepository<Elemento,Long> {

    @Query("SELECT e FROM Elemento e WHERE e.id = :id")
    Elemento obtenerElementoPorId(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Elemento e SET e.fueComprado = true WHERE e.id = :id")
    void comprarElemento(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Elemento e SET e.cantidadComprada = :cantidadComprada WHERE e.id = :id")
    void comprarCantidadDeElemento(@Param("id") Long id, @Param("cantidadComprada") Integer cantidadComprada);

    @Modifying
    @Query("UPDATE Elemento e SET e.cantidadNecesaria = :cantidadNecesaria WHERE e.id = :id")
    void cambiarCantidadNecesaria(@Param("id") Long id, @Param("cantidadNecesaria") Integer cantidadNecesaria);
}
