package com.mourosub.web.controller;
import java.util.*;
import java.util.UUID;

import com.mourosub.web.exception.exceptions;
import com.mourosub.web.model.*;
import com.mourosub.web.dto.ReservaFormDTO;
import com.mourosub.web.model.Actividad;
import com.mourosub.web.model.Reserva;
import com.mourosub.web.repository.ActividadRepository;
import com.mourosub.web.repository.ReservaRepository;
import com.mourosub.web.repository.UsuarioRepository;
import com.mourosub.web.service.ReservaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador de las reservas: muestra los formularios de reserva y procesa su guardado.
@Controller
@RequestMapping("/reservas")

public class ReservaController {

    // Servicio con la logica de negocio de las reservas (crear, cancelar, calcular precio...).
    private final ReservaService reservaService;
    // Repositorios para acceder directamente a la base de datos cuando hace falta.
    private final ReservaRepository reservaRepository;
    private final ActividadRepository actividadRepository;
    private final UsuarioRepository usuarioRepository;

    // Spring inyecta el servicio y los tres repositorios por el constructor.
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

    // GET /reservas -> pagina inicial de reservas.
    @GetMapping
    public String index(Model model){
        // "seccion" le dice a la plantilla que parte mostrar; aqui, la de inicio.
        model.addAttribute("seccion","inicio");
        return "fragments/reservas/index";
    }

    // GET /reservas/cursos -> formulario de reserva para bautismos y cursos.
    @GetMapping("/cursos")
    public String cursos(Model model){
        // La plantilla mostrara el formulario.
        model.addAttribute("seccion","formulario");
        model.addAttribute("titulo", "Bautismo y cursos");
        // Objeto vacio (DTO) que recogera los datos del formulario.
        model.addAttribute("reservaForm", new ReservaFormDTO());
        // Solo las actividades de tipo BAUTISMO o CURSO, para el desplegable.
        model.addAttribute("actividades",actividadRepository.findByTipoIn(List.of("BAUTISMO", "CURSO")));
        // Lista de usuarios disponible para la vista.
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "fragments/reservas/index";
    }

    // GET /reservas/actividades -> formulario de reserva para actividades (inmersiones, snorkel, paseos).
    @GetMapping("/actividades")
    public String actividades(Model model){
        model.addAttribute("seccion","formulario");
        model.addAttribute("titulo", "Actividades");
        model.addAttribute("reservaForm", new ReservaFormDTO());
        // Solo las actividades de estos tres tipos.
        model.addAttribute("actividades", actividadRepository.findByTipoIn(List.of("INMERSION", "SNORKEL", "PASEO_BARCO")));
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "fragments/reservas/index";
    }

    // POST /reservas/guardar -> recibe el formulario, crea el usuario y la reserva.
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ReservaFormDTO reservaForm) {
    // 1) Creamos un usuario nuevo con los datos del formulario.
    Usuario usuario = new Usuario();
    // Le asignamos un id de Supabase aleatorio (este flujo no pasa por el login real).
    usuario.setSupabaseUserId(UUID.randomUUID());
    usuario.setNombre(reservaForm.getNombre());
    usuario.setApellidos(reservaForm.getApellidos());
    usuario.setEmail(reservaForm.getEmail());
    usuario.setDni(reservaForm.getDni());
    usuario.setTelefono(reservaForm.getTelefono());
    usuario.setDireccion(reservaForm.getDireccion());
    usuario.setCodPostal(reservaForm.getCodPostal());
    usuario.setLocalidad(reservaForm.getLocalidad());
    usuario.setFechaNacimiento(reservaForm.getFechaNacimiento());

    // Guardamos el usuario en la base de datos (asi obtiene su id).
    usuarioRepository.save(usuario);

    // 2) Creamos la reserva con el numero de participantes del formulario.
    Reserva reserva = new Reserva();
    reserva.setNumParticipantes(reservaForm.getNumParticipantes());

        // 3) Asociamos la actividad elegida (solo necesitamos su id; el servicio cargara el resto).
        Actividad actividad = actividadRepository.findById(reservaForm.getIdActividad())
                .orElseThrow(() -> new exceptions.ActividadNotFoundException("Actividad no encontrada"));
        reserva.setActividad(actividad);

        // 4) El servicio crea la reserva (comprueba plazas, asigna instructor, calcula precio...).
        reservaService.crearReserva(reserva, List.of(usuario.getidUsuario()));

        // Redirige a la pagina de reservas anyadiendo el id del usuario al final.
        return "redirect:/reservas/mis-reservas/" + usuario.getidUsuario();
    }
    //Quitar el usuario de la reserva
    // GET /reservas/mis-reservas/{idUsuario} -> lista las reservas de un usuario concreto.
    @GetMapping("/mis-reservas/{idUsuario}")
    public String misReservas(@PathVariable Long idUsuario, Model model){
        // Buscamos en la base de datos todas las reservas en las que aparece ese usuario.
        model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(idUsuario));

        return "reservas/mis-reservas";
    }
    //Cancelar reserva
    // GET /reservas/cancelar/{idReserva} -> cancela la reserva indicada.
    @GetMapping("/cancelar/{idReserva}")
    public String cancelar (@PathVariable Long idReserva){
        // El servicio aplica las reglas de cancelacion (plazo, liberar plazas, etc.).
        reservaService.cancelarReserva(idReserva);

        return "redirect:/reservas";
    }
}
