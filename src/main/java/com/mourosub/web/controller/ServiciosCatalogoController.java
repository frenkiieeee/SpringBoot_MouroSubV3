package com.mourosub.web.controller;

import com.mourosub.web.model.Actividad;
import com.mourosub.web.model.Alquileres;
import com.mourosub.web.model.Curso;
import com.mourosub.web.model.Inmersiones;
import com.mourosub.web.model.Seguros;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.CursoRepository;
import com.mourosub.web.repository.InmersionRepository;
import com.mourosub.web.repository.UsuarioRepository;
import com.mourosub.web.service.MaterialService;
import com.mourosub.web.service.SegurosService;
import com.mourosub.web.service.ServicioActividades;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/servicios")
public class ServiciosCatalogoController {

    private final MaterialService materialService;
    private final SegurosService segurosService;
    private final ServicioActividades servicioActividades;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final InmersionRepository inmersionRepository;

    public ServiciosCatalogoController(MaterialService materialService,
                                       SegurosService segurosService,
                                       ServicioActividades servicioActividades,
                                       UsuarioRepository usuarioRepository,
                                       CursoRepository cursoRepository,
                                       InmersionRepository inmersionRepository) {
        this.materialService = materialService;
        this.segurosService = segurosService;
        this.servicioActividades = servicioActividades;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.inmersionRepository = inmersionRepository;
    }

    @GetMapping("/cursos")
    public String cursos(Model model) {
        List<Curso> cursos = cursoRepository.findAllActive();
        model.addAttribute("cursos", cursos);
        return "servicios/cursos";
    }

    @GetMapping("/inmersiones")
    public String inmersiones(Model model) {
        List<Inmersiones> inmersiones = inmersionRepository.findByActivoTrue();
        model.addAttribute("inmersiones", inmersiones);
        return "servicios/inmersiones";
    }

    @GetMapping("/actividades")
    public String actividades(Model model) {
        List<Actividad> actividadesList = servicioActividades.listarTodos().stream()
                .filter(Actividad::getActivo)
                .toList();
        model.addAttribute("actividades", actividadesList);
        return "servicios/actividades";
    }

    @GetMapping("/alquiler")
    public String alquiler(Model model) {
        model.addAttribute("alquiler", new Alquileres());
        model.addAttribute("seguro", new Seguros());
        model.addAttribute("materiales", materialService.listarTodos());
        return "servicios/alquiler";
    }

    @GetMapping("/seguros")
    public String seguros(Model model) {
        List<Seguros> segurosList = new java.util.ArrayList<>();

        Seguros defaultSeguro = new Seguros();
        defaultSeguro.setIdSeguro(0L);
        defaultSeguro.setNombre("Seguro Anual");
        defaultSeguro.setCompania("MouroSub Seguros");
        defaultSeguro.setCoberturaGastos(120.00);
        defaultSeguro.setDescripcion("Cobertura básica anual para buceo. Incluye asistencia médica internacional, responsabilidad civil y gastos de rescate.");
        defaultSeguro.setActivo(true);
        segurosList.add(defaultSeguro);

        segurosService.listarTodos().stream()
                .filter(Seguros::getActivo)
                .filter(s -> s.getIdSeguro() != 0)
                .forEach(segurosList::add);

        model.addAttribute("seguros", segurosList);
        return "servicios/seguros";
    }

    // Contratacion de un seguro por parte del usuario logueado.
    // Buscamos el seguro plantilla, hacemos una copia asociada al usuario y la guardamos.
    @PostMapping("/seguros/reservar")
    public String reservarSeguro(@RequestParam Long seguroId,
                                 @RequestParam(required = false) String supabaseUserId,
                                 RedirectAttributes flash) {
        // Sin sesion no se puede contratar; el popup de la vista cubre este caso pero
        // protegemos tambien el endpoint por si llega una peticion directa.
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            return "redirect:/login";
        }

        Usuario usuario;
        try {
            usuario = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId)).orElse(null);
        } catch (IllegalArgumentException ex) {
            usuario = null;
        }
        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscamos el seguro plantilla. Si llega el id 0 (la opcion fija que añade el listado)
        // creamos los datos a mano sin tocar la BD.
        Seguros plantilla = seguroId != null && seguroId > 0
                ? segurosService.buscarPorId(seguroId)
                : null;

        Seguros contratado = new Seguros();
        contratado.setUsuario(usuario);
        contratado.setActivo(true);
        if (plantilla != null) {
            contratado.setNombre(plantilla.getNombre());
            contratado.setCompania(plantilla.getCompania());
            contratado.setCoberturaGastos(plantilla.getCoberturaGastos());
            contratado.setDescripcion(plantilla.getDescripcion());
        } else {
            contratado.setNombre("Seguro Anual");
            contratado.setCompania("MouroSub Seguros");
            contratado.setCoberturaGastos(120.00);
            contratado.setDescripcion("Cobertura basica anual para buceo.");
        }
        segurosService.guardarSeguro(contratado);

        flash.addFlashAttribute("ok", "Seguro contratado correctamente. Tienes el detalle en tu cuenta.");
        return "redirect:/servicios/seguros";
    }

    @GetMapping("/material")
    public String material(Model model) {
        model.addAttribute("materiales", materialService.listarTodos());
        return "servicios/material";
    }
}
