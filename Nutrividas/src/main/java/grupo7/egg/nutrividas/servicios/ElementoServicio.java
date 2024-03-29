package grupo7.egg.nutrividas.servicios;


import grupo7.egg.nutrividas.entidades.*;
import grupo7.egg.nutrividas.exeptions.FieldAlreadyExistException;
import grupo7.egg.nutrividas.exeptions.FieldInvalidException;
import grupo7.egg.nutrividas.repositorios.CanastaRepository;
import grupo7.egg.nutrividas.repositorios.ElementoRepository;
import grupo7.egg.nutrividas.repositorios.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ElementoServicio {

    private ElementoRepository elementoRepository;

    private ProductoServicio productoServicio;


    @Transactional
    public Elemento crearElemento(Long idProducto, Credencial credencial){

        if(idProducto == null){
            throw new FieldInvalidException("El producto es obligatorio");
        }

        Elemento elemento = new Elemento();

        elemento.setProducto(productoServicio.obtenerProductoPorId(idProducto));
        elemento.setCantidadNecesaria(1);
        elemento.setCantidadComprada(0);
        elemento.setFueComprado(false);
        elemento.setCredencial(credencial);
        elemento.setAsignado(false);
        return elementoRepository.save(elemento);
    }

    @Transactional
    public Elemento editarCantidad(Long idElemento,Integer cantidadNecesaria,Boolean asignado){

        Elemento elemento = elementoRepository.findById(idElemento).orElseThrow(
                () -> new NoSuchElementException("No existe un elemento con en id '"+idElemento+"'")
        );

        if(cantidadNecesaria == null || cantidadNecesaria <= 0){
            throw new FieldInvalidException("Error en cantidad Necesaria");
        }

        elemento.setCantidadNecesaria(cantidadNecesaria);
        elemento.setAsignado(asignado);
        return elementoRepository.save(elemento);
    }

    @Transactional
    public void eliminarElemento(Long id){
        Elemento elemento = elementoRepository.findById(id).orElseThrow(
                ()-> new NoSuchElementException("El elemento que desea eliminar no existe"));
        elementoRepository.deleteById(elemento.getId());
    }

    @Transactional
    public void eliminarElementosProducto(Long idProducto){
        List<Elemento> elementos = elementoRepository.findByProducto_Id(idProducto);
        elementos.forEach(e -> elementoRepository.deleteById(e.getId()));
    }

    @Transactional(readOnly = true)
    public Optional<Elemento> existeElementoSesion(Long idProducto, String mail){
        Producto producto = productoServicio.obtenerProductoPorId(idProducto);
        return elementoRepository.existeElementoSesion(producto,mail);
    }

    @Transactional(readOnly = true)
    public List<Elemento> buscarPorProducto(Long idProducto){
        return elementoRepository.findByProducto_Id(idProducto);
    }

    @Transactional(readOnly = true)
    public List<Elemento> obtenerElemntosSesion(String mail){
        return elementoRepository.obtenerElementosSesion(mail);
    }

    @Transactional
    public void asignarACanasta(Elemento elemento, Canasta canasta){
        elemento.setCanasta(canasta);
        elemento.setAsignado(true);
        elementoRepository.save(elemento);
    }


    @Transactional
    public void comprarCantidadDeElemento(Long idElemento, Integer cantidadComprada){
        Elemento elemento = elementoRepository.obtenerElementoPorId(idElemento);
        Integer cantidadNecesaria = elemento.getCantidadNecesaria();
        Integer cantidad = elemento.getCantidadComprada();

        // no puedo comprar mas de lo que se necesita
        if(cantidadComprada > cantidadNecesaria){
            throw new FieldInvalidException("La cantidad a comprar no puede ser mayor a la necesaria");
        }

        // si la cantidad que se compra no lanza excepcion cambio la cantidad comprada a la cantidad anterior + lo comprado
        elementoRepository.comprarCantidadDeElemento(idElemento, cantidadComprada + cantidad);

        //modifico la nueva cantidad necesaria
        Integer nuevaCantidadNecesaria = cantidadNecesaria - cantidadComprada;
        elementoRepository.cambiarCantidadNecesaria(idElemento, nuevaCantidadNecesaria);

        //si la cantidad necesaria luego de comprar una cantidad x es igual a 0, se da al elemento por comprado
        if(nuevaCantidadNecesaria == 0){
            comprarElemento(idElemento);
        }
    }

    @Transactional
    public void comprarElemento(Long idElemento){
        elementoRepository.comprarElemento(idElemento);
    }


}
