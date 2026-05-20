package com.mourosub.web.service;

import com.mourosub.web.model.TicketRespuesta;
import com.mourosub.web.model.Tickets;
import com.mourosub.web.model.Usuario;
import com.mourosub.web.repository.TicketRespuestaRepository;
import com.mourosub.web.repository.TicketsRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketRespuestaService {

    private final TicketRespuestaRepository respuestaRepository;
    private final TicketsRepository ticketsRepository;
    private final UsuarioRepository usuarioRepository;

    public TicketRespuestaService(TicketRespuestaRepository respuestaRepository,
                                   TicketsRepository ticketsRepository,
                                   UsuarioRepository usuarioRepository) {
        this.respuestaRepository = respuestaRepository;
        this.ticketsRepository = ticketsRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<TicketRespuesta> listarPorTicket(Long idTicket) {
        return respuestaRepository.findByTicketIdTicketOrderByFechaCreacionAsc(idTicket);
    }

    @Transactional
    public TicketRespuesta crearRespuesta(Long idTicket, Long idUsuario, String contenido, Boolean esAdmin) {
        Tickets ticket = ticketsRepository.findById(idTicket)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TicketRespuesta respuesta = new TicketRespuesta();
        respuesta.setTicket(ticket);
        respuesta.setUsuario(usuario);
        respuesta.setContenido(contenido);
        respuesta.setEsAdmin(esAdmin);

        return respuestaRepository.save(respuesta);
    }

    @Transactional
    public void eliminarRespuesta(Long idRespuesta) {
        respuestaRepository.deleteById(idRespuesta);
    }
}