package com.mourosub.web.controller;

import com.mourosub.web.model.Tickets;
import com.mourosub.web.service.TicketsService;
import com.mourosub.web.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketsController {

    private final TicketsService ticketsService;
    private final UsuarioService usuarioService;

    public TicketsController(TicketsService ticketsService, UsuarioService usuarioService) {
        this.ticketsService = ticketsService;
        this.usuarioService = usuarioService;
    }

    // Admin: ver todos los tickets
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tickets", ticketsService.listarTodos());
        return "tickets/lista";
    }

    // Usuario: ver sus tickets
    @GetMapping("/usuario/{idUsuario}")
    public String listarPorUsuario(@PathVariable Long idUsuario, Model model) {
        model.addAttribute("tickets", ticketsService.listarPorUsuario(idUsuario));
        model.addAttribute("usuario", usuarioService.buscarPorId(idUsuario));
        return "tickets/lista";
    }

    // Usuario: formulario para crear ticket
    @GetMapping("/nuevo/usuario/{idUsuario}")
    public String nuevoDesdeUsuario(@PathVariable Long idUsuario, Model model) {
        Tickets tickets = new Tickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("usuario", usuarioService.buscarPorId(idUsuario));
        return "tickets/formulario";
    }

    // Usuario: guardar ticket nuevo
    @PostMapping("/guardar/usuario/{idUsuario}")
    public String guardarDesdeUsuario(@PathVariable Long idUsuario, @ModelAttribute Tickets tickets) {
        ticketsService.crearTicket(idUsuario, tickets);
        return "redirect:/tickets/usuario/" + idUsuario;
    }

    // Admin: editar ticket
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("tickets", ticketsService.buscarPorId(id));
        return "tickets/formulario-admin";
    }

    // Admin: guardar cambios del ticket
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Tickets tickets) {
        ticketsService.actualizar(tickets);
        return "redirect:/tickets";
    }

    // Admin: eliminar ticket
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ticketsService.eliminar(id);
        return "redirect:/tickets";
    }
}