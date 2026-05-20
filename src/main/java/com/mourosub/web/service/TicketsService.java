package com.mourosub.web.service;

import com.mourosub.web.model.Tickets;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.TicketsRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketsService {

    private final TicketsRepository ticketsRepository;
    private final UsuarioRepository usuarioRepository;

    public TicketsService(TicketsRepository ticketsRepository, UsuarioRepository usuarioRepository) {
        this.ticketsRepository = ticketsRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Tickets> listarTodos() {
        return ticketsRepository.findAll();
    }

    public List<Tickets> listarPorUsuario(Long idUsuario) {
        return ticketsRepository.findByUsuario_IdUsuario(idUsuario);
    }

    public List<Tickets> listarPorSupabaseId(UUID supabaseUserId) {
        Usuario usuario = usuarioRepository.findBySupabaseUserId(supabaseUserId).orElse(null);
        if (usuario == null) {
            return List.of();
        }
        return ticketsRepository.findByUsuario_IdUsuario(usuario.getidUsuario());
    }

    public Tickets buscarPorId(Long id) {
        return ticketsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado: " + id));
    }

    public Tickets crearTicket(Long idUsuario, Tickets tickets) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        tickets.setUsuario(usuario);
        tickets.setEstado("ABIERTO");

        return ticketsRepository.save(tickets);
    }

    public Tickets crearTicket(Long idUsuario, String asunto, String descripcion) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        Tickets ticket = new Tickets();
        ticket.setAsunto(asunto);
        ticket.setDescripcion(descripcion);
        ticket.setUsuario(usuario);
        ticket.setEstado("ABIERTO");

        return ticketsRepository.save(ticket);
    }

    public Tickets crearTicket(UUID supabaseUserId, String asunto, String descripcion) {
        Usuario usuario = usuarioRepository.findBySupabaseUserId(supabaseUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Tickets ticket = new Tickets();
        ticket.setAsunto(asunto);
        ticket.setDescripcion(descripcion);
        ticket.setUsuario(usuario);
        ticket.setEstado("ABIERTO");

        return ticketsRepository.save(ticket);
    }

    public Tickets actualizar(Tickets tickets) {
        Tickets ticketExistente = buscarPorId(tickets.getIdTicket());

        ticketExistente.setAsunto(tickets.getAsunto());
        ticketExistente.setDescripcion(tickets.getDescripcion());
        ticketExistente.setEstado(tickets.getEstado());

        return ticketsRepository.save(ticketExistente);
    }

    public Tickets cambiarEstado(Long id, String estado) {
        Tickets ticket = buscarPorId(id);
        ticket.setEstado(estado);
        return ticketsRepository.save(ticket);
    }

    public void eliminar(Long id) {
        ticketsRepository.deleteById(id);
    }
}

