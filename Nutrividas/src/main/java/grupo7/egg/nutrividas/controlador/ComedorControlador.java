package grupo7.egg.nutrividas.controlador;

import grupo7.egg.nutrividas.entidades.Comedor;
import grupo7.egg.nutrividas.entidades.Persona;
import grupo7.egg.nutrividas.enums.Sexo;
import grupo7.egg.nutrividas.repositorios.PersonaRepository;
import grupo7.egg.nutrividas.servicios.ComedorServicio;
import grupo7.egg.nutrividas.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/comedor")
public class ComedorControlador {

    @Autowired
    private ComedorServicio comedorServicio;

    @GetMapping
    public ModelAndView mostrarComedores(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                         @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                         @RequestParam(value = "order", required = false, defaultValue = "OrderByNombreASC") Sort order,
                                         HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("comedores");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("exito", flashMap.get("exito"));
            mav.addObject("error", flashMap.get("error"));
        }

        mav.addObject("comedores", comedorServicio.listarComedores(page, size, order));
        return mav;
    }

    @GetMapping("/crear")
    public ModelAndView crearComedor(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("comedor-formulario");
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            mav.addObject("error", flashMap.get("error"));
            mav.addObject("comedor", flashMap.get("comedor"));
        } else {
            mav.addObject("comedor", new Comedor());
        }

        mav.addObject("title", "Ingresar Comedor");
        mav.addObject("action", "guardar");
        return mav;
    }

    @PostMapping("/guardar")
    public RedirectView guardarComedor(@RequestParam String nombre, @RequestParam String apellido, @RequestParam Long documento, @RequestParam LocalDate fechaNacimiento,
                                       @RequestParam Double altura, @RequestParam Double peso,
                                       @RequestParam Boolean aptoCeliacos, @RequestParam Boolean aptoHipertensos,
                                       @RequestParam Boolean aptoDiabeticos, @RequestParam Boolean aptoIntoleranteLactosa,
                                       @RequestParam Sexo sexo, @RequestParam Long idComedor, RedirectAttributes attributes){
        RedirectView redirectView = new RedirectView("/comedor");

        //modificar parametros
        try {
            //comedorServicio.crearComedor(nombre, apellido, documento, fechaNacimiento, peso, altura, aptoIntoleranteLactosa, aptoCeliacos, aptoHipertensos, aptoDiabeticos, sexo, idComedor);
            attributes.addFlashAttribute("exito", "La creación ha sido realizada satisfactoriamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", e.getMessage());
            redirectView.setUrl("/comedor/crear");
        }

        return redirectView;
    }

}