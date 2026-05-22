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

// marcamos la clase como servicio para que spring sepa que aqui van las reglas de negocio
@Service
public class ReservaService {

    // inyectamos todos los repositorios que nos van a hacer falta para cruzar datos
    private final ReservaRepository reservaRepo;
    private final ActividadRepository actividadRepo;
    private final UsuarioRepository usuarioRepo;
    private final InstructorRepository instructorRepo;

    // constructor para que spring meta los repositorios automaticamente
    public ReservaService(ReservaRepository reservaRepo,
                          ActividadRepository actividadRepo,
                          UsuarioRepository usuarioRepo,
                          InstructorRepository instructorRepo) {
        this.reservaRepo = reservaRepo;
        this.actividadRepo = actividadRepo;
        this.usuarioRepo = usuarioRepo;
        this.instructorRepo = instructorRepo;
    }

    // transactional asegura que si algo peta a medias no se guarde nada roto en la base de datos
    @Transactional
    public Reserva crearReserva(Reserva reserva, List<Long> usuarioIds) {
        // pillamos la actividad de la bd y si no existe reventamos con una excepcion
        Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
            .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        // rellenamos los datos de la reserva copiandolos de la actividad
        reserva.setActividad(actividad);
        reserva.setTipoServicio(actividad.getTipo());
        reserva.setRefServicio(actividad.getNombre());
        reserva.setRefServicioId(String.valueOf(actividad.getIdActividad()));
        reserva.setFechaActividad(actividad.getFechaActividad());

        // pasamos los filtros de seguridad basicos y los de tipo de actividad
        validarReservaBasica(reserva, actividad);
        validarReglasTipoActividad(actividad);

        // comprobamos que haya hueco para esa cantidad de gente
        if (!comprobarDisponibilidad(actividad, reserva.getNumParticipantes())) {
            throw new RuntimeException("No hay plazas disponibles");
        }

        // buscamos a un instructor que no tenga curro ese dia y se lo asignamos
        Instructor instructor = asignarInstructorDisponible(actividad);
        if (reserva.getInstructores() == null) {
            reserva.setInstructores(new ArrayList<>());
        }
        reserva.getInstructores().add(instructor);

        // buscamos los usuarios por sus ids y los metemos en la reserva
        List<Usuario> usuarios = usuarioRepo.findAllById(usuarioIds);
        if (usuarios.size() != usuarioIds.size()) {
            throw new RuntimeException("Uno o mas usuarios no se encontraron");
        }
        reserva.setUsuarios(usuarios);

        // calculamos la pasta multiplicando el precio base por los participantes
        reserva.setPrecioTotal(calcularPrecio(actividad, reserva.getNumParticipantes()));
        reserva.setEstado("ACTIVA");
        reserva.setFechaReserva(LocalDate.now());

        // sumamos los participantes a las plazas ocupadas y guardamos la actividad actualizada
        actividad.setplazasOcupadas(actividad.getplazasOcupadas() + reserva.getNumParticipantes());
        actividadRepo.save(actividad);

        // guardamos la reserva completa en la bd
        return reservaRepo.save(reserva);
    }

    // metodo para cancelar una reserva que tambien esta blindado con transactional
    @Transactional
    public void cancelarReserva(Long idReserva) {
        // buscamos la reserva
        Reserva reserva = reservaRepo.findById(idReserva)
            .orElseThrow(() -> new ReservaNotFoundException("Reserva no encontrada con id: " + idReserva));

        // si ya estaba cancelada no hacemos nada para evitar duplicar acciones
        if ("CANCELADA".equalsIgnoreCase(reserva.getEstado())) {
            throw new ReservaYaCanceladaException("Esta reserva esta cancelada");
        }

        // seguro anti despistes por si la reserva perdio su conexion con la actividad
        if (reserva.getActividad() == null) {
            throw new RuntimeException("La reserva no tiene actividad asociada");
        }

        // regla de negocio vital para que no cancelen con menos de 24 horas de margen
        LocalDate fechaActividad = reserva.getActividad().getFechaActividad();
        if (fechaActividad.isBefore(LocalDate.now().plusDays(1))) {
            throw new FueraDePlazoException("No se puede cancelar la reserva con menos de 24h de antelacion");
        }

        // cambiamos el estado y borramos a los instructores para liberarles la agenda
        reserva.setEstado("CANCELADA");
        reserva.getInstructores().clear();

        // restamos a la gente de las plazas ocupadas asegurandonos de no bajar de cero
        Actividad actividad = reserva.getActividad();
        int plazasLiberadas = reserva.getNumParticipantes();
        actividad.setplazasOcupadas(Math.max(0, actividad.getplazasOcupadas() - plazasLiberadas));
        
        // guardamos actividad y reserva actualizadas
        actividadRepo.save(actividad);
        reservaRepo.save(reserva);
    }

    // funcion simple que resta el maximo menos las ocupadas a ver si caben los nuevos
    public boolean comprobarDisponibilidad(Actividad actividad, int participantes) {
        return actividad.getPlazasMax() - actividad.getplazasOcupadas() >= participantes;
    }

    // pilla a todos los instructores y filtra al primero que este libre ese dia
    public Instructor asignarInstructorDisponible(Actividad actividad) {
        List<Instructor> instructores = instructorRepo.findAll();

        return instructores.stream()
            .filter(instructor -> estaDisponible(instructor, actividad.getFechaActividad()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No hay instructores disponibles"));
    }

    // comprueba que el instructor no tenga ninguna reserva activa para esa misma fecha
    private boolean estaDisponible(Instructor instructor, LocalDate fechaActividad) {
        return instructor.getReservas().stream()
            .noneMatch(reserva ->
                !"CANCELADA".equalsIgnoreCase(reserva.getEstado())
                    && fechaActividad.equals(reserva.getFechaActividad())
            );
    }

    // multiplica el precio unitario por las personas usando bigdecimal para no perder precision
    public BigDecimal calcularPrecio(Actividad actividad, int participantes) {
        return actividad.getPrecio().multiply(BigDecimal.valueOf(participantes));
    }

    // comprobaciones basicas de que todo este en orden antes de intentar guardar nada
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

    // logica extra preparada por si la actividad exige tener un nivel de buceo especifico
    private void validarReglasTipoActividad(Actividad actividad) {
        if (actividad instanceof Inmersiones inmersion) {
            if (inmersion.getNivelReq() != null && !inmersion.getNivelReq().isBlank()) {
                // logica que se hara por otro lado
            }
        }
    }
}