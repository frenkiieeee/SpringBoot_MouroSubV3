package com.mourosub.web.controller;

import com.mourosub.web.model.TicketRespuesta;
import com.mourosub.web.model.Tickets;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.service.TicketRespuestaService;
import com.mourosub.web.service.TicketsService;
import com.mourosub.web.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketRespuestaController {

    private final TicketRespuestaService respuestaService;
    private final TicketsService ticketsService;
    private final UsuarioService usuarioService;

    public TicketRespuestaController(TicketRespuestaService respuestaService,
                                       TicketsService ticketsService,
                                       UsuarioService usuarioService) {
        this.respuestaService = respuestaService;
        this.ticketsService = ticketsService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}/respuestas")
    public ResponseEntity<List<TicketRespuesta>> listarRespuestas(@PathVariable Long id) {
        List<TicketRespuesta> respuestas = respuestaService.listarPorTicket(id);
        return ResponseEntity.ok(respuestas);
    }

    @PostMapping("/{id}/respuestas")
    public ResponseEntity<TicketRespuesta> crearRespuesta(@PathVariable Long id,
                                                             @RequestBody Map<String, Object> body) {
        String contenido = (String) body.get("contenido");
        String supabaseUserId = (String) body.get("supabaseUserId");
        Boolean esAdmin = body.containsKey("esAdmin") ? (Boolean) body.get("esAdmin") : false;

        if (contenido == null || contenido.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (supabaseUserId == null) {
            return ResponseEntity.status(401).build();
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioService.buscarPorSupabaseId(uuid);
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        TicketRespuesta respuesta = respuestaService.crearRespuesta(id, usuario.getidUsuario(), contenido, esAdmin);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Tickets> cambiarEstado(@PathVariable Long id,
                                                   @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null || estado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Tickets ticket = ticketsService.buscarPorId(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        ticket.setEstado(estado);
        ticketsService.actualizar(ticket);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<Tickets> crearTicket(@RequestBody Map<String, Object> body) {
        String asunto = (String) body.get("asunto");
        String descripcion = (String) body.get("descripcion");
        String supabaseUserId = (String) body.get("supabaseUserId");

        if (asunto == null || asunto.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (supabaseUserId == null) {
            return ResponseEntity.status(401).build();
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(supabaseUserId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioService.buscarPorSupabaseId(uuid);
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        Tickets ticket = ticketsService.crearTicket(usuario.getidUsuario(), asunto, descripcion);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tickets> obtenerTicket(@PathVariable Long id) {
        Tickets ticket = ticketsService.buscarPorId(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticket);
    }
}