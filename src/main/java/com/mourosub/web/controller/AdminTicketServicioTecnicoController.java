package com.mourosub.web.controller;

import com.mourosub.web.service.TicketServicioTecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controlador del panel admin para gestionar los tickets de servicio tecnico
// (las solicitudes que envian los clientes). Permite listarlos, ver el detalle
// y cambiarles el estado.
@Controller
@RequestMapping("/admin/tickets/servicio-tecnico")
public class AdminTicketServicioTecnicoController {

    // Servicio con la logica de negocio de los tickets
    private final TicketServicioTecnicoService ticketServicioTecnicoService;

    // Spring inyecta el servicio por el constructor
    public AdminTicketServicioTecnicoController(TicketServicioTecnicoService ticketServicioTecnicoService) {
        this.ticketServicioTecnicoService = ticketServicioTecnicoService;
    }

    // GET /admin/tickets/servicio-tecnico -> lista de tickets, con filtro opcional por estado
    @GetMapping
    public String listar(@RequestParam(value = "estado", required = false) String estado, Model model) {
        // 'estado' viene de la URL (?estado=...). required=false -> puede no venir; entonces se listan todos.
        model.addAttribute("tickets", ticketServicioTecnicoService.listarTickets(estado));
        // Guardamos el estado seleccionado para que el desplegable del filtro lo recuerde.
        model.addAttribute("estado", estado);
        // Lista de estados posibles, para rellenar el desplegable del filtro en la vista.
        model.addAttribute("estados", new String[]{"NUEVO", "EN_PROCESO", "RESUELTO", "CANCELADO"});
        return "admin/tickets/servicio-tecnico/lista";
    }

    // GET /admin/tickets/servicio-tecnico/{id} -> detalle de un ticket concreto.
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        // Buscamos el ticket por su id y lo mandamos a la vista.
        model.addAttribute("ticket", ticketServicioTecnicoService.buscarPorId(id));
        // Estados posibles, para el desplegable que permite cambiar el estado.
        model.addAttribute("estados", new String[]{"NUEVO", "EN_PROCESO", "RESUELTO", "CANCELADO"});
        return "admin/tickets/servicio-tecnico/detalle";
    }

    // POST /admin/tickets/servicio-tecnico/{id}/estado -> cambia el estado de un ticket.
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam("estado") String estado) {
        // El id viene de la URL y el nuevo estado del formulario.
        ticketServicioTecnicoService.cambiarEstado(id, estado);
        // Volvemos al detalle del mismo ticket para ver el cambio aplicado.
        return "redirect:/admin/tickets/servicio-tecnico/" + id;
    }
}
