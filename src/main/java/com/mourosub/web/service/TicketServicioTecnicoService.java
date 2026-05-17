package com.mourosub.web.service;

import com.mourosub.web.model.TicketServicioTecnico;
import com.mourosub.web.repository.TicketServicioTecnicoRepository;
import com.mourosub.web.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// @Service: logica de negocio de los tickets de servicio tecnico.
@Service
public class TicketServicioTecnicoService {

    // Repositorio de tickets y repositorio de usuarios (para asociar el ticket a su usuario).
    private final TicketServicioTecnicoRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    // Spring inyecta los dos repositorios por el constructor.
    public TicketServicioTecnicoService(TicketServicioTecnicoRepository ticketRepository,
                                        UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Crea un ticket nuevo a partir de los datos del formulario.
    public TicketServicioTecnico crearTicket(TicketServicioTecnico ticket, String supabaseUserId) {
        // Comprobamos que los datos obligatorios esten completos.
        validarTicket(ticket);

        // Convertimos el id de texto a UUID y buscamos el usuario; si no existe, error.
        UUID supabaseId = parseSupabaseUserId(supabaseUserId);
        ticket.setUsuario(usuarioRepository.findBySupabaseUserId(supabaseId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado para supabaseUserId")));

        // Si no se indico categoria, ponemos "General" por defecto.
        if (isBlank(ticket.getCategoria())) {
            ticket.setCategoria("General");
        }

        // Si no viene estado, el ticket empieza como "NUEVO".
        if (isBlank(ticket.getEstado())) {
            ticket.setEstado("NUEVO");
        }

        // Si no viene fecha de creacion, usamos la fecha y hora actuales.
        if (ticket.getFechaCreacion() == null) {
            ticket.setFechaCreacion(LocalDateTime.now());
        }

        // Guardamos el ticket ya completo en la base de datos.
        return ticketRepository.save(ticket);
    }

    // Lista tickets: si no se pasa estado, devuelve todos; si se pasa, filtra por ese estado.
    public List<TicketServicioTecnico> listarTickets(String estado) {
        if (isBlank(estado)) {
            return ticketRepository.findAllByOrderByFechaCreacionDesc();
        }
        return ticketRepository.findByEstadoOrderByFechaCreacionDesc(estado);
    }

    // Busca un ticket por su id. Si no existe, lanza una excepcion.
    public TicketServicioTecnico buscarPorId(Long id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado: " + id));
    }

    // Cambia el estado de un ticket existente.
    public TicketServicioTecnico cambiarEstado(Long id, String nuevoEstado) {
        // El estado no puede venir vacio.
        if (isBlank(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no valido");
        }
        TicketServicioTecnico ticket = buscarPorId(id);
        ticket.setEstado(nuevoEstado);
        return ticketRepository.save(ticket);
    }

    // Validacion: comprueba que los campos obligatorios del ticket esten rellenos.
    private void validarTicket(TicketServicioTecnico ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket invalido");
        }
        if (isBlank(ticket.getNombre())) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }
        if (isBlank(ticket.getEmail())) {
            throw new IllegalArgumentException("Email obligatorio");
        }
        if (isBlank(ticket.getDescripcionProblema())) {
            throw new IllegalArgumentException("Descripcion obligatoria");
        }
    }

    // Metodo auxiliar: devuelve true si el texto es null o esta vacio (solo espacios).
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Convierte el id de usuario (texto) a UUID. Si esta vacio o mal formado, lanza error.
    private UUID parseSupabaseUserId(String supabaseUserId) {
        if (isBlank(supabaseUserId)) {
            throw new IllegalArgumentException("Necesitas iniciar sesion");
        }
        try {
            return UUID.fromString(supabaseUserId.trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("supabaseUserId invalido");
        }
    }
}
