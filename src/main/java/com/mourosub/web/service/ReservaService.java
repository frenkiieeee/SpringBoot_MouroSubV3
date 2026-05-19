package com.mourosub.web.service;

import com.mourosub.web.exception.exceptions.*;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepo;
    private final ActividadRepository actividadRepo;
    private final UsuarioRepository usuarioRepo;
    private final InstructorRepository instructorRepo;

    public ReservaService(ReservaRepository reservaRepo,
                          ActividadRepository actividadRepo,
                          UsuarioRepository usuarioRepo,
                          InstructorRepository instructorRepo) {
        this.reservaRepo = reservaRepo;
        this.actividadRepo = actividadRepo;
        this.usuarioRepo = usuarioRepo;
        this.instructorRepo = instructorRepo;
    }

    @Transactional
    public Reserva crearReserva(Reserva reserva, List<Long> usuarioIds) {
        Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
            .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        reserva.setActividad(actividad);
        reserva.setTipoServicio(actividad.getTipo());
        reserva.setRefServicio(actividad.getNombre());
        reserva.setRefServicioId(String.valueOf(actividad.getIdActividad()));
        reserva.setFechaActividad(actividad.getFechaActividad());

        validarReservaBasica(reserva, actividad);
        validarReglasTipoActividad(actividad);

        if (!comprobarDisponibilidad(actividad, reserva.getNumParticipantes())) {
            throw new RuntimeException("No hay plazas disponibles");
        }

        Instructor instructor = asignarInstructorDisponible(actividad);
        if (reserva.getInstructores() == null) {
            reserva.setInstructores(new ArrayList<>());
        }
        reserva.getInstructores().add(instructor);

        List<Usuario> usuarios = usuarioRepo.findAllById(usuarioIds);
        if (usuarios.size() != usuarioIds.size()) {
            throw new RuntimeException("Uno o mas usuarios no se encontraron");
        }
        reserva.setUsuarios(usuarios);

        reserva.setPrecioTotal(calcularPrecio(actividad, reserva.getNumParticipantes()));
        reserva.setEstado("ACTIVA");
        reserva.setFechaReserva(LocalDate.now());

        actividad.setplazasOcupadas(actividad.getplazasOcupadas() + reserva.getNumParticipantes());
        actividadRepo.save(actividad);

        return reservaRepo.save(reserva);
    }

    @Transactional
    public void cancelarReserva(Long idReserva) {
        Reserva reserva = reservaRepo.findById(idReserva)
            .orElseThrow(() -> new ReservaNotFoundException("Reserva no encontrada con id: " + idReserva));

        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new ReservaYaCanceladaException("Esta reserva esta cancelada");
        }

        if (reserva.getActividad() == null) {
            throw new RuntimeException("La reserva no tiene actividad asociada");
        }

        LocalDate fechaActividad = reserva.getActividad().getFechaActividad();
        if (fechaActividad.isBefore(LocalDate.now().plusDays(1))) {
            throw new FueraDePlazoException("No se puede cancelar la reserva con menos de 24h de antelacion");
        }

        reserva.setEstado("CANCELADA");
        reserva.getInstructores().clear();

        Actividad actividad = reserva.getActividad();
        int plazasLiberadas = reserva.getNumParticipantes();
        actividad.setplazasOcupadas(Math.max(0, actividad.getplazasOcupadas() - plazasLiberadas));
        actividadRepo.save(actividad);

        reservaRepo.save(reserva);
    }

    public boolean comprobarDisponibilidad(Actividad actividad, int participantes) {
        return actividad.getPlazasMax() - actividad.getplazasOcupadas() >= participantes;
    }

    public Instructor asignarInstructorDisponible(Actividad actividad) {
        List<Instructor> instructores = instructorRepo.findAll();

        return instructores.stream()
            .filter(instructor -> estaDisponible(instructor, actividad.getFechaActividad()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No hay instructores disponibles"));
    }

    private boolean estaDisponible(Instructor instructor, LocalDate fechaActividad) {
        return instructor.getReservas().stream()
            .noneMatch(reserva ->
                !"CANCELADA".equalsIgnoreCase(reserva.getEstado())
                    && fechaActividad.equals(reserva.getFechaActividad())
            );
    }

    public BigDecimal calcularPrecio(Actividad actividad, int participantes) {
        return actividad.getPrecio().multiply(BigDecimal.valueOf(participantes));
    }

    private void validarReservaBasica(Reserva reserva, Actividad actividad) {
        if (!Boolean.TRUE.equals(actividad.getActivo())) {
            throw new RuntimeException("La actividad no esta activa");
        }

        if (actividad.getFechaActividad().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede reservar una actividad pasada");
        }

        if (reserva.getNumParticipantes() == null || reserva.getNumParticipantes() <= 0) {
            throw new RuntimeException("El numero de participantes debe ser mayor que cero");
        }
    }

    private void validarReglasTipoActividad(Actividad actividad) {
        if (actividad instanceof Inmersiones inmersion) {
            if (inmersion.getNivelReq() != null && !inmersion.getNivelReq().isBlank()) {
                // Aqui queda preparada la logica especifica de inmersiones.
                // La validacion real del nivel se hara por otro lado.
            }
        }
    }
}
