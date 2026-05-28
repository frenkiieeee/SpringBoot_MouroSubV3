package com.mourosub.web.controller;

/* comentario: controlador modificado para permitir reservar seguros sin redirigir al login y añadir endpoint JSON para mis seguros */

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

        // Seguro por defecto (plantilla fija con id 0) que siempre se ofrece.
        Seguros defaultSeguro = new Seguros();
        defaultSeguro.setIdSeguro(0L);
        defaultSeguro.setNombre("Seguro Anual");
        defaultSeguro.setCompania("MouroSub Seguros");
        defaultSeguro.setCoberturaGastos(120.00);
        defaultSeguro.setDescripcion("Cobertura básica anual para buceo. Incluye asistencia médica internacional, responsabilidad civil y gastos de rescate.");
        defaultSeguro.setActivo(true);
        segurosList.add(defaultSeguro);

        // Solo mostramos en el catalogo las plantillas (sin usuario asociado). Asi las
        // copias contratadas por usuarios NO aparecen aqui y no se ve duplicado.
        segurosService.listarPlantillas().stream()
                .filter(Seguros::getActivo)
                .filter(s -> s.getIdSeguro() != 0)
                .forEach(segurosList::add);

        model.addAttribute("seguros", segurosList);
        return "servicios/seguros";
    }

    // Contratacion de un seguro por parte del usuario logueado.
    // Buscamos el seguro plantilla, hacemos una copia asociada al usuario y la guardamos.
    @PostMapping("/seguros/reservar")
    public String reservarSeguro(@RequestParam(required = false) Long seguroId,
                                 @RequestParam(required = false) String supabaseUserId,
                                 RedirectAttributes flash) {
        // Sin sesion no se puede contratar; el popup de la vista cubre este caso pero
        // protegemos tambien el endpoint por si llega una peticion directa.
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            flash.addFlashAttribute("error", "Necesitas iniciar sesion para contratar un seguro.");
            return "redirect:/servicios/seguros";
        }

        Usuario usuario;
        try {
            usuario = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId)).orElse(null);
        } catch (IllegalArgumentException ex) {
            usuario = null;
        }
        if (usuario == null) {
            flash.addFlashAttribute("error", "Necesitas iniciar sesion para contratar un seguro.");
            return "redirect:/servicios/seguros";
        }

        // Buscamos el seguro plantilla. Si llega el id 0 (la opcion fija que añade el listado)
        // creamos los datos a mano sin tocar la BD.
        Seguros plantilla = null;
        if (seguroId == null) {
            flash.addFlashAttribute("error", "Selecciona un seguro valido antes de continuar.");
            return "redirect:/servicios/seguros";
        }
        if (seguroId > 0) {
            try {
                plantilla = segurosService.buscarPorId(seguroId);
            } catch (RuntimeException ex) {
                flash.addFlashAttribute("error", "El seguro seleccionado ya no esta disponible.");
                return "redirect:/servicios/seguros";
            }
        }

        String nombre = plantilla != null ? plantilla.getNombre() : "Seguro Anual";

        // Evitamos duplicados: si el usuario ya tiene contratado un seguro con ese nombre,
        // no creamos otra copia y avisamos.
        if (segurosService.yaContratado(usuario.getidUsuario(), nombre)) {
            flash.addFlashAttribute("error", "Ya tienes contratado el seguro \"" + nombre + "\".");
            return "redirect:/servicios/seguros";
        }

        Seguros contratado = new Seguros();
        contratado.setUsuario(usuario);
        contratado.setActivo(true);
        contratado.setNombre(nombre);
        if (plantilla != null) {
            contratado.setCompania(plantilla.getCompania());
            contratado.setCoberturaGastos(plantilla.getCoberturaGastos());
            contratado.setDescripcion(plantilla.getDescripcion());
        } else {
            contratado.setCompania("MouroSub Seguros");
            contratado.setCoberturaGastos(120.00);
            contratado.setDescripcion("Cobertura basica anual para buceo.");
        }
        segurosService.guardarSeguro(contratado);

        flash.addFlashAttribute("ok", "Seguro contratado correctamente. Tienes el detalle en tu cuenta.");
        return "redirect:/servicios/seguros";
    }

    @GetMapping("/seguros/mis-seguros-json")
    @ResponseBody
    public List<Map<String, Object>> misSegurosJson(@RequestParam(required = false) String supabaseUserId) {
        List<Map<String, Object>> response = new ArrayList<>();
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            return response;
        }
        try {
            Usuario usuario = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId)).orElse(null);
            if (usuario == null) {
                return response;
            }
            List<Seguros> seguros = segurosService.listarPorUsuario(usuario.getidUsuario());
            for (Seguros s : seguros) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("idSeguro", s.getIdSeguro());
                item.put("nombre", s.getNombre());
                item.put("compania", s.getCompania());
                item.put("coberturaGastos", s.getCoberturaGastos());
                item.put("descripcion", s.getDescripcion());
                item.put("activo", s.getActivo());
                response.add(item);
            }
        } catch (IllegalArgumentException ignored) {
            return response;
        }
        return response;
    }

    // Lista los seguros que el usuario logueado ya tiene contratados.
    // Cierra el flujo: contratar -> aparece aqui. Se accede desde "Mi cuenta".
    @GetMapping("/mis-seguros")
    public String misSeguros(@RequestParam(required = false) String supabaseUserId, Model model) {
        List<Seguros> seguros = new java.util.ArrayList<>();
        if (supabaseUserId != null && !supabaseUserId.isBlank()) {
            try {
                Usuario usuario = usuarioRepository.findBySupabaseUserId(UUID.fromString(supabaseUserId)).orElse(null);
                if (usuario != null) {
                    seguros = segurosService.listarPorUsuario(usuario.getidUsuario());
                }
            } catch (IllegalArgumentException ignored) {
                // id invalido: dejamos la lista vacia y la vista mostrara el aviso de login
            }
        }
        model.addAttribute("seguros", seguros);
        return "servicios/mis-seguros";
    }

    @GetMapping("/material")
    public String material(Model model) {
        model.addAttribute("materiales", materialService.listarTodos());
        return "servicios/material";
    }
}
