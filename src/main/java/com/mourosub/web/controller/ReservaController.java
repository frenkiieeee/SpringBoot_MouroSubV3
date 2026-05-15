package com.mourosub.web.controller;

import com.mourosub.web.forms.ReservaForm;
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

    @GetMapping("/nueva")
    public String nueva (Model model){
    model.addAttribute("reservaForm", new ReservaForm());
    model.addAttribute("actividades", actividadRepository.findAll());
    model.addAttribute("usuario", usuarioRepository.findAll());

    return "reservas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar (@ModelAttribute ReservaForm reservaForm){
        Reserva reserva = new Reserva();

        reserva.setTipoServicio(reservaForm.getTipoServicio());
        reserva.setRefServicio(reservaForm.getRefServicio());
        reserva.setRefServicioId(reservaForm.getRefServicioId());
        reserva.setNumParticipantes(reservaForm.getNumParticipantes());
        reserva.setFechaActividad(reservaForm.getFechaActividad());


        Actividad actividad = new Actividad();

        actividad.setIdActividad (reservaForm.getIdActividad());
        reserva.setActividad(actividad);
        
        reservaService.crearReserva(reserva, reservaForm.getUsuarioIds());
        
        return "redirect:/reservas/mis-reservas/" + reservaForm.getUsuarioIds().get(0);
    }
    //Quitar el usuario de la reserva
    @GetMapping("/cancelar/{idUsuario}")
    public String misReservas(@PathVariable long idUsuario, Model model){
        model.addAttribute("reservas", reservaRepository.findByUsuarios_IdUsuario(idUsuario));

        return "reservas/mis-reservas";
    }
    //Cancelar reserva
    @GetMapping("/cancelar/{idReserva}")
    public String cancelar (@PathVariable Long idReserva){
        reservaService.cancelarReserva(idReserva);

        return "redirect:/reservas/nueva";
    }
}
