package com.mourosub.web.controller;
import java.util.*;
import com.mourosub.web.exception.exceptions;
import com.mourosub.web.exception.exceptions.ActividadNotFoundException;
import com.mourosub.web.model.*;
import com.mourosub.web.dto.ReservaFormDTO;
import com.mourosub.web.repository.ActividadRepository;
import com.mourosub.web.repository.CursoRepository;
import com.mourosub.web.repository.InmersionRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import com.mourosub.web.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// controller devuelve paginas web html y mapea todas las rutas bajo barra reservas
@Controller
@RequestMapping("/reservas")
public class ReservaController {

    // inyectamos el servicio y los repositorios necesarios para mover datos a la vista
    private final ReservaService reservaService;
    private final ReservaRepository reservaRepository;
    private final ActividadRepository actividadRepository;
    private final CursoRepository cursoRepository;
    private final InmersionRepository inmersionRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaController(
        ReservaService reservaService,
        ReservaRepository reservaRepository,
        ActividadRepository actividadRepository,
        CursoRepository cursoRepository,
        InmersionRepository inmersionRepository,
        UsuarioRepository usuarioRepository
    ){
        this.reservaService = reservaService;
        this.reservaRepository = reservaRepository;
        this.actividadRepository = actividadRepository;
        this.cursoRepository = cursoRepository;
        this.inmersionRepository = inmersionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Ruta base. Si llega con un id de actividad/curso/inmersion redirigimos al
    // sub-formulario correspondiente con ese id pre-seleccionado; si no, mostramos el menu.
    @GetMapping
    public String index(
        @RequestParam(required = false) Long actividadId,
        @RequestParam(required = false) Long cursoId,
        @RequestParam(required = false) Long inmersionId,
        Model model
    ){
        if (actividadId != null) return "redirect:/reservas/actividades?id=" + actividadId;
        if (cursoId != null) return "redirect:/reservas/cursos?id=" + cursoId;
        if (inmersionId != null) return "redirect:/reservas/inmersiones?id=" + inmersionId;

        model.addAttribute("seccion","inicio");
        return "fragments/reservas/index";
    }

    // prepara el formulario para reservar una actividad en concreto pasandole la id por url
    @GetMapping("/nueva")
    public String nueva(@RequestParam Long idActividad, Model model) {
        Actividad actividad = actividadRepository.findById(idActividad)
            .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada"));

        Reserva reserva = new Reserva();
        reserva.setActividad(actividad);

        // metemos en la mochila model todo lo que el html necesita para pintar la pantalla
        model.addAttribute("seccion", "formulario");
        model.addAttribute("titulo", "Reserva de " + actividad.getNombre());
        model.addAttribute("reserva", reserva);
        model.addAttribute("actividades", List.of(actividad));
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "fragments/reservas/index";
    }

    // GET /reservas/cursos -> formulario de reserva para cursos. Si llega ?id=X pre-seleccionamos ese curso.
    @GetMapping("/cursos")
    public String cursos(@RequestParam(required = false) Long id, Model model){
        List<Curso> todosLosCursos = cursoRepository.findAllActive();

        // Mapa con los cursos agrupados por categoria. Lo usa la tabla principal del form.
        Map<String, List<Curso>> cursosPorCategoria = new LinkedHashMap<>();
        // Version "ligera" del mapa anterior con solo idCurso, nombre y precio. La pasamos
        // al JS porque Thymeleaf no puede serializar la entidad entera (LocalDate, lazy
        // collections, etc) y trunca la respuesta cuando lo intenta.
        Map<String, List<Map<String, Object>>> cursosJson = new LinkedHashMap<>();

        for (Curso curso : todosLosCursos) {
            String cat = curso.getCategoria() != null ? curso.getCategoria() : "OTROS";
            cursosPorCategoria.computeIfAbsent(cat, k -> new ArrayList<>()).add(curso);

            Map<String, Object> resumen = new LinkedHashMap<>();
            resumen.put("idCurso", curso.getIdCurso());
            resumen.put("nombre", curso.getNombre());
            resumen.put("precio", curso.getPrecio());
            cursosJson.computeIfAbsent(cat, k -> new ArrayList<>()).add(resumen);
        }

        List<String> categorias = new ArrayList<>(cursosPorCategoria.keySet());

        model.addAttribute("seccion","cursos");
        model.addAttribute("titulo", "Cursos");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("cursosPorCategoria", cursosPorCategoria);
        model.addAttribute("cursosJson", cursosJson);
        model.addAttribute("categorias", categorias);
        // Id pre-seleccionado (puede ser null si entran al menu sin elegir nada).
        model.addAttribute("seleccionadoId", id);
        if (id != null) {
            // Si el curso no tiene categoria asignada usamos "OTROS" para que coincida con
            // la clave que se usa al agrupar cursosPorCategoria mas arriba.
            cursoRepository.findById(id).ifPresent(c -> {
                String cat = c.getCategoria() != null ? c.getCategoria() : "OTROS";
                model.addAttribute("seleccionadoCategoria", cat);
            });
        }

        return "fragments/reservas/index";
    }

    // prepara el formulario filtrando lo puramente de ocio como inmersiones o paseos
    @GetMapping("/actividades")
    public String actividades(@RequestParam(required = false) Long id, Model model){
        List<Actividad> todas = actividadRepository.findByActivoTrueOrderByCategoriaAscNombreAsc();

        Map<String, List<Actividad>> actividadesPorCategoria = new LinkedHashMap<>();
        for (Actividad a : todas) {
            String cat = a.getCategoria() != null ? a.getCategoria() : "OTROS";
            actividadesPorCategoria.computeIfAbsent(cat, k -> new ArrayList<>()).add(a);
        }

        model.addAttribute("seccion","actividades");
        model.addAttribute("titulo", "Actividades");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("actividadesPorCategoria", actividadesPorCategoria);
        model.addAttribute("seleccionadoId", id);
        return "fragments/reservas/index";
    }

    // formulario de reserva para inmersiones
    @GetMapping("/inmersiones")
    public String inmersiones(@RequestParam(required = false) Long id, Model model){
        model.addAttribute("seccion","inmersiones");
        model.addAttribute("titulo", "Inmersiones");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("inmersiones", inmersionRepository.findByActivoTrue());
        model.addAttribute("seleccionadoId", id);
        return "fragments/reservas/index";
    }

    // aqui llega el formulario relleno con metodo post para que lo guardemos
    @PostMapping("/guardar")
    public String guardar(
        @RequestParam(required = false) Long idActividad,
        @RequestParam(required = false) Long idCurso,
        @RequestParam(required = false) Long idInmersion,
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) String apellidos,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String telefono,
        @RequestParam (required = false) String dni,
        @RequestParam(required = false) String codigoPostal,
        @RequestParam(required = false, defaultValue = "1") Integer numParticipantes,
        @RequestParam(required = false) String supabaseUserId
    ) {
        // Buscamos al usuario logueado por su id de Supabase. Si no llega o no existe
        // mandamos al login (los formularios solo se pueden enviar con sesion iniciada).
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            return "redirect:/login";
        }
        UUID id;
        try {
            id = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException ex) {
            return "redirect:/login";
        }
        Usuario usuario = usuarioRepository.findBySupabaseUserId(id).orElse(null);
        if (usuario == null) return "redirect:/login";

