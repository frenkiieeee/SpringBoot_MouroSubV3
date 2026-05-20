package com.mourosub.web.controller;

import com.mourosub.web.model.TicketRespuesta;
import com.mourosub.web.model.Tickets;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.service.TicketRespuestaService;
import com.mourosub.web.service.TicketsService;
import com.mourosub.web.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/tickets")
public class TicketsController {

    private final TicketsService ticketsService;
    private final TicketRespuestaService respuestaService;
    private final UsuarioService usuarioService;

    public TicketsController(TicketsService ticketsService,
                             TicketRespuestaService respuestaService,
                             UsuarioService usuarioService) {
        this.ticketsService = ticketsService;
        this.respuestaService = respuestaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/mis-tickets")
    public String misTickets(@RequestParam("supabaseUserId") String supabaseUserIdStr, Model model) {
        UUID supabaseUserId = UUID.fromString(supabaseUserIdStr);
        List<Tickets> tickets = ticketsService.listarPorSupabaseId(supabaseUserId);
        model.addAttribute("tickets", tickets);
        model.addAttribute("supabaseUserId", supabaseUserIdStr);
        return "tickets/mis-tickets";
    }

    @GetMapping("/mis-tickets/nuevo")
    public String nuevoTicket(@RequestParam("supabaseUserId") String supabaseUserIdStr, Model model) {
        model.addAttribute("supabaseUserId", supabaseUserIdStr);
        return "tickets/nuevo";
    }

    @PostMapping("/mis-tickets/crear")
    public String crearTicket(@RequestParam("supabaseUserId") String supabaseUserIdStr,
                              @RequestParam("asunto") String asunto,
                              @RequestParam("descripcion") String descripcion) {
        UUID supabaseUserId = UUID.fromString(supabaseUserIdStr);
        ticketsService.crearTicket(supabaseUserId, asunto, descripcion);
        return "redirect:/tickets/mis-tickets?supabaseUserId=" + supabaseUserIdStr;
    }

    @GetMapping("/mis-tickets/{id}")
    public String miDetalle(@PathVariable Long id,
                            @RequestParam("supabaseUserId") String supabaseUserIdStr,
                            Model model) {
        Tickets ticket = ticketsService.buscarPorId(id);
        List<TicketRespuesta> respuestas = respuestaService.listarPorTicket(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("respuestas", respuestas);
        model.addAttribute("supabaseUserId", supabaseUserIdStr);
        return "tickets/mi-detalle";
    }

    @PostMapping("/mis-tickets/{id}/responder")
    public String responder(@PathVariable Long id,
                           @RequestParam("supabaseUserId") String supabaseUserIdStr,
                           @RequestParam("contenido") String contenido) {
        UUID supabaseUserId = UUID.fromString(supabaseUserIdStr);
        Usuario usuario = usuarioService.buscarPorSupabaseId(supabaseUserId);
        if (usuario != null) {
            respuestaService.crearRespuesta(id, usuario.getidUsuario(), contenido, false);
        }
        return "redirect:/tickets/mis-tickets/" + id + "?supabaseUserId=" + supabaseUserIdStr;
    }

    @GetMapping("/admin/tickets")
    public String listarAdmin(Model model) {
        model.addAttribute("tickets", ticketsService.listarTodos());
        model.addAttribute("estados", new String[]{"ABIERTO", "EN_PROCESO", "CERRADO"});
        return "tickets/lista";
    }

    @GetMapping("/admin/tickets/{id}")
    public String detalleAdmin(@PathVariable Long id, Model model) {
        Tickets ticket = ticketsService.buscarPorId(id);
        List<TicketRespuesta> respuestas = respuestaService.listarPorTicket(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("respuestas", respuestas);
        model.addAttribute("estados", new String[]{"ABIERTO", "EN_PROCESO", "CERRADO"});
        return "tickets/detalle";
    }

    @PostMapping("/admin/tickets/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam("estado") String estado) {
        ticketsService.cambiarEstado(id, estado);
        return "redirect:/tickets/admin/tickets/" + id;
    }

    @PostMapping("/admin/tickets/{id}/responder")
    public String responderAdmin(@PathVariable Long id,
                                  @RequestParam("contenido") String contenido) {
        Usuario admin = usuarioService.listarTodos().stream().findFirst().orElse(null);
        if (admin != null) {
            respuestaService.crearRespuesta(id, admin.getidUsuario(), contenido, true);
        }
        return "redirect:/tickets/admin/tickets/" + id;
    }

    @GetMapping("/admin/tickets/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ticketsService.eliminar(id);
        return "redirect:/tickets/admin/tickets";
    }
}