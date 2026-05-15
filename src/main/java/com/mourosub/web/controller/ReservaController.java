package com.mourosub.web.controller;
import java.util.*;
import java.util.UUID;
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

@Controller
@RequestMapping("/reservas")
    
public class ReservaController {

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

    @GetMapping
    public String index(Model model){
        model.addAttribute("seccion","inicio");
        return "Fragments/reservas/index";
    }

    @GetMapping("/cursos")
    public String cursos(Model model){
        model.addAttribute("seccion","formulario");
        model.addAttribute("titulo", "Bautismo y cursos");
        model.addAttribute("reservaForm", new ReservaFormDTO());
        model.addAttribute("actividades",actividadRepository.findByTipoIn(List.of("BAUTISMO", "CURSO")));
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "Fragments/reservas/index";
    }

    @GetMapping("/actividades")
    public String actividades(Model model){
        model.addAttribute("seccion","formulario");
        model.addAttribute("titulo", "Actividades");
        model.addAttribute("reservaForm", new ReservaFormDTO());
        model.addAttribute("actividades", actividadRepository.findByTipoIn(List.of("INMERSION", "SNORKEL", "PASEO_BARCO")));
        model.addAttribute("usuarios", usuarioRepository.findAll());

        return "Fragments/reservas/index";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ReservaFormDTO reservaForm) {
    Usuario usuario = new Usuario();
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

    usuarioRepository.save(usuario);

    Reserva reserva = new Reserva();
    reserva.setNumParticipantes(reservaForm.getNumParticipantes());
    
        Actividad actividad = new Actividad();
        actividad.setIdActividad (reservaForm.getIdActividad());
        reserva.setActividad(actividad);
        
        reservaService.crearReserva(reserva, List.of(usuario.getidUsuario()));
        
        return "redirect:/reservas" + usuario.getidUsuario();
    }
    //Quitar el usuario de la reserva
    @GetMapping("/mis-reservas/{idUsuario}")
    public String misReservas(@PathVariable Long idUsuario, Model model){
        model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(idUsuario));

        return "reservas/mis-reservas";
    }
    //Cancelar reserva
    @GetMapping("/cancelar/{idReserva}")
    public String cancelar (@PathVariable Long idReserva){
        reservaService.cancelarReserva(idReserva);

        return "redirect:/reservas";
    }
}
