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
    private final CursoRepository cursoRepo;
    private final InmersionRepository inmersionRepo;

    // constructor para que spring meta los repositorios automaticamente
    public ReservaService(ReservaRepository reservaRepo,
                          ActividadRepository actividadRepo,
                          UsuarioRepository usuarioRepo,
                          InstructorRepository instructorRepo,
                          CursoRepository cursoRepo,
                          InmersionRepository inmersionRepo) {
        this.reservaRepo = reservaRepo;
        this.actividadRepo = actividadRepo;
        this.usuarioRepo = usuarioRepo;
        this.instructorRepo = instructorRepo;
        this.cursoRepo = cursoRepo;
        this.inmersionRepo = inmersionRepo;
    }

    // transactional asegura que si algo peta a medias no se guarde nada roto en la base de datos
    @Transactional
    public Reserva crearReserva(Reserva reserva, List<Long> usuarioIds) {
        Object servicio = null;
        String tipo = null;

        if (reserva.getActividad() != null) {
            Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
            reserva.setActividad(actividad);
            reserva.setTipoServicio(actividad.getTipo());
            reserva.setRefServicio(actividad.getNombre());
            reserva.setRefServicioId(String.valueOf(actividad.getIdActividad()));
            reserva.setFechaActividad(actividad.getFechaActividad());
            validarReservaBasica(reserva, actividad);
            if (!comprobarDisponibilidad(actividad, reserva.getNumParticipantes())) {
                throw new RuntimeException("No hay plazas disponibles");
            }
            servicio = actividad;
        } else if (reserva.getCurso() != null) {
            Curso curso = cursoRepo.findById(reserva.getCurso().getIdCurso())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            reserva.setCurso(curso);
            reserva.setTipoServicio("CURSO");
            reserva.setRefServicio(curso.getNombre());
            reserva.setRefServicioId(String.valueOf(curso.getIdCurso()));
            reserva.setFechaActividad(curso.getFechaActividad());
            if (!comprobarDisponibilidadCurso(curso, reserva.getNumParticipantes())) {
                throw new RuntimeException("No hay plazas disponibles");
            }
            servicio = curso;
        } else if (reserva.getInmersion() != null) {
            Inmersiones inmersion = inmersionRepo.findById(reserva.getInmersion().getIdInmersion())
                .orElseThrow(() -> new RuntimeException("Inmersion no encontrada"));
            reserva.setInmersion(inmersion);
            reserva.setTipoServicio("INMERSION");
            reserva.setRefServicio(inmersion.getNombre());
            reserva.setRefServicioId(String.valueOf(inmersion.getIdInmersion()));
            reserva.setFechaActividad(inmersion.getFechaActividad());
            if (!comprobarDisponibilidadInmersion(inmersion, reserva.getNumParticipantes())) {
                throw new RuntimeException("No hay plazas disponibles");
            }
            servicio = inmersion;
        } else {
            throw new RuntimeException("La reserva no tiene ningun servicio asociado");
        }

        // buscamos a un instructor que no tenga curro ese dia y se lo asignamos
        Instructor instructor = asignarInstructorDisponible(reserva.getFechaActividad());
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
        reserva.setPrecioTotal(calcularPrecio(servicio, reserva.getNumParticipantes()));
        reserva.setEstado("ACTIVA");
        reserva.setFechaReserva(LocalDate.now());

        // incrementamos plazas ocupadas segun el tipo
        if (servicio instanceof Actividad) {
            Actividad a = (Actividad) servicio;
            a.setPlazasOcupadas(a.getPlazasOcupadas() + reserva.getNumParticipantes());
            actividadRepo.save(a);
        } else if (servicio instanceof Curso) {
            Curso c = (Curso) servicio;
            c.setPlazasOcupadas(c.getPlazasOcupadas() + reserva.getNumParticipantes());
            cursoRepo.save(c);
        } else if (servicio instanceof Inmersiones) {
            Inmersiones i = (Inmersiones) servicio;
            i.setPlazasOcupadas(i.getPlazasOcupadas() + reserva.getNumParticipantes());
            inmersionRepo.save(i);
        }

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

        // buscamos el servicio y la fecha de la actividad
        LocalDate fechaActividad = reserva.getFechaActividad();
        int plazasLiberadas = reserva.getNumParticipantes();

        // decrementamos plazas segun el tipo de servicio
        if (reserva.getActividad() != null) {
            Actividad actividad = reserva.getActividad();
            actividad.setPlazasOcupadas(Math.max(0, actividad.getPlazasOcupadas() - plazasLiberadas));
            actividadRepo.save(actividad);
        } else if (reserva.getCurso() != null) {
            Curso curso = reserva.getCurso();
            curso.setPlazasOcupadas(Math.max(0, curso.getPlazasOcupadas() - plazasLiberadas));
            cursoRepo.save(curso);
        } else if (reserva.getInmersion() != null) {
            Inmersiones inmersion = reserva.getInmersion();
            inmersion.setPlazasOcupadas(Math.max(0, inmersion.getPlazasOcupadas() - plazasLiberadas));
            inmersionRepo.save(inmersion);
        }

        // regla de negocio vital para que no cancelen con menos de 24 horas de margen
        if (fechaActividad.isBefore(LocalDate.now().plusDays(1))) {
            throw new FueraDePlazoException("No se puede cancelar la reserva con menos de 24h de antelacion");
        }

        // cambiamos el estado y borramos a los instructores para liberarles la agenda
        reserva.setEstado("CANCELADA");
        reserva.getInstructores().clear();

        // guardamos reserva actualizada
        reservaRepo.save(reserva);
    }

    // funcion simple que resta el maximo menos las ocupadas a ver si caben los nuevos
    public boolean comprobarDisponibilidad(Actividad actividad, int participantes) {
        return actividad.getPlazasMax() - actividad.getPlazasOcupadas() >= participantes;
    }

    // pilla a todos los instructores y filtra al primero que este libre ese dia
    public Instructor asignarInstructorDisponible(LocalDate fechaActividad) {
        List<Instructor> instructores = instructorRepo.findAll();

        return instructores.stream()
            .filter(instructor -> estaDisponible(instructor, fechaActividad))
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
    public BigDecimal calcularPrecio(Object servicio, int participantes) {
        BigDecimal precio = null;
        if (servicio instanceof Actividad) {
            precio = ((Actividad) servicio).getPrecio();
        } else if (servicio instanceof Curso) {
            precio = ((Curso) servicio).getPrecio();
        } else if (servicio instanceof Inmersiones) {
            precio = ((Inmersiones) servicio).getPrecio();
        }
        if (precio == null) {
            throw new RuntimeException("No se pudo determinar el precio del servicio");
        }
        return precio.multiply(BigDecimal.valueOf(participantes));
    }

    public boolean comprobarDisponibilidadCurso(Curso curso, int participantes) {
        return curso.getPlazasMax() - curso.getPlazasOcupadas() >= participantes;
    }

    public boolean comprobarDisponibilidadInmersion(Inmersiones inmersion, int participantes) {
        return inmersion.getPlazasMax() - inmersion.getPlazasOcupadas() >= participantes;
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
    }
}