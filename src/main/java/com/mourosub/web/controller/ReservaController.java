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

    // ruta base que solo carga el fragmento inicial de la pagina
    @GetMapping
    public String index(Model model){
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

    // GET /reservas/cursos -> formulario de reserva para cursos.
    @GetMapping("/cursos")
    public String cursos(Model model){
        List<Curso> todosLosCursos = cursoRepository.findAllActive();
        
        Map<String, List<Curso>> cursosPorCategoria = new LinkedHashMap<>();
        for (Curso curso : todosLosCursos) {
            String cat = curso.getCategoria() != null ? curso.getCategoria() : "OTROS";
            cursosPorCategoria.computeIfAbsent(cat, k -> new ArrayList<>()).add(curso);
        }
        
        List<String> categorias = new ArrayList<>(cursosPorCategoria.keySet());

        model.addAttribute("seccion","cursos");
        model.addAttribute("titulo", "Cursos");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("cursosPorCategoria", cursosPorCategoria);
        model.addAttribute("categorias", categorias);

        return "fragments/reservas/index";
    }

    // prepara el formulario filtrando lo puramente de ocio como inmersiones o paseos
    @GetMapping("/actividades")
    public String actividades(Model model){
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
        return "fragments/reservas/index";
    }

    // formulario de reserva para inmersiones
    @GetMapping("/inmersiones")
    public String inmersiones(Model model){
        model.addAttribute("seccion","inmersiones");
        model.addAttribute("titulo", "Inmersiones");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("inmersiones", inmersionRepository.findByActivoTrue());
        return "fragments/reservas/index";
    }

    // aqui llega el formulario relleno con metodo post para que lo guardemos
    @PostMapping("/guardar")
    public String guardar(
        @RequestParam(required = false) Long idActividad,
        @RequestParam(required = false) Long idCurso,
        @RequestParam(required = false) Long idInmersion,
        @RequestParam String nombre,
        @RequestParam String apellidos,
        @RequestParam String email,
        @RequestParam String telefono,
        @RequestParam (required = false) String dni,
        @RequestParam String codigoPostal,
        @RequestParam Integer numParticipantes
    ) {
        // creamos un usuario sobre la marcha generandole un uuid falso porque no pasa por el login oficial
        Usuario usuario = new Usuario();
        usuario.setSupabaseUserId(UUID.randomUUID());
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setDni(dni);
        usuario.setCodPostal(codigoPostal);
        usuarioRepository.save(usuario);

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

    // lista el historial de reservas de un usuario buscando por su id
    @GetMapping("/mis-reservas/{idUsuario}")
    public String misReservas(@PathVariable Long idUsuario, Model model){
        model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(idUsuario));
        return "Fragments/reservas/mis-reservas";
    }

    // ruta tonta que coge la id y se la manda al servicio para que ejecute la cancelacion
    @GetMapping("/cancelar/{idReserva}")
    public String cancelar (@PathVariable Long idReserva){
        reservaService.cancelarReserva(idReserva);
        return "redirect:/reservas";
    }
}
