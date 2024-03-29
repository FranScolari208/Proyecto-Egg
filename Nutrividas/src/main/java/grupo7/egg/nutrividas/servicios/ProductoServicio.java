package grupo7.egg.nutrividas.servicios;

import grupo7.egg.nutrividas.entidades.*;
import grupo7.egg.nutrividas.enums.Categoria;
import grupo7.egg.nutrividas.exeptions.FieldAlreadyExistException;
import grupo7.egg.nutrividas.exeptions.FieldInvalidException;
import grupo7.egg.nutrividas.repositorios.ProductoRepository;
import grupo7.egg.nutrividas.util.Validations;
import grupo7.egg.nutrividas.util.paginacion.Paged;
import grupo7.egg.nutrividas.util.paginacion.Paging;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductoServicio {


    private ProductoRepository productoRepository;

    private MarcaServicio marcaServicio;

    @Transactional
    public Producto crearProducto(String nombre, Long idMarca, Double precio, Categoria categoria,
                                  Boolean aptoIntoleranteLactosa, Boolean aptoCeliaco, Boolean aptoHipertenso,
                                  Boolean aptoDiabeticos){

        Marca marca = marcaServicio.buscarPorId(idMarca);
        if(productoRepository.existsByNombreAndMarca_Nombre(nombre,marca.getNombre())){
            throw new FieldAlreadyExistException("Ya esite un producto registrado con el mismo nombre y marca");
        }

        Producto producto = new Producto();
        producto.setNombre(Validations.formatText(nombre));
        producto.setMarca(marcaServicio.buscarPorId(idMarca));
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setAptoIntoleranteLactosa(aptoIntoleranteLactosa);
        producto.setAptoCeliacos(aptoCeliaco);
        producto.setAptoHipertensos(aptoHipertenso);
        producto.setAptoDiabeticos(aptoDiabeticos);
        producto.setAlta(true);

        return productoRepository.save(producto);
    }


    @Transactional
    public Producto modificarProducto(Long id,String nombre, Long idMarca, Double precio, Categoria categoria,
                                  Boolean aptoIntoleranteLactosa, Boolean aptoCeliaco, Boolean aptoHipertenso,
                                  Boolean aptoDiabeticos){

        Marca marca = marcaServicio.buscarPorId(idMarca);
        Producto producto = productoRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No se encontró un producto con el id "+id));

        if(productoRepository.existsByNombreAndMarca_Nombre(nombre,marca.getNombre()) &&
                (productoRepository.findByNombreAndMarca_Nombre(nombre,marca.getNombre()).get().getId() != id)){

            throw new FieldAlreadyExistException("Ya exisite un producto registrado con el mismo nombre y marca");
        }

        producto.setNombre(Validations.formatText(nombre));
        producto.setMarca(marca);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setAptoIntoleranteLactosa(aptoIntoleranteLactosa);
        producto.setAptoCeliacos(aptoCeliaco);
        producto.setAptoHipertensos(aptoHipertenso);
        producto.setAptoDiabeticos(aptoDiabeticos);
        producto.setAlta(true);

        return productoRepository.save(producto);
    }

    @Transactional
    public void deshabilitarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("No se existe un producto asociado al id"+id));
        productoRepository.deleteById(producto.getId());
    }

    @Transactional
    public void habilitarProducto(Long id) {
        productoRepository.habilitarProducto(id);
    }

    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Long id){
        return productoRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No se encontró un producto con el id "+id));
    }

    @Transactional
    public void crearFoto(Foto foto, Long id){
        if(foto == null){
            throw new FieldInvalidException("La imagen no puede ser nula");
        }
        productoRepository.findById(id).orElseThrow(
                ()->new NoSuchElementException("No se halló un producto con el id '"+id+"'"));

        productoRepository.actualizarFoto(foto,id);
    }

    @Transactional(readOnly = true)
    public Paged<Producto> buscarTodos(int page, int size,Sort order){
        PageRequest request = PageRequest.of(page - 1, size, order);
        Page<Producto> productPage = productoRepository.findAll(request);
        return new Paged(productPage, Paging.of(productPage.getTotalPages(), page, size));
    }

    @Transactional(readOnly = true)
    public Paged<Producto> buscarPorTodosCampos(String busqueda, int page, int size,Sort order){
        PageRequest request = PageRequest.of(page - 1, size, order);
        Page<Producto> productosPage = productoRepository.buscarPorTodosCampos(busqueda,request);
        return new Paged(productosPage, Paging.of(productosPage.getTotalPages(), page, size));
    }

    @Transactional(readOnly = true)
    public List<Producto> listarProductos(){
        return productoRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Paged<Producto> buscarPorCategoria(String categoria,int page, int size,Sort order){

        Pageable request = PageRequest.of(page - 1, size, order);
        Page<Producto> productPage = productoRepository.findByCategoria(Validations.getCategoria(categoria),request);
        return new Paged(productPage, Paging.of(productPage.getTotalPages(), page, size));
    }

    @Transactional(readOnly = true)
    public Paged<Producto> buscarApto(Boolean apto,String patologia, int page, int size,Sort order){

        Pageable request = PageRequest.of(page - 1, size, order);
        Page<Producto> productPage = null;

        switch (patologia){
            case "celiacos":
                productPage = productoRepository.findByAptoCeliacos(apto,request);
                break;
            case  "diabeticos":
                productPage = productoRepository.findByAptoDiabeticos(apto,request);
                break;
            case "hipertensos":
                productPage = productoRepository.findByAptoHipertensos(apto,request);
                break;
            case "intolerantesLactosa":
                productPage = productoRepository.findByAptoIntoleranteLactosa(apto,request);
                break;
        }
        return new Paged(productPage, Paging.of(productPage.getTotalPages(), page, size));
    }

}