        // buscamos la entidad correspondiente y montamos la reserva
        Reserva reserva = new Reserva();
        reserva.setNumParticipantes(numParticipantes);

        if (idActividad != null) {
            Actividad actividad = actividadRepository.findById(idActividad)
                .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada"));
            reserva.setActividad(actividad);
        } else if (idCurso != null) {
            Curso curso = cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            reserva.setCurso(curso);
        } else if (idInmersion != null) {
            Inmersiones inmersion = inmersionRepository.findById(idInmersion)
                .orElseThrow(() -> new RuntimeException("Inmersion no encontrada"));
            reserva.setInmersion(inmersion);
        }

        // le pasamos la bola al servicio para que haga los calculos asigne instructor y guarde en bd
        reservaService.crearReserva(reserva, List.of(usuario.getidUsuario()));

        // redirigimos a la vista de sus propias reservas para que vea que se ha guardado
        return "redirect:/reservas/mis-reservas/" + usuario.getidUsuario();
    }

    // lista el historial de reservas de un usuario buscando por su id interno
    @GetMapping("/mis-reservas/{idUsuario}")
    public String misReservas(@PathVariable Long idUsuario, Model model){
        model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(idUsuario));
        return "fragments/reservas/mis-reservas";
    }

    // Atajo desde el menu del header: el usuario solo necesita su id de Supabase, que
    // ya guardamos en localStorage al iniciar sesion. Aqui lo traducimos al id interno
    // y reutilizamos la misma vista de mis reservas.
    @GetMapping("/mis-reservas")
    public String misReservasPorSupabase(@RequestParam(required = false) String supabaseUserId, Model model) {
        if (supabaseUserId == null || supabaseUserId.isBlank()) {
            // sin id no hay nada que listar; el JS de la vista mostrara el aviso de login
            model.addAttribute("reservas", new ArrayList<>());
            return "fragments/reservas/mis-reservas";
        }
        try {
            UUID supaId = UUID.fromString(supabaseUserId);
            Usuario usuario = usuarioRepository.findBySupabaseUserId(supaId).orElse(null);
            if (usuario == null) {
                model.addAttribute("reservas", new ArrayList<>());
            } else {
                model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(usuario.getidUsuario()));
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("reservas", new ArrayList<>());
        }
        return "fragments/reservas/mis-reservas";
    }

    // ruta tonta que coge la id y se la manda al servicio para que ejecute la cancelacion
    @GetMapping("/cancelar/{idReserva}")
    public String cancelar (@PathVariable Long idReserva){
        reservaService.cancelarReserva(idReserva);
        return "redirect:/reservas";
    }
}
