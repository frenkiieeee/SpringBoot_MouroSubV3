package com.mourosub.web.service;

import com.mourosub.web.model.Tickets;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.TicketsRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Tickets buscarPorId(Long id) {
        return ticketsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado: " + id));
    }

    public Tickets crearTicket(Long idUsuario, Tickets tickets) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        tickets.setUsuario(usuario);
        tickets.setEstado("Abierto");

        return ticketsRepository.save(tickets);
    }

    public Tickets actualizar(Tickets tickets) {
        Tickets ticketExistente = buscarPorId(tickets.getIdTicket());

        ticketExistente.setAsunto(tickets.getAsunto());
        ticketExistente.setDescripcion(tickets.getDescripcion());
        ticketExistente.setEstado(tickets.getEstado());
        ticketExistente.setPrioridad(tickets.getPrioridad());

        return ticketsRepository.save(ticketExistente);
    }

    public void eliminar(Long id) {
        ticketsRepository.deleteById(id);
    }
}

