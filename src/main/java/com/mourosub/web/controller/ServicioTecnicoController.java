package com.mourosub.web.controller;

import com.mourosub.web.model.TicketServicioTecnico;
import com.mourosub.web.service.ServicioTecnicoService;
import com.mourosub.web.service.TicketServicioTecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador de la parte PUBLICA del servicio tecnico (lo que ve el cliente vamos)
// la pagina con el catalogo de servicios y el formulario para enviar una solicitud.
@Controller
@RequestMapping("/servicios/tecnico")
public class ServicioTecnicoController {

    // Servicio para leer el catalogo de servicios tecnicos.
    private final ServicioTecnicoService servicioTecnicoService;
    // Servicio para crear los tickets (las solicitudes) de servicio tecnico.
    private final TicketServicioTecnicoService ticketServicioTecnicoService;

    // Spring inyecta los dos servicios por el constructor.
    public ServicioTecnicoController(ServicioTecnicoService servicioTecnicoService,
            TicketServicioTecnicoService ticketServicioTecnicoService) {
        this.servicioTecnicoService = servicioTecnicoService;
        this.ticketServicioTecnicoService = ticketServicioTecnicoService;
    }

    // GET /servicios/tecnico -> muestra el catalogo y el formulario de solicitud.
    @GetMapping
    public String verServicios(Model model,
            // ?ok aparece tras enviar una solicitud con exito (parametro opcional).
            @RequestParam(value = "ok", required = false) String ok,
            // id del usuario logueado en Supabase (opcional, llega desde el frontend).
            @RequestParam(value = "supabaseUserId", required = false) String supabaseUserId) {
        // Solo los servicios activos: son los que el cliente puede solicitar.
        model.addAttribute("servicios", servicioTecnicoService.listarActivos());
        // Objeto ticket vacio para que el formulario lo vaya rellenando.
        model.addAttribute("ticket", new TicketServicioTecnico());
        // 'ok' sera true si el parametro vino en la URL (para mostrar el mensaje de
        // exito).
        model.addAttribute("ok", ok != null);
        // Pasamos el id de usuario a la vista para meterlo en el formulario.
        model.addAttribute("supabaseUserId", supabaseUserId);
        return "servicios/tecnico";
    }

    // POST /servicios/tecnico/solicitud -> recibe el formulario y crea el ticket.
    @PostMapping("/solicitud")
    public String crearSolicitud(@ModelAttribute("ticket") TicketServicioTecnico ticket,
            @RequestParam(value = "supabaseUserId", required = false) String supabaseUserId,
            Model model) {
        try {
            // Intentamos crear el ticket; el servicio valida los datos y lo guarda.
            ticketServicioTecnicoService.crearTicket(ticket, supabaseUserId);
            // Si fue bien, redirigimos con ?ok para mostrar el mensaje de exito.
            return "redirect:/servicios/tecnico?ok";
        } catch (IllegalArgumentException ex) {
            // Si el servicio lanza un error de validacion, volvemos a mostrar la pagina
            // con el mensaje de error, sin perder lo que escribio el usuario.
            model.addAttribute("servicios", servicioTecnicoService.listarActivos());
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("supabaseUserId", supabaseUserId);
            return "servicios/tecnico";
        }
    }
}
