package com.mourosub.web.service;
import com.mourosub.web.model.*;
import com.mourosub.web.repository.*;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {
    
    private final ReservaRepository reservaRepo;
    private final ActividadRepository actividadRepo;
    private final UsuarioRepository usuarioRepo;
    private final InstructorRepository instructorRepo;

    public ReservaService (ReservaRepository reservaRepo,
                           ActividadRepository actividadRepo,
                           UsuarioRepository usuarioRepo,
                           InstructorRepository instructorRepo) {
        this.reservaRepo = reservaRepo;
        this.actividadRepo = actividadRepo;
        this.usuarioRepo = usuarioRepo;
        this.instructorRepo = instructorRepo;
    }

    //Crear reserva

    public Reserva crearReserva (Reserva reserva, List<Long> usuarioIds){

        //Obtener actividad
        Actividad actividad = actividadRepo.findById(reserva.getActividad().getIdActividad())
        .orElseThrow(()-> new RuntimeException("Actividad no encontrada"));
        
        //Comprobar usuarios de la reserva
        if (!comprobarDisponibilidad(actividad, reserva.getNumParticipantes())){
            throw new RuntimeException("No hay plazas disponibles");
        }
        
        //Asignar instructores disponibles
        Instructor instructor = asignarInstructorDisponible();
        reserva.getInstructores().add(instructor);
        


     return reservaRepo.save(reserva);
    }
    
    
    //Metodos

    //Metodo de comprobar disponibilidad
    public boolean comprobarDisponibilidad (Actividad actividad, int participantes){
        return actividad.getPlazasMax() >= participantes;
    }

    //Metodo para asginar instructores

    public Instructor asignarInstructorDisponible(){
        List<Instructor> instructores = instructorRepo.findAll();
        
        if(instructores.isEmpty()){
            throw new RuntimeException ("No hay instructores disponibles");
        }
        
        return instructores.get(0);
    }

    //Metodo para calcular el precio
    public BigDecimal calcularPrecio(Actividad actividad, int participantes){
        return actividad.getPrecio().multiply(BigDecimal.valueOf(participantes));
    }

}
