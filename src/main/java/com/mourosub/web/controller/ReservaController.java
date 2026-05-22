package com.mourosub.web.controller;
import java.util.*;
import com.mourosub.web.exception.exceptions;
import com.mourosub.web.exception.exceptions.ActividadNotFoundException;
import com.mourosub.web.model.*;
import com.mourosub.web.dto.ReservaFormDTO;
import com.mourosub.web.repository.ActividadRepository;
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
    private final UsuarioRepository usuarioRepository;

    public ReservaController(
        ReservaService reservaService,
        ReservaRepository reservaRepository,
        ActividadRepository actividadRepository,
        UsuarioRepository usuarioRepository
    ){
        this.reservaService = reservaService;
        this.reservaRepository = reservaRepository;
        this.actividadRepository = actividadRepository;
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
        // La plantilla mostrara el formulario.
        model.addAttribute("seccion","cursos");
        model.addAttribute("titulo", "Cursos");
        // Objeto vacio (DTO) que recogera los datos del formulario.
        model.addAttribute("reserva", new Reserva());
        // Solo las actividades de tipo CURSO, para el desplegable.
        model.addAttribute("cursos",actividadRepository.findByTipoIn(List.of( "CURSO")));
        // Lista de usuarios disponible para la vista.

        return "fragments/reservas/index";
    }

    // prepara el formulario filtrando lo puramente de ocio como inmersiones o paseos
    @GetMapping("/actividades")
    public String actividades(Model model){
        model.addAttribute("seccion","actividades");
        model.addAttribute("titulo", "Actividades");
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("actividades", actividadRepository.findByTipoIn(List.of("INMERSION", "SNORKEL", "PASEO_BARCO")));
        return "fragments/reservas/index";
    }

    // aqui llega el formulario relleno con metodo post para que lo guardemos
    @PostMapping("/guardar")
    public String guardar(
        @RequestParam Long idActividad,
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

        // buscamos la actividad y montamos el esqueleto de la reserva
        Actividad actividad = actividadRepository.findById(idActividad)
            .orElseThrow(() -> new ActividadNotFoundException("Actividad no encontrada"));
            
        Reserva reserva = new Reserva ();
        reserva.setActividad(actividad);
        reserva.setNumParticipantes(numParticipantes);

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